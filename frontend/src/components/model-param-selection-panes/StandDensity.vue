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
                  <v-text-field
                    label="% Stockable Area"
                    type="number"
                    v-model="percentStockableArea"
                    max="100"
                    min="0"
                    step="5"
                    :rules="[validatePercent]"
                    :error-messages="percentStockableAreaError"
                    placeholder=""
                    persistent-placeholder
                    hide-details="auto"
                    density="compact"
                    dense
                  ></v-text-field>
                  <v-label
                    v-show="Util.isEmptyOrZero(percentStockableArea)"
                    style="font-size: 12px"
                    >A default will be computed when the model is run.</v-label
                  >
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="3">
                  <v-text-field
                    type="number"
                    v-model="basalArea"
                    min="0.1000"
                    max="250.0000"
                    step="2.5"
                    :rules="[validateMinimum]"
                    :error-messages="basalAreaError"
                    persistent-placeholder
                    :placeholder="basalAreaPlaceholder"
                    density="compact"
                    dense
                    :disabled="isBasalAreaDisabled"
                  >
                    <template v-slot:label>
                      Basal Area (m<sup>2</sup>/ha)
                    </template>
                  </v-text-field></v-col
                >
                <v-col cols="5" v-show="Util.isZeroValue(age)">
                  <span style="font-size: 12px"
                    >Density Measurements cannot be supplied without an
                    Age.</span
                  >
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="3">
                  <v-text-field
                    label="Trees per Hectare (tree/ha)"
                    type="number"
                    v-model="treesPerHectare"
                    min="0.10"
                    max="9999.90"
                    step="250.00"
                    :rules="[validateMinimum]"
                    :error-messages="treesPerHectareError"
                    persistent-placeholder
                    :placeholder="tphPlaceholder"
                    density="compact"
                    dense
                    :disabled="isTreesPerHectareDisabled"
                  >
                  </v-text-field>
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
                    v-model="percentCrownClosure"
                    max="100"
                    min="0"
                    step="5"
                    :rules="[validatePercent]"
                    :error-messages="percentCrownClosureError"
                    persistent-placeholder
                    :placeholder="crownClosurePlaceholder"
                    hide-details="auto"
                    density="compact"
                    dense
                    :disabled="isPercentCrownClosureDisabled"
                  ></v-text-field>
                  <v-label
                    v-show="
                      Util.isEmptyOrZero(percentCrownClosure) &&
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
import { storeToRefs } from 'pinia'
import { minimumDBHLimitsOptions } from '@/constants/options'
import {
  PANEL,
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  DEFAULT_VALUES,
  NOT_AVAILABLE_INDI,
} from '@/constants/constants'

const form = ref<HTMLFormElement>()

const modelParameterStore = useModelParameterStore()
const {
  panelOpenStates,
  derivedBy,
  siteSpeciesValues,
  age,
  percentStockableArea,
  basalArea,
  treesPerHectare,
  minimumDBHLimit,
  currentDiameter,
  percentCrownClosure,
} = storeToRefs(modelParameterStore)

const panelName = 'standDensity'
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

const updateBasalAreaState = (isEnabled: boolean, isAgeZero: boolean) => {
  isBasalAreaDisabled.value = !isEnabled || isAgeZero

  if (isBasalAreaDisabled.value) {
    basalAreaPlaceholder.value = NOT_AVAILABLE_INDI.NA
    basalArea.value = null
  } else {
    basalAreaPlaceholder.value = ''
    basalArea.value = 10.0
  }
}

const updateTreesPerHectareState = (isEnabled: boolean, isAgeZero: boolean) => {
  isTreesPerHectareDisabled.value = !isEnabled || isAgeZero

  if (isTreesPerHectareDisabled.value) {
    tphPlaceholder.value = NOT_AVAILABLE_INDI.NA
    treesPerHectare.value = null
  } else {
    tphPlaceholder.value = ''
    treesPerHectare.value = 1000.0
  }
}

const updateCrownClosureState = (
  isVolume: boolean,
  isComputed: boolean,
  isAgeEmptyOrZero: boolean,
) => {
  isPercentCrownClosureDisabled.value =
    !(isVolume && isComputed) || isAgeEmptyOrZero

  if (isPercentCrownClosureDisabled.value) {
    crownClosurePlaceholder.value = NOT_AVAILABLE_INDI.NA
    percentCrownClosure.value = null
  } else {
    crownClosurePlaceholder.value = ''
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
  const isAgeEmptyOrZero = Util.isEmptyOrZero(newAge)

  // Update states using individual functions
  updateBasalAreaState(isBasalArea && isComputed, isAgeEmptyOrZero)
  updateTreesPerHectareState(isBasalArea && isComputed, isAgeEmptyOrZero)
  updateCrownClosureState(isVolume, isComputed, isAgeEmptyOrZero)
}

watch(
  [derivedBy, siteSpeciesValues, age],
  ([newDerivedBy, newSiteSpeciesValues, newAge]) => {
    updateStates(newDerivedBy, newSiteSpeciesValues, newAge)
  },
  { immediate: true },
)

const validatePercent = (value: any) => {
  if (value === null || value === '') {
    return true
  }
  if (value < 0 || value > 100) {
    return 'Please enter a value between 0 and 100'
  }
  return true
}

const validateMinimum = (value: any) => {
  if (value === null || value === '') {
    return true
  }
  if (value < 0) {
    return 'Please enter a value greater than 0'
  }
  return true
}

const percentStockableAreaError = computed(() => {
  const error = validatePercent(percentStockableArea.value)
  return error === true ? [] : [error]
})

const percentCrownClosureError = computed(() => {
  const error = validatePercent(percentCrownClosure.value)
  return error === true ? [] : [error]
})

const basalAreaError = computed(() => {
  const error = validateMinimum(basalArea.value)
  return error === true ? [] : [error]
})

const treesPerHectareError = computed(() => {
  const error = validateMinimum(treesPerHectare.value)
  return error === true ? [] : [error]
})

const clear = () => {
  if (form.value) {
    form.value.reset()
  }
  minimumDBHLimit.value = DEFAULT_VALUES.MINIMUM_DBH_LIMIT
  percentCrownClosure.value = DEFAULT_VALUES.PERCENT_CROWN_CLOSURE
}
const onConfirm = () => {
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
