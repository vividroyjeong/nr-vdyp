import { PANEL, MODEL_PARAMETER_PANEL } from '@/constants/constants'

export type MessageType = '' | 'info' | 'success' | 'error' | 'warning'
// Define a type for the panel names
export type PanelName =
  | typeof MODEL_PARAMETER_PANEL.SPECIES_INFO
  | typeof MODEL_PARAMETER_PANEL.SITE_INFO
  | typeof MODEL_PARAMETER_PANEL.STAND_DENSITY
  | typeof MODEL_PARAMETER_PANEL.ADDY_STAND_ATTR
  | typeof MODEL_PARAMETER_PANEL.REPORT_INFO

// Define a type for panel open states
export type PanelState = typeof PANEL.OPEN | typeof PANEL.CLOSE
