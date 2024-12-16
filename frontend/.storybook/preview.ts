import type { Preview } from '@storybook/vue3'
import { setup } from '@storybook/vue3'
import { registerPlugins } from '../src/plugins'
import { withVuetifyTheme } from './withVeutifyTheme.decorator'
import type { App } from 'vue'

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
  // Add global theme types
  globalTypes: {
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
  },
  decorators: [withVuetifyTheme],
}

export default preview

// Setup function to register plugins
setup((app: App) => {
  registerPlugins(app)
})
