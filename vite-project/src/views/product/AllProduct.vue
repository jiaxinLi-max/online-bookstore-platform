
<template>
  <el-main class="product-list bgimage">
    <el-card
        v-for="product in products"
        :key="product.id"
        class="product-card"
        @click="goToProductDetail(product.id)"
    >
      <div class="product-image">
        <img :src='product.cover' alt="Product Cover" />
      </div>
      <h3>{{ product.title }}</h3>
      <p>点击查看详情</p>
    </el-card>
    <!--    <el-button type="primary" @click="createNewStore">创建商店</el-button>-->
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllProduct, createProduct, Product } from '../../api/product.ts';

//const products = ref<Product[]>([]);
// 定义静态假数据
const products = ref([
  {
    id: 1,
    title: '商品1',
    cover: 'https://bpic.588ku.com/back_origin_min_pic/19/10/22/7d5760a4e3926576c237d950d5964db1.jpg',
  },
  {
    id: 2,
    title: '商品2',
    cover: 'https://via.placeholder.com/300x200?text=Product+2',
  },
  {
    id: 3,
    title: '商品3',
    cover: 'https://via.placeholder.com/300x200?text=Product+3',
  },
  {
    id: 4,
    title: '商品4',
    cover: 'https://via.placeholder.com/300x200?text=Product+4',
  },
  {
    id: 5,
    title: '商品5',
    cover: 'https://via.placeholder.com/300x200?text=Product+5',
  },
]);
const router = useRouter();

// 获取所有商店数据
async function get_getAllproducts() {
  try {
    const res = await getAllProduct();
    if (res.data && Array.isArray(res.data.result)) {
      products.value = res.data.result;
      console.log(res.data.result);
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
});
</script>

<style scoped>

.product-list {
  min-height: 800px;
  display: flex;
  flex-wrap: wrap; /* 允许子元素换行 */
  justify-content: center; /* 水平居中对齐 */
  gap: 20px; /* 设置子元素之间的间距 */
}

.product-card {
  width: calc((100% / 4) - 20px); /* 每行三个卡片，减去间距 */
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box; /* 确保 padding 和 border 不影响宽度 */
}

.product-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.product-image img {
  width: 100%; /* 确保图片宽度充满容器 */
  height: auto; /* 高度自适应 */
  border-radius: 8px; /* 圆角效果 */
  max-width: 200px; /* 最大宽度限制为 200px */
  max-height: 150px; /* 最大高度限制为 150px */
  object-fit: cover; /* 裁剪图片以适应容器 */
}
.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/kenan.png");
}
</style>
