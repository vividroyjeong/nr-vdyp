import { PANEL } from '@/constants/constants'

export type MessageType = '' | 'info' | 'success' | 'error' | 'warning'
// Define a type for the panel names
export type PanelName =
  | 'speciesInfo'
  | 'siteInfo'
  | 'standDensity'
  | 'additionalStandAttributes'
  | 'reportInfo'

// Define a type for panel open states
export type PanelState = typeof PANEL.OPEN | typeof PANEL.CLOSE
