export default class AddtStandAttrs {
  public loreyHeight: string
  public wholeStemVol75: string
  public basalArea125: string
  public wholeStemVol125: string
  public cuVol: string
  public cuNetDecayVol: string
  public cuNetDecayWasteVol: string

  constructor(blob: any) {
    this.loreyHeight = blob.loreyHeight || ''
    this.wholeStemVol75 = blob.wholeStemVol75 || ''
    this.basalArea125 = blob.basalArea125 || ''
    this.wholeStemVol125 = blob.wholeStemVol125 || ''
    this.cuVol = blob.cuVol || ''
    this.cuNetDecayVol = blob.cuNetDecayVol || ''
    this.cuNetDecayWasteVol = blob.cuNetDecayWasteVol || ''
  }
}
