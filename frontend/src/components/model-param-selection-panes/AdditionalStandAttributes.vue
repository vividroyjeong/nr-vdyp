<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.additionalStandAttributes">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.additionalStandAttributes === 0
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
                    min="0"
                    step="0.01"
                    :rules="[validateMinimum]"
                    :error-messages="loreyHeightError"
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
                    min="0"
                    step="0.1"
                    :rules="[validateMinimum]"
                    :error-messages="wholeStemVolume75cmError"
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
                    min="0"
                    step="0.0001"
                    :rules="[validateMinimum]"
                    :error-messages="basalArea125cmError"
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
                    min="0"
                    step="0.1"
                    :rules="[validateMinimum]"
                    :error-messages="wholeStemVolume125cmError"
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
                    min="0"
                    step="0.1"
                    :rules="[validateMinimum]"
                    :error-messages="closeUtilVolumeError"
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
                    min="0"
                    step="0.1"
                    :rules="[validateMinimum]"
                    :error-messages="closeUtilNetDecayVolumeError"
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
                    min="0"
                    step="0.1"
                    :rules="[validateMinimum]"
                    :error-messages="closeUtilNetDecayWasteVolumeError"
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
              <v-btn class="white-btn" @click="clear">Clear</v-btn>
              <v-btn class="blue-btn ml-2" @click="confirm">Confirm</v-btn>
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
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  COMPUTED_VALUES,
  DEFAULT_VALUES,
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
  loreyHeight,
  basalArea125cm,
  closeUtilVolume,
  closeUtilNetDecayWasteVolume,
  wholeStemVolume75cm,
  wholeStemVolume125cm,
  closeUtilNetDecayVolume,
} = storeToRefs(modelParameterStore)

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
  // TODO - Make sure that all fields are changed to 'N/A' by Age.
  if (Util.isEmptyOrZero(newAge)) {
    loreyHeightPlaceholder.value = 'N/A'
    wholeStemVolume75cmPlaceholder.value = 'N/A'
    basalArea125cmPlaceholder.value = 'N/A'
    wholeStemVolume125cmPlaceholder.value = 'N/A'
    closeUtilVolumePlaceholder.value = 'N/A'
    closeUtilNetDecayVolumePlaceholder.value = 'N/A'
    closeUtilNetDecayWasteVolumePlaceholder.value = 'N/A'
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
  // TODO - Make sure that all fields are changed to 'N/A' by Age.
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

const validateMinimum = (value: any) => {
  if (value === null || value === '') {
    return true
  }
  if (value < 0) {
    return 'Please enter a value greater than 0'
  }
  return true
}

const loreyHeightError = computed(() => {
  const error = validateMinimum(loreyHeight.value)
  return error === true ? [] : [error]
})

const wholeStemVolume75cmError = computed(() => {
  const error = validateMinimum(wholeStemVolume75cm.value)
  return error === true ? [] : [error]
})

const basalArea125cmError = computed(() => {
  const error = validateMinimum(basalArea125cm.value)
  return error === true ? [] : [error]
})

const wholeStemVolume125cmError = computed(() => {
  const error = validateMinimum(wholeStemVolume125cm.value)
  return error === true ? [] : [error]
})

const closeUtilVolumeError = computed(() => {
  const error = validateMinimum(closeUtilVolume.value)
  return error === true ? [] : [error]
})

const closeUtilNetDecayVolumeError = computed(() => {
  const error = validateMinimum(closeUtilNetDecayVolume.value)
  return error === true ? [] : [error]
})

const closeUtilNetDecayWasteVolumeError = computed(() => {
  const error = validateMinimum(closeUtilNetDecayWasteVolume.value)
  return error === true ? [] : [error]
})

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

const confirm = () => {
  const isAllFieldsValid = validateAllFields()
  const isModificationValid = validateComputedValuesModification()

  if (isAllFieldsValid && isModificationValid) {
    form.value?.validate()
  }
}
</script>
<style scoped></style>
