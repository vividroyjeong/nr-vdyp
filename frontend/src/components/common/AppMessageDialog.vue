<template>
  <v-dialog v-model="dialog" persistent :max-width="options.width">
    <v-card>
      <v-card-title
        style="font-weight: 300 !important; padding-left: 30px !important"
        class="popup-header"
        >{{ title }}</v-card-title
      >
      <v-card-text
        v-show="!!message"
        class="pa-4"
        style="
          font-size: 14px;
          padding-left: 35px !important;
          padding-right: 35px !important;
        "
      >
        {{ message }}
      </v-card-text>
      <v-card-actions
        class="pt-3"
        style="background-color: #f6f6f6; border-top: 1px solid #0000001f"
      >
        <v-spacer></v-spacer>
        <v-btn class="blue-btn ml-2" @click="agree">{{ btnLabel }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useMessageDialogStore } from '@/stores/common/messageDialogStore'

const messageDialogStore = useMessageDialogStore()

const dialog = computed(() => messageDialogStore.isOpen)
const title = computed(() => messageDialogStore.dialogTitle)
const message = computed(() => messageDialogStore.dialogMessage)
const btnLabel = computed(() => messageDialogStore.dialogBtnLabel)
const options = computed(() => messageDialogStore.dialogOptions)

const agree = () => {
  messageDialogStore.agree()
}
</script>

<style scoped>
.v-card-text {
  padding: 1rem !important;
}
</style>
