import AppButton from './AppButton.vue'

describe('<AppButton />', () => {
  const defaultProps = {
    label: 'Button',
    isDisabled: false,
    primary: true,
  }

  it('renders with default props', () => {
    cy.mount(AppButton, { props: defaultProps })

    // Check if the button exists
    cy.get('button').should('exist')

    // Verify the label text
    cy.get('button').should('contain', 'Button')

    // Check if the button has the primary style
    cy.get('button').should('have.class', 'blue-btn')
  })

  it('applies secondary style when primary is false', () => {
    cy.mount(AppButton, {
      props: {
        ...defaultProps,
        primary: false,
      },
    })

    // Verify the button has the secondary style
    cy.get('button').should('have.class', 'white-btn')
  })

  it('renders as disabled when isDisabled is true', () => {
    cy.mount(AppButton, {
      props: {
        ...defaultProps,
        isDisabled: true,
      },
    })

    // Verify the button is disabled
    cy.get('button').should('be.disabled')
  })

  it('emits click event when clicked', () => {
    const onClickSpy = cy.spy().as('onClickSpy')

    cy.mount(AppButton, {
      props: {
        ...defaultProps,
        onClick: onClickSpy,
      },
    })

    cy.get('button').click()

    cy.get('@onClickSpy').should('have.been.calledOnceWith', 1)
  })

  it('does not emit click event when disabled', () => {
    const onClickSpy = cy.spy().as('onClickSpy')

    cy.mount(AppButton, {
      props: {
        ...defaultProps,
        isDisabled: true,
        onClick: onClickSpy,
      },
    })

    cy.get('button').should('be.disabled')

    cy.get('@onClickSpy').should('not.have.been.called')
  })

  it('handles missing optional props', () => {
    cy.mount(AppButton, {
      props: {
        label: 'Test Button',
      },
    })

    // Verify the button renders with minimal props
    cy.get('button').should('contain', 'Test Button')
    cy.get('button').should('not.be.disabled')
    cy.get('button').should('have.class', 'blue-btn') // Default to primary style
  })

  it('renders with default props', () => {
    cy.mount(AppButton, { props: defaultProps })

    cy.get('button').should('exist')
    cy.get('button').should('contain', 'Button')
    cy.get('button').should('have.class', 'blue-btn')
  })
})
