import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // 后端API的基础URL
  withCredentials: false,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json"
  }
});

// 创建模拟
export const createSimulation = dto => {
  return api.post("/simulation", dto);
};

// 获取模拟信息
export const getSimulation = () => {
  return api.get("/simulation");
};

// 结束当前模拟
export const endSimulation = () => {
  return api.post("/simulation/end");
};

// 创建Agent
export const createAgent = dto => {
  return api.post("/agent", dto);
};

// 启动对话
export const startDialogue = prompt => {
  return api.post("/start-dialogue", { prompt });
};
// 检查是否有类似这样的导出
export const getHarvestData = () => {
  // 具体的实现代码
};

// 随机天气
export const randomizeWeather = () => {
  return api.post("/random-weather");
};

// 创建农作物
export const createCrop = dto => {
  return api.post("/crop", dto);
};

// 获取收益对比
export const getRevenueComparison = () => {
  return api.get("/revenue-comparison");
};

// 启用专家模式
export const enableExpertMode = () => {
  return api.post("/enable-expert-mode");
};

// 创建环境
export const createEnvironment = dto => {
  return api.post("/environment", dto);
};

// 设置时间速率
export const setTimeScale = timeScale => {
  return api.post("/set-time-scale", null, { params: { timeScale } });
};

// 获取作物列表
export const getCrops = () => {
  return api.get("/crops");
};

// 获取对话历史
export const getDialogueHistory = () => {
  return api.get("/dialogue-history");
};

// 设置模拟时间加速
export const setSimulationTimeScale = (id, scale) => {
  return api.post(`/simulation/${id}/time-scale`, { scale });
};

// 切换模拟暂停状态
export const toggleSimulationPause = id => {
  return api.post(`/simulation/${id}/toggle-pause`);
};

// 获取当前模拟的JSON指令
export const getSimulationJson = () => {
  return api.post("/simulation/get-json");
};

// 创建收获记录
export const createHarvest = (data) => {
    return api.post('/harvest', data);
};

// 根据simulationId获取收获记录
export const getHarvestsBySimulationId = (simulationId) => {
    return api.get(`/harvest/${simulationId}`);
};
