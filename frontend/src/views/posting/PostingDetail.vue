<!--<template>-->
<!--  <div class="post">-->
<!--    <div class="posting-detail-container">-->
<!--      <el-button type="primary" @click="backToAllPosting" class="back-button">-->
<!--        <el-icon><ArrowLeft /></el-icon>-->
<!--        返回笔记列表-->
<!--      </el-button>-->

<!--      <el-card class="detail-card" v-if="title">-->
<!--        <div class="post-header">-->
<!--          <div class="user-info">-->
<!--            <el-avatar :src="avatar" size="large" />-->
<!--            <div class="user-meta">-->
<!--              <h3 class="username">{{ userName }}</h3>-->
<!--              <span class="post-time">{{ time }}</span>-->
<!--            </div>-->
<!--          </div>-->
<!--          <el-button-->
<!--              v-if="role === 'MANAGER'"-->
<!--              type="danger"-->
<!--              @click="handleDelete"-->
<!--              size="small"-->
<!--              class="delete-btn"-->
<!--          >-->
<!--            <el-icon><Delete /></el-icon>-->
<!--            删除笔记-->
<!--          </el-button>-->
<!--        </div>-->

<!--        <div class="post-content">-->
<!--          <h2 class="post-title">{{ title }}</h2>-->
<!--          <el-carousel-->
<!--              v-if="Array.isArray(covers) && covers.length > 0"-->
<!--              :interval="4000"-->
<!--              type="card"-->
<!--              height="300px"-->
<!--              class="covers-carousel"-->
<!--          >-->
<!--            <el-carousel-item v-for="(img, index) in covers" :key="index">-->
<!--              <img :src="img" alt="笔记图片" class="carousel-image"/>-->
<!--            </el-carousel-item>-->
<!--          </el-carousel>-->
<!--          <div class="content-text" v-html="content"></div>-->
<!--        </div>-->

<!--        <div v-if="linkedProducts.length > 0" class="linked-products">-->
<!--          <h3>笔记中提到的书籍</h3>-->
<!--          <div class="products-list">-->
<!--            <el-card v-for="product in linkedProducts" :key="product.id" class="product-card" @click="goToProductDetail(product.id)" shadow="hover">-->
<!--              <div class="product-image">-->
<!--                <img :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : ''" alt="Product Cover" />-->
<!--              </div>-->
<!--              <h4 class="product-title">{{ product.title }}</h4>-->
<!--            </el-card>-->
<!--          </div>-->
<!--        </div>-->

<!--        <div class="action-buttons">-->


<!--          <el-button-->
<!--              v-if="role === 'CUSTOMER'"-->
<!--              :loading="likeLoading"-->
<!--              type="warning"-->
<!--              @click="handleLike"-->
<!--              class="action-btn"-->
<!--          >-->
<!--            <el-icon><Star /></el-icon>-->
<!--            点赞 ({{ like }})-->
<!--          </el-button>-->


<!--          <el-button-->
<!--              v-if="role === 'CUSTOMER'"-->
<!--              :loading="dislikeLoading"-->
<!--              type="warning"-->
<!--              @click="handleDislike"-->
<!--              class="action-btn"-->
<!--          >-->
<!--            <el-icon></el-icon>-->
<!--            点踩 ({{ dislike }})-->
<!--          </el-button>-->
<!--        </div>-->
<!--      </el-card>-->

<!--      <div v-else class="loading-container">-->
<!--        <el-icon class="loading-icon"><Loading /></el-icon>-->
<!--        正在加载笔记详情...-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { ref, onMounted } from 'vue'-->
<!--import { useRouter, useRoute } from 'vue-router'-->
<!--import {ArrowLeft, Delete, Loading, Star,} from '@element-plus/icons-vue'-->
<!--import { ElMessage } from 'element-plus'-->
<!--import { getPostingDetail, deletePost, likePost, dislikePost} from '../../api/posting.ts'-->
<!--import { getUserInfo } from '../../api/user.ts'-->
<!--import { getProduct, Product } from '../../api/product.ts'-->

