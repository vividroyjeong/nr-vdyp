<template>
  <v-btn
    :class="primary ? 'blue-btn' : 'white-btn'"
    :disabled="isDisabled"
    :style="{ ...defaultStyles, ...computedStyle }"
    @click="onClick"
  >
    {{ label }}
  </v-btn>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  isDisabled: {
    type: Boolean,
    default: false,
  },
  primary: {
    type: Boolean,
    default: true,
  },
  backgroundColor: {
    type: String,
    default: null,
  },
})

const emit = defineEmits<(e: 'click', id: number) => void>()

const defaultStyles = {
  background: undefined,
}

const computedStyle = computed(() => {
  return {
    background: props.backgroundColor || undefined,
  }
})

const onClick = () => {
  if (!props.isDisabled) {
    emit('click', 1)
  }
}
</script>

<style scoped>
.blue-btn {
  background: #003366 !important;
  color: #ffffff !important;
  letter-spacing: 0.25px;
  padding-left: 25px;
  padding-right: 25px;
  font-weight: 500;
  box-shadow: none !important;
  text-transform: none;
}

.white-btn {
  background: #ffffff !important;
  color: #003366 !important;
  letter-spacing: 0.25px;
  border: 1px solid #003366;
  padding-left: 25px;
  padding-right: 25px;
  font-weight: 500;
  box-shadow: none !important;
  text-transform: none;
}

.v-btn--disabled.v-btn--variant-elevated,
.v-btn--disabled.v-btn--variant-flat {
  opacity: 0.26;
}
</style>
