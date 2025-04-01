<script setup lang="ts">
import { ref } from "vue";
// 引入 PlusProComponents 的对话框表单组件样式
import "plus-pro-components/es/components/dialog-form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

// 定义表单列配置
const columns: PlusColumn[] = [
  {
    // 标签显示为 "模拟总描述"
    label: "模拟描述",
    width: 300, // 增加宽度以确保描述能在一行显示
    prop: "environmentDefinition",
    valueType: "input"
    // tooltip: "请简要描述环境模拟的情况"
  },
  {
    // 标签显示为 "Agent 角色"
    label: "Agent ",
    width: 120,
    // 对应数据对象的属性名
    prop: "role",
    valueType: "cascader",
    options: [
      {
        value: "0",
        label: "技术型人员",
        children: [
          {
            value: "0-0",
            label: "农业专家"
          },
          {
            value: "0-1",
            label: "气象专家"
          }
        ]
      },
      {
        value: "1",
        label: "操作型人员",
        children: [
          {
            value: "1-0",
            label: "无人机驾驶员"
          },
          {
            value: "1-1",
            label: "农业技术员"
          }
        ]
      },
      {
        value: "2",
        label: "管理型人员",
        children: [
          {
            value: "2-0",
            label: "总负责人"
          }
        ]
      }
    ]
  },
  {
    // 标签显示为 "姓名"
    label: "姓名",
    width: 120,
    prop: "name",
    valueType: "input"
    // 提示信息
    // tooltip: "请输入 Agent 的姓名"
  },
  {
    // 标签显示为 "角色描述"
    label: "角色描述",
    width: 120,
    prop: "description",
    valueType: "textarea",
    fieldProps: {
      // 最大输入长度
      maxlength: 200,
      // 显示字数限制
      showWordLimit: true,
      // 自动调整文本区域大小
      autosize: { minRows: 2, maxRows: 4 }
    }
  },
  {
    // 标签显示为 "温度"
    label: "温度",
    prop: "temperature",
    valueType: "input-number",
    fieldProps: {
      // 精度为 1 位小数
      precision: 1,
      // 步长为 0.5
      step: 0.5
    }
  },
  {
    label: "农业技术",
    prop: "arigiculture",
    valueType: "select",
    options: [
      { value: '传统农业', label: '传统农业' },
      { value: '精准农业', label: '精准农业' },
      { value: '有机农业', label: '有机农业' },
      { value: '智能农业', label: '智能农业' },
    ]
  },
  {
    label: "土壤肥力",
    prop: "soil",
    valueType: "select",
    options: [
      { value: '肥沃', label: '肥沃' },
      { value: '中等', label: '中等' },
      { value: '贫瘠', label: '贫瘠' },
    ]
  },
  {
    // 标签显示为 "气候情况"
    label: "气候情况",
    prop: "climate",
    valueType: "select",
    options: [
      {
        label: "温带季风气候",
        value: "temperate_monsoon"
      },
      {
        label: "热带雨林气候",
        value: "tropical_rainforest"
      },
      {
        label: "内陆干旱气候",
        value: "inland_drought"
      },
      {
        label: "温带海洋气候",
        value: "temperate_marine"
      }
    ]
  }
];

// 控制对话框是否显示的响应式变量
const visible = ref(false);
// 表单数据对象
const values = ref<FieldValues>({});

// 处理打开对话框的函数
const handleOpen = () => {
  visible.value = true;
};

// 表单提交处理函数
const handleSubmit = () => {
  // 这里可以添加实际的表单数据提交逻辑，例如发送到后端接口
  console.log('提交的表单数据:', values.value);
  // 提交后关闭弹窗
  visible.value = false;
};
</script>

<template>
  <div>
    <!-- 点击按钮触发 handleOpen 函数打开对话框 -->
    <el-button @click="handleOpen">场景任务设计</el-button>
    <!-- 对话框表单组件，绑定可见性和表单数据 -->
    <PlusDialogForm
      v-model:visible="visible"
      v-model="values"
      :form="{ columns }"
      @submit="handleSubmit"
    />
  </div>
</template>
