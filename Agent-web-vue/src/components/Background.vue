<template>
  <div class="page-container">
    <!-- 场景创建与管理部分 -->
    <div class="card">
    <div class="scene-management">
      <h2 class="section-title">场景创建与管理</h2>
      <div class="input-group">
        <label for="scene-name" class="input-label">场景名称</label>
        <input
          type="text"
          id="scene-name"
          v-model="sceneName"
          placeholder="输入场景名称"
          class="input-field"
        />
        <button @click="createSimulation">创建模拟</button>
        <button @click="handleCustomizeAgent">自定义 Agent</button>
        <button @click="createScene" class="action-button">创建场景</button>
      </div>
      <div class="input-group">
        <label for="scene-select" class="input-label">选择场景</label>
        <select id="scene-select" v-model="selectedSceneId" @change="loadScene" class="input-field">
          <option value="">无</option>
          <option v-for="scene in scenes" :key="scene.id" :value="scene.id">
            {{ scene.name }}
          </option>
        </select>
      </div>
      <div class="environment-controls">
        <h3 class="subsection-title">环境控制</h3>
        <div class="control-group">
          <label for="temperature" class="input-label">温度 (℃)</label>
          <input
            type="range"
            id="temperature"
            v-model="currentTemperature"
            min="0"
            max="50"
            @input="updateTemperature"
            class="range-input"
          />
          <span class="range-value">{{ currentTemperature }}</span>
        </div>
        <div class="control-group">
          <label for="moisture" class="input-label">降水量</label>
          <select
            id="moisture"
            v-model="currentMoisture"
            @change="updateMoisture"
            class="input-field"
          >
            <option value="DRY"><500mm</option>
            <option value="NORMAL"><500mm,1000mm></option>
            <option value="WET">>1000mm</option>
          </select>
        </div>
        <div class="control-group">
          <label for="fertility" class="input-label">土壤肥沃</label>
          <select
            id="fertility"
            v-model="currentFertility"
            @change="updateFertility"
            class="input-field"
          >
            <option value="LOW">贫瘠</option>
            <option value="MEDIUM">中等</option>
            <option value="HIGH">肥沃</option>
          </select>
        </div>
        <div class="control-group">
          <label for="weather" class="input-label">气候情况</label>
          <select id="weather" v-model="currentWeather" @change="updateWeather" class="input-field">
            <option value="SUNNY">温带季风气候</option>
            <option value="CLOUDY">亚寒带季风气候</option>
            <option value="RAINY">内陆干旱气候</option>
          </select>
        </div>
      </div>

      <div v-if="selectedScene" class="scene-info">
        <h3 class="subsection-title">场景详情</h3>
        <p><span class="info-label">场景名称:</span> {{ selectedScene.name }}</p>
        <p><span class="info-label">温度:</span> {{ selectedScene.temperature }}℃</p>
        <p><span class="info-label">气候情况:</span> {{ selectedScene.moisture }}</p>
        <p><span class="info-label">土壤肥沃度:</span> {{ selectedScene.fertility }}</p>
        <p><span class="info-label">降水量:</span> {{ selectedScene.weather }}</p>
      </div>
      <div v-if="scenes.length > 0" class="scene-list">
        <h3 class="subsection-title">所有场景列表</h3>
        <ul>
          <li v-for="scene in scenes" :key="scene.id">
            {{ scene.name }} - 温度: {{ scene.temperature }}℃, 干湿情况: {{ scene.moisture }},
            土壤肥沃度: {{ scene.fertility }}, 天气: {{ scene.weather }}
          </li>
        </ul>
      </div>
    </div>
    <!-- Agent 对话查询部分 -->
    <div class="agent-conversation">
      <!-- 修改：添加 agent-conversation 类 -->
      <button @click="showAgentConversation" class="action-button">查看 Agent 对话记录</button>
      <div v-if="showConversation" class="input-container">
        <div class="input-group">
          <div class="input-item">
            <h3 class="subsection-title">查询条件</h3>
            <el-input v-model="query" clearable placeholder="输入查询条件" class="el-input-field" />
          </div>
          <div class="input-item">
            <el-button @click="fetchAgentConversation" class="action-button">查询</el-button>
          </div>
        </div>
        <div class="input-group">
          <div class="input-item">
            <h3 class="subsection-title">Agent 对话记录</h3>
            <el-input
              type="textarea"
              v-model="conversationContent"
              :rows="10"
              readonly
              placeholder="这里将显示 agent 互相对话的内容"
              class="el-input-field"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
    <div class="card">
  <!-- 农场任务选择 -->
  <div class="task-selection">
    <h3>农场任务分配</h3>
    <div class="input-group inline-input-group">
      <label for="selected-tasks">选择任务：</label>
      <el-select v-model="selectedTasks" multiple placeholder="请选择农场任务">
        <el-option
          v-for="task in farmTasks"
          :key="task.value"
          :label="task.label"
          :value="task.value"
        />
      </el-select>
    </div>
  </div>
  <!-- 农场技术选择 -->
  <div class="technology-selection">
    <h3>农场技术选择</h3>
    <div class="input-group inline-input-group">
      <label for="selected-technologies">选择技术：</label>
      <el-select v-model="selectedTechnologies" multiple placeholder="请选择农场技术">
        <el-option
          v-for="tech in farmTechnologies"
          :key="tech.value"
          :label="tech.label"
          :value="tech.value"
        />
      </el-select>
    </div>
  </div>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'
