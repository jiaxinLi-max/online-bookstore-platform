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
        v-for="(product, index) in products"
        :key="product.id"
        class="product-card"
        @click="goToProductDetail(product.id)"
    >
      <div class="rank-badge" v-if="index < 3">
        第{{ index + 1 }}名
      </div>
      <div class="product-image">
        <img :src="product.cover" alt="Product Cover" />
      </div>
      <h3>{{ product.title }}</h3>
      <p>⭐ 评分：{{ product.rate ?.toFixed(1) ?? '暂无评分' }}</p>
      <p>点击查看详情</p>
    </el-card>
  </el-main>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getRankProduct, Product } from '../../api/product.ts';
import { ElMessage } from 'element-plus'

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
  ElMessage({
    message: '当前页面为按评分排行展示商品',
    type: 'info',
    duration: 3000
  });
});
</script>

<style scoped>

.product-list {
  display: flex;
  flex-direction: column; /* 垂直方向布局，每一项一行 */
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.product-card {
  width: 40%; /* 一行一个卡片 */
  padding: 20px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box;
  background-color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.5);
  position: relative; /* 让角标定位相对于卡片 */
}

.product-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.product-image img {
  width: 100%;
  height: auto;
  border-radius: 8px;
  max-height: 150px;
  object-fit: cover;
}

.rank-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: gold;
  color: black;
  font-weight: bold;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
}

.bgimage {
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
}

</style>

