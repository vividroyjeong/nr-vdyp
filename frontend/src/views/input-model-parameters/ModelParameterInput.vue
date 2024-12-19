<template>
  <AppProgressCircular
    :isShow="isProgressVisible"
    :showMessage="true"
    :message="progressMessage"
    :hasBackground="true"
  />
  <v-container fluid>
    <div class="top-project-year mt-3">
      <h1 class="top-project">Projects</h1>
      <span class="top-year">Year: 2024/2025</span>
    </div>

    <template v-if="modelSelection === MODEL_SELECTION.INPUT_MODEL_PARAMETERS">
      <div class="hr-line-2 mb-4"></div>
      <v-spacer class="space"></v-spacer>
      <div class="mt-n1 mb-3">
        <h3>Model Parameter Selection</h3>
      </div>
    </template>

    <v-card class="pa-4 job-type-sel-card" elevation="0">
      <JobTypeSelection />
    </v-card>

    <v-spacer class="space"></v-spacer>
    <div class="hr-line mb-5"></div>
    <v-spacer class="space"></v-spacer>

    <template v-if="modelSelection === MODEL_SELECTION.INPUT_MODEL_PARAMETERS">
      <v-tabs
        v-model="currentTab"
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
      <v-tabs-window v-model="currentTab">
        <v-tabs-window-item
          v-for="(tab, index) in tabs"
          :key="index"
          :value="index"
        >
          <component :is="tab.component"></component>
        </v-tabs-window-item>
      </v-tabs-window>

      <template v-if="isModelParameterPanelsVisible">
        <v-spacer class="space"></v-spacer>
        <SiteInfo />
        <v-spacer class="space"></v-spacer>
        <StandDensity />
        <v-spacer class="space"></v-spacer>
        <ReportInfo />

        <v-card class="mt-5 pa-4 run-model-card" elevation="0">
          <v-card-actions class="pr-0 mr-2">
            <v-spacer></v-spacer>
            <v-btn
              class="blue-btn ml-2"
              :disabled="!modelParameterStore.runModelEnabled"
              @click="runModel"
              >Run Model</v-btn
            >
          </v-card-actions>
        </v-card>
      </template>
    </template>
    <template v-else>
      <FileUpload />
    </template>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAppStore } from '@/stores/appStore'
import { useModelParameterStore } from '@/stores/modelParameterStore'
import { storeToRefs } from 'pinia'
import JobTypeSelection from '@/components/JobTypeSelection.vue'
import ModelParameterSelection from '@/views/input-model-parameters/ModelParameterSelection.vue'
import ModelReport from '@/views/input-model-parameters/ModelReport.vue'
import ViewLogFile from '@/views/input-model-parameters/ViewLogFile.vue'
import ViewErrorMessages from '@/views/input-model-parameters/ViewErrorMessages.vue'
import SiteInfo from '@/components/model-param-selection-panes/SiteInfo.vue'
import StandDensity from '@/components/model-param-selection-panes/StandDensity.vue'
import ReportInfo from '@/components/model-param-selection-panes/ReportInfo.vue'
import FileUpload from '@/views/input-model-parameters/FileUpload.vue'
import AppProgressCircular from '@/components/common/AppProgressCircular.vue'
import {
  MODEL_SELECTION,
  MODEL_PARAM_TAB_NAME,
  INVENTORY_CODES,
  DERIVED_BY,
  DOWNLOAD_FILE_NAME,
} from '@/constants/constants'
import { projectionHcsvPost } from '@/services/apiActions'
import { handleApiError } from '@/services/apiErrorHandler'
import { SelectedExecutionOptionsEnum } from '@/services/vdyp-api'
import { Util } from '@/utils/util'
import { logSuccessMessage } from '@/utils/messageHandler'
import { SUCESS_MSG, FILE_UPLOAD_ERR, PROGRESS_MSG } from '@/constants/message'
import { POLYGON_HEADERS, LAYER_HEADERS } from '@/constants/csvHeaders'
import type { CSVRowType } from '@/types/types'
import { saveAs } from 'file-saver'

const isProgressVisible = ref(false)
const progressMessage = ref('')

const appStore = useAppStore()
const modelParameterStore = useModelParameterStore()

const { currentTab, modelSelection } = storeToRefs(appStore)

