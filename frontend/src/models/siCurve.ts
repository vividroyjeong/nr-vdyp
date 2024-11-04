export default class SICurve {
  public ageRange: string
  public species: string
  public curveName: string

  constructor(blob: any) {
    this.ageRange = blob.ageRange || ''
    this.species = blob.species || ''
    this.curveName = blob.curveName || ''
  }
}
