import { PANEL, MODEL_PARAMETER_PANEL, SORT_ORDER } from '@/constants/constants'

// Define a type for snackbar message
export type SnackbarType = '' | 'info' | 'success' | 'error' | 'warning'

// Define a type for the panel names
export type PanelName =
  | typeof MODEL_PARAMETER_PANEL.SPECIES_INFO
  | typeof MODEL_PARAMETER_PANEL.SITE_INFO
  | typeof MODEL_PARAMETER_PANEL.STAND_DENSITY
  | typeof MODEL_PARAMETER_PANEL.ADDY_STAND_ATTR
  | typeof MODEL_PARAMETER_PANEL.REPORT_INFO

// Define a type for panel open states
export type PanelState = typeof PANEL.OPEN | typeof PANEL.CLOSE

// Define a type for sort order lowercase letters
export type SortOrder = Lowercase<(typeof SORT_ORDER)[keyof typeof SORT_ORDER]>
