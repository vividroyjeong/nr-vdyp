export default class SpeciesParam {
  public species: string
  public percentComp: string
  public totAge: string
  public bhAge: string
  public height: string
  public si: string
  public ytbh: string

  constructor(blob: any) {
    this.species = blob.species || ''
    this.percentComp = blob.percentComp || ''
    this.totAge = blob.totAge || ''
    this.bhAge = blob.bhAge || ''
    this.height = blob.height || ''
    this.si = blob.si || ''
    this.ytbh = blob.ytbh || ''
  }
}
