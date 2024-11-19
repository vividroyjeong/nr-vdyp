<template>
  <div>
    <v-row class="mt-n5">
      <v-col cols="3" sm="3" md="3">
        <span class="search-label">Model</span>
        <v-select
          :items="modelTypeOptions"
          v-model="modelType"
          item-title="label"
          item-value="value"
          hide-details
          dense
          density="compact"
          variant="outlined"
          style="background-color: white !important"
        ></v-select>
      </v-col>
      <v-col cols="2" sm="2" md="2">
        <span class="search-label">Version</span>
        <v-select
          :items="engineVersionOptions"
          v-model="engineVersion"
          item-title="label"
          item-value="value"
          hide-details
          dense
          density="compact"
          variant="outlined"
          style="background-color: white !important"
        ></v-select>
      </v-col>
      <v-col cols="2" sm="2" md="2">
        <span class="search-label">Job ID</span>
        <v-text-field
          v-model="jobId"
          hide-details
          density="compact"
          dense
          variant="outlined"
          style="background-color: white !important"
          disabled
        ></v-text-field>
      </v-col>
    </v-row>
  </div>
</template>
<script setup lang="ts">
import { useAppStore } from '@/stores/appStore'
import { storeToRefs } from 'pinia'
import { modelTypeOptions, engineVersionOptions } from '@/constants/options'

const appStore = useAppStore()
const { modelType, engineVersion, jobId } = storeToRefs(appStore)
</script>

<style scoped>
.no-padding {
  padding: 0px;
}
.no-padding-bottom {
  padding-bottom: 0px;
}
</style>
