// src/api/index.js
import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8080', // 替换为你的后端 API 基础地址
  timeout: 5000 // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么，例如添加请求头
    return config;
  },
  error => {
    // 处理请求错误
    console.log(error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response.data;
  },
  error => {
    // 处理响应错误
    console.log('err' + error);
    return Promise.reject(error);
  }
);

// 封装创建模拟接口
export const createSimulation = (data) => {
  return service({
    url: '/api/simulation',
    method: 'post',
    data
  });
};

// 封装创建 Agent 接口
export const createAgent = (data) => {
  return service({
    url: '/api/agent',
    method: 'post',
    data
  });
};

// 封装创建作物接口
export const createCrop = (data) => {
  return service({
    url: '/api/crop',
    method: 'post',
    data
  });
};

// 封装获取环境数据接口
export const getLatestEnvironment = () => {
  return service({
    url: '/api/environment',
    method: 'get'
  });
};
