<template>
  <div id="app">
    <!-- 引入顶部导航栏组件 -->
    <TopNavBar />
    <div class="sidebar-main-container">
      <!-- 引入侧边栏组件 -->
      <SideBar />
<!--       主内容区域，用于显示路由匹配的组件-->
      <main class="main-content">
        <router-view></router-view>
      </main>
<!--      //可以考虑动画的淡入淡出效果-->
<!--      <transition name="fade">-->
<!--        <router-view></router-view>-->
<!--      </transition>-->
    </div>
  </div>
</template>

<script setup>
// 引入顶部导航栏组件
import TopNavBar from './components/link/TopNavBar.vue'
// 引入侧边栏组件
import SideBar from './components/link/SideBar.vue'
// 引入 Vue Router 的 RouterView 组件
import { RouterView } from 'vue-router'

</script>

<style scoped>
.sidebar-main-container {
  display: flex;
  flex: 1;
}

.main-content {
  flex: 1;
  padding: 20px;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

</style>
