<template>
  <div class="combined-page row-layout">
    <!-- 左栏：场景创建与管理 -->
    <div class="left-column">
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
<!--              <button @click="createScene" class="generate-button">创建场景</button>-->
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
              </div>
              <div class="input-group">
                <label class="input-label">作物名称:</label>
                <input type="text" v-model="cropName" class="input-field" />
                <button @click="handleCreateCrop" class="generate-button">
                  创建农作物
                </button>
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
              <input
                type="number"
                v-model="customSoilFertility"
                min="0"
                max="100"
                step="1"
              />
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
              <input
                type="number"
                v-model="customPrecipitation"
                step="1"
              />
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
            <el-select
              id="land"
              v-model="customTerrain"
              multiple
              placeholder="请选择地形"
            >
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
            <el-select
              v-model="customAgriculturalTechnology"
              multiple
              placeholder="请选择农场技术"
            >
              <el-option
                v-for="task in farmTasks"
                :key="task.value"
                :label="task.label"
                :value="task.value"
              />
            </el-select>
          </div>
          <button @click="handleCreateSimulation" class="generate-button">
            创建模拟
          </button>
          <button @click="handleGetSimulation" class="generate-button">
            开始模拟
          </button>
          <pre>{{ simulationInfo }}</pre>
          <p v-if="error">{{ error }}</p>
          <p v-if="loading">加载中...</p>
          <button @click="handleEndSimulation" class="generate-button">
            结束模拟
          </button>
          <pre>{{ customEnvResult }}</pre>
        </div>
      </div>
    </div>
    <!-- 右栏：多智能体农场模拟 -->
    <div class="right-column">
      <div class="multi-agent-farm-simulation">
        <div class="agent-cards">
          <!-- Agent 1 -->
          <div class="agent-card">
            <h3>Agent 1</h3>
            <div class="input-group inline-input-group">
              <label for="agent1-name">名称</label>
              <el-input v-model="agent1Name" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent1-role">角色</label>
              <el-input v-model="agent1Role" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent1-count">数量</label>
              <el-select v-model="agent1Count" placeholder="请选择">
                <el-option
                  v-for="count in agentCountOptions"
                  :key="count"
                  :label="count"
                  :value="count"
                />
              </el-select>
            </div>
          </div>
          <!-- Agent 2 -->
          <div class="agent-card">
            <h3>Agent 2</h3>
            <div class="input-group inline-input-group">
              <label for="agent2-name">名称</label>
              <el-input v-model="agent2Name" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent2-role">角色</label>
              <el-input v-model="agent2Role" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent2-count">数量</label>
              <el-select v-model="agent2Count" placeholder="请选择">
                <el-option
                  v-for="count in agentCountOptions"
                  :key="count"
                  :label="count"
                  :value="count"
                />
              </el-select>
            </div>
          </div>
          <div class="agent-card random-agent-generation">
            <div class="agent-card">
              <h3>Agent 3</h3>
              <div class="input-group inline-input-group">
                <label for="agent3-name">名称</label>
                <el-input v-model="agent3Name" />
              </div>
              <div class="input-group inline-input-group">
                <label for="agent3-role">角色</label>
                <el-input v-model="agent3Role" />
              </div>
              <div class="input-group inline-input-group">
                <label for="agent3-count">数量</label>
                <el-select v-model="agent3Count">
                  <el-option
                    v-for="count in agentCountOptions"
                    :key="count"
                    :label="count"
                    :value="count"
                  />
                </el-select>
              </div>
            </div>
          </div>
          <div class="row-layout">
            <!-- 随机生成 Agent 角色 1 -->
            <div class="agent-card random-agent-generation">
              <h3>随机生成 Agent 角色 1</h3>
              <div class="input-group">
                <button @click="generateRandomAgent1" class="generate-button">
                  生成随机 Agent 1
                </button>
              </div>
              <div v-if="randomAgent1" class="input-group inline-input-group">
                <h4>{{ randomAgent1.name }}</h4>
                <p>角色设定：{{ randomAgent1.role }}</p>
                <p>数量：{{ randomAgent1.count }}</p>
              </div>
            </div>
            <div class="agent-card random-agent-generation">
              <h3>随机生成 Agent 角色 2</h3>
              <div class="input-group">
                <button @click="generateRandomAgent2" class="generate-button">
                  生成随机 Agent 2
                </button>
              </div>
              <div v-if="randomAgent2" class="input-group inline-input-group">
                <h4>{{ randomAgent2.name }}</h4>
                <p>角色设定：{{ randomAgent2.role }}</p>
                <p>数量：{{ randomAgent2.count }}</p>
              </div>
            </div>
          </div>
        </div>
        <!-- 统一创建智能体按钮 -->
        <div class="create-agent-btn-container">
          <button @click="handleCreateAgent" class="generate-button">创建</button>
          <pre>{{ agentResult }}</pre>
        </div>
        <div class="agent-card random-agent-generation">
          <h3>启动对话</h3>
          <div class="agent-card">
            <div class="input-group inline-input-group">
              <label for="dialoguePrompt">对话提示</label>
              <input type="text" id="dialoguePrompt" placeholder="输入对话提示" />
              <button @click="handleStartDialogue()" class="generate-button">启动对话</button>
              <button @click="handleGetDialogueHistory" class="generate-button">
                查看 Agent 对话记录
              </button>
            </div>
            <div v-if="showConversation" class="input-container">
              <div class="input-group">
                <div class="input-item">
                  <h3 class="subsection-title">!查询条件</h3>
                  <el-input
                    v-model="query"
                    clearable
                    placeholder="输入查询条件"
                    class="el-input-field"
                  />
                </div>
                <div class="input-item">
                  <el-button @click="fetchAgentConversation" class="action-button">!查询</el-button>
                </div>
              </div>
              <div class="input-group">
                <div class="input-item">
                  <h3 class="subsection-title">!Agent 对话记录</h3>
                  <div class="agent-card">
                    <div class="input-group inline-input-group">
                      <input type="text" id="simulationIdForHistory" placeholder="输入场景名称" />
                      <button @click="handleGetDialogueHistory()" class="generate-button">!获取历史</button>
                    </div>
                  </div>
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
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {
  createSimulation,
  getSimulation,
  endSimulation,
  createCrop,
} from '../api/index';

