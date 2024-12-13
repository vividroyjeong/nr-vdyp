import JobTypeSelection from '@/components/JobTypeSelection.vue'
import type { Meta, StoryFn } from '@storybook/vue3'
import { modelSelectionOptions } from '@/constants/options'

export default {
  title: 'Components/JobTypeSelection',
  component: JobTypeSelection,
  argTypes: {
    modelSelection: {
      control: { type: 'select' },
      options: modelSelectionOptions.map((option) => option.value),
      defaultValue: modelSelectionOptions[0]?.value,
    },
  },
} as Meta<typeof JobTypeSelection>

const Template: StoryFn<typeof JobTypeSelection> = (args) => ({
  components: { JobTypeSelection },
  setup() {
    return { args }
  },
  template: '<JobTypeSelection v-bind="args" />',
})

export const Default = Template.bind({})
Default.args = {
  modelSelection: modelSelectionOptions[0]?.value,
}
