import { post } from '@/services/apiService'
import Code from '@/models/code'

export const code = async (code: Code): Promise<any> => {
  return post(`/codeTables/${code.codeTableName}`, code)
}

export const uploadDcsvProjection = (
  projectionParameters: File,
  inputData: File,
) => {
  const formData = new FormData()
  formData.append('projectionParameters', projectionParameters)
  formData.append('inputData', inputData)

  return post('/projection/dcsv', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export const uploadHcsvProjection = (
  projectionParameters: Record<string, any>,
  layerFile: File,
  polygonFile: File,
) => {
  const formData = new FormData()
  formData.append('projectionParameters', JSON.stringify(projectionParameters))
  formData.append('layerFile', layerFile)
  formData.append('polygonFile', polygonFile)
  return post('/projection/hcsv', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export const uploadScsvProjection = (
  projectionParameters: File,
  polygonInputData: File,
  layerInputData: File,
  historyInputData: File,
  nonVegetationInputData: File,
  otherVegetationInputData: File,
  polygonIdInputData: File,
  speciesInputData: File,
  vriAdjustInputData: File,
) => {
  const formData = new FormData()
  formData.append('projectionParameters', projectionParameters)
  formData.append('polygonInputData', polygonInputData)
  formData.append('layerInputData', layerInputData)
  formData.append('historyInputData', historyInputData)
  formData.append('nonVegetationInputData', nonVegetationInputData)
  formData.append('otherVegetationInputData', otherVegetationInputData)
  formData.append('polygonIdInputData', polygonIdInputData)
  formData.append('speciesInputData', speciesInputData)
  formData.append('vriAdjustInputData', vriAdjustInputData)

  return post('/projection/scsv', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
