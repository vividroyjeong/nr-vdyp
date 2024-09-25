<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpen">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpen === 0 ? 'mdi-chevron-up' : 'mdi-chevron-down'
              }}</v-icon>
            </v-col>
            <v-col>
              <span class="text-h6">Site Information</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <div>
            <v-row>
              <v-col cols="6">
                <v-row>
                  <v-col cols="6">
                    <v-select
                      label="BEC Zone"
                      :items="computedBecZoneOptions"
                      v-model="becZone"
                      item-title="label"
                      item-value="value"
                      clearable
                      hide-details="auto"
                      persistent-placeholder
                      placeholder="Select Bec Zone"
                      density="compact"
                      dense
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
                    ></v-select>
                  </v-col>
                </v-row>
              </v-col>
              <v-col class="col-space-6" />
              <v-col>
                <v-row>
                  <v-col cols="6">
                    <v-checkbox
                      label="Include Secondary Dominant Height in Yield Table"
                      v-model="incSecondaryHeight"
                      hide-details="auto"
                      :disabled="derivedBy === DERIVED_BY.VOLUME"
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
                      :readonly="derivedBy === DERIVED_BY.VOLUME"
                    ></v-select>
                  </v-col>
                  <v-col class="col-space-6" />
                  <v-col>
                    <v-select
                      label="Site Index Curve"
                      :items="siteIndexCurveOptions"
                      v-model="siteIndexCurve"
                      item-title="label"
                      item-value="value"
                      hide-details="auto"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                      readonly
                    ></v-select>
                  </v-col>
                </v-row>
              </v-col>
              <v-col class="col-space-6" />
              <v-col>
                <v-col>
                  <div class="mb-5" style="font-size: 0.875rem">
                    *Ministry Default Curve for this Species
                  </div></v-col
                >
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
                  :readonly="isSiteSpeciesValueComputed"
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
                <v-row>
                  <v-col cols="6">
                    <v-select
                      label="Age Type"
                      :items="ageTypeOptions"
                      v-model="ageType"
                      item-title="label"
                      item-value="value"
                      clearable
                      hide-details="auto"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                    ></v-select>
                  </v-col>
                  <v-col class="col-space-6" />
                  <v-col>
                    <v-text-field
                      label="Age (years)"
                      type="number"
                      v-model="age"
                      max="100"
                      min="0"
                      step="0.1"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                      @input="handleAgeInput($event)"
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="6" />
                  <v-col class="col-space-6" />
                  <v-col>
                    <v-text-field
                      label="Height (meters)"
                      type="number"
                      v-model="height"
                      max="100"
                      min="0"
                      step="0.1"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                      @input="handleHeightInput($event)"
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="6" />
                  <v-col class="col-space-6" />
                  <v-col>
                    <v-text-field
                      label="BHA 50 Site Index"
                      type="number"
                      v-model="bha50SiteIndex"
                      max="100"
                      min="0"
                      step="0.1"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                      @input="handleBHA50SiteIndexInput($event)"
                    ></v-text-field
                  ></v-col>
                </v-row>
              </v-col>
              <v-col cols="6">
                <div class="mt-2">
                  <v-radio-group v-model="floating" row>
                    <v-radio
                      label="float"
                      value="ageFloat"
                      style="margin-bottom: 45px"
                    ></v-radio>
                    <v-radio
                      label="float"
                      value="heightFloat"
                      style="margin-bottom: 45px"
                    ></v-radio>
                    <v-radio label="float" value="siteIndexFloat"></v-radio>
                  </v-radio-group>
                </div>
              </v-col>
            </v-row>
          </div>
          <v-card-actions class="mt-5 pr-0">
            <v-spacer></v-spacer>
            <v-btn class="white-btn" @click="clear">Clear</v-btn>
            <v-btn class="blue-btn ml-2" @click="confirm">Confirm</v-btn>
          </v-card-actions>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-card>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useModelParameterStore } from '@/stores/modelParameterStore'
import { storeToRefs } from 'pinia'
import {
  becZoneMap,
  ecoZoneOptions,
  siteIndexCurveOptions,
  siteSpeciesValuesOptions,
  ageTypeOptions,
} from '@/constants/options'
import { DERIVED_BY } from '@/constants/constants'

const panelOpen = ref(0)

const floating = ref(null)

const modelParameterStore = useModelParameterStore()
const {
  derivedBy,
  speciesGroups,
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
} = storeToRefs(modelParameterStore)

const computedBecZoneOptions = computed(() =>
  (Object.keys(becZoneMap) as Array<keyof typeof becZoneMap>).map((code) => ({
    label: `${code} - ${becZoneMap[code]}`,
    value: code,
  })),
)

const siteSpeciesOptions = computed(() =>
  speciesGroups.value.map((group) => ({
    label: group.siteSpecies,
    value: group.siteSpecies,
  })),
)

const isSiteSpeciesValueComputed = ref(false)

watch(
  derivedBy,
  (newValue) => {
    if (newValue === DERIVED_BY.VOLUME) {
      incSecondaryHeight.value = false
    } else if (newValue === DERIVED_BY.BASAL_AREA) {
      //
    }
  },
  { immediate: true }, // make it responsive from the start with 'immediate' settings
)

const handleAgeInput = (event: Event) => {
  const input = event.target as HTMLInputElement
  let value = input.value

  // Remove any non-digit characters (also prevents the entry of '.')
  value = value.replace(/\D/g, '')

  // Ensure the value is a valid integer and between 0 and 100
  const intValue = parseInt(value, 10)

  if (!isNaN(intValue) && intValue >= 0 && intValue <= 100) {
    age.value = intValue
  } else if (intValue > 100) {
    age.value = 100 // Limit to 100 if the value exceeds
  } else {
    age.value = null // Handle invalid or empty input
  }
}

const handleHeightInput = (event: Event) => {
  const input = event.target as HTMLInputElement
  let value = input.value

  // Allow only up to the first decimal place
  if (value.includes('.')) {
    const [integerPart, decimalPart] = value.split('.')
    if (decimalPart.length > 1) {
      value = `${integerPart}.${decimalPart.slice(0, 1)}`
    }
  }

  // Convert value to a number and ensure it is between 0 and 100
  let floatValue = parseFloat(value)
  if (!isNaN(floatValue)) {
    if (floatValue < 0) {
      floatValue = 0
    } else if (floatValue > 100) {
      floatValue = 100
    }
    height.value = floatValue
  } else {
    height.value = null // Handle invalid or empty input
  }
}

const handleBHA50SiteIndexInput = (event: Event) => {
  const input = event.target as HTMLInputElement
  let value = input.value

  // allow only up to the first decimal place
  if (value.includes('.')) {
    const [integerPart, decimalPart] = value.split('.')
    if (decimalPart.length > 1) {
      value = `${integerPart}.${decimalPart.slice(0, 1)}`
    }
  }

  // Convert value to a number and ensure it is between 0 and 100
  let floatValue = parseFloat(value)
  if (!isNaN(floatValue)) {
    if (floatValue < 0) {
      floatValue = 0
    } else if (floatValue > 100) {
      floatValue = 100
    }
    bha50SiteIndex.value = floatValue
  } else {
    bha50SiteIndex.value = null // Handle invalid or empty input
  }
}

const clear = () => {}
const confirm = () => {}
</script>

<style scoped></style>
