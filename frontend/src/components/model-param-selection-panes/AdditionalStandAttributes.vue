<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.additionalStandAttributes">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.additionalStandAttributes === PANEL.OPEN
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
              These additional Stand attributes require that a Stand Age and
              Basal Area be supplied on the Site Index and the Density pages
            </div>
            <div>
              <v-row>
                <v-col cols="auto">
                  <v-radio-group
                    v-model="computedValues"
                    density="compact"
                    dense
                    :disabled="isComputedValuesDisabled"
                  >
                    <v-radio
                      v-for="option in additionalStandAttributesOptions"
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
                    type="number"
                    v-model="loreyHeight"
                    min="0.01"
                    max="99.90"
                    step="0.01"
                    persistent-placeholder
                    :placeholder="loreyHeightPlaceholder"
                    density="compact"
                    dense
                    :disabled="isLoreyHeightDisabled"
                  ></v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="number"
                    v-model="wholeStemVolume75cm"
                    min="0.1"
                    max="2500.0"
                    step="0.1"
                    persistent-placeholder
                    :placeholder="wholeStemVolume75cmPlaceholder"
                    density="compact"
                    dense
                    :disabled="isWholeStemVolume75cmDisabled"
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
                    type="number"
                    v-model="basalArea125cm"
                    min="0.1000"
                    max="250.0000"
                    step="0.0001"
                    persistent-placeholder
                    :placeholder="basalArea125cmPlaceholder"
                    density="compact"
                    dense
                    :disabled="isBasalArea125cmDisabled"
                  >
                    <template v-slot:label>
                      Basal Area - 12.5cm+ (m<sup>2</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="number"
                    v-model="wholeStemVolume125cm"
                    min="0.0"
                    max="2500.0"
                    step="0.1"
                    persistent-placeholder
                    :placeholder="wholeStemVolume125cmPlaceholder"
                    density="compact"
                    dense
                    :disabled="isWholeStemVolume125cmDisabled"
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
                    type="number"
                    v-model="closeUtilVolume"
                    min="0.0"
                    max="2500.0"
                    step="0.1"
                    persistent-placeholder
                    :placeholder="closeUtilVolumePlaceholder"
                    density="compact"
                    dense
                    :disabled="isCloseUtilVolumeDisabled"
                  >
                    <template v-slot:label>
                      Close Utilization Volume - 12.5cm+ (m<sup>3</sup>/ha)
                    </template>
                  </v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5">
                  <v-text-field
                    type="number"
                    v-model="closeUtilNetDecayVolume"
                    min="0.0"
                    max="2500.0"
                    step="0.1"
                    persistent-placeholder
                    :placeholder="closeUtilNetDecayVolumePlaceholder"
                    density="compact"
                    dense
                    :disabled="isCloseUtilNetDecayVolumeDisabled"
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
                    type="number"
                    v-model="closeUtilNetDecayWasteVolume"
                    min="0.0"
                    max="2500.0"
                    step="0.1"
                    persistent-placeholder
                    :placeholder="closeUtilNetDecayWasteVolumePlaceholder"
                    density="compact"
                    dense
                    :disabled="isCloseUtilNetDecayWasteVolumeDisabled"
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
import { additionalStandAttributesOptions } from '@/constants/options'
import {
  PANEL,
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  COMPUTED_VALUES,
  DEFAULT_VALUES,
  NOT_AVAILABLE_INDI,
} from '@/constants/constants'

const form = ref<HTMLFormElement>()

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
  basalArea125cm,
  closeUtilVolume,
  closeUtilNetDecayWasteVolume,
  wholeStemVolume75cm,
  wholeStemVolume125cm,
  closeUtilNetDecayVolume,
} = storeToRefs(modelParameterStore)

const panelName = 'additionalStandAttributes'
const isConfirmEnabled = computed(
  () => modelParameterStore.panelState[panelName].editable,
)
const isConfirmed = computed(
  () => modelParameterStore.panelState[panelName].confirmed,
)

const isComputedValuesDisabled = ref(false)
const isLoreyHeightDisabled = ref(false)
const isWholeStemVolume75cmDisabled = ref(false)
const isBasalArea125cmDisabled = ref(false)
const isWholeStemVolume125cmDisabled = ref(false)
const isCloseUtilVolumeDisabled = ref(false)
const isCloseUtilNetDecayVolumeDisabled = ref(false)
const isCloseUtilNetDecayWasteVolumeDisabled = ref(false)

const loreyHeightPlaceholder = ref('')
const wholeStemVolume75cmPlaceholder = ref('')
const basalArea125cmPlaceholder = ref('')
const wholeStemVolume125cmPlaceholder = ref('')
const closeUtilVolumePlaceholder = ref('')
const closeUtilNetDecayVolumePlaceholder = ref('')
const closeUtilNetDecayWasteVolumePlaceholder = ref('')

