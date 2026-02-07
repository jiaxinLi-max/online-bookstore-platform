<!--<script setup lang="ts">-->
<!--import { onMounted, ref, watch } from 'vue';-->
<!--import { useRoute, useRouter } from 'vue-router';-->
<!--import { searchProduct } from '../../api/product.ts';-->

<!--const route = useRoute();-->
<!--const router = useRouter();-->

<!--const keyword = ref(route.params.keyword as string || '');-->
<!--const results = ref<any[]>([]);-->
<!--const loading = ref(false);-->
<!--const error = ref('');-->
<!--import { addCart } from '../../api/cart.ts';-->
<!--import { ElMessage } from 'element-plus';-->

<!--async function handleAddCart(productId: number) {-->
<!--  const userId = Number(sessionStorage.getItem('userId'));-->
<!--  if (!userId) {-->
<!--    ElMessage.warning('请先登录');-->
<!--    return;-->
<!--  }-->
<!--  try {-->
<!--    const res = await addCart(userId, productId, 1);-->
<!--    if (res.data.code === '200') ElMessage.success('已加入购物车');-->
<!--    else ElMessage.error(res.data.msg || '添加失败');-->
<!--  } catch {-->
<!--    ElMessage.error('网络错误');-->
<!--  }-->
<!--}-->
<!--async function fetchResults() {-->
<!--  if (!keyword.value.trim()) {-->
<!--    results.value = [];-->
<!--    return;-->
<!--  }-->
<!--  loading.value = true;-->
<!--  error.value = '';-->
<!--  try {-->
<!--    const res = await searchProduct(keyword.value);-->
<!--    results.value = res.data.data || [];-->
<!--  } catch (e) {-->
<!--    error.value = '搜索失败，请稍后重试';-->
<!--  } finally {-->
<!--    loading.value = false;-->
<!--  }-->
<!--}-->

<!--onMounted(() => {-->
<!--  fetchResults();-->
<!--});-->

<!--// 监听路由参数变化，关键词变化时重新搜索-->
<!--watch(() => route.params.keyword, (newKeyword) => {-->
<!--  keyword.value = newKeyword as string;-->
<!--  fetchResults();-->
<!--});-->

<!--function goToProduct(id: number) {-->
<!--  router.push(`/home/product/${id}`);-->
<!--}-->
<!--</script>-->

<!--<template>-->
<!--  <el-main class="search-results-page">-->
<!--    &lt;!&ndash; 加载/空态保持原样 &ndash;&gt;-->
<!--    <div v-if="loading">加载中...</div>-->
<!--    <div v-if="error" class="error">{{ error }}</div>-->
<!--    <div v-if="!loading && results.length === 0">没有找到相关商品</div>-->

<!--    &lt;!&ndash; 当当式横向列表 &ndash;&gt;-->
<!--    <div class="results-list">-->
<!--      <div-->
<!--          v-for="item in results"-->
<!--          :key="item.id"-->
<!--          class="result-row"-->
<!--          @click="goToProduct(item.id)"-->
<!--      >-->
<!--        &lt;!&ndash; 左侧图书封面 &ndash;&gt;-->
<!--        <img-->
<!--            :src="item.cover || 'https://via.placeholder.com/90x120?text=无图'"-->
<!--            alt="书籍图片"-->
<!--            class="book-img"-->
<!--        />-->

<!--        &lt;!&ndash; 右侧信息区 &ndash;&gt;-->
<!--        <div class="info-wrap">-->
<!--          &lt;!&ndash; 第一行：书名 + 价格 &ndash;&gt;-->
<!--          <div class="line1">-->
<!--            <h3 class="title">{{ item.title }}</h3>-->
<!--            <span class="price">￥{{ item.price?.toFixed(2) }}</span>-->
<!--          </div>-->

<!--          &lt;!&ndash; 第二行：简介 &ndash;&gt;-->
<!--          <p class="intro">{{ item.description || '暂无简介' }}</p>-->

<!--          &lt;!&ndash; 第三行：评分 + 加入购物车 &ndash;&gt;-->
<!--          <div class="line3">-->
<!--            <el-rate-->
<!--                :model-value="item.rate || 0"-->
<!--                disabled-->
<!--                size="small"-->
<!--                show-score-->
<!--                text-color="#999"-->
<!--            />-->
<!--            <el-button-->
<!--                type="warning"-->
<!--                size="small"-->
<!--                class="add-cart-btn"-->
<!--                @click.stop="handleAddCart(item.id)"-->
<!--            >-->
<!--              加入购物车-->
<!--            </el-button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </el-main>-->
<!--</template>-->


<!--<style scoped>-->
<!--/* 整区白底 */-->
<!--.search-results-page {-->
<!--  background: #fff;-->
<!--  min-height: 100vh;-->
<!--  padding: 20px 0;-->
<!--}-->

<!--/* 单条横向卡片 */-->
<!--.results-list {-->
<!--  max-width: 1000px;-->
<!--  margin: 0 auto;-->
<!--}-->
<!--.result-row {-->
<!--  display: flex;-->
<!--  align-items: flex-start;-->
<!--  padding: 15px 20px;-->
<!--  border-bottom: 1px solid #ebebeb;-->
<!--  cursor: pointer;-->
<!--  transition: background 0.2s;-->
<!--}-->
<!--.result-row:hover {-->
<!--  background: #fafafa;-->
<!--}-->

