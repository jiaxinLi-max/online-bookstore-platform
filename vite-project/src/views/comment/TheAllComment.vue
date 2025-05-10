<template>
  <el-main class="comment-list bgimage">
    <el-card
        v-for="comment in comments"
        :key="comment.id"
        class="advertisement-card"
        @click="goToCommentDetail(comment.id)"
        shadow="hover"
    >
      <h3>{{ comment.content }}</h3>
      <p class="advertisement-content">评分：{{ comment.score }} ⭐</p>
      <p class="link-text">点击查看详情</p>
    </el-card>
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import {useRoute, useRouter} from 'vue-router';
import { getTheAllComment,Comment } from "../../api/comment.ts";
import {ElMessage, type UploadFile} from "element-plus";
import {getImage} from "../../api/tools.ts";
import {Plus} from "@element-plus/icons-vue";


const comments = ref<Comment[]>([]);
const router = useRouter();
const route = useRoute();
const productId = Number(route.params.productId).toString();


// 获取所有商店数据
async function get_getAllComments() {
  try {

    const res = await getTheAllComment(productId);
    console.log("get_getAllComments",res);
    if (res.data && Array.isArray(res.data.data)) {
      comments.value = res.data.data;
      console.log("res.data.data",res.data.data);
      console.log(comments.value[0]);
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取评论列表失败:', error);
  }
}

// 导航到商品详情
function goToCommentDetail(commentId: number) {
  router.push({ path: `/home/product/${productId}/comments/${commentId}` });
}


// 在组件挂载时获取商店数据
onMounted(() => {
  get_getAllComments()

});
</script>

<style scoped>

.comment-list {
  min-height: 800px;
  display: flex;
  flex-wrap: wrap; /* 允许子元素换行 */
  justify-content: center; /* 水平居中对齐 */
  gap: 20px; /* 设置子元素之间的间距 */
}

.comment-card {
  background-color: rgba(255, 255, 255, 0.6); /* 透明白色背景 */
  border: 1px solid rgba(255, 255, 255, 0.5); /* 半透明边框 */
  width: calc((100% / 4) - 20px); /* 每行三个卡片，减去间距 */
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box; /* 确保 padding 和 border 不影响宽度 */
}

.comment-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}


.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/kenan.png");
}
</style>