import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Agentrole from '../Agentrole.vue'
import axios from 'axios'

vi.mock('axios')

describe('Agentrole.vue', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(Agentrole)
  })

  it('should call createAgent API when button is clicked', async () => {
    const agent1NameInput = wrapper.find('#agent1-name')
    await agent1NameInput.setValue('Test Agent')

    const agent1RoleInput = wrapper.find('#agent1-role')
    await agent1RoleInput.setValue('Test Role')

    const createAllAgentsButton = wrapper.find('button:contains("确定")')
    await createAllAgentsButton.trigger('click')

    expect(axios.post).toHaveBeenCalledWith('/api/agent', {
      agentName: 'Test Agent',
      roleType: 'Test Role'
    })
  })
})
