import apiClient from '@/services/apiClient'
import type {
  ProjectionHcsvPostRequest,
  ParameterDetailsMessage,
  RootResource,
} from '@/services/vdyp-api'

export const helpGet = async (): Promise<ParameterDetailsMessage[]> => {
  try {
    const response = await apiClient.helpGet()
    return response.data
  } catch (error) {
    console.error('Error fetching help details:', error)
    throw error
  }
}

export const projectionHcsvPost = async (
  body: ProjectionHcsvPostRequest,
): Promise<Blob> => {
  try {
    const response = await apiClient.projectionHcsvPost(body)
    return response.data
  } catch (error) {
    console.error('Error running projection:', error)
    throw error
  }
}

export const rootGet = async (): Promise<RootResource> => {
  try {
    const response = await apiClient.rootGet()
    return response.data
  } catch (error) {
    console.error('Error fetching root details:', error)
    throw error
  }
}
