import {
  PANEL,
  MODEL_PARAMETER_PANEL,
  SORT_ORDER,
  MESSAGE_TYPE,
} from '@/constants/constants'

// Define a type for snackbar message
export type MessageType =
  | ''
  | typeof MESSAGE_TYPE.INFO
  | typeof MESSAGE_TYPE.SUCCESS
  | typeof MESSAGE_TYPE.ERROR
  | typeof MESSAGE_TYPE.WARNING

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
