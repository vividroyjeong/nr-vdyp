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
  USE: 'Use Computed Values',
  MODIFY: 'Modify Computed Values',
})

export const DEFAULT_VALUES = Object.freeze({
  BEC_ZONE: '8',
  SITE_SPECIES_VALUES: SITE_SPECIES_VALUES.COMPUTED,
  AGE_TYPE: AGE_TYPE.TOTAL,
  AGE: 60,
  HEIGHT: 17.0,
  BHA50_SITE_INDEX: 16.3,
  PERCENT_STOCKABLE_AREA: 55,
  PERCENT_CROWN_CLOSURE: 0,
  MINIMUM_DBH_LIMIT: '7.5 cm+',
  LOREY_HEIGHT: 13.45,
  WHOLE_STEM_VOLUME: 106.6,
  BASAL_AREA_125CM: 17.0482,
  WHOLE_STEM_VOLUME_125CM: 97.0,
  CLOSE_UTIL_VOLUME: 84.1,
  CLOSE_UTIL_NET_DECAY_VOLUME: 78.2,
  CLOSE_UTIL_NET_DECAY_WASTE_VOLUME: 75.1,
  STARTING_AGE: 0,
  FINISHIN_GAGE: 250,
  AGE_INCREMENT: 25,
  SELECTED_VOLUME_REPORTED: ['Whole Stem'],
  PROJECTION_TYPE: 'Volume',
  REPORT_TITLE: 'A Sample Report Title',
})