<!--const router = useRouter()-->
<!--const route = useRoute()-->
<!--const role = sessionStorage.getItem('role')-->
<!--const id = Number(route.params.id)-->

<!--const userId = ref('')-->
<!--const userName = ref('')-->
<!--const title = ref('')-->
<!--const content = ref('')-->
<!--const covers = ref<string[]>([]) // 修正: covers 是一个数组-->
<!--const avatar = ref('')-->
<!--const time = ref('')-->
<!--const like = ref(0)-->
<!--const dislike = ref(0)-->
<!--const curUserId = sessionStorage.getItem('userId')-->
<!--const linkedProducts = ref<Product[]>([])-->
<!--const MAX_SIZE = 1024 * 1024; // 1MB-->
<!--const likeLoading = ref(false)-->
<!--const dislikeLoading = ref(false)-->
<!--function formatTime(timeStr: string): string {-->
<!--  const date = new Date(timeStr)-->
<!--  const year = date.getFullYear()-->
<!--  const month = String(date.getMonth() + 1).padStart(2, '0')-->
<!--  const day = String(date.getDate()).padStart(2, '0')-->
<!--  const hours = String(date.getHours()).padStart(2, '0')-->
<!--  const minutes = String(date.getMinutes()).padStart(2, '0')-->
<!--  return `${year}年${month}月${day}日 ${hours}时${minutes}分`-->
<!--}-->

<!--const goToProductDetail = (productId: number) => {-->
<!--  router.push({ path: `/home/product/${productId}` });-->
<!--}-->

<!--const fetchLinkedProducts = async (productIds: number[]) => {-->
<!--  if (!productIds || productIds.length === 0) return;-->
<!--  try {-->
<!--    const productRequests = productIds.map(pid => getProduct(String(pid)));-->
<!--    const productResponses = await Promise.all(productRequests);-->
<!--    linkedProducts.value = productResponses.map(res => res.data.data);-->
<!--  } catch (error) {-->
<!--    console.error("获取关联商品失败:", error);-->
<!--  }-->
<!--}-->

