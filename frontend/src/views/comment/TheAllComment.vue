<template>
  <el-main class="comment-list bgimage">
    <div class="sort-select-wrapper">
      <el-select v-model="sortOption" placeholder="选择排序方式" @change="fetchCommentsBySort" class="sort-select">
        <el-option label="按时间排序" value="time" />
        <el-option label="按点赞排序" value="like" />
      </el-select>
    </div>

    <el-card
        v-for="comment in comments"
        :key="comment.id"
        class="advertisement-card"
        @click="openReplyDialog(comment)"
        shadow="hover"
    >
      <el-avatar :src="comment.avatar" class="comment-avatar" size="large" />
      <div class="comment-content">
        <h3 class="username">{{ comment.username }}</h3>
        <p class="comment-time">🕒 {{ formatTime(comment.time) }}</p>
        <div class="score-like-row">
          <span>评分：{{ comment.score }} ⭐</span>
          <span class="like-count">👍 点赞数：{{ comment.likes }}</span>
        </div>
        <p>{{ comment.content }}</p>
        <p class="link-text">点击查看回复</p>
      </div>
    </el-card>

    <el-dialog
        v-model="replyDialogVisible"
        :title="`查看“${currentParentComment?.username}”的评论回复`"
        width="50%"
        top="5vh"
    >
      <div class="parent-comment-in-dialog">
        <el-card>
          <p>{{ currentParentComment?.content }}</p>
        </el-card>
      </div>
      <el-divider>回复列表</el-divider>
      <div v-loading="subCommentsLoading" class="sub-comments-container">
        <div v-if="subComments.length > 0">
          <div v-for="reply in subComments" :key="reply.id" class="sub-comment-item">
            <el-avatar :src="reply.avatar" size="default" />
            <div class="sub-comment-content">
              <span class="sub-comment-username">{{ reply.username }}</span>
              <p class="sub-comment-text">{{ reply.content }}</p>
              <span class="sub-comment-time">{{ formatTime(reply.time) }}</span>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无回复"></el-empty>
      </div>
    </el-dialog>

  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getTheAllComment, getSubComments, type Comment } from "../../api/comment.ts";
import { getUserInfo } from "../../api/user.ts";
import { sortByTime, sortByLike } from '../../api/tools.ts';

const router = useRouter();
const route = useRoute();
const productId = String(Number(route.params.productId));

const comments = ref<(Comment & { username: string; avatar: string })[]>([]);
const sortOption = ref<'time' | 'like'>('time');

function formatTime(timeStr: string): string {
  const date = new Date(timeStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}年${month}月${day}日 ${hours}时${minutes}分`;
}

async function fetchCommentsBySort() {
  try {
    let res;
    if (sortOption.value === 'time') {
      res = await sortByTime('comment', Number(productId));
    } else {
      res = await sortByLike('comment', Number(productId));
    }

    if (res.data.code == 200 && Array.isArray(res.data.data)) {
      const rawComments: Comment[] = res.data.data;
      const enriched = await Promise.all(
          rawComments.map(async (comment) => {
            let username = '未知用户';
            let avatar = '';
            try {
              const userRes = await getUserInfo(comment.userId);
              if (userRes.data.code == 200) {
                username = userRes.data.data.username;
                avatar = userRes.data.data.avatar;
              }
            } catch (e) {}
            return { ...comment, username, avatar };
          })
      );
      comments.value = enriched;
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (err) {
    console.error('获取评论失败:', err);
  }
}

function goToCommentDetail(commentId: number) {
  router.push({ path: `/home/product/${productId}/comments/${commentId}` });
}

onMounted(() => {
  fetchCommentsBySort();
});

// --- 【以下为新增的逻辑】 ---
const replyDialogVisible = ref(false);
const subCommentsLoading = ref(false);
const currentParentComment = ref<(Comment & { username: string; avatar: string }) | null>(null);
const subComments = ref<(Comment & { username: string; avatar: string })[]>([]);

const openReplyDialog = async (parentComment: Comment & { username: string; avatar: string }) => {
  replyDialogVisible.value = true;
  subCommentsLoading.value = true;
  currentParentComment.value = parentComment;
  subComments.value = [];

  try {
    const res = await getSubComments(parentComment.id); // 调用新的API
    if (res.data.code == 200 && Array.isArray(res.data.data)) {
      const rawSubComments: Comment[] = res.data.data;
      const enrichedSubComments = await Promise.all(
          rawSubComments.map(async (reply) => {
            let username = '未知用户';
            let avatar = '';
            try {
              const userRes = await getUserInfo(reply.userId);
              if (userRes.data.code == 200) {
                username = userRes.data.data.username;
                avatar = userRes.data.data.avatar;
              }
            } catch(e) {}
            return { ...reply, username, avatar };
          })
      );
      subComments.value = enrichedSubComments;
    }
  } catch(error) {
    console.error("获取子评论失败:", error);
  } finally {
    subCommentsLoading.value = false;
  }
}
</script>

<style scoped>
.comment-list { min-height: 800px; display: flex; flex-direction: column; align-items: center; gap: 20px; }
.advertisement-card { display: flex; flex-direction: row; align-items: flex-start; background-color: rgba(255, 255, 255, 0.6); border: 1px solid rgba(255, 255, 255, 0.5); width: 70%; max-width: 600px; padding: 10px; cursor: pointer; transition: box-shadow 0.3s; box-sizing: border-box; }
.comment-avatar { margin-right: 10px; }
.comment-content { flex: 1; display: flex; flex-direction: column; gap: 10px; }
.username { margin: 0; font-size: 1.2em; color: #333; }
.bgimage { background-color: rgba(0, 0, 0, 0.3); background-image: url("../../assets/780.jpg"); background-size: cover; background-position: center; padding: 40px 0; }
.score-like-row { display: flex; justify-content: space-between; gap: 250px; font-weight: bold; color: #333; }
.like-count { color: #f56c6c; }
.sort-select-wrapper { width: 70%; max-width: 600px; display: flex; justify-content: flex-end; margin-bottom: 10px; }
.sort-select { width: 160px; }
.link-text { color: #409EFF; font-size: 0.9em; align-self: flex-end; }
.parent-comment-in-dialog { background-color: #f9f9f9; padding: 15px; border-radius: 8px; margin-bottom: 20px; }
.sub-comments-container { min-height: 200px; max-height: 50vh; overflow-y: auto; padding-right: 10px; }
.sub-comment-item { display: flex; gap: 10px; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #f0f0f0; }
.sub-comment-item:last-child { border-bottom: none; }
.sub-comment-content { display: flex; flex-direction: column; flex: 1; }
.sub-comment-username { font-weight: bold; color: #333; }
.sub-comment-text { margin: 5px 0; color: #606266; }
.sub-comment-time { font-size: 0.8em; color: #909399; align-self: flex-end; }
</style>