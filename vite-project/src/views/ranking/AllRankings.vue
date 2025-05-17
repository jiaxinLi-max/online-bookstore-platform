<!--<template>-->
<!--  <div class="p-4">-->
<!--    <h2 class="text-xl font-bold mb-2">🕒 最新帖子</h2>-->
<!--    <button class="btn btn-primary mb-2" @click="fetchPostsByTime">-->
<!--      获取按时间排序的帖子-->
<!--    </button>-->
<!--    <ul v-if="postsByTime.length > 0">-->
<!--      <li v-for="p in postsByTime" :key="p.id" class="mb-1">-->
<!--        <b>{{ p.title }}</b>: {{ p.content }} <br />-->
<!--        🗓 {{ formatTime(p.time) }}-->
<!--      </li>-->
<!--    </ul>-->
<!--    <p v-else class="text-gray-500">暂无帖子数据</p>-->

<!--    <h2 class="text-xl font-bold mt-6 mb-2">👍 商品评价（按点赞）</h2>-->
<!--    <div class="flex items-center gap-2 mb-2">-->
<!--      <input v-model.number="productId" type="number" placeholder="输入商品ID" class="input input-bordered w-48" />-->
<!--      <button class="btn btn-success" @click="fetchCommentsByLike">获取</button>-->
<!--    </div>-->
<!--    <ul v-if="commentsByLike.length > 0">-->
<!--      <li v-for="c in commentsByLike" :key="c.id" class="mb-1">-->
<!--        ⭐ 评分: {{ c.score }} ｜ 👍 点赞: {{ c.likes }}<br />-->
<!--        💬 内容: {{ c.content }}<br />-->
<!--        🕓 时间: {{ formatTime(c.time) }}-->
<!--      </li>-->
<!--    </ul>-->
<!--    <p v-else class="text-gray-500">暂无评论数据</p>-->

<!--    <p v-if="errorMsg" class="text-red-500 mt-4">❌ 错误：{{ errorMsg }}</p>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { ref } from 'vue'-->
<!--import { sortByTime, sortByLike } from '../../api/tools.ts'-->

<!--const postsByTime = ref<any[]>([])-->
<!--const commentsByLike = ref<any[]>([])-->
<!--const productId = ref<number>(3) // 默认值为 3-->
<!--const errorMsg = ref<string>('')-->

<!--function formatTime(raw: string): string {-->
<!--  const date = new Date(raw)-->
<!--  return date.toLocaleString()-->
<!--}-->

<!--const fetchPostsByTime = async () => {-->
<!--  errorMsg.value = ''-->
<!--  try {-->
<!--    const res = await sortByTime('post')-->
<!--    postsByTime.value = res.data.data-->
<!--  } catch (err: any) {-->
<!--    errorMsg.value = '获取帖子失败'-->
<!--    console.error(err)-->
<!--  }-->
<!--}-->

<!--const fetchCommentsByLike = async () => {-->
<!--  errorMsg.value = ''-->
<!--  try {-->
<!--    const res = await sortByLike('comment', productId.value)-->
<!--    commentsByLike.value = res.data.data-->
<!--  } catch (err: any) {-->
<!--    errorMsg.value = '获取评论失败'-->
<!--    console.error(err)-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--.btn {-->
<!--  @apply bg-blue-500 hover:bg-blue-600 text-white font-semibold py-1 px-3 rounded;-->
<!--}-->
<!--.input {-->
<!--  @apply border rounded px-2 py-1;-->
<!--}-->
<!--</style>-->

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
import { getRankProduct, Product } from '../../api/product.ts';

const products = ref<Product[]>([]);

const router = useRouter();

// 获取所有商店数据
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



// 在组件挂载时获取商店数据
onMounted(() => {
  get_getRankproducts();
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
  background-color: rgba(255, 255, 255, 0.6); /* 透明白色背景 */
  border: 1px solid rgba(255, 255, 255, 0.5); /* 半透明边框 */
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
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
}
</style>

