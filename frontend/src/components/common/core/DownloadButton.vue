<template>
  <v-btn class="white-btn" :disabled="isDisabled" @click="emitDownload">
    Download
  </v-btn>
</template>

<script setup lang="ts">
const props = defineProps({
  onDownload: {
    type: Function,
    required: true,
  },
  isDisabled: {
    type: Boolean,
    required: true,
  },
})

const emitDownload = () => {
  if (!props.isDisabled) {
    props.onDownload()
  }
}
</script>

<style scoped></style>
