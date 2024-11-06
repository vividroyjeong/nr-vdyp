import { del } from '@/services/apiService'

export const code = async (
  codeTableName: string,
  codeName: string,
  ifMatch: string,
): Promise<any> => {
  if (!codeTableName) {
    throw new Error('codeTableName is required.')
  }

  if (!codeName) {
    throw new Error('codeName is required.')
  }

  if (!ifMatch) {
    throw new Error('ifMatch is required.')
  }

  const config = {
    headers: {
      'If-Match': `"${ifMatch}"`,
    },
  }

  return del<any>(`/codeTables/${codeTableName}/codes/${codeName}`, config)
}
