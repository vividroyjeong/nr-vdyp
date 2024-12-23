<template>
  <div>
    <AppProgressCircular
      :isShow="isProgressVisible"
      :showMessage="true"
      :message="progressMessage"
      :hasBackground="true"
    />
    <AppMessageDialog
      :dialog="messageDialog.dialog"
      :title="messageDialog.title"
      :message="messageDialog.message"
      :dialogWidth="messageDialog.dialogWidth"
      :btnLabel="messageDialog.btnLabel"
      @update:dialog="(value) => (messageDialog.dialog = value)"
      @close="handleDialogClose"
    />
    <v-form ref="form" @submit.prevent="fileUploadRunModel">
      <v-card class="elevation-4">
        <div class="pl-16 pt-10">
          <v-row>
            <v-col cols="3">
              <v-text-field
                label="Starting Age"
                type="number"
                v-model.number="startingAge"
                :min="NUM_INPUT_LIMITS.STARTING_AGE_MIN"
                :max="NUM_INPUT_LIMITS.STARTING_AGE_MAX"
                :step="NUM_INPUT_LIMITS.STARTING_AGE_STEP"
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
                v-model.number="finishingAge"
                :min="NUM_INPUT_LIMITS.FINISHING_AGE_MIN"
                :max="NUM_INPUT_LIMITS.FINISHING_AGE_MAX"
                :step="NUM_INPUT_LIMITS.FINISHING_AGE_STEP"
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
                v-model.number="ageIncrement"
                :min="NUM_INPUT_LIMITS.AGE_INC_MIN"
                :max="NUM_INPUT_LIMITS.AGE_INC_MAX"
                :step="NUM_INPUT_LIMITS.AGE_INC_STEP"
                persistent-placeholder
                placeholder=""
                density="compact"
                dense
              ></v-text-field>
            </v-col>
          </v-row>
        </div>
        <div class="pl-16 pr-16 pb-5">
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
              <span class="text-h9">Report Title</span>
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
                  style="max-width: 41% !important"
                ></v-text-field>
              </v-col>
            </v-row>
          </div>
          <div class="ml-4 mt-10">
            <div class="ml-n4 mt-n5">
              <span class="text-h7">Attachments</span>
            </div>
            <v-row class="mb-n10">
              <v-col cols="5">
                <v-file-input
                  label="Layer File"
                  v-model="layerFile"
                  show-size
                  chips
                  clearable
                  flat
                  persistent-placeholder
                  placeholder="Select Eco Zone"
                  density="compact"
                  accept=".csv"
                />
              </v-col>
              <v-col class="col-space-3" />
              <v-col cols="5">
                <v-file-input
                  label="Polygon File"
                  v-model="polygonFile"
                  show-size
                  chips
                  clearable
                  density="compact"
                  accept=".csv"
                />
              </v-col>
            </v-row>
          </div>
        </div>
        <v-card class="mt-5 pa-4 file-upload-run-model-card" elevation="0">
          <v-card-actions class="pr-0 mr-2">
            <v-spacer></v-spacer>
            <v-btn class="blue-btn ml-2" @click="fileUploadRunModel"
              >Run Model</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-card>
    </v-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { SelectedExecutionOptionsEnum } from '@/services/vdyp-api'
import { projectionHcsvPost } from '@/services/apiActions'
import { handleApiError } from '@/services/apiErrorHandler'
import AppMessageDialog from '@/components/common/AppMessageDialog.vue'
import AppProgressCircular from '@/components/common/AppProgressCircular.vue'
import {
  volumeReportedOptions,
  includeInReportOptions,
  projectionTypeOptions,
} from '@/constants/options'
import {
  NUM_INPUT_LIMITS,
  FILE_NAME,
  BUTTON_LABEL,
} from '@/constants/constants'
import {
  MDL_PRM_INPUT_ERR,
  MSG_DIALOG_TITLE,
  FILE_UPLOAD_ERR,
  PROGRESS_MSG,
  SUCESS_MSG,
} from '@/constants/message'
import type { MessageDialog } from '@/interfaces/interfaces'
import { DEFAULT_VALUES } from '@/constants/defaults'
import { FileUploadValidation } from '@/validation/fileUploadValidation'
import { Util } from '@/utils/util'
import { logSuccessMessage } from '@/utils/messageHandler'

const form = ref<HTMLFormElement>()

const isProgressVisible = ref(false)
const progressMessage = ref('')

const fileUploadValidator = new FileUploadValidation()

const startingAge = ref<number | null>(DEFAULT_VALUES.STARTING_AGE)
const finishingAge = ref<number | null>(DEFAULT_VALUES.FINISHING_AGE)
const ageIncrement = ref<number | null>(DEFAULT_VALUES.AGE_INCREMENT)
const volumeReported = ref<string[]>(DEFAULT_VALUES.VOLUME_REPORTED)
const includeInReport = ref<string[]>([])
const projectionType = ref<string | null>(DEFAULT_VALUES.PROJECTION_TYPE)
const reportTitle = ref<string | null>(DEFAULT_VALUES.REPORT_TITLE)

