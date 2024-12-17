<template>
  <v-dialog
    :model-value="computedDialog"
    @update:model-value="updateDialog"
    persistent
    :max-width="computedDialogWidth"
  >
    <v-card :style="computedDialogStyle">
      <v-card-title :style="computedHeaderStyle" class="popup-header">
        {{ computedTitle }}
      </v-card-title>
      <v-card-text
        v-show="Boolean(computedMessage)"
        class="pa-4"
        style="
          font-size: 14px;
          padding-left: 35px !important;
          padding-right: 35px !important;
          white-space: pre-line;
        "
      >
        {{ computedMessage }}
      </v-card-text>
      <v-card-actions
        class="pt-3"
        :style="{
          backgroundColor: computedActionsBackground,
          borderTop: '1px solid #0000001f',
        }"
      >
        <v-spacer></v-spacer>
        <v-btn class="blue-btn ml-2" @click="agree">{{
          computedBtnLabel
        }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, computed } from 'vue'
import { BUTTON_LABEL } from '@/constants/constants'

const props = defineProps<{
  dialog?: boolean
  title?: string
  message?: string
  dialogWidth?: number
  dialogBorderRadius?: number
  btnLabel?: string
  headerBackground?: string
  headerColor?: string
  actionsBackground?: string
}>()

const emit = defineEmits(['update:dialog', 'close'])

const computedDialog = computed(() => props.dialog ?? false)
const computedTitle = computed(() => props.title ?? '')
const computedMessage = computed(() => props.message ?? '')
const computedDialogWidth = computed(() => props.dialogWidth ?? 400)
const computedBtnLabel = computed(
  () => props.btnLabel ?? BUTTON_LABEL.CONT_EDIT,
)

const computedHeaderStyle = computed(() => {
  return {
    fontWeight: '300 !important',
    paddingLeft: '30px !important',
    padding: '1rem !important',
    background: props.headerBackground ?? '#003366 !important',
    color: props.headerColor ?? '#ffffff !important',
  }
})

const computedActionsBackground = computed(
  () => props.actionsBackground ?? '#f6f6f6 !important;',
)

const computedDialogStyle = computed(() => {
  return {
    borderRadius: `${props.dialogBorderRadius ?? 8}px`,
  }
})

// Emit updates for dialog visibility
const updateDialog = (value: boolean) => {
  emit('update:dialog', value)
}

// Emit close event and close the dialog
const agree = () => {
  emit('update:dialog', false)
  emit('close')
}
</script>

<style scoped>
.v-card-text {
  padding: 1rem !important;
}
</style>
