<!--<template>-->
<!--  <div class="comment-detail">-->
<!--    &lt;!&ndash; 返回按钮 &ndash;&gt;-->
<!--    <el-button type="primary" @click="goBack" class="back-button">-->
<!--      <i class="el-icon-arrow-left"></i> 返回评价列表-->
<!--    </el-button>-->

<!--    &lt;!&ndash; 加载状态 &ndash;&gt;-->
<!--    <div v-if="loading" class="loading-container">-->
<!--      <el-icon class="loading-icon"><loading /></el-icon>-->
<!--      <span>正在加载评价详情...</span>-->
<!--    </div>-->

<!--    &lt;!&ndash; 错误提示 &ndash;&gt;-->
<!--    <div v-else-if="error" class="error-container">-->
<!--      <el-alert :title="error" type="error" :closable="false" />-->
<!--    </div>-->

<!--    &lt;!&ndash; 评价详情内容 &ndash;&gt;-->
<!--    <div v-else class="comment-content">-->


<!--      <div class="meta-info">-->
<!--        <span class="score">评分: {{ comment.score }}/5</span>-->
<!--        <span class="likes">❤ {{ comment.likes }} 人点赞</span>-->
<!--        <span class="time">发布时间: {{ formatTime(comment.time) }}</span>-->
<!--      </div>-->

<!--      <div class="content-box">-->
<!--        <p class="content">{{ comment.content }}</p>-->
<!--      </div>-->

<!--      <div class="author-info">-->
<!--        <el-avatar :size="40" :src="userAvatar" class="avatar"></el-avatar>-->
<!--        <span class="username">{{ comment.username }}</span>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--import { defineComponent, ref, onMounted } from 'vue';-->
<!--import { useRoute, useRouter } from 'vue-router';-->
<!--import { ElLoading } from 'element-plus';-->
<!--import { getComment } from '../../api/comment'-->
<!--import { Comment } from '../../api/comment'-->
<!--import { Loading } from '@element-plus/icons-vue';-->

<!--import { userInfo } from '../../api/user';-->

