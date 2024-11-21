import { ValidationBase } from './validationBase'
import { COMPUTED_VALUES, NUM_INPUT_LIMITS } from '@/constants/constants'
import { Util } from '@/utils/util'
import { MDL_PRM_INPUT_ERR } from '@/constants/message'

export class AddtStandAttrsValidation extends ValidationBase {
  validateFieldPresence(fieldValue: string | null): boolean {
    return !Util.isBlank(fieldValue)
  }

  validateAllFields(fields: { value: string | null }[]): boolean {
    return fields.every((field) => this.validateFieldPresence(field.value))
  }

  validateComputedValuesModification(
    computedValues: string | null,
    fields: { original: { value: string | null }; current: string | null }[],
  ): boolean {
    if (computedValues === COMPUTED_VALUES.MODIFY) {
      const hasModification = fields.some((field) => {
        return field.original.value !== field.current
      })

      return hasModification
    }
    return true
  }

  validateComparison(
    basalArea125: string | null,
    basalArea: string | null,
    wholeStemVol125: string | null,
    wholeStemVol75: string | null,
    cuVol: string | null,
    cuNetDecayVol: string | null,
    cuNetDecayWasteVol: string | null,
  ): string | null {
    if (
      basalArea125 !== null &&
      basalArea !== null &&
      parseFloat(basalArea125) > parseFloat(basalArea)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_COMP_BSL_AREA(basalArea)
    }

    if (
      wholeStemVol125 !== null &&
      wholeStemVol75 !== null &&
      wholeStemVol125 > wholeStemVol75
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_COMP_WSV
    }

    if (cuVol !== null && wholeStemVol125 !== null && cuVol > wholeStemVol125) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_COMP_CUV
    }

    if (cuNetDecayVol !== null && cuVol !== null && cuNetDecayVol > cuVol) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_COMP_CUNDV
    }

    if (
      cuNetDecayWasteVol !== null &&
      cuNetDecayVol !== null &&
      cuNetDecayWasteVol > cuNetDecayVol
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_COMP_CUNDWV
    }

    return null
  }

  validateValueRange(
    loreyHeight: string | null,
    wholeStemVol75: string | null,
    basalArea125: string | null,
    wholeStemVol125: string | null,
    cuVol: string | null,
    cuNetDecayVol: string | null,
    cuNetDecayWasteVol: string | null,
  ): string | null {
    if (
      loreyHeight !== null &&
      (parseFloat(loreyHeight) < NUM_INPUT_LIMITS.LOREY_HEIGHT_MIN ||
        parseFloat(loreyHeight) > NUM_INPUT_LIMITS.LOREY_HEIGHT_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_LRY_HEIGHT_RNG
    }

    if (
      wholeStemVol75 !== null &&
      (parseFloat(wholeStemVol75) < NUM_INPUT_LIMITS.WHOLE_STEM_VOL75_MIN ||
        parseFloat(wholeStemVol75) > NUM_INPUT_LIMITS.WHOLE_STEM_VOL75_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_WSV75_RNG
    }

    if (
      basalArea125 !== null &&
      (parseFloat(basalArea125) < NUM_INPUT_LIMITS.BASAL_AREA125_MIN ||
        parseFloat(basalArea125) > NUM_INPUT_LIMITS.BASAL_AREA125_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_BSL_AREA_RNG
    }

    if (
      wholeStemVol125 !== null &&
      (parseFloat(wholeStemVol125) < NUM_INPUT_LIMITS.WHOLE_STEM_VOL125_MIN ||
        parseFloat(wholeStemVol125) > NUM_INPUT_LIMITS.WHOLE_STEM_VOL125_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_WSV125_RNG
    }

    if (
      cuVol !== null &&
      (parseFloat(cuVol) < NUM_INPUT_LIMITS.CU_VOL_MIN ||
        parseFloat(cuVol) > NUM_INPUT_LIMITS.CU_VOL_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_CUV_RNG
    }

    if (
      cuNetDecayVol !== null &&
      (parseFloat(cuNetDecayVol) < NUM_INPUT_LIMITS.CU_NET_DECAY_VOL_MIN ||
        parseFloat(cuNetDecayVol) > NUM_INPUT_LIMITS.CU_NET_DECAY_VOL_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_CUNDV_RNG
    }

    if (
      cuNetDecayWasteVol !== null &&
      (parseFloat(cuNetDecayWasteVol) <
        NUM_INPUT_LIMITS.CU_NET_DECAY_WASTE_VOL_MIN ||
        parseFloat(cuNetDecayWasteVol) >
          NUM_INPUT_LIMITS.CU_NET_DECAY_WASTE_VOL_MAX)
    ) {
      return MDL_PRM_INPUT_ERR.ATTR_VLD_CUNDWV_RNG
    }

    return null
  }
}