const loreyHeightOriginal = ref<number | null>(null)
const wholeStemVolume75cmOriginal = ref<number | null>(null)
const basalArea125cmOriginal = ref<number | null>(null)
const wholeStemVolume125cmOriginal = ref<number | null>(null)
const closeUtilVolumeOriginal = ref<number | null>(null)
const closeUtilNetDecayVolumeOriginal = ref<number | null>(null)
const closeUtilNetDecayWasteVolumeOriginal = ref<number | null>(null)

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
  isWholeStemVolume75cmDisabled.value = isDisabled
  isBasalArea125cmDisabled.value = isDisabled
  isWholeStemVolume125cmDisabled.value = isDisabled
  isCloseUtilVolumeDisabled.value = isDisabled
  isCloseUtilNetDecayVolumeDisabled.value = isDisabled
  isCloseUtilNetDecayWasteVolumeDisabled.value = isDisabled
}

const updateFieldPlaceholderStates = (newAge: number | null) => {
  // TODO - Make sure that all fields are changed to not available indicator by Age.
  if (Util.isEmptyOrZero(newAge)) {
    loreyHeightPlaceholder.value = NOT_AVAILABLE_INDI.NA
    wholeStemVolume75cmPlaceholder.value = NOT_AVAILABLE_INDI.NA
    basalArea125cmPlaceholder.value = NOT_AVAILABLE_INDI.NA
    wholeStemVolume125cmPlaceholder.value = NOT_AVAILABLE_INDI.NA
    closeUtilVolumePlaceholder.value = NOT_AVAILABLE_INDI.NA
    closeUtilNetDecayVolumePlaceholder.value = NOT_AVAILABLE_INDI.NA
    closeUtilNetDecayWasteVolumePlaceholder.value = NOT_AVAILABLE_INDI.NA
  } else {
    loreyHeightPlaceholder.value = ''
    wholeStemVolume75cmPlaceholder.value = ''
    basalArea125cmPlaceholder.value = ''
    wholeStemVolume125cmPlaceholder.value = ''
    closeUtilVolumePlaceholder.value = ''
    closeUtilNetDecayVolumePlaceholder.value = ''
    closeUtilNetDecayWasteVolumePlaceholder.value = ''
  }
}

const updateFieldValueStates = (newAge: number | null) => {
  // TODO - Make sure that all fields are changed to not available by Age.
  if (Util.isEmptyOrZero(newAge)) {
    loreyHeight.value = null
    wholeStemVolume75cm.value = null
    basalArea125cm.value = null
    wholeStemVolume125cm.value = null
    closeUtilVolume.value = null
    closeUtilNetDecayVolume.value = null
    closeUtilNetDecayWasteVolume.value = null
  }
}

const updateOriginalValues = () => {
  if (computedValues.value === COMPUTED_VALUES.MODIFY) {
    loreyHeightOriginal.value = loreyHeight.value
    wholeStemVolume75cmOriginal.value = wholeStemVolume75cm.value
    basalArea125cmOriginal.value = basalArea125cm.value
    wholeStemVolume125cmOriginal.value = wholeStemVolume125cm.value
    closeUtilVolumeOriginal.value = closeUtilVolume.value
    closeUtilNetDecayVolumeOriginal.value = closeUtilNetDecayVolume.value
    closeUtilNetDecayWasteVolumeOriginal.value =
      closeUtilNetDecayWasteVolume.value
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

const validateFieldPresenceAndValue = (
  fieldValue: number | null,
  fieldName: string,
): boolean => {
  if (Util.isBlank(fieldValue)) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      `${fieldName}: is not a valid number`,
      { width: 400 },
    )
    return false
  }

  if (Util.isZeroValue(fieldValue)) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      `${fieldName}: must be greater than 0.0`,
      { width: 450 },
    )
    return false
  }

  return true
}

const validateAllFields = (): boolean => {
  const fieldsToValidate = [
    { value: loreyHeight.value, name: 'Lorey Height - 7.5cm+' },
    { value: wholeStemVolume75cm.value, name: 'Whole Stem Volume - 7.5cm+' },
    { value: basalArea125cm.value, name: 'Basal Area - 12.5cm+' },
    { value: wholeStemVolume125cm.value, name: 'Whole Stem Volume - 12.5cm+' },
    {
      value: closeUtilVolume.value,
      name: 'Close Utilization Volume - 12.5cm+',
    },
    {
      value: closeUtilNetDecayVolume.value,
      name: 'Close Utilization Net Decay Volume - 12.5cm+',
    },
    {
      value: closeUtilNetDecayWasteVolume.value,
      name: 'Close Utilization Net Decay Waste Volume - 12.5cm+',
    },
  ]

  for (const field of fieldsToValidate) {
    if (!validateFieldPresenceAndValue(field.value, field.name)) {
      return false
    }
  }

  return true
}

