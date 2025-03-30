<template>
  <div class="agent-simulation-container">
    <div class="page-container">
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
  <!-- WebSocket 测试 -->
  <div class="section">
    <h2>WebSocket 测试</h2>
    <button @click="connectWebSocket" class="generate-bzzzutton">连接 WebSocket</button>
    <div id="wsLog">{{ wsLog }}</div>
  </div>
  </div>
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

// WebSocket 测试
const wsLog = ref('')
let ws = null

// WebSocket 连接
const connectWebSocket = () => {
  ws = new WebSocket('ws://localhost:8080/ws/simulation')

  ws.onopen = () => {
    logWsMessage('连接已建立')
    ws.send(
      JSON.stringify({
        type: 'start-dialogue',
        simulationId: 1,
        data: { prompt: '测试问题' },
      }),
    )
  }

  ws.onmessage = (event) => {
    logWsMessage('收到消息: ' + event.data)
  }

  ws.onerror = (error) => {
    logWsMessage('错误: ' + error.message)
  }

  ws.onclose = () => {
    logWsMessage('连接已关闭')
  }
}

const logWsMessage = (message) => {
  wsLog.value += `${new Date().toLocaleTimeString()}: ${message}\n`
}


</script>

<style scoped>
.generate-button,
.btn {
padding:10px 10px;
background-color: #006400; /* 墨绿色按钮 */
color: white;
border: none;
border-radius: 4px;
cursor: pointer;
transition: background-color 0.3s ease;

}
.generate-button:hover,
.btn:hover {
background-color: #004d00; /* 深一点的墨绿色悬停效果 */
}

</style>
