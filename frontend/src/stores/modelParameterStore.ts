import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  DERIVED_BY,
  SITE_SPECIES_VALUES,
  AGE_TYPE,
  FLOATING,
} from '@/constants/constants'

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
      minimumDBHLimit: '7.5 cm+',
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
  const percentStockableArea = ref<number | null>(0)
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
  const selectedVolumeReported = ref<string[]>([])
  const projectionType = ref<string | null>(null)
  const reportTitle = ref<string | null>(null)

  // set default values
  const setDefaultValues = () => {
    derivedBy.value = DERIVED_BY.VOLUME
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
      minimumDBHLimit: '7.5 cm+',
    }))

    becZone.value = '8'
    siteSpeciesValues.value = SITE_SPECIES_VALUES.COMPUTED
    ageType.value = AGE_TYPE.TOTAL
    age.value = 60
    height.value = 17.0
    bha50SiteIndex.value = 16.3
    floating.value = FLOATING.SITEINDEX
    percentStockableArea.value = 55
    percentCrownClosure.value = 0
    minimumDBHLimit.value = '7.5 cm+'
    loreyHeight.value = 13.45
    wholeStemVolume75cm.value = 106.6
    basalArea125cm.value = 17.0482
    wholeStemVolume125cm.value = 97.0
    closeUtilVolume.value = 84.1
    closeUtilNetDecayVolume.value = 78.2
    closeUtilNetDecayWasteVolume.value = 75.1
    startingAge.value = 0
    finishingAge.value = 250
    ageIncrement.value = 25
    selectedVolumeReported.value = ['Whole Stem']
    projectionType.value = 'Volume'
    reportTitle.value = 'A Sample Report Title'
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
    selectedVolumeReported,
    projectionType,
    reportTitle,
    // set default values
    setDefaultValues,
  }
})
