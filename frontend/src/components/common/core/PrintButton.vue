<template>
  <v-btn class="blue-btn" :disabled="isDisabled" @click="emitPrint">
    Print
  </v-btn>
</template>

<script setup lang="ts">
const props = defineProps({
  onPrint: {
    type: Function,
    required: true,
  },
  isDisabled: {
    type: Boolean,
    required: true,
  },
})

const emitPrint = () => {
  if (!props.isDisabled) {
    props.onPrint()
  }
}
</script>

<style scoped></style>
