export class ValidationBase {
  public validateRange(
    value: number,
    min: number,
    max: number,
    errorMessage: string,
  ): string | true {
    return value >= min && value <= max ? true : errorMessage
  }

  public validateRequired(value: any): boolean {
    return !!value
  }

  public validateDecimalFormat(
    value: string,
    regex = /^\d+(\.\d{1,2})?$/,
    errorMessage: string,
  ): string | true {
    return regex.test(value) ? true : errorMessage
  }
}
