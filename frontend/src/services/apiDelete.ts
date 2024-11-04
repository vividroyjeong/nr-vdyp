import { del } from '@/services/apiService'

export const code = async (
  codeTableName: string,
  codeName: string,
  ifMatch: string,
): Promise<any> => {
  const config = {
    headers: {
      'If-Match': `"${ifMatch}"`,
    },
  }

  return del<any>(`/codeTables/${codeTableName}/codes/${codeName}`, config)
}
