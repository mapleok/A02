<template>
  <div class="section">
    <h2>农业收成数据</h2>
    <button @click="handleGetHarvestData" class="generate-button">获取收成数据</button>
    <pre>{{ harvestResult }}</pre>
  </div>
  <div class="container">

    <!-- 第一行：柱状图和折线图 -->
    <div class="row">
      <div class="chart-container">
        <h3>作物产量</h3>
        <div ref="barChartRef" class="chart"></div>
      </div>
      <div class="chart-container">
        <h3>收益走线图</h3>
        <div ref="lineChartRef" class="chart"></div>
      </div>
    </div>
    <!-- 第二行：饼状图和堆叠柱状图 -->
    <div class="row">
      <div class="chart-container">
        <h3>作物种类</h3>
        <div ref="pieChartRef" class="chart"></div>
      </div>
      <div class="chart-container">
        <h3>产量对比图</h3>
        <div ref="stackedBarChartRef" class="chart"></div>
      </div>
    </div>

    <!-- 第三行：双Y轴折线图和嵌套饼图 -->
    <div class="row">
      <div class="chart-container">
        <h3>收益对比图</h3>
        <div ref="doubleYLineChartRef" class="chart"></div>
      </div>
      <div class="chart-container">
        <h3>嵌套饼图</h3>
        <div ref="nestedPieChartRef" class="chart"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, nextTick, watch } from 'vue'
import * as echarts from 'echarts'

import {
getHarvestData,
} from '../api/index'

// 农业收成数据
const harvestSimId = ref('')
const harvestResult = ref('')


// 获取收成数据
const handleGetHarvestData = async () => {
  const result = await getHarvestData(harvestSimId.value)
  harvestResult.value = JSON.stringify(result, null, 2)
}


// 模拟数据
// 柱状图数据：不同作物每周的产量
const barData = {
  xAxisData: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
  seriesData: [150, 220, 180, 130, 160, 200, 210],
}

// 折线图数据：每月的农场营收
const lineData = {
  xAxisData: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
  seriesData: [5000, 6000, 7500, 8000, 9200, 10500, 11000],
}

// 饼状图数据：不同作物的营收占比
const pieData = [
  { value: 3000, name: '小麦' },
  { value: 2500, name: '玉米' },
  { value: 2000, name: '蔬菜' },
  { value: 1500, name: '水果' },
  { value: 1000, name: '其他' },
]

// 堆叠柱状图数据：不同作物每周的产量（分为早熟和晚熟品种）
const stackedBarData = {
  xAxisData: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
  seriesData: [
    { name: '早熟品种', data: [80, 100, 90, 70, 85, 95, 100] },
    { name: '晚熟品种', data: [70, 120, 90, 60, 75, 105, 110] },
  ],
}

// 双Y轴折线图数据：每月的农场营收和成本
const doubleYLineData = {
  xAxisData: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
  seriesData1: [5000, 6000, 7500, 8000, 9200, 10500, 11000], // 营收
  seriesData2: [3000, 3500, 4000, 4200, 4500, 4800, 5000], // 成本
}

// 嵌套饼图数据：不同作物的营收细分（分为批发和零售）
const nestedPieData = [
  {
    value: 3000,
    name: '小麦',
    children: [
      { value: 2000, name: '批发' },
      { value: 1000, name: '零售' },
    ],
  },
  { value: 2500, name: '玉米' },
  { value: 2000, name: '蔬菜' },
  { value: 1500, name: '水果' },
  { value: 1000, name: '其他' },
]

const barChartRef = ref(null)
const lineChartRef = ref(null)
const pieChartRef = ref(null)
const stackedBarChartRef = ref(null)
const doubleYLineChartRef = ref(null)
const nestedPieChartRef = ref(null)

// 公共的 xAxis 和 yAxis 配置
const commonAxisOptions = {
  axisLine: { lineStyle: { color: '#ccc' } },
}

// 初始化柱状图
const initBarChart = () => {
  const barChart = echarts.init(barChartRef.value)
  const barOption = {
    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
      { offset: 0, color: '#409EFF' },
      { offset: 1, color: '#67C23A' },
    ]),
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
    },
    xAxis: {
      ...commonAxisOptions,
      type: 'category',
      data: barData.xAxisData,
    },
    yAxis: {
      ...commonAxisOptions,
      type: 'value',
    },
    series: [
      {
        data: barData.seriesData,
        type: 'bar',
        itemStyle: {
          borderRadius: [5, 5, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#409EFF' },
            { offset: 1, color: '#67C23A' },
          ]),
        },
      },
    ],
  }
  barChart.setOption(barOption)
  return barChart
}

// 初始化折线图
const initLineChart = () => {
  const lineChart = echarts.init(lineChartRef.value)
  const lineOption = {
    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
      { offset: 0, color: '#E6A23C' },
      { offset: 1, color: '#F56C6C' },
    ]),
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      ...commonAxisOptions,
      type: 'category',
      data: lineData.xAxisData,
    },
    yAxis: {
      ...commonAxisOptions,
      type: 'value',
    },
    series: [
      {
        data: lineData.seriesData,
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 3,
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#E6A23C' },
            { offset: 1, color: '#F56C6C' },
          ]),
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(230, 162, 60, 0.6)' },
            { offset: 1, color: 'rgba(245, 108, 108, 0.2)' },
          ]),
        },
      },
    ],
  }
  lineChart.setOption(lineOption)
  return lineChart
}

