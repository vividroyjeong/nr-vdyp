// src/router/router.ts
import { createRouter, createWebHistory } from 'vue-router'
import { routes } from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  if (
    to.hash.includes('code=') &&
    to.hash.includes('state=') &&
    to.hash.includes('session_state=') &&
    to.hash.includes('iss=')
  ) {
    const hashParams = new URLSearchParams(to.hash.slice(1))
    hashParams.delete('code')
    hashParams.delete('state')
    hashParams.delete('session_state')
    hashParams.delete('iss')

    const newHash = hashParams.toString() ? `#${hashParams.toString()}` : ''

    next({
      path: to.path + newHash,
      replace: true,
    })
  } else {
    next()
  }
})

export default router
