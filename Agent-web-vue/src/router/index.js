import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import Workbench from '../components/Workbench.vue';
import Background from '../components/Background.vue';
import DataVisualization from '../components/DataVisualization.vue';
import API_Page from '../components/API-Page.vue';
import PersonCenter from '@/components/PersonCenter.vue';
import Agentrole  from '../components/Agentrole.vue';

// 模拟用户是否已登录的函数
function isAuthenticated() {
  // 这里可以根据实际情况判断用户是否已登录，例如检查本地存储中的 token
  return localStorage.getItem('token') !== null;
}

const routes= [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    { path: '/Background',
      component: Background
    },

    { path: '/data-visualization',
      component: DataVisualization
    },
    {
      path: '/workbench',
      name: 'workbench',
      component: Workbench,
    },
    { path: '/API_Page',
      component: API_Page
    },
    {
      path: '/person',
      name: 'person',
      component:PersonCenter,
    },
    { path: '/Background',
      home: 'Background',
      component: Background
    },
    {
      path:'/agent-role',
      home: 'agent-role',
      component:Agentrole
    },
  ];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局前置守卫，用于路由权限验证
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !isAuthenticated()) {
    next('/login');
  } else {
    next();
  }
});

export default router
