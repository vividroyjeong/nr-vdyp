import axios from 'axios'
import type {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios'
import { useAuthStore } from '@/stores/common/authStore'
import { AXIOS } from '@/constants/constants'
import { env } from '@/env'

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
    // TODO - Other alternatives => axios-auth-refresh npm package

    return Promise.reject(convertToAxiosError(error))
  },
)

// TODO - If don't need 401 Unauthorized handling in interceptors.response, use the following

// convert an error object to AxiosError
function convertToAxiosError(error: unknown): Error {
  if (error instanceof Error) {
    return error
  }

  return new Error(String(error))
}

// TODO - Other alternatives => axios-auth-refresh npm package

export default axiosInstance
