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

export const AUTH_ERR_MSG = Object.freeze({
  AUTH_001:
    'Error during User authentication verification (Error: AUTH_001). Please log in again.',
  AUTH_002:
    'Error during User authentication verification (Error: AUTH_002). Please log in again.',
  AUTH_003:
    'Error during User authentication verification (Error: AUTH_003). Please log in again.',
  AUTH_004:
    'Error during User authentication verification (Error: AUTH_004). Please log in again.',
  AUTH_010:
    'Error during User authentication initialization (Error: AUTH_010). Please log in again.',
  AUTH_011:
    'Error during User authentication initialization (Error: AUTH_011). Please log in again.',
  AUTH_012:
    'Error during User authentication initialization (Error: AUTH_012). Please log in again.',
  AUTH_020:
    'Error during user authentication refresh (Error: AUTH_020). Please log in again.',
  AUTH_021:
    'Error during user authentication refresh (Error: AUTH_021). Please log in again.',
  AUTH_030:
    'Error during user authentication re-validation (Error: AUTH_030). Please log in again.',
  AUTH_031:
    'Session has exceeded the maximum duration, logging out (Error: AUTH_031). Please log in again.',
  AUTH_032:
    'Error during user authentication re-validation (Error: AUTH_032). Please log in again.',
})
