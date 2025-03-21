<template>
  <div class="multi-agent-farm-simulation">
    <div class="row-layout">

      <div class="page-container">
        <h2 class="section-title">场景创建与管理</h2>
        <!-- 场景创建与管理部分 -->
        <div class="card">
          <div class="scene-management">
            <div class="input-group">
              <label for="scene-name" class="input-label">场景名称</label>
              <input
                type="text"
                id="scene-name"
                v-model="sceneName"
                placeholder="输入场景名称"
                class="input-field"
              />
              <button @click="createScene"  class="generate-button">创建场景</button>
            </div>
            <div class="input-group">
              <label for="scene-select" class="input-label">选择场景</label>
              <select
                id="scene-select"
                v-model="selectedSceneId"
                @change="loadScene"
                class="input-field"
              >
                <option value="">无</option>
                <option v-for="scene in scenes" :key="scene.id" :value="scene.id">
                  {{ scene.name }}
                </option>
              </select>
            </div>

            <button @click="StartSimulation" class="generate-button">开始模拟</button>
            <button @click="handleEndSimulation" class="generate-button">结束模拟</button>
          </div>
            <div class="row-layout">
            <!-- 模拟管理 -->
            <div class="scene-management">
              <h2 class="section-title">新建环境场景</h2>
              <div class="input-group">
                <label for="sim-name" class="input-label">模拟名称</label>
                <input
                  type="text"
                  id="sim-name"
                  v-model="simName"
                  placeholder="输入模拟名称"
                  class="input-field"
                />
                <button @click="handleCreateSimulation" class="generate-button">创建模拟</button>
              </div>
              <div class="input-group">
                <label  class="input-label">作物名称:</label>
                <input type="text" v-model="cropName"  class="input-field"/>
                <button @click="handleCreateCrop" class="generate-button">创建农作物</button>
                <pre>{{ cropResult }}</pre>
              </div>
                <div class="input-group">
                <label for="sim-desc" class="input-label">描述</label>
                <input
                  type="text"
                  id="sim-desc"
                  v-model="simDesc"
                  placeholder="输入模拟描述"
                  class="input-field"
                />
                </div>
              <pre>{{ simResult }}</pre>
            </div>


            <div class="control-group">
              <label for="temperature" class="input-label">温度 (℃)</label>
              <input
                type="range"
                id="temperature"
                v-model="customTemperature"
                min="0"
                max="50"
                @input="updateTemperature"
                class="range-input"
                step="0.01"
              />
              <span class="range-value">{{ currentTemperature }}</span>
            </div>

            <div class="control-group">
              <label for="fertility" class="input-label">土壤肥力</label>
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
              <input type="number" v-model="customSoilFertility" min="0" max="100" step="1" />
            </div>
            <div class="control-group">
              <label for="moisture" class="input-label">降水量 (mm)</label>
              <select
                id="moisture"
                v-model="currentMoisture"
                @change="updateMoisture"
                class="input-field"
              >
                <option value="DRY">小于500mm</option>
                <option value="NORMAL">(500mm,1000mm)</option>
                <option value="WET">大于1000mm</option>
              </select>
              <input type="number" v-model="customPrecipitation" step="1" />
            </div>

          </div>
          <div class="input-group inline-input-group">
            <label for="selected-technologies" class="input-label">气候</label>
            <el-select v-model="customClimate" multiple placeholder="请选择气候">
              <el-option
                v-for="option in climateOptions"
                :key="option.value"
                :value="option.value"
                :label="option.label"
              ></el-option>
            </el-select>
          </div>
            <div class="control-group">
              <label for="land" class="input-label">地形</label>
              <el-select id="land" v-model="customTerrain" multiple placeholder="请选择地形">
                <el-option
                v-for="option in landOptions"
                :key="option.value"
                :value="option.value"
                :label="option.label"
                ></el-option>
              </el-select>
            </div>

          <div class="input-group inline-input-group">
            <label for="selected-tasks" class="input-label">农场技术</label>
            <el-select v-model="customAgriculturalTechnology" multiple placeholder="请选择农场技术">
              <el-option
                v-for="task in farmTasks"
                :key="task.value"
                :label="task.label"
                :value="task.value"
              />
            </el-select>
          </div>
            <button @click="handleCreateCustomEnvironment" class="generate-button">提交自定义环境</button>
            <pre>{{ customEnvResult }}</pre>

          </div>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
// 定义响应式数据
const currentWeather = ref('');

