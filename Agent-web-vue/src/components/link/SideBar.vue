<template>
  <!-- 侧边栏容器 -->
  <div class="sidebar-container" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <!-- 侧边栏显示/隐藏按钮 -->
    <button class="toggle-button" @click="toggleSidebar">
      <i :class="isSidebarVisible ? 'fas fa-angle-left' : 'fas fa-angle-right'"></i>
    </button>
    <!-- 侧边栏 -->
    <div :class="['sidebar', {'hidden':!isSidebarVisible}]">
      <!-- Element Plus 的菜单组件 -->
      <el-menu :default-active="selectedIndex" @select="handleMenuSelect" :default-openeds="defaultOpeneds"
               background-color="transparent" text-color="#000000" active-text-color="#000000">
        <!-- 首页子菜单 -->
        <el-sub-menu index="1">
          <template #title>
            <span class="sidebar-icon-text">
              <!-- 使用 Font Awesome 的农场相关图标 -->
              <i class="fas fa-tractor"></i>
              <span>首页</span>
            </span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1">
              <router-link to="/workbench" class="sidebar-nav-link">
                <span class="sidebar-icon-text">
                  <!-- 使用 Font Awesome 的工具图标表示工作台 -->
                  <i class="fas fa-tools"></i>
                  <span>工作台</span>
                </span>
              </router-link>
            </el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>

        <!-- 任务设计子菜单 -->
        <el-sub-menu index="3">
          <template #title>
            <span class="sidebar-icon-text">
              <!-- 使用 Font Awesome 的铅笔图标表示任务设计 -->
              <i class="fas fa-pencil"></i>
              <span>任务设计</span>
            </span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="3-1">
              <router-link to="/agent-role" class="sidebar-nav-link">
                <span class="sidebar-icon-text">
                  <!-- 使用 Font Awesome 的用户齿轮图标表示 Agent 角色 -->
                  <i class="fas fa-user-cog"></i>
                  <span>Agent 角色</span>
                </span>
              </router-link>
            </el-menu-item>
            <el-menu-item index="3-2">
              <router-link to="/Background" class="sidebar-nav-link">
                <span class="sidebar-icon-text">
                  <!-- 使用 Font Awesome 的调色板图标表示场景设计 -->
                  <i class="fas fa-palette"></i>
                  <span>场景设计</span>
                </span>
              </router-link>
            </el-menu-item>
            <el-menu-item index="3-3">
              <router-link to="/API_Page" class="sidebar-nav-link">
                <span class="sidebar-icon-text">
                  <!-- 使用 Font Awesome 的齿轮图标表示大模型调用 -->
                  <i class="fas fa-cogs"></i>
                  <span>大模型调用</span>
                </span>
              </router-link>
            </el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>

        <el-menu-item index="2">
          <router-link to="/data-visualization" >
            <span class="sidebar-icon-text">
              <!-- 使用 Font Awesome 的图表图标表示数据可视化 -->
              <i class="fas fa-chart-bar"></i>
              <span>数据可视化</span>
            </span>
          </router-link>
        </el-menu-item>

        <!-- 个人中心菜单项 -->
        <el-menu-item index="4">
          <router-link to="/person" >
            <span class="sidebar-icon-text">
              <!-- 使用 Font Awesome 的用户图标表示个人中心 -->
              <i class="fas fa-user"></i>
              <span>个人中心</span>
            </span>
          </router-link>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';

const selectedIndex = ref("3");
const defaultOpeneds = ref(["1", "3"]);
const isSidebarVisible = ref(true);

// 处理菜单选择事件的函数
const handleMenuSelect = (index, indexPath) => {
  // 打印选中的索引和路径，方便调试
  console.log("选中的索引:", index, "选中的路径:", indexPath);

  // 更新选中的索引
  selectedIndex.value = index;

  // 模拟异步操作，比如请求数据
  setTimeout(() => {
    console.log("模拟异步操作完成，加载新内容");
    // 这里可以添加实际的异步操作，如调用 API 获取数据等
  }, 1000);
};

// 切换侧边栏显示/隐藏的函数
const toggleSidebar = () => {
  isSidebarVisible.value =!isSidebarVisible.value;
};

// 处理鼠标移动事件的函数
const handleMouseMove = (event) => {
  if (event.clientX < 20) {
    isSidebarVisible.value = true;
  }
};

// 处理鼠标离开事件的函数
const handleMouseLeave = () => {
  isSidebarVisible.value = false;
};
</script>

<style scoped>
.sidebar-container {
  position: fixed;
  top: 62px; /* 假设顶部导航栏高度为 60px */
  left: 0;
  bottom: 0;
  width: 200px;
  z-index: 101; /* 确保侧边栏在所有组件上方显示 */
}

.sidebar {
  width: 200px;
  height: 100%;
  background-color: #f0f0f0;
  overflow-x: hidden;
  transform: translateX(0);
  transition: transform 0.3s ease;
}

.sidebar.hidden {
  transform: translateX(-100%);
  pointer-events: none; /* 防止隐藏时仍能点击 */
}

.toggle-button {
  position: fixed;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  background-color: #7c9f4a;
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  z-index: 102;
  transition: left 0.3s ease;
}

.sidebar.hidden + .toggle-button {
  left: 0;
}

.sidebar:not(.hidden) + .toggle-button {
  left: 200px;
}
</style>
