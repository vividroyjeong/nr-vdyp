import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { CONSTANTS } from '@/constants'
import { DEFAULT_VALUES } from '@/constants/defaults'
import type { PanelName, PanelState } from '@/types/types'
import type { SpeciesList, SpeciesGroup } from '@/interfaces/interfaces'
import { Util } from '@/utils/util'

export const useModelParameterStore = defineStore('modelParameter', () => {
  // panel open
  const panelOpenStates = ref<Record<PanelName, PanelState>>({
    speciesInfo: CONSTANTS.PANEL.OPEN,
    siteInfo: CONSTANTS.PANEL.CLOSE,
    standDensity: CONSTANTS.PANEL.CLOSE,
    reportInfo: CONSTANTS.PANEL.CLOSE,
  })

  // Panel states for confirming and editing
  const panelState = ref<
    Record<PanelName, { confirmed: boolean; editable: boolean }>
  >({
    speciesInfo: { confirmed: false, editable: true },
    siteInfo: { confirmed: false, editable: false },
    standDensity: { confirmed: false, editable: false },
    reportInfo: { confirmed: false, editable: false },
  })

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
      CONSTANTS.MODEL_PARAMETER_PANEL.SPECIES_INFO,
      CONSTANTS.MODEL_PARAMETER_PANEL.SITE_INFO,
      CONSTANTS.MODEL_PARAMETER_PANEL.STAND_DENSITY,
      CONSTANTS.MODEL_PARAMETER_PANEL.REPORT_INFO,
    ]
    const currentIndex = panelOrder.indexOf(panelName)
    if (currentIndex !== -1 && currentIndex < panelOrder.length - 1) {
      // The next panel opens automatically, switching to the editable.
      const nextPanel = panelOrder[currentIndex + 1]
      panelOpenStates.value[nextPanel] = CONSTANTS.PANEL.OPEN
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
      CONSTANTS.MODEL_PARAMETER_PANEL.SPECIES_INFO,
      CONSTANTS.MODEL_PARAMETER_PANEL.SITE_INFO,
      CONSTANTS.MODEL_PARAMETER_PANEL.STAND_DENSITY,
      CONSTANTS.MODEL_PARAMETER_PANEL.REPORT_INFO,
    ]
    const currentIndex = panelOrder.indexOf(panelName)
    if (currentIndex !== -1) {
      for (let i = currentIndex + 1; i < panelOrder.length; i++) {
        // All of the next panels are automatically closed, uneditable, and unconfirmed
        const nextPanel = panelOrder[i]
        panelState.value[nextPanel].confirmed = false
        panelState.value[nextPanel].editable = false
        panelOpenStates.value[nextPanel] = CONSTANTS.PANEL.CLOSE
      }
    }

    // Disable 'Run Model' button
    runModelEnabled.value = false
  }

  // species info
  const derivedBy = ref<string | null>(null)

  const speciesList = ref<SpeciesList[]>([
    { species: null, percent: null },
    { species: null, percent: null },
    { species: null, percent: null },
    { species: null, percent: null },
    { species: null, percent: null },
    { species: null, percent: null },
  ])

  const speciesGroups = ref<SpeciesGroup[]>([])

  // determined in Species Information
  const highestPercentSpecies = ref<string | null>(null)

  // auto-populated once highestPercentSpecies is determined, but could be changed in Site Information
  const selectedSiteSpecies = ref<string | null>(null)

  const totalSpeciesPercent = computed(() => {
    const totalPercent = speciesList.value.reduce((acc, item) => {
      return acc + (parseFloat(item.percent as any) || 0)
    }, 0)

    // Preserve to the first decimal place and convert to string in '##0.0' format
    const formattedPercent = (Math.floor(totalPercent * 10) / 10).toFixed(
      CONSTANTS.NUM_INPUT_LIMITS.SPECIES_PERCENT_DECIMAL_NUM,
    )

    return formattedPercent
  })

  const totalSpeciesGroupPercent = computed(() => {
    return speciesGroups.value.reduce((acc, group) => {
      return acc + (parseFloat(group.percent) || 0)
    }, 0)
  })

  const updateSpeciesGroup = () => {
    const groupMap: { [key: string]: number } = {}

    // Iterate through speciesList and build a group map
    for (const item of speciesList.value) {
      if (!item.species || Util.isEmptyOrZero(item.percent)) {
        continue
      }

      // Initialize group if it doesn't exist in groupMap
      if (!groupMap[item.species]) {
        groupMap[item.species] = 0
      }

      // Add percent to the group
      groupMap[item.species] += parseFloat(item.percent as any) || 0
    }

    // Convert groupMap to speciesGroups array
    speciesGroups.value = Object.keys(groupMap).map((key) => ({
      group: key,
      percent: groupMap[key].toFixed(
        CONSTANTS.NUM_INPUT_LIMITS.SPECIES_PERCENT_DECIMAL_NUM,
      ),
      siteSpecies: key,
    }))

    speciesGroups.value.sort(
      (a, b) => parseFloat(b.percent) - parseFloat(a.percent),
    )

    // Update highestPercentSpecies and selectedSiteSpecies with the first siteSpecies in speciesGroups
    highestPercentSpecies.value = selectedSiteSpecies.value =
      speciesGroups.value.length > 0 ? speciesGroups.value[0].siteSpecies : null
  }

  // site info
  const becZone = ref<string | null>(null)
  const ecoZone = ref<string | null>(null)
  const incSecondaryHeight = ref(false)
  const siteSpeciesValues = ref<string | null>(null)
  const bha50SiteIndex = ref<string | null>(null)

  // stand density
  const percentStockableArea = ref<number | null>(null)

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
      { species: 'PL', percent: '30.0' },
      { species: 'AC', percent: '30.0' },
      { species: 'H', percent: '30.0' },
      { species: 'S', percent: '10.0' },
      { species: null, percent: '0.0' },
      { species: null, percent: '0.0' },
    ]

    updateSpeciesGroup()

    speciesGroups.value = speciesGroups.value.map((group) => ({
      ...group,
    }))

    becZone.value = DEFAULT_VALUES.BEC_ZONE
    siteSpeciesValues.value = DEFAULT_VALUES.SITE_SPECIES_VALUES
    bha50SiteIndex.value = DEFAULT_VALUES.BHA50_SITE_INDEX
    percentStockableArea.value = DEFAULT_VALUES.PERCENT_STOCKABLE_AREA
    startingAge.value = DEFAULT_VALUES.STARTING_AGE
    finishingAge.value = DEFAULT_VALUES.FINISHING_AGE
    ageIncrement.value = DEFAULT_VALUES.AGE_INCREMENT
    volumeReported.value = DEFAULT_VALUES.VOLUME_REPORTED
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
    updateSpeciesGroup,
    // site info
    becZone,
    ecoZone,
    incSecondaryHeight,
    siteSpeciesValues,
    bha50SiteIndex,
    // stand density
    percentStockableArea,

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