const validateComputedValuesModification = (): boolean => {
  if (computedValues.value === COMPUTED_VALUES.MODIFY) {
    const fields = [
      { original: loreyHeightOriginal, current: loreyHeight.value },
      {
        original: wholeStemVolume75cmOriginal,
        current: wholeStemVolume75cm.value,
      },
      { original: basalArea125cmOriginal, current: basalArea125cm.value },
      {
        original: wholeStemVolume125cmOriginal,
        current: wholeStemVolume125cm.value,
      },
      { original: closeUtilVolumeOriginal, current: closeUtilVolume.value },
      {
        original: closeUtilNetDecayVolumeOriginal,
        current: closeUtilNetDecayVolume.value,
      },
      {
        original: closeUtilNetDecayWasteVolumeOriginal,
        current: closeUtilNetDecayWasteVolume.value,
      },
    ]

    // Returns true if any element satisfies the condition, otherwise returns false.
    const hasModification = fields.some((field) => {
      return field.original.value !== field.current
    })

    if (!hasModification) {
      messageDialogStore.openDialog(
        'No Modifications!',
        "At least one of the starting values must have been modified from the original computed values.\n\n Please modify at least one starting value or switch to 'Computed Values' mode.",
        { width: 400 },
      )
      return false
    }
  }
  return true
}

// Validation by comparing entered values
const validateComparison = (): boolean => {
  if (
    basalArea125cm.value !== null &&
    basalArea.value !== null &&
    basalArea125cm.value > basalArea.value
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      `'Basal Area - 12.5cm+' is greater than the Basal Area defined on the Stand Density Pane.\n\n 'Basal Area - 7.5cm+' on Stand Density Pane: ${basalArea.value}`,
      { width: 400 },
    )
    return false
  }

  if (
    wholeStemVolume125cm.value !== null &&
    wholeStemVolume75cm.value !== null &&
    wholeStemVolume125cm.value > wholeStemVolume75cm.value
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Whole Stem Volume - 12.5cm+': is greater than 'Whole Stem Volume - 7.5cm+'",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilVolume.value !== null &&
    wholeStemVolume125cm.value !== null &&
    closeUtilVolume.value > wholeStemVolume125cm.value
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Volume - 12.5cm+': is greater than 'Whole Stem Volume - 12.5cm+'",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilNetDecayVolume.value !== null &&
    closeUtilVolume.value !== null &&
    closeUtilNetDecayVolume.value > closeUtilVolume.value
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Net Decay Volume - 12.5cm+': is greater than 'Close Utilization Volume - 12.5cm+'",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilNetDecayWasteVolume.value !== null &&
    closeUtilNetDecayVolume.value !== null &&
    closeUtilNetDecayWasteVolume.value > closeUtilNetDecayVolume.value
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Net Decay Waste Volume - 12.5cm+': is greater than 'Close Utilization Net Decay Volume - 12.5cm+'",
      { width: 400 },
    )
    return false
  }

  return true
}

// Validation to check the range of input values
const validateRange = (): boolean => {
  if (
    loreyHeight.value !== null &&
    (loreyHeight.value < 0.01 || loreyHeight.value > 99.9)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Lorey Height - 7.5cm+': must range from 0.01 and 99.90",
      { width: 400 },
    )
    return false
  }

  if (
    wholeStemVolume75cm.value !== null &&
    (wholeStemVolume75cm.value < 0.1 || wholeStemVolume75cm.value > 2500.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Whole Stem Volume - 7.5cm+': must range from 0.1 and 2500.0",
      { width: 400 },
    )
    return false
  }

  if (
    basalArea125cm.value !== null &&
    (basalArea125cm.value < 0.1 || basalArea125cm.value > 250.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Basal Area - 12.5cm+': must range from 0.1000 and 250.0000",
      { width: 400 },
    )
    return false
  }

  if (
    wholeStemVolume125cm.value !== null &&
    (wholeStemVolume125cm.value < 0.0 || wholeStemVolume125cm.value > 2500.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Whole Stem Volume - 12.5cm+': must range from 0.0 and 2500.0",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilVolume.value !== null &&
    (closeUtilVolume.value < 0.0 || closeUtilVolume.value > 2500.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Volume - 12.5cm+': must range from 0.0 and 2500.0",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilNetDecayVolume.value !== null &&
    (closeUtilNetDecayVolume.value < 0.0 ||
      closeUtilNetDecayVolume.value > 2500.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Net Decay Volume - 12.5cm+': must range from 0.0 and 2500.0",
      { width: 400 },
    )
    return false
  }

  if (
    closeUtilNetDecayWasteVolume.value !== null &&
    (closeUtilNetDecayWasteVolume.value < 0.0 ||
      closeUtilNetDecayWasteVolume.value > 2500.0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Close Utilization Net Decay Waste Volume - 12.5cm+': must range from 0.0 and 2500.0",
      { width: 400 },
    )
    return false
  }

  return true
}

const onConfirm = () => {
  const isAllFieldsValid = validateAllFields()
  const isModificationValid = validateComputedValuesModification()
  const isComparisonValid = validateComparison()
  const isRangeValid = validateRange()

  if (
    isAllFieldsValid &&
    isModificationValid &&
    isComparisonValid &&
    isRangeValid
  ) {
    form.value?.validate()
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