const scenes = ref([]);
const customTemperature = ref(0);
const currentTemperature = ref(0);
const currentFertility = ref('');
const customSoilFertility = ref(0);
const currentMoisture = ref('');
const customPrecipitation = ref(0);
const customClimate = ref([]);
const customTerrain = ref('');
const landOptions = [
  { value: '平原', label: '平原' },
  { value: '山地', label: '山地' },
  { value: '丘陵', label: '丘陵' },
  { value: '盆地', label: '盆地' }
];

const cropName = ref('');
const cropSimId = ref('');
const cropResult = ref('');
const agentName = ref('');

const agentRole = ref('');
const agentResult = ref('');

const customAgriculturalTechnology = ref([]);


// 定义方法
const updateWeather = () => {
  // 更新天气的逻辑
};

const updateTemperature = () => {
  currentTemperature.value = customTemperature.value;
};

const updateFertility = () => {
  // 更新土壤肥力的逻辑
};

const updateMoisture = () => {
  // 更新降水量的逻辑
};

const handleCreateAgent = () => {
  // 创建 Agent 的逻辑
};

// 定义气候选项
const climateOptions = [
  { value: '温带季风气候', label: '温带季风气候' },
  { value: '热带雨林气候', label: '热带雨林气候' },
  { value: '地中海气候', label: '地中海气候' },
  { value: '高原气候', label: '高原气候' }
];

const farmTasks = [
  { value: '传统农业',label:'传统农业' },
  {value: '精准农业',label:'精准农业'},
  {value: '有机农业',label:'有机农业'},
  {value: '智能农业',label:'智能农业'},
]

const customEnvSimId = ref('')

// 对话管理
const dialogueSimId = ref('')

// 创建模拟
const handleCreateSimulation = async () => {
  const result = await createSimulation(simName.value, simDesc.value)
  simResult.value = JSON.stringify(result, null, 2)
}

// 获取模拟
const handleGetSimulation = async () => {
  const id = prompt('请输入模拟 ID:')
  if (id) {
    const result = await getSimulation(id)
    simResult.value = JSON.stringify(result, null, 2)
  }
}

// 结束模拟
const handleEndSimulation = async () => {
  const id = prompt('请输入模拟 ID:')
  if (id) {
    const result = await endSimulation(id)
    simResult.value = JSON.stringify(result, null, 2)
  }
}

// 创建农作物
const handleCreateCrop = async () => {
  const result = await createCrop(cropName.value, cropSimId.value)
  cropResult.value = JSON.stringify(result, null, 2)
}

// 创建自定义环境
const handleCreateCustomEnvironment = async () => {
  const dto = {
    temperature: parseFloat(customTemperature.value),
    soilFertility: parseFloat(customSoilFertility.value),
    precipitation: parseFloat(customPrecipitation.value),
    terrain: customTerrain.value,
    climate: customClimate.value,
    agriculturalTechnology: customAgriculturalTechnology.value,
    simulationId: customEnvSimId.value,
  }
  const result = await createCustomEnvironment(dto)
  customEnvResult.value = `环境创建成功，ID: ${result}`
}

// 启动对话
const handleStartDialogue = async () => {
  const result = await startDialogue(dialogueSimId.value, prompt.value)
  dialogueResult.value = JSON.stringify(result, null, 2)
}

</script>
/* multi-agent-farm-simulation 类样式 */
.multi-agent-farm-simulation {
padding: 20px;
background-color: #fff;
color: #333;
font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
}
<style scoped>
/* 整体页面容器样式 */
.page-container {
font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
padding: 20px;
background-color: #fff;
display: flex;
flex-direction: column;
align-items: center;
}

/* 卡片样式 */
.card {
background-color: #f9f9f9;
border-radius: 8px;
padding: 20px;
margin-bottom: 20px;
width: 80%;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

.left-column,
.right-column {
flex: 1;
}

.generate-button,
.btn {
  padding: 10px 20px;
  background-color: #006400; /* 墨绿色按钮 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 5px;
}
.generate-button:hover,
.btn:hover {
  background-color: #004d00; /* 深一点的墨绿色悬停效果 */
}

.input-field {
flex: 1;
padding: 10px;
border: 1px solid #ccc;
border-radius: 6px;
font-size: 16px;
transition: border-color 0.3s ease;
}

.input-field:focus {
border-color: #2f8026;
outline: none;
}


.action-button {
padding: 10px 20px;
background-color: transparent;
color: #28a424;
border: 1px solid #267244;
border-radius: 6px;
cursor: pointer;
font-size: 16px;
transition:
background-color 0.3s ease,
color 0.3s ease;
}

.action-button:hover {
background-color: #2b9054;
color: white;
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
margin-top: 20px;
}

.el-input-field {
margin-bottom: 10px;
}

</style>
