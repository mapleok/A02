<script setup lang="ts">
defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "Unity"
});
</script>

<template>
  <h1>Unity实时画面2</h1>
  <iframe
    src="src/unity-webgl/index1.html"
    width="100%"
    height="650px"
    frameborder="0"
    class="unity-iframe"
  />
</template>
