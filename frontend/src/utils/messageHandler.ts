import { useNotificationStore } from '@/stores/common/notificationStore'
import type { MessageType } from '@/types/types'
import { MESSAGE_TYPE } from '@/constants/constants'

/**
 * Displays job success or failure messages to notification and/or console.
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
  const notificationStore = useNotificationStore()

  if (isSuccess) {
    console.info(successMessage)
    notificationStore.showSuccessMessage(successMessage)
  } else {
    console.warn(failMessage, error)
    notificationStore.showWarningMessage(failMessage)
  }
}

/**
 * Logs messages to both the console or/and notification with conditional control.
 * @param {string} message - The message to display.
 * @param {string} messageType - The type of message.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableNotification=false] - Whether to disable notification messages.
 */
export const logMessage = (
  message: string,
  messageType: MessageType = MESSAGE_TYPE.INFO,
  optionalMessage?: string | null,
  disableConsole: boolean = false,
  disableNotification: boolean = false,
) => {
  const notificationStore = useNotificationStore()

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

  if (!disableNotification) {
    switch (messageType) {
      case MESSAGE_TYPE.ERROR:
        notificationStore.showErrorMessage(message)
        break
      case MESSAGE_TYPE.WARNING:
        notificationStore.showWarningMessage(message)
        break
      case MESSAGE_TYPE.INFO:
        notificationStore.showInfoMessage(message)
        break
      case MESSAGE_TYPE.SUCCESS:
        notificationStore.showSuccessMessage(message)
        break
      default:
        notificationStore.showInfoMessage(message)
    }
  }
}

/**
 * Logs info messages to both the console or/and notification with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableNotification=false] - Whether to disable notification messages.
 */
export const logInfoMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableNotification = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.INFO,
    optionalMessage,
    disableConsole,
    disableNotification,
  )

/**
 * Logs error messages to both the console or/and notification with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableNotification=false] - Whether to disable notification messages.
 */
export const logErrorMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableNotification = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.ERROR,
    optionalMessage,
    disableConsole,
    disableNotification,
  )

/**
 * Logs success messages to both the console or/and notification with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableNotification=false] - Whether to disable notification messages.
 */
export const logSuccessMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableNotification = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.SUCCESS,
    optionalMessage,
    disableConsole,
    disableNotification,
  )

/**
 * Logs warning messages to both the console or/and notification with conditional control.
 * @param {string} message - The message to display.
 * @param {string} [optionalMessage] - Optional detail message for console output.
 * @param {boolean} [disableConsole=false] - Whether to disable console logging.
 * @param {boolean} [disableNotification=false] - Whether to disable notification messages.
 */
export const logWarningMessage = (
  message: string,
  optionalMessage?: string | null,
  disableConsole = false,
  disableNotification = false,
) =>
  logMessage(
    message,
    MESSAGE_TYPE.WARNING,
    optionalMessage,
    disableConsole,
    disableNotification,
  )
