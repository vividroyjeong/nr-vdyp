import * as MAP from '@/constants/mappings'
import { Util } from '@/utils/util'

/**
 * Function that takes in a BEC Zone and returns whether the Zone is coastal or not
 * @param becZone BEC Zone code
 * @returns true if the BEC Zone is coastal, false if interior
 */
export function isCoastalZone(becZone: string): boolean {
  const normalizedBecZone = becZone.trim().toUpperCase()

  if (normalizedBecZone in MAP.BEC_ZONE_COASTAL_MAP) {
    return MAP.BEC_ZONE_COASTAL_MAP[normalizedBecZone]
  } else {
    console.warn(`BEC Zone ${becZone} is not recognized.`)
    return false
  }
}

/**
 * Function that validates whether the given basal area is within acceptable limits for the species.
 * It calculates the maximum allowable basal area using species-specific coefficients and height.
 * @param species - The species code to look up the basal area coefficients.
 * @param isCoastal - Boolean indicating whether the region is coastal (true) or interior (false).
 * @param basalArea - The basal area to be validated.
 * @param height - The height of the stand used in the calculation.
 * @returns true if the basal area is within the valid limit, false otherwise.
 * @example speceis:'H', coastal, ba: 50, height: 8
 */
export function validateBasalAreaLimits(
  species: string,
  isCoastal: boolean,
  basalArea: number,
  height: number,
): boolean {
  if (!(species in MAP.BA_LIMIT_COEFFICIENTS)) {
    console.warn(`Species ${species} not found in BA_LIMIT_COEFFICIENTS.`)
    return true
  }

  const speciesData =
    MAP.BA_LIMIT_COEFFICIENTS[species as keyof typeof MAP.BA_LIMIT_COEFFICIENTS]

  const region = isCoastal ? 'coastal' : 'interior'
  const coeffs = speciesData[region]

  // -999 indicates unavailable coefficient
  if (coeffs.coeff1 === -999 || coeffs.coeff2 === -999) {
    return true
  }

  // Equation constants
  const { const1, const2 } = MAP.BA_EQUATION_CONSTANTS

  // Validate the basal area against the calculated limit
  const fBALimit =
    Math.exp(coeffs.coeff2 / (height - const2)) * coeffs.coeff1 + const1

  return basalArea <= fBALimit
}

/**
 * Validates the Trees per Hectare (TPH) limits based on the basal area, TPH, height, species, and region.
 * This function calculates the likely minimum and maximum TPH values using species-specific coefficients
 * for coastal or interior regions and compares the input TPH with those limits.
 *
 * @param basalArea - The basal area in square meters per hectare.
 * @param tph - Trees per hectare.
 * @param height - The height of the trees in meters.
 * @param species - The species code to look up TPH limit coefficients.
 * @param coastal - Boolean indicating whether the region is coastal (true) or interior (false).
 * @returns A string with an error message if TPH is outside the calculated limits, or null if valid.
 *
 * @example
 * tph < tphMin: species = AC, coastal, height = 10.0, ba = 25.0, TPH = 50
 * tph > tphMax: species = AC, coastal, height = 10.0, ba = 5.0, TPH = 4000
 */
export function validateTreePerHectareLimits(
  basalArea: number,
  tph: number,
  height: number,
  species: string,
  coastal: boolean,
): string | null {
  // Handle unknown species
  if (!(species in MAP.TPH_LIMIT_COEFFICIENTS)) {
    console.warn('Unknown species')
    return null
  }

  const speciesCoefficients = MAP.TPH_LIMIT_COEFFICIENTS[
    species as keyof typeof MAP.TPH_LIMIT_COEFFICIENTS
  ] as { coastal?: any; interior?: any }

  // Set region based on whether it's Coastal or not
  const region = coastal ? 'coastal' : 'interior'

  // Check if the region data exists
  if (!speciesCoefficients[region]) {
    console.warn('No data for region')
    return null
  }

  const regionCoefficients = speciesCoefficients[region]
  const { P10, P90 } = regionCoefficients || {}

  // Verify that P10 and P90 data are present
  if (!P10 || !P90) {
    console.warn('No data for region')
    return null
  }

  // Defining constants
  const const1 = MAP.TPH_EQUATION_CONSTANTS.const1
  const const2 = MAP.TPH_EQUATION_CONSTANTS.const2
  const const3 = MAP.TPH_EQUATION_CONSTANTS.const3
  const unavailable = -999 // Handle when data is unavailable

  let tphMin: number | null = null
  let tphMax: number | null = null

  // Return null if P10, P90 values are invalid
  if (P10.a0 === unavailable || P90.a0 === unavailable) {
    console.warn('Unavailable TPH coefficients for this region')
    return null
  }

  // Minimum TPH calculation
  const dqMax =
    const1 +
    P90.a0 +
    P90.b0 * (height - const2) +
    P90.b1 * (height - const2) ** 2

  if (dqMax > 0) {
    tphMin = basalArea / (const3 * dqMax ** 2)
  }

  // Maximum TPH calculation
  const dqMin =
    -const1 +
    P10.a0 +
    P10.b0 * (height - const2) +
    P10.b1 * (height - const2) ** 2

  if (dqMin > 0) {
    tphMax = basalArea / (const3 * dqMin ** 2)
  }

  // Minimum and maximum TPH values not calculated correctly
  if (tphMin === null || tphMax === null) {
    console.warn('TPH calculation failed')
    return null
  }

  if (tph < tphMin) {
    return 'Trees/ha is less than a likely minimum for entered height. Do you wish to proceed?'
  }

  if (tph > tphMax) {
    return 'Trees/ha is above a likely maximum for entered height. Do you wish to proceed?'
  }

  // Return null if in range (valid value)
  return null
}

/**
 * Validates the quadratic mean diameter based on the basal area, trees per hectare (TPH), and minimum DBH limit.
 * If the calculated quadratic diameter is less than the required minimum DBH limit, an error message is returned.
 *
 * @param {number} basalArea - The basal area in m²/ha.
 * @param {number} tph - Trees per hectare.
 * @param {string | null} minDBHLimit - The minimum DBH limit in cm, provided as a string.
 * @returns {string | null} - Returns an error message if the quadratic diameter is less than the minimum limit, otherwise null.
 *
 * @example
 * basalArea: 4, tph: 1000, minDBHLimit: 7.5 cm+
 */
export function validateQuadraticDiameter(
  basalArea: number,
  tph: number,
  minDBHLimit: string | null,
): string | null {
  if (!minDBHLimit) {
    console.warn('Unknown minDBHLimit')
    return null
  }

  let diam = 0
  if (tph > 0) {
    diam = Math.sqrt(basalArea / tph / 0.00007854)
  }

  if (diam < Util.extractNumeric(minDBHLimit)) {
    return `Quadratic Mean Diameter of ${diam.toFixed(1)} cm is less than the required diameter of: ${minDBHLimit}\nModify one or both of Basal Area or Trees per Hectare.`
  }

  // Return null if in range (valid value)
  return null
}