<!--async function getPost() {-->
<!--  try {-->
<!--    const res = await getPostingDetail(id)-->
<!--    console.log("posting: ", res.data);-->
<!--    if (res.code === '200') {-->
<!--      const postData = res.data-->
<!--      title.value = postData.title-->
<!--      content.value = postData.content-->
<!--      covers.value = Array.isArray(postData.covers) ? postData.covers : [] // 确保 covers 是一个数组-->
<!--      time.value = formatTime(postData.time)-->
<!--      like.value = Number(postData.like) || 0-->
<!--      dislike.value = Number(postData.dislike) || 0-->
<!--      userId.value = postData.userId-->
<!--      if (postData.productIds) {-->
<!--        fetchLinkedProducts(postData.productIds);-->
<!--      }-->

<!--      const userRes = await getUserInfo(Number(userId.value))-->
<!--      if (userRes.data.code === '200') {-->
<!--        userName.value = userRes.data.data.username-->
<!--        avatar.value = userRes.data.data.avatar-->
<!--      }-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('加载笔记详情失败')-->
<!--  }-->
<!--}-->

<!--async function handleDelete() {-->
<!--  try {-->
<!--    const res = await deletePost(id)-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage.success('笔记删除成功')-->
<!--      await router.push({name: 'AllPostings'})-->
<!--    } else {-->
<!--      console.error("Error in deleting post:", res.data.msg);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('删除失败')-->
<!--  }-->
<!--}-->
<!--//-->
<!--// async function handleLike() {-->
<!--//   try {-->
<!--//     const res = await likePost(id, Number(curUserId))-->
<!--//     if (res.data.code === '200') {-->
<!--//       ElMessage.success(res.data.data)-->
<!--//       await getPost();-->
<!--//     }-->
<!--//   } catch (error) {-->
<!--//     ElMessage.error('点赞失败')-->
<!--//   }-->
<!--// }-->
<!--// 点赞处理-->
<!--async function handleLike() {-->
<!--  likeLoading.value = true-->
<!--  try {-->
<!--    const res = await likePost (id, Number(curUserId))-->
<!--    console.log("resPost", res.code)-->
<!--    if (res.code === '200') {-->
<!--      ElMessage.success(res.data)-->
<!--      await getPost();-->
<!--    }-->
<!--    else ElMessage.error(res.data.msg)-->
<!--  } catch (error) {-->
<!--    ElMessage.error('点赞失败')-->
<!--  } finally {-->
<!--    likeLoading.value = false-->
<!--  }-->
<!--}-->
<!--// async function handleDislike1() {-->
<!--//   try {-->
<!--//     const res = await dislikePost(id, Number(curUserId))-->
<!--//     if (res.data.code === '200') {-->
<!--//       ElMessage.success(res.data.data)-->
<!--//       await getPost();-->
<!--//     }-->
<!--//   } catch (error) {-->
<!--//     ElMessage.error('点踩失败')-->
<!--//   }-->
<!--// }-->
<!--async function handleDislike() {-->
<!--  dislikeLoading.value = true-->
<!--  try {-->
<!--    const res = await dislikePost (id, Number(curUserId))-->
<!--    console.log("resPost", res.code)-->
<!--    if (res.code === '200') {-->
<!--      ElMessage.success(res.data)-->
<!--      await getPost();-->
<!--    }-->
<!--    else ElMessage.error(res.data.msg)-->
<!--  } catch (error) {-->
<!--    ElMessage.error('点踩失败')-->
<!--  } finally {-->
<!--    dislikeLoading.value = false-->
<!--  }-->
<!--}-->
<!--function backToAllPosting() {-->
<!--  router.push({ name: 'AllPostings' })-->
<!--}-->

<!--onMounted(() => {-->
<!--  getPost()-->
<!--})-->
<!--</script>-->

<!--<style scoped>-->
<!--.posting-detail-container {-->
<!--  max-width: 1200px;-->
<!--  margin: 20px auto;-->
<!--  padding: 20px;-->
<!--}-->

<!--.post {-->
<!--  background-color: #121212; /* 整页背景黑色 */-->
<!--  min-height: 100vh;-->
<!--  padding: 20px 0;-->
<!--}-->

<!--.back-button {-->
<!--  margin-bottom: 30px;-->
<!--  background-color: #4a4a4a; /* 深灰按钮 */-->
<!--  color: #ffcc00;            /* 金色文字 */-->
<!--  border: 1px solid #666;-->
<!--  border-radius: 6px;-->
<!--  transition: background-color 0.3s, transform 0.2s;-->
<!--}-->
<!--.back-button:hover {-->
<!--  background-color: #5a5a5a;-->
<!--}-->

<!--.detail-card {-->
<!--  padding: 30px;-->
<!--  background-color: rgba(30, 30, 30, 0.85); /* 半透明深黑 */-->
<!--  border-radius: 10px;-->
<!--  color: #fff;-->
<!--}-->

<!--.post-header {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--  margin-bottom: 30px;-->
<!--}-->

<!--.user-info {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 15px;-->
<!--}-->

<!--.user-meta {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--}-->

<!--.username {-->
<!--  margin: 0;-->
<!--  font-size: 1.3em;-->
<!--  color: #ffcc00; /* 金色用户名 */-->
<!--}-->

<!--.post-time {-->
<!--  color: #ccc;-->
<!--  font-size: 0.9em;-->
<!--}-->

<!--.post-content {-->
<!--  margin-top: 20px;-->
<!--}-->

<!--.post-title {-->
<!--  font-size: 2em;-->
<!--  margin-bottom: 20px;-->
<!--  color: #ffcc00; /* 金色标题 */-->
<!--}-->

<!--.covers-carousel {-->
<!--  margin: 20px 0;-->
<!--}-->

<!--.carousel-image {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  object-fit: contain;-->
<!--}-->

<!--.content-text {-->
<!--  font-size: 16px;-->
<!--  line-height: 1.8;-->
<!--  color: #ccc; /* 半透明内容文字 */-->
<!--}-->

<!--.action-buttons {-->
<!--  margin-top: 40px;-->
<!--  display: flex;-->
<!--  gap: 20px;-->
<!--}-->

<!--.action-buttons .el-button {-->
<!--  background-color: #4a4a4a; /* 深灰按钮 */-->
<!--  color: #ffcc00;            /* 金色文字 */-->
<!--  border: 1px solid #666;-->
<!--  border-radius: 6px;-->
<!--  transition: background-color 0.3s, transform 0.2s;-->
<!--}-->
<!--.action-buttons .el-button:hover {-->
<!--  background-color: #5a5a5a;-->
<!--}-->

<!--.loading-container {-->
<!--  text-align: center;-->
<!--  padding: 50px;-->
<!--  font-size: 18px;-->
<!--  color: #ccc;-->
<!--}-->

<!--.loading-icon {-->
<!--  animation: rotating 2s linear infinite;-->
<!--  margin-right: 10px;-->
<!--}-->

<!--@keyframes rotating {-->
<!--  from { transform: rotate(0deg); }-->
<!--  to { transform: rotate(360deg); }-->
<!--}-->

<!--.delete-btn {-->
<!--  margin-left: auto;-->
<!--  background-color: #4a4a4a; /* 深灰按钮 */-->
<!--  color: #ffcc00;            /* 金色文字 */-->
<!--  border: 1px solid #666;-->
<!--  border-radius: 6px;-->
<!--  transition: background-color 0.3s;-->
<!--}-->
<!--.delete-btn:hover {-->
<!--  background-color: #5a5a5a;-->
<!--}-->

<!--.posting-detail-container { max-width: 1200px; margin: 20px auto; padding: 20px; }-->

<!--.back-button { margin-bottom: 30px; }-->
<!--.detail-card { padding: 30px; }-->
<!--.post-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }-->
<!--.user-info { display: flex; align-items: center; gap: 15px; }-->
<!--.user-meta { display: flex; flex-direction: column; }-->
<!--.username { margin: 0; font-size: 1.3em; }-->
<!--.post-time { color: #888; font-size: 0.9em; }-->

<!--.covers-carousel { margin: 20px 0; }-->
<!--.carousel-image { width: 100%; height: 100%; object-fit: contain; }-->

<!--.action-buttons { margin-top: 40px; display: flex; gap: 20px; }-->
<!--.loading-container { text-align: center; padding: 50px; font-size: 18px; }-->
<!--.loading-icon { animation: rotating 2s linear infinite; margin-right: 10px; }-->
<!--@keyframes rotating { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }-->
<!--.delete-btn { margin-left: auto; }-->

<!--.linked-products h3 {-->
<!--  font-size: 1.5em; /* 调整标题大小 */-->
<!--  color: #ffcc00; /* 匹配页面主色调 */-->
<!--  margin-top: 40px; /* 与上方内容隔开距离 */-->
<!--  margin-bottom: 20px;-->
<!--  padding-bottom: 10px;-->
<!--  border-bottom: 1px solid #444; /* 添加一条分割线 */-->
<!--}-->

<!--/* 书籍卡片列表的容器 */-->
<!--.products-list {-->
<!--  display: flex; /* 使用 Flexbox 布局实现横向排列 */-->
<!--  gap: 20px; /* 设置卡片之间的间距 */-->
<!--  overflow-x: auto; /* 当内容超出容器宽度时，显示横向滚动条 */-->
<!--  padding-bottom: 15px; /* 为滚动条留出空间，避免遮挡卡片阴影 */-->
<!--}-->

<!--/* 单个书籍卡片样式 - 这是控制大小的关键 */-->
<!--.product-card {-->
<!--  width: 160px; /* **核心：设置一个固定的卡片宽度** */-->
<!--  flex-shrink: 0; /* 防止卡片在 Flex 容器中被压缩 */-->
<!--  background-color: #2a2a2a; /* 卡片背景色 */-->
<!--  border: 1px solid #555;-->
<!--  border-radius: 8px;-->
<!--  overflow: hidden; /* 隐藏超出圆角的部分 */-->
<!--  display: flex;-->
<!--  flex-direction: column; /* 让图片和标题垂直排列 */-->
<!--  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;-->
<!--}-->

<!--.product-card:hover {-->
<!--  transform: translateY(-5px);-->
<!--  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);-->
<!--}-->

