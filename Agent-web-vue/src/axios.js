import axios from 'axios';

const instance = axios.create({
  baseURL: 'https://localhost:8080', // 将这里替换为你的后端 API 地址和端口号
  timeout: 5000 // 请求超时时间，单位为毫秒
});

export default instance;
