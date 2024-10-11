<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.siteInfo">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.siteInfo === PANEL.OPEN
                  ? 'mdi-chevron-up'
                  : 'mdi-chevron-down'
              }}</v-icon>
            </v-col>
            <v-col>
              <span class="text-h6">Site Information</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <v-form ref="form">
            <div>
              <v-row>
                <v-col cols="6">
                  <v-row class="mb-3">
                    <v-col cols="6">
                      <v-select
                        label="BEC Zone"
                        :items="becZoneOptions"
                        v-model="becZone"
                        item-title="label"
                        item-value="value"
                        hide-details="auto"
                        persistent-placeholder
                        placeholder="Select Bec Zone"
                        density="compact"
                        dense
                        :disabled="!isConfirmEnabled"
                      ></v-select>
                    </v-col>
                    <v-col class="col-space-6" />
                    <v-col>
                      <v-select
                        label="Eco Zone"
                        :items="ecoZoneOptions"
                        v-model="ecoZone"
                        item-title="label"
                        item-value="value"
                        clearable
                        hide-details="auto"
                        persistent-placeholder
                        placeholder="Select Eco Zone"
                        density="compact"
                        dense
                        :disabled="!isConfirmEnabled"
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col class="col-space-6" />
                <v-col>
                  <v-row>
                    <v-col cols="12">
                      <v-checkbox
                        label="Include Secondary Dominant Height in Yield Table"
                        v-model="incSecondaryHeight"
                        hide-details="auto"
                        :disabled="
                          isIncSecondaryHeightDisabled || !isConfirmEnabled
                        "
                      ></v-checkbox>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
              <div class="hr-line"></div>
              <v-row class="mt-7">
                <v-col cols="6">
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Site Species"
                        :items="siteSpeciesOptions"
                        v-model="selectedSiteSpecies"
                        item-title="label"
                        item-value="value"
                        hide-details="auto"
                        persistent-placeholder
                        placeholder="Select..."
                        density="compact"
                        dense
                        :disabled="
                          isSelectedSiteSpeciesDisabled || !isConfirmEnabled
                        "
                      ></v-select>
                    </v-col>
                    <v-col class="col-space-6" />
                    <v-col>
                      <v-select
                        label="Site Index Curve"
                        :items="computedSpeciesOptions"
                        v-model="siteIndexCurve"
                        item-title="label"
                        item-value="value"
                        hide-details="auto"
                        persistent-placeholder
                        placeholder="Select..."
                        density="compact"
                        dense
                        disabled
                      ></v-select>
                      <v-label style="font-size: 12px"
                        >*Ministry Default Curve for this Species
                      </v-label>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
              <div class="hr-line"></div>
              <v-row
                class="mt-1"
                style="display: inline-flex; align-items: center"
              >
                <v-col cols="auto" style="margin-bottom: 20px">
                  <div class="mt-2">Site Species Values:</div>
                </v-col>
                <v-col cols="auto">
                  <v-radio-group
                    v-model="siteSpeciesValues"
                    inline
                    :disabled="isSiteSpeciesValueDisabled || !isConfirmEnabled"
                  >
                    <v-radio
                      v-for="option in siteSpeciesValuesOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    ></v-radio>
                  </v-radio-group>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="6">
                  <v-row style="height: 70px !important">
                    <v-col cols="6">
                      <v-select
                        label="Age Type"
                        :items="ageTypeOptions"
                        v-model="ageType"
                        item-title="label"
                        item-value="value"
                        hide-details
                        persistent-placeholder
                        placeholder=""
                        density="compact"
                        dense
                        :disabled="isAgeTypeDisabled || !isConfirmEnabled"
                      ></v-select>
                    </v-col>
                    <v-col class="col-space-6" />
                    <v-col>
                      <v-text-field
                        label="Age (years)"
                        type="number"
                        v-model="age"
                        :max="NUM_INPUT_LIMITS.AGE_MAX"
                        :min="NUM_INPUT_LIMITS.AGE_MIN"
                        :step="NUM_INPUT_LIMITS.AGE_STEP"
                        persistent-placeholder
                        :placeholder="agePlaceholder"
                        hide-details
                        density="compact"
                        dense
                        :disabled="isAgeDisabled || !isConfirmEnabled"
                      ></v-text-field>
                      <v-label
                        v-show="Util.isZeroValue(age)"
                        style="font-size: 12px"
                        >A value of zero indicates not known.</v-label
                      >
                    </v-col>
                  </v-row>
                  <v-row style="height: 70px !important">
                    <v-col cols="6" />
                    <v-col class="col-space-6" />
                    <v-col>
                      <div style="position: relative; width: 100%">
                        <v-text-field
                          label="Height (meters)"
                          type="text"
                          v-model="height"
                          persistent-placeholder
                          :placeholder="heightPlaceholder"
                          hide-details
                          density="compact"
                          dense
                          style="padding-left: 15px"
                          variant="plain"
                          :disabled="isHeightDisabled || !isConfirmEnabled"
                        ></v-text-field>
                        <!-- spin buttons -->
                        <div class="spin-box">
                          <div
                            class="spin-up-arrow-button"
                            @mousedown="startIncrementHeight"
                            @mouseup="stopIncrementHeight"
                            @mouseleave="stopIncrementHeight"
                            :class="{
                              disabled: isHeightDisabled || !isConfirmEnabled,
                            }"
                          >
                            {{ SPIN_BUTTON.UP }}
                          </div>
                          <div
                            class="spin-down-arrow-button"
                            @mousedown="startDecrementHeight"
                            @mouseup="stopDecrementHeight"
                            @mouseleave="stopDecrementHeight"
                            :class="{
                              disabled: isHeightDisabled || !isConfirmEnabled,
                            }"
                          >
                            {{ SPIN_BUTTON.DOWN }}
                          </div>
                        </div>
                        <div class="spin-text-field-bottom-line"></div>
                      </div>
                      <v-label
                        v-show="Util.isZeroValue(height)"
                        style="font-size: 12px"
                      >
                        A value of zero indicates not known.
                      </v-label>
                    </v-col>
                  </v-row>
                  <v-row style="height: 70px !important">
                    <v-col cols="6" />
                    <v-col class="col-space-6" />
                    <v-col>
                      <div style="position: relative; width: 100%">
                        <v-text-field
                          label="BHA 50 Site Index"
                          type="text"
                          v-model="bha50SiteIndex"
                          persistent-placeholder
                          placeholder=""
                          hide-details
                          density="compact"
                          dense
                          style="padding-left: 15px"
                          variant="plain"
                          :disabled="
                            isBHA50SiteIndexDisabled || !isConfirmEnabled
                          "
                        ></v-text-field>
                        <!-- spin buttons -->
                        <div class="spin-box">
                          <div
                            class="spin-up-arrow-button"
                            @mousedown="startIncrementBHA50SiteIndex"
                            @mouseup="stopIncrementBHA50SiteIndex"
                            @mouseleave="stopIncrementBHA50SiteIndex"
                            :class="{
                              disabled:
                                isBHA50SiteIndexDisabled || !isConfirmEnabled,
                            }"
                          >
                            {{ SPIN_BUTTON.UP }}
                          </div>
                          <div
                            class="spin-down-arrow-button"
                            @mousedown="startDecrementBHA50SiteIndex"
                            @mouseup="stopDecrementBHA50SiteIndex"
                            @mouseleave="stopDecrementBHA50SiteIndex"
                            :class="{
                              disabled:
                                isBHA50SiteIndexDisabled || !isConfirmEnabled,
                            }"
                          >
                            {{ SPIN_BUTTON.DOWN }}
                          </div>
                        </div>
                        <div class="spin-text-field-bottom-line"></div>
                      </div>
                      <v-label
                        v-show="Util.isZeroValue(bha50SiteIndex)"
                        style="font-size: 12px"
                        >A value of zero indicates not known.</v-label
                      >
                    </v-col>
                  </v-row>
                </v-col>
                <v-col cols="6">
                  <div class="mt-2">
                    <v-radio-group
                      v-model="floating"
                      row
                      hide-details
                      :disabled="isFloatingDisabled || !isConfirmEnabled"
                    >
                      <v-radio
                        v-for="option in floatingOptions"
                        :key="option.value"
                        :label="option.label"
                        :value="option.value"
                        style="margin-bottom: 25px"
                      ></v-radio>
                    </v-radio-group>
                  </div>
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
import {
  becZoneOptions,
  ecoZoneOptions,
  siteSpeciesValuesOptions,
  ageTypeOptions,
  floatingOptions,
} from '@/constants/options'
import { SITE_INDEX_CURVE_MAP } from '@/constants/mappings'
import {
  PANEL,
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  FLOATING,
  NOT_AVAILABLE_INDI,
  MODEL_PARAMETER_PANEL,
  NUM_INPUT_LIMITS,
  CONTINUOUS_INC_DEC,
  SPIN_BUTTON,
} from '@/constants/constants'
import { DEFAULT_VALUES } from '@/constants/defaults'

