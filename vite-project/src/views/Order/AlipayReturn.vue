<template>
  <div class="container">
    <p>支付处理中，此窗口将自动关闭...</p>
    <p v-if="!closed">如果窗口没有自动关闭，请手动关闭此标签页。</p>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';

const closed = ref(false);

// onMounted 是Vue的生命周期钩子，当组件被加载到页面上后会自动执行。
onMounted(() => {
  // 延迟一小段时间再关闭，以确保脚本能成功执行
  setTimeout(() => {
    window.close();
    // 如果上面这行代码因为浏览器安全限制而失败，下面的提示依然会留在页面上。
    closed.value = true;
  }, 200);
});
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  text-align: center;
  font-family: sans-serif;
  color: #555;
}
</style>