<template>
  <v-container fluid>
    <div class="top-project-year">
      <h1 class="top-project">Projects</h1>
      <span class="top-year">Year: 2024/2025</span>
    </div>
    <div>
      <h3>Model Parameter Selection</h3>
    </div>

    <v-card
      class="pa-4"
      elevation="0"
      style="
        padding-bottom: 16px !important;
        background-color: #f6f6f6;
        border-top: 1px solid #0000001f;
        border-bottom: 1px solid #0000001f;
        border-radius: 0px;
      "
    >
      <JobTypeSelection />
    </v-card>
    <v-spacer class="space"></v-spacer>
    <div class="hr-line"></div>
    <v-spacer class="space"></v-spacer>
    <v-tabs
      v-model="tabStore.currentTab"
      :hideSlider="true"
      :centerActive="true"
      :showArrows="true"
      height="60px"
    >
      <v-tab
        v-for="(tab, index) in tabs"
        :key="index"
        :class="{ 'first-tab': index === 0 }"
        >{{ tab.label }}</v-tab
      >
    </v-tabs>
    <v-tabs-window v-model="tabStore.currentTab">
      <v-tabs-window-item
        v-for="(tab, index) in tabs"
        :key="index"
        :value="index"
      >
        <component :is="tab.component"></component>
      </v-tabs-window-item>
    </v-tabs-window>

    <template v-if="tabStore.currentTab === 0">
      <v-spacer class="space"></v-spacer>
      <SiteInfo />
      <v-spacer class="space"></v-spacer>
      <StandDensity />
      <v-spacer class="space"></v-spacer>
      <AdditionalStandAttributes />
      <v-spacer class="space"></v-spacer>
      <ReportInfo />

      <v-card
        class="mt-5 pa-4"
        elevation="0"
        style="
          padding-bottom: 16px !important;
          background-color: #f6f6f6;
          border: 1px solid #0000001f;
          border-top-left-radius: 0px;
          border-top-right-radius: 0px;
          border-bottom-left-radius: 10px;
          border-bottom-right-radius: 10px;
          display: flex;
          justify-content: end;
          align-items: end;
          text-align: end;
        "
      >
        <v-card-actions class="pr-0">
          <v-spacer></v-spacer>
          <v-btn class="white-btn mr-3" @click="cancel">Cancel</v-btn>
          <v-btn
            class="blue-btn mr-2"
            :disabled="!modelParameterStore.runModelEnabled"
            @click="runModel"
            >Run Model</v-btn
          >
        </v-card-actions>
      </v-card>
    </template>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useInputModelParamTabStore } from '@/stores/inputModelParamTabStore'
import { useModelParameterStore } from '@/stores/modelParameterStore'

import JobTypeSelection from '@/components/JobTypeSelection.vue'

import ModelParameterSelection from '@/views/input-model-parameters/ModelParameterSelection.vue'
import Results from '@/views/input-model-parameters/SpeciesResults.vue'
import ViewLogFile from '@/views/input-model-parameters/ViewLogFile.vue'
import ViewErrorMessages from '@/views/input-model-parameters/ViewErrorMessages.vue'

import SiteInfo from '@/components/model-param-selection-panes/SiteInfo.vue'
import StandDensity from '@/components/model-param-selection-panes/StandDensity.vue'
import AdditionalStandAttributes from '@/components/model-param-selection-panes/AdditionalStandAttributes.vue'
import ReportInfo from '@/components/model-param-selection-panes/ReportInfo.vue'

const tabStore = useInputModelParamTabStore()
const modelParameterStore = useModelParameterStore()

const tabs = [
  { label: 'Model Parameter Selection', component: ModelParameterSelection },
  { label: 'Model Report', component: Results },
  { label: 'View Log File', component: ViewLogFile },
  { label: 'View Error Messages', component: ViewErrorMessages },
]

onMounted(() => {
  modelParameterStore.setDefaultValues()
})

const cancel = () => {}

const runModel = () => {}
</script>

<style>
.space {
  margin-top: 10px;
}
</style>
