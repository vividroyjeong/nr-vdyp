import globalAxios from 'axios'
import type { AxiosResponse, AxiosInstance, AxiosRequestConfig } from 'axios'
import { Configuration } from '../configuration'
import { BASE_PATH, BaseAPI } from '../base'
import type { RequestArgs } from '../base'
import type { FileUpload, Parameters } from '../models'
import { env } from '@/env'

export const RunSCSVProjectionApiAxiosParamCreator = function (
  configuration?: Configuration,
) {
  return {
    projectionScsvPostForm: async (
      polygonInputData?: FileUpload,
      layersInputData?: FileUpload,
      historyInputData?: FileUpload,
      nonVegetationInputData?: FileUpload,
      otherVegetationInputData?: FileUpload,
      polygonIdInputData?: FileUpload,
      speciesInputData?: FileUpload,
      vriAdjustInputData?: FileUpload,
      projectionParameters?: Parameters,
      trialRun?: boolean,
      options: AxiosRequestConfig = {},
    ): Promise<RequestArgs> => {
      const localVarPath = `/api/v8/projection/scsv`
      const localVarUrlObj = new URL(localVarPath, env.VITE_API_URL)
      let baseOptions
      if (configuration) {
        baseOptions = configuration.baseOptions
      }
      const localVarRequestOptions: AxiosRequestConfig = {
        method: 'POST',
        ...baseOptions,
        ...options,
      }
      const localVarHeaderParameter = {} as any
      const localVarQueryParameter = {} as any
      const localVarFormParams = new FormData()

      if (trialRun !== undefined) {
        localVarQueryParameter['trialRun'] = trialRun
      }

      if (polygonInputData !== undefined) {
        localVarFormParams.append('polygonInputData', polygonInputData as any)
      }

      if (layersInputData !== undefined) {
        localVarFormParams.append('layersInputData', layersInputData as any)
      }

      if (historyInputData !== undefined) {
        localVarFormParams.append('historyInputData', historyInputData as any)
      }

      if (nonVegetationInputData !== undefined) {
        localVarFormParams.append(
          'nonVegetationInputData',
          nonVegetationInputData as any,
        )
      }

      if (otherVegetationInputData !== undefined) {
        localVarFormParams.append(
          'otherVegetationInputData',
          otherVegetationInputData as any,
        )
      }

      if (polygonIdInputData !== undefined) {
        localVarFormParams.append(
          'polygonIdInputData',
          polygonIdInputData as any,
        )
      }

      if (speciesInputData !== undefined) {
        localVarFormParams.append('speciesInputData', speciesInputData as any)
      }

      if (vriAdjustInputData !== undefined) {
        localVarFormParams.append(
          'vriAdjustInputData',
          vriAdjustInputData as any,
        )
      }

      if (projectionParameters !== undefined) {
        localVarFormParams.append(
          'projectionParameters',
          projectionParameters as any,
        )
      }

      localVarHeaderParameter['Content-Type'] = 'multipart/form-data'
      const query = new URLSearchParams(localVarUrlObj.search)
      for (const key in localVarQueryParameter) {
        query.set(key, localVarQueryParameter[key])
      }
      for (const key in options.params) {
        query.set(key, options.params[key])
      }
      localVarUrlObj.search = new URLSearchParams(query).toString()
      const headersFromBaseOptions =
        baseOptions && baseOptions.headers ? baseOptions.headers : {}
      localVarRequestOptions.headers = {
        ...localVarHeaderParameter,
        ...headersFromBaseOptions,
        ...options.headers,
      }
      localVarRequestOptions.data = localVarFormParams

      return {
        url:
          localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
        options: localVarRequestOptions,
      }
    },
  }
}

export const RunSCSVProjectionApiFp = function (configuration?: Configuration) {
  return {
    async projectionScsvPostForm(
      polygonInputData?: FileUpload,
      layersInputData?: FileUpload,
      historyInputData?: FileUpload,
      nonVegetationInputData?: FileUpload,
      otherVegetationInputData?: FileUpload,
      polygonIdInputData?: FileUpload,
      speciesInputData?: FileUpload,
      vriAdjustInputData?: FileUpload,
      projectionParameters?: Parameters,
      trialRun?: boolean,
      options?: AxiosRequestConfig,
    ): Promise<
      (axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>
    > {
      const localVarAxiosArgs = await RunSCSVProjectionApiAxiosParamCreator(
        configuration,
      ).projectionScsvPostForm(
        polygonInputData,
        layersInputData,
        historyInputData,
        nonVegetationInputData,
        otherVegetationInputData,
        polygonIdInputData,
        speciesInputData,
        vriAdjustInputData,
        projectionParameters,
        trialRun,
        options,
      )
      return (
        axios: AxiosInstance = globalAxios,
        basePath: string = BASE_PATH,
      ) => {
        const axiosRequestArgs: AxiosRequestConfig = {
          ...localVarAxiosArgs.options,
          url: basePath + localVarAxiosArgs.url,
        }
        return axios.request(axiosRequestArgs)
      }
    },
  }
}

export const RunSCSVProjectionApiFactory = function (
  configuration?: Configuration,
  basePath?: string,
  axios?: AxiosInstance,
) {
  return {
    async projectionScsvPostForm(
      polygonInputData?: FileUpload,
      layersInputData?: FileUpload,
      historyInputData?: FileUpload,
      nonVegetationInputData?: FileUpload,
      otherVegetationInputData?: FileUpload,
      polygonIdInputData?: FileUpload,
      speciesInputData?: FileUpload,
      vriAdjustInputData?: FileUpload,
      projectionParameters?: Parameters,
      trialRun?: boolean,
      options?: AxiosRequestConfig,
    ): Promise<AxiosResponse<void>> {
      return RunSCSVProjectionApiFp(configuration)
        .projectionScsvPostForm(
          polygonInputData,
          layersInputData,
          historyInputData,
          nonVegetationInputData,
          otherVegetationInputData,
          polygonIdInputData,
          speciesInputData,
          vriAdjustInputData,
          projectionParameters,
          trialRun,
          options,
        )
        .then((request) => request(axios, basePath))
    },
  }
}

export class RunSCSVProjectionApi extends BaseAPI {
  public async projectionScsvPostForm(
    polygonInputData?: FileUpload,
    layersInputData?: FileUpload,
    historyInputData?: FileUpload,
    nonVegetationInputData?: FileUpload,
    otherVegetationInputData?: FileUpload,
    polygonIdInputData?: FileUpload,
    speciesInputData?: FileUpload,
    vriAdjustInputData?: FileUpload,
    projectionParameters?: Parameters,
    trialRun?: boolean,
    options?: AxiosRequestConfig,
  ): Promise<AxiosResponse<void>> {
    return RunSCSVProjectionApiFp(this.configuration)
      .projectionScsvPostForm(
        polygonInputData,
        layersInputData,
        historyInputData,
        nonVegetationInputData,
        otherVegetationInputData,
        polygonIdInputData,
        speciesInputData,
        vriAdjustInputData,
        projectionParameters,
        trialRun,
        options,
      )
      .then((request) => request(this.axios, this.basePath))
  }
}
