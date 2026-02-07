<template>
  <footer class="footer">
    <div class="footer-container">

      <!-- 左侧：品牌介绍 -->
      <div class="footer-section about">
        <h3 class="footer-title">真相书局 TRUTH BOOKSTORE</h3>
        <p class="footer-text">
          专注推理与悬疑小说的线上书局。
          在这里，每一本书都是通往真相的钥匙。
        </p>
        <p class="footer-text">
          © {{ new Date().getFullYear() }} Truth Bookstore. All Rights Reserved.
        </p>
      </div>

      <!-- 中间：快速导航 -->
      <div class="footer-section nav">
        <h4 class="footer-subtitle">快速导航</h4>
        <ul class="footer-list">
          <li @click="router.push('/home/all-products')">全部书籍</li>
          <li @click="router.push('/home/all-columns')">栏目精选</li>
          <li @click="router.push('/home/all-postings')">真相笔记</li>
        </ul>
      </div>

      <!-- 右侧：联系方式 -->
      <div class="footer-section contact">
        <h4 class="footer-subtitle">联系 & 支持</h4>
        <ul class="footer-list">
          <li>Email：support@truthbookstore.com</li>
          <li>服务热线：400-123-9876</li>
          <li >关于我们</li>
          <li >用户协议</li>
          <li >隐私政策</li>
        </ul>
      </div>

    </div>
  </footer>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter();
</script>

<style scoped>
.footer {
  background: #111;
  color: #ddd;
  padding: 50px 40px 60px; /* 上下内边距可以保留 */
  margin-top: 0;            /* 去掉多余空白 */
  border-top: 1px solid rgba(255,255,255,0.08);
}


.footer-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 50px;
}

/* 左侧品牌 */
.footer-title {
  color: #e7d3a0;
  margin-bottom: 12px;
  letter-spacing: 1px;
}
.footer-text {
  margin: 6px 0;
  line-height: 1.6;
  opacity: 0.8;
}

/* 每一列 */
.footer-section {
  flex: 1;
  min-width: 250px;
}

/* 小标题 */
.footer-subtitle {
  font-size: 16px;
  margin-bottom: 12px;
  color: #d7caa8;
}

/* 列表 */
.footer-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.footer-list li {
  margin: 8px 0;
  cursor: pointer;
  transition: 0.2s;
  opacity: 0.85;
}
.footer-list li:hover {
  color: #fff;
  opacity: 1;
  transform: translateX(4px);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .footer-container {
    flex-direction: column;
    gap: 40px;
  }
  .footer {
    padding: 40px 20px 60px;
  }
}
</style>
