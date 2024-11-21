import apiClient from '@/services/apiClient'
import type { ProjectionHcsvBody } from '@/services/vdyp-api/models'
import { useProjectionStore } from '@/stores/projectionStore'
import { StatusCodes } from 'http-status-codes'
import type { Parameters } from '@/services/vdyp-api/models/parameters'

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

/**
 * Call projectionHcsvPost with properly formatted body.
 * @param {object} projectionParameters - Parameters for the projection.
 * @param {File} layerFile - The layer file to upload.
 * @param {File} polygonFile - The polygon file to upload.
 * @returns {Promise<any>} API response.
 */
export const projectionHcsvPost = async (
  parameters: Parameters,
  layerFile: File,
  polygonFile: File,
): Promise<Blob> => {
  if (!parameters || !layerFile || !polygonFile) {
    throw new Error('Invalid input for projection parameters or files.')
  }

  const body: ProjectionHcsvBody = {
    projectionParameters: {
      ageStart: undefined,
      ageEnd: undefined,
      ageIncrement: undefined,
    },
    layerInputData: undefined,
    polygonInputData: undefined,
  }

  const response = await apiClient.projectionHcsvPost(body)

  if (response.status === StatusCodes.CREATED) {
    return response.data
  } else {
    throw new Error(`Unexpected status code: ${response.status}`)
  }
}
