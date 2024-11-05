import { get } from '@/services/apiService'
import { StatusCodes } from 'http-status-codes'
import Code from '@/models/code'

export const topLevel = async (): Promise<any> => {
  const response = await get<any>('/')

  if (!response || !response.status || !response.data) {
    console.warn('Unexpected response format or status')
    return null
  }

  if (response.status === StatusCodes.OK) {
    return response.data
  } else {
    return null
  }
}

export const pingServer = async (): Promise<any> => {
  const response = await get<any>('/ping')

  if (!response || !response.status || !response.data) {
    console.warn('Unexpected response format or status')
    return null
  }

  return response.status === StatusCodes.OK ? response.data : null
}

export const code = async (
  codeTableName: string,
  codeName: string,
): Promise<Code | null> => {
  const response = await get<any>(
    `/codeTables/${codeTableName}/codes/${codeName}`,
  )

  if (!response || !response.status || !response.data || !response.headers) {
    console.warn('Unexpected response format or status')
    return null
  }

  if (response.status === StatusCodes.OK) {
    const codeJson = response.data
    const etagQuoted = response.headers['etag']
    const etag = etagQuoted ? parseInt(etagQuoted.replace(/"/g, '')) : 0

    return new Code({ ...codeJson, codeTableName, etagQuoted, etag })
  } else {
    return null
  }
}
