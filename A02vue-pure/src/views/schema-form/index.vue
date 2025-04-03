<script setup lang="ts">
// 引入Vue的ref函数，用于创建响应式数据
import { ref } from "vue";
// 引入名为list的数组数据，假设list.js中定义了相关内容
import { list } from "./list";

// 定义组件的选项，这里设置组件名称为SchemaForm
defineOptions({
  name: "SchemaForm"
});

// 创建一个响应式变量selected，用于存储当前选中的选项卡索引，初始值为0
const selected = ref(0);

// 定义tabClick函数，用于处理选项卡点击事件
function tabClick({ index }) {
  // 将当前点击的选项卡索引赋值给selected，实现选项卡切换效果
  selected.value = index;
}
</script>

<template>
  <!-- 使用Element Plus的el-card组件，设置不显示阴影，body样式为空对象 -->
  <el-card shadow="never" :body-style="{}">
    <!-- 定义el-card的头部插槽内容 -->
    <template #header>
      <!-- 显示文本“任务设计” -->
      <span>任务设计</span>
    </template>
    <!-- 使用Element Plus的el-tabs组件，并绑定tabClick函数到tab-click事件 -->
    <el-tabs @tab-click="tabClick">
      <!-- 使用v-for指令遍历list数组，为每个元素创建一个模板 -->
      <template v-for="(item, index) of list" :key="item.key">
        <!-- 当索引等于3时，渲染el-tab-pane组件 -->
        <el-tab-pane :lazy="true" v-if="index === 3">
          <!-- 动态渲染组件，组件名由item.component指定，这里渲染第4个示例的内容 -->
          <component :is="item.component" />
          <!-- 动态渲染组件，组件名由list数组中第二个元素的component指定，这里内嵌第2个示例的内容 -->
          <component :is="list[1].component" />
        </el-tab-pane>
      </template>
    </el-tabs>
  </el-card>
</template>

<style scoped>
/* 使用深度选择器修改el-tabs组件的导航栏底部线条高度为1px */
:deep(.el-tabs__nav-wrap)::after {
  height: 0px !important;
  display: none !important;
}
/* 修改el-tabs组件的下一个和上一个导航按钮的字体大小为16px，颜色为Element Plus的主要文本颜色 */
:deep(.el-tabs__nav-next),
:deep(.el-tabs__nav-prev) {
  font-size: 16px;
  color: var(--el-text-color-primary);
}
/* 修改el-tabs组件的下一个和上一个导航按钮在禁用状态下的透明度为0.5 */
:deep(.el-tabs__nav-next.is-disabled),
:deep(.el-tabs__nav-prev.is-disabled) {
  opacity: 0.5;
}
</style>