const form = ref<HTMLFormElement>()

const modelParameterStore = useModelParameterStore()
const messageDialogStore = useMessageDialogStore()

const {
  panelOpenStates,
  derivedBy,
  speciesGroups,
  highestPercentSpecies,
  selectedSiteSpecies,
  becZone,
  ecoZone,
  incSecondaryHeight,
  siteIndexCurve,
  siteSpeciesValues,
  ageType,
  age,
  height,
  bha50SiteIndex,
  floating,
} = storeToRefs(modelParameterStore)

const panelName = MODEL_PARAMETER_PANEL.SITE_INFO
const isConfirmEnabled = computed(
  () => modelParameterStore.panelState[panelName].editable,
)
const isConfirmed = computed(
  () => modelParameterStore.panelState[panelName].confirmed,
)

const computedSpeciesOptions = computed(() =>
  (
    Object.keys(SITE_INDEX_CURVE_MAP) as Array<
      keyof typeof SITE_INDEX_CURVE_MAP
    >
  ).map((code) => ({
    label: `${SITE_INDEX_CURVE_MAP[code]}`,
    value: code,
  })),
)

const siteSpeciesOptions = computed(() =>
  speciesGroups.value.map((group) => ({
    label: group.siteSpecies,
    value: group.siteSpecies,
  })),
)

