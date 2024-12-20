export interface ConfigurationParameters {
  apiKey?:
    | string
    | Promise<string>
    | ((name: string) => string)
    | ((name: string) => Promise<string>)
  username?: string
  password?: string
  accessToken?:
    | string
    | Promise<string>
    | ((name?: string, scopes?: string[]) => string)
    | ((name?: string, scopes?: string[]) => Promise<string>)
  basePath?: string
  baseOptions?: any
}

export class Configuration {
  apiKey?:
    | string
    | Promise<string>
    | ((name: string) => string)
    | ((name: string) => Promise<string>)

  username?: string

  password?: string

  accessToken?:
    | string
    | Promise<string>
    | ((name?: string, scopes?: string[]) => string)
    | ((name?: string, scopes?: string[]) => Promise<string>)

  basePath?: string

  baseOptions?: any

  constructor(param: ConfigurationParameters = {}) {
    this.apiKey = param.apiKey
    this.username = param.username
    this.password = param.password
    this.accessToken = param.accessToken
    this.basePath = param.basePath
    this.baseOptions = param.baseOptions
  }
}
