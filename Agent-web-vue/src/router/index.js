import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Workbench from '../components/Workbench.vue';
import Background from '../components/Background.vue';
import DataVisualization from '../components/DataVisualization.vue';
import API_Page from '../components/API-Page.vue';
import PersonCenter from '@/components/PersonCenter.vue';
import Agentrole  from '../components/Agentrole.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
  ],
});

export default router