<!--.book-img {-->
<!--  width: 90px;-->
<!--  height: 120px;-->
<!--  object-fit: cover;-->
<!--  flex-shrink: 0;-->
<!--  margin-right: 18px;-->
<!--}-->

<!--/* 右侧信息 */-->
<!--.info-wrap {-->
<!--  flex: 1;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  justify-content: space-between;-->
<!--}-->
<!--.line1 {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--}-->
<!--.title {-->
<!--  font-size: 16px;-->
<!--  font-weight: 600;-->
<!--  color: #333;-->
<!--  margin: 0;-->
<!--}-->
<!--.price {-->
<!--  font-size: 18px;-->
<!--  color: #e4393c;-->
<!--  font-weight: bold;-->
<!--}-->
<!--.intro {-->
<!--  font-size: 13px;-->
<!--  color: #666;-->
<!--  margin: 8px 0 12px 0;-->
<!--  line-height: 1.5;-->
<!--}-->
<!--.line3 {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: space-between;-->
<!--}-->
<!--.add-cart-btn {-->
<!--  margin-left: auto;-->
<!--}-->
<!--</style>-->
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { searchProduct } from '../../api/product.ts';
import { addCart } from '../../api/cart.ts';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();

const keyword = ref(route.params.keyword as string || '');
const results = ref<any[]>([]);
const loading = ref(false);
const error = ref('');

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

watch(() => route.params.keyword, (newKeyword) => {
  keyword.value = newKeyword as string;
  fetchResults();
});

function goToProduct(id: number) {
  router.push(`/home/product/${id}`);
}

// ===== 新增：返回首页的函数 =====
function goHome() {
  router.push('/home/all-products');
}
</script>

<template>
  <el-main class="search-results-page">
    <!-- 加载和错误提示保持不变 -->
    <div v-if="loading" class="status-info">加载中...</div>
    <div v-if="error" class="status-info error">{{ error }}</div>

    <!-- ===== 修改开始：使用 el-empty 组件美化空状态 ===== -->
    <div v-if="!loading && !error && results.length === 0" class="empty-container">
      <el-empty>
        <!-- 自定义描述内容，提供更友好的提示 -->
        <template #description>
          <div class="empty-text">
            <p>抱歉，没有找到与 “<strong class="keyword">{{ keyword }}</strong>” 相关的商品</p>
            <p class="suggestion-title">您可以尝试：</p>
            <ul class="suggestion-list">
              <li>检查您的拼写是否正确</li>
              <li>更换或使用更简单的关键词</li>
            </ul>
          </div>
        </template>
        <!-- 提供一个返回首页的操作按钮 -->
        <el-button class="custom-home-btn" @click="goHome">
          返回首页
        </el-button>
      </el-empty>
    </div>
    <!-- ===== 修改结束 ===== -->

    <!-- 搜索结果列表 (使用 v-else 关联) -->
    <div v-else-if="!loading && results.length > 0" class="results-list">
      <div
          v-for="item in results"
          :key="item.id"
          class="result-row"
          @click="goToProduct(item.id)"
      >
        <!-- 左侧图书封面 -->
        <img
            :src="item.cover[0] || 'https://via.placeholder.com/90x120?text=无图'"
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
  padding: 20px; /* 增加内边距 */
}

/* 状态提示通用样式 (加载中/错误) */
.status-info {
  text-align: center;
  margin-top: 50px;
  color: #666;
  font-size: 16px;
}
.error {
  color: #f56c6c;
}

/* ===== 新增：空状态容器和内部样式 ===== */
.empty-container {
  padding-top: 80px; /* 距离顶部一些距离，使其更居中 */
}
.empty-text {
  color: #606266;
  text-align: center;
}
.empty-text .keyword {
  color: #e4393c; /* 搜索关键词高亮，使用价格的颜色 */
  font-weight: bold;
}
.empty-text p {
  margin: 0 0 10px;
}
.suggestion-title {
  font-weight: bold;
  color: #303133;
  margin-top: 20px !important;
}
.suggestion-list {
  list-style-type: none;
  padding: 0;
  color: #909399;
  font-size: 14px;
  line-height: 1.8;
}

/* ===== 结果列表样式 (基本保持不变) ===== */
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
/* ===== 新增：自定义返回首页按钮样式 ===== */
.custom-home-btn {
  /* 背景改为深炭灰色 (接近黑色，但比纯黑更有质感，契合您的顶部导航栏) */
  background-color: #2c2c2c;
  color: #ffffff;            /* 白字 */
  border: 1px solid #2c2c2c; /* 边框同色 */
  padding: 10px 25px;        /*稍微加宽一点 */
  font-weight: 500;
  transition: all 0.3s ease; /* 添加平滑过渡动画 */
}

/* 鼠标悬停时的效果 */
.custom-home-btn:hover {
  /* 背景稍微变亮一点点，体现交互感 */
  background-color: #454545;
  border-color: #454545;
  /* 文字变为金色，呼应由于您Logo "TRUTH BOOKSTORE" 的颜色 */
  color: #ffcc00;
}

/* 鼠标点击按下时的效果 */
.custom-home-btn:active {
  background-color: #1a1a1a;
  border-color: #1a1a1a;
}
</style>