<template>
  <div class="multi-agent-farm-simulation">
    <div class="row-layout">
      <div class="page-container">
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
              <el-input v-model="createCrop" placeholder="你好" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent2-role">角色</label>
              <el-input v-model="agent2Role" placeholder="" />
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
      </div>
    </div>
    <div class="right-column">
      <!-- 统一创建智能体按钮 -->
      <div class="create-agent-btn-container">
        <button @click="handleCreateAgent" class="generate-button">创建</button>
        <pre>{{ agentResult }}</pre>
      </div>
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
</template>

<script setup>import { ref } from 'vue'

// Agent 1 相关数据
const agent1Name = ref('')
const agent1Role = ref('')
const agent1Count = ref('')

// Agent 2 相关数据
const agent2Role = ref('')
const agent2Count = ref('')

const agent3Name = ref('')
const agent3Role = ref('')
const agent3Count = ref('')
// 可选择的 Agent 数量选项
const agentCountOptions = [1, 2, 3, 4, 5]

const showConversation = ref(false)
const query = ref('')
const conversationContent = ref('')

import {
  createAgent,
  startDialogue,
  getDialogueHistory,

} from '../api/index';

const error = ref(null);
const loading = ref(false);

const handleCreateAgent = async () => {
  loading.value = true;
  error.value = null;
  try {
    const dto = {
      agents: [
        {
          name: agent1Name.value,
          role: agent1Role.value,
          count: agent1Count.value
        },
        {
          name: agent2Role.value, // 原代码中 agent2 输入框绑定错误，这里假设修正
          role: agent2Role.value,
          count: agent2Count.value
        },
        {
          name: agent3Name.value,
          role: agent3Role.value,
          count: agent3Count.value
        }
      ]
    };
    const response = await createAgent(dto);
    console.log('Agent创建成功:', response.data);
  } catch (error) {
    error.value = '创建Agent失败: ' + error.message;
  } finally {
    loading.value = false;
  }
};

const handleStartDialogue = async () => {
  loading.value = true;
  error.value = null;
  try {
    const prompt = '示例提示';
    const response = await startDialogue(prompt);
    console.log('对话启动:', response.data);
  } catch (error) {
    error.value = '启动对话失败: ' + error.message;
  } finally {
    loading.value = false;
  }
};

const handleGetDialogueHistory = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await getDialogueHistory();
    console.log('对话历史:', response.data);
  } catch (error) {
    error.value = '获取对话历史失败: ' + error.message;
  } finally {
    loading.value = false;
  }
};

// const showAgentConversation = () => {
//   showConversation.value = !showConversation.value
// }

// const fetchAgentConversation = () => {
//   // 查询 Agent 对话记录的逻辑
// }

const agentResult = ref('')

// 随机生成的 Agent 1
const randomAgent1 = ref(null)

// 随机生成的 Agent 2
const randomAgent2 = ref(null)

// 随机生成 Agent 角色 1 的函数
const generateRandomAgent1 = () => {
  const agentNames = ['智能灌溉 Agent', '土壤改良 Agent', '气象预报 Agent', '作物保护 Agent']
  const agentRoles = [
    '根据土壤湿度数据，精准灌溉',
    '定期改良土壤的结构和肥力',
    '实时预报气象变化，为农场生产提供参考',
    '分析作物病虫害情况，采取相应的防治措施',
  ]
  const randomName = agentNames[Math.floor(Math.random() * agentNames.length)]
  const randomRole = agentRoles[Math.floor(Math.random() * agentRoles.length)]
  const randomCount = agentCountOptions[Math.floor(Math.random() * agentCountOptions.length)]
  randomAgent1.value = {
    name: randomName,
    role: randomRole,
    count: randomCount,
  }
}

// 随机生成 Agent 角色 2 的函数
const generateRandomAgent2 = () => {
  const agentNames = ['农产品加工 Agent', '农场物流 Agent', '农场财务 Agent', '农场市场分析 Agent']
  const agentRoles = [
    '负责农产品的加工和包装',
    '管理农场的物流运输，确保农产品及时送达',
    '进行农场的财务管理，控制成本和预算',
    '分析市场需求和趋势，为农场生产提供决策依据',
  ]
  const randomName = agentNames[Math.floor(Math.random() * agentNames.length)]
  const randomRole = agentRoles[Math.floor(Math.random() * agentRoles.length)]
  const randomCount = agentCountOptions[Math.floor(Math.random() * agentCountOptions.length)]
  randomAgent2.value = {
    name: randomName,
    role: randomRole,
    count: randomCount,
  }
}
</script>

<style scoped>
.multi-agent-farm-simulation {
  padding: 20px;
  background-color: #fff;
  color: #333;
  font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
  min-height: 100vh;
  box-sizing: border-box;
}

.row-layout {
  display: flex;
  gap: 20px;
}

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

.input-group {
  margin-bottom: 10px;
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

.generate-button {
  padding: 10px 20px;
  background-color: #006400;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.generate-button:hover {
  background-color: #004d00;
}

.input-container {
  margin-top: 20px;
}

.el-input-field {
  margin-bottom: 10px;
}

.subsection-title {
  font-size: 18px;
  color: #555;
  margin-bottom: 10px;
  font-weight: 600;
}
</style>
