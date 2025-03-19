<template>
  <div class="user-settings-container">
    <!-- 用户基本信息及操作区域 -->
    <div class="main-content">
      <div class="user-info-section">
        <figure class="user-avatar">
          <img src="@/assets/logo.svg" alt="用户头像" />
        </figure>
        <div class="user-details">
          <div class="detail-item">
            <span class="detail-label">用户名:</span>
            <span class="detail-value">AI跃迁者</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">账号ID:</span>
            <span class="detail-value">2489372598</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">注册时间:</span>
            <span class="detail-value">2025-01-01</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">实名认证:</span>
            <span class="detail-value verified">已认证</span>
            <button class="action-button" @click="handleModifyVerification">修改</button>
          </div>
          <div class="detail-item">
            <span class="detail-label">手机号码:</span>
            <span class="detail-value">1234567890</span>
            <button class="action-button" @click="handleModifyPhone">修改</button>
          </div>
        </div>
      </div>

      <!-- 选项卡切换区域 -->
      <div class="settings-tabs">
        <button
          class="tab-button"
          :class="{ active: activeTab === 'security' }"
          @click="activeTab = 'security'"
        >
          安全设置
        </button>
        <button
          class="tab-button"
          :class="{ active: activeTab === 'certification' }"
          @click="activeTab = 'certification'"
        >
          实名认证
        </button>
      </div>

      <!-- 安全设置内容展示区域 -->
      <div v-if="activeTab === 'security'" class="security-settings-section">
        <!-- 登录密码 -->
        <div class="security-setting-item">
          <span class="setting-title">登录密码:</span>
          <span class="setting-description"
            >已设置。密码至少6位字符，支持数字、字母和除空格外的特殊字符，且必须同时包含数字和大小写字母。</span
          >
          <button class="action-button" @click="handleModifyPassword">修改</button>
        </div>
        <!-- 密保问题 -->
        <div class="security-setting-item">
          <span class="setting-title">密保问题:</span>
          <span class="setting-description"
            >您暂未设置密保问题，密保问题可以有效的保护账号的安全。</span
          >
          <button class="action-button" @click="handleSetSecurityQuestion">设置</button>
        </div>
        <!-- 安全手机 -->
        <div class="security-setting-item">
          <span class="setting-title">安全手机:</span>
          <span class="setting-description">已绑定: 150****50</span>
          <button class="action-button" @click="handleModifySecurityPhone">修改</button>
        </div>
        <!-- 安全邮箱 -->
        <div class="security-setting-item">
          <span class="setting-title">安全邮箱:</span>
          <span class="setting-description"
            >您暂未设置邮箱，绑定邮箱可以用来找回密码、接收通知等。</span
          >
          <button class="action-button" @click="handleSetSecurityEmail">设置</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 控制当前激活的选项卡
const activeTab = ref('security')

// 模拟修改实名认证的方法
const handleModifyVerification = () => {
  console.log('点击了修改实名认证')
}

// 模拟修改手机号码的方法
const handleModifyPhone = () => {
  console.log('点击了修改手机号码')
}

// 模拟修改登录密码的方法
const handleModifyPassword = () => {
  console.log('点击了修改登录密码')
}

// 模拟设置密保问题的方法
const handleSetSecurityQuestion = () => {
  console.log('点击了设置密保问题')
}

// 模拟修改安全手机的方法
const handleModifySecurityPhone = () => {
  console.log('点击了修改安全手机')
}

// 模拟设置安全邮箱的方法
const handleSetSecurityEmail = () => {
  console.log('点击了设置安全邮箱')
}
</script>

<style scoped>
/* 让页面占据整个可视区域 */
html,
body {
  height: 100%;
  margin: 0;
}

.user-settings-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  min-height: 100vh;
  box-sizing: border-box;
}

/* 主要内容区域样式 */
.main-content {
  flex: 1;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 用户信息展示区域样式 */
.user-info-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 20px;
}

.user-avatar img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-right: 20px;
  object-fit: cover;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-details {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.detail-label {
  font-weight: 600;
  color: #666;
  min-width: 80px;
}

.detail-value {
  color: #333;
}

.verified {
  color: #28a745;
  font-weight: 600;
}

/* 选项卡切换样式 */
.settings-tabs {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.tab-button {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  background-color: #f4f6f8;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease;
  font-size: 16px;
  font-weight: 600;
  color: #666;
}

.tab-button.active {
  background-color: #28a745;
  color: #fff;
}

.tab-button:hover {
  background-color: #e9ecef;
}

/* 安全设置内容样式 */
.security-settings-section {
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 20px;
}

.security-setting-item {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  transition: box-shadow 0.3s ease;
}

.security-setting-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.setting-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  min-width: 120px;
}

.setting-description {
  font-size: 14px;
  color: #666;
  flex: 1;
  margin-right: 20px;
}

.action-button {
  padding: 8px 16px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-size: 14px;
}

.action-button:hover {
  background-color: #218838;
}
.user-settings-container {
  height: 100%;
}

.main-content {
  height: 100%;
  /* 可以根据需要添加其他样式 */
}
</style>
