<template>
  <div class="section">
    <h2>WebSocket测试</h2>
    <button @click="connectWebSocket">连接WebSocket</button>
    <div id="wsLog"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ws: null
    };
  },
  methods: {
    connectWebSocket() {
      this.ws = new WebSocket('ws://localhost:8080/ws/simulation');

      this.ws.onopen = () => {
        this.logWsMessage('连接已建立');
        this.ws.send(JSON.stringify({
          type: 'start-dialogue',
          simulationId: 1,
          data: { prompt: "测试问题" }
        }));
      };

      this.ws.onmessage = (event) => {
        this.logWsMessage('收到消息: ' + event.data);
      };

      this.ws.onerror = (error) => {
        this.logWsMessage('错误: ' + error.message);
      };

      this.ws.onclose = () => {
        this.logWsMessage('连接已关闭');
      };
    },
    logWsMessage(message) {
      const log = document.getElementById('wsLog');
      log.innerHTML += `<div>${new Date().toLocaleTimeString()}: ${message}</div>`;
      log.scrollTop = log.scrollHeight;
    }
  }
};
</script>

<style scoped>
/* 样式部分可以参考前面的组件 */
#wsLog {
  height: 200px;
  overflow-y: scroll;
  border: 1px solid #ddd;
  padding: 10px;
}
</style>
