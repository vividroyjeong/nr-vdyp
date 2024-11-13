<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.addtStandAttrs">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.addtStandAttrs === PANEL.OPEN
                  ? 'mdi-chevron-up'
                  : 'mdi-chevron-down'
              }}</v-icon>
            </v-col>
            <v-col>
              <span class="text-h6">Additional Stand Attributes</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <v-form ref="form">
            <div class="mb-2" style="font-size: 14px">
              {{ MDL_PRM_INPUT_HINT.ATTR_REQ_AGE_BSL_AREA }}
            </div>
            <div>
              <v-row>
                <v-col cols="auto">
                  <v-radio-group
                    v-model="computedValues"
                    density="compact"
                    dense
                    :disabled="isComputedValuesDisabled || !isConfirmEnabled"
                  >
                    <v-radio
                      v-for="option in addtStandAttrsOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    ></v-radio>
                  </v-radio-group>
                </v-col>
              </v-row>
            </div>
            <div>
              <v-row>
                <v-col cols="5">
                  <v-text-field
                    label="Lorey Height - 7.5cm+ (meters)"
                    type="text"
                    v-model.number="loreyHeight"
                    persistent-placeholder
                    :placeholder="loreyHeightPlaceholder"
                    density="compact"
                    dense
                    :disabled="isLoreyHeightDisabled || !isConfirmEnabled"
                  ></v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="wholeStemVol75"
                    persistent-placeholder
                    :placeholder="wholeStemVol75Placeholder"
                    density="compact"
                    dense
                    :disabled="isWholeStemVol75Disabled || !isConfirmEnabled"
                  >
                    <template v-slot:label>
                      Whole Stem Volume - 7.5cm+ (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="basalArea125"
                    persistent-placeholder
                    :placeholder="basalArea125Placeholder"
                    density="compact"
                    dense
                    :disabled="isBasalArea125Disabled || !isConfirmEnabled"
                  >
                    <template v-slot:label>
                      Basal Area - 12.5cm+ (m<sup>2</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="wholeStemVol125"
                    persistent-placeholder
                    :placeholder="wholeStemVol125Placeholder"
                    density="compact"
                    dense
                    :disabled="isWholeStemVol125Disabled || !isConfirmEnabled"
                  >
                    <template v-slot:label>
                      Whole Stem Volume - 12.5cm+ (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="cuVol"
                    persistent-placeholder
                    :placeholder="cuVolPlaceholder"
                    density="compact"
                    dense
                    :disabled="isCuVolDisabled || !isConfirmEnabled"
                  >
                    <template v-slot:label>
                      Close Utilization Volume - 12.5cm+ (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="cuNetDecayVol"
                    persistent-placeholder
                    :placeholder="cuNetDecayVolPlaceholder"
                    density="compact"
                    dense
                    :disabled="isCuNetDecayVolDisabled || !isConfirmEnabled"
                  >
                    <template v-slot:label>
                      Close Utilization Net Decay Volume - 12.5cm+
                      (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="5">
                  <v-text-field
                    type="text"
                    v-model.number="cuNetDecayWasteVol"
                    persistent-placeholder
                    :placeholder="cuNetDecayWasteVolPlaceholder"
                    density="compact"
                    dense
                    :disabled="
                      isCuNetDecayWasteVolDisabled || !isConfirmEnabled
                    "
                  >
                    <template v-slot:label>
                      Close Utilization Net Decay Waste Volume - 12.5cm+
                      (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
              </v-row>
            </div>
            <v-card-actions class="mt-5 pr-0">
              <v-spacer></v-spacer>
              <v-btn
                class="white-btn"
                :disabled="!isConfirmEnabled"
                @click="clear"
                >Clear</v-btn
              >
              <v-btn
                v-show="!isConfirmed"
                class="blue-btn ml-2"
                :disabled="!isConfirmEnabled"
                @click="onConfirm"
                >Confirm</v-btn
              >
              <v-btn v-show="isConfirmed" class="blue-btn ml-2" @click="onEdit"
                >Edit</v-btn
              >
            </v-card-actions>
          </v-form>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-card>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Util } from '@/utils/util'
import { useModelParameterStore } from '@/stores/modelParameterStore'
import { useMessageDialogStore } from '@/stores/common/messageDialogStore'
import { storeToRefs } from 'pinia'
import { addtStandAttrsOptions } from '@/constants/options'
import {
  PANEL,
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  COMPUTED_VALUES,
  SPECIAL_INDICATORS,
  MODEL_PARAMETER_PANEL,
  NUM_INPUT_LIMITS,
} from '@/constants/constants'
import { DEFAULT_VALUES } from '@/constants/defaults'
import {
  MDL_PRM_INPUT_ERR,
  MSG_DIALOG_TITLE,
  MDL_PRM_INPUT_HINT,
} from '@/constants/message'
import { AddtStandAttrsValidation } from '@/validation/addtStandAttrsValidation'

const form = ref<HTMLFormElement>()

const addtStandAttrsValidator = new AddtStandAttrsValidation()

const modelParameterStore = useModelParameterStore()
const messageDialogStore = useMessageDialogStore()

const {
  panelOpenStates,
  derivedBy,
  siteSpeciesValues,
  computedValues,
  age,
  basalArea,
  loreyHeight,
  basalArea125,
  cuVol,
  cuNetDecayWasteVol,
  wholeStemVol75,
  wholeStemVol125,
  cuNetDecayVol,
} = storeToRefs(modelParameterStore)

const panelName = MODEL_PARAMETER_PANEL.ADDT_STAND_ATTRS
const isConfirmEnabled = computed(
  () => modelParameterStore.panelState[panelName].editable,
)
const isConfirmed = computed(
  () => modelParameterStore.panelState[panelName].confirmed,
)

const isComputedValuesDisabled = ref(false)
const isLoreyHeightDisabled = ref(false)
const isWholeStemVol75Disabled = ref(false)
const isBasalArea125Disabled = ref(false)
const isWholeStemVol125Disabled = ref(false)
const isCuVolDisabled = ref(false)
const isCuNetDecayVolDisabled = ref(false)
const isCuNetDecayWasteVolDisabled = ref(false)

const loreyHeightPlaceholder = ref('')
const wholeStemVol75Placeholder = ref('')
const basalArea125Placeholder = ref('')
const wholeStemVol125Placeholder = ref('')
const cuVolPlaceholder = ref('')
const cuNetDecayVolPlaceholder = ref('')
const cuNetDecayWasteVolPlaceholder = ref('')

const loreyHeightOriginal = ref<string | null>(null)
const wholeStemVol75Original = ref<string | null>(null)
const basalArea125Original = ref<string | null>(null)
const wholeStemVol125Original = ref<string | null>(null)
const cuVolOriginal = ref<string | null>(null)
const cuNetDecayVolOriginal = ref<string | null>(null)
const cuNetDecayWasteVolOriginal = ref<string | null>(null)

const updateComputedValuesState = (
  newDerivedBy: string | null,
  newSiteSpeciesValues: string | null,
) => {
  isComputedValuesDisabled.value = !(
    newDerivedBy === DERIVED_BY.BASAL_AREA &&
    newSiteSpeciesValues === SITE_SPECIES_VALUES.COMPUTED
  )

  if (isComputedValuesDisabled.value) {
    computedValues.value = COMPUTED_VALUES.USE
  }
}

const updateFieldDisabledStates = (newComputedValues: string | null) => {
  const isDisabled = newComputedValues === COMPUTED_VALUES.USE

  isLoreyHeightDisabled.value = isDisabled
  isWholeStemVol75Disabled.value = isDisabled
  isBasalArea125Disabled.value = isDisabled
  isWholeStemVol125Disabled.value = isDisabled
  isCuVolDisabled.value = isDisabled
  isCuNetDecayVolDisabled.value = isDisabled
  isCuNetDecayWasteVolDisabled.value = isDisabled
}

const updateFieldPlaceholderStates = (newAge: number | null) => {
  // TODO - Make sure that all fields are changed to not available indicator by Age.
  if (Util.isEmptyOrZero(newAge)) {
    loreyHeightPlaceholder.value = SPECIAL_INDICATORS.NA
    wholeStemVol75Placeholder.value = SPECIAL_INDICATORS.NA
    basalArea125Placeholder.value = SPECIAL_INDICATORS.NA
    wholeStemVol125Placeholder.value = SPECIAL_INDICATORS.NA
    cuVolPlaceholder.value = SPECIAL_INDICATORS.NA
    cuNetDecayVolPlaceholder.value = SPECIAL_INDICATORS.NA
    cuNetDecayWasteVolPlaceholder.value = SPECIAL_INDICATORS.NA
  } else {
    loreyHeightPlaceholder.value = ''
    wholeStemVol75Placeholder.value = ''
    basalArea125Placeholder.value = ''
    wholeStemVol125Placeholder.value = ''
    cuVolPlaceholder.value = ''
    cuNetDecayVolPlaceholder.value = ''
    cuNetDecayWasteVolPlaceholder.value = ''
  }
}

const updateFieldValueStates = (newAge: number | null) => {
  // TODO - Make sure that all fields are changed to not available by Age.
  if (Util.isEmptyOrZero(newAge)) {
    loreyHeight.value = null
    wholeStemVol75.value = null
    basalArea125.value = null
    wholeStemVol125.value = null
    cuVol.value = null
    cuNetDecayVol.value = null
    cuNetDecayWasteVol.value = null
  }
}

const updateOriginalValues = () => {
  if (computedValues.value === COMPUTED_VALUES.MODIFY) {
    loreyHeightOriginal.value = loreyHeight.value
    wholeStemVol75Original.value = wholeStemVol75.value
    basalArea125Original.value = basalArea125.value
    wholeStemVol125Original.value = wholeStemVol125.value
    cuVolOriginal.value = cuVol.value
    cuNetDecayVolOriginal.value = cuNetDecayVol.value
    cuNetDecayWasteVolOriginal.value = cuNetDecayWasteVol.value
  }
}

const handleStateChanges = (
  newDerivedBy: string | null,
  newSiteSpeciesValues: string | null,
  newComputedValues: string | null,
  newAge: number | null,
) => {
  updateComputedValuesState(newDerivedBy, newSiteSpeciesValues)
  updateFieldDisabledStates(newComputedValues)
  updateFieldValueStates(newAge)
  updateFieldPlaceholderStates(newAge)
  updateOriginalValues()
}

watch(
  [derivedBy, siteSpeciesValues, computedValues, age],
  ([newDerivedBy, newSiteSpeciesValues, newComputedValues, newAge]) => {
    handleStateChanges(
      newDerivedBy,
      newSiteSpeciesValues,
      newComputedValues,
      newAge,
    )
  },
  { immediate: true },
)

const clear = () => {
  if (form.value) {
    form.value.reset()
  }

  computedValues.value = DEFAULT_VALUES.COMPUTED_VALUES

  // TODO - set all text-fields on this screen with calculated values based on seleciton in the previous screen
}

const validateAllFields = (): boolean => {
  const fieldsToValidate = [
    { value: loreyHeight.value, name: 'Lorey Height - 7.5cm+' },
    { value: wholeStemVol75.value, name: 'Whole Stem Volume - 7.5cm+' },
    { value: basalArea125.value, name: 'Basal Area - 12.5cm+' },
    { value: wholeStemVol125.value, name: 'Whole Stem Volume - 12.5cm+' },
    { value: cuVol.value, name: 'Close Utilization Volume - 12.5cm+' },
    {
      value: cuNetDecayVol.value,
      name: 'Close Utilization Net Decay Volume - 12.5cm+',
    },
    {
      value: cuNetDecayWasteVol.value,
      name: 'Close Utilization Net Decay Waste Volume - 12.5cm+',
    },
  ]

  if (!addtStandAttrsValidator.validateAllFields(fieldsToValidate)) {
    for (const field of fieldsToValidate) {
      if (!addtStandAttrsValidator.validateFieldPresence(field.value)) {
        messageDialogStore.openDialog(
          MSG_DIALOG_TITLE.INVALID_INPUT,
          MDL_PRM_INPUT_ERR.ATTR_VLD_FLDS(field.name),
          { width: 400 },
        )
        return false
      }
    }
  }

  return true
}

const validateComputedValuesModification = (): boolean => {
  const fields = [
    { original: loreyHeightOriginal, current: loreyHeight.value },
    { original: wholeStemVol75Original, current: wholeStemVol75.value },
    { original: basalArea125Original, current: basalArea125.value },
    { original: wholeStemVol125Original, current: wholeStemVol125.value },
    { original: cuVolOriginal, current: cuVol.value },
    { original: cuNetDecayVolOriginal, current: cuNetDecayVol.value },
    { original: cuNetDecayWasteVolOriginal, current: cuNetDecayWasteVol.value },
  ]

  const isModified = addtStandAttrsValidator.validateComputedValuesModification(
    computedValues.value,
    fields,
  )

  if (!isModified) {
    messageDialogStore.openDialog(
      MSG_DIALOG_TITLE.NO_MODIFY,
      MDL_PRM_INPUT_ERR.ATTR_VLD_NO_MODIFY,
      { width: 400 },
    )
    return false
  }

  return true
}

const validateComparison = (): boolean => {
  const errorMessage = addtStandAttrsValidator.validateComparison(
    basalArea125.value,
    basalArea.value,
    wholeStemVol125.value,
    wholeStemVol75.value,
    cuVol.value,
    cuNetDecayVol.value,
    cuNetDecayWasteVol.value,
  )

  if (errorMessage) {
    messageDialogStore.openDialog(
      MSG_DIALOG_TITLE.INVALID_INPUT,
      errorMessage,
      { width: 400 },
    )
    return false
  }

  return true
}

const validateRange = (): boolean => {
  const errorMessage = addtStandAttrsValidator.validateValueRange(
    loreyHeight.value,
    wholeStemVol75.value,
    basalArea125.value,
    wholeStemVol125.value,
    cuVol.value,
    cuNetDecayVol.value,
    cuNetDecayWasteVol.value,
  )

  if (errorMessage) {
    messageDialogStore.openDialog(
      MSG_DIALOG_TITLE.INVALID_INPUT,
      errorMessage,
      { width: 400 },
    )
    return false
  }

  return true
}

const formattingValues = (): void => {
  if (loreyHeight.value) {
    loreyHeight.value = parseFloat(loreyHeight.value).toFixed(
      NUM_INPUT_LIMITS.LOREY_HEIGHT_DECIMAL_NUM,
    )
  }

  if (wholeStemVol75.value) {
    wholeStemVol75.value = parseFloat(wholeStemVol75.value).toFixed(
      NUM_INPUT_LIMITS.WHOLE_STEM_VOL75_DECIMAL_NUM,
    )
  }

  if (basalArea125.value) {
    basalArea125.value = parseFloat(basalArea125.value).toFixed(
      NUM_INPUT_LIMITS.BASAL_AREA125_DECIMAL_NUM,
    )
  }

  if (wholeStemVol125.value) {
    wholeStemVol125.value = parseFloat(wholeStemVol125.value).toFixed(
      NUM_INPUT_LIMITS.WHOLE_STEM_VOL125_DECIMAL_NUM,
    )
  }

  if (cuVol.value) {
    cuVol.value = parseFloat(cuVol.value).toFixed(
      NUM_INPUT_LIMITS.CU_VOL_DECIMAL_NUM,
    )
  }

  if (cuNetDecayVol.value) {
    cuNetDecayVol.value = parseFloat(cuNetDecayVol.value).toFixed(
      NUM_INPUT_LIMITS.CU_NET_DECAY_DECIMAL_NUM,
    )
  }

  if (cuNetDecayWasteVol.value) {
    cuNetDecayWasteVol.value = parseFloat(cuNetDecayWasteVol.value).toFixed(
      NUM_INPUT_LIMITS.CU_NET_DECAY_WASTE_DECIMAL_NUM,
    )
  }
}

const onConfirm = () => {
  if (
    validateAllFields() &&
    validateComputedValuesModification() &&
    validateComparison() &&
    validateRange()
  ) {
    if (form.value) {
      form.value.validate()
    }

    formattingValues()

    // this panel is not in a confirmed state
    if (!isConfirmed.value) {
      modelParameterStore.confirmPanel(panelName)
    }
  }
}

const onEdit = () => {
  // this panel has already been confirmed.
  if (isConfirmed.value) {
    modelParameterStore.editPanel(panelName)
  }
}
</script>
<style scoped></style>
