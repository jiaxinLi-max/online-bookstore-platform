
<template>
  <el-main class="product-list bgimage">
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

const products = ref<Product[]>([]);

const router = useRouter();

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
  background-color: rgba(255, 255, 255, 0.7);
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
  max-width: 960px;
  background-color: rgba(255, 255, 255, 0.5);
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
  color: white;
}

.hot-books {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}

.bgimage {
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
}


</style>
