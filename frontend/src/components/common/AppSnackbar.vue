<template>
  <div>
    {{ SNACKBAR.SHOW_TIME }}
    <v-snackbar
      v-model="isShow"
      :timeout="SNACKBAR.SHOW_TIME"
      :color="type"
      class="elevation-12"
      location="top"
      @click:outside="closeMessage"
    >
      <div class="d-flex align-center">
        <v-icon class="mr-2">{{ getIcon(type) }}</v-icon>
        <span> {{ message }}</span>
      </div>

      <template #actions>
        <v-btn icon variant="text" @click="closeMessage">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>
<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useSnackbarStore } from '@/stores/common/snackbarStore'
import type { MessageType } from '@/types/types'
import { SNACKBAR } from '@/constants/constants'

const snackbarStore = useSnackbarStore()

const { isShow, message, type } = storeToRefs(snackbarStore)

const closeMessage = () => {
  snackbarStore.resetMessage()
}

const getIcon = (type: MessageType) => {
  const icon: { [key in MessageType]: string } = {
    '': '',
    info: 'mdi-information',
    success: 'mdi-check-circle',
    error: 'mdi-alert-circle',
    warning: 'mdi-alert',
  }

  return icon[type]
}
</script>