const tabs = [
  {
    label: MODEL_PARAM_TAB_NAME.MODEL_PARAM_SELECTION,
    component: ModelParameterSelection,
  },
  { label: MODEL_PARAM_TAB_NAME.MODEL_REPORT, component: ModelReport },
  { label: MODEL_PARAM_TAB_NAME.VIEW_LOG_FILE, component: ViewLogFile },
  {
    label: MODEL_PARAM_TAB_NAME.VIEW_ERROR_MESSAGES,
    component: ViewErrorMessages,
  },
]

const isModelParameterPanelsVisible = computed(() => {
  return (
    modelSelection.value === MODEL_SELECTION.INPUT_MODEL_PARAMETERS &&
    currentTab.value === 0
  )
})

onMounted(() => {
  modelParameterStore.setDefaultValues()
})

const createCSVFiles = () => {
  const derivedByCode =
    modelParameterStore.derivedBy === DERIVED_BY.VOLUME
      ? INVENTORY_CODES.FIP
      : modelParameterStore.derivedBy === DERIVED_BY.BASAL_AREA
        ? INVENTORY_CODES.VRI
        : ''

  const polygonData: CSVRowType = [
    POLYGON_HEADERS,
    [
      'FEATURE_ID',
      'MAP_ID',
      'POLYGON_NUMBER',
      'ORG_UNIT',
      'TSA_NAME',
      'TFL_NAME',
      derivedByCode || '', // 'INVENTORY_STANDARD_CODE'
      'TSA_NUMBER',
      'SHRUB_HEIGHT',
      'SHRUB_CROWN_CLOSURE',
      'SHRUB_COVER_PATTERN',
      'HERB_COVER_TYPE_CODE',
      'HERB_COVER_PCT',
      'HERB_COVER_PATTERN_CODE',
      'BRYOID_COVER_PCT',
      modelParameterStore.becZone || '', // 'BEC_ZONE_CODE'
      modelParameterStore.ecoZone || '', // 'CFS_ECOZONE'
      modelParameterStore.percentStockableArea || '', // 'PRE_DISTURBANCE_STOCKABILITY'
      'YIELD_FACTOR',
      'NON_PRODUCTIVE_DESCRIPTOR_CD',
      'BCLCS_LEVEL1_CODE',
      'BCLCS_LEVEL2_CODE',
      'BCLCS_LEVEL3_CODE',
      'BCLCS_LEVEL4_CODE',
      'BCLCS_LEVEL5_CODE',
      'PHOTO_ESTIMATION_BASE_YEAR',
      'REFERENCE_YEAR',
      'PCT_DEAD',
      'NON_VEG_COVER_TYPE_1',
      'NON_VEG_COVER_PCT_1',
      'NON_VEG_COVER_PATTERN_1',
      'NON_VEG_COVER_TYPE_2',
      'NON_VEG_COVER_PCT_2',
      'NON_VEG_COVER_PATTERN_2',
      'NON_VEG_COVER_TYPE_3',
      'NON_VEG_COVER_PCT_3',
      'NON_VEG_COVER_PATTERN_3',
      'LAND_COVER_CLASS_CD_1',
      'LAND_COVER_PCT_1',
      'LAND_COVER_CLASS_CD_2',
      'LAND_COVER_PCT_2',
      'LAND_COVER_CLASS_CD_3',
      'LAND_COVER_PCT_3',
    ],
  ]

  const layerData: CSVRowType = [
    LAYER_HEADERS,
    [
      'FEATURE_ID',
      'TREE_COVER_LAYER_ESTIMATED_ID',
      'MAP_ID',
      'POLYGON_NUMBER',
      'LAYER_LEVEL_CODE',
      'VDYP7_LAYER_CD',
      'LAYER_STOCKABILITY',
      'FOREST_COVER_RANK_CODE',
      'NON_FOREST_DESCRIPTOR_CODE',
      modelParameterStore.highestPercentSpecies || '', // 'EST_SITE_INDEX_SPECIES_CD'
      modelParameterStore.bha50SiteIndex || '', // 'ESTIMATED_SITE_INDEX'
      '', // 'CROWN_CLOSURE'
      '', // 'BASAL_AREA_75'
      '', // 'STEMS_PER_HA_75'
      modelParameterStore.speciesList[0].species || '', // 'SPECIES_CD_1'
      modelParameterStore.speciesList[0].percent || '', // 'SPECIES_PCT_1'
      modelParameterStore.speciesList[1].species || '', // 'SPECIES_CD_2'
      modelParameterStore.speciesList[1].percent || '', // 'SPECIES_PCT_2'
      modelParameterStore.speciesList[2].species || '', // 'SPECIES_CD_3'
      modelParameterStore.speciesList[2].percent || '', // 'SPECIES_PCT_3'
      modelParameterStore.speciesList[3].species || '', // 'SPECIES_CD_4'
      modelParameterStore.speciesList[3].percent || '', // 'SPECIES_PCT_4'
      modelParameterStore.speciesList[4].species || '', // 'SPECIES_CD_5'
      modelParameterStore.speciesList[4].percent || '', // 'SPECIES_PCT_5'
      modelParameterStore.speciesList[5].species || '', // 'SPECIES_CD_6'
      modelParameterStore.speciesList[5].percent || '', // 'SPECIES_PCT_6'
      '', // 'EST_AGE_SPP1',
      '', // 'EST_HEIGHT_SPP1',
      '', // 'EST_AGE_SPP2',
      '', // 'EST_HEIGHT_SPP2',
      '', // 'ADJ_IND',
      '', // 'LOREY_HEIGHT_75',
      '', // 'BASAL_AREA_125',
      '', // 'WS_VOL_PER_HA_75',
      '', // 'WS_VOL_PER_HA_125',
      '', // 'CU_VOL_PER_HA_125',
      '', // 'D_VOL_PER_HA_125',
      '', // 'DW_VOL_PER_HA_125',
    ],
  ]

  const convertToCSV = (data: CSVRowType): string => {
    return data
      .map((row) =>
        row
          .map((value) =>
            value !== null && value !== undefined ? String(value) : '',
          )
          .join(','),
      )
      .join('\n')
  }

  const polygonCSV = convertToCSV(polygonData)
  const layerCSV = convertToCSV(layerData)

  const blobPolygon = new Blob([polygonCSV], {
    type: 'text/csv;charset=utf-8;',
  })
  const blobLayer = new Blob([layerCSV], { type: 'text/csv;charset=utf-8;' })

  // saveAs(blobPolygon, 'VDYP7_INPUT_POLY.csv')
  // saveAs(blobLayer, 'VDYP7_INPUT_LAYER.csv')

  return { blobPolygon, blobLayer }
}

