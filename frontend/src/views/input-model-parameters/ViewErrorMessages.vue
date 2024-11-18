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
      class="mr-2"
    >
      <v-card-actions class="pr-0">
        <v-spacer></v-spacer>
        <v-btn class="white-btn" @click="download">Download</v-btn>
        <v-btn class="blue-btn ml-2" @click="print">Print</v-btn>
      </v-card-actions>
    </v-card>
    <div
      id="err-msg-rslt-print-area"
      ref="errMsgRsltPrintAreaRef"
      class="ml-2 mr-2"
    >
      <h2 class="mt-5 mb-10">View Error Message Results</h2>
      <div
        class="v-row"
        v-for="(group, groupIndex) in chunkedErrMsgResults"
        :key="groupIndex"
      >
        <template v-for="(item, index) in group" :key="index">
          <!-- data column -->
          <div class="v-col-md-2 v-col-12 mb-5">
            <div style="border-bottom: 1px solid rgb(223, 220, 220)">
              <div
                class="mb-1 pl-3 pb-1 ml-3 readonly-label"
                :title="item.label"
                style="
                  white-space: nowrap;
                  overflow: hidden;
                  text-overflow: ellipsis;
                "
              >
                {{ item.label }}
                <div class="pl-0 readonly-text">{{ item.value }}</div>
              </div>
            </div>
          </div>
          <!-- empty column: Insert Except Last Column -->
          <div
            v-if="index < group.length - 1"
            class="v-col-md-1 v-col-12"
            style="max-width: 3%"
          ></div>
        </template>
      </div>
    </div>
  </v-container>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import printJS from 'print-js'
import { saveAs } from 'file-saver'
import { Util } from '@/utils/util'

const errMsgRsltPrintAreaRef = ref<HTMLElement | null>(null)

const errMsgResults = [
  { label: 'Insufficient Stockable Area Supplied', value: '55' },
  { label: 'CFS Eco Zone', value: 'Number not provided' },
  { label: 'Trees Per Hectare', value: '<Not Used>' },
  { label: 'Measured Basal Area', value: '<Not Used>' },
  { label: 'Additional Stand Attributes', value: 'None Applied' },
]

// Bundle data into groups of five
const chunkedErrMsgResults = errMsgResults.reduce(
  (acc, _, i) => {
    if (i % 5 === 0) acc.push(errMsgResults.slice(i, i + 5))
    return acc
  },
  [] as Array<Array<{ label: string; value: string }>>,
)

const download = () => {
  const content = 'Hello, this is your text file!'
  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })

  saveAs(blob, 'view-log-file.txt')
}

const print = () => {
  if (errMsgRsltPrintAreaRef.value) {
    const styles =
      Util.extractStylesFromDocument(document.styleSheets) +
      `
      @page {
        size: Letter portrait;
        margin: 7mm;
      }
      .v-col-md-2 { flex: 0 0 16.6666666667%; max-width: 16.6666666667%; }
      .readonly-label { font-size: 10px; }
      .readonly-text { font-size: 10px; }
      html, body { font-family: 'BCSans', 'Noto Sans', Verdana, Arial, sans-serif; font-size: 10px; font-weight: 400;}
    `

    printJS({
      printable: errMsgRsltPrintAreaRef.value.id,
      type: 'html',
      scanStyles: false,
      style: styles,
    })
  }
}
</script>
<style scoped></style>