// 初始化饼状图
const initPieChart = () => {
  const pieChart = echarts.init(pieChartRef.value)
  const pieOption = {
    color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'],
    tooltip: {
      trigger: 'item',
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: pieData,
        label: {
          show: true,
          formatter: '{b}: {c} ({d}%)',
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2,
        },
      },
    ],
  }
  pieChart.setOption(pieOption)
  return pieChart
}

// 初始化堆叠柱状图
const initStackedBarChart = () => {
  const stackedBarChart = echarts.init(stackedBarChartRef.value)
  const stackedBarOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
    },
    legend: {
      data: stackedBarData.seriesData.map((item) => item.name),
    },
    xAxis: {
      type: 'category',
      data: stackedBarData.xAxisData,
    },
    yAxis: {
      type: 'value',
    },
    series: stackedBarData.seriesData.map((item) => ({
      name: item.name,
      type: 'bar',
      stack: 'total',
      data: item.data,
    })),
  }
  stackedBarChart.setOption(stackedBarOption)
  return stackedBarChart
}

// 初始化双Y轴折线图
const initDoubleYLineChart = () => {
  const doubleYLineChart = echarts.init(doubleYLineChartRef.value)
  const doubleYLineOption = {
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: ['营收', '成本'],
    },
    xAxis: {
      type: 'category',
      data: doubleYLineData.xAxisData,
    },
    yAxis: [
      {
        type: 'value',
        name: '营收',
        axisLine: {
          lineStyle: {
            color: '#409EFF',
          },
        },
        axisLabel: {
          color: '#409EFF',
        },
      },
      {
        type: 'value',
        name: '成本',
        axisLine: {
          lineStyle: {
            color: '#E6A23C',
          },
        },
        axisLabel: {
          color: '#E6A23C',
        },
      },
    ],
    series: [
      {
        name: '营收',
        type: 'line',
        data: doubleYLineData.seriesData1,
        yAxisIndex: 0,
        lineStyle: {
          color: '#409EFF',
        },
      },
      {
        name: '成本',
        type: 'line',
        data: doubleYLineData.seriesData2,
        yAxisIndex: 1,
        lineStyle: {
          color: '#E6A23C',
        },
      },
    ],
  }
  doubleYLineChart.setOption(doubleYLineOption)
  return doubleYLineChart
}

// 初始化嵌套饼图
const initNestedPieChart = () => {
  const nestedPieChart = echarts.init(nestedPieChartRef.value)
  const nestedPieOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
    },
    series: [
      {
        name: '作物营收细分',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center',
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '30',
            fontWeight: 'bold',
          },
        },
        labelLine: {
          show: false,
        },
        data: nestedPieData,
      },
    ],
  }
  nestedPieChart.setOption(nestedPieOption)
  return nestedPieChart
}

let barChartInstance
let lineChartInstance
let pieChartInstance
let stackedBarChartInstance
let doubleYLineChartInstance
let nestedPieChartInstance

onMounted(() => {
  nextTick(() => {
    barChartInstance = initBarChart()
    lineChartInstance = initLineChart()
    pieChartInstance = initPieChart()
    stackedBarChartInstance = initStackedBarChart()
    doubleYLineChartInstance = initDoubleYLineChart()
    nestedPieChartInstance = initNestedPieChart()

    // 监听窗口大小变化
    const resizeHandler = () => {
      barChartInstance.resize()
      lineChartInstance.resize()
      pieChartInstance.resize()
      stackedBarChartInstance.resize()
      doubleYLineChartInstance.resize()
      nestedPieChartInstance.resize()
    }
    window.addEventListener('resize', resizeHandler)

    // 在组件销毁时移除监听
    onUnmounted(() => {
      window.removeEventListener('resize', resizeHandler)
      barChartInstance.dispose()
      lineChartInstance.dispose()
      pieChartInstance.dispose()
      stackedBarChartInstance.dispose()
      doubleYLineChartInstance.dispose()
      nestedPieChartInstance.dispose()
    })
  })
})

// 监听数据变化，重新初始化图表
watch([barData, lineData, pieData, stackedBarData, doubleYLineData, nestedPieData], () => {
  barChartInstance = initBarChart()
  lineChartInstance = initLineChart()
  pieChartInstance = initPieChart()
  stackedBarChartInstance = initStackedBarChart()
  doubleYLineChartInstance = initDoubleYLineChart()
  nestedPieChartInstance = initNestedPieChart()
}, { deep: true })
</script>

<style scoped>
.container {
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 两列布局 */
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.chart-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.chart-container:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.chart-container h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
}

.chart {
  width: 100%;
  height: 300px;
}

.generate-button,
.btn {
  padding:  10px 10px ;
  background-color: #006400; /* 墨绿色按钮 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;

}
.generate-button:hover,
.btn:hover {
  background-color: #004d00; /* 深一点的墨绿色悬停效果 */
}

</style>
