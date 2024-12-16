<template>
  <div
    class="centered-progress progress-wrapper"
    v-show="computedIsShow"
    :style="{
      backgroundColor: computedHasBackground
        ? computedBackgroundColor
        : 'transparent',
      boxShadow: computedHasBackground
        ? '0 2px 4px rgba(0, 0, 0, 0.1)'
        : 'none',
      borderRadius: `${computedBorderRadius}px`,
      padding: `${computedPadding}px`,
    }"
  >
    <v-progress-circular
      indeterminate
      :size="computedCircleSize"
      :width="computedCircleWidth"
      :color="computedCircleColor"
    ></v-progress-circular>
    <div v-show="computedShowMessage" class="message">
      {{ computedMessage }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps } from 'vue'

const props = defineProps<{
  isShow?: boolean
  showMessage?: boolean
  message?: string
  circleSize?: number
  circleWidth?: number
  circleColor?: string
  backgroundColor?: string
  hasBackground?: boolean
  padding?: number
  borderRadius?: number
}>()

const computedIsShow = computed(() => props.isShow ?? false)
const computedShowMessage = computed(() => props.showMessage ?? true)
const computedMessage = computed(() => props.message ?? 'Loading...')
const computedCircleSize = computed(() => props.circleSize ?? 70)
const computedCircleWidth = computed(() => props.circleWidth ?? 5)
const computedCircleColor = computed(() => props.circleColor ?? 'primary')
const computedBackgroundColor = computed(
  () => props.backgroundColor ?? 'rgba(255, 255, 255, 0.8)',
)
const computedHasBackground = computed(() => props.hasBackground ?? true)
const computedPadding = computed(() => props.padding ?? 20)
const computedBorderRadius = computed(() => props.borderRadius ?? 10)
</script>

<style scoped>
.centered-progress {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
}
.progress-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.message {
  font-weight: bold;
  color: rgba(0, 0, 0, 0.87);
  margin-top: 10px;
}
</style>
