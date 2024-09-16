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
              <span class="text-h6">Report Information</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text">
          <div class="species-row mt-1">
            <v-row>
              <v-col cols="4">
                <v-text-field
                  label="Starting Age"
                  type="number"
                  v-model="startingAge"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  label="Finishing Age"
                  type="number"
                  v-model="finishingAge"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  label="Age Increment"
                  type="number"
                  v-model="ageIncrement"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
            </v-row>
          </div>
          <div>
            <v-container class="mt-5">
              <div class="ml-n4 mt-n5">
                <span class="text-h6">Volume Reported</span>
              </div>
              <v-row class="ml-n6">
                <v-col cols="12" md="2" sm="2">
                  <div>
                    <div>
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Whole Stem"
                        value="Whole Stem"
                        hide-details
                      ></v-checkbox>
                    </div>
                    <div class="mt-3">
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Computed MAI"
                        value="Computed MAI"
                        hide-details
                      ></v-checkbox>
                    </div>
                  </div>
                </v-col>
                <v-col cols="12" md="2" sm="2">
                  <div>
                    <div>
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Close Utilization"
                        value="Close Utilization"
                        hide-details
                      ></v-checkbox>
                    </div>
                    <div class="mt-3">
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Species Composition"
                        value="Species Composition"
                        hide-details
                      ></v-checkbox>
                    </div>
                  </div>
                </v-col>
                <v-col cols="12" md="2" sm="2">
                  <div>
                    <div>
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Net Decay"
                        value="Net Decay"
                        hide-details
                      ></v-checkbox>
                    </div>
                    <div class="mt-3">
                      <v-checkbox
                        v-model="selectedVolumeReported"
                        label="Culmination Values"
                        value="Culmination Values"
                        hide-details
                      ></v-checkbox>
                    </div>
                  </div>
                </v-col>
                <v-col cols="12" md="2" sm="2">
                  <div class="ml-n3">
                    <v-checkbox
                      v-model="selectedVolumeReported"
                      label="Net Decay and Waste"
                      value="Net Decay and Waste"
                      hide-details
                    ></v-checkbox>
                  </div>
                  <div>
                    <v-select
                      label="Projection Type"
                      :items="projectionTypeOptions"
                      v-model="projectionType"
                      item-title="label"
                      item-value="value"
                      clearable
                      hide-details="auto"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                    ></v-select>
                  </div>
                </v-col>
                <v-col cols="12" md="1" sm="1"> </v-col>
                <v-col cols="12" md="2" sm="2">
                  <div class="ml-n3">
                    <v-checkbox
                      v-model="selectedVolumeReported"
                      label="Net Decay, Waste and Breakage"
                      value="Net Decay, Waste and Breakage"
                      hide-details
                    ></v-checkbox>
                  </div>
                  <div>
                    <v-select
                      label="Report Title"
                      :items="reportTitleOptions"
                      v-model="reportTitle"
                      item-title="label"
                      item-value="value"
                      clearable
                      hide-details="auto"
                      persistent-placeholder
                      placeholder="Select..."
                      density="compact"
                      dense
                    ></v-select>
                  </div>
                </v-col>
              </v-row>
            </v-container>
          </div>
          <div class="mt-5">
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Minimum DBH Limit by Species #1"
                  type="string"
                  v-model="minimumDBHLimit1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="5" class="ma-5">
                <!-- <div>value: {{ slidervalue1 }}</div> -->
                <vue-slider
                  v-model="slidervalue1"
                  :data="sliderData"
                  :data-value="'id'"
                  :data-label="'name'"
                  :contained="true"
                  :tooltip="'none'"
                  :dotStyle="{ backgroundColor: '#787878' }"
                  :rail-style="{ backgroundColor: '#f5f5f5' }"
                  :process-style="{ backgroundColor: '#787878' }"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Minimum DBH Limit by Species #2"
                  type="string"
                  v-model="minimumDBHLimit2"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="5" class="ma-5">
                <!-- <div>value: {{ slidervalue2 }}</div> -->
                <vue-slider
                  v-model="slidervalue2"
                  :data="sliderData"
                  :data-value="'id'"
                  :data-label="'name'"
                  :contained="true"
                  :tooltip="'none'"
                  :dotStyle="{ backgroundColor: '#787878' }"
                  :rail-style="{ backgroundColor: '#f5f5f5' }"
                  :process-style="{ backgroundColor: '#787878' }"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Minimum DBH Limit by Species #3"
                  type="string"
                  v-model="minimumDBHLimit3"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="5" class="ma-5">
                <!-- <div>value: {{ slidervalue3 }}</div> -->
                <vue-slider
                  v-model="slidervalue3"
                  :data="sliderData"
                  :data-value="'id'"
                  :data-label="'name'"
                  :contained="true"
                  :tooltip="'none'"
                  :dotStyle="{ backgroundColor: '#787878' }"
                  :rail-style="{ backgroundColor: '#f5f5f5' }"
                  :process-style="{ backgroundColor: '#787878' }"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-text-field
                  label="Minimum DBH Limit by Species #4"
                  type="string"
                  v-model="minimumDBHLimit4"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="5" class="ma-5">
                <!-- <div>value: {{ slidervalue4 }}</div> -->
                <vue-slider
                  v-model="slidervalue4"
                  :data="sliderData"
                  :data-value="'id'"
                  :data-label="'name'"
                  :contained="true"
                  :tooltip="'none'"
                  :dotStyle="{ backgroundColor: '#787878' }"
                  :rail-style="{ backgroundColor: '#f5f5f5' }"
                  :process-style="{ backgroundColor: '#787878' }"
                />
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
import { ref } from 'vue'
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'

const panelOpen = ref(0)

const startingAge = ref(null)
const finishingAge = ref(null)
const ageIncrement = ref(null)
const selectedVolumeReported = ref([])
const projectionType = ref(null)
const reportTitle = ref(null)
const projectionTypeOptions = ref([])
const reportTitleOptions = ref([])

const minimumDBHLimit1 = ref(null)
const minimumDBHLimit2 = ref(null)
const minimumDBHLimit3 = ref(null)
const minimumDBHLimit4 = ref(null)

const slidervalue1 = ref(4.0)
const slidervalue2 = ref(4.0)
const slidervalue3 = ref(4.0)
const slidervalue4 = ref(4.0)

const sliderData = [
  {
    id: 4.0,
    name: '4.0 cm+',
  },
  {
    id: 7.5,
    name: '7.5 cm+',
  },
  {
    id: 12.5,
    name: '12.5 cm+',
  },
  {
    id: 17.5,
    name: '17.5 cm+',
  },
  {
    id: 22.5,
    name: '22.5 cm+',
  },
]

const clear = () => {}
const confirm = () => {}
</script>
<style scoped></style>