import { createAgent } from '@/api/index.js'

// 场景创建与管理部分数据
const sceneName = ref('')
const scenes = reactive([])
const selectedSceneId = ref('')
const selectedScene = ref(null)
const currentTemperature = ref(25)
const currentMoisture = ref('NORMAL')
const currentFertility = ref('MEDIUM')
const currentWeather = ref('SUNNY')

// Agent 对话查询部分数据
const query = ref('')
const conversationContent = ref('')
const showConversation = ref(false)

// 定义 API 基础地址和模拟相关数据
const API_BASE = 'http://localhost:8080/api'
const simName = ref('')
const simDesc = ref('')
const simResult = ref('')

// 模拟相关方法
const createSimulation = async () => {
  try {
    const response = await axios.post(`${API_BASE}/simulation`, {
      name: simName.value,
      description: simDesc.value,
    })
    simResult.value = JSON.stringify(response.data, null, 2)
  } catch (error) {
    simResult.value = `错误: ${error.message}`
  }
}

// 场景相关方法
const createScene = () => {
  if (sceneName.value) {
    const newScene = {
      id: Date.now(),
      name: sceneName.value,
      temperature: currentTemperature.value,
      moisture: currentMoisture.value,
      fertility: currentFertility.value,
      weather: currentWeather.value,
    }
    scenes.push(newScene)
    sceneName.value = ''
    console.log(`创建新场景: ${newScene.name}`)
  } else {
    console.error('请输入场景名称')
  }
}

const loadScene = () => {
  const scene = scenes.find((s) => s.id === selectedSceneId.value)
  if (scene) {
    selectedScene.value = scene
    currentTemperature.value = scene.temperature
    currentMoisture.value = scene.moisture
    currentFertility.value = scene.fertility
    currentWeather.value = scene.weather
  }
}

const updateTemperature = (event) => {
  currentTemperature.value = parseInt(event.target.value)
  if (selectedScene.value) {
    selectedScene.value.temperature = currentTemperature.value
  }
  console.log(`更新温度为: ${currentTemperature.value}℃`)
}

const updateMoisture = () => {
  if (selectedScene.value) {
    selectedScene.value.moisture = currentMoisture.value
  }
  console.log(`更新干湿情况为: ${currentMoisture.value}`)
}

const updateFertility = () => {
  if (selectedScene.value) {
    selectedScene.value.fertility = currentFertility.value
  }
  console.log(`更新土壤肥沃度为: ${currentFertility.value}`)
}

const updateWeather = () => {
  if (selectedScene.value) {
    selectedScene.value.weather = currentWeather.value
  }
  console.log(`更新天气为: ${currentWeather.value}`)
}

// Agent 对话查询方法
const fetchAgentConversation = async () => {
  try {
    // 这里模拟 API 调用，实际开发中需要替换为真实的 API 地址
    const response = await fetch(`/api/agent-conversation?query=${query.value}`)
    if (response.ok) {
      const data = await response.json()
      // 将获取到的对话内容赋值给 conversationContent
      conversationContent.value = data.conversation
    } else {
      conversationContent.value = '获取对话记录失败，请稍后重试。'
    }
  } catch (error) {
    console.error('获取对话记录时出错：', error)
    conversationContent.value = '获取对话记录时出现错误，请检查网络。'
  }
}

const showAgentConversation = () => {
  showConversation.value = !showConversation.value
}

// 农场任务选项
const farmTasks = [
  { value: 'planting', label: '种植任务' },
  { value: 'harvesting', label: '收割任务' },
  { value: 'fertilizing', label: '施肥任务' },
  { value: 'pest_control', label: '病虫害防治任务' },
]

