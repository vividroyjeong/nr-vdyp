import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { FLOATING, DEFAULT_VALUES } from '@/constants/constants'

export const useModelParameterStore = defineStore('modelParameter', () => {
  // panel open
  const panelOpenStates = ref({
    speciesInfo: 0, // 0: open, -1: closed
    siteInfo: 0,
    standDensity: 0,
    additionalStandAttributes: 0,
    reportInfo: 0,
  })

  // species info
  const derivedBy = ref<string | null>(null)

  const speciesList = ref<{ species: string | null; percent: number | null }[]>(
    [
      { species: null, percent: null },
      { species: null, percent: null },
      { species: null, percent: null },
      { species: null, percent: null },
      { species: null, percent: null },
      { species: null, percent: null },
    ],
  )

  const speciesGroups = ref<
    {
      group: string
      percent: number
      siteSpecies: string
      minimumDBHLimit: string
    }[]
  >([])

  // determined in Species Information
  const highestPercentSpecies = ref<string | null>(null)

  // auto-populated once highestPercentSpecies is determined, but could be changed in Site Information
  const selectedSiteSpecies = ref<string | null>(null)

  const totalSpeciesPercent = computed(() => {
    const totalPercent = speciesList.value.reduce((acc, item) => {
      return acc + (parseFloat(item.percent as any) || 0)
    }, 0)
    // preserve to the first decimal place and truncate after that
    return Math.floor(totalPercent * 10) / 10
  })

  const totalSpeciesGroupPercent = computed(() => {
    return speciesGroups.value.reduce((acc, group) => {
      return acc + group.percent
    }, 0)
  })

  const isOverTotalPercent = computed(() => {
    return totalSpeciesPercent.value > 100
  })

  const updateSpeciesGroup = () => {
    const groupMap: { [key: string]: number } = {}

    speciesList.value.forEach((item) => {
      if (item.species && item.percent !== null) {
        if (!groupMap[item.species]) {
          groupMap[item.species] = 0
        }
        groupMap[item.species] += parseFloat(item.percent as any) || 0
      }
    })

    speciesGroups.value = Object.keys(groupMap).map((key) => ({
      group: key,
      percent: groupMap[key],
      siteSpecies: key,
      minimumDBHLimit: DEFAULT_VALUES.MINIMUM_DBH_LIMIT,
    }))

    speciesGroups.value.sort((a, b) => b.percent - a.percent)

    // update highestPercentSpecies and selectedSiteSpecies
    highestPercentSpecies.value = selectedSiteSpecies.value =
      speciesGroups.value.length > 0 ? speciesGroups.value[0].siteSpecies : null
  }

  // site info
  const becZone = ref<string | null>(null)
  const ecoZone = ref<string | null>(null)
  const incSecondaryHeight = ref(false)
  const siteIndexCurve = ref<string | null>(null)
  const siteSpeciesValues = ref<string | null>(null)
  const ageType = ref<string | null>(null)
  const age = ref<number | null>(null)
  const height = ref<number | null>(null)
  const bha50SiteIndex = ref<number | null>(null)
  const floating = ref<string | null>(null)

  // stand density
  const percentStockableArea = ref<number | string | null>(null)
  const basalArea = ref<number | null>(null)
  const treesPerHectare = ref<number | null>(null)
  const minimumDBHLimit = ref<string | null>(null)
  const percentCrownClosure = ref<number | string | null>(null)

  // additional stand attributes
  const computedValues = ref<string | null>(null)
  const loreyHeight = ref<number | null>(null)
  const basalArea125cm = ref<number | null>(null)
  const closeUtilVolume = ref<number | null>(null)
  const closeUtilNetDecayWasteVolume = ref<number | null>(null)
  const wholeStemVolume75cm = ref<number | null>(null)
  const wholeStemVolume125cm = ref<number | null>(null)
  const closeUtilNetDecayVolume = ref<number | null>(null)

  // report info
  const startingAge = ref<number | null>(null)
  const finishingAge = ref<number | null>(null)
  const ageIncrement = ref<number | null>(null)
  const volumeReported = ref<string[]>([])
  const includeInReport = ref<string[]>([])
  const projectionType = ref<string | null>(null)
  const reportTitle = ref<string | null>(null)

  // set default values
  const setDefaultValues = () => {
    derivedBy.value = DEFAULT_VALUES.DERIVED_BY
    speciesList.value = [
      { species: 'PL', percent: 30 },
      { species: 'AC', percent: 30 },
      { species: 'H', percent: 30 },
      { species: 'S', percent: 10 },
      { species: null, percent: 0 },
      { species: null, percent: 0 },
    ]

    updateSpeciesGroup()

    speciesGroups.value = speciesGroups.value.map((group) => ({
      ...group,
      minimumDBHLimit: DEFAULT_VALUES.MINIMUM_DBH_LIMIT,
    }))

    becZone.value = DEFAULT_VALUES.BEC_ZONE
    siteSpeciesValues.value = DEFAULT_VALUES.SITE_SPECIES_VALUES
    ageType.value = DEFAULT_VALUES.AGE_TYPE
    age.value = DEFAULT_VALUES.AGE
    height.value = DEFAULT_VALUES.HEIGHT
    bha50SiteIndex.value = DEFAULT_VALUES.BHA50_SITE_INDEX
    floating.value = FLOATING.SITEINDEX
    percentStockableArea.value = DEFAULT_VALUES.PERCENT_STOCKABLE_AREA
    percentCrownClosure.value = DEFAULT_VALUES.PERCENT_CROWN_CLOSURE
    minimumDBHLimit.value = DEFAULT_VALUES.MINIMUM_DBH_LIMIT
    computedValues.value = DEFAULT_VALUES.COMPUTED_VALUES
    loreyHeight.value = DEFAULT_VALUES.LOREY_HEIGHT
    wholeStemVolume75cm.value = DEFAULT_VALUES.WHOLE_STEM_VOLUME
    basalArea125cm.value = DEFAULT_VALUES.BASAL_AREA_125CM
    wholeStemVolume125cm.value = DEFAULT_VALUES.WHOLE_STEM_VOLUME_125CM
    closeUtilVolume.value = DEFAULT_VALUES.CLOSE_UTIL_VOLUME
    closeUtilNetDecayVolume.value = DEFAULT_VALUES.CLOSE_UTIL_NET_DECAY_VOLUME
    closeUtilNetDecayWasteVolume.value =
      DEFAULT_VALUES.CLOSE_UTIL_NET_DECAY_WASTE_VOLUME
    startingAge.value = DEFAULT_VALUES.STARTING_AGE
    finishingAge.value = DEFAULT_VALUES.FINISHING_AGE
    ageIncrement.value = DEFAULT_VALUES.AGE_INCREMENT
    volumeReported.value = DEFAULT_VALUES.SELECTED_VOLUME_REPORTED
    projectionType.value = DEFAULT_VALUES.PROJECTION_TYPE
    reportTitle.value = DEFAULT_VALUES.REPORT_TITLE
  }

  return {
    // panel open
    panelOpenStates,
    // species info
    derivedBy,
    speciesList,
    speciesGroups,
    highestPercentSpecies,
    selectedSiteSpecies,
    totalSpeciesPercent,
    totalSpeciesGroupPercent,
    isOverTotalPercent,
    updateSpeciesGroup,
    // site info
    becZone,
    ecoZone,
    incSecondaryHeight,
    siteIndexCurve,
    siteSpeciesValues,
    ageType,
    age,
    height,
    bha50SiteIndex,
    floating,
    // stand density
    percentStockableArea,
    basalArea,
    treesPerHectare,
    minimumDBHLimit,
    percentCrownClosure,
    // additional stand attributes
    computedValues,
    loreyHeight,
    basalArea125cm,
    closeUtilVolume,
    closeUtilNetDecayWasteVolume,
    wholeStemVolume75cm,
    wholeStemVolume125cm,
    closeUtilNetDecayVolume,
    // report info
    startingAge,
    finishingAge,
    ageIncrement,
    volumeReported,
    includeInReport,
    projectionType,
    reportTitle,
    // set default values
    setDefaultValues,
  }
})
