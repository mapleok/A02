<template>
  <!-- 侧边栏容器 -->
  <div class="sidebar">
    <!-- Element Plus 的菜单组件 -->
    <el-menu :default-active="selectedIndex" @select="handleMenuSelect" :default-openeds="defaultOpeneds"
      background-color="#f4f7f9" text-color="#000000" active-text-color="#000000" style="width: 240px;">
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
            <i class="fas fa-pencil-alt"></i>
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
        <router-link to="/data-visualization" class="sidebar-nav-link">
          <span class="sidebar-icon-text">
            <!-- 使用 Font Awesome 的图表图标表示数据可视化 -->
            <i class="fas fa-chart-bar"></i>
            <span>数据可视化</span>
          </span>
        </router-link>
      </el-menu-item>

      <!-- 个人中心菜单项 -->
      <el-menu-item index="4">
        <router-link to="/person" class="sidebar-nav-link">
          <span class="sidebar-icon-text">
            <!-- 使用 Font Awesome 的用户图标表示个人中心 -->
            <i class="fas fa-user"></i>
            <span>个人中心</span>
          </span>
        </router-link>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';

const selectedIndex = ref("3");
const defaultOpeneds = ref(["1", "3"]);

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
</script>

<style scoped>
/* 引入 Font Awesome 图标库 */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css');

.sidebar {
  min-width: 100px;
  height: 100vh;
}

.sidebar-icon-text {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--color-text);
  font-size: 16px;
}


.sidebar-nav-link {
  color: var(--color-text);
  text-decoration: none;
  display: flex;
  align-items: center;
  padding: 0 10px 0 10px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.sidebar-nav-link:hover {
  background-color: rgba(255, 255, 255, 0);

}

.sidebar-nav-link.router-link-active {

  font-weight: bold;
}

</style>
