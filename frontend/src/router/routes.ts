import type { RouteRecordRaw } from 'vue-router'

// import JobList from '@/views/JobList.vue'
import ModelParameterInput from '@/views/input-model-parameters/ModelParameterInput.vue'
// import ModelParameterSelection from '@/views/input-model-parameters/ModelParameterSelection.vue'
import PageNotFound from '@/views/PageNotFound.vue'
import AuthInfo from '@/views/test/AuthInfo.vue'
import APITest from '@/views/test/APITest.vue'

export const routes: Array<RouteRecordRaw> = [
  // {
  //   path: '/',
  //   name: 'Home',
  //   component: JobList,
  // },
  {
    // path: '/input-model-parameters',
    path: '/',
    name: 'ModelParameterInput',
    component: ModelParameterInput,
    // children: [
    //   {
    //     path: 'model-parameter-selection',
    //     name: 'ModelParameterSelection',
    //     component: ModelParameterSelection,
    //   },
    // ],
  },
  {
    path: '/auth-info',
    name: 'AuthInfo',
    component: AuthInfo,
  },
  {
    path: '/api-test',
    name: 'APITest',
    component: APITest,
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: PageNotFound },
]
