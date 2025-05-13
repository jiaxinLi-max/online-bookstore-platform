<template>
  <el-main class="comment-list bgimage">
    <div class="back-button-wrapper">
      <el-button type="primary" icon="ArrowLeft" @click="goToProductDetail">返回商品详情</el-button>
    </div>
    <el-card
        v-for="comment in comments"
        :key="comment.id"
        class="advertisement-card"
        @click="goToCommentDetail(comment.id)"
        shadow="hover"
    >
      <!-- 用户头像与信息 -->
      <el-avatar :src="comment.avatar" class="comment-avatar" size="large" />
      <div class="comment-content">
        <h3 class="username">{{ comment.username }}</h3>
        <p class="comment-time">🕒 {{ formatTime(comment.time) }}</p>
        <div class="score-like-row">
          <span>评分：{{ comment.score }} ⭐</span>
          <span class="like-count">👍 点赞数：{{ comment.likes }}</span>
        </div>
        <p>{{ comment.content }}</p>
        <p class="link-text">点击查看详情</p>
      </div>
    </el-card>
  </el-main>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getTheAllComment, type Comment } from "../../api/comment.ts";
import { userInfo } from "../../api/user.ts";

// Vue Router
const router = useRouter();
const route = useRoute();
const productId = String(Number(route.params.productId)); // 确保是字符串

// 最终展示的评论列表
const comments = ref<(Comment & { username: string; avatar: string })[]>([]);
const time = ref('')

function formatTime(timeStr: string): string {
  const date = new Date(timeStr)

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')

  return `${year}年${month}月${day}日 ${hours}时${minutes}分`
}

// 获取所有评论并补充用户信息
async function get_getAllComments() {
  try {
    const res = await getTheAllComment(productId);
    if (res.data && Array.isArray(res.data.data)) {
      const rawComments: Comment[] = res.data.data;

      // 并行获取用户名与头像
      const enrichedComments = await Promise.all(
          rawComments.map(async (comment) => {
            let username = '未知用户';
            let avatar = '';

            try {
              const userRes = await userInfo(comment.userId);
              if (userRes.data.code === '200') {
                username = userRes.data.data.username;
                avatar = userRes.data.data.avatar;
              }
            } catch (err) {
              // 留空默认
            }

            return {
              ...comment,
              username,
              avatar,
            };
          })
      );

      comments.value = enrichedComments;
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取评论列表失败:', error);
  }
}

// 跳转到详情页
function goToCommentDetail(commentId: number) {
  router.push({ path: `/home/product/${productId}/comments/${commentId}` });
}
function goToProductDetail() {
  router.push({ path: `/home/product/${productId}` });
}

// 初始化
onMounted(() => {
  get_getAllComments();
});
</script>

<style scoped>
.comment-list {
  min-height: 800px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.advertisement-card {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  background-color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.5);
  width: 70%;
  max-width: 600px;
  padding: 10px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box;
}

.comment-avatar {
  margin-right: 10px;
}

.comment-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.username {
  margin: 0;
  font-size: 1.2em;
  color: #333;
}

.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center;
  padding: 40px 0;
}
.score-like-row {
  display: flex;
  justify-content: space-between;
  gap: 250px;
  font-weight: bold;
  color: #333;
}

.like-count {
  color: #f56c6c;
}
.back-button-wrapper {
  width: 70%;
  max-width: 600px;
  display: flex;
  justify-content: flex-start;
  margin-bottom: 10px;

}

</style>