<!--/* 书籍封面的容器 */-->
<!--.product-image {-->
<!--  width: 100%;-->
<!--  height: 210px; /* **核心：设置一个固定的图片区域高度** */-->
<!--}-->

<!--/* 封面图片本身 */-->
<!--.product-image img {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  object-fit: cover; /* **核心：让图片填满容器，同时保持比例（多余部分会被裁剪）** */-->
<!--}-->

<!--/* 书籍标题样式 */-->
<!--.product-title {-->
<!--  margin: 0;-->
<!--  padding: 12px 10px;-->
<!--  font-size: 14px;-->
<!--  color: #ccc;-->
<!--  text-align: center;-->
<!--  /* 处理长标题，避免换行破坏布局 */-->
<!--  white-space: nowrap;-->
<!--  overflow: hidden;-->
<!--  text-overflow: ellipsis;-->
<!--}-->


<!--/* (可选) 美化横向滚动条 */-->
<!--.products-list::-webkit-scrollbar {-->
<!--  height: 8px;-->
<!--}-->
<!--.products-list::-webkit-scrollbar-track {-->
<!--  background: #2a2a2a;-->
<!--  border-radius: 4px;-->
<!--}-->
<!--.products-list::-webkit-scrollbar-thumb {-->
<!--  background: #555;-->
<!--  border-radius: 4px;-->
<!--}-->
<!--.products-list::-webkit-scrollbar-thumb:hover {-->
<!--  background: #777;-->
<!--}-->
<!--</style>-->
<template>
  <div class="page-container">
    <div class="posting-detail-wrapper">

      <!-- 1. 顶部导航：改为极简风格，呼应顶部Header -->
      <div class="top-nav">
        <div class="back-link" @click="backToAllPosting">
          <el-icon><ArrowLeft /></el-icon>
          <span>返回社区列表</span>
        </div>
      </div>

      <!-- 2. 主内容卡片：模拟纸质阅读质感 -->
      <div class="content-card" v-if="title">

        <!-- 文章头部：作者与时间 -->
        <div class="article-header">
          <div class="author-block">
            <el-avatar :src="avatar" :size="48" class="author-avatar" />
            <div class="author-info">
              <span class="author-name">{{ userName }}</span>
              <span class="publish-date">{{ time }}</span>
            </div>
          </div>

          <el-button
              v-if="role === 'MANAGER'"
              text
              bg
              type="danger"
              size="small"
              @click="handleDelete"
              class="delete-btn"
          >
            <el-icon style="margin-right: 4px"><Delete /></el-icon>
            删除
          </el-button>
        </div>

        <!-- 文章标题 -->
        <h1 class="article-title">{{ title }}</h1>

        <!-- 轮播图 (如果有) -->
        <div class="carousel-section" v-if="Array.isArray(covers) && covers.length > 0">
          <el-carousel :interval="5000" type="card" height="320px" indicator-position="outside">
            <el-carousel-item v-for="(img, index) in covers" :key="index" class="styled-carousel-item">
              <div class="img-container">
                <img :src="img" alt="笔记图片" />
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>

        <!-- 正文内容：优化排版 -->
        <div class="article-body" v-html="content"></div>

        <!-- 3. 关联书籍：设计成“推荐书架”的样式 -->
        <div v-if="linkedProducts.length > 0" class="book-shelf-section">
          <div class="shelf-header">
            <span class="shelf-icon">📖</span>
            <h3>文中提到的书籍</h3>
          </div>

          <div class="shelf-scroll-container">
            <div
                v-for="product in linkedProducts"
                :key="product.id"
                class="shelf-book-card"
                @click="goToProductDetail(product.id)"
            >
              <div class="book-cover-wrapper">
                <img :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : ''" alt="Cover" />
                <div class="book-shadow"></div>
              </div>
              <div class="book-info">
                <h4 class="book-name">{{ product.title }}</h4>
                <span class="check-detail">点击查看</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 4. 底部交互区：重设计按钮，匹配 Header 颜色 -->
        <div class="interaction-area">
          <div class="divider">
            <span>END</span>
          </div>

          <div class="button-group">
            <!-- 点赞按钮：深色边框，悬停变红 -->
            <button
                class="custom-btn like-btn"
                :class="{ 'is-loading': likeLoading }"
                @click="handleLike"
                v-if="role === 'CUSTOMER'"
            >
              <el-icon class="icon" :size="20"><Star /></el-icon>
              <span class="label">喜欢</span>
              <span class="count">{{ like }}</span>
            </button>

            <!-- 点踩按钮：灰色 -->
            <button
                class="custom-btn dislike-btn"
                :class="{ 'is-loading': dislikeLoading }"
                @click="handleDislike"
                v-if="role === 'CUSTOMER'"
            >
              <!-- 使用 Close 或其他图标代表踩 -->
              <el-icon class="icon" :size="20"><CircleClose /></el-icon>
              <span class="label">无感</span>
              <span class="count">{{ dislike }}</span>
            </button>
          </div>
        </div>

      </div>

      <!-- 加载状态 -->
      <div v-else class="loading-state">
        <el-icon class="is-loading" :size="30"><Loading /></el-icon>
        <p>正在翻阅笔记...</p>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