<!--interface CommentWithUser extends Comment {-->
<!--  username?: string;-->
<!--  avatar?: string;-->
<!--}-->
<!--export default defineComponent({-->
<!--  name: 'CommentDetail',-->
<!--  components: { Loading },-->
<!--  setup() {-->
<!--    const route = useRoute();-->
<!--    const router = useRouter();-->

<!--    // 响应式数据-->
<!--    const comment = ref<Comment>({} as Comment);-->
<!--    const loading = ref(true);-->
<!--    const error = ref('');-->
<!--    const userAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');-->

<!--    // 安全获取参数-->
<!--    const getSafeId = (param: string | string[]): number => {-->
<!--      const id = Array.isArray(param) ? param[0] : param;-->
<!--      const numId = Number(id);-->
<!--      return isNaN(numId) ? -1 : numId;-->
<!--    };-->

<!--    // 获取并验证路由参数-->
<!--    const productId = getSafeId(route.params.productId);-->
<!--    const commentId = getSafeId(route.params.commentId);-->

<!--    // 参数有效性检查-->
<!--    if (productId === -1 || commentId === -1) {-->
<!--      error.value = '无效的商品ID或评价ID';-->
<!--      loading.value = false;-->
<!--      router.replace('/404'); // 跳转到404页面-->
<!--    }-->

<!--    // 加载评价详情-->
<!--    const loadComment = async () => {-->
<!--      const loadingInstance = ElLoading.service({ fullscreen: true });-->


<!--      try {-->
<!--        const response = await getComment(commentId.toString());-->
<!--        if (response.data) {-->
<!--          comment.value = response.data;-->
<!--          const userRes = await userInfo(response.data.userId);-->
<!--        } else {-->
<!--          throw new Error('评价不存在');-->
<!--        }-->
<!--      } catch (err) {-->
<!--        error.value = err.response?.data?.message || '加载评价详情失败';-->
<!--        console.error('加载失败:', err);-->
<!--      } finally {-->
<!--        loading.value = false;-->
<!--        loadingInstance.close();-->
<!--      }-->
<!--    };-->

<!--    // 时间格式化-->
<!--    const formatTime = (timestamp: number) => {-->
<!--      return new Date(timestamp).toLocaleString();-->
<!--    };-->

<!--    // 返回上一页-->
<!--    const goBack = () => {-->
<!--      router.push({-->
<!--        name: 'ProductComments',-->
<!--        params: { productId }-->
<!--      });-->
<!--    };-->

<!--    onMounted(() => {-->
<!--      if (productId !== -1 && commentId !== -1) {-->
<!--        loadComment();-->
<!--      }-->
<!--    });-->

<!--    return {-->
<!--      comment,-->
<!--      loading,-->
<!--      error,-->
<!--      userAvatar,-->
<!--      formatTime,-->
<!--      goBack-->
<!--    };-->
<!--  }-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.comment-detail {-->
<!--  max-width: 800px;-->
<!--  margin: 20px auto;-->
<!--  padding: 20px;-->
<!--  background-color: #fff;-->
<!--  border-radius: 8px;-->
<!--  box-shadow: 0 2px 12px rgba(0,0,0,0.1);-->
<!--}-->

<!--.back-button {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.loading-container {-->
<!--  text-align: center;-->
<!--  padding: 40px 0;-->
<!--  .loading-icon {-->
<!--    animation: rotating 2s linear infinite;-->
<!--    margin-right: 10px;-->
<!--  }-->
<!--}-->

<!--.error-container {-->
<!--  margin-top: 20px;-->
<!--}-->

<!--.comment-content {-->
<!--  padding: 20px;-->
<!--  .comment-title {-->
<!--    color: #303133;-->
<!--    margin-bottom: 15px;-->
<!--  }-->

<!--  .meta-info {-->
<!--    margin-bottom: 20px;-->
<!--    color: #909399;-->
<!--    span {-->
<!--      margin-right: 15px;-->
<!--    }-->
<!--    .score {-->
<!--      color: #f56c6c;-->
<!--      font-weight: bold;-->
<!--    }-->
<!--  }-->

<!--  .content-box {-->
<!--    background-color: #f5f7fa;-->
<!--    padding: 15px;-->
<!--    border-radius: 4px;-->
<!--    .content {-->
<!--      line-height: 1.6;-->
<!--      color: #606266;-->
<!--    }-->
<!--  }-->

<!--  .author-info {-->
<!--    margin-top: 20px;-->
<!--    display: flex;-->
<!--    align-items: center;-->
<!--    .avatar {-->
<!--      margin-right: 10px;-->
<!--    }-->
<!--    .username {-->
<!--      color: #409eff;-->
<!--      font-weight: 500;-->
<!--    }-->
<!--  }-->
<!--}-->

<!--@keyframes rotating {-->
<!--  from { transform: rotate(0deg); }-->
<!--  to { transform: rotate(360deg); }-->
<!--}-->
<!--</style>-->
<template>
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
        <div class="like-count">
          <el-icon color="#F56C6C"><Star /></el-icon>
          {{ like }} 人点赞
        </div>

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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Delete, Star, Flag, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getComment, } from '../../api/comment.ts'
import { userInfo } from '../../api/user.ts'
import { formatTime } from '../..//utils/date'
const router = useRouter()
const route = useRoute()

const id = Number(route.params.commentId)
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
      const userRes = await userInfo(postData.userId)
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
</style>
<!--<script lang="ts">-->
<!--import { defineComponent, ref, onMounted } from 'vue';-->
<!--import { useRoute, useRouter } from 'vue-router';-->
<!--import { getComment } from '../../api/comment'-->
<!--import { userInfo } from '../../api/user';-->
<!--import { formatTime } from '../../utils/date.ts';-->
<!--import { ElLoading } from 'element-plus';-->

<!--interface EnhancedComment extends Comment {-->
<!--  username?: string;-->
<!--  avatar?: string;-->
<!--  formattedTime?: string;-->
<!--}-->

<!--export default defineComponent({-->
<!--  setup() {-->
<!--    const route = useRoute();-->
<!--    const router = useRouter();-->

<!--    // 安全获取参数-->
<!--    const getValidId = (param: string | string[]): number => {-->
<!--      const str = Array.isArray(param) ? param[0] : param;-->
<!--      const num = Number(str);-->
<!--      return isNaN(num) ? -1 : num;-->
<!--    };-->

<!--    const productId = Number(route.params.productId);-->
<!--    const commentId = Number(route.params.commentId);-->

<!--    // 参数校验-->
<!--    if (productId === -1 || commentId === -1) {-->
<!--      router.replace('/invalid-params');-->
<!--      return {};-->
<!--    }-->

<!--    const comment = ref<EnhancedComment>({});-->
<!--    const loading = ref(true);-->

<!--    const loadData = async () => {-->
<!--      const loadingInstance = ElLoading.service();-->

<!--      try {-->
<!--        // 获取评价基础数据-->
<!--        const commentRes = await getComment(commentId);-->

<!--        // 并行获取用户信息-->
<!--        const userPromise = userInfo(commentRes.data.userId);-->

<!--        // 处理时间格式-->
<!--        const formattedTime = formatTime(commentRes.data.createTime);-->

<!--        // 等待所有请求完成-->
<!--        const userRes = await userPromise;-->

<!--        // 合并数据-->
<!--        comment.value = {-->
<!--          ...commentRes.data,-->
<!--          username: userRes.data.data.username,-->
<!--          formattedTime-->
<!--        };-->
<!--      } catch (err) {-->
<!--        console.error('数据加载失败:', err);-->
<!--        ElMessage.error('加载失败，请稍后重试');-->
<!--      } finally {-->
<!--        loading.value = false;-->
<!--        loadingInstance.close();-->
<!--      }-->
<!--    };-->

<!--    onMounted(loadData);-->

<!--    return {-->
<!--      comment,-->
<!--      loading,-->
<!--      formatTime-->
<!--    };-->
<!--  }-->
<!--});-->
<!--</script>-->