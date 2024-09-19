import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useSpeciesStore = defineStore('species', () => {
  const derivedBy = ref(null)

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
    { group: string; percent: number; siteSpecies: string }[]
  >([])

  const speciesOptions = ref<string[]>([
    'PL - Lodgepole',
    'AC - Popular',
    'H - Hemlock',
    'S - Spruce',
  ])

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

  const siteSpecies = computed(() => {
    if (speciesGroups.value.length === 0) return null
    return speciesGroups.value[0].siteSpecies
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
    }))

    speciesGroups.value.sort((a, b) => b.percent - a.percent)
  }

  return {
    derivedBy,
    speciesList,
    speciesGroups,
    speciesOptions,
    totalSpeciesPercent,
    totalSpeciesGroupPercent,
    isOverTotalPercent,
    siteSpecies,
    updateSpeciesGroup,
  }
})
