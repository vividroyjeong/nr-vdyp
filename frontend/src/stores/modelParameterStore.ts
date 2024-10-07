import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { PANEL, FLOATING } from '@/constants/constants'
import { DEFAULT_VALUES } from '@/constants/defaults'

// Define a type for the panel names
type PanelName =
  | 'speciesInfo'
  | 'siteInfo'
  | 'standDensity'
  | 'additionalStandAttributes'
  | 'reportInfo'

// Define a type for panel open states
type PanelState = typeof PANEL.OPEN | typeof PANEL.CLOSE

export const useModelParameterStore = defineStore('modelParameter', () => {
  // panel open
  const panelOpenStates = ref<Record<string, PanelState>>({
    speciesInfo: PANEL.OPEN,
    siteInfo: PANEL.OPEN,
    standDensity: PANEL.OPEN,
    additionalStandAttributes: PANEL.OPEN,
    reportInfo: PANEL.OPEN,
  })

  // Panel states for confirming and editing
  const panelState = ref<
    Record<PanelName, { confirmed: boolean; editable: boolean }>
  >({
    speciesInfo: { confirmed: false, editable: true }, // Only speciesInfo is editable initially
    siteInfo: { confirmed: false, editable: false },
    standDensity: { confirmed: false, editable: false },
    additionalStandAttributes: { confirmed: false, editable: false },
    reportInfo: { confirmed: false, editable: false },
  })

  // Run Model button state
  const runModelEnabled = ref(false)

  // <confirmed === true>
  // Indicates that the panel has validated and confirmed the user's input.
  // When the user completes all of the input within the panel and clicks the confirm button to pass validation, confirmed is set to true.
  // This state means that the data in that panel is currently valid and does not need to be modified.
  // When this happens, the confirm button on the current panel changes to "Edit" and the confirm button on the next panel becomes active.

  // <editable === true>
  // Indicates that the panel can be modified by the user.
  // editable becomes true when a panel is active and allows users to enter or modify data.
  // On initial loading, only the first panel (SpeciesInfo) is set to editable to allow modification;
  // the other panels start with editable false because the previous panel has not been confirmed.
  // In this state, the user can modify the input fields within the panel, and the confirm button is enabled to confirm with validation.

  // Method to handle confirm action for each panel
  const confirmPanel = (panelName: PanelName) => {
    panelState.value[panelName].confirmed = true
    panelState.value[panelName].editable = false

    // Enable the next panel's confirm and clear buttons
    const panelOrder: PanelName[] = [
      'speciesInfo',
      'siteInfo',
      'standDensity',
      'additionalStandAttributes',
      'reportInfo',
    ]
    const currentIndex = panelOrder.indexOf(panelName)
    if (currentIndex !== -1 && currentIndex < panelOrder.length - 1) {
      const nextPanel = panelOrder[currentIndex + 1]
      panelState.value[nextPanel].editable = true
    }

    // Check if all panels are confirmed to enable the 'Run Model' button
    runModelEnabled.value = panelOrder.every(
      (panel) => panelState.value[panel].confirmed,
    )
  }

  // Method to handle edit action for each panel
  const editPanel = (panelName: PanelName) => {
    panelState.value[panelName].confirmed = false
    panelState.value[panelName].editable = true

    // Disable all subsequent panels
    const panelOrder: PanelName[] = [
      'speciesInfo',
      'siteInfo',
      'standDensity',
      'additionalStandAttributes',
      'reportInfo',
    ]
    const currentIndex = panelOrder.indexOf(panelName)
    if (currentIndex !== -1) {
      for (let i = currentIndex + 1; i < panelOrder.length; i++) {
        const nextPanel = panelOrder[i]
        panelState.value[nextPanel].confirmed = false
        panelState.value[nextPanel].editable = false
      }
    }

    // Disable 'Run Model' button
    runModelEnabled.value = false
  }

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
  const currentDiameter = ref<number | null>(null)

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
    minimumDBHLimit.value = DEFAULT_VALUES.MINIMUM_DBH_LIMIT
    currentDiameter.value = DEFAULT_VALUES.CURRENT_DIAMETER
    percentCrownClosure.value = DEFAULT_VALUES.PERCENT_CROWN_CLOSURE
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
    // Panel state
    panelState,
    runModelEnabled,
    confirmPanel,
    editPanel,
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
    currentDiameter,
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