const isIncSecondaryHeightDisabled = ref(false)
const isSelectedSiteSpeciesDisabled = ref(false)
const isSiteSpeciesValueDisabled = ref(false)
const isAgeTypeDisabled = ref(false)
const isAgeDisabled = ref(false)
const isHeightDisabled = ref(false)
const isBHA50SiteIndexDisabled = ref(false)
const isFloatingDisabled = ref(false)

const agePlaceholder = ref('')
const heightPlaceholder = ref('')

// Interval references for continuous increment/decrement
let heightIncrementInterval: number | null = null
let heightDecrementInterval: number | null = null
let bha50IncrementInterval: number | null = null
let bha50DecrementInterval: number | null = null

const setFloatingState = (newFloating: string | null) => {
  isAgeTypeDisabled.value = false
  isAgeDisabled.value = false
  isHeightDisabled.value = false
  isBHA50SiteIndexDisabled.value = false
  floating.value = newFloating

  if (newFloating === FLOATING.AGE) {
    isAgeTypeDisabled.value = true
    isAgeDisabled.value = true
  } else if (newFloating === FLOATING.HEIGHT) {
    isHeightDisabled.value = true
  } else if (newFloating === FLOATING.SITEINDEX) {
    isBHA50SiteIndexDisabled.value = true
  }
}

const handleSiteSpeciesValuesState = (
  newSiteSpeciesValues: string | null,
  newFloating: string | null,
) => {
  if (newSiteSpeciesValues === SITE_SPECIES_VALUES.COMPUTED) {
    isFloatingDisabled.value = false
    setFloatingState(newFloating)

    // TODO - set values based on species, beczone, agetype
    // if age or height float is selected, age, height or bha 50 site index should also be factored into the calculation for these values
    age.value = 60
    height.value = DEFAULT_VALUES.HEIGHT

    agePlaceholder.value = ''
    heightPlaceholder.value = ''
  } else if (newSiteSpeciesValues === SITE_SPECIES_VALUES.SUPPLIED) {
    isAgeTypeDisabled.value = true
    isAgeDisabled.value = true
    isHeightDisabled.value = true
    isBHA50SiteIndexDisabled.value = false
    isFloatingDisabled.value = true

    age.value = null
    height.value = null
    agePlaceholder.value = NOT_AVAILABLE_INDI.NA
    heightPlaceholder.value = NOT_AVAILABLE_INDI.NA
  }
}

