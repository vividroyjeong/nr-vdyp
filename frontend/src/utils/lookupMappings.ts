import * as MAP from '@/constants/mappings'

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
 * e.g. speceis:'H', coastal, ba: 50, height: 8
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
  const { const1, const2 } = MAP.EQUATION_CONSTANTS

  // Validate the basal area against the calculated limit
  const fBALimit =
    Math.exp(coeffs.coeff2 / (height - const2)) * coeffs.coeff1 + const1

  return basalArea <= fBALimit
}
