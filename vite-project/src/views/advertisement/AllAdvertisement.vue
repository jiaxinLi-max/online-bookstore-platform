<!--<template>-->
<!--  <el-main class="advertisement-list bgimage">-->
<!--    <el-card-->
<!--        v-for="advertisement in advertisements"-->
<!--        :key="advertisement.id"-->
<!--        class="advertisement-card"-->
<!--        @click="goToProductDetail(advertisement.product_id)"-->
<!--    >-->
<!--      <div class="product-image">-->
<!--        <img :src='advertisement.image_url' alt="Product Cover" />-->
<!--      </div>-->
<!--      <h3>{{ advertisement.title }}</h3>-->
<!--      <p>点击查看详情</p>-->
<!--    </el-card>-->
<!--  </el-main>-->
<!--</template>-->
<template>
  <el-main class="advertisement-list bgimage">
    <!-- 创建广告按钮 -->
    <el-button
        v-if="role === 'MANAGER'"
        class="create-advertisement-button"
        @click="goToCreateAdvertisement"
        style="margin-bottom: 20px;"
    >
      创建广告
    </el-button>

    <el-card
        v-for="advertisement in advertisements"
        :key="advertisement.id"
        class="advertisement-card"
        @click="goToProductDetail(advertisement.product_id)"
    >
      <div class="product-image">
        <img :src="advertisement.image_url" alt="Product Cover" />
      </div>
      <h3>{{ advertisement.title }}</h3>
      <p>点击查看详情</p>
    </el-card>
  </el-main>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {getAllAdvertisement,Advertisement} from "../../api/advertisement.ts";

const advertisements = ref<Advertisement[]>([]);
const router = useRouter();
const role=sessionStorage.getItem("role");
// 获取所有商店数据
async function get_getAlladvertisements() {
  try {
    const res = await getAllAdvertisement();
    console.log("get_getAllAdvertisements",res);
    if (res.data && Array.isArray(res.data.data)) {
      advertisements.value = res.data.data;
      console.log(res.data);
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取广告列表失败:', error);
  }
}

// 导航到商品详情
function goToProductDetail(productId: number) {
  router.push({ path: `/home/product/${productId}` });
}
// 导航到创建广告页面
function goToCreateAdvertisement() {
  router.push({ path: '/home/create-advertisement' }); // 确保路由路径正确
}



// 在组件挂载时获取商店数据
onMounted(() => {
  get_getAlladvertisements()
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