import type { Meta, StoryObj } from '@storybook/vue3'

import CardView from '../components/CardView.vue'
import { title } from 'process'

const meta: Meta<typeof CardView> = {
  component: CardView,
}

export default meta
type Story = StoryObj<typeof CardView>

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { CardView },
    setup() {
      return { args }
    },
    template: '<CardView v-bind="args" />',
  }),
  args: {
    // 👇 The args you need here will depend on your component
    title: 'Primary',
    subText: 'Example sub text',
  },
}