const handleDerivedByChange = (
  newDerivedBy: string | null,
  newSiteSpecies: string | null,
  newSiteSpeciesValues: string | null,
  newFloating: string | null,
) => {
  if (newDerivedBy === DERIVED_BY.VOLUME) {
    incSecondaryHeight.value = false
    isIncSecondaryHeightDisabled.value = true
    isSelectedSiteSpeciesDisabled.value = true
    handleSiteSpeciesValuesState(newSiteSpeciesValues, newFloating)
  } else if (newDerivedBy === DERIVED_BY.BASAL_AREA) {
    isIncSecondaryHeightDisabled.value = false
    isSelectedSiteSpeciesDisabled.value = false
    isSiteSpeciesValueDisabled.value =
      newSiteSpecies !== highestPercentSpecies.value
    handleSiteSpeciesValuesState(newSiteSpeciesValues, newFloating)
  }
}

// Update siteIndexCurve based on selectedSiteSpecies
const updateSiteIndexCurve = (newSiteSpecies: string | null) => {
  if (
    newSiteSpecies &&
    SITE_INDEX_CURVE_MAP[newSiteSpecies as keyof typeof SITE_INDEX_CURVE_MAP]
  ) {
    siteIndexCurve.value =
      SITE_INDEX_CURVE_MAP[newSiteSpecies as keyof typeof SITE_INDEX_CURVE_MAP]
  } else {
    siteIndexCurve.value = null // Clear if no mapping found
  }
}

watch(selectedSiteSpecies, (newSiteSpecies) => {
  updateSiteIndexCurve(newSiteSpecies)
})

watch(
  [derivedBy, selectedSiteSpecies, siteSpeciesValues, floating],
  ([newDerivedBy, newSiteSpecies, newSiteSpeciesValues, newFloating]) => {
    handleDerivedByChange(
      newDerivedBy,
      newSiteSpecies,
      newSiteSpeciesValues,
      newFloating,
    )
  },
  { immediate: true },
)

const incrementHeight = () => {
  const newValue = Util.increaseItemBySpinButton(
    height.value,
    NUM_INPUT_LIMITS.HEIGHT_MAX,
    NUM_INPUT_LIMITS.HEIGHT_MIN,
    NUM_INPUT_LIMITS.HEIGHT_STEP,
  )
  // Format the value to ##0.00
  height.value = newValue.toFixed(NUM_INPUT_LIMITS.HEIGHT_DECIMAL_NUM)
}

const decrementHeight = () => {
  let newValue = Util.decrementItemBySpinButton(
    height.value,
    NUM_INPUT_LIMITS.HEIGHT_MAX,
    NUM_INPUT_LIMITS.HEIGHT_MIN,
    NUM_INPUT_LIMITS.HEIGHT_STEP,
  )
  // Format the value to ##0.00
  height.value = newValue.toFixed(NUM_INPUT_LIMITS.HEIGHT_DECIMAL_NUM)
}

const incrementBHA50SiteIndex = () => {
  const newValue = Util.increaseItemBySpinButton(
    bha50SiteIndex.value,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MAX,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MIN,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_STEP,
  )
  // Format the value to ##0.00
  bha50SiteIndex.value = newValue.toFixed(
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_DECIMAL_NUM,
  )
}

const decrementBHA50SiteIndex = () => {
  let newValue = Util.decrementItemBySpinButton(
    bha50SiteIndex.value,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MAX,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_MIN,
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_STEP,
  )
  // Format the value to ##0.00
  bha50SiteIndex.value = newValue.toFixed(
    NUM_INPUT_LIMITS.BHA50_SITE_INDEX_DECIMAL_NUM,
  )
}

