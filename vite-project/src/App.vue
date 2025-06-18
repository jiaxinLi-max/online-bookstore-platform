<script setup lang="ts">
</script>

<template>
  <el-container direction="vertical" class="app-wrapper">
    <router-view />
  </el-container>
</template>

<style>
/* 全局背景图设置 */
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
}

body {
  background-image: url("assets/780.jpg"); /* 确保路径正确 */
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center top;
  background-attachment: fixed;
  background-color: #7b6b4d;
}

.app-wrapper {
  min-height: 100%;
  background-color: transparent; /* 不遮盖背景图 */
}
</style>
