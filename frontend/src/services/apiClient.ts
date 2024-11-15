import { DefaultApi } from '@/services/vdyp-api/'
import axiosInstance from '@/services/axiosInstance'

const apiInstance = new DefaultApi(undefined, undefined, axiosInstance)

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
   * @returns AxiosResponse<void>
   */
  projectionHcsvPost: (body?: any, options?: any) => {
    return apiInstance.projectionHcsvPost(body, options)
  },

  /**
   * Forecast yields by year using an input file in HCSV format.
   * @param body Request data of type ProjectionDcsvBody
   * @param options Axios request options
   * @returns AxiosResponse<void>
   */
  projectionDcsvPost: (body?: any, options?: any) => {
    return apiInstance.projectionDcsvPost(body, options)
  },

  /**
   * Forecast yields by year using input files in SCSV format.
   * @param body Request data of type ProjectionScsvBody
   * @param options Axios request options
   * @returns AxiosResponse<void>
   */
  projectionScsvPost: (body?: any, options?: any) => {
    return apiInstance.projectionScsvPost(body, options)
  },
}

export default apiClient
