export const SERVICE_ERR_MSG = Object.freeze({
  DEFAULT: 'Service Communication Error. Please try again later.',
  REQUEST_TIMEOUT: 'Request timed out. Please try again. (Error: Timeout)',
  SERVICE_UNAVAILABLE:
    'The service is currently unavailable. Please try later. (Error: Unavailable)',
  GATEWAY_TIMEOUT:
    'The server did not respond in time. Please try again later. (Error: Gateway Timeout)',
  INTERNAL_SERVER_ERROR:
    'Service Internal Server Error. Please try again later.',
})
