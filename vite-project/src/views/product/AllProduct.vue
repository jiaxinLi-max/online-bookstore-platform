<template>
  <el-main class="product-list bgimage">
    <div class="top-section">

      <!-- 左边：分类入口 -->
      <div class="side-card">
        <div class="side-title">📚 分类</div>

        <!-- 栏目列表，最多显示10个 -->
        <div class="side-columns-list">
          <div
              v-for="(column, index) in columns.slice(0, 10)"
              :key="column.id"
              class="side-column-item"
              @click.stop="goToColumnDetail(column.id)"
          >
            {{ column.theme }}
          </div>
        </div>

        <div class="side-more" @click.stop="goToColumns">查看更多 &gt;</div>
      </div>

      <!-- 中间：广告轮播（不变） -->
      <el-carousel
          height="200px"
          :interval="2000"
          trigger="click"
          type="card"
          arrow="never"
          class="ad-carousel"
      >
        <el-carousel-item
            v-for="ad in advertisements"
            :key="ad.id"
            @click="goToProductDetail(ad.productId)"
            class="ad-carousel-item"
        >
          <div class="ad-item-container">
            <img :src="ad.imgUrl" :alt="ad.title" class="ad-image-left" />
            <div class="ad-content-right">
              <div class="ad-title">{{ ad.title }}</div>
              <div class="ad-desc">{{ ad.content || '' }}</div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <!-- 右边：排行榜入口 -->
      <div class="side-card">
        <div class="side-title">🏆 排行榜</div>

        <div class="side-rank-list">
          <div
              v-for="(product, index) in products.slice(0, 10)"
              :key="product.id"
              class="side-rank-item"
              @click.stop="goToProductDetail(product.id)"
          >
            {{ index + 1 }}. {{ product.title }}
          </div>
        </div>

<!--        <div class="side-more" @click.stop="goToRankings">查看更多 &gt;</div>-->
      </div>


    </div>

    <div class="all-books">
      <el-card
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToProductDetail(product.id)"
          shadow="hover"
      >
        <div class="product-image">
          <img :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : ''" alt="Product Cover" />
        </div>
        <h3 class="product-title">{{ product.title }}</h3>
        <!-- ✨ 新增：显示价格 -->
        <div class="product-price">
          ￥{{ product.price }}
        </div>
      </el-card>
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
/* ======== 页面整体 ======== */
.bgimage {
  background: #f7f8fa;
  min-height: 100vh;
}

.product-list {
  font-size: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 32px;
  padding: 20px;
}


/* =================== 顶部三栏布局 =================== */
.top-section {
  width: 100%;
  max-width: 1300px;
  display: grid;
  grid-template-columns: 240px 1fr 240px;
  gap: 32px;
  align-items: stretch;
  margin-top: 12px;
}


/* =================== 左右侧卡片（分类 & 排行） =================== */
.side-card {
  height: 420px;
  border-radius: 18px;
  padding: 26px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.side-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}

.side-title {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 18px;
}

/* 通用列表样式 */
.side-columns-list,
.side-rank-list {
  flex: 1;
  overflow-y: auto;
  padding-right: 4px;
}

.side-column-item,
.side-rank-item {
  font-size: 16px;
  padding: 10px 12px;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.2s ease;
  margin-bottom: 6px;
}

.side-column-item {
  color: #409eff;
}

.side-column-item:hover {
  background-color: rgba(64, 158, 255, 0.1);
}

.side-rank-item {
  color: #f56c6c;
}

.side-rank-item:hover {
  background-color: rgba(245, 108, 108, 0.1);
}

.side-more {
  font-size: 16px;
  margin-top: 12px;
  color: #666;
  text-align: right;
}


/* =================== 中间轮播图 =================== */
.ad-carousel {
  width: 100%;
  max-width: 1000px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 14px rgba(0,0,0,0.12);
}

.ad-carousel-item {
  height: 260px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: transparent;
}


/* =================== 轮播内卡片（左右结构） =================== */
.ad-item-container {
  display: flex;
  width: 760px;
  height: 240px;
  border-radius: 14px;
  background: #fff;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0,0,0,0.10);
  transition: transform 0.35s ease, box-shadow 0.35s ease, filter 0.4s ease;
}

/* 图片区 */
.ad-image-left {
  width: 160px;
  height: 100%;
  object-fit: cover;
}

/* 文本区 */
.ad-content-right {
  flex: 1;
  padding: 28px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #333;
}

.ad-title {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 14px;
}

.ad-desc {
  font-size: 16px;
  line-height: 1.45;
  opacity: 0.9;
}

.el-carousel__item {
  height: 400px !important; /* 和你的 carousel 高度一致 */
}


/* 轮播激活状态 */
.el-carousel__item.is-active .ad-item-container {
  transform: scale(1.12);
  box-shadow: 0 10px 30px rgba(0,0,0,0.25);
  z-index: 10;
}

/* 非激活状态变淡 */
.el-carousel__item:not(.is-active) .ad-item-container {
  transform: scale(0.85);
  filter: brightness(0.85);
}

/* =================== 商品列表 =================== */
.all-books {
  display: flex;
  flex-wrap: wrap;
  gap: 28px;
  justify-content: center;
  max-width: 1150px;
}

.product-card {
  width: 190px;
  min-height: 260px;
  padding: 14px;
  border-radius: 14px;
  background: #fff;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  transition: transform 0.25s ease, box-shadow 0.2s ease;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}

.product-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 4px 18px rgba(0,0,0,0.15);
}

.product-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 10px;
}

.product-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-top: 10px;
  line-height: 1.3;
}

.product-price {
  margin-top: 6px;
  font-size: 17px;
  font-weight: 700;
  color: #e14d4d;
}

</style>