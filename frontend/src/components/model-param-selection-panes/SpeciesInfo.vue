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
              <span class="text-h6">Species Information</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n4">
          <div>
            <v-row style="display: inline-flex; align-items: center">
              <v-col cols="auto" style="margin-bottom: 20px">
                <span>Species % derived by:</span>
              </v-col>
              <v-col cols="auto">
                <v-radio-group v-model="derivedBy" inline>
                  <v-radio label="Volume" value="volume"></v-radio>
                  <v-radio label="Basal Area" value="basalArea"></v-radio>
                </v-radio-group>
              </v-col>
            </v-row>
          </div>
          <div class="mt-n5">
            <v-row>
              <!-- input -->
              <v-col cols="5">
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #1"
                        :items="speciesOptions"
                        v-model="species1"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #1 Percent"
                        type="number"
                        v-model="speciesPercent1"
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
                <div class="hr-line mb-1"></div>
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #2"
                        :items="speciesOptions"
                        v-model="species2"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #2 Percent"
                        type="number"
                        v-model="speciesPercent2"
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
                <div class="hr-line mb-1"></div>
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #3"
                        :items="speciesOptions"
                        v-model="species3"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #3 Percent"
                        type="number"
                        v-model="speciesPercent3"
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
                <div class="hr-line mb-1"></div>
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #4"
                        :items="speciesOptions"
                        v-model="species4"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #4 Percent"
                        type="number"
                        v-model="speciesPercent4"
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
                <div class="hr-line mb-1"></div>
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #5"
                        :items="speciesOptions"
                        v-model="species5"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #5 Percent"
                        type="number"
                        v-model="speciesPercent5"
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
                <div class="hr-line mb-1"></div>
                <div>
                  <v-row>
                    <v-col cols="6">
                      <v-select
                        label="Species #6"
                        :items="speciesOptions"
                        v-model="species6"
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
                    <v-col cols="6">
                      <v-text-field
                        label="Species #6 Percent"
                        type="number"
                        v-model="speciesPercent6"
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
                <div class="hr-line mb-1"></div>
              </v-col>
              <v-col class="vertical-line pb-0" />
              <!-- output -->
              <v-col cols="6">
                <div class="mt-2">
                  <v-row>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group"
                        :model-value="speciesGroup1"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group Percent"
                        :model-value="speciesGroupPercent1"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Site Species"
                        :model-value="siteSpecies1"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </div>
                <div class="hr-line mb-3"></div>
                <div>
                  <v-row>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group"
                        :model-value="speciesGroup2"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group Percent"
                        :model-value="speciesGroupPercent2"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Site Species"
                        :model-value="siteSpecies2"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </div>
                <div class="hr-line mb-3"></div>
                <div>
                  <v-row>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group"
                        :model-value="speciesGroup3"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group Percent"
                        :model-value="speciesGroupPercent3"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Site Species"
                        :model-value="siteSpecies3"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </div>
                <div class="hr-line mb-3"></div>
                <div>
                  <v-row>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group"
                        :model-value="speciesGroup4"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Species Group Percent"
                        :model-value="speciesGroupPercent4"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                    <v-col cols="4" sm="4" md="4">
                      <v-text-field
                        label="Site Species"
                        :model-value="siteSpecies4"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </div>
              </v-col>
            </v-row>
          </div>
          <div>
            <v-row>
              <v-col cols="5">
                <div>
                  <v-row>
                    <v-col cols="6"> </v-col>
                    <v-col cols="6">
                      <v-text-field
                        label="Total Species Percent"
                        :model-value="totalSpeciesPercent"
                        variant="underlined"
                        readonly
                        density="compact"
                        dense
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </div>
              </v-col>
              <v-col class="vertical-line" />
              <v-col cols="6" />
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
import { useSpeciesStore } from '@/stores/speciesStore'
import { storeToRefs } from 'pinia'

const panelOpen = ref(0)

const derivedBy = ref('volume')

const speciesStore = useSpeciesStore()
const { speciesList, speciesOptions, speciesGroups, totalSpeciesPercent } =
  storeToRefs(speciesStore)

const species1 = ref<string | null>('PL - Lodgepole')
const species2 = ref<string | null>('AC - Poplar')
const species3 = ref<string | null>('H - Hemlock')
const species4 = ref<string | null>('S - Spruce')
const species5 = ref<string | null>(null)
const species6 = ref<string | null>(null)

const speciesPercent1 = ref<number | null>(30.0)
const speciesPercent2 = ref<number | null>(30.0)
const speciesPercent3 = ref<number | null>(30.0)
const speciesPercent4 = ref<number | null>(10.0)
const speciesPercent5 = ref<number | null>(null)
const speciesPercent6 = ref<number | null>(null)

const speciesGroup1 = ref<string | null>('PL')
const speciesGroup2 = ref<string | null>('AC')
const speciesGroup3 = ref<string | null>('H')
const speciesGroup4 = ref<string | null>('S')

const speciesGroupPercent1 = ref<number | null>(30.0)
const speciesGroupPercent2 = ref<number | null>(30.0)
const speciesGroupPercent3 = ref<number | null>(30.0)
const speciesGroupPercent4 = ref<number | null>(10.0)

const siteSpecies1 = ref<string | null>('PL')
const siteSpecies2 = ref<string | null>('AC')
const siteSpecies3 = ref<string | null>('H')
const siteSpecies4 = ref<string | null>('S')

const clear = () => {}
const confirm = () => {}
</script>

<style scoped>
.vertical-line {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1px;
}

.vertical-line::before {
  content: '';
  display: block;
  border-left: 1px dashed rgba(0, 0, 0, 0.12);
  height: 100%;
}
</style>
