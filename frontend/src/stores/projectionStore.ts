import { defineStore } from 'pinia'
import { ref } from 'vue'
import JSZip from 'jszip'

export const useProjectionStore = defineStore('projectionStore', () => {
  const errorMessages = ref<string[]>([])
  const logMessages = ref<string[]>([])
  const yieldTable = ref<string>('')

  const processZipResponse = async (zipData: Blob): Promise<void> => {
    try {
      const zip = await JSZip.loadAsync(zipData)
      const requiredFiles = {
        error: 'Output_Error.txt',
        log: 'Output_Log.txt',
        yield: 'Output_YldTbl.csv',
      }

      const errorFile = zip.file(requiredFiles.error)
      const logFile = zip.file(requiredFiles.log)
      const yieldFile = zip.file(requiredFiles.yield)

      if (!errorFile || !logFile || !yieldFile) {
        throw new Error(
          `Missing one or more required files: ${Object.values(requiredFiles)
            .filter((file) => !zip.file(file))
            .join(', ')}`,
        )
      }

      errorMessages.value = (await errorFile.async('string')).split(/\r?\n/)
      logMessages.value = (await logFile.async('string')).split(/\r?\n/)
      yieldTable.value = await yieldFile.async('string')
    } catch (error) {
      console.error('Error processing ZIP file:', error)
      throw new Error('Failed to process ZIP file. Please check the response.')
    }
  }

  return {
    errorMessages,
    logMessages,
    yieldTable,
    processZipResponse,
  }
})