// 引入 CircleClose 作为点踩图标
import { ArrowLeft, Delete, Loading, Star, CircleClose } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getPostingDetail, deletePost, likePost, dislikePost} from '../../api/posting.ts'
import { getUserInfo } from '../../api/user.ts'
import { getProduct, Product } from '../../api/product.ts'

const router = useRouter()
const route = useRoute()
const role = sessionStorage.getItem('role')
const id = Number(route.params.id)

const userId = ref('')
const userName = ref('')
const title = ref('')
const content = ref('')
const covers = ref<string[]>([])
const avatar = ref('')
const time = ref('')
const like = ref(0)
const dislike = ref(0)
const curUserId = sessionStorage.getItem('userId')
const linkedProducts = ref<Product[]>([])
const likeLoading = ref(false)
const dislikeLoading = ref(false)

function formatTime(timeStr: string): string {
  const date = new Date(timeStr)
  return `${date.getFullYear()} / ${String(date.getMonth() + 1).padStart(2, '0')} / ${String(date.getDate()).padStart(2, '0')}`
}

const goToProductDetail = (productId: number) => {
  router.push({ path: `/home/product/${productId}` });
}

const fetchLinkedProducts = async (productIds: number[]) => {
  if (!productIds || productIds.length === 0) return;
  try {
    const productRequests = productIds.map(pid => getProduct(String(pid)));
    const productResponses = await Promise.all(productRequests);
    linkedProducts.value = productResponses.map(res => res.data.data);
  } catch (error) {
    console.error("获取关联商品失败:", error);
  }
}

