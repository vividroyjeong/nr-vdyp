<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.standDensity">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.standDensity === PANEL.OPEN
                  ? 'mdi-chevron-up'
                  : 'mdi-chevron-down'
              }}</v-icon>
            </v-col>
            <v-col>
              <span class="text-h6">Stand Density</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <v-form ref="form">
            <div>
              <v-row>
                <v-col cols="3">
                  <div
                    style="position: relative; width: 100%; margin-top: 10px"
                  >
                    <v-text-field
                      type="text"
                      v-model="basalArea"
                      persistent-placeholder
                      :placeholder="basalAreaPlaceholder"
                      hide-details
                      density="compact"
                      dense
                      style="padding-left: 15px"
                      variant="plain"
                      :disabled="isBasalAreaDisabled || !isConfirmEnabled"
                    >
                      <template v-slot:label>
                        Basal Area (m<sup>2</sup>/ha)
                      </template>
                    </v-text-field>
                    <!-- spin buttons -->
                    <div class="spin-box">
                      <div
                        class="spin-up-arrow-button"
                        @mousedown="startIncrementBasalArea"
                        @mouseup="stopIncrementBasalArea"
                        @mouseleave="stopIncrementBasalArea"
                        :class="{
                          disabled: isBasalAreaDisabled || !isConfirmEnabled,
                        }"
                      >
                        {{ SPIN_BUTTON.UP }}
                      </div>
                      <div
                        class="spin-down-arrow-button"
                        @mousedown="startDecrementBasalArea"
                        @mouseup="stopDecrementBasalArea"
                        @mouseleave="stopDecrementBasalArea"
                        :class="{
                          disabled: isBasalAreaDisabled || !isConfirmEnabled,
                        }"
                      >
                        {{ SPIN_BUTTON.DOWN }}
                      </div>
                    </div>
                    <div class="spin-text-field-bottom-line"></div>
                  </div>
                </v-col>
                <v-col class="col-space-3" />
                <v-col v-show="Util.isZeroValue(age)">
                  <span style="font-size: 12px"
                    >Density Measurements cannot be supplied without an
                    Age.</span
                  >
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="3">
                  <div
                    style="position: relative; width: 100%; margin-top: 10px"
                  >
                    <v-text-field
                      label="Trees per Hectare (tree/ha)"
                      type="text"
                      v-model="treesPerHectare"
                      persistent-placeholder
                      :placeholder="tphPlaceholder"
                      hide-details
                      density="compact"
                      dense
                      style="padding-left: 15px"
                      variant="plain"
                      :disabled="isTreesPerHectareDisabled || !isConfirmEnabled"
                    >
                    </v-text-field>
                    <!-- spin buttons -->
                    <div class="spin-box">
                      <div
                        class="spin-up-arrow-button"
                        @mousedown="startIncrementTPH"
                        @mouseup="stopIncrementTPH"
                        @mouseleave="stopIncrementTPH"
                        :class="{
                          disabled:
                            isTreesPerHectareDisabled || !isConfirmEnabled,
                        }"
                      >
                        {{ SPIN_BUTTON.UP }}
                      </div>
                      <div
                        class="spin-down-arrow-button"
                        @mousedown="startDecrementTPH"
                        @mouseup="stopDecrementTPH"
                        @mouseleave="stopDecrementTPH"
                        :class="{
                          disabled:
                            isTreesPerHectareDisabled || !isConfirmEnabled,
                        }"
                      >
                        {{ SPIN_BUTTON.DOWN }}
                      </div>
                    </div>
                    <div class="spin-text-field-bottom-line"></div>
                  </div>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="3">
                  <v-select
                    label="Minimum DBH Limit"
                    :items="minimumDBHLimitsOptions"
                    v-model="minimumDBHLimit"
                    item-title="label"
                    item-value="value"
                    hide-details
                    persistent-placeholder
                    placeholder=""
                    density="compact"
                    dense
                    disabled
                  ></v-select>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="3">
                  <v-text-field
                    label="Crown Closure (%)"
                    type="number"
                    v-model.number="percentCrownClosure"
                    :max="NUM_INPUT_LIMITS.CROWN_CLOSURE_MAX"
                    :min="NUM_INPUT_LIMITS.CROWN_CLOSURE_MIN"
                    :step="NUM_INPUT_LIMITS.CROWN_CLOSURE_STEP"
                    persistent-placeholder
                    :placeholder="crownClosurePlaceholder"
                    hide-details
                    density="compact"
                    dense
                    :disabled="
                      isPercentCrownClosureDisabled || !isConfirmEnabled
                    "
                  ></v-text-field>
                  <v-label
                    v-show="
                      Util.isZeroValue(percentCrownClosure) &&
                      !isPercentCrownClosureDisabled
                    "
                    style="font-size: 12px"
                    >Applying Default of 50%</v-label
                  >
                </v-col>
                <v-col class="col-space-3" />
                <v-col
                  cols="3"
                  v-show="
                    derivedBy === DERIVED_BY.BASAL_AREA &&
                    siteSpeciesValues === SITE_SPECIES_VALUES.COMPUTED
                  "
                >
                  <v-text-field
                    label="Current Diameter (cm)"
                    v-model="currentDiameter"
                    persistent-placeholder
                    placeholder=""
                    hide-details
                    density="compact"
                    dense
                    disabled
                  ></v-text-field>
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
import { useConfirmDialogStore } from '@/stores/common/confirmDialogStore'
import { storeToRefs } from 'pinia'
import { minimumDBHLimitsOptions } from '@/constants/options'
import {
  PANEL,
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  NOT_AVAILABLE_INDI,
  MODEL_PARAMETER_PANEL,
  NUM_INPUT_LIMITS,
  SPIN_BUTTON,
  CONTINUOUS_INC_DEC,
} from '@/constants/constants'
import { DEFAULT_VALUES } from '@/constants/defaults'
import {
  isCoastalZone,
  validateBasalAreaLimits,
  validateTreePerHectareLimits,
  validateQuadraticDiameter,
} from '@/utils/lookupMappings'

