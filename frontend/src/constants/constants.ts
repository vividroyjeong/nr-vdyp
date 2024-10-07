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
