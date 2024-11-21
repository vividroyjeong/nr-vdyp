import { VdypApi } from '@/services/vdyp-api/'
import axiosInstance from '@/services/axiosInstance'
import type { AxiosResponse } from 'axios'
import type { ProjectionHcsvBody } from '@/services/vdyp-api/models'

const apiInstance = new VdypApi(undefined, undefined, axiosInstance)

export const apiClient = {
  /**
   * Returns a detailed description of the parameters related to Projection.
   * @param options Axios request options
   * @returns AxiosResponse<Array<ParameterDetailsMessage>>
   */
  helpGet: (options?: any) => {
    return apiInstance.helpGet(options)
  },

  /**
   * Forecast yields by year using an input file in HCSV format.
   * @param body Request data of type ProjectionHcsvBody
   * @param options Axios request options
   * @returns AxiosResponse<Blob>
   */
  projectionHcsvPost: async (
    body?: ProjectionHcsvBody,
    options?: any,
  ): Promise<AxiosResponse<Blob>> => {
    return apiInstance.projectionHcsvPost(body, options)
  },
}

export default apiClient
