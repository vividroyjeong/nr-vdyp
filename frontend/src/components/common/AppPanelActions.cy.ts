import { mount } from 'cypress/vue'
import { createVuetify } from 'vuetify'
import 'vuetify/styles'
import AppPanelActions from './AppPanelActions.vue'

const vuetify = createVuetify()

describe('AppPanelActions.vue', () => {
  beforeEach(() => {
    cy.viewport(1024, 768)

    cy.document().then((doc) => {
      const style = doc.createElement('style')
      style.innerHTML = `
        body {
          background-color: rgb(240, 240, 240) !important;
        }
      `
      doc.head.appendChild(style)
    })
  })

  it('renders correctly with initial props', () => {
    mount(AppPanelActions, {
      props: { isConfirmEnabled: true, isConfirmed: false },
      global: {
        plugins: [vuetify],
      },
    })

    // Verify Clear button is rendered and enabled
    cy.contains('button', 'Clear').should('exist').and('not.be.disabled')

    // Verify Confirm button is rendered and enabled
    cy.contains('button', 'Confirm').should('exist').and('not.be.disabled')

    // Verify Edit button is not rendered initially
    cy.contains('button', 'Edit').should('not.visible')
  })

  it('disables buttons when isConfirmEnabled is false', () => {
    mount(AppPanelActions, {
      props: { isConfirmEnabled: false, isConfirmed: false },
      global: {
        plugins: [vuetify],
      },
    })

    // Verify Clear button is disabled
    cy.contains('button', 'Clear').should('exist').and('be.disabled')

    // Verify Confirm button is disabled
    cy.contains('button', 'Confirm').should('exist').and('be.disabled')
  })

  it('renders Edit button when isConfirmed is true', () => {
    mount(AppPanelActions, {
      props: { isConfirmEnabled: true, isConfirmed: true },
      global: {
        plugins: [vuetify],
      },
    })

    // Verify Clear button is rendered
    cy.contains('button', 'Clear').should('exist').and('not.be.disabled')

    // Verify Confirm button is not rendered
    cy.contains('button', 'Confirm').should('not.visible')

    // Verify Edit button is rendered
    cy.contains('button', 'Edit').should('exist').and('not.be.disabled')
  })

  it('emits events when buttons are clicked', () => {
    const onClearSpy = cy.spy().as('clearSpy')
    const onConfirmSpy = cy.spy().as('confirmSpy')
    const onEditSpy = cy.spy().as('editSpy')

    // Mount with isConfirmed = false
    mount(AppPanelActions, {
      props: {
        isConfirmEnabled: true,
        isConfirmed: false,
      },
      global: {
        plugins: [vuetify],
      },
      attrs: {
        onClear: onClearSpy,
        onConfirm: onConfirmSpy,
        onEdit: onEditSpy,
      },
    })

    // Click Clear button
    cy.contains('button', 'Clear').click()
    cy.get('@clearSpy').should('have.been.called')

    // Click Confirm button
    cy.contains('button', 'Confirm').click()
    cy.get('@confirmSpy').should('have.been.called')

    // Re-mount with isConfirmed = true
    mount(AppPanelActions, {
      props: { isConfirmEnabled: true, isConfirmed: true },
      global: {
        plugins: [vuetify],
      },
      attrs: {
        onClear: onClearSpy,
        onConfirm: onConfirmSpy,
        onEdit: onEditSpy,
      },
    })

    // Click Edit button
    cy.contains('button', 'Edit').click({ force: true })
    cy.get('@editSpy').should('have.been.called')
  })
})
