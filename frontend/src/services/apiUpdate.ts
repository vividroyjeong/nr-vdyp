import { put } from '@/services/apiService'
import Code from '@/models/code'

export const code = async (code: Code, ifMatch: string): Promise<any> => {
  if (!code || !code.codeTableName || !code.codeName) {
    console.warn('Invalid Code object or missing required properties')
    return null
  }

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
