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

        <div class="side-more" @click.stop="goToRankings">查看更多 &gt;</div>
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
.product-list { display: flex; flex-direction: column; align-items: center; gap: 24px; padding: 20px; min-height: 800px; }
.all-books { display: flex; flex-wrap: wrap; gap: 16px; justify-content: center; max-width: 750px; }
.product-card {width: 140px;height: auto;min-height: 200px;cursor: pointer; padding: 8px;  border-radius: 10px; text-align: center; display: flex; flex-direction: column; align-items: center; transition: transform 0.2s ease; }
.product-card:hover:hover { transform: scale(1.05); }
.product-image img { width: 100%; height: 150px; object-fit: cover; border-radius: 6px; }
.product-title{ font-size: 13px; font-weight: 500; margin-top: 6px; color: #333; line-height: 1.2; }
.ad-carousel { width: 100%; max-width: 960px; border-radius: 12px; overflow: hidden; }
.ad-carousel-item { height: 200px; cursor: pointer; padding: 0; display: flex; justify-content: center; align-items: center; }
/* 轮播项容器，左右结构卡片 */
.ad-item-container {
  display: flex;
  border-radius: 12px;
  background: #fff8dc;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  cursor: pointer;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  width: 600px;  /* 调整宽度适应你的需求 */
  height: 180px; /* 与轮播高度相符 */
}

/* 左侧图片 */
.ad-image-left {
  width: 100px;
  height: 100%;
  object-fit: cover;
  flex-shrink: 0;
}

/* 右侧文字 */
.ad-content-right {
  flex-grow: 1;
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
  color: #333;
}


/* 标题 */
.ad-title {
  font-weight: 700;
  font-size: 20px;
  margin-bottom: 12px;
}

/* 描述 */
.ad-desc {
  font-size: 14px;
  line-height: 1.4;
  opacity: 0.9;
  white-space: pre-wrap;
}

/* 当前激活卡片放大并加阴影，提升层级 */
.el-carousel__item.is-active .ad-item-container {
  transform: scale(1.15);
  box-shadow: 0 8px 24px rgba(0,0,0,0.25);
  z-index: 10;
}

/* 非激活卡片缩小且变淡 */
.el-carousel__item:not(.is-active) .ad-item-container {
  transform: scale(0.85);
  filter: brightness(0.85);
  transition: transform 0.4s ease, filter 0.4s ease;
  z-index: 1;
}

.bgimage {
  background: #ffffff;
  min-height: 100vh;
}
.product-price {
  margin-top: 4px;
  font-size: 14px;
  font-weight: bold;
  color: #d9534f; /* 温和一点的红色 */
}
.top-section {
  width: 100%;
  max-width: 1100px;
  display: grid;
  grid-template-columns: 160px 1fr 160px;
  gap: 20px;
  align-items: stretch;
}

.side-card {
  height: 200px;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: 0 0 8px rgba(0,0,0,0.15);
  transition: transform 0.2s;
}

.side-card:hover {
  transform: scale(1.05);
}

.side-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 14px;
}

.side-more {
  font-size: 14px;
  color: #323d4a;
}

/*column*/
.side-columns-list {
  margin: 8px 0;
  max-height: 300px; /* 限制最大高度，可选 */
  overflow-y: auto;  /* 超出可滚动 */
}

.side-column-item {
  font-size: 14px;
  padding: 6px 8px;
  cursor: pointer;
  color: #409eff;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.side-column-item:hover {
  background-color: rgba(64, 158, 255, 0.1);
}


/*rank*/
.side-rank-list {
  margin: 8px 0;
  max-height: 300px;
  overflow-y: auto;
}

.side-rank-item {
  font-size: 14px;
  padding: 6px 8px;
  cursor: pointer;
  color: #f56c6c; /* 红色调，突显排行 */
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.side-rank-item:hover {
  background-color: rgba(245, 108, 108, 0.1);
}


</style>