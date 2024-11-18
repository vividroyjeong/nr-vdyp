import { ValidationBase } from './validationBase'
import { NUM_INPUT_LIMITS } from '@/constants/constants'
import Papa from 'papaparse'

export class FileUploadValidation extends ValidationBase {
  expectedPolygonHeaders = [
    'FEATURE_ID',
    'MAP_ID',
    'POLYGON_NUMBER',
    'ORG_UNIT',
    'TSA_NAME',
    'TFL_NAME',
    'INVENTORY_STANDARD_CODE',
    'TSA_NUMBER',
    'SHRUB_HEIGHT',
    'SHRUB_CROWN_CLOSURE',
    'SHRUB_COVER_PATTERN',
    'HERB_COVER_TYPE_CODE',
    'HERB_COVER_PCT',
    'HERB_COVER_PATTERN_CODE',
    'BRYOID_COVER_PCT',
    'BEC_ZONE_CODE',
    'CFS_ECOZONE',
    'PRE_DISTURBANCE_STOCKABILITY',
    'YIELD_FACTOR',
    'NON_PRODUCTIVE_DESCRIPTOR_CD',
    'BCLCS_LEVEL1_CODE',
    'BCLCS_LEVEL2_CODE',
    'BCLCS_LEVEL3_CODE',
    'BCLCS_LEVEL4_CODE',
    'BCLCS_LEVEL5_CODE',
    'PHOTO_ESTIMATION_BASE_YEAR',
    'REFERENCE_YEAR',
    'PCT_DEAD',
    'NON_VEG_COVER_TYPE_1',
    'NON_VEG_COVER_PCT_1',
    'NON_VEG_COVER_PATTERN_1',
    'NON_VEG_COVER_TYPE_2',
    'NON_VEG_COVER_PCT_2',
    'NON_VEG_COVER_PATTERN_2',
    'NON_VEG_COVER_TYPE_3',
    'NON_VEG_COVER_PCT_3',
    'NON_VEG_COVER_PATTERN_3',
    'LAND_COVER_CLASS_CD_1',
    'LAND_COVER_PCT_1',
    'LAND_COVER_CLASS_CD_2',
    'LAND_COVER_PCT_2',
    'LAND_COVER_CLASS_CD_3',
    'LAND_COVER_PCT_3',
  ]

  expectedLayerHeaders = [
    'FEATURE_ID',
    'TREE_COVER_LAYER_ESTIMATED_ID',
    'MAP_ID',
    'POLYGON_NUMBER',
    'LAYER_LEVEL_CODE',
    'VDYP7_LAYER_CD',
    'LAYER_STOCKABILITY',
    'FOREST_COVER_RANK_CODE',
    'NON_FOREST_DESCRIPTOR_CODE',
    'EST_SITE_INDEX_SPECIES_CD',
    'ESTIMATED_SITE_INDEX',
    'CROWN_CLOSURE',
    'BASAL_AREA_75',
    'STEMS_PER_HA_75',
    'SPECIES_CD_1',
    'SPECIES_PCT_1',
    'SPECIES_CD_2',
    'SPECIES_PCT_2',
    'SPECIES_CD_3',
    'SPECIES_PCT_3',
    'SPECIES_CD_4',
    'SPECIES_PCT_4',
    'SPECIES_CD_5',
    'SPECIES_PCT_5',
    'SPECIES_CD_6',
    'SPECIES_PCT_6',
    'EST_AGE_SPP1',
    'EST_HEIGHT_SPP1',
    'EST_AGE_SPP2',
    'EST_HEIGHT_SPP2',
    'ADJ_IND',
    'LOREY_HEIGHT_75',
    'BASAL_AREA_125',
    'WS_VOL_PER_HA_75',
    'WS_VOL_PER_HA_125',
    'CU_VOL_PER_HA_125',
    'D_VOL_PER_HA_125',
    'DW_VOL_PER_HA_125',
  ]

  validateAgeComparison(
    finishingAge: number | null,
    startingAge: number | null,
  ): boolean {
    if (finishingAge !== null && startingAge !== null) {
      return finishingAge >= startingAge
    }
    return true
  }

  validateStartingAgeRange(startingAge: number | null): boolean {
    if (startingAge !== null) {
      return (
        startingAge >= NUM_INPUT_LIMITS.STARTING_AGE_MIN &&
        startingAge <= NUM_INPUT_LIMITS.STARTING_AGE_MAX
      )
    }
    return true
  }

  validateFinishingAgeRange(finishingAge: number | null): boolean {
    if (finishingAge !== null) {
      return (
        finishingAge >= NUM_INPUT_LIMITS.FINISHING_AGE_MIN &&
        finishingAge <= NUM_INPUT_LIMITS.FINISHING_AGE_MAX
      )
    }
    return true
  }

  validateAgeIncrementRange(ageIncrement: number | null): boolean {
    if (ageIncrement !== null) {
      return (
        ageIncrement >= NUM_INPUT_LIMITS.AGE_INC_MIN &&
        ageIncrement <= NUM_INPUT_LIMITS.AGE_INC_MAX
      )
    }
    return true
  }

  async isCSVFile(file: File): Promise<boolean> {
    return new Promise((resolve) => {
      Papa.parse(file, {
        complete: (results: any) => {
          resolve(results.errors.length === 0)
        },
        error: () => resolve(false),
      })
    })
  }

  async validateCSVHeaders(
    file: File,
    expectedHeaders: string[],
  ): Promise<boolean> {
    return new Promise((resolve) => {
      Papa.parse(file, {
        complete: (results: any) => {
          if (results.errors.length > 0) {
            resolve(false)
          } else {
            const headers = results.data[0]
            const isValid = expectedHeaders.every((header) =>
              headers.includes(header),
            )
            resolve(isValid)
          }
        },
        error: () => resolve(false),
      })
    })
  }

  async validateLayerFileHeaders(file: File): Promise<boolean> {
    return await this.validateCSVHeaders(file, this.expectedLayerHeaders)
  }

  async validatePolygonFileHeaders(file: File): Promise<boolean> {
    return await this.validateCSVHeaders(file, this.expectedPolygonHeaders)
  }

  async validateDataRowCounts(
    file: File,
    expectedHeaderCount: number,
  ): Promise<boolean> {
    return new Promise((resolve) => {
      Papa.parse(file, {
        complete: (results: any) => {
          const rows = results.data.slice(1) // Skip headers
          const isValid = rows.every(
            (row: any) => row.length === expectedHeaderCount,
          )
          resolve(isValid)
        },
        error: () => resolve(false),
      })
    })
  }
}
