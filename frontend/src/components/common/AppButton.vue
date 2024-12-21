<template>
  <v-btn :class="buttonClass" :disabled="isDisabled" @click="emitClick">
    {{ label }}
  </v-btn>
</template>

<script setup lang="ts">
const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  isDisabled: {
    type: Boolean,
    required: true,
  },
  buttonClass: {
    type: String,
    default: '',
  },
  onClick: {
    type: Function,
    required: true,
  },
})

const emitClick = () => {
  if (!props.isDisabled) {
    props.onClick()
  }
}
</script>

<style scoped></style>
