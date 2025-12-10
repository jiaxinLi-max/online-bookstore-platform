<template>
  <el-main class="product-list bgimage">
    <div class="top-section">

      <div class="side-card category-card">
        <div class="side-header">
          <span class="header-icon">📚</span>
          <span class="header-text">图书分类</span>
        </div>

        <div class="side-columns-list">
          <div
              v-for="(column, index) in columns.slice(0, 10)"
              :key="column.id"
              class="side-column-item"
              @click.stop="goToColumnDetail(column.id)"
          >
            <span class="col-dot"></span>
            {{ column.theme }}
          </div>
        </div>

        <div class="side-footer" @click.stop="goToColumns">
          <span>全部分类</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>

      <div class="carousel-container">
        <el-carousel
            height="380px"
            :interval="4000"
            trigger="click"
            type="card"
            arrow="hover"
            class="ad-carousel"
        >
          <el-carousel-item
              v-for="ad in advertisements"
              :key="ad.id"
              class="ad-carousel-item"
          >
            <div class="ad-card-inner" @click="goToProductDetail(ad.productId)">
              <div class="ad-info">
                <h2 class="ad-title">{{ ad.title }}</h2>
                <p class="ad-desc">{{ ad.content || '探索更多精彩内容，尽在书海...' }}</p>
                <el-button type="primary" round class="ad-btn">立即查看</el-button>
              </div>
              <div class="ad-image-wrapper">
                <img :src="ad.imgUrl" :alt="ad.title" class="ad-img" />
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="side-card rank-card">
        <div class="side-header rank-header">
          <span class="header-icon">🏆</span>
          <span class="header-text">热销榜单</span>
        </div>

        <div class="side-rank-list">
          <div
              v-for="(product, index) in products.slice(0, 10)"
              :key="product.id"
              class="side-rank-item"
              @click.stop="goToProductDetail(product.id)"
          >
            <span class="rank-num" :class="'rank-' + (index + 1)">{{ index + 1 }}</span>
            <span class="rank-name">{{ product.title }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section-title-bar">
      <h2>全部书籍</h2>
      <span class="subtitle">海量好书任你挑选</span>
    </div>

    <div class="all-books-grid">
      <div
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToProductDetail(product.id)"
      >
        <div class="card-image-box">
          <img
              :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : 'https://via.placeholder.com/150'"
              loading="lazy"
              alt="Product Cover"
          />
          <div class="hover-overlay">
            <span>点击详情</span>
          </div>
        </div>

        <div class="card-info">
          <h3 class="product-title" :title="product.title">{{ product.title }}</h3>
          <div class="product-meta">
            <div class="product-price">
              <span class="currency">￥</span>
              <span class="amount">{{ product.price }}</span>
            </div>
            <div class="cart-icon-btn">GO</div>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllProduct, Product, getRankProduct } from '../../api/product.ts';
import { getAllAdvertisement, Advertisement } from '../../api/advertisement.ts';
import { getAllColumns } from '../../api/columns';

