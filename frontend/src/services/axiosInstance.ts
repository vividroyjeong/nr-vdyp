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
  (config: InternalAxiosRequestConfig) => {
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
  (response: AxiosResponse) => {
    // Process a successful response
    return response
  },
  (error) => {
    console.error('Response error:', error)
    return Promise.reject(convertToAxiosError(error))
  },
)

// convert an error object to AxiosError
function convertToAxiosError(error: unknown): Error {
  if (error instanceof Error) {
    return error
  }

  return new Error(String(error))
}

export default axiosInstance
