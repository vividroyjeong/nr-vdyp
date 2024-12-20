<template>
  <v-container>
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
        <v-btn class="white-btn" @click="download">Download</v-btn>
        <v-btn class="blue-btn ml-2" @click="print">Print</v-btn>
      </v-card-actions>
    </v-card>
    <div
      class="ml-2 mr-2"
      style="
        white-space: pre;
        font-family: 'Courier New', Courier, monospace;
        overflow-y: scroll;
        height: 420px;
        font-size: 14px;
        line-height: 1.5;
        overflow-x: auto;
        border: 1px solid #ccc;
        padding: 10px;
        background-color: #f9f9f9;
      "
    >
      {{ formattedText }}
    </div>
  </v-container>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useProjectionStore } from '@/stores/projectionStore'
import { FILE_NAME } from '@/constants/constants'
import printJS from 'print-js'
import { saveAs } from 'file-saver'

const projectionStore = useProjectionStore()
const items = computed(() => projectionStore.yieldTableArray)

const formattedText = computed(() => items.value.join('\n'))

const download = () => {
  // Convert items to CSV format
  const csvContent = items.value
    .map((row) => row.split(',').join(','))
    .join('\n')

  // Create a Blob with CSV content
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })

  saveAs(blob, FILE_NAME.YIELD_TABLE_CSV)
}

const print = () => {
  const container = document.createElement('div')
  container.style.fontFamily = "'Courier New', Courier, monospace"
  container.style.fontSize = '10px'
  container.style.whiteSpace = 'pre'
  container.style.lineHeight = '1.5'
  container.textContent = formattedText.value

  const printStyles = `
    @page {
      size: A4 landscape;
      margin: 10mm;
    }
    body {
      font-family: 'Courier New', Courier, monospace;
      font-size: 10px;
      white-space: pre;
      line-height: 1.5;
    }
  `

  printJS({
    printable: container.innerHTML,
    type: 'raw-html',
    style: printStyles,
  })
}
</script>
<style scoped></style>
