<template>
  <div class="multi-agent-farm-simulation">
    <!-- 整体容器，使用 flex 布局实现横行布局 -->
    <div class="row-layout">
      <div class="left-column">
        <div class="agent-cards">
          <!-- Agent 1 -->
          <div class="agent-card">
            <h3>Agent 1 - 农场种植规划 Agent</h3>
            <div class="input-group inline-input-group">
              <label for="agent1-name">名称</label>
              <el-input v-model="agent1Name" placeholder="例如：智能种植规划 Agent" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent1-role">角色</label>
              <el-input
                v-model="agent1Role"
                placeholder="例如：根据农场土地情况和市场需求，制定种植计划"
              />
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
            <h3>Agent 2 - 农场养殖管理 Agent</h3>
            <div class="input-group inline-input-group">
              <label for="agent2-name">名称</label>
              <el-input v-model="agent2Name" placeholder="例如：智能养殖管理 Agent" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent2-role">角色</label>
              <el-input
                v-model="agent2Role"
                placeholder="例如：负责农场养殖动物的日常管理和疾病防控"
              />
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
          <!-- Agent 3 -->
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
            <h3>Agent 3 - 农场销售推广 Agent</h3>
            <div class="input-group inline-input-group">
              <label for="agent3-name">名称</label>
              <el-input v-model="agent3Name" placeholder="例如：智能销售推广 Agent" />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent3-role">角色</label>
              <el-input
                v-model="agent3Role"
                placeholder="例如：通过各种渠道推广农场产品，提高销售额"
              />
            </div>
            <div class="input-group inline-input-group">
              <label for="agent3-count">数量</label>
              <el-select v-model="agent3Count" placeholder="请选择">
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
      </div>
      <div class="right-section">
        <div class="agent-card random-agent-generation">
          <div class="agent-card">
            <h3>🤖 农业智能体</h3>
            <div class="btn-group">
              <div class="input-group inline-input-group">
                <label for="agent3-name">名称</label>
                <el-input v-model="agent3Name" placeholder="例如：智能销售推广 Agent" />
              </div>
              <div class="input-group inline-input-group">
                <label for="agent3-role">角色</label>
                <el-input
                  v-model="agent3Role"
                  placeholder="例如：通过各种渠道推广农场产品，提高销售额"
                />
              </div>
              <div class="input-group inline-input-group">
                <label for="agent3-count">数量</label>
                <el-select v-model="agent3Count" placeholder="请选择">
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

          <div class="agent-card random-agent-generation">
            <div class="agent-card">
              <h3>🌱 作物管理</h3>
            </div>
            <div class="btn-group">
              <!-- 创建作物（对应新增作物） -->
              <button @click="handleCreateCrop">新增作物</button>
              <button class="btn btn-sm btn-primary" @click="showAddCropModal">🌾 新增作物</button>
              <button class="btn btn-sm btn-warning" @click="refreshCropStatus">🔄 刷新状态</button>
              <button class="btn btn-sm btn-info" @click="showUpdateCropStatusModal">
                🌱 更新状态
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 统一创建智能体按钮 -->
    <div class="create-agent-btn-container">
      <button @click="handleCreateAllAgents" class="generate-button">确定</button>
    </div>
  </div>
</template>

<style scoped>
/* 移除与全局样式冲突的按钮样式等 */
/* 保留组件特定的样式 */
.multi-agent-farm-simulation {
  padding: 20px;
  background-color: #fff;
  color: #333;
  font-family: 'SF Pro Text', 'SF Pro Icons', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
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

<script setup>
import { ref } from 'vue'
// import { createSimulation, createAgent, createCrop } from '../api'

// Agent 1 相关数据
const agent1Name = ref('')
const agent1Role = ref('')
const agent1Count = ref('')

// Agent 2 相关数据
const agent2Name = ref('')
const agent2Role = ref('')
const agent2Count = ref('')

// Agent 3 相关数据
const agent3Name = ref('')
const agent3Role = ref('')
const agent3Count = ref('')

// 可选择的 Agent 数量选项
const agentCountOptions = [1, 2, 3, 4, 5]

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

// 创建作物
const handleCreateCrop = async () => {
  try {
    // 这里假设需要一个单独的作物名称输入框，目前代码中没有，可根据实际情况调整
    const cropNameInput = '作物名称' // 可替换为实际输入框的值
    const data = {
      cropName: cropNameInput,
      growthRate: 1.0,
    }
    const result = await createCrop(data)
    cropResult.value = `作物 ID: ${result.id}`
  } catch (error) {
    console.error('创建作物失败:', error)
    cropResult.value = '创建作物失败，请稍后重试'
  }
}
</script>
