import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useInputModelParamTabStore = defineStore(
  'inputModelParamTabStore',
  () => {
    const currentTab = ref(0) // Default tab is "Model Parameter Selection"

    return {
      currentTab,
    }
  },
)
