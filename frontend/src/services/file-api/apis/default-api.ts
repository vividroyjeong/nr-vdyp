/* tslint:disable */
/* eslint-disable */
/**
 * Variable Density Yield Projection
 * API for the Variable Density Yield Projection service
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import globalAxios from 'axios'
import type { AxiosResponse, AxiosInstance, AxiosRequestConfig } from 'axios'
import { Configuration } from '../configuration'
// Some imports not used depending on template conditions
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, BaseAPI, RequiredError } from '../base'
import type { RequestArgs } from '../base'
import type {
  Messages,
  ParameterDetailsMessage,
  ProjectionDcsvBody,
  ProjectionHcsvBody,
  ProjectionScsvBody,
} from '../models'
/**
 * DefaultApi - axios parameter creator
 * @export
 */
export const DefaultApiAxiosParamCreator = function (
  configuration?: Configuration,
) {
  return {
    /**
     *
     * @summary returns a detailed description of the parameters available when executing a projection.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    helpGet: async (options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
      const localVarPath = `/help`
      // use dummy base URL string because the URL constructor only accepts absolute URLs.
      const localVarUrlObj = new URL(localVarPath, 'https://example.com')
      let baseOptions
      if (configuration) {
        baseOptions = configuration.baseOptions
      }
      const localVarRequestOptions: AxiosRequestConfig = {
        method: 'GET',
        ...baseOptions,
        ...options,
      }
      const localVarHeaderParameter = {} as any
      const localVarQueryParameter = {} as any

      // authentication accessCode required
      // oauth required
      if (configuration && configuration.accessToken) {
        const localVarAccessTokenValue =
          typeof configuration.accessToken === 'function'
            ? await configuration.accessToken('accessCode', ['write', 'read'])
            : await configuration.accessToken
        localVarHeaderParameter['Authorization'] =
          'Bearer ' + localVarAccessTokenValue
      }

      const query = new URLSearchParams(localVarUrlObj.search)
      for (const key in localVarQueryParameter) {
        query.set(key, localVarQueryParameter[key])
      }
      for (const key in options.params) {
        query.set(key, options.params[key])
      }
      localVarUrlObj.search = new URLSearchParams(query).toString()
      let headersFromBaseOptions =
        baseOptions && baseOptions.headers ? baseOptions.headers : {}
      localVarRequestOptions.headers = {
        ...localVarHeaderParameter,
        ...headersFromBaseOptions,
        ...options.headers,
      }

      return {
        url:
          localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
        options: localVarRequestOptions,
      }
    },
    /**
     * Run a projection of polygons in the supplied DCSV formatted input file as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionDcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    projectionDcsvPost: async (
      body?: ProjectionDcsvBody,
      options: AxiosRequestConfig = {},
    ): Promise<RequestArgs> => {
      const localVarPath = `/projection/dcsv`
      // use dummy base URL string because the URL constructor only accepts absolute URLs.
      const localVarUrlObj = new URL(localVarPath, 'https://example.com')
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

      // authentication accessCode required
      // oauth required
      if (configuration && configuration.accessToken) {
        const localVarAccessTokenValue =
          typeof configuration.accessToken === 'function'
            ? await configuration.accessToken('accessCode', ['write', 'read'])
            : await configuration.accessToken
        localVarHeaderParameter['Authorization'] =
          'Bearer ' + localVarAccessTokenValue
      }

      localVarHeaderParameter['Content-Type'] = 'application/json'

      const query = new URLSearchParams(localVarUrlObj.search)
      for (const key in localVarQueryParameter) {
        query.set(key, localVarQueryParameter[key])
      }
      for (const key in options.params) {
        query.set(key, options.params[key])
      }
      localVarUrlObj.search = new URLSearchParams(query).toString()
      let headersFromBaseOptions =
        baseOptions && baseOptions.headers ? baseOptions.headers : {}
      localVarRequestOptions.headers = {
        ...localVarHeaderParameter,
        ...headersFromBaseOptions,
        ...options.headers,
      }
      const needsSerialization =
        typeof body !== 'string' ||
        (localVarRequestOptions.headers &&
          localVarRequestOptions.headers['Content-Type'] === 'application/json')
      localVarRequestOptions.data = needsSerialization
        ? JSON.stringify(body !== undefined ? body : {})
        : body || ''

      return {
        url:
          localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
        options: localVarRequestOptions,
      }
    },
    /**
     * Run a projection of polygons in the supplied HCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionHcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    projectionHcsvPost: async (
      body?: ProjectionHcsvBody,
      options: AxiosRequestConfig = {},
    ): Promise<RequestArgs> => {
      const localVarPath = `/projection/hcsv`
      // use dummy base URL string because the URL constructor only accepts absolute URLs.
      const localVarUrlObj = new URL(localVarPath, 'https://example.com')
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

      // authentication accessCode required
      // oauth required
      if (configuration && configuration.accessToken) {
        const localVarAccessTokenValue =
          typeof configuration.accessToken === 'function'
            ? await configuration.accessToken('accessCode', ['write', 'read'])
            : await configuration.accessToken
        localVarHeaderParameter['Authorization'] =
          'Bearer ' + localVarAccessTokenValue
      }

      localVarHeaderParameter['Content-Type'] = 'application/json'

      const query = new URLSearchParams(localVarUrlObj.search)
      for (const key in localVarQueryParameter) {
        query.set(key, localVarQueryParameter[key])
      }
      for (const key in options.params) {
        query.set(key, options.params[key])
      }
      localVarUrlObj.search = new URLSearchParams(query).toString()
      let headersFromBaseOptions =
        baseOptions && baseOptions.headers ? baseOptions.headers : {}
      localVarRequestOptions.headers = {
        ...localVarHeaderParameter,
        ...headersFromBaseOptions,
        ...options.headers,
      }
      const needsSerialization =
        typeof body !== 'string' ||
        (localVarRequestOptions.headers &&
          localVarRequestOptions.headers['Content-Type'] === 'application/json')
      localVarRequestOptions.data = needsSerialization
        ? JSON.stringify(body !== undefined ? body : {})
        : body || ''

      return {
        url:
          localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
        options: localVarRequestOptions,
      }
    },
    /**
     * Run a projection of polygons in the supplied SCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionScsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    projectionScsvPost: async (
      body?: ProjectionScsvBody,
      options: AxiosRequestConfig = {},
    ): Promise<RequestArgs> => {
      const localVarPath = `/projection/scsv`
      // use dummy base URL string because the URL constructor only accepts absolute URLs.
      const localVarUrlObj = new URL(localVarPath, 'https://example.com')
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

      // authentication accessCode required
      // oauth required
      if (configuration && configuration.accessToken) {
        const localVarAccessTokenValue =
          typeof configuration.accessToken === 'function'
            ? await configuration.accessToken('accessCode', ['write', 'read'])
            : await configuration.accessToken
        localVarHeaderParameter['Authorization'] =
          'Bearer ' + localVarAccessTokenValue
      }

      localVarHeaderParameter['Content-Type'] = 'application/json'

      const query = new URLSearchParams(localVarUrlObj.search)
      for (const key in localVarQueryParameter) {
        query.set(key, localVarQueryParameter[key])
      }
      for (const key in options.params) {
        query.set(key, options.params[key])
      }
      localVarUrlObj.search = new URLSearchParams(query).toString()
      let headersFromBaseOptions =
        baseOptions && baseOptions.headers ? baseOptions.headers : {}
      localVarRequestOptions.headers = {
        ...localVarHeaderParameter,
        ...headersFromBaseOptions,
        ...options.headers,
      }
      const needsSerialization =
        typeof body !== 'string' ||
        (localVarRequestOptions.headers &&
          localVarRequestOptions.headers['Content-Type'] === 'application/json')
      localVarRequestOptions.data = needsSerialization
        ? JSON.stringify(body !== undefined ? body : {})
        : body || ''

      return {
        url:
          localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
        options: localVarRequestOptions,
      }
    },
  }
}

/**
 * DefaultApi - functional programming interface
 * @export
 */
export const DefaultApiFp = function (configuration?: Configuration) {
  return {
    /**
     *
     * @summary returns a detailed description of the parameters available when executing a projection.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async helpGet(
      options?: AxiosRequestConfig,
    ): Promise<
      (
        axios?: AxiosInstance,
        basePath?: string,
      ) => Promise<AxiosResponse<Array<ParameterDetailsMessage>>>
    > {
      const localVarAxiosArgs =
        await DefaultApiAxiosParamCreator(configuration).helpGet(options)
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
    /**
     * Run a projection of polygons in the supplied DCSV formatted input file as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionDcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionDcsvPost(
      body?: ProjectionDcsvBody,
      options?: AxiosRequestConfig,
    ): Promise<
      (axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>
    > {
      const localVarAxiosArgs = await DefaultApiAxiosParamCreator(
        configuration,
      ).projectionDcsvPost(body, options)
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
    /**
     * Run a projection of polygons in the supplied HCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionHcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionHcsvPost(
      body?: ProjectionHcsvBody,
      options?: AxiosRequestConfig,
    ): Promise<
      (axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>
    > {
      const localVarAxiosArgs = await DefaultApiAxiosParamCreator(
        configuration,
      ).projectionHcsvPost(body, options)
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
    /**
     * Run a projection of polygons in the supplied SCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionScsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionScsvPost(
      body?: ProjectionScsvBody,
      options?: AxiosRequestConfig,
    ): Promise<
      (axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>
    > {
      const localVarAxiosArgs = await DefaultApiAxiosParamCreator(
        configuration,
      ).projectionScsvPost(body, options)
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

/**
 * DefaultApi - factory interface
 * @export
 */
export const DefaultApiFactory = function (
  configuration?: Configuration,
  basePath?: string,
  axios?: AxiosInstance,
) {
  return {
    /**
     *
     * @summary returns a detailed description of the parameters available when executing a projection.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async helpGet(
      options?: AxiosRequestConfig,
    ): Promise<AxiosResponse<Array<ParameterDetailsMessage>>> {
      return DefaultApiFp(configuration)
        .helpGet(options)
        .then((request) => request(axios, basePath))
    },
    /**
     * Run a projection of polygons in the supplied DCSV formatted input file as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionDcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionDcsvPost(
      body?: ProjectionDcsvBody,
      options?: AxiosRequestConfig,
    ): Promise<AxiosResponse<void>> {
      return DefaultApiFp(configuration)
        .projectionDcsvPost(body, options)
        .then((request) => request(axios, basePath))
    },
    /**
     * Run a projection of polygons in the supplied HCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionHcsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionHcsvPost(
      body?: ProjectionHcsvBody,
      options?: AxiosRequestConfig,
    ): Promise<AxiosResponse<void>> {
      return DefaultApiFp(configuration)
        .projectionHcsvPost(body, options)
        .then((request) => request(axios, basePath))
    },
    /**
     * Run a projection of polygons in the supplied SCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
     * @summary Project the growth of one or more polygons to a given year.
     * @param {ProjectionScsvBody} [body]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    async projectionScsvPost(
      body?: ProjectionScsvBody,
      options?: AxiosRequestConfig,
    ): Promise<AxiosResponse<void>> {
      return DefaultApiFp(configuration)
        .projectionScsvPost(body, options)
        .then((request) => request(axios, basePath))
    },
  }
}

/**
 * DefaultApi - object-oriented interface
 * @export
 * @class DefaultApi
 * @extends {BaseAPI}
 */
export class DefaultApi extends BaseAPI {
  /**
   *
   * @summary returns a detailed description of the parameters available when executing a projection.
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof DefaultApi
   */
  public async helpGet(
    options?: AxiosRequestConfig,
  ): Promise<AxiosResponse<Array<ParameterDetailsMessage>>> {
    return DefaultApiFp(this.configuration)
      .helpGet(options)
      .then((request) => request(this.axios, this.basePath))
  }
  /**
   * Run a projection of polygons in the supplied DCSV formatted input file as  controlled by the parameters in the supplied projection parameters file.
   * @summary Project the growth of one or more polygons to a given year.
   * @param {ProjectionDcsvBody} [body]
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof DefaultApi
   */
  public async projectionDcsvPost(
    body?: ProjectionDcsvBody,
    options?: AxiosRequestConfig,
  ): Promise<AxiosResponse<void>> {
    return DefaultApiFp(this.configuration)
      .projectionDcsvPost(body, options)
      .then((request) => request(this.axios, this.basePath))
  }
  /**
   * Run a projection of polygons in the supplied HCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
   * @summary Project the growth of one or more polygons to a given year.
   * @param {ProjectionHcsvBody} [body]
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof DefaultApi
   */
  public async projectionHcsvPost(
    body?: ProjectionHcsvBody,
    options?: AxiosRequestConfig,
  ): Promise<AxiosResponse<void>> {
    return DefaultApiFp(this.configuration)
      .projectionHcsvPost(body, options)
      .then((request) => request(this.axios, this.basePath))
  }
  /**
   * Run a projection of polygons in the supplied SCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.
   * @summary Project the growth of one or more polygons to a given year.
   * @param {ProjectionScsvBody} [body]
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof DefaultApi
   */
  public async projectionScsvPost(
    body?: ProjectionScsvBody,
    options?: AxiosRequestConfig,
  ): Promise<AxiosResponse<void>> {
    return DefaultApiFp(this.configuration)
      .projectionScsvPost(body, options)
      .then((request) => request(this.axios, this.basePath))
  }
}