const runModel = async () => {
  try {
    isProgressVisible.value = true
    progressMessage.value = PROGRESS_MSG.RUNNING_MODEL

    await Util.delay(1000)

    const modelParameterStore = useModelParameterStore()

    const { blobPolygon, blobLayer } = createCSVFiles()

    const formData = new FormData()

    const selectedExecutionOptions = [
      SelectedExecutionOptionsEnum.DoEnableProgressLogging,
      SelectedExecutionOptionsEnum.DoEnableErrorLogging,
      SelectedExecutionOptionsEnum.DoEnableDebugLogging,
    ]

    if (modelParameterStore.incSecondaryHeight) {
      selectedExecutionOptions.push(
        SelectedExecutionOptionsEnum.DoIncludeSecondarySpeciesDominantHeightInYieldTable,
      )
    }

    const projectionParameters = {
      ageStart: modelParameterStore.startingAge,
      ageEnd: modelParameterStore.finishingAge,
      ageIncrement: modelParameterStore.ageIncrement,
      selectedExecutionOptions: selectedExecutionOptions,
    }

    formData.append(
      'projectionParameters',
      new Blob([JSON.stringify(projectionParameters)], {
        type: 'application/json',
      }),
    )

    formData.append('polygonInputData', blobPolygon, 'VDYP7_INPUT_POLY.csv')
    formData.append('layersInputData', blobLayer, 'VDYP7_INPUT_LAYER.csv')

    const result = await projectionHcsvPost(formData, false)

    const url = window.URL.createObjectURL(new Blob([result]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', DOWNLOAD_FILE_NAME.MULTI_POLYGON_OUTPUT)
    document.body.appendChild(link)
    link.click()
    link.remove()

    logSuccessMessage(SUCESS_MSG.FILE_UPLOAD_RUN_MODEL_RESULT)
  } catch (error) {
    handleApiError(error, FILE_UPLOAD_ERR.FAIL_RUN_MODEL)
  } finally {
    isProgressVisible.value = false
  }
}
</script>

<style scoped>
.space {
  margin-top: 10px;
}

.run-model-card {
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
}

.job-type-sel-card {
  padding-bottom: 16px !important;
  background-color: #f6f6f6;
  border-top: 1px solid #0000001f;
  border-bottom: 1px solid #0000001f;
  border-radius: 0px;
}
</style>