const products = ref<Product[]>([]);
async function get_getRankproducts() {
  try {
    const res = await getRankProduct();
    console.log("get_getRankproducts",res);
    if (res.data && Array.isArray(res.data.data)) {
      products.value = res.data.data;
      console.log(res.data);
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取商店列表失败:', error);
  }
}

function goToProductDetail(productId: number) {
  router.push({ path: `/home/product/${productId}` });
}




interface Column {
  id: number;
  theme: string;
  description: string;
  covers: string[];
}

const columns = ref<Column[]>([]);

const fetchAllColumns = async () => {
  try {
    const res = await getAllColumns();
    if (res.data.code === '200') {
      columns.value = res.data.data;
    }
  } catch (error) {
    console.error("获取所有栏目失败:", error);
  }
};

const router = useRouter();
const advertisements = ref<Advertisement[]>([]);

async function fetchAdvertisements() {
  try {
    const res = await getAllAdvertisement();
    if (res.data && Array.isArray(res.data.data)) {
      advertisements.value = res.data.data;
    }
  } catch (error) { console.error('获取广告失败:', error); }
}

const hotProducts = ref<Product[]>([]);

async function fetchHotProducts() {
  try {
    const res = await getRankProduct();
    const list = res.data.data || [];
    hotProducts.value = list.slice(0, 4);
  } catch (error) { console.error("获取热门书籍失败:", error); }
}

async function get_getAllproducts() {
  try {
    const res = await getAllProduct();
    if (res.data && Array.isArray(res.data.data)) {
      products.value = res.data.data;
    }
  } catch (error) { console.error('获取商品列表失败:', error); }
}

function goToColumns() {
  router.push('/home/all-columns');
}

function goToRankings() {
  router.push('/home/rankings');
}

const goToColumnDetail = (columnId: number) => {
  router.push({ path: `/home/columns/${columnId}` });
};


onMounted(() => {
  get_getAllproducts();
  fetchHotProducts();
  fetchAdvertisements();
  fetchAllColumns();
  get_getRankproducts();
});
</script>

<style scoped>
/* ======== 基础设置 ======== */
.bgimage {
  background: #f0f2f5; /*稍微深一点的灰白，突出白色卡片*/
  min-height: 100vh;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  padding: 0; /* 让 main 撑满 */
  padding-bottom: 60px;
}

/* 顶部布局容器 */
.top-section {
  width: 100%;
  max-width: 1440px; /* 增加宽度 */
  margin: 20px auto;
  padding: 0 20px;
  display: grid;
  /* 左侧栏 | 中间轮播 | 右侧栏 - 比例调整 */
  grid-template-columns: 260px 1fr 260px;
  gap: 24px;
  align-items: stretch;
}

/* ======== 侧边栏通用卡片 ======== */
.side-card {
  background: #fff;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  height: 420px; /* 和轮播高度区配（轮播380+上下边距/阴影） */
  border: 1px solid #fff;
}

.side-header {
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid #f2f2f2;
}

/* 分类特定的头部颜色 */
.category-card .side-header {
  background: linear-gradient(135deg, #e3f2fd 0%, #ffffff 100%);
}
.category-card .header-text { color: #1976d2; font-weight: 700; font-size: 18px; }

/* 排行榜特定的头部颜色 */
.rank-card .side-header {
  background: linear-gradient(135deg, #fff3e0 0%, #ffffff 100%);
}
.rank-card .header-text { color: #ed6c02; font-weight: 700; font-size: 18px; }

.header-icon { font-size: 20px; }

/* 列表区域 */
.side-columns-list, .side-rank-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px 0;
}

/* 滚动条美化 */
.side-columns-list::-webkit-scrollbar, .side-rank-list::-webkit-scrollbar {
  width: 4px;
}
.side-columns-list::-webkit-scrollbar-thumb { background: #e0e0e0; border-radius: 4px;}

/* ---- 分类列表项 ---- */
.side-column-item {
  padding: 10px 20px;
  font-size: 15px;
  color: #555;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.2s;
}
.side-column-item:hover {
  background-color: #f0f9ff;
  color: #1976d2;
  padding-left: 24px; /* 悬停右移效果 */
}
.col-dot {
  width: 6px;
  height: 6px;
  background: #ccc;
  border-radius: 50%;
  margin-right: 10px;
  transition: background 0.2s;
}
.side-column-item:hover .col-dot { background: #1976d2; }

/* ---- 排行榜列表项 ---- */
.side-rank-item {
  padding: 9px 20px;
  font-size: 14px;
  color: #444;
  cursor: pointer;
  display: flex;
  align-items: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.side-rank-item:hover { background-color: #fff8f0; }

.rank-num {
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: #999;
  background: #f5f5f5;
  margin-right: 10px;
  flex-shrink: 0;
}
/* 前三名高亮 */
.rank-1 { background: #ffc107; color: #fff; text-shadow: 0 1px 1px rgba(0,0,0,0.2); } /* 金 */
.rank-2 { background: #cfd8dc; color: #fff; text-shadow: 0 1px 1px rgba(0,0,0,0.2); } /* 银 */
.rank-3 { background: #d7ccc8; color: #fff; text-shadow: 0 1px 1px rgba(0,0,0,0.2); } /* 铜 */

.rank-name {
  overflow: hidden;
  text-overflow: ellipsis;
}

.side-footer {
  padding: 12px;
  text-align: center;
  font-size: 13px;
  color: #888;
  cursor: pointer;
  border-top: 1px solid #f5f5f5;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
}
.side-footer:hover { color: #1976d2; }


/* ======== 广告轮播 ======== */
.carousel-container {
  overflow: visible; /* 允许阴影溢出 */
}

/* 覆盖 Element Plus 样式 */
:deep(.el-carousel__mask) {
  background-color: #fff;
  opacity: 0.4;
}

.ad-carousel-item {
  border-radius: 12px;
  /* 解决 transform 导致的圆角失效 */
  overflow: hidden;
}

.ad-card-inner {
  width: 100%;
  height: 100%;
  display: flex;
  background: #fff;
  /* 增加一个微妙的渐变背景 */
  background: linear-gradient(120deg, #ffffff 30%, #f0f7ff 100%);
  cursor: pointer;
  position: relative;
}

.ad-info {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  z-index: 2;
}

.ad-title {
  font-size: 28px;
  font-weight: 800;
  color: #333;
  margin-bottom: 16px;
  line-height: 1.2;
}

.ad-desc {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.ad-btn {
  padding: 10px 28px;
  font-weight: 600;
  background: linear-gradient(90deg, #409eff, #337ecc);
  border: none;
  box-shadow: 0 4px 12px rgba(64,158,255,0.4);
}

.ad-image-wrapper {
  flex: 0 0 55%; /* 图片占宽度的 55% */
  height: 100%;
  overflow: hidden;
  position: relative;
}

.ad-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 斜切效果 */
  mask-image: linear-gradient(to right, transparent 0%, black 20%);
  -webkit-mask-image: linear-gradient(to right, transparent 0%, black 15%);
}


/* ======== 下方书籍网格 (重点修改) ======== */
.section-title-bar {
  max-width: 1440px;
  margin: 40px auto 20px;
  padding: 0 20px;
  display: flex;
  align-items: baseline;
  gap: 12px;
}
.section-title-bar h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}
.section-title-bar .subtitle {
  font-size: 14px;
  color: #999;
}

.all-books-grid {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  /* 关键：使用 Grid 布局，最小宽度 180px，自动填满 */
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 32px;
}

.product-card {
  background: #fff;
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border: 1px solid transparent;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-8px); /* 上浮 */
  box-shadow: 0 12px 24px rgba(0,0,0,0.1);
  border-color: #e0e0e0;
}

.card-image-box {
  width: 100%;
  aspect-ratio: 3 / 4; /* 固定图片比例 */
  background: #f9f9f9;
  position: relative;
  overflow: hidden;
}

.card-image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

/* 图片悬停放大 */
.product-card:hover .card-image-box img {
  transform: scale(1.08);
}

.card-info {
  padding: 12px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
  /* 限制两行，多出显示省略号 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto; /* 推到底部 */
}

.product-price {
  color: #f56c6c;
  font-weight: 700;
}
.currency { font-size: 12px; }
.amount { font-size: 18px; }

.cart-icon-btn {
  font-size: 12px;
  background: #f2f3f5;
  color: #666;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s;
}
.product-card:hover .cart-icon-btn {
  background: #ecf5ff;
  color: #409eff;
}
</style>