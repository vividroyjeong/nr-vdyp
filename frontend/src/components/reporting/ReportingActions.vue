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
      <PrintButton :isDisabled="isButtonDisabled" @print="handlePrint" />
      <v-spacer></v-spacer>
      <DownloadButton
        :isDisabled="isButtonDisabled"
        @download="handleDownload"
      />
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import PrintButton from '@/components/common/core/PrintButton.vue'
import DownloadButton from '@/components/common/core/DownloadButton.vue'

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
