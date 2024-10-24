import { useSnackbarStore } from '@/stores/common/snackbarStore'
import type { MessageType } from '@/types/types'
import { MESSAGE_TYPE } from '@/constants/constants'

/**
 * Displays job success or failure messages to snackbar and/or console.
 * @param {boolean} isSuccess - Whether the operation succeeded.
 * @param {string} successMessage - Message for successful operation.
 * @param {string} failMessage - Message for failed operation.
 * @param {Error | null} error - Optional error object for logging.
 */
export const messageResult = (
  isSuccess: boolean,
  successMessage: string,
  failMessage: string,
  error: Error | null = null,
) => {
  const snackbarStore = useSnackbarStore()

  if (isSuccess) {
    console.info(successMessage)
    snackbarStore.showSuccessMessage(successMessage)
  } else {
    console.warn(failMessage, error)
    snackbarStore.showWarningMessage(failMessage)
  }
}

/**
 * Logs messages to both the console or/and snackbar with conditional control.
 * @param {string} message - The message to display.
 * @param {string} messageType - The type of message.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableSnackbar=false] - Whether to disable snackbar messages.
 */
export const logMessage = (
  message: string,
  messageType: MessageType = MESSAGE_TYPE.INFO,
  optionalMessage?: string | null,
  disableConsole: boolean = false,
  disableSnackbar: boolean = false,
) => {
  const snackbarStore = useSnackbarStore()

  const consoleMessage = optionalMessage
    ? `${message} (${optionalMessage})`
    : message

  if (!disableConsole) {
    switch (messageType) {
      case MESSAGE_TYPE.ERROR:
        console.error(consoleMessage)
        break
      case MESSAGE_TYPE.WARNING:
        console.warn(consoleMessage)
        break
      case MESSAGE_TYPE.INFO:
        console.info(consoleMessage)
        break
      case MESSAGE_TYPE.SUCCESS:
        console.log(consoleMessage)
        break
      default:
        console.log(consoleMessage)
    }
  }

  if (!disableSnackbar) {
    switch (messageType) {
      case MESSAGE_TYPE.ERROR:
        snackbarStore.showErrorMessage(message)
        break
      case MESSAGE_TYPE.WARNING:
        snackbarStore.showWarningMessage(message)
        break
      case MESSAGE_TYPE.INFO:
        snackbarStore.showInfoMessage(message)
        break
      case MESSAGE_TYPE.SUCCESS:
        snackbarStore.showSuccessMessage(message)
        break
      default:
        snackbarStore.showInfoMessage(message)
    }
  }
}

/**
 * Logs info messages to both the console or/and snackbar with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableSnackbar=false] - Whether to disable snackbar messages.
 */
export const logInfoMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableSnackbar = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.INFO,
    optionalMessage,
    disableConsole,
    disableSnackbar,
  )

/**
 * Logs error messages to both the console or/and snackbar with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableSnackbar=false] - Whether to disable snackbar messages.
 */
export const logErrorMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableSnackbar = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.ERROR,
    optionalMessage,
    disableConsole,
    disableSnackbar,
  )

/**
 * Logs success messages to both the console or/and snackbar with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableSnackbar=false] - Whether to disable snackbar messages.
 */
export const logSuccessMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableSnackbar = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.SUCCESS,
    optionalMessage,
    disableConsole,
    disableSnackbar,
  )

/**
 * Logs warning messages to both the console or/and snackbar with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableSnackbar=false] - Whether to disable snackbar messages.
 */
export const logWarningMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableSnackbar = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.WARNING,
    optionalMessage,
    disableConsole,
    disableSnackbar,
  )
