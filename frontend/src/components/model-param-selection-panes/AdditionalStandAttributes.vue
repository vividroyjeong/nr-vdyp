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
              <span class="text-h6">Additional Stand Attributes</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n3">
          <div>
            <v-row>
              <v-col cols="auto">
                <v-radio-group v-model="computedValues">
                  <v-radio
                    label="Use Computed Values (These additional Stand attributes require that a Stand Age and Basal Area be supplied on the Site Index and the Density pages)"
                    value="use"
                  ></v-radio>
                  <v-radio
                    label="Modify Computed Values"
                    value="modify"
                  ></v-radio>
                </v-radio-group>
              </v-col>
            </v-row>
          </div>
          <div class="mt-n5">
            <v-row>
              <v-col cols="4">
                <v-text-field
                  label="Lorey Height - 7.5cm+ (meters)"
                  type="number"
                  max="100"
                  min="0"
                  step="0.1"
                  v-model="loreyHeight"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                ></v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="wholeStemVolume75cm"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Whole Stem Volume - 7.5cm+ (m<sup>3</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
            </v-row>
            <v-row>
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
                    Basal Area - 12.5cm+ (m<sup>2</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="wholeStemVolume125cm"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Whole Stem Volume - 12.5cm+ (m<sup>3</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="closeUtilVolume"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Close Utilization Volume - 12.5cm+ (m<sup>3</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="closeUtilNetDecayVolume"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Close Utilization Net Decay Volume - 12.5cm+
                    (m<sup>3</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4">
                <v-text-field
                  type="number"
                  v-model="closeUtilNetDecayWasteVolume"
                  max="100"
                  min="0"
                  step="0.1"
                  persistent-placeholder
                  placeholder="N/A"
                  density="compact"
                  dense
                >
                  <template v-slot:label>
                    Close Utilization Net Decay Waste Volume - 12.5cm+
                    (m<sup>3</sup>/ha)
                  </template>
                </v-text-field>
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="4"></v-col>
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

const computedValues = ref('use')
const loreyHeight = ref('21.83')
const basalArea = ref('39.3337')
const closeUtilVolume = ref('304.8')
const closeUtilNetDecayWasteVolume = ref('245.5')
const wholeStemVolume75cm = ref('332.4')
const wholeStemVolume125cm = ref('328.1')
const closeUtilNetDecayVolume = ref('263.6')

const clear = () => {}
const confirm = () => {}
</script>

<style scoped></style>
