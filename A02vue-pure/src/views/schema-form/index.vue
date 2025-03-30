<script setup lang="ts">
import { ref } from "vue";
import { list } from "./list";

defineOptions({
  name: "SchemaForm"
});

const selected = ref(0);

function tabClick({ index }) {
  selected.value = index;
}
</script>

<template>
  <el-card shadow="never" :body-style="{ height: 'calc(100vh - 260px)' }">
    <template #header>
      <span>
        任务设计
      </span>
    </template>
    <el-tabs @tab-click="tabClick">
      <template v-for="(item, index) of list" :key="item.key">
        <el-tab-pane :lazy="true" v-if="index === 3">
          <!-- 渲染第4个示例的内容 -->
            <component :is="item.component" />
            <!-- 内嵌第2个示例的内容 -->
            <component :is="list[1].component" />
        </el-tab-pane>
      </template>
    </el-tabs>
  </el-card>
</template>

<style scoped>
:deep(.el-tabs__nav-wrap)::after {
  height: 1px;
}

:deep(.el-tabs__nav-next),
:deep(.el-tabs__nav-prev) {
  font-size: 16px;
  color: var(--el-text-color-primary);
}

:deep(.el-tabs__nav-next.is-disabled),
:deep(.el-tabs__nav-prev.is-disabled) {
  opacity: 0.5;
}


</style>
