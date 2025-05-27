
<template>
  <el-main class="product-list bgimage">
    <!-- 广告轮播区域 -->
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

    <!-- 热门图书区域 -->
    <div class="hot-section">
      <h2 class="hot-title">🔥 热门书籍</h2>
      <div class="hot-books">
        <el-card
            v-for="product in hotProducts"
            :key="'hot-' + product.id"
            class="hot-card"
            @click="goToProductDetail(product.id)"
        >
          <img :src="product.cover" alt="Hot Book Cover" class="hot-image" />
          <div class="hot-title-text">{{ product.title }}</div>
        </el-card>
      </div>
    </div>

    <!-- 所有书籍区域 -->
    <div class="all-books">
      <el-card
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToProductDetail(product.id)"
          shadow="hover"
      >
        <div class="product-image">
          <img :src="product.cover" alt="Product Cover" />
        </div>
        <h3 class="product-title">{{ product.title }}</h3>
      </el-card>
    </div>
  </el-main>
</template>



<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllProduct, Product ,getRankProduct} from '../../api/product.ts';
import { getAllAdvertisement, Advertisement } from '../../api/advertisement.ts';

const products = ref<Product[]>([]);

const router = useRouter();

const advertisements = ref<Advertisement[]>([]);

async function fetchAdvertisements() {
  try {
    const res = await getAllAdvertisement();
    if (res.data && Array.isArray(res.data.data)) {
      advertisements.value = res.data.data;
      console.log('广告数据:', advertisements.value);
    } else {
      console.error('广告数据格式不正确');
    }
  } catch (error) {
    console.error('获取广告失败:', error);
  }
}

const hotProducts = ref<Product[]>([]);

async function fetchHotProducts() {
  try {
    const res = await getRankProduct();
    const list = res.data.data || [];
    hotProducts.value = list.slice(0, 4); // 只取前四个
  } catch (error) {
    console.error("获取热门书籍失败:", error);
  }
}
// 获取所有商店数据
async function get_getAllproducts() {
  try {
    const res = await getAllProduct();
    console.log("get_getAllproducts",res);
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

// 导航到商店详情
function goToProductDetail(productId: number) {
  router.push({ path: `/home/product/${productId}` });
}



// 在组件挂载时获取商店数据
onMounted(() => {
  get_getAllproducts();
  fetchHotProducts();
  fetchAdvertisements();
});
</script>

<style scoped>
.product-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding: 20px;
  min-height: 800px;
}

/* 所有书籍卡片区域 */
.all-books {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
  max-width: 750px;
}

/* 单个卡片（热门 + 所有通用） */
.product-card,
.hot-card {
  width: 140px;
  height: 200px;
  cursor: pointer;
  padding: 8px;
  background-color: rgba(255, 248, 220, 0.8);
  border-radius: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: transform 0.2s ease;
}

.product-card:hover,
.hot-card:hover {
  transform: scale(1.05);
}

.product-image img,
.hot-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 6px;
}

.product-title,
.hot-title-text {
  font-size: 13px;
  font-weight: 500;
  margin-top: 6px;
  color: #333;
  line-height: 1.2;
}

/* 热门区域整体背景与标题 */
.hot-section {
  width: 100%;
  max-width: 900px;
  max-height: 500px;
  background-color: rgba(200, 200, 200, 0.7);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.hot-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 16px;
  color: black;
}

.hot-books {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}
.ad-carousel {
  width: 100%;
  max-width: 960px;
  border-radius: 12px;
  overflow: hidden;
}

.ad-carousel-item {
  height: 200px;
  cursor: pointer;
  padding: 0;
  display: flex; /* 建议加上防止内容坍塌 */
  justify-content: center;
  align-items: center;
}

.ad-item-container {
  display: flex;
  height: 100%;
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 10px rgba(0,0,0,0.2);
}

.ad-image-left {
  width: 300px;
  height: 100%;
  object-fit: cover;
  flex-shrink: 0;
}

.ad-content-right {
  flex-grow: 1;
  background-color: rgba(255, 248, 220, 0.8);
  color: black;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.ad-title {
  font-weight: 700;
  font-size: 20px;
  margin-bottom: 12px;
}

.ad-desc {
  font-size: 14px;
  line-height: 1.4;
  opacity: 0.9;
  white-space: pre-wrap;
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
