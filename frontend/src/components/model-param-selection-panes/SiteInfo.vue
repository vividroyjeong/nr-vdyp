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
              <v-col cols="3">
                <v-select
                  label="BEC Zone"
                  :items="becZones"
                  v-model="selectedBecZone"
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
              <v-col class="col-space" />
              <v-col cols="3">
                <v-select
                  label="Eco Zone"
                  :items="ecoZones"
                  v-model="selectedEcoZone"
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
              <v-col class="col-space" />
              <v-col cols="3">
                <v-checkbox
                  label="Include Secondary Dominant Height in Yield Table"
                  v-model="includeSecondaryHeight"
                  hide-details="auto"
                ></v-checkbox>
              </v-col>
              <v-col class="col-space" />
              <v-col>
                <div class="mt-2">
                  <v-text-field
                    label="Species Group"
                    :model-value="speciesGroup"
                    variant="underlined"
                    hide-details
                    readonly
                    density="compact"
                    dense
                  ></v-text-field>
                </div>
              </v-col>
            </v-row>
            <div class="hr-line mt-2"></div>
            <v-row class="mt-7">
              <v-col cols="3">
                <v-select
                  label="Site Species"
                  :items="siteSpecies"
                  v-model="selectedSpecies"
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
              <v-col class="col-space" />
              <v-col cols="3">
                <v-select
                  label="Site Index Curve"
                  :items="siteIndexCurveOptions"
                  v-model="selectedSiteIndexCurve"
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
              <v-col class="col-space" />
              <v-col>
                <div class="mt-5 ml-2" style="font-size: 0.875rem">
                  *Ministry Default Curve for this Species
                </div>
              </v-col>
            </v-row>
            <div class="hr-line mt-6"></div>
            <v-row
              class="mt-7"
              style="display: inline-flex; align-items: center"
            >
              <v-col cols="auto" style="margin-bottom: 20px">
                <span>Site Species Values:</span>
              </v-col>
              <v-col cols="auto">
                <v-radio-group v-model="siteSpeciesValues" inline>
                  <v-radio label="Computed" value="computed"></v-radio>
                  <v-radio label="Supplied" value="supplied"></v-radio>
                </v-radio-group>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="3">
                <v-select
                  label="Age Type"
                  :items="ageTypes"
                  v-model="selectedAgeType"
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
              <v-col class="col-space" />
              <v-col cols="3">
                <v-text-field
                  label="Age (years)"
                  type="number"
                  v-model="ageYears"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col class="col-space" />
              <v-col>
                <div class="mt-2">
                  <v-radio-group v-model="floatOptions1" row>
                    <v-radio label="Float" value="float"></v-radio>
                  </v-radio-group>
                </div>
              </v-col>
            </v-row>
            <v-row class="mt-n7">
              <v-col cols="3"> &nbsp; </v-col>
              <v-col class="col-space" />
              <v-col cols="3">
                <v-text-field
                  label="Height (meters)"
                  type="number"
                  v-model="heightMeters"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col class="col-space" />
              <v-col>
                <div class="mt-2">
                  <v-radio-group v-model="floatOptions2" row>
                    <v-radio label="Float" value="float"></v-radio>
                  </v-radio-group>
                </div>
              </v-col>
            </v-row>
            <v-row class="mt-n7">
              <v-col cols="3"> &nbsp; </v-col>
              <v-col class="col-space" />
              <v-col cols="3">
                <v-text-field
                  label="BHA 50 Site Index"
                  type="number"
                  v-model="siteIndex"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="Select..."
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col class="col-space" />
              <v-col>
                <div class="mt-2">
                  <v-radio-group v-model="floatOptions3" row>
                    <v-radio label="Float" value="float"></v-radio>
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
import { ref } from 'vue'

const panelOpen = ref(0)

const becZones = ref(['IDF - Interior Douglas Fir', 'xxx'])
const ecoZones = ref(['Eco Zone 1', 'Eco Zone 2', 'Eco Zone 3'])
const selectedBecZone = ref('IDF - Interior Douglas Fir')
const selectedEcoZone = ref(null)

const speciesGroup = ref('PL')

const includeSecondaryHeight = ref(true)

const siteSpecies = ref(['PL', 'AC', 'H', 'S'])
const selectedSpecies = ref('PL')

const siteIndexCurveOptions = ref(['Thrower (1994)'])
const selectedSiteIndexCurve = ref('Thrower (1994)')

const siteSpeciesValues = ref(null)

const ageTypes = ref(['Total', 'Partial'])
const selectedAgeType = ref('Total')

const ageYears = ref(60)
const heightMeters = ref(17)
const siteIndex = ref(30.0)

const floatOptions1 = ref(null)
const floatOptions2 = ref(null)
const floatOptions3 = ref(null)

const clear = () => {}
const confirm = () => {}
</script>

<style scoped></style>
