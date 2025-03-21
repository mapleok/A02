<template>

    <!-- 整体容器，使用 flex 布局实现横行布局 -->
    
      <div class="left-column">
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
    <div class="right-column">
      <!-- 右侧区域 -->
      <div class="right-section">
        <!-- 农业智能体和作物管理 -->
        <div class="agent-card random-agent-generation">
          <div class="agent-card">
            <h3>Agent 3 </h3>
            <div class="input-group inline-input-group">
              <label for="agent3-name">名称</label>
              <el-input v-model="agent3Name"/>
            </div>
            <div class="input-group inline-input-group">
              <label for="agent3-role">角色</label>
              <el-input v-model="agent3Role"/>
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

        <!-- 统一创建智能体按钮 -->
        <div class="create-agent-btn-container">
          <button @click="createAgent" class="generate-button">创建</button>
          <pre>{{ agentResult }}</pre>
        </div>
      </div>

      <div class="agent-card random-agent-generation">
        <h3>启动对话</h3>
        <div class="agent-card">
          <div class="input-group inline-input-group">
          <label for="dialoguePrompt">对话提示</label>
          <input type="text" id="dialoguePrompt" placeholder="输入对话提示">
          <button onclick="startDialogue()" class="generate-button">启动对话</button>

    <button @click="showAgentConversation" class="generate-button">查看 Agent 对话记录</button>
          </div>
    <div v-if="showConversation" class="input-container">
      <div class="input-group">
        <div class="input-item">
          <h3 class="subsection-title">查询条件</h3>
          <el-input
            v-model="query"
            clearable
            placeholder="输入查询条件"
            class="el-input-field"
          />
        </div>
        <div class="input-item">
          <el-button @click="fetchAgentConversation" class="action-button">查询</el-button>
        </div>
      </div>
      <div class="input-group">
        <div class="input-item">
          <h3 class="subsection-title">Agent 对话记录</h3>
          <div class="agent-card">
            <div class="input-group inline-input-group">
              <input type="text" id="simulationIdForHistory" placeholder="输入场景名称">
              <button onclick="getDialogueHistory()" class="generate-button">获取历史</button>
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

</template>

<script setup>
import { ref } from 'vue'

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

// 创建Agent
// 添加 simulationId 的响应式变量
const simulationId = ref('');

const showConversation = ref(false);
const query = ref('');
const conversationContent = ref('');
async function createAgent() {
  const result = await callApi('/agent', 'POST', {
    agentName: agent1Name.value,
    simulationId: simulationId.value, // 使用响应式变量
    roleType: agent1Role.value,
  })
  agentResult.value = JSON.stringify(result, null, 2)
}


// 启动对话
async function startDialogue() {
  const simulationId = document.getElementById("simulationIdForDialogue").value;
  const prompt = document.getElementById("dialoguePrompt").value;
  const response = await fetch(`${baseUrl}/simulation/${simulationId}/start-dialogue`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ prompt })
  });
  const data = await response.json();
  document.getElementById("startDialogueResponse").innerText = JSON.stringify(data, null, 2);
}

// 获取对话历史
async function getDialogueHistory() {
  const simulationId = document.getElementById("simulationIdForHistory").value;
  const response = await fetch(`${baseUrl}/simulation/${simulationId}/dialogue-history`);
  const data = await response.json();
  document.getElementById("dialogueHistoryResponse").innerText = JSON.stringify(data, null, 2);
}


const showAgentConversation = () => {
  showConversation.value = !showConversation.value;
};

const fetchAgentConversation = () => {
  // 查询 Agent 对话记录的逻辑
};

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
/* 移除与全局样式冲突的按钮样式等 */
/* 保留组件特定的样式 */
.multi-agent-farm-simulation {
  padding: 20px;
  background-color: #fff;
  color: #333;
  font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
}
.h3{
  text-align: left;
}
.row-layout {
  display: flex; /* 使用 flex 布局 */
  gap: 20px; /* 两栏之间的间距 */
}

.left-column,
.right-column {
  flex: 1;
}

.agent-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.agent-card {
  background-color: #f9f9f9; /* 浅灰色背景 */
  border: none; /* 移除边框 */
  border-radius: 8px;
  padding: 5px 10px;
  flex: 1 1 calc(50% - 20px);
}

.input-group {
  margin-bottom: 10px;
}

.inline-input-group {
  display: flex; /* 使用 flex 布局让子元素在一行显示 */
  align-items: center; /* 垂直居中对齐 */
  gap: 10px; /* 元素之间的间距 */
}

.inline-input-group label {
  margin-bottom: 0; /* 移除 label 的底部外边距 */
  white-space: nowrap; /* 防止 label 文字换行 */
}

.inline-input-group .el-input,
.inline-input-group .el-select {
  flex: 1; /* 让输入框占据剩余空间 */
}

.random-agent-generation {
  margin-top: 20px;
  text-align: center;
}

.generate-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.task-selection,
.technology-selection {
  margin-top: 20px;
}

.form-label {
  display: block;
  margin-bottom: 5px;
}

.form-control {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.btn-outline-success {
  border: 1px solid #28a745;
  color: #28a745;
  background-color: transparent;
}
.generate-button:hover,
.btn:hover {
  background-color: #004d00; /* 深一点的墨绿色悬停效果 */
}

.btn-outline-success {
  border: 1px solid #006400;
  color: #006400;
  background-color: transparent;
}

.btn-outline-success:hover {
  background-color: #006400;
  color: white;
}

.btn-sm {
  font-size: 0.8rem;
}

.btn-success {
  background-color: #28a745;
  color: white;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-warning {
  background-color: #ffc107;
  color: white;
}

.btn-info {
  background-color: #17a2b8;
  color: white;
}

.text-muted {
  font-size: 0.8rem;
  color: #6c757d;
}

.dashboard-card {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  margin-top: 20px;
}

.btn-group {
  margin-bottom: 10px;
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
}

button:hover {
  background: #45a049;
}
</style>
