<script setup>
import { RouterLink } from 'vue-router';
</script>

<template>
  <div class="top-nav-bar">
    <div class="logo">
      <i class="fas fa-tractor"></i> 多Agent智慧农业模拟系统
    </div>
    <div class="nav-links">
      <router-link to="/workbench" class="top-nav-link">
        <i class="fas fa-tools"></i> 工作台
      </router-link>
      <router-link to="/agent-role" class="top-nav-link">
        <i class="fas fa-file-alt"></i> 任务设计
      </router-link>
      <router-link to="/data-visualization" class="top-nav-link">
        <i class="fas fa-chart-bar"></i> 数据可视化
      </router-link>
      <router-link to="/websocket" class="top-nav-link">
        <i class="fas fa-user"></i> 个人中心
      </router-link>
    </div>
  </div>
</template>

<style scoped>
/* 引入 Font Awesome 图标库 */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css');

/* 顶部导航栏链接样式 */
.top-nav-link {
  color: #ffffff;
  display: flex;
  align-items: center;
  text-decoration: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 18px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* 顶部导航栏链接鼠标悬停样式 */
.top-nav-link:hover {
  background-color: #e0f2c1; /* 农场主题的淡绿色 */
  color: #7c9f4a; /* 农场主题的深绿色 */
  font-weight: bold;
}

/* 顶部导航栏激活链接样式 */
.top-nav-link.router-link-active {
  font-weight: bold;
  color: #4c662c; /* 农场主题的深绿色 */
}

/* 顶部导航栏样式 */
.top-nav-bar {
  background-color: #7c9f4a; /* 农场主题的绿色 */
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

/* 顶部导航栏 logo 样式 */
.logo {
  font-size: 20px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.logo i {
  margin-right: 10px;
}

/* 导航链接容器样式 */
.nav-links {
  display: flex;
  align-items: center;
  color: #e0e0e0;
}

/* 导航链接图标样式 */
.top-nav-link i {
  margin-right: 5px;
}
</style>