// 农场技术选项
const farmTechnologies = [
  { value: 'precision_agriculture', label: '精准农业技术' },
  { value: 'hydroponics', label: '水培技术' },
  { value: 'vertical_farming', label: '垂直农业技术' },
  { value: 'drone_technology', label: '无人机技术' },
]

// 选择的农场任务
const selectedTasks = ref([])

// 选择的农场技术
const selectedTechnologies = ref([])

// 定义响应式数据

const agentResult = ref('')
const cropResult = ref('')
const envData = ref('')

// 创建模拟
const handleCreateSimulation = async () => {
  try {
    const data = {
      name: '模拟名称',
      description: '模拟描述',
    }
    const result = await createSimulation(data)
    simResult.value = `模拟 ID: ${result.id}`
  } catch (error) {
    console.error('创建模拟失败:', error)
    simResult.value = '创建模拟失败，请稍后重试'
  }
}

// 创建 Agent
const handleCreateAgent = async () => {
  try {
    const data = {
      agentName: agent1Name.value || agent2Name.value || agent3Name.value,
      roleType: agent1Role.value || agent2Role.value || agent3Role.value,
    }
    const result = await createAgent(data)
    agentResult.value = `Agent ID: ${result.id}`
  } catch (error) {
    console.error('创建 Agent 失败:', error)
    agentResult.value = '创建 Agent 失败，请稍后重试'
  }
}
</script>

<style scoped>
/* 整体页面容器样式 */
.page-container {
  font-family:
    'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif; /* Apple 常用字体 */
  padding: 0; /* 去掉内边距 */
  background-color: #fff; /* 白色背景 */
  border-radius: 0; /* 去掉圆角 */
  box-shadow: none; /* 去掉阴影 */
  max-width: none; /* 不限制最大宽度 */
  width: 100%; /* 占据整个页面宽度 */
  min-height: 100vh; /* 占据整个页面高度 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 场景管理部分样式 */
.scene-management {
  background-color: transparent; /* 无色背景 */
  border-radius: 0; /* 去掉圆角 */
  padding: 20px;
  box-shadow: none; /* 去掉阴影 */
  margin-bottom: 20px;
  width: 80%; /* 可根据需要调整宽度 */
}

/* Agent 对话查询部分样式 */
.agent-conversation {
  background-color: transparent; /* 无色背景 */
  border-radius: 0; /* 去掉圆角 */
  padding: 20px;
  box-shadow: none; /* 去掉阴影 */
  margin-top: 20px;
  width: 80%; /* 可根据需要调整宽度 */
}

/* 标题样式 */
.section-title {
  font-size: 22px;
  color: #333;
  margin-bottom: 15px;
  font-weight: 600;
}

.subsection-title {
  font-size: 18px;
  color: #555;
  margin-bottom: 10px;
  font-weight: 600;
}

/* 输入组样式 */
.input-group {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.input-label {
  font-size: 16px;
  color: #666;
  min-width: 100px;
}

.input-field {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc; /* 灰色边框 */
  border-radius: 6px;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

.input-field:focus {
  border-color: #2f8026; /* Apple 蓝色边框 */
  outline: none;
}

.action-button {
  padding: 10px 20px;
  background-color: transparent; /* 无色背景 */
  color: #28a424; /* Apple 蓝色文字 */
  border: 1px solid #267244; /* Apple 蓝色边框 */
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  transition:
    background-color 0.3s ease,
    color 0.3s ease;
}

.action-button:hover {
  background-color: #2b9054; /* Apple 蓝色背景 */
  color: white; /* 白色文字 */
}

/* 环境控制组样式 */
.control-group {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.range-input {
  flex: 1;
}

.range-value {
  font-size: 16px;
  color: #666;
}

/* 场景信息样式 */
.scene-info {
  margin-bottom: 20px;
}

.info-label {
  font-weight: 600;
  color: #666;
  margin-right: 5px;
}

/* 场景列表样式 */
.scene-list ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.scene-list li {
  margin-bottom: 8px;
  color: #666;
}

/* Agent 对话查询部分样式 */
.input-container {
  background-color: transparent; /* 去掉多余背景色 */
  border-radius: 0; /* 去掉多余边框圆角 */
  padding: 0; /* 去掉多余内边距 */
  box-shadow: none; /* 去掉多余阴影 */
  margin-top: 20px;
}

.el-input-field {
  margin-bottom: 10px;
}
.card{
  background-color: #f9f9f9; /* 浅灰色背景 */
  border: none; /* 移除边框 */
  border-radius: 8px;
  padding: 5px 10px;
  flex: 1 1 calc(50% - 20px);
  }

</style>