// Methods to handle continuous increment/decrement for Height
const startIncrementHeight = () => {
  incrementHeight()
  heightIncrementInterval = window.setInterval(
    incrementHeight,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopIncrementHeight = () => {
  if (heightIncrementInterval !== null) {
    clearInterval(heightIncrementInterval)
    heightIncrementInterval = null
  }
}

const startDecrementHeight = () => {
  decrementHeight()
  heightDecrementInterval = window.setInterval(
    decrementHeight,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopDecrementHeight = () => {
  if (heightDecrementInterval !== null) {
    clearInterval(heightDecrementInterval)
    heightDecrementInterval = null
  }
}

// Methods to handle continuous increment/decrement for BHA 50 Site Index
const startIncrementBHA50SiteIndex = () => {
  incrementBHA50SiteIndex()
  bha50IncrementInterval = window.setInterval(
    incrementBHA50SiteIndex,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopIncrementBHA50SiteIndex = () => {
  if (bha50IncrementInterval !== null) {
    clearInterval(bha50IncrementInterval)
    bha50IncrementInterval = null
  }
}

const startDecrementBHA50SiteIndex = () => {
  decrementBHA50SiteIndex()
  bha50DecrementInterval = window.setInterval(
    decrementBHA50SiteIndex,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopDecrementBHA50SiteIndex = () => {
  if (bha50DecrementInterval !== null) {
    clearInterval(bha50DecrementInterval)
    bha50DecrementInterval = null
  }
}

const clear = () => {
  if (form.value) {
    form.value.reset()
  }

  selectedSiteSpecies.value = highestPercentSpecies.value
  updateSiteIndexCurve(selectedSiteSpecies.value)

  becZone.value = DEFAULT_VALUES.BEC_ZONE
  siteSpeciesValues.value = DEFAULT_VALUES.SITE_SPECIES_VALUES
  ageType.value = DEFAULT_VALUES.AGE_TYPE
  floating.value = DEFAULT_VALUES.FLOATING

  handleDerivedByChange(
    derivedBy.value,
    selectedSiteSpecies.value,
    siteSpeciesValues.value,
    floating.value,
  )
}

// Validation to check the range of input values
const validateRange = (): boolean => {
  if (age.value !== null) {
    if (age.value < 0 || age.value > 500) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Stand Age' must range from 0 and 500",
        { width: 400 },
      )
      return false
    }
  }

  if (height.value !== null) {
    const numericHeight = parseFloat(height.value)
    if (isNaN(numericHeight) || numericHeight < 0 || numericHeight > 99.9) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Stand Height' must range from 0.00 and 99.90",
        { width: 400 },
      )
      return false
    }
  }

  if (height.value !== null) {
    const numericHeight = parseFloat(height.value)
    if (isNaN(numericHeight) || numericHeight < 0 || numericHeight > 60) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Site Index' must range from 0.00 and 60.00",
        { width: 400 },
      )
      return false
    }
  }

  return true
}

const validateRequiredFields = (): boolean => {
  if (siteSpeciesValues.value === SITE_SPECIES_VALUES.COMPUTED) {
    if (
      Util.isEmptyOrZero(age.value) ||
      Util.isEmptyOrZero(height.value) ||
      Util.isEmptyOrZero(bha50SiteIndex.value)
    ) {
      messageDialogStore.openDialog(
        'Missing Information',
        `The species '${selectedSiteSpecies.value}' must have Age/Height/BHA 50 Site Index values supplied.`,
        { width: 400 },
      )
      return false
    }
  } else if (siteSpeciesValues.value === SITE_SPECIES_VALUES.SUPPLIED) {
    if (Util.isEmptyOrZero(bha50SiteIndex.value)) {
      messageDialogStore.openDialog(
        'Missing Information',
        `The species '${selectedSiteSpecies.value}' must have an BHA 50 Site Index value supplied.`,
        { width: 400 },
      )
      return false
    }
  }

  return true
}

const onConfirm = () => {
  const isRequiredFieldsValid = validateRequiredFields()
  const isRangeValid = validateRange()

  if (isRequiredFieldsValid && isRangeValid) {
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
