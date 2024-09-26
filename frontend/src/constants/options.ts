export const derivedByOptions = [
  { label: 'Volume', value: 'Volume' },
  { label: 'Basal Area', value: 'Basal Area' },
]

export const speciesMap = {
  PL: 'Lodgepole',
  AC: 'Popular',
  H: 'Hemlock',
  S: 'Spruce',
}

export const becZoneMap = {
  AT: 'Alpine Tundra',
  BG: 'Bunch Grass',
  BWBS: 'Boreal White and Black Spruce',
  CDF: 'Coastal Douglas Fir',
  CWH: 'Coastal Western Hemlock',
  ESSF: 'Engelmann Spruce',
  ICH: 'Interior Cedar Hemlock',
  IDF: 'Interior Douglas Fir',
  MH: 'Mountain Hemlock',
  MS: 'Montane Spruce',
  PP: 'Ponderosa Pine',
  SBPS: 'Sub-Boreal Pine-Spruce',
  SBS: 'Sub-Boreal Spruce',
  SWB: 'Spruce-Willow-Birch',
}

export const siteSpeciesValuesOptions = [
  { label: 'Computed', value: 'Computed' },
  { label: 'Supplied', value: 'Supplied' },
]

export const ageTypeOptions = [
  { label: 'Total', value: 'Total' },
  { label: 'Breast', value: 'Breast' },
]

export const ecoZoneOptions = [
  { label: 'Boreal Cordillera', value: 'Boreal Cordillera' },
  { label: 'Boreal Plains', value: 'Boreal Plains' },
  { label: 'Montane Cordillera', value: 'Montane Cordillera' },
  { label: 'Pacific Maritime', value: 'Pacific Maritime' },
  { label: 'Taiga Plains', value: 'Taiga Plains' },
]

export const siteIndexCurveOptions = [
  { label: 'Thrower (1994)', value: 'Thrower (1994)' },
]

export const floatingOptions = [
  { label: 'Float', value: 'Age' },
  { label: 'Float', value: 'Height' },
  { label: 'Float', value: 'SiteIndex' },
]

export const minimumDBHLimitsOptions = [
  { label: '4.0 cm+', value: '4.0 cm+' },
  { label: '7.5 cm+', value: '7.5 cm+' },
  { label: '12.5 cm+', value: '12.5 cm+' },
  { label: '17.5 cm+', value: '17.5 cm+' },
  { label: '22.5 cm+', value: '22.5 cm+' },
]

export const additionalStandAttributesOptions = [
  {
    label:
      'Use Computed Values (These additional Stand attributes require that a Stand Age and Basal Area be supplied on the Site Index and the Density pages)',
    value: 'Use Computed Values',
  },
  { label: 'Modify Computed Values', value: 'Modify Computed Values' },
]

export const volumeReportedOptions = [
  { label: 'Whole Stem', value: 'Whole Stem' },
  { label: 'Close Utilization', value: 'Close Utilization' },
  { label: 'Net Decay', value: 'Net Decay' },
  { label: 'Net Decay and Waste', value: 'Net Decay and Waste' },
  {
    label: 'Net Decay, Waste and Breakage',
    value: 'Net Decay, Waste and Breakage',
  },
]

export const includeInReportOptions = [
  { label: 'Computed MAI', value: 'Computed MAI' },
  { label: 'Species Composition', value: 'Species Composition' },
  { label: 'Culmination Values', value: 'Culmination Values' },
]

export const projectionTypeOptions = [{ label: '', value: '' }]
