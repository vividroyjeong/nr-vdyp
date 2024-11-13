import axiosInstance from './axiosInstance'
import type { AxiosRequestConfig, AxiosResponse } from 'axios'

export const get = async <T>(
  url: string,
  config?: AxiosRequestConfig,
): Promise<AxiosResponse<T>> => {
  return axiosInstance.get<T>(url, config)
}

export const post = async <T>(
  url: string,
  data: any,
  config?: AxiosRequestConfig,
): Promise<AxiosResponse<T>> => {
  return axiosInstance.post<T>(url, data, config)
}

export const put = async <T>(
  url: string,
  data: any,
  config?: AxiosRequestConfig,
): Promise<AxiosResponse<T>> => {
  return axiosInstance.put<T>(url, data, config)
}

export const del = async <T>(
  url: string,
  config?: AxiosRequestConfig,
): Promise<AxiosResponse<T>> => {
  return axiosInstance.delete<T>(url, config)
}
