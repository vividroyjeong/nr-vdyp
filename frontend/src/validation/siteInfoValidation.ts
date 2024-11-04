import { ValidationBase } from './validationBase'
import { SITE_SPECIES_VALUES, NUM_INPUT_LIMITS } from '@/constants/constants'
import { Util } from '@/utils/util'

export class SiteInfoValidation extends ValidationBase {
  validateRequiredFields(
    siteSpeciesValues: string | null,
    age: number | null,
    height: string | null,
    bha50SiteIndex: string | null,
  ): boolean {
    if (siteSpeciesValues === SITE_SPECIES_VALUES.COMPUTED) {
      return !(
        Util.isEmptyOrZero(age) ||
        Util.isEmptyOrZero(height) ||
        Util.isEmptyOrZero(bha50SiteIndex)
      )
    } else if (siteSpeciesValues === SITE_SPECIES_VALUES.SUPPLIED) {
      return !Util.isEmptyOrZero(bha50SiteIndex)
    }
    return true
  }

  validatePercentStockableAreaRange(psa: number | null): boolean {
    const numericPsa = Util.toNumber(psa)
    if (!numericPsa) return true

    return this.validateRange(
      numericPsa,
      NUM_INPUT_LIMITS.PERCENT_STOCKABLE_AREA_MIN,
      NUM_INPUT_LIMITS.PERCENT_STOCKABLE_AREA_MAX,
    )
  }

  validateAgeRange(age: number | null): boolean {
    if (!age) return true

    return this.validateRange(
      age,
      NUM_INPUT_LIMITS.AGE_MIN,
      NUM_INPUT_LIMITS.AGE_MAX,
    )
  }

  validateHeightRange(height: string | null): boolean {
    if (!height) return true

    const numericHeight = parseFloat(height)

    return this.validateRange(
      numericHeight,
      NUM_INPUT_LIMITS.HEIGHT_MIN,
      NUM_INPUT_LIMITS.HEIGHT_MAX,
    )
  }

  validateBha50SiteIndexRange(bha50SiteIndex: string | null): boolean {
    if (!bha50SiteIndex) return true

    const numericBha50 = parseFloat(bha50SiteIndex)

    return this.validateRange(
      numericBha50,
      NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MIN,
      NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MAX,
    )
  }
  validateHeight(height: string | null): boolean {
    if (!height) return true

    const decimalPlaces = NUM_INPUT_LIMITS.HEIGHT_DECIMAL_NUM
    const regex = new RegExp(`^\\d+(\\.\\d{1,${decimalPlaces}})?$`)

    return this.validateDecimalandFormat(height, regex)
  }

  validateBha50SiteIndex(bha50SiteIndex: string | null): boolean {
    if (!bha50SiteIndex) return true

    const decimalPlaces = NUM_INPUT_LIMITS.BHA50_SITE_INDEX_DECIMAL_NUM
    const regex = new RegExp(`^\\d+(\\.\\d{1,${decimalPlaces}})?$`)

    return this.validateDecimalandFormat(bha50SiteIndex, regex)
  }
}
