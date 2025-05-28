
<template>
  <div class="bgimage">
    <div class="posting-detail-container">
    <!-- 返回按钮 -->
    <el-button type="primary" @click="backToAllComment" class="back-button">
      <el-icon><ArrowLeft /></el-icon>
      返回评价列表
    </el-button>

    <!-- 评价内容卡片 -->
    <el-card class="detail-card" v-if="content">
      <!-- 用户信息头部 -->
      <div class="post-header">
        <div class="user-info">
          <el-avatar :src="avatar" size="large" />
          <div class="user-meta">
            <h3 class="username">{{ userName }}</h3>
            <div class="rating-time">
              <el-rate
                  v-model="score"
                  disabled
                  :colors="['#F56C6C', '#F56C6C', '#F56C6C']"
                  class="rating-stars"
              />
              <span class="post-time">{{ formattedTime }}</span>
            </div>
          </div>
        </div>

      </div>

      <!-- 新增操作按钮区域 -->
      <div class="action-buttons">
        <!-- 管理员删除按钮 -->
        <el-button
            v-if="currentUserRole === 'MANAGER' || isCommentOwner"
            type="danger"
            @click="handleDelete"
            class="action-btn"
        >
          <el-icon><Delete /></el-icon>
          删除评价
        </el-button>

        <!-- 用户编辑按钮 -->
        <el-button
            v-if="currentUserRole === 'CUSTOMER' && isCommentOwner"
            type="primary"
            @click="showEditDialog"
            class="action-btn"
        >
          <el-icon><Edit /></el-icon>
          编辑评价
        </el-button>

        <!-- 点赞按钮 -->
        <el-button
            v-if="currentUserRole === 'CUSTOMER' && !isCommentOwner"
            :loading="likeLoading"
            type="warning"
            @click="handleLike"
            class="action-btn"
        >
          <el-icon><Star /></el-icon>
          点赞 ({{ like }})
        </el-button>
      </div>

      <!-- 帖子主要内容 -->
      <div class="post-content">
        <div class="content-text" v-html="content"></div>
      </div>

    </el-card>
      <!-- 加载状态 -->
      <div v-else class="loading-container">
        <el-icon class="loading-icon"><Loading /></el-icon>
        正在加载帖子详情...
      </div>

      <!-- 编辑对话框 -->
      <el-dialog v-model="editDialogVisible" title="编辑评价" width="600px">
        <el-form :model="editForm">
          <el-form-item label="评分">
            <el-rate v-model="editForm.score" :max="5" :allow-half="true"/>
          </el-form-item>
          <el-form-item label="内容">
            <el-input
                v-model="editForm.content"
                type="textarea"
                :rows="4"
                placeholder="请输入评价内容"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">提交修改</el-button>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Delete, Star, Flag, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getComment, deleteComment, updateComment, likeComment } from '../../api/comment.ts'
import { getUserInfo } from '../../api/user.ts'
import { formatTime } from '../..//utils/date'
const router = useRouter()
const route = useRoute()

const id = Number(route.params.commentId)
const currentUserRole = sessionStorage.getItem('role')
const currentUserName = sessionStorage.getItem('username')
const isCommentOwner = computed(() => currentUserName === userName.value)

const editDialogVisible = ref(false)
const editForm = reactive({
  content: '',
  score: 0,
})

const formattedTime = ref('')

// 响应式数据
const userId = ref('')
const userName = ref('')

const content = ref('')

const avatar = ref('')
const time = ref('')
const score = ref(0)
const like = ref(0)

const likeLoading = ref(false)

const curUserId = sessionStorage.getItem('userId')


// 获取帖子详情
async function get_getComment() {
  try {
    const res = await getComment(id)
    if (res.data.code === '200') {
      const postData = res.data.data
      formattedTime.value = formatTime(postData.time)

      content.value = postData.content

      time.value = postData.time  // 使用time字段
      like.value = postData.likes
      score.value = postData.score
      userId.value = postData.userId

      // 获取用户信息
      const userRes = await getUserInfo(postData.userId)
      if (userRes.data.code === '200') {
        userName.value = userRes.data.data.username
        avatar.value = userRes.data.data.avatar
      }
    }
  } catch (error) {
    ElMessage.error('加载帖子详情失败')
  }
}



// 返回帖子列表
function backToAllComment() {
  router.push({ name: 'ProductComments' })
}

// 删除处理
async function handleDelete() {
  try {
    const res = await deleteComment(id)
    if (res.data.code === '200') ElMessage.success(res.data.data)
    else ElMessage.error(res.data.msg)
    backToAllComment()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 点赞处理
async function handleLike() {
  likeLoading.value = true
  try {
    const res = await likeComment(id, Number(curUserId))
    if (res.data.code === '200') {
      ElMessage.success(res.data.data)
      await get_getComment();
    }
    else ElMessage.error(res.data.msg)
  } catch (error) {
    ElMessage.error('点赞失败')
  } finally {
    likeLoading.value = false
  }
}

// 编辑处理
function showEditDialog() {
  editForm.content = content.value
  editForm.score = score.value
  editDialogVisible.value = true
}

async function submitEdit() {
  try {
    const res = await updateComment({
      id: id,
      content: editForm.content,
      score: editForm.score
    })
    if (res.data.code === '200') ElMessage.success(res.data.data)
    else ElMessage.error(res.data.msg)
    editDialogVisible.value = false
    await get_getComment() // 刷新数据
  } catch (error) {
    ElMessage.error('修改失败')
  }
}

onMounted(() => {
  get_getComment()
})
</script>

<style scoped>
/* 保持原有样式不变 */
.posting-detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
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

.rating-time {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 8px;
}

.rating-stars {
  display: inline-block;
}

.like-count {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #606266;
  font-size: 14px;
}

.user-meta {
  flex-grow: 1;
}
.bgimage {
  min-height: 100vh;
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  background-repeat: no-repeat;
  padding: 20px; /* 让内容不会贴边 */
}

.posting-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  /* 不需要 min-height 了，由外层控制 */
}
</style>
