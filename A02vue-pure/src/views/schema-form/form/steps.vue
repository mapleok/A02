<template>
  <PlusStepsForm
    v-model="active"
    simple
    class="w-[800px] m-auto"
    :data="stepForm as any"
    align="center"
    @next="next"
    @finish="submitAll"
  />
</template>

<script setup lang="ts">
import { ref } from "vue";
import "plus-pro-components/es/components/steps-form/style/css";
import { PlusStepsForm } from "plus-pro-components";
import axios from "axios";

const active = ref(1);
const stepForm = ref([
  {
    title: "第一步",
    form: {
      labelPosition: "top",
      style: {
        width: "400px",
        margin: "40px auto",
        maxHeight: "400px",
        overflowY: "auto"
      },
      labelWidth: "100",
      modelValue: {},
      columns: [
        {
          label: "模拟名称",
          width: 120,
          prop: "name",
          valueType: "copy",
          tooltip: "名称最多显示6个字符"
        },
        {
          label: "模拟类型",
          width: 120,
          prop: "status",
          valueType: "select",
          options: [
            {
              label: "种植业",
              value: "0",
              color: "red"
            },
            {
              label: "畜牧业",
              value: "1",
              color: "blue"
            },
            {
              label: "林业",
              value: "2",
              color: "yellow"
            },
            {
              label: "渔业",
              value: "3",
              color: "red"
            }
          ]
        },
        {
          label: "模拟描述",
          width: 300,
          prop: "environmentDefinition",
          valueType: "input"
        }
      ],
      rules: {
        name: [
          {
            required: true,
            message: "请输入名称"
          }
        ]
      }
    }
  },
  {
    title: "第二步",
    form: {
      labelPosition: "top",
      style: {
        width: "400px",
        margin: "40px auto"
      },
      labelWidth: "100",
      modelValue: {},
      columns: [
        {
          label: "Agent 角色",
          width: 120,
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
          label: "姓名",
          width: 120,
          prop: "agentName",
          valueType: "input"
        },
        {
          label: "角色描述",
          width: 120,
          prop: "description",
          valueType: "textarea",
          fieldProps: {
            maxlength: 200,
            showWordLimit: true,
            autosize: { minRows: 2, maxRows: 4 }
          }
        },
        {
          label: "温度",
          prop: "temperature",
          valueType: "input-number",
          fieldProps: {
            precision: 1,
            step: 0.5
          }
        },
        {
          label: "农业技术",
          prop: "agriculture",
          valueType: "select",
          options: [
            { value: "传统农业", label: "传统农业" },
            { value: "精准农业", label: "精准农业" },
            { value: "有机农业", label: "有机农业" },
            { value: "智能农业", label: "智能农业" }
          ]
        },
        {
          label: "土壤肥力",
          prop: "soil",
          valueType: "select",
          options: [
            { value: "肥沃", label: "肥沃" },
            { value: "中等", label: "中等" },
            { value: "贫瘠", label: "贫瘠" }
          ]
        },
        {
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
        },
        {
          label: "是否显示",
          width: 100,
          prop: "switch",
          valueType: "switch"
        }
      ],
      rules: {
        tag: [
          {
            required: true,
            message: "请输入标签"
          }
        ],
        progress: [
          {
            required: true,
            message: "请输入执行进度"
          }
        ]
      }
    }
  },
  {
    title: "第三步",
    form: {
      labelPosition: "top",
      style: {
        width: "400px",
        margin: "40px auto"
      },
      modelValue: {},
      columns: [
        {
          label: "模拟时间",
          prop: "time",
          valueType: "date-picker"
        }
      ],
      rules: {
        time: [
          {
            required: true,
            trigger: "change",
            message: "请选择时间"
          }
        ]
      }
    }
  }
]);

const formData = ref({
  name: "",
  agentName: "",
  status: "",
  environmentDefinition: "",
  role: "",
  description: "",
  temperature: 0,
  agriculture: "",
  soil: "",
  climate: "",
  switch: false,
  time: null
});

const next = (actives: number, values: any) => {
  console.log("Current step:", actives);
  console.log("Next step triggered", actives, values);
  active.value = actives;

  // 更新 formData
  Object.assign(formData.value, values);
};

const submitAll = () => {
  console.log("submitAll function called");

  // 验证 formData 是否完整
  if (
    !formData.value.name ||
    !formData.value.agentName ||
    !formData.value.status ||
    !formData.value.environmentDefinition ||
    !formData.value.role ||
    !formData.value.description ||
    isNaN(formData.value.temperature) ||
    !formData.value.agriculture ||
    !formData.value.soil ||
    !formData.value.climate ||
    formData.value.time === null
  ) {
    console.error("Some fields are missing or invalid.");
    return;
  }

  // 提交创建模拟数据
  axios.post("http://localhost:8080/api/simulation", {
    name: formData.value.name,
    status: formData.value.status,
    environmentDefinition: formData.value.environmentDefinition
  }).then(response => {
    console.log("Simulation created successfully:", response.data);

    // 提交创建 Agent 数据
    axios.post("http://localhost:8080/api/agent", {
      role: formData.value.role,
      agentName: formData.value.agentName,
      description: formData.value.description
    }).then(response => {
      console.log("Agent created successfully:", response.data);

      // 提交开始对话数据
      axios.post("http://localhost:8080/api/start-dialogue", {
        time: formData.value.time
      }).then(response => {
        console.log("Dialogue started successfully:", response.data);
      }).catch(error => {
        console.error("Error starting dialogue:", error);
      });
    }).catch(error => {
      console.error("Error creating agent:", error);
    });
  }).catch(error => {
    console.error("Error creating simulation:", error);
  });
};
</script>

<style scoped>
.el-step__title.is-success {
  color: var(--el-color-success);
  white-space: nowrap;
}
</style>
