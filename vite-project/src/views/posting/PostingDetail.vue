<template>
  <div class="posting-detail-container">
    <!-- 返回按钮 -->
    <el-button type="primary" @click="backToAllPosting" class="back-button">
      <el-icon><ArrowLeft /></el-icon>
      返回帖子列表
    </el-button>

    <!-- 帖子内容卡片 -->
    <el-card class="detail-card" v-if="title">
      <!-- 用户信息头部 -->
      <div class="post-header">
        <div class="user-info">
          <el-avatar :src="avatar" size="large" />
          <div class="user-meta">
            <h3 class="username">{{ userName }}</h3>
            <span class="post-time">{{ time }}</span>
          </div>
        </div>
        <el-button
            v-if="role === 'MANAGER'"
            type="danger"
            @click="handleDelete"
            size="small"
            class="delete-btn"
        >
          <el-icon><Delete /></el-icon>
          删除帖子
        </el-button>
      </div>

      <!-- 帖子主要内容 -->
      <div class="post-content">
        <h2 class="post-title">{{ title }}</h2>
        <img v-if="cover" :src="cover" alt="封面图" class="cover-image" />
        <div class="content-text" v-html="content"></div>
      </div>

      <!-- 互动按钮 -->
      <div class="action-buttons">
        <el-button
            v-if="role === 'CUSTOMER'"
            type="primary"
            @click="handleLike"
            size="large"
        >
          <span>赞</span>
          <span>{{ like }}</span>
        </el-button>

        <el-button
            v-if="role === 'CUSTOMER'"
            type="primary"
            @click="handleDislike"
            size="large"
        >
          <span>踩</span>
          <span>{{ dislike }}</span>
        </el-button>
      </div>
    </el-card>

    <!-- 加载状态 -->
    <div v-else class="loading-container">
      <el-icon class="loading-icon"><Loading /></el-icon>
      正在加载帖子详情...
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Delete, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getPostingDetail, deletePost, likePost, dislikePost} from '../../api/posting.ts'
import { getUserInfo } from '../../api/user.ts'

const router = useRouter()
const route = useRoute()
const role = sessionStorage.getItem('role')
const id = Number(route.params.id)

// 响应式数据
const userId = ref('')
const userName = ref('')
const title = ref('')
const content = ref('')
const cover = ref('')
const avatar = ref('')
const time = ref('')
const like = ref(0)
const dislike = ref(0)

function formatTime(timeStr: string): string {
  const date = new Date(timeStr)

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0') // 月份从 0 开始
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')

  return `${year}年${month}月${day}日 ${hours}时${minutes}分`
}

// 获取帖子详情
async function getPost() {
  try {
    const res = await getPostingDetail(id)
    if (res.data.code === '200') {
      const postData = res.data.data
      title.value = postData.title
      content.value = postData.content
      cover.value = postData.cover
      time.value = formatTime(postData.time)  // 使用time字段
      like.value = Number(postData.like) || 0
      dislike.value = Number(postData.dislike) || 0
      userId.value = postData.userId

      // 获取用户信息
      const userRes = await getUserInfo(Number(userId.value))
      if (userRes.data.code === '200') {
        userName.value = userRes.data.data.username
        avatar.value = userRes.data.data.avatar
        console.log(userName.value)
        console.log(avatar.value)
      }
    }
  } catch (error) {
    ElMessage.error('加载帖子详情失败')
  }
}

// 删除处理
async function handleDelete() {
  try {
    await deletePost(id)
    ElMessage.success('帖子删除成功')
    await router.push({ name: 'AllPostings' })
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 点赞处理
async function handleLike() {
  try {
    const res = await likePost(id)
    if (res.data.code === '200') {
      like.value++
      console.log(like.value)
      ElMessage.success(res.data.data)
    }
  } catch (error) {
    ElMessage.error('点赞失败')
  }
}

// 点踩处理
async function handleDislike() {
  try {
    const res = await dislikePost(id)
    if (res.data.code === '200') {
      dislike.value++
      console.log(dislike.value)
      ElMessage.success(res.data.data)
    }
  } catch (error) {
    ElMessage.error('点踩失败')
  }
}

// 返回帖子列表
function backToAllPosting() {
  router.push({ name: 'AllPostings' })
}

onMounted(() => {
  getPost()
})
</script>

<style scoped>
/* 保持原有样式不变 */
.posting-detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
}

.back-button {
  margin-bottom: 30px;
}

.detail-card {
  padding: 30px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-meta {
  display: flex;
  flex-direction: column;
}

.username {
  margin: 0;
  font-size: 1.3em;
}

.post-time {
  color: #888;
  font-size: 0.9em;
}

.post-content {
  margin-top: 20px;
}

.post-title {
  font-size: 2em;
  margin-bottom: 20px;
  color: #333;
}

.cover-image {
  max-width: 100%;
  max-height: 500px;
  border-radius: 8px;
  margin: 20px 0;
  object-fit: contain;
}

.content-text {
  font-size: 16px;
  line-height: 1.8;
  color: #444;
}

.action-buttons {
  margin-top: 40px;
  display: flex;
  gap: 20px;
}

.loading-container {
  text-align: center;
  padding: 50px;
  font-size: 18px;
}

.loading-icon {
  animation: rotating 2s linear infinite;
  margin-right: 10px;
}

@keyframes rotating {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.delete-btn {
  margin-left: auto;
}
</style>