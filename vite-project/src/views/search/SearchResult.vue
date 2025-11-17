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
import { addCart } from '../../api/cart.ts';
import { ElMessage } from 'element-plus';

async function handleAddCart(productId: number) {
  const userId = Number(sessionStorage.getItem('userId'));
  if (!userId) {
    ElMessage.warning('请先登录');
    return;
  }
  try {
    const res = await addCart(userId, productId, 1);
    if (res.data.code === '200') ElMessage.success('已加入购物车');
    else ElMessage.error(res.data.msg || '添加失败');
  } catch {
    ElMessage.error('网络错误');
  }
}
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
    <!-- 加载/空态保持原样 -->
    <div v-if="loading">加载中...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="!loading && results.length === 0">没有找到相关商品</div>

    <!-- 当当式横向列表 -->
    <div class="results-list">
      <div
          v-for="item in results"
          :key="item.id"
          class="result-row"
          @click="goToProduct(item.id)"
      >
        <!-- 左侧图书封面 -->
        <img
            :src="item.cover || 'https://via.placeholder.com/90x120?text=无图'"
            alt="书籍图片"
            class="book-img"
        />

        <!-- 右侧信息区 -->
        <div class="info-wrap">
          <!-- 第一行：书名 + 价格 -->
          <div class="line1">
            <h3 class="title">{{ item.title }}</h3>
            <span class="price">￥{{ item.price?.toFixed(2) }}</span>
          </div>

          <!-- 第二行：简介 -->
          <p class="intro">{{ item.description || '暂无简介' }}</p>

          <!-- 第三行：评分 + 加入购物车 -->
          <div class="line3">
            <el-rate
                :model-value="item.rate || 0"
                disabled
                size="small"
                show-score
                text-color="#999"
            />
            <el-button
                type="warning"
                size="small"
                class="add-cart-btn"
                @click.stop="handleAddCart(item.id)"
            >
              加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>


<style scoped>
/* 整区白底 */
.search-results-page {
  background: #fff;
  min-height: 100vh;
  padding: 20px 0;
}

/* 单条横向卡片 */
.results-list {
  max-width: 1000px;
  margin: 0 auto;
}
.result-row {
  display: flex;
  align-items: flex-start;
  padding: 15px 20px;
  border-bottom: 1px solid #ebebeb;
  cursor: pointer;
  transition: background 0.2s;
}
.result-row:hover {
  background: #fafafa;
}

.book-img {
  width: 90px;
  height: 120px;
  object-fit: cover;
  flex-shrink: 0;
  margin-right: 18px;
}

/* 右侧信息 */
.info-wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.line1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}
.price {
  font-size: 18px;
  color: #e4393c;
  font-weight: bold;
}
.intro {
  font-size: 13px;
  color: #666;
  margin: 8px 0 12px 0;
  line-height: 1.5;
}
.line3 {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.add-cart-btn {
  margin-left: auto;
}
</style>