async function getPost() {
  try {
    const res = await getPostingDetail(id)
    if (res.code === '200') {
      const postData = res.data
      title.value = postData.title
      content.value = postData.content
      covers.value = Array.isArray(postData.covers) ? postData.covers : []
      time.value = formatTime(postData.time)
      like.value = Number(postData.like) || 0
      dislike.value = Number(postData.dislike) || 0
      userId.value = postData.userId
      if (postData.productIds) {
        fetchLinkedProducts(postData.productIds);
      }

      const userRes = await getUserInfo(Number(userId.value))
      if (userRes.data.code === '200') {
        userName.value = userRes.data.data.username
        avatar.value = userRes.data.data.avatar
      }
    }
  } catch (error) {
    ElMessage.error('加载笔记详情失败')
  }
}

async function handleDelete() {
  try {
    const res = await deletePost(id)
    if (res.data.code === '200') {
      ElMessage.success('笔记已删除')
      await router.push({name: 'AllPostings'})
    } else {
      console.error("Error:", res.data.msg);
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function handleLike() {
  if(likeLoading.value) return;
  likeLoading.value = true
  try {
    const res = await likePost (id, Number(curUserId))
    if (res.code === '200') {
      ElMessage.success({ message: '点赞成功', type: 'success', plain: true })
      await getPost();
    }
    else ElMessage.error(res.data.msg)
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    likeLoading.value = false
  }
}

async function handleDislike() {
  if(dislikeLoading.value) return;
  dislikeLoading.value = true
  try {
    const res = await dislikePost (id, Number(curUserId))
    if (res.code === '200') {
      ElMessage.info({ message: '已记录您的反馈', plain: true })
      await getPost();
    }
    else ElMessage.error(res.data.msg)
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    dislikeLoading.value = false
  }
}

function backToAllPosting() {
  router.push({ name: 'AllPostings' })
}

onMounted(() => {
  getPost()
})
</script>

<style scoped>
/* ======== 1. 全局容器与背景 ======== */
.page-container {
  /* 稍微暖一点的浅灰，更有书卷气 */
  background-color: #f5f5f7;
  min-height: 100vh;
  padding: 30px 20px 80px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.posting-detail-wrapper {
  max-width: 960px; /* 类似 Medium 或 知乎专栏的阅读宽度 */
  margin: 0 auto;
}

/* ======== 2. 顶部导航 (极简风) ======== */
.top-nav {
  margin-bottom: 24px;
}
.back-link {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #666;
  font-size: 15px;
  font-weight: 500;
  transition: color 0.3s;
  padding: 8px 0;
}
.back-link:hover {
  color: #2c2c2c; /* 悬停变为 Header 同款深灰 */
}
.back-link .el-icon {
  margin-right: 6px;
  font-size: 18px;
}

/* ======== 3. 主内容卡片 ======== */
.content-card {
  background: #ffffff;
  border-radius: 16px; /* 更大的圆角 */
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04); /* 极淡的高级阴影 */
  padding: 50px 60px; /* 宽裕的内边距，留白产生高级感 */
  position: relative;
}

/* 响应式调整：手机端减少内边距 */
@media (max-width: 768px) {
  .content-card {
    padding: 30px 20px;
  }
}

/* ---- 文章头部 ---- */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}
.author-block {
  display: flex;
  align-items: center;
  gap: 14px;
}
.author-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.author-info {
  display: flex;
  flex-direction: column;
}
.author-name {
  font-size: 16px;
  font-weight: 700;
  color: #2c2c2c;
}
.publish-date {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
  font-family: Georgia, serif; /* 日期用衬线体更有味道 */
  letter-spacing: 0.5px;
}

/* ---- 标题与正文 ---- */
.article-title {
  font-size: 32px;
  color: #1a1a1a;
  line-height: 1.4;
  font-weight: 800;
  margin-bottom: 30px;
  letter-spacing: -0.5px;
}

.article-body {
  font-size: 17px; /* 稍大的正文字号，适合阅读 */
  line-height: 1.8;
  color: #333;
  margin-bottom: 50px;
}

/* ---- 轮播图美化 ---- */
.carousel-section {
  margin: 30px 0 50px;
}
.styled-carousel-item {
  border-radius: 12px;
  overflow: hidden;
}
.img-container {
  width: 100%;
  height: 100%;
  background: #f8f8f8;
}
.img-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
/* 修改 Element Carousel 指示器颜色 */
:deep(.el-carousel__indicator--horizontal .el-carousel__button) {
  background-color: #999;
}
:deep(.el-carousel__indicator--horizontal.is-active .el-carousel__button) {
  background-color: #2c2c2c; /* 激活态为深灰 */
}

/* ======== 4. 推荐书架 (关联书籍) ======== */
.book-shelf-section {
  background: #fafafa; /* 淡淡的灰色背景块 */
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 40px;
  border: 1px solid #f0f0f0;
}

.shelf-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}
.shelf-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #444;
}

.shelf-scroll-container {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding-bottom: 10px;
}
.shelf-scroll-container::-webkit-scrollbar {
  height: 4px;
}
.shelf-scroll-container::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 2px;
}

/* 拟物化书籍卡片 */
.shelf-book-card {
  flex: 0 0 120px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s;
}
.shelf-book-card:hover {
  transform: translateY(-5px);
}
.book-cover-wrapper {
  width: 120px;
  height: 160px;
  position: relative;
  box-shadow: 2px 4px 10px rgba(0,0,0,0.15); /* 书本阴影 */
  border-radius: 4px 6px 6px 4px; /* 模拟书脊 */
  overflow: hidden;
  margin-bottom: 12px;
}
.book-cover-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
/* 书脊高光效果 */
.book-cover-wrapper::before {
  content: '';
  position: absolute;
  top: 0; left: 0; bottom: 0;
  width: 6px;
  background: linear-gradient(to right, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
  z-index: 2;
}
.book-info {
  text-align: center;
}
.book-name {
  font-size: 13px;
  color: #333;
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.check-detail {
  font-size: 11px;
  color: #999;
}


/* ======== 5. 底部交互区 (重点修改) ======== */
.interaction-area {
  margin-top: 60px;
  text-align: center;
}

.divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
}
.divider span {
  font-size: 12px;
  color: #ccc;
  letter-spacing: 2px;
  position: relative;
}
.divider span::before, .divider span::after {
  content: "";
  display: inline-block;
  width: 40px;
  height: 1px;
  background: #eee;
  vertical-align: middle;
  margin: 0 10px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 24px;
}

/* 自定义胶囊按钮 */
.custom-btn {
  border: none;
  outline: none;
  background: #fff;
  cursor: pointer;
  padding: 12px 32px;
  border-radius: 50px; /* 胶囊形 */
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.custom-btn:active {
  transform: scale(0.96);
}

/* 点赞按钮样式 */
.like-btn {
  border: 1px solid #2c2c2c; /* 深灰边框，呼应 Header */
  color: #2c2c2c;
}
.like-btn:hover {
  background-color: #2c2c2c; /* 悬停变黑 */
  color: #fff; /* 文字变白 */
  box-shadow: 0 4px 12px rgba(44, 44, 44, 0.3);
}
/* 激活或喜欢时的状态：加一点红色点缀 */
.like-btn:hover .icon {
  color: #ff4757; /* 悬停时爱心变红 */
}

/* 点踩按钮样式 */
.dislike-btn {
  border: 1px solid #e0e0e0;
  color: #999;
}
.dislike-btn:hover {
  border-color: #ccc;
  background-color: #f5f5f5;
  color: #666;
}

.count {
  font-size: 13px;
  opacity: 0.8;
  margin-left: 4px;
}

/* ======== 加载态 ======== */
.loading-state {
  text-align: center;
  padding: 100px 0;
  color: #999;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}
</style>