import * as CONSTANTS from '@/constants/constants'

export const derivedByOptions = [
  { label: 'Volume', value: 'Volume' },
  { label: 'Basal Area', value: 'Basal Area' },
]

export const becZoneOptions = [
  { label: 'AT - Alpine Tundra', value: 'AT' },
  { label: 'BG - Bunch Grass', value: 'BG' },
  { label: 'BWBS - Boreal White and Black Spruce', value: 'BWBS' },
  { label: 'CDF - Coastal Douglas Fir', value: 'CDF' },
  { label: 'CWH - Coastal Western Hemlock', value: 'CWH' },
  { label: 'ESSF - Engelmann Spruce', value: 'ESSF' },
  { label: 'ICH - Interior Cedar Hemlock', value: 'ICH' },
  { label: 'IDF - Interior Douglas Fir', value: 'IDF' },
  { label: 'MH - Mountain Hemlock', value: 'MH' },
  { label: 'MS - Montane Spruce', value: 'MS' },
  { label: 'PP - Ponderosa Pine', value: 'PP' },
  { label: 'SBPS - Sub-Boreal Pine-Spruce', value: 'SBPS' },
  { label: 'SBS - Sub-Boreal Spruce', value: 'SBS' },
  { label: 'SWB - Spruce-Willow-Birch', value: 'SWB' },
]

export const siteSpeciesValuesOptions = [
  { label: 'Computed', value: CONSTANTS.SITE_SPECIES_VALUES.COMPUTED },
  { label: 'Supplied', value: CONSTANTS.SITE_SPECIES_VALUES.SUPPLIED },
]

export const ageTypeOptions = [
  { label: 'Total', value: CONSTANTS.AGE_TYPE.TOTAL },
  { label: 'Breast', value: CONSTANTS.AGE_TYPE.BREAST },
]

export const ecoZoneOptions = [
  { label: 'Boreal Cordillera', value: '1' },
  { label: 'Boreal Plains', value: '2' },
  { label: 'Montane Cordillera', value: '3' },
  { label: 'Pacific Maritime', value: '4' },
  { label: 'Taiga Plains', value: '5' },
]

export const floatingOptions = [
  { label: 'Float', value: CONSTANTS.FLOATING.AGE },
  { label: 'Float', value: CONSTANTS.FLOATING.HEIGHT },
  { label: 'Float', value: CONSTANTS.FLOATING.SITEINDEX },
]

export const minimumDBHLimitsOptions = [
  { label: '4.0 cm+', value: CONSTANTS.MINIMUM_DBH_LIMITS.CM4_0 },
  { label: '7.5 cm+', value: CONSTANTS.MINIMUM_DBH_LIMITS.CM7_5 },
  { label: '12.5 cm+', value: CONSTANTS.MINIMUM_DBH_LIMITS.CM12_5 },
  { label: '17.5 cm+', value: CONSTANTS.MINIMUM_DBH_LIMITS.CM17_5 },
  { label: '22.5 cm+', value: CONSTANTS.MINIMUM_DBH_LIMITS.CM22_5 },
]

export const additionalStandAttributesOptions = [
  {
    label: 'Use Computed Values',
    value: CONSTANTS.COMPUTED_VALUES.USE,
  },
  { label: 'Modify Computed Values', value: CONSTANTS.COMPUTED_VALUES.MODIFY },
]

export const volumeReportedOptions = [
  { label: 'Whole Stem', value: CONSTANTS.VOLUME_REPORTED.WHOLE_STEM },
  { label: 'Close Utilization', value: CONSTANTS.VOLUME_REPORTED.CLOSE_UTIL },
  { label: 'Net Decay', value: CONSTANTS.VOLUME_REPORTED.NET_DECAY },
  {
    label: 'Net Decay and Waste',
    value: CONSTANTS.VOLUME_REPORTED.NET_DECAY_WASTE,
  },
  {
    label: 'Net Decay, Waste and Breakage',
    value: CONSTANTS.VOLUME_REPORTED.NET_DECAY_WASTE_BREAKAGE,
  },
]

export const includeInReportOptions = [
  { label: 'Computed MAI', value: 'Computed MAI' },
  { label: 'Species Composition', value: 'Species Composition' },
  { label: 'Culmination Values', value: 'Culmination Values' },
]

export const projectionTypeOptions = [
  { label: 'Volume', value: CONSTANTS.PROJECTION_TYPE.VOLUME },
  { label: 'CFS Biomass', value: CONSTANTS.PROJECTION_TYPE.CFS_BIOMASS },
]
