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
              <span class="text-h6">Stand Density</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <div>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Percent Stockable Area"
                  type="number"
                  v-model="percentStockableArea"
                  max="100"
                  min="0"
                  step="5"
                  :rules="[validatePercentStockableArea]"
                  :error-messages="percentStockableAreaError"
                  placeholder="Select..."
                  persistent-placeholder
                  density="compact"
                  dense
                ></v-text-field
              ></v-col>
              <v-col class="col-space" />
              <v-col cols="3">
                <v-text-field
                  type="number"
                  v-model="basalArea"
                  min="0"
                  step="0.0001"
                  :rules="[validateMinimum]"
                  :error-messages="basalAreaError"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Basal Area (m<sup>2</sup>/ha)
                  </template>
                </v-text-field></v-col
              >
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Trees per Hectare"
                  type="number"
                  v-model="treesPerHectare"
                  min="0"
                  step="0.01"
                  :rules="[validateMinimum]"
                  :error-messages="treesPerHectareError"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col class="col-space" />
              <v-col cols="3">
                <v-select
                  label="Minimum DBH Limit"
                  :items="minimumDBHLimits"
                  v-model="selectedMinimumDBHLimit"
                  item-title="label"
                  item-value="value"
                  hide-details
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                  readonly
                ></v-select>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Percent Crown Closure"
                  type="number"
                  v-model="percentCrownClosure"
                  max="100"
                  min="0"
                  step="0.1"
                  :rules="[validatePercentCrownClosure]"
                  :error-messages="percentCrownClosureError"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field>
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
import { ref, computed } from 'vue'

const panelOpen = ref(0)

const minimumDBHLimits = ref(['Eco Zone 1', 'Eco Zone 2', 'Eco Zone 3'])
const percentStockableArea = ref()
const basalArea = ref()
const treesPerHectare = ref()
const selectedMinimumDBHLimit = ref('7.5 cm+')
const percentCrownClosure = ref(50)

const validatePercentStockableArea = (value: any) => {
  if (value === null || value === '') {
    return 'Percent Stockable Area is required'
  }
  if (value < 0 || value > 100) {
    return 'Please enter a value between 0 and 100'
  }
  return true
}

const validatePercentCrownClosure = (value: any) => {
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
  const error = validatePercentStockableArea(percentStockableArea.value)
  return error === true ? [] : [error]
})

const percentCrownClosureError = computed(() => {
  const error = validatePercentCrownClosure(percentCrownClosure.value)
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

const clear = () => {}
const confirm = () => {}
</script>

<style scoped></style>
