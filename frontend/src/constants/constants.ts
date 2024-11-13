export const KEYCLOAK = Object.freeze({
  PKCE_METHOD: 'S256',
  ONLOAD: 'check-sso',
  CHECK_LOGIN_IFRAME: false,
  IDP_AZUR_IDIR: 'azureidir', // Identity Provider: IDIR with MFA
  MAX_SESSION_DURATION: 8 * 60 * 60 * 1000, // 8 hours (in milliseconds)
  UPDATE_TOKEN_MIN_VALIDITY: 120, // 2 min, in seconds
  IS_TOKEN_EXP_MIN_VALIDITY: 120, // 2 min, in seconds
  ENABLE_LOGGING: false,
})

export const SORT_ORDER = Object.freeze({
  ASC: 'ASC',
  DESC: 'DESC',
})

export const PANEL = Object.freeze({
  OPEN: 0,
  CLOSE: -1,
})

export const DERIVED_BY = Object.freeze({
  VOLUME: 'Volume',
  BASAL_AREA: 'Basal Area',
})

export const SITE_SPECIES_VALUES = Object.freeze({
  COMPUTED: 'Computed',
  SUPPLIED: 'Supplied',
})

export const AGE_TYPE = Object.freeze({
  TOTAL: 'Total',
  BREAST: 'Breast',
})

export const FLOATING = Object.freeze({
  AGE: 'Age',
  HEIGHT: 'Height',
  SITEINDEX: 'SiteIndex',
})

export const COMPUTED_VALUES = Object.freeze({
  USE: 'Use',
  MODIFY: 'Modify',
})

export const MINIMUM_DBH_LIMITS = Object.freeze({
  CM4_0: '4.0 cm+',
  CM7_5: '7.5 cm+',
  CM12_5: '12.5 cm+',
  CM17_5: '17.5 cm+',
  CM22_5: '22.5 cm+',
})

export const VOLUME_REPORTED = Object.freeze({
  WHOLE_STEM: 'Whole Stem',
  CLOSE_UTIL: 'Close Utilization',
  NET_DECAY: 'Net Decay',
  NET_DECAY_WASTE: 'Net Decay and Waste',
  NET_DECAY_WASTE_BREAKAGE: 'Net Decay, Waste and Breakage',
})

export const PROJECTION_TYPE = Object.freeze({
  VOLUME: 'Volume',
  CFS_BIOMASS: 'CFS Biomass',
})

export const SPECIAL_INDICATORS = Object.freeze({
  NA: 'N/A',
  NOT_USED: '<Not Used>',
})

export const MODEL_PARAMETER_PANEL = Object.freeze({
  SPECIES_INFO: 'speciesInfo',
  SITE_INFO: 'siteInfo',
  STAND_DENSITY: 'standDensity',
  ADDT_STAND_ATTRS: 'addtStandAttrs',
  REPORT_INFO: 'reportInfo',
})

export const NUM_INPUT_LIMITS = Object.freeze({
  SPECIES_PERCENT_MAX: 100,
  SPECIES_PERCENT_MIN: 0,
  SPECIES_PERCENT_STEP: 5,
  SPECIES_PERCENT_DECIMAL_NUM: 1,
  TOTAL_SPECIES_PERCENT: 100,
  AGE_MAX: 500,
  AGE_MIN: 0,
  AGE_STEP: 10,
  HEIGHT_MAX: 99.9,
  HEIGHT_MIN: 0,
  HEIGHT_STEP: 1,
  HEIGHT_DECIMAL_NUM: 2,
  BHA50_SITE_INDEX_MAX: 60,
  BHA50_SITE_INDEX_MIN: 0,
  BHA50_SITE_INDEX_STEP: 1,
  BHA50_SITE_INDEX_DECIMAL_NUM: 2,
  PERCENT_STOCKABLE_AREA_MAX: 100,
  PERCENT_STOCKABLE_AREA_MIN: 0,
  PERCENT_STOCKABLE_AREA_STEP: 5,
  BASAL_AREA_MAX: 250,
  BASAL_AREA_MIN: 0.1,
  BASAL_AREA_STEP: 2.5,
  BASAL_AREA_DECIMAL_NUM: 4,
  TPH_MAX: 9999.9,
  TPH_MIN: 0.1,
  TPH_STEP: 250,
  TPH_DECIMAL_NUM: 2,
  CROWN_CLOSURE_MAX: 100,
  CROWN_CLOSURE_MIN: 0,
  CROWN_CLOSURE_STEP: 5,
  LOREY_HEIGHT_MAX: 99.9,
  LOREY_HEIGHT_MIN: 0.01,
  LOREY_HEIGHT_DECIMAL_NUM: 2,
  WHOLE_STEM_VOL75_MAX: 2500,
  WHOLE_STEM_VOL75_MIN: 0.1,
  WHOLE_STEM_VOL75_DECIMAL_NUM: 1,
  BASAL_AREA125_MAX: 250,
  BASAL_AREA125_MIN: 0.1,
  BASAL_AREA125_DECIMAL_NUM: 4,
  WHOLE_STEM_VOL125_MAX: 2500,
  WHOLE_STEM_VOL125_MIN: 0,
  WHOLE_STEM_VOL125_DECIMAL_NUM: 1,
  CU_VOL_MAX: 2500,
  CU_VOL_MIN: 0,
  CU_VOL_DECIMAL_NUM: 1,
  CU_NET_DECAY_VOL_MAX: 2500,
  CU_NET_DECAY_VOL_MIN: 0,
  CU_NET_DECAY_DECIMAL_NUM: 1,
  CU_NET_DECAY_WASTE_VOL_MAX: 2500,
  CU_NET_DECAY_WASTE_VOL_MIN: 0,
  CU_NET_DECAY_WASTE_DECIMAL_NUM: 1,
  STARTING_AGE_MAX: 500,
  STARTING_AGE_MIN: 0,
  STARTING_AGE_STEP: 10,
  FINISHING_AGE_MAX: 450,
  FINISHING_AGE_MIN: 1,
  FINISHING_AGE_STEP: 10,
  AGE_INC_MAX: 350,
  AGE_INC_MIN: 1,
  AGE_INC_STEP: 5,
})

export const CONTINUOUS_INC_DEC = Object.freeze({
  INTERVAL: 100, // (e.g., 100ms)
})

export const SPIN_BUTTON = Object.freeze({
  UP: '▲',
  DOWN: '▼',
})

export const NOTIFICATION = Object.freeze({
  SHOW_TIME: 5000, // in milliseconds (5 sec)
})

export const AXIOS = Object.freeze({
  DEFAULT_TIMEOUT: 100000, // in milliseconds (100 sec)
  ACCEPT: 'application/json',
  CONTENT_TYPE: 'application/json',
})

export const MESSAGE_TYPE = Object.freeze({
  INFO: 'info',
  SUCCESS: 'success',
  ERROR: 'error',
  WARNING: 'warning',
})

export const MODEL_SELECTION = Object.freeze({
  FILE_UPLOAD: 'File Upload',
  INPUT_MODEL_PARAMETERS: 'Input Model Parameters',
})

export const ENGINE_VERSION = Object.freeze({
  VDYP8: 'VDYP 8',
  VDYP9: 'VDYP 9',
})

export const MODEL_PARAM_TAB_NAME = Object.freeze({
  MODEL_PARAM_SELECTION: 'Model Parameter Selection',
  FILE_UPLOAD: 'File Upload',
  MODEL_REPORT: 'Model Report',
  VIEW_LOG_FILE: 'View Log File',
  VIEW_ERROR_MESSAGES: 'View Error Messages',
})
