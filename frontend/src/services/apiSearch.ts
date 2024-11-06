import { get } from '@/services/apiService'
import type { CodeSearchParams } from '@/interfaces/interfaces'

function buildQueryString(params: Record<string, any>): string {
  const query = Object.entries(params)
    .filter(([, value]) => value !== undefined && value !== null)
    .map(
      ([key, value]) =>
        `${encodeURIComponent(key)}=${encodeURIComponent(value)}`,
    )
    .join('&')

  return query ? `?${query}` : ''
}

export const code = async (param: CodeSearchParams): Promise<any> => {
  const queryString = buildQueryString({
    pageNumber:
      typeof param.pageNumber === 'number' && param.pageNumber >= 0
        ? param.pageNumber
        : 0,
    pageSize:
      typeof param.pageSize === 'number' && param.pageSize >= 0
        ? param.pageSize
        : 10,
  })

  const response = await get<any>(`/codeTables${queryString}`)
  if (!response || !response.data) {
    console.warn('Unexpected response format')
    return null
  }

  return response.data
}

export const csvExport = async (): Promise<Blob | null> => {
  const response = await get<Blob>(`/contactsExport?exportOption=All`, {
    headers: {
      Accept: 'text/csv',
    },
    responseType: 'blob',
    timeout: 5000,
  })

  if (!response || !response.data) {
    console.warn('Unexpected response format')
    return null
  }

  return response.data
}