const form = ref<HTMLFormElement>()

const modelParameterStore = useModelParameterStore()
const messageDialogStore = useMessageDialogStore()
const confirmDialogStore = useConfirmDialogStore()

const {
  panelOpenStates,
  derivedBy,
  becZone,
  selectedSiteSpecies,
  siteSpeciesValues,
  age,
  height,
  basalArea,
  treesPerHectare,
  minimumDBHLimit,
  currentDiameter,
  percentCrownClosure,
} = storeToRefs(modelParameterStore)

const panelName = MODEL_PARAMETER_PANEL.STAND_DENSITY
const isConfirmEnabled = computed(
  () => modelParameterStore.panelState[panelName].editable,
)
const isConfirmed = computed(
  () => modelParameterStore.panelState[panelName].confirmed,
)

const isPercentCrownClosureDisabled = ref(false)
const isBasalAreaDisabled = ref(false)
const isTreesPerHectareDisabled = ref(false)

const basalAreaPlaceholder = ref('')
const tphPlaceholder = ref('')
const crownClosurePlaceholder = ref('')

// Interval references for continuous increment/decrement
let basalAreaIncrementInterval: number | null = null
let basalAreaDecrementInterval: number | null = null
let tphIncrementInterval: number | null = null
let tphDecrementInterval: number | null = null

const updateBasalAreaState = (isEnabled: boolean, isAgeZero: boolean) => {
  isBasalAreaDisabled.value = !isEnabled || isAgeZero

  if (isBasalAreaDisabled.value) {
    basalAreaPlaceholder.value = NOT_AVAILABLE_INDI.NA
    basalArea.value = null
  } else {
    basalAreaPlaceholder.value = ''
    basalArea.value = DEFAULT_VALUES.BASAL_AREA
  }
}

const updateTreesPerHectareState = (isEnabled: boolean, isAgeZero: boolean) => {
  isTreesPerHectareDisabled.value = !isEnabled || isAgeZero

  if (isTreesPerHectareDisabled.value) {
    tphPlaceholder.value = NOT_AVAILABLE_INDI.NA
    treesPerHectare.value = null
  } else {
    tphPlaceholder.value = ''
    treesPerHectare.value = DEFAULT_VALUES.TPH
  }
}

const updateCrownClosureState = (
  isVolume: boolean,
  isComputed: boolean,
  isAgeZero: boolean,
) => {
  isPercentCrownClosureDisabled.value = !(isVolume && isComputed) || isAgeZero

  if (isPercentCrownClosureDisabled.value) {
    crownClosurePlaceholder.value = NOT_AVAILABLE_INDI.NA
    percentCrownClosure.value = null
  } else {
    crownClosurePlaceholder.value = ''
    percentCrownClosure.value = 0
  }
}

