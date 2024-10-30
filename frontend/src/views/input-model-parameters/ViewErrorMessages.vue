<template>
  <v-container fluid min-height="600px">
    <v-card
      elevation="0"
      style="
        display: flex;
        justify-content: end;
        align-items: end;
        text-align: end;
      "
    >
      <v-card-actions class="pr-0">
        <v-spacer></v-spacer>
        <v-btn class="white-btn mr-3" @click="download">Download</v-btn>
        <v-btn class="blue-btn mr-2" @click="print">Print</v-btn>
      </v-card-actions>
    </v-card>

    <div>
      <div id="print-area" ref="printAreaRef">
        <h1>print content</h1>
        <p>This will be printed.</p>
      </div>
    </div>
  </v-container>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import printJS from 'print-js'
import { saveAs } from 'file-saver'

const printAreaRef = ref<HTMLElement | null>(null)

const download = () => {
  const content = 'Hello, this is your text file!'
  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })

  saveAs(blob, 'view-log-file.txt')
}

const print = () => {
  if (printAreaRef.value) {
    printJS({
      printable: 'print-area',
      type: 'html',
    })
  }
}
</script>
<style scoped></style>
