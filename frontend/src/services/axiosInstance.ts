import axios from 'axios'
import type {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios'
import { useAuthStore } from '@/stores/common/authStore'
import { AXIOS } from '@/constants/constants'
import { env } from '@/env'
// import * as messageHandler from '@/utils/messageHandler'
// import { StatusCodes } from 'http-status-codes'
// import { handleTokenValidation, refreshToken } from '@/services/keycloak'
// import createAuthRefreshInterceptor from 'axios-auth-refresh'

const axiosInstance: AxiosInstance = axios.create({
  baseURL: env.VITE_API_BASE_URL,
  headers: {
    Accept: AXIOS.ACCEPT,
    'Content-Type': AXIOS.CONTENT_TYPE,
  },
  timeout: AXIOS.DEFAULT_TIMEOUT,
})

axiosInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    // TODO - performance issues or network overhead issue?
    // then consider timer-based refresh + refresh token on 401 error
    // await handleTokenValidation() // Ensure token is valid or refreshed

    const authStore = useAuthStore()
    const token = authStore.user?.accessToken

    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(convertToAxiosError(error))
  },
)

axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    // TODO - 401 Unauthorized handling: Retry after token renewal
    // const authStore = useAuthStore()
    // const { status, config } = error.response || {}

    // if (status === StatusCodes.UNAUTHORIZED) {
    //   try {
    //     const refreshed = await refreshToken(-1) // forcibly token refresh
    //     console.log(`token forcibly refreshed? ${refreshed}`)

    //     const token = authStore.user?.accessToken

    //     if (token && config.headers) {
    //       config.headers.Authorization = `Bearer ${token}`
    //       return axiosInstance(config)
    //     }
    //   } catch (err) {
    //     messageHandler.logErrorMessage(
    //       'Session expired. Please log in again.',
    //       `Retry failed after token renewal for 401 Unauthorized response : ${err}`,
    //     )
    //   }
    // }
    // TODO - Other alternatives => axios-auth-refresh npm package

    return Promise.reject(convertToAxiosError(error))
  },
)

// TODO - If don't need 401 Unauthorized handling in interceptors.response, use the following
// axiosInstance.interceptors.response.use(
//   (response: AxiosResponse) => {
//     // Process a successful response
//     return response
//   },
//   (error) => {
//     console.error('Response error:', error)
//     return Promise.reject(convertToAxiosError(error))
//   },
// )

// convert an error object to AxiosError
function convertToAxiosError(error: unknown): Error {
  if (error instanceof Error) {
    return error
  }

  return new Error(String(error))
}

// TODO - Other alternatives => axios-auth-refresh npm package
/*
// Setting up the axios-auth-refresh interceptor
const refreshAuthLogic = async (failedRequest: any) => {
  try {
    const refreshed = await refreshToken(-1)
    if (refreshed) {
      const authStore = useAuthStore()
      const newToken = authStore.user?.accessToken

      if (newToken) {
        failedRequest.response.config.headers['Authorization'] =
          `Bearer ${newToken}`
        return Promise.resolve()
      }
    }
  } catch (error) {
    console.error('Token refresh failed:', error)
    return Promise.reject(convertToAxiosError(error))
  }
}

// Setting up the axios-auth-refresh interceptor
createAuthRefreshInterceptor(axiosInstance, refreshAuthLogic, {
  statusCodes: [401, 403],
})

// Setting up the axios-auth-refresh interceptor
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error) => {
    console.error('Response error:', error)
    return Promise.reject(convertToAxiosError(error))
  },
)
*/

export default axiosInstance