const updateStates = (
  newDerivedBy: string | null,
  newSiteSpeciesValues: string | null,
  newAge: number | null,
) => {
  const isVolume = newDerivedBy === DERIVED_BY.VOLUME
  const isBasalArea = newDerivedBy === DERIVED_BY.BASAL_AREA
  const isComputed = newSiteSpeciesValues === SITE_SPECIES_VALUES.COMPUTED
  const isAgeZero = Util.isZeroValue(newAge)

  // Update states using individual functions
  updateBasalAreaState(isBasalArea && isComputed, isAgeZero)
  updateTreesPerHectareState(isBasalArea && isComputed, isAgeZero)
  updateCrownClosureState(isVolume, isComputed, isAgeZero)
}

watch(
  [derivedBy, siteSpeciesValues, age],
  ([newDerivedBy, newSiteSpeciesValues, newAge]) => {
    updateStates(newDerivedBy, newSiteSpeciesValues, newAge)
  },
  { immediate: true },
)

const incrementBasalArea = () => {
  const newValue = Util.increaseItemBySpinButton(
    basalArea.value,
    NUM_INPUT_LIMITS.BASAL_AREA_MAX,
    NUM_INPUT_LIMITS.BASAL_AREA_MIN,
    NUM_INPUT_LIMITS.BASAL_AREA_STEP,
  )
  // Format the value to ##0.0000
  basalArea.value = newValue.toFixed(NUM_INPUT_LIMITS.BASAL_AREA_DECIMAL_NUM)
}

const decrementBasalArea = () => {
  let newValue = Util.decrementItemBySpinButton(
    basalArea.value,
    NUM_INPUT_LIMITS.BASAL_AREA_MAX,
    NUM_INPUT_LIMITS.BASAL_AREA_MIN,
    NUM_INPUT_LIMITS.BASAL_AREA_STEP,
  )
  // Format the value to ##0.0000
  basalArea.value = newValue.toFixed(NUM_INPUT_LIMITS.BASAL_AREA_DECIMAL_NUM)
}

const incrementTPH = () => {
  const newValue = Util.increaseItemBySpinButton(
    treesPerHectare.value,
    NUM_INPUT_LIMITS.TPH_MAX,
    NUM_INPUT_LIMITS.TPH_MIN,
    NUM_INPUT_LIMITS.TPH_STEP,
  )
  // Format the value to ###0.00
  treesPerHectare.value = newValue.toFixed(NUM_INPUT_LIMITS.TPH_DECIMAL_NUM)
}

const decrementTPH = () => {
  let newValue = Util.decrementItemBySpinButton(
    treesPerHectare.value,
    NUM_INPUT_LIMITS.TPH_MAX,
    NUM_INPUT_LIMITS.TPH_MIN,
    NUM_INPUT_LIMITS.TPH_STEP,
  )
  // Format the value to ###0.00
  treesPerHectare.value = newValue.toFixed(NUM_INPUT_LIMITS.TPH_DECIMAL_NUM)
}

