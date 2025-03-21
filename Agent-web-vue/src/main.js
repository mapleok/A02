import './assets/main.css'
import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
//添加代码1
import ElementPlus from 'element-plus'

const app = createApp(App)

//全局挂载
app.config.globalProperties.$axios = axios;

app.use(router)
app.use(ElementPlus)

app.mount('#app')
