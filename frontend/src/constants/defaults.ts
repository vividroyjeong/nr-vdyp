import * as CONSTANTS from '@/constants/constants'

export const DEFAULT_VALUES = Object.freeze({
  DERIVED_BY: CONSTANTS.DERIVED_BY.VOLUME,
  BEC_ZONE: 'IDF',
  SITE_SPECIES_VALUES: CONSTANTS.SITE_SPECIES_VALUES.SUPPLIED,
  BHA50_SITE_INDEX: '16.30',
  PERCENT_STOCKABLE_AREA: 55,
  STARTING_AGE: 0,
  FINISHING_AGE: 250,
  AGE_INCREMENT: 25,
  VOLUME_REPORTED: [CONSTANTS.VOLUME_REPORTED.WHOLE_STEM],
  PROJECTION_TYPE: CONSTANTS.PROJECTION_TYPE.VOLUME,
  REPORT_TITLE: 'A Sample Report Title',
  MODEL_SELECTION: CONSTANTS.MODEL_SELECTION.FILE_UPLOAD,
})
