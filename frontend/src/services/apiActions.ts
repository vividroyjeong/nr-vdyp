import apiClient from '@/services/apiClient'
import type { ProjectionHcsvBody } from '@/services/vdyp-api/models'

/**
 * Call projectionHcsvPost with properly formatted body.
 * @param {object} projectionParameters - Parameters for the projection.
 * @param {File} layerFile - The layer file to upload.
 * @param {File} polygonFile - The polygon file to upload.
 * @returns {Promise<any>} API response.
 */
export const projectionHcsvPost = async (
  projectionParameters: object,
  layerFile: File,
  polygonFile: File,
): Promise<any> => {
  if (!projectionParameters || !layerFile || !polygonFile) {
    throw new Error('Invalid input for projection parameters or files.')
  }

  const body: ProjectionHcsvBody = {
    projectionParameters,
    layerInputData: layerFile,
    polygonInputData: polygonFile,
  }

  return await apiClient.projectionHcsvPost(body)
}

/**
 * Call helpGet to retrieve parameter details.
 * @returns {Promise<any>} API response containing parameter details.
 */
export const helpGet = async (): Promise<any> => {
  try {
    const response = await apiClient.helpGet()
    return response.data
  } catch (error) {
    console.error('Error fetching help details:', error)
    throw error
  }
}
