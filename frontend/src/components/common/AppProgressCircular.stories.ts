import type { Meta, StoryObj } from '@storybook/vue3'
import AppProgressCircular from '@/components/common/AppProgressCircular.vue'

const meta: Meta<typeof AppProgressCircular> = {
  title: 'components/common/AppProgressCircular',
  component: AppProgressCircular,
  argTypes: {
    isShow: { control: { type: 'boolean' }, defaultValue: true },
    showMessage: { control: { type: 'boolean' }, defaultValue: true },
    message: { control: { type: 'text' }, defaultValue: 'Loading...' },
    circleSize: {
      control: { type: 'range', min: 10, max: 200, step: 1 },
      defaultValue: 70,
      description: 'Size of the circular progress indicator',
    },
    circleWidth: {
      control: { type: 'range', min: 1, max: 50, step: 1 },
      defaultValue: 5,
      description: 'Width of the circular progress indicator',
    },
    circleColor: {
      control: { type: 'color' },
      defaultValue: 'primary',
      description: 'Color of the circular progress indicator',
    },
    backgroundColor: {
      control: { type: 'color' },
      defaultValue: 'rgba(255, 255, 255, 0.8)',
      description: 'Background color of the wrapper div',
    },
    hasBackground: {
      control: { type: 'boolean' },
      defaultValue: true,
      description: 'Whether the background color is applied',
    },
    padding: {
      control: { type: 'range', min: 0, max: 50, step: 1 },
      defaultValue: 20,
      description: 'Padding around the circular progress',
    },
    borderRadius: {
      control: { type: 'range', min: 0, max: 50, step: 1 },
      defaultValue: 10,
      description: 'Border radius of the wrapper div',
    },
  },
}

export default meta

type Story = StoryObj<typeof AppProgressCircular>

export const Primary: Story = {
  args: {
    isShow: true,
    showMessage: true,
    message: 'Loading...',
    circleSize: 70,
    circleWidth: 5,
    circleColor: 'primary',
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    hasBackground: true,
    padding: 20,
    borderRadius: 10,
  },
}

export const NoBackground: Story = {
  args: {
    ...Primary.args,
    hasBackground: false,
  },
}
