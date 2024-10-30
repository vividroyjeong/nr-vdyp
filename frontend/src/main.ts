import { createApp } from 'vue'
import { registerPlugins } from '@/plugins'
import App from './App.vue'
import { initializeKeycloak } from '@/services/keycloak'

// import Auth from './auth'
// import { useAuthStore } from '@/stores/common/authStore'

import '@bcgov/bc-sans/css/BCSans.css'
import '@/styles/style.scss'

// To ensure that unauthenticated users cannot see any part of the application, even momentarily
// delay the app initialization until Keycloak is fully initialized and the user's authentication state is confirmed.
const bootstrap = async () => {
  const app = createApp(App)

  registerPlugins(app)

  // API test -------------------------------------------
  // const auth = new Auth()
  // const path = auth.authorize()
  // if (path) {
  //   const token = auth.getJWTfromStorage()

  //   const authStore = useAuthStore()
  //   if (token) {
  //     authStore.setUser({
  //       accessToken: token,
  //       refToken: '',
  //       idToken: '',
  //     })

  //     authStore.loadUserFromStorage()
  //     console.log(authStore.user?.accessToken)
  //   }
  // }
  // console.log(`path : ${path}`)
  // API test end -------------------------------------------

  try {
    const keycloak = await initializeKeycloak()

    if (keycloak?.authenticated) {
      app.mount('#app')
    } else {
      keycloak?.login()
    }
  } catch (error) {
    console.error('Failed to initialize Keycloak:', error)
  }
}

bootstrap()
