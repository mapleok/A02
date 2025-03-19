<template>
  <div class="agent-simulation-container">
    <h3>Agent 合作系统模拟 - 后端 API 调用数据显示</h3>
    <!-- 搜索框，用于筛选 API 调用记录 -->
    <el-input
      v-model="searchQuery"
      placeholder="搜索 API 调用记录"
      @input="filterApiCalls"
      style="margin-bottom: 20px"
    />
    <!-- 表格展示 API 调用数据 -->
    <el-table :data="filteredApiCalls" style="width: 100%">
      <el-table-column prop="apiName" label="API 名称" />
      <el-table-column prop="requestTime" label="请求时间" />
      <el-table-column prop="responseTime" label="响应时间" />
      <el-table-column prop="statusCode" label="状态码" />
      <el-table-column prop="responseData" label="响应数据" />
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="filteredApiCalls.length"
      style="margin-top: 20px"
    />
  </div>

  <!--  <div>-->
  <!--    &lt;!&ndash; 创建模拟相关 &ndash;&gt;-->
  <!--    <h2>创建模拟</h2>-->
  <!--    <input id="simName" type="text" placeholder="模拟名称" />-->
  <!--    <input id="simDesc" type="text" placeholder="模拟描述" />-->
  <!--    <button @click="createSimulation">创建模拟</button>-->
  <!--    <div id="simResult"></div>-->

  <!--    &lt;!&ndash; 创建Agent相关 &ndash;&gt;-->
  <!--    <h2>创建Agent</h2>-->
  <!--    <input id="agentName" type="text" placeholder="Agent名称" />-->
  <!--    <select id="agentRole">-->
  <!--      <option value="role1">角色1</option>-->
  <!--      <option value="role2">角色2</option>-->
  <!--    </select>-->
  <!--    <button @click="createAgent">创建Agent</button>-->
  <!--    <div id="agentResult"></div>-->

  <!--    &lt;!&ndash; 创建作物相关 &ndash;&gt;-->
  <!--    <h2>创建作物</h2>-->
  <!--    <input id="cropName" type="text" placeholder="作物名称" />-->
  <!--    <input id="growthRate" type="number" placeholder="生长速率" />-->
  <!--    <button @click="createCrop">创建作物</button>-->
  <!--    <div id="cropResult"></div>-->

  <!--    &lt;!&ndash; WebSocket状态显示 &ndash;&gt;-->
  <!--    <p>WebSocket状态: <span id="wsStatus"></span></p>-->

  <!--    &lt;!&ndash; 对话相关 &ndash;&gt;-->
  <!--    <h2>对话</h2>-->
  <!--    <div id="dialogueHistory"></div>-->
  <!--    <input id="promptInput" type="text" placeholder="输入指令" />-->
  <!--    <button @click="sendPrompt">发送指令</button>-->

  <!--    &lt;!&ndash; 获取环境数据相关 &ndash;&gt;-->
  <!--    <h2>环境数据</h2>-->
  <!--    <button @click="getLatestEnvironment">获取最新环境数据</button>-->
  <!--    <div id="envData"></div>-->
  <!--  </div>-->
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
// import axios from 'axios'

// 模拟后端 API 调用数据
const apiCalls = reactive([
  {
    apiName: 'API1',
    requestTime: '2025-03-14 10:00:00',
    responseTime: '2025-03-14 10:00:02',
    statusCode: 200,
    responseData: '{"message": "Success"}',
  },
  {
    apiName: 'API2',
    requestTime: '2025-03-14 10:05:00',
    responseTime: '2025-03-14 10:05:03',
    statusCode: 200,
    responseData: '{"data": "Some data"}',
  },
  {
    apiName: 'API3',
    requestTime: '2025-03-14 10:10:00',
    responseTime: '2025-03-14 10:10:05',
    statusCode: 500,
    responseData: '{"error": "Internal Server Error"}',
  },
  {
    apiName: 'API4',
    requestTime: '2025-03-14 11:00:00',
    responseTime: '2025-03-14 11:00:04',
    statusCode: 200,
    responseData: '{"result": "OK"}',
  },
  {
    apiName: 'API5',
    requestTime: '2025-03-14 11:05:00',
    responseTime: '2025-03-14 11:05:06',
    statusCode: 200,
    responseData: '{"info": "Some information"}',
  },
  {
    apiName: 'API6',
    requestTime: '2025-03-14 11:10:00',
    responseTime: '2025-03-14 11:10:08',
    statusCode: 404,
    responseData: '{"error": "Not Found"}',
  },
])

