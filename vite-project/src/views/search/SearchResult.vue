<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { searchProduct } from '../../api/product.ts';

const route = useRoute();
const router = useRouter();

const keyword = ref(route.params.keyword as string || '');
const results = ref<any[]>([]);
const loading = ref(false);
const error = ref('');

async function fetchResults() {
  if (!keyword.value.trim()) {
    results.value = [];
    return;
  }
  loading.value = true;
  error.value = '';
  try {
    const res = await searchProduct(keyword.value);
    results.value = res.data.data || [];
  } catch (e) {
    error.value = '搜索失败，请稍后重试';
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  fetchResults();
});

// 监听路由参数变化，关键词变化时重新搜索
watch(() => route.params.keyword, (newKeyword) => {
  keyword.value = newKeyword as string;
  fetchResults();
});

function goToProduct(id: number) {
  router.push(`/home/product/${id}`);
}
</script>

<template>
  <div class="bgimage"> <!-- 全屏背景容器 -->
    <el-main class="search-results-page">


      <div v-if="loading">加载中...</div>
      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="!loading && results.length === 0">没有找到相关商品</div>

      <div class="results-list">
        <div v-for="item in results" :key="item.id" class="result-item" @click="goToProduct(item.id)">
          <img :src="item.cover || 'https://via.placeholder.com/120x160?text=无图'" alt="书籍图片" class="book-img" />
          <div class="info">
            <h3 class="title">{{ item.title }}</h3>
            <p class="author">{{ item.author || '未知作者' }}</p>
            <p class="price">价格: ￥{{ item.price?.toFixed(2) || '暂无' }}</p>
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>



<style scoped>


.results-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.result-item {
  cursor: pointer;
  display: flex;
  width: 100%;
  max-width: 250px;
  border-radius: 8px;
  padding: 12px;
  background-color: rgba(255, 248, 220, 0.8); /* 半透明白色 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  transition: box-shadow 0.3s;
  border: none; /* 去掉灰色边框 */
}


.result-item:hover {
  box-shadow: 0 3px 10px rgba(0,0,0,0.15);
}

.book-img {
  width: 120px;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.info {
  flex: 1;
}

.title {
  font-size: 18px;
  margin: 0 0 6px 0;
  font-weight: 700;
}

.author,
.price {
  margin: 0 0 4px 0;
  color: #666;
}
.error {
  color: red;
}
/* 让背景图铺满整个页面 */
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-color: #7b6b4d;
  min-height: 100vh;
  width: 100%;
}

/* 内容宽度限制和居中 */
.search-results-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 30px 20px;
}


</style>
