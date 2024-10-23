import type { SnackbarType, SortOrder } from '@/types/types'

export interface ProgressCircularState {
  isShow: boolean
  message: string | undefined
}

export interface SnackbarState {
  isShow: boolean
  message: string
  type: SnackbarType
  timeoutId: number | null
}

export interface JobSearchParams {
  pageNumber: number
  pageSize: number
  sortBy?: string
  sortOrder?: SortOrder
  searchJobName?: string
  startDate?: string | null
  endDate?: string | null
  status?: string
}

export interface TableOptions {
  page: number
  itemsPerPage: number
  sortBy: string
  sortDesc: string
}