const layerFile = ref<File | null>(null)
const polygonFile = ref<File | null>(null)

const messageDialog = ref<MessageDialog>({
  dialog: false,
  title: '',
  message: '',
})

const validateComparison = (): boolean => {
  if (
    !fileUploadValidator.validateAgeComparison(
      finishingAge.value,
      startingAge.value,
    )
  ) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_INPUT,
      message: MDL_PRM_INPUT_ERR.RPT_VLD_COMP_FNSH_AGE,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  return true
}

const validateRange = (): boolean => {
  if (!fileUploadValidator.validateStartingAgeRange(startingAge.value)) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_INPUT,
      message: MDL_PRM_INPUT_ERR.RPT_VLD_START_AGE_RNG(
        NUM_INPUT_LIMITS.STARTING_AGE_MIN,
        NUM_INPUT_LIMITS.STARTING_AGE_MAX,
      ),
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  if (!fileUploadValidator.validateFinishingAgeRange(finishingAge.value)) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_INPUT,
      message: MDL_PRM_INPUT_ERR.RPT_VLD_START_FNSH_RNG(
        NUM_INPUT_LIMITS.FINISHING_AGE_MIN,
        NUM_INPUT_LIMITS.FINISHING_AGE_MAX,
      ),
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  if (!fileUploadValidator.validateAgeIncrementRange(ageIncrement.value)) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_INPUT,
      message: MDL_PRM_INPUT_ERR.RPT_VLD_AGE_INC_RNG(
        NUM_INPUT_LIMITS.AGE_INC_MIN,
        NUM_INPUT_LIMITS.AGE_INC_MAX,
      ),
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  return true
}

const validateFiles = async () => {
  if (!layerFile.value) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.MISSING_FILE,
      message: FILE_UPLOAD_ERR.LAYER_FILE_MISSING,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  if (!polygonFile.value) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.MISSING_FILE,
      message: FILE_UPLOAD_ERR.POLYGON_FILE_MISSING,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  if (!(await fileUploadValidator.isCSVFile(layerFile.value))) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_FILE,
      message: FILE_UPLOAD_ERR.LAYER_FILE_NOT_CSV_FORMAT,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  if (!(await fileUploadValidator.isCSVFile(polygonFile.value))) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_FILE,
      message: FILE_UPLOAD_ERR.POLYGON_FILE_NOT_CSV_FORMAT,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }

  return true
}

const validateRequiredFields = (): boolean => {
  if (
    !fileUploadValidator.validateRequiredFields(
      startingAge.value,
      finishingAge.value,
      ageIncrement.value,
    )
  ) {
    messageDialog.value = {
      dialog: true,
      title: MSG_DIALOG_TITLE.INVALID_INPUT,
      message: FILE_UPLOAD_ERR.RPT_VLD_REQUIRED_FIELDS,
      btnLabel: BUTTON_LABEL.CONT_EDIT,
    }
    return false
  }
  return true
}

const fileUploadRunModel = async () => {
  try {
    const isValidationSuccessful =
      validateRequiredFields() &&
      validateComparison() &&
      validateRange() &&
      (await validateFiles())

    if (!isValidationSuccessful) {
      return
    }

    if (form.value) {
      form.value.validate()
    } else {
      console.warn('Form reference is null. Validation skipped.')
    }

    isProgressVisible.value = true
    progressMessage.value = PROGRESS_MSG.RUNNING_MODEL

    await Util.delay(1000)

    const formData = new FormData()

    const selectedExecutionOptions = [
      SelectedExecutionOptionsEnum.DoEnableProgressLogging,
      SelectedExecutionOptionsEnum.DoEnableErrorLogging,
      SelectedExecutionOptionsEnum.DoEnableDebugLogging,
    ]

    const projectionParameters = {
      ageStart: startingAge.value,
      ageEnd: finishingAge.value,
      ageIncrement: ageIncrement.value,
      selectedExecutionOptions: selectedExecutionOptions,
    }

    formData.append(
      'projectionParameters',
      new Blob([JSON.stringify(projectionParameters)], {
        type: 'application/json',
      }),
    )
    formData.append('polygonInputData', polygonFile.value as Blob)
    formData.append('layersInputData', layerFile.value as Blob)

    const result = await projectionHcsvPost(formData, false)

    const url = window.URL.createObjectURL(new Blob([result]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', FILE_NAME.PROJECTION_RESULT_ZIP)
    document.body.appendChild(link)
    link.click()
    link.remove()

    logSuccessMessage(SUCESS_MSG.FILE_UPLOAD_RUN_RESULT)
  } catch (error) {
    handleApiError(error, FILE_UPLOAD_ERR.FAIL_RUN_MODEL)
  } finally {
    isProgressVisible.value = false
  }
}

const handleDialogClose = () => {}
</script>
<style scoped>
.file-upload-run-model-card {
  padding-bottom: 16px !important;
  background-color: #f6f6f6;
  border-top: 1px solid #0000001f;
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
  border-bottom-left-radius: 3px;
  border-bottom-right-radius: 3px;
  display: flex;
  justify-content: end;
  align-items: end;
  text-align: end;
}
</style>
