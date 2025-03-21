<template>
  <div class="main-container">
    <div class="nav-container">
      <ul class="nav-links">
        <h3 class="page-title">Unity 实时画面与信息查看</h3>
        <li>
          <router-link to="/agent-role">
            <el-button class="nav-button">查看 agent 状态</el-button>
          </router-link>
        </li>
        <li>
          <router-link to="/scene-edit">
            <el-button class="nav-button">场景编辑</el-button>
          </router-link>
        </li>
        <li>
          <router-link to="/task-progress">
            <el-button class="nav-button">整体任务进度</el-button>
          </router-link>
        </li>
        <li>
          <router-link to="/role-definition">
            <el-button class="nav-button">角色定义进度</el-button>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
  <div class="right-section">
    <iframe src="src/unity-webgl/index1.html" width="100%" height="650px" frameborder="0" class="unity-iframe"></iframe>
  </div>
</template>

<script setup>
import 'element-plus/dist/index.css';
</script>

<style scoped>

/* 页面标题样式 */
.page-title {
  color: #333;
  font-size: 24px;
  font-weight: bold;
  text-align: left;
  margin: 10px 20px;
}

.main-container {
  display: flex;
  gap: 20px;
  margin-left: 0;
  border-radius: 4px
}

.nav-container {
  margin-top: 0px;
  background-color: #fff;
  border-radius: 12px;
  padding: 5px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  width: 100%;
  gap: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-title {
  font-size: 20px;
  color: #426571d0;
  font-weight: bold;
  margin-right: 20px;
}

.nav-links {
  text-align: center;
}

/* 导航链接列表项样式 */
.nav-links li {
  margin: 10px 10px 10px 10px;
}

/* 导航按钮样式 */
.nav-button {
  width: 140px;
  padding: 12px 15px;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.nav-button:hover {
  background-color: #e0e0e0;
}

/*  Unity 画面容器样式 */
.right-section {
  flex: 2;
  min-width: 0;
}

/* Unity 画面 iframe 样式 */
.unity-iframe {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  width: 100%;
  height: 650px;
  display: block;
}

/* 按钮示例容器样式 */
.button-examples {
  padding: 30px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* 示例标题样式 */
.example-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
}
</style>
