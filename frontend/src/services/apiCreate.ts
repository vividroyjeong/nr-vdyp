import { post } from '@/services/apiService'
import Code from '@/models/code'

export const code = async (code: Code): Promise<any> => {
  return post(`/codeTables/${code.codeTableName}`, code)
}

export const uploadFilesWithData = (
  layerFile: File,
  polygonFile: File,
  jsonData: Record<string, any>,
) => {
  const formData = new FormData()
  formData.append('layerFile', layerFile)
  formData.append('polygonFile', polygonFile)
  formData.append('jsonData', JSON.stringify(jsonData))

  return post('/projection/dcsv', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
