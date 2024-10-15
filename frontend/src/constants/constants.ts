export const KEYCLOAK = Object.freeze({
  PKCE_METHOD: 'S256',
  ONLOAD: 'check-sso',
  CHECK_LOGIN_IFRAME: false,
  // SILENT_CHECK_SSO_REDIRECT_PAGE: '/silent-check-sso.html',
  IDP_AZUR_IDIR: 'azureidir', // Identity Provider: IDIR with MFA
  MAX_SESSION_DURATION: 8 * 60 * 60 * 1000, // 8 hours (in milliseconds)
  UPDATE_TOKEN_MIN_VALIDITY: 5, // if -1, forcibly refreshed (in minutes)
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

export const NOT_AVAILABLE_INDI = Object.freeze({
  NA: 'N/A',
})

export const MODEL_PARAMETER_PANEL = Object.freeze({
  SPECIES_INFO: 'speciesInfo',
  SITE_INFO: 'siteInfo',
  STAND_DENSITY: 'standDensity',
  ADDY_STAND_ATTR: 'additionalStandAttributes',
  REPORT_INFO: 'reportInfo',
})

export const NUM_INPUT_LIMITS = Object.freeze({
  SPECIES_PERCENT_MAX: 100,
  SPECIES_PERCENT_MIN: 0,
  SPECIES_PERCENT_STEP: 5,
  SPECIES_PERCENT_DECIMAL_NUM: 1,
  AGE_MAX: 500,
  AGE_MIN: 0,
  AGE_STEP: 10,
  HEIGHT_MAX: 99,
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
  WHOLE_STEM_VOL_75CM_MAX: 2500,
  WHOLE_STEM_VOL_75CM_MIN: 0.1,
  BASAL_AREA_125CM_MAX: 250,
  BASAL_AREA_125CM_MIN: 0.1,
  WHOLE_STEM_VOL_125CM_MAX: 2500,
  WHOLE_STEM_VOL_125CM_MIN: 0,
  CU_VOLUME_MAX: 2500,
  CU_VOLUME_MIN: 0,
  CU_NET_DECAY_VOL_MAX: 2500,
  CU_NET_DECAY_VOL_MIN: 0,
  CU_NET_DECAY_WASTE_VOL_MAX: 2500,
  CU_NET_DECAY_WASTE_VOL_MIN: 0,
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