// 定义响应式数据
const scenes = ref([]);
const customTemperature = ref(0);
const currentTemperature = ref(0);
const currentFertility = ref('');
const customSoilFertility = ref(0);
const currentMoisture = ref('');
const customPrecipitation = ref(0);
const customClimate = ref([]);
const customTerrain = ref([]);
const landOptions = [
  { value: '平原', label: '平原' },
  { value: '山地', label: '山地' },
  { value: '丘陵', label: '丘陵' },
  { value: '盆地', label: '盆地' },
];

const cropName = ref('');
const cropResult = ref('');
const simName = ref('');
const simDesc = ref('');
const customAgriculturalTechnology = ref([]);
const climateOptions = [
  { value: '温带季风气候', label: '温带季风气候' },
  { value: '热带雨林气候', label: '热带雨林气候' },
  { value: '地中海气候', label: '地中海气候' },
  { value: '高原气候', label: '高原气候' },
];
const farmTasks = [
  { value: '传统农业', label: '传统农业' },
  { value: '精准农业', label: '精准农业' },
  { value: '有机农业', label: '有机农业' },
  { value: '智能农业', label: '智能农业' },
];
const simulationInfo = ref(null);
const error = ref(null);
const loading = ref(false);

const updateTemperature = () => {
  currentTemperature.value = customTemperature.value;
};

const updateFertility = () => {
  // 更新土壤肥力的逻辑
};

const updateMoisture = () => {
  // 更新降水量的逻辑
};

const handleCreateSimulation = async () => {
  loading.value = true;
  error.value = null;
  try {
    const dto = {
      name: simName.value,
      description: simDesc.value,
      temperature: parseFloat(customTemperature.value),
      fertility: currentFertility.value,
      moisture: currentMoisture.value,
      climate: customClimate.value,
      terrain: customTerrain.value,
      agriculturalTechnology: customAgriculturalTechnology.value,
    };
    const response = await createSimulation(dto);
    console.log('模拟创建成功:', response);
    simulationInfo.value = response;
  } catch (error) {
    error.value = error.response?.data?.message || '创建模拟失败';
  } finally {
    loading.value = false;
  }
};

const handleGetSimulation = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await getSimulation();
    simulationInfo.value = response;
  } catch (error) {
    error.value = error.response?.data?.message || '获取模拟信息失败';
  } finally {
    loading.value = false;
  }
};

const handleEndSimulation = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await endSimulation();
    console.log('模拟结束:', response);
  } catch (error) {
    error.value = error.response?.data?.message || '结束模拟失败';
  } finally {
    loading.value = false;
  }
};

const handleCreateCrop = async () => {
  loading.value = true;
  error.value = null;
  try {
    const dto = {
      name: cropName.value,
    };
    const response = await createCrop(dto);
    console.log('农作物创建成功:', response);
    cropResult.value = response;
  } catch (error) {
    error.value = error.response?.data?.message || '创建农作物失败';
  } finally {
    loading.value = false;
  }
};
</script>
<style scoped>
/* 整体布局样式 */
.combined-page {
  display: flex;
  gap: 20px;
  min-height: 100vh;
  box-sizing: border-box;
  padding: 20px;
  background-color: #fff;
  color: #333;
  font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
}

.left-column,
.right-column {
  flex: 1;
}

/* 卡片样式 */
.card {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
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

/* Agent 卡片样式 */
.agent-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.agent-card {
  background-color: #f9f9f9;
  border: none;
  border-radius: 8px;
  padding: 5px 10px;
  flex: 1 1 calc(50% - 20px);
}

.inline-input-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.inline-input-group label {
  margin-bottom: 0;
  white-space: nowrap;
}

.inline-input-group .el-input,
.inline-input-group .el-select {
  flex: 1;
}

.random-agent-generation {
  text-align: center;
}

/* Agent 对话查询部分样式 */
.input-container {
  margin-top: 20px;
}

.el-input-field {
  margin-bottom: 10px;
}

.create-agent-btn-container {
  margin-top: 20px;
}
</style>
