
<template>
  <el-main class="posting-list bgimage">
    <!-- 创建帖子按钮 -->
    <el-button
        class="create-posting-button"
        @click="goToCreatePosting"
        style="margin-bottom: 20px;"
        v-if="role === 'customer'"
    >
      发帖子
    </el-button>
    <el-card
        v-for="posting in postings"
        :key="posting.id"
        class="posting-card"
        @click="goToPostingDetail(posting.id)"
    >
      <div class="posting-image">
        <img :src='posting.cover' alt="Posting Cover" />
      </div>
      <h3>{{ posting.title }}</h3>
      <p>点击查看详情</p>
    </el-card>
    <!--    <el-button type="primary" @click="createNewStore">创建商店</el-button>-->
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllPosting, Posting} from '../../api/posting.ts';
import {ElMessage} from "element-plus";

const postings = ref<Posting[]>([]);

const router = useRouter();

const role = sessionStorage.getItem("role");

async function get_getAllpostings() {
  try {
    const res = await getAllPosting();
    //console.log("get_getAllproducts",res);
    if (res.data && Array.isArray(res.data.data)) {
      postings.value = res.data.data;
      //console.log(res.data);
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取帖子列表失败:', error);
  }
}

// 导航到商店详情
function goToPostingDetail(postingId: number) {
  router.push({ name: 'PostingDetail', params: { id: postingId } });
}
function goToCreatePosting() {
  router.push({ path: '/home/create-posting' }); // 确保路由路径正确
}

// 在组件挂载时获取商店数据
onMounted(() => {
  get_getAllpostings();
});
</script>

<style scoped>

.posting-list {
  min-height: 800px;
  display: flex;
  flex-wrap: wrap; /* 允许子元素换行 */
  justify-content: center; /* 水平居中对齐 */
  gap: 20px; /* 设置子元素之间的间距 */
}

.posting-card {
  width: calc((100% / 4) - 20px); /* 每行三个卡片，减去间距 */
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box; /* 确保 padding 和 border 不影响宽度 */
  background-color: rgba(255, 255, 255, 0.6); /* 透明白色背景 */
  border: 1px solid rgba(255, 255, 255, 0.5); /* 半透明边框 */
}

.posting-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.posting-image img {
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
