import { defineStore } from 'pinia'
import { ref } from 'vue'
import { MODEL_SELECTION, ENGINE_VERSION } from '@/constants/constants'

// export const MODEL_PARAM_TAB_IDX = Object.freeze({
//   MODEL_PARAM_SELECTION: 0,
//   FILE_UPLOAD: 0,
//   MODEL_REPORT: 1,
//   VIEW_LOG_FILE: 2,
//   VIEW_ERROR_MESSAGES: 3,
// })

export const useAppStore = defineStore('appStore', () => {
  // Model Parameter Selection bar
  const modelType = ref<string>(MODEL_SELECTION.FILE_UPLOAD)
  const engineVersion = ref<string>(ENGINE_VERSION.VDYP8)
  const jobId = ref<string>('')

  // Tabs
  const currentTab = ref<number>(0)

  return {
    // Model Parameter Selection bar
    modelType,
    engineVersion,
    jobId,
    // Tabs
    currentTab,
  }
})
