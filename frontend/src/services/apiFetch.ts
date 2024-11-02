import { get } from '@/services/apiService'
import { StatusCodes } from 'http-status-codes'
import Code from '@/models/code'

export const topLevel = async (): Promise<any> => {
  const response = await get<any>('/')

  if (response.status === StatusCodes.OK) {
    return response.data
  } else {
    return null
  }
}

export const pingServer = async (): Promise<any> => {
  const response = await get<any>('/ping')
  return response.status === StatusCodes.OK ? response.data : null
}

export const code = async (
  codeTableName: string,
  codeName: string,
): Promise<Code | null> => {
  const response = await get<any>(
    `/codeTables/${codeTableName}/codes/${codeName}`,
  )

  if (response.status === StatusCodes.OK) {
    const codeJson = response.data
    const etagQuoted = response.headers['etag']
    const etag = etagQuoted ? parseInt(etagQuoted.replace(/"/g, '')) : 0

    return new Code({ ...codeJson, codeTableName, etagQuoted, etag })
  } else {
    return null
  }
}
