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
  <el-main class="search-results-page">
    <div class="content-wrapper">
      <h2>搜索结果：{{ keyword }}</h2>

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
    </div>
  </el-main>
</template>


<style scoped>
.search-results-page {
  max-width: 900px;
  margin: 20px auto;
  padding: 10px 15px;
}

.results-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.result-item {
  cursor: pointer;
  display: flex;
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.85); /* 半透明白色 */
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
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: 1500px auto; /* 或根据需求调整为 cover 或百分比 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 关键属性：背景固定 */
  background-color: #7b6b4d; /* 深羊驼色兜底 */
  min-height: 100vh;
}
</style>
