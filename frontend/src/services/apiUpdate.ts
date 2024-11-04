import { put } from '@/services/apiService'
import Code from '@/models/code'

export const code = async (code: Code, ifMatch: string): Promise<any> => {
  const config = {
    headers: {
      'If-Match': `"${ifMatch}"`,
    },
  }

  return put<Code>(
    `/codeTables/${code.codeTableName}/codes/${code.codeName}`,
    code,
    config,
  )
}