// 搜索关键字
const searchQuery = ref('')
// 当前页码
const currentPage = ref(1)
// 每页显示数量
const pageSize = ref(5)
// 当前模拟ID
// const currentSimulationId = ref(null)
// // WebSocket实例
// const ws = ref(null)

// 过滤 API 调用数据
const filterApiCalls = () => {
  if (!searchQuery.value) {
    return apiCalls
  }
  return apiCalls.filter((call) => {
    return call.apiName.toLowerCase().includes(searchQuery.value.toLowerCase())
  })
}

// 计算过滤后的 API 调用数据
const filteredApiCalls = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  const filtered = filterApiCalls()
  return filtered.slice(start, end)
})

// 处理每页显示数量变化
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
}

// 处理页码变化
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

// // WebSocket连接
// const connectWebSocket = () => {
//   ws.value = new WebSocket('ws://localhost:8080/ws/simulation')
//   ws.value.onopen = () => {
//     document.getElementById('wsStatus').textContent = '（已连接）'
//     console.log('WebSocket connected')
//   }
//   ws.value.onmessage = (event) => {
//     const dialogueHistory = document.getElementById('dialogueHistory')
//     dialogueHistory.innerHTML += `<div class="message">Agent回复: ${event.data}</div>`
//     dialogueHistory.scrollTop = dialogueHistory.scrollHeight
//   }
//   ws.value.onclose = () => {
//     document.getElementById('wsStatus').textContent = '（连接断开）'
//     console.log('WebSocket disconnected')
//   }
// }

// // 创建模拟
// const createSimulation = async () => {
//   try {
//     const response = await axios.post('/api/simulation', {
//       name: document.getElementById('simName').value,
//       description: document.getElementById('simDesc').value,
//     })
//     currentSimulationId.value = response.data.id
//     document.getElementById('simResult').innerHTML = `模拟ID: ${response.data.id}`
//     connectWebSocket()
//   } catch (error) {
//     console.error('创建模拟失败', error)
//   }
// }

// // 创建Agent
// const createAgent = async () => {
//   try {
//     const response = await axios.post('/api/agent', {
//       agentName: document.getElementById('agentName').value,
//       roleType: document.getElementById('agentRole').value,
//     })
//     document.getElementById('agentResult').innerHTML = `Agent ID: ${response.data.id}`
//   } catch (error) {
//     console.error('创建Agent失败', error)
//   }
// }

// // 创建作物
// const createCrop = async () => {
//   try {
//     const response = await axios.post('/api/crop', {
//       cropName: document.getElementById('cropName').value,
//       growthRate: parseFloat(document.getElementById('growthRate').value),
//     })
//     document.getElementById('cropResult').innerHTML = `作物ID: ${response.data.id}`
//   } catch (error) {
//     console.error('创建作物失败', error)
//   }
// }

// // 发送对话指令
// const sendPrompt = () => {
//   const prompt = document.getElementById('promptInput').value
//   if (ws.value && ws.value.readyState === WebSocket.OPEN) {
//     ws.value.send(JSON.stringify({ content: prompt }))
//     document.getElementById('dialogueHistory').innerHTML +=
//       `<div class="message">用户指令: ${prompt}</div>`
//     document.getElementById('promptInput').value = ''
//   }
// }

// // 获取环境数据
// const getLatestEnvironment = async () => {
//   try {
//     const response = await axios.get('/api/environment')
//     document.getElementById('envData').innerHTML = `
//             温度: ${response.data.temperature.toFixed(1)}℃<br>
//             土壤肥力: ${(response.data.soilFertility * 100).toFixed(0)}%<br>
//             降水量: ${response.data.precipitation.toFixed(1)}mm
//           `
//   } catch (error) {
//     console.error('获取环境数据失败', error)
//   }
// }
</script>
