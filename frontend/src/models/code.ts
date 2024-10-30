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
    this.codeTableName = blob.codeTableName || ''
    this.codeName = blob.codeName || ''
    this.description = blob.description || ''
    this.displayOrder = blob.displayOrder || ''
    this.effectiveDate = blob.effectiveDate
      ? new Date(blob.effectiveDate)
      : null
    this.expiryDate = blob.expiryDate ? new Date(blob.expiryDate) : null
    this.etag = blob.etag !== undefined ? blob.etag : null
  }
}
