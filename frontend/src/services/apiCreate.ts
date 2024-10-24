import { post } from '@/services/apiService'
import Code from '@/models/code'

export const code = async (code: Code): Promise<any> => {
  return await post(`/codeTables/${code.codeTableName}`, code)
}
