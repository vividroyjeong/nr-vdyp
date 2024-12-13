import { setup } from '@storybook/vue3'
import { registerPlugins } from '../src/plugins'
import { withVuetifyTheme } from './withVeutifyTheme.decorator.js'

setup((app) => {
  // Registers your app's plugins into Storybook
  registerPlugins(app)
})

// Add global theme types
export const globalTypes = {
  theme: {
    name: 'Theme',
    description: 'Global theme for components',
    toolbar: {
      icon: 'paintbrush',
      items: [
        { value: 'light', title: 'Light', left: '🌞' },
        { value: 'dark', title: 'Dark', left: '🌛' },
      ],
      dynamicTitle: true,
    },
  },
}

/* snipped for brevity */

export const decorators = [withVuetifyTheme]