// Methods to handle continuous increment/decrement for Basal Area
const startIncrementBasalArea = () => {
  incrementBasalArea()
  basalAreaIncrementInterval = window.setInterval(
    incrementBasalArea,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopIncrementBasalArea = () => {
  if (basalAreaIncrementInterval !== null) {
    clearInterval(basalAreaIncrementInterval)
    basalAreaIncrementInterval = null
  }
}

const startDecrementBasalArea = () => {
  decrementBasalArea()
  basalAreaDecrementInterval = window.setInterval(
    decrementBasalArea,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopDecrementBasalArea = () => {
  if (basalAreaDecrementInterval !== null) {
    clearInterval(basalAreaDecrementInterval)
    basalAreaDecrementInterval = null
  }
}

// Methods to handle continuous increment/decrement for TPH
const startIncrementTPH = () => {
  incrementTPH()
  tphIncrementInterval = window.setInterval(
    incrementTPH,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopIncrementTPH = () => {
  if (tphIncrementInterval !== null) {
    clearInterval(tphIncrementInterval)
    tphIncrementInterval = null
  }
}

const startDecrementTPH = () => {
  decrementTPH()
  tphDecrementInterval = window.setInterval(
    decrementTPH,
    CONTINUOUS_INC_DEC.INTERVAL,
  )
}

const stopDecrementTPH = () => {
  if (tphDecrementInterval !== null) {
    clearInterval(tphDecrementInterval)
    tphDecrementInterval = null
  }
}

const clear = () => {
  if (form.value) {
    form.value.reset()
  }
  minimumDBHLimit.value = DEFAULT_VALUES.MINIMUM_DBH_LIMIT
  percentCrownClosure.value = DEFAULT_VALUES.PERCENT_CROWN_CLOSURE
}

const validateValues = (): boolean => {
  if (
    percentCrownClosure.value &&
    (!Number.isInteger(percentCrownClosure.value) ||
      percentCrownClosure.value < 0)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Crown Closure' must be a non-negative integer",
      { width: 400 },
    )
    return false
  }

  if (basalArea.value && !/^\d+(\.\d{4})?$/.test(basalArea.value)) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Basal Area' must be in the format ##0.0000",
      { width: 400 },
    )
    return false
  }

  if (treesPerHectare.value && !/^\d+(\.\d{2})?$/.test(treesPerHectare.value)) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Trees per Hectare' must be in the format ####0.00",
      { width: 400 },
    )
    return false
  }

  return true
}

const validateRange = (): boolean => {
  const ba = Util.toNumber(basalArea.value)
  if (
    ba &&
    (ba < NUM_INPUT_LIMITS.BASAL_AREA_MIN ||
      ba > NUM_INPUT_LIMITS.BASAL_AREA_MAX)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Basal Area' must range from 0.1000 and 250.0000",
      { width: 400 },
    )
    return false
  }

  const tph = Util.toNumber(treesPerHectare.value)
  if (
    tph &&
    (tph < NUM_INPUT_LIMITS.TPH_MIN || tph > NUM_INPUT_LIMITS.TPH_MAX)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Trees per Hectare' must range from 0.10 and 9999.90",
      { width: 400 },
    )
    return false
  }

  const pcc = Util.toNumber(percentCrownClosure.value)
  if (
    pcc &&
    (pcc < NUM_INPUT_LIMITS.CROWN_CLOSURE_MIN ||
      pcc > NUM_INPUT_LIMITS.CROWN_CLOSURE_MAX)
  ) {
    messageDialogStore.openDialog(
      'Invalid Input!',
      "'Crown Closure' must range from 0 and 100",
      { width: 400 },
    )
    return false
  }

  return true
}

function validateBALimits(): boolean {
  if (
    selectedSiteSpecies.value &&
    becZone.value &&
    basalArea.value &&
    height.value
  ) {
    const isValid = validateBasalAreaLimits(
      selectedSiteSpecies.value,
      isCoastalZone(becZone.value),
      basalArea.value,
      height.value,
    )

    return isValid
  }
  return true
}

function validateTPHLimits(): string | null {
  if (
    basalArea.value &&
    treesPerHectare.value &&
    height.value &&
    selectedSiteSpecies.value &&
    becZone.value
  ) {
    return validateTreePerHectareLimits(
      basalArea.value,
      treesPerHectare.value,
      height.value,
      selectedSiteSpecies.value,
      isCoastalZone(becZone.value),
    )
  }

  return null
}

function validateQuadDiameter(): string | null {
  if (basalArea.value && treesPerHectare.value && minimumDBHLimit.value) {
    return validateQuadraticDiameter(
      basalArea.value,
      treesPerHectare.value,
      minimumDBHLimit.value,
    )
  }

  return null
}

async function validateFormInputs(): Promise<boolean> {
  if (!validateRange() || !validateValues()) {
    return false
  }

  const isBasalAreaValid = validateBALimits()
  if (!isBasalAreaValid) {
    const userResponse = await confirmDialogStore.openDialog(
      'Confirm',
      'Basal Area is above a likely maximum for the entered height. Do you wish to proceed?',
    )

    if (!userResponse) {
      return false
    }
  }

  const validateTPHmessage = validateTPHLimits()
  if (validateTPHmessage) {
    const userResponse = await confirmDialogStore.openDialog(
      'Confirm',
      validateTPHmessage,
    )

    if (!userResponse) {
      return false
    }
  }

  const validateQuadDiamMessage = validateQuadDiameter()
  if (validateQuadDiamMessage) {
    const userResponse = await confirmDialogStore.openDialog(
      'Confirm',
      validateQuadDiamMessage,
    )

    if (!userResponse) {
      return false
    }
  }

  return true
}

const onConfirm = async () => {
  const isFormValid = await validateFormInputs()

  if (!isFormValid) {
    return
  }

  form.value?.validate()

  // this panel is not in a confirmed state
  if (!isConfirmed.value) {
    modelParameterStore.confirmPanel(panelName)
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
