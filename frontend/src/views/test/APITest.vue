<template>
  <v-container fluid min-height="600px">
    <div>
      <v-btn @click="fetchTopLevel()">Fetch Top Level</v-btn>
      <v-btn @click="csvExport()">Export CSV</v-btn>
      <v-btn @click="search">Fetch Data</v-btn>
      <v-btn @click="createCode()">Create</v-btn>
      <v-form ref="codeForm" v-model="validForm" lazy-validation>
        <v-data-table-server
          :loading="loading"
          loading-text="Loading... Please wait"
          :headers="headers"
          :items="data.items"
          :items-length="data.itemsLength"
          :page="tblOptions.page + 1"
          :items-per-page="tblOptions.itemsPerPage"
          @update:options="onUpdateOptions"
          :items-per-page-options="[5, 10, 25, 50]"
          class="elevation-0"
        >
          <template v-slot:[`item.effectiveDate`]="{ item }">
            {{ item.effectiveDate }}
          </template>

          <template v-slot:[`item.actions`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ props }">
                <v-icon
                  aria-label="Edit"
                  class="mr-2"
                  @click="updateCode(item)"
                  v-bind="props"
                  >mdi-pencil</v-icon
                >
              </template>
              <span>Edit</span>
            </v-tooltip>

            <v-tooltip bottom>
              <template v-slot:activator="{ props }">
                <v-icon
                  aria-label="Delete"
                  class="mr-2"
                  @click="deleteCode(item)"
                  v-bind="props"
                  >mdi-delete</v-icon
                >
              </template>
              <span>Delete</span>
            </v-tooltip>
          </template>
        </v-data-table-server>
      </v-form>
    </div>
  </v-container>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { API } from '@/services/apiModules'
import { StatusCodes } from 'http-status-codes'
import { handleApiError } from '@/services/apiErrorHandler'
import * as messageHandler from '@/utils/messageHandler'
import { useConfirmDialogStore } from '@/stores/common/confirmDialogStore'
import { useProgressCircularStore } from '@/stores/common/progressCircularStore'
import type { CodeSearchParams, TableOptions } from '@/interfaces/interfaces'
import { SORT_ORDER } from '@/constants/constants'
import Code from '@/models/code'
import { env } from '@/env'

const loading = ref(false)

const confirmDialogStore = useConfirmDialogStore()
const progressCircularStore = useProgressCircularStore()

const headers = [
  { title: 'Date', key: 'effectiveDate', sortable: false },
  { title: 'Name', key: 'codeName', sortable: false },
  { text: 'Actions', value: 'actions', sortable: false, width: '180px' },
]

const tblOptions: TableOptions = reactive({
  page: 1,
  itemsPerPage: 5,
  sortBy: '',
  sortDesc: SORT_ORDER.ASC,
})

const data = reactive({
  items: [] as Array<Code>,
  itemsLength: 0,
})

const onUpdateOptions = async (_options: any) => {
  tblOptions.page = _options.page - 1
  tblOptions.itemsPerPage = _options.itemsPerPage

  if (!loading.value) {
    search().catch((error) => {
      console.error(error)
    })
  }
}

const search = async () => {
  loading.value = true

  try {
    const param: CodeSearchParams = {
      pageNumber: tblOptions.page,
      pageSize: tblOptions.itemsPerPage,
    }

    const response = await API.search.code(param)

    if (response.status === StatusCodes.OK && response?.data?.elements) {
      data.items = []

      for (const element of response.data.elements) {
        if (element.codeTableName === env.VITE_CODE_TB_NAME) {
          for (const code of element.codes) {
            data.items.push(
              new Code({
                codeTableName: element.codeTableName,
                codeName: code.codeName,
                description: code.description,
                displayOrder: code.displayOrder,
                effectiveDate: new Date(code.effectiveDate).toLocaleString(),
                expiryDate: code.expiryDate,
              }),
            )
          }
        }
      }
    }
  } catch (err) {
    handleApiError(err, 'Failed to search Codes')
  } finally {
    loading.value = false
  }
}

const validForm = ref(true)
const codeForm = ref()

const resetValidation = () => {
  codeForm.value?.resetValidation()
}

const createCode = async () => {
  const newCode = new Code({
    codeTableName: env.VITE_CODE_TB_NAME,
    codeName: 'test',
    description: 'test desc',
    displayOrder: 100,
    effectiveDate: new Date(),
    expiryDate: new Date(),
  })

  resetValidation()

  if (validForm.value) {
    try {
      const response = await API.create.code(newCode)

      messageHandler.messageResult(
        response?.status === StatusCodes.CREATED,
        'Code Created!',
        'Failed to create Code',
      )
    } catch (err) {
      handleApiError(err, 'Failed to create Code')
    } finally {
      search()
    }
  }
}

const deleteCode = async (item: Code): Promise<void> => {
  try {
    const fetchResult = await API.fetch.code(item.codeTableName, item.codeName)
    if (fetchResult) {
      const ifMatch = fetchResult.etag ? fetchResult.etag.toString() : '0'

      const isConfirmed = await confirmDialogStore.openDialog(
        'Confirm',
        'Are you sure you want to delete this Code?',
      )

      if (isConfirmed) {
        const deleteResult = await API.delete.code(
          item.codeTableName,
          item.codeName,
          ifMatch,
        )

        messageHandler.messageResult(
          deleteResult?.status === StatusCodes.NO_CONTENT,
          'Code Deleted!',
          'Failed to delete Code',
        )
      }
    } else {
      messageHandler.logWarningMessage(
        'Failed to delete Code. Please try again.',
        'Failed to fetch Code',
      )
    }
  } catch (err) {
    handleApiError(err, 'Failed to delete Code')
  } finally {
    search()
  }
}

const validate = (): boolean => {
  return codeForm.value?.validate()
}

const updateCode = async (item: Code): Promise<void> => {
  try {
    if (validate()) {
      const fetchResult = await API.fetch.code(
        item.codeTableName,
        item.codeName,
      )
      if (fetchResult) {
        // TODO sync edits with actual

        const ifMatch = fetchResult.etag ? fetchResult.etag.toString() : '0'
        const updateResult = await API.update.code(fetchResult, ifMatch)

        messageHandler.messageResult(
          updateResult?.status === StatusCodes.OK,
          'Code Updated!',
          'Failed to update Code',
        )
      } else {
        messageHandler.logWarningMessage(
          'Failed to update Code. Please try again.',
          'Failed to fetch Code',
        )
      }
    }
  } catch (err) {
    handleApiError(err, 'Failed to update Code')
  } finally {
    search()
  }
}

const fetchTopLevel = async (): Promise<void> => {
  try {
    const responseData = await API.fetch.topLevel()
    if (responseData?.links) {
      for (const link of responseData.links) {
        console.log(
          `method: ${link.method}, href: ${link.href}, rel: ${link.rel}`,
        )
      }
    } else {
      messageHandler.logWarningMessage(
        'Failed to fetch Code',
        null,
        false,
        true,
      )
    }
  } catch (err) {
    handleApiError(err, 'Failed to fetch top level')
  }
}

const csvExport = async (): Promise<void> => {
  progressCircularStore.showProgress()

  try {
    const blob = await API.search.csvExport()

    if (blob) {
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.style.display = 'none'
      a.href = url
      a.download = 'export.csv'
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)

      messageHandler.logSuccessMessage('CSV exported successfully!')
    } else {
      messageHandler.logWarningMessage('Failed to export CSV')
    }
  } catch (err) {
    handleApiError(err, 'Failed to export CSV')
  } finally {
    progressCircularStore.hideProgress()
  }
}
</script>
<style scoped></style>
