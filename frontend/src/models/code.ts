import { env } from '@/env'
export default class Code {
  public codeTableName: string
  public codeName: string
  public description: string
  public displayOrder: number
  public effectiveDate: Date | null
  public expiryDate: Date | null
  public etag: number | null
  public readonly '@type' = env.VITE_CODE_TYPE_URL

  constructor(blob: any) {
    this.codeTableName =
      blob && typeof blob.codeTableName === 'string' ? blob.codeTableName : ''
    this.codeName =
      blob && typeof blob.codeName === 'string' ? blob.codeName : ''
    this.description =
      blob && typeof blob.description === 'string' ? blob.description : ''
    this.displayOrder =
      blob && typeof blob.displayOrder === 'number' ? blob.displayOrder : 0
    this.effectiveDate =
      blob && blob.effectiveDate ? new Date(blob.effectiveDate) : null
    this.expiryDate = blob && blob.expiryDate ? new Date(blob.expiryDate) : null
    this.etag = blob && typeof blob.etag === 'number' ? blob.etag : null
  }
}
