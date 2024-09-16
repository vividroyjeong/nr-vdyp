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
              <v-col cols="4">
                <v-text-field
                  label="Percent Stockable Area"
                  type="number"
                  v-model="percentStockableArea"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field
              ></v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="basalArea"
                  max="100"
                  min="0"
                  step="0.1"
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
              <v-col cols="4">
                <v-text-field
                  label="Trees per Hectare"
                  type="number"
                  v-model="treesPerHectare"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4">
                <v-select
                  label="Minimum DBH Limit"
                  :items="minimumDBHLimits"
                  v-model="selectedMinimumDBHLimit"
                  item-title="label"
                  item-value="value"
                  clearable
                  hide-details="auto"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-select>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4">
                <v-text-field
                  label="Percent Crown Closure"
                  type="number"
                  v-model="percentCrownClosure"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4"> </v-col>
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
import { ref } from 'vue'

const panelOpen = ref(0)

const minimumDBHLimits = ref(['Eco Zone 1', 'Eco Zone 2', 'Eco Zone 3'])
const percentStockableArea = ref(55)
const basalArea = ref()
const treesPerHectare = ref()
const selectedMinimumDBHLimit = ref('7.5 cm+')
const percentCrownClosure = ref(50)

const clear = () => {}
const confirm = () => {}
</script>

<style scoped></style>
