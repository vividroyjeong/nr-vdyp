import { defineStore } from 'pinia'
import type { MessageType } from '@/types/types'
import { SNACKBAR } from '@/constants/constants'

interface MessageState {
  isShow: boolean
  message: string
  type: MessageType
  timeoutId: number | null
}

export const useSnackbarStore = defineStore({
  id: 'snackbarStore',
  state: (): MessageState => ({
    isShow: false,
    message: '',
    type: '',
    timeoutId: null,
  }),
  getters: {},
  actions: {
    resetMessage() {
      this.isShow = false
      if (this.timeoutId) {
        clearTimeout(this.timeoutId)
        this.timeoutId = null
      }
    },
    showMessage(message: string, type: MessageType = '') {
      this.resetMessage()
      this.message = message
      this.type = type
      this.isShow = true

      // Automatically close messages after SNACKBAR.SHOW_TIME
      this.timeoutId = setTimeout(() => {
        this.isShow = false
      }, SNACKBAR.SHOW_TIME) as unknown as number
    },
    showSuccessMessage(message: string) {
      this.showMessage(message, 'success')
    },
    showErrorMessage(message: string) {
      this.showMessage(message, 'error')
    },
    showInfoMessage(message: string) {
      this.showMessage(message, 'info')
    },
    showWarningMessage(message: string) {
      this.showMessage(message, 'warning')
    },
  },
})
