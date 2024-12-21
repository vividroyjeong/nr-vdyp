<template>
  <v-card
    elevation="0"
    style="
      display: flex;
      justify-content: end;
      align-items: end;
      text-align: end;
    "
    class="mr-2"
  >
    <v-card-actions class="pr-0">
      <v-spacer></v-spacer>
      <AppButton
        label="Print"
        :isDisabled="isButtonDisabled"
        buttonClass="blue-btn"
        @click="handlePrint"
      />
      <v-spacer></v-spacer>
      <AppButton
        label="Download"
        :isDisabled="isButtonDisabled"
        buttonClass="white-btn"
        @click="handleDownload"
      />
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import AppButton from '@/components/common/AppButton.vue'

const props = defineProps({
  onPrint: {
    type: Function,
    required: true,
  },
  onDownload: {
    type: Function,
    required: true,
  },
  isButtonDisabled: {
    type: Boolean,
    required: true,
  },
})

const handlePrint = () => {
  props.onPrint()
}

const handleDownload = () => {
  props.onDownload()
}
</script>
