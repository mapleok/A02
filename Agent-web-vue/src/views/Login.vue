<template>
  <div>
    <h1>Login Page</h1>
    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />
    <button @click="login">Login</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const username = ref('');
const password = ref('');

const login = () => {
  // 这里可以添加实际的登录逻辑，例如发送请求到后端验证用户名和密码
  // 为了简单起见，我们直接模拟登录成功并设置 token
  localStorage.setItem('token', 'dummy_token');
  router.push('/');
};
</script>

<style scoped>
/* 这里可以添加 Login 页面的样式 */
</style>
