<template>
  <v-card class="elevation-4">
    <v-expansion-panels v-model="panelOpenStates.reportInfo">
      <v-expansion-panel hide-actions>
        <v-expansion-panel-title>
          <v-row no-gutters class="expander-header">
            <!-- Place an arrow icon to the left of the title -->
            <v-col cols="auto" class="expansion-panel-icon-col">
              <v-icon class="expansion-panel-icon">{{
                panelOpenStates.reportInfo === PANEL.OPEN
                  ? 'mdi-chevron-up'
                  : 'mdi-chevron-down'
              }}</v-icon>
            </v-col>
            <v-col>
              <span class="text-h6">Report Information</span>
            </v-col>
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="expansion-panel-text mt-n2">
          <v-form ref="form">
            <div>
              <v-row>
                <v-col cols="3">
                  <v-text-field
                    label="Starting Age"
                    type="number"
                    v-model="startingAge"
                    min="0"
                    max="500"
                    step="10"
                    persistent-placeholder
                    placeholder=""
                    density="compact"
                    dense
                  ></v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="3">
                  <v-text-field
                    label="Finishing Age"
                    type="number"
                    v-model="finishingAge"
                    min="1"
                    max="450"
                    step="10"
                    persistent-placeholder
                    placeholder=""
                    density="compact"
                    dense
                  ></v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="3">
                  <v-text-field
                    label="Age Increment"
                    type="number"
                    v-model="ageIncrement"
                    min="1"
                    max="350"
                    step="5"
                    persistent-placeholder
                    placeholder=""
                    density="compact"
                    dense
                  ></v-text-field>
                </v-col>
              </v-row>
            </div>
            <div class="ml-4 mt-5">
              <div class="ml-n4 mt-n5">
                <span class="text-h7">Volume Reported</span>
              </div>
              <v-row class="ml-n6">
                <v-col cols="12" style="padding-top: 0px">
                  <v-row>
                    <v-col
                      v-for="(option, index) in volumeReportedOptions"
                      :key="index"
                      :style="{ 'max-width': index < 4 ? '20%' : 'auto' }"
                    >
                      <v-checkbox
                        v-model="volumeReported"
                        :label="option.label"
                        :value="option.value"
                        hide-details
                      ></v-checkbox>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
            </div>
            <div class="ml-4 mt-5">
              <div class="ml-n4 mt-n5">
                <span class="text-h7">Include in Report</span>
              </div>
              <v-row class="ml-n6">
                <v-col cols="12" style="padding-top: 0px">
                  <v-row>
                    <v-col
                      v-for="(option, index) in includeInReportOptions"
                      :key="index"
                      :style="{ 'max-width': index < 4 ? '20%' : 'auto' }"
                    >
                      <v-checkbox
                        v-model="includeInReport"
                        :label="option.label"
                        :value="option.value"
                        hide-details
                      ></v-checkbox>
                    </v-col>

                    <v-col style="max-width: 20% !important">
                      <v-select
                        label="Projection Type"
                        :items="projectionTypeOptions"
                        v-model="projectionType"
                        item-title="label"
                        item-value="value"
                        hide-details="auto"
                        persistent-placeholder
                        placeholder="Select..."
                        density="compact"
                        dense
                        style="max-width: 70% !important"
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
            </div>
            <div class="ml-4 mt-5">
              <div class="ml-n4 mt-n5">
                <span class="text-h7">Report Title</span>
              </div>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    type="string"
                    v-model="reportTitle"
                    hide-details="auto"
                    persistent-placeholder
                    placeholder="Enter a report title..."
                    density="compact"
                    dense
                    style="max-width: 50% !important"
                  ></v-text-field>
                </v-col>
              </v-row>
            </div>
            <div class="mt-5">
              <v-row v-for="(group, index) in speciesGroups" :key="index">
                <v-col cols="3">
                  <v-text-field
                    :label="`Minimum DBH Limit by Species #${index + 1}`"
                    type="string"
                    v-model="group.group"
                    persistent-placeholder
                    placeholder="Select..."
                    density="compact"
                    dense
                    readonly
                  ></v-text-field>
                </v-col>
                <v-col class="col-space-3" />
                <v-col cols="5" class="ma-5">
                  <vue-slider
                    v-model="group.minimumDBHLimit"
                    :data="minimumDBHLimitsOptions"
                    :data-value="'value'"
                    :data-label="'label'"
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
import { ref, computed } from 'vue'
import { useModelParameterStore } from '@/stores/modelParameterStore'
import { useMessageDialogStore } from '@/stores/common/messageDialogStore'
import { storeToRefs } from 'pinia'
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'
import {
  volumeReportedOptions,
  includeInReportOptions,
  projectionTypeOptions,
  minimumDBHLimitsOptions,
} from '@/constants/options'
import {
  PANEL,
  DEFAULT_VALUES,
  MINIMUM_DBH_LIMITS,
} from '@/constants/constants'

const form = ref<HTMLFormElement>()

const modelParameterStore = useModelParameterStore()
const messageDialogStore = useMessageDialogStore()

const {
  panelOpenStates,
  speciesGroups,
  startingAge,
  finishingAge,
  ageIncrement,
  volumeReported,
  includeInReport,
  projectionType,
  reportTitle,
} = storeToRefs(modelParameterStore)

const panelName = 'reportInfo'
const isConfirmEnabled = computed(
  () => modelParameterStore.panelState[panelName].editable,
)
const isConfirmed = computed(
  () => modelParameterStore.panelState[panelName].confirmed,
)

const clear = () => {
  startingAge.value = null
  finishingAge.value = null
  ageIncrement.value = null
  volumeReported.value = []
  includeInReport.value = []
  reportTitle.value = null

  projectionType.value = DEFAULT_VALUES.PROJECTION_TYPE
  speciesGroups.value = speciesGroups.value.map((group) => ({
    ...group,
    minimumDBHLimit: MINIMUM_DBH_LIMITS.CM4_0,
  }))
}
// Validation by comparing entered values
const validateComparison = (): boolean => {
  if (finishingAge.value !== null && startingAge.value !== null) {
    if (finishingAge.value < startingAge.value) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Finish Age' must be at least as great as the 'Start Age'.",
        { width: 400 },
      )
      return false
    }
  }
  return true
}

// Validation to check the range of input values
const validateRange = (): boolean => {
  if (startingAge.value !== null) {
    if (startingAge.value < 0 || startingAge.value > 500) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Starting Age' must range from 0 and 500.",
        { width: 400 },
      )
      return false
    }
  }

  if (finishingAge.value !== null) {
    if (finishingAge.value < 1 || finishingAge.value > 450) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Finishing Age' must range from 1 and 450.",
        { width: 400 },
      )
      return false
    }
  }

  if (ageIncrement.value !== null) {
    if (ageIncrement.value < 1 || ageIncrement.value > 350) {
      messageDialogStore.openDialog(
        'Invalid Input!',
        "'Age Increment' must range from 1 and 350.",
        { width: 400 },
      )
      return false
    }
  }

  return true
}
const onConfirm = () => {
  const isComparisonValid = validateComparison()
  const isRangeValid = validateRange()

  if (isComparisonValid && isRangeValid) {
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
