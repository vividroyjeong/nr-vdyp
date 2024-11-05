import { defineStore } from 'pinia'
import { ref } from 'vue'
import { MODEL_SELECTION, ENGINE_VERSION } from '@/constants/constants'

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
