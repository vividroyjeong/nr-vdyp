import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  MODEL_TYPE,
  ENGINE_VERSION,
  MODEL_PARAM_TAB_IDX,
} from '@/constants/constants'

export const useAppStore = defineStore('appStore', () => {
  // Model Parameter Selection bar
  const modelType = ref<string>(MODEL_TYPE.FILE_UPLOAD)
  const engineVersion = ref<string>(ENGINE_VERSION.VDYP8)
  const jobId = ref<string>('')

  // Input Model Parameter tabs
  const currentTab = ref<number>(MODEL_PARAM_TAB_IDX.MODEL_PARAM_SELECTION)

  return {
    // Model Parameter Selection bar
    modelType,
    engineVersion,
    jobId,
    // Input Model Parameter tabs
    currentTab,
  }
})
