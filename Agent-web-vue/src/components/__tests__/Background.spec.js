// src/components/__tests__/Background.spec.js
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import Background from '../Background.vue';
import axios from 'axios';

// 模拟 axios
vi.mock('axios');

describe('Background.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(Background);
  });

  // 测试组件是否成功渲染
  it('renders properly', () => {
    expect(wrapper.exists()).toBe(true);
  });

  // 测试创建场景功能
  it('creates a new scene', async () => {
    const sceneNameInput = wrapper.find('#scene-name');
    const createSceneButton = wrapper.find('button.action-button');

    // 设置场景名称
    await sceneNameInput.setValue('Test Scene');

    // 点击创建场景按钮
    await createSceneButton.trigger('click');

    // 检查场景列表是否包含新创建的场景
    const sceneList = wrapper.find('.scene-list');
    expect(sceneList.text()).toContain('Test Scene');
  });

  // 测试创建模拟功能
  it('creates a new simulation', async () => {
    const mockResponse = { data: { id: 1 } };
    axios.post.mockResolvedValue(mockResponse);

    const createSimulationButton = wrapper.find('button').at(0);
    await createSimulationButton.trigger('click');

    // 检查模拟结果是否正确显示
    const simResultElement = wrapper.find('.sim-result');
    expect(simResultElement.text()).toContain('模拟 ID: 1');
  });

  // 测试更新温度功能
  it('updates the temperature', async () => {
    const temperatureInput = wrapper.find('#temperature');

    // 设置新的温度值
    await temperatureInput.setValue(30);

    // 触发输入事件
    await temperatureInput.trigger('input');

    // 检查温度值是否更新
    const rangeValue = wrapper.find('.range-value');
    expect(rangeValue.text()).toContain('30');
  });

  // 测试查看 Agent 对话记录功能
  it('shows agent conversation', async () => {
    const showConversationButton = wrapper.find('button.action-button').at(1);
    await showConversationButton.trigger('click');

    // 检查对话记录输入框是否显示
    const conversationInput = wrapper.find('.el-input-field');
    expect(conversationInput.exists()).toBe(true);
  });
});
