import {
  HelpEndpointApi,
  ProjectionEndpointApi,
  RootEndpointApi,
} from '@/services/vdyp-api/'
import axiosInstance from '@/services/axiosInstance'
import type { ProjectionHcsvPostRequest } from '@/services/vdyp-api'
import type { AxiosRequestConfig } from 'axios'

const helpApiInstance = new HelpEndpointApi(undefined, undefined, axiosInstance)
const projectionApiInstance = new ProjectionEndpointApi(
  undefined,
  undefined,
  axiosInstance,
)
const rootApiInstance = new RootEndpointApi(undefined, undefined, axiosInstance)

export const apiClient = {
  helpGet: (options?: AxiosRequestConfig) => {
    return helpApiInstance.v8HelpGet(options)
  },

  projectionHcsvPost: (
    body?: ProjectionHcsvPostRequest,
    options?: AxiosRequestConfig,
  ) => {
    return projectionApiInstance.v8ProjectionHcsvPost(body, options)
  },

  rootGet: (options?: AxiosRequestConfig) => {
    return rootApiInstance.v8Get(options)
  },
}

export default apiClient
