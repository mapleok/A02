import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

// 通用请求方法
const callApi = async (endpoint, method, body) => {
  try {
    const response = await axios({
      url: `${API_BASE}${endpoint}`,
      method,
      headers: { 'Content-Type': 'application/json' },
      data: body
    });
    return response.data;
  } catch (error) {
    return { error: error.message };
  }
};


// 启动对话
export const startDialogue = async (simulationId, prompt) => {
  return callApi(`/simulation/${simulationId}/start-dialogue`, 'POST', { prompt });
};

// 获取收成数据
export const getHarvestData = async (simulationId) => {
  try {
    const response = await axios.get(`${API_BASE}/harvest/${simulationId}`);
    return response.data;
  } catch (error) {
    return { error: error.message };
  }
};

export const createAgent = (agentData) => {
  // 函数逻辑
  return fetch('/api/agent', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(agentData)
  })
    .then(response => response.json());
};

export const createCrop = (cropData) => {
  // 函数逻辑
  return fetch('/api/crop', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(cropData)
  })
    .then(response => response.json());
};
