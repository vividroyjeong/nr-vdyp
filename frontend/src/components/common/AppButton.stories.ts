import type { Meta, StoryObj } from '@storybook/vue3'
import AppButton from './AppButton.vue'

const meta: Meta<typeof AppButton> = {
  title: 'components/common/AppButton',
  component: AppButton,
  tags: ['autodocs'],
  argTypes: {
    label: {
      control: 'text',
      description: 'The label displayed on the button',
      defaultValue: 'Button',
    },
    isDisabled: {
      control: 'boolean',
      description: 'Disables the button if true',
      defaultValue: false,
    },
    primary: {
      control: 'boolean',
      description:
        'Determines the button style: primary (blue) or secondary (white)',
      defaultValue: true,
    },
    backgroundColor: {
      control: 'color',
      description: 'Background color',
    },
    onClick: {
      action: 'click',
      description: 'Emits when the button is clicked',
    },
  },
}

export default meta
type Story = StoryObj<typeof AppButton>

export const Primary: Story = {
  args: {
    label: 'Button',
    isDisabled: false,
    primary: true,
  },
}

export const Secondary: Story = {
  args: {
    label: 'Button',
    isDisabled: false,
    primary: false,
  },
}
