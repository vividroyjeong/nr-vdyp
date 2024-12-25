// ***********************************************************
// This example support/component.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

import vuetify from '../../src/plugins/vuetify'
import { VApp } from 'vuetify/components'
import { mount } from 'cypress/vue'
import { h } from 'vue'
import { createPinia } from 'pinia'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import '@bcgov/bc-sans/css/BCSans.css'
import '../../src/styles/style.scss'

const pinia = createPinia()

Cypress.Commands.add('mount', (component, options = {}) => {
  options.global = options.global || {}
  options.global.plugins = options.global.plugins || []
  options.global.plugins.push(vuetify)
  options.global.plugins.push(pinia)

  return mount(
    {
      render() {
        return h(VApp, {}, [h(component, options.props)])
      },
    },
    options,
  )
})
