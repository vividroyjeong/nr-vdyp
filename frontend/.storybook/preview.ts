import type { Preview } from '@storybook/vue3'
import { setup } from '@storybook/vue3'
import { registerPlugins } from '../src/plugins'
import { withVuetifyTheme } from './withVeutifyTheme.decorator'
import type { App } from 'vue'
import '@bcgov/bc-sans/css/BCSans.css'
import '../src/styles/style.scss'

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
  decorators: [
    withVuetifyTheme,
    (story) => ({
      components: { story },
      template: `
        <div style="font-family: 'BCSans', 'Noto Sans', Verdana, Arial, sans-serif;">
          <story />
        </div>
      `,
    }),
  ],
}

export default preview

// Setup function to register plugins
setup((app: App) => {
  registerPlugins(app)
})
