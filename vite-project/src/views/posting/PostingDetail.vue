<template>
  <div class="post">
    <div class="posting-detail-container">
      <el-button type="primary" @click="backToAllPosting" class="back-button">
        <el-icon><ArrowLeft /></el-icon>
        返回笔记列表
      </el-button>

      <el-card class="detail-card" v-if="title">
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
            删除笔记
          </el-button>
        </div>

        <div class="post-content">
          <h2 class="post-title">{{ title }}</h2>
          <el-carousel
              v-if="Array.isArray(covers) && covers.length > 0"
              :interval="4000"
              type="card"
              height="300px"
              class="covers-carousel"
          >
            <el-carousel-item v-for="(img, index) in covers" :key="index">
              <img :src="img" alt="笔记图片" class="carousel-image"/>
            </el-carousel-item>
          </el-carousel>
          <div class="content-text" v-html="content"></div>
        </div>

        <div v-if="linkedProducts.length > 0" class="linked-products">
          <h3>笔记中提到的书籍</h3>
          <div class="products-list">
            <el-card v-for="product in linkedProducts" :key="product.id" class="product-card" @click="goToProductDetail(product.id)" shadow="hover">
              <div class="product-image">
                <img :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : ''" alt="Product Cover" />
              </div>
              <h4 class="product-title">{{ product.title }}</h4>
            </el-card>
          </div>
        </div>

        <div class="action-buttons">


          <el-button
              v-if="role === 'CUSTOMER'"
              :loading="likeLoading"
              type="warning"
              @click="handleLike"
              class="action-btn"
          >
            <el-icon><Star /></el-icon>
            点赞 ({{ like }})
          </el-button>


          <el-button
              v-if="role === 'CUSTOMER'"
              :loading="dislikeLoading"
              type="warning"
              @click="handleDislike"
              class="action-btn"
          >
            <el-icon></el-icon>
            点踩 ({{ dislike }})
          </el-button>
        </div>
      </el-card>

      <div v-else class="loading-container">
        <el-icon class="loading-icon"><Loading /></el-icon>
        正在加载笔记详情...
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {ArrowLeft, Delete, Loading, Star,} from '@element-plus/icons-vue'
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
const covers = ref<string[]>([]) // 修正: covers 是一个数组
const avatar = ref('')
const time = ref('')
const like = ref(0)
const dislike = ref(0)
const curUserId = sessionStorage.getItem('userId')
const linkedProducts = ref<Product[]>([])
const MAX_SIZE = 1024 * 1024; // 1MB
const likeLoading = ref(false)
const dislikeLoading = ref(false)
function formatTime(timeStr: string): string {
  const date = new Date(timeStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}年${month}月${day}日 ${hours}时${minutes}分`
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
    console.log("posting: ", res.data);
    if (res.code === '200') {
      const postData = res.data
      title.value = postData.title
      content.value = postData.content
      covers.value = Array.isArray(postData.covers) ? postData.covers : [] // 确保 covers 是一个数组
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
      ElMessage.success('笔记删除成功')
      await router.push({name: 'AllPostings'})
    } else {
      console.error("Error in deleting post:", res.data.msg);
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
//
// async function handleLike() {
//   try {
//     const res = await likePost(id, Number(curUserId))
//     if (res.data.code === '200') {
//       ElMessage.success(res.data.data)
//       await getPost();
//     }
//   } catch (error) {
//     ElMessage.error('点赞失败')
//   }
// }
// 点赞处理
async function handleLike() {
  likeLoading.value = true
  try {
    const res = await likePost (id, Number(curUserId))
    console.log("resPost", res.code)
    if (res.code === '200') {
      ElMessage.success(res.data)
      await getPost();
    }
    else ElMessage.error(res.data.msg)
  } catch (error) {
    ElMessage.error('点赞失败')
  } finally {
    likeLoading.value = false
  }
}
// async function handleDislike1() {
//   try {
//     const res = await dislikePost(id, Number(curUserId))
//     if (res.data.code === '200') {
//       ElMessage.success(res.data.data)
//       await getPost();
//     }
//   } catch (error) {
//     ElMessage.error('点踩失败')
//   }
// }
async function handleDislike() {
  dislikeLoading.value = true
  try {
    const res = await dislikePost (id, Number(curUserId))
    console.log("resPost", res.code)
    if (res.code === '200') {
      ElMessage.success(res.data)
      await getPost();
    }
    else ElMessage.error(res.data.msg)
  } catch (error) {
    ElMessage.error('点踩失败')
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
.posting-detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.post {
  background-color: #121212; /* 整页背景黑色 */
  min-height: 100vh;
  padding: 20px 0;
}

.back-button {
  margin-bottom: 30px;
  background-color: #4a4a4a; /* 深灰按钮 */
  color: #ffcc00;            /* 金色文字 */
  border: 1px solid #666;
  border-radius: 6px;
  transition: background-color 0.3s, transform 0.2s;
}
.back-button:hover {
  background-color: #5a5a5a;
}

.detail-card {
  padding: 30px;
  background-color: rgba(30, 30, 30, 0.85); /* 半透明深黑 */
  border-radius: 10px;
  color: #fff;
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
  color: #ffcc00; /* 金色用户名 */
}

.post-time {
  color: #ccc;
  font-size: 0.9em;
}

.post-content {
  margin-top: 20px;
}

.post-title {
  font-size: 2em;
  margin-bottom: 20px;
  color: #ffcc00; /* 金色标题 */
}

.covers-carousel {
  margin: 20px 0;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.content-text {
  font-size: 16px;
  line-height: 1.8;
  color: #ccc; /* 半透明内容文字 */
}

.action-buttons {
  margin-top: 40px;
  display: flex;
  gap: 20px;
}

.action-buttons .el-button {
  background-color: #4a4a4a; /* 深灰按钮 */
  color: #ffcc00;            /* 金色文字 */
  border: 1px solid #666;
  border-radius: 6px;
  transition: background-color 0.3s, transform 0.2s;
}
.action-buttons .el-button:hover {
  background-color: #5a5a5a;
}

.loading-container {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #ccc;
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
  background-color: #4a4a4a; /* 深灰按钮 */
  color: #ffcc00;            /* 金色文字 */
  border: 1px solid #666;
  border-radius: 6px;
  transition: background-color 0.3s;
}
.delete-btn:hover {
  background-color: #5a5a5a;
}

.posting-detail-container { max-width: 1200px; margin: 20px auto; padding: 20px; }

.back-button { margin-bottom: 30px; }
.detail-card { padding: 30px; }
.post-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.user-info { display: flex; align-items: center; gap: 15px; }
.user-meta { display: flex; flex-direction: column; }
.username { margin: 0; font-size: 1.3em; }
.post-time { color: #888; font-size: 0.9em; }

.covers-carousel { margin: 20px 0; }
.carousel-image { width: 100%; height: 100%; object-fit: contain; }

.action-buttons { margin-top: 40px; display: flex; gap: 20px; }
.loading-container { text-align: center; padding: 50px; font-size: 18px; }
.loading-icon { animation: rotating 2s linear infinite; margin-right: 10px; }
@keyframes rotating { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
.delete-btn { margin-left: auto; }

.linked-products h3 {
  font-size: 1.5em; /* 调整标题大小 */
  color: #ffcc00; /* 匹配页面主色调 */
  margin-top: 40px; /* 与上方内容隔开距离 */
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #444; /* 添加一条分割线 */
}

/* 书籍卡片列表的容器 */
.products-list {
  display: flex; /* 使用 Flexbox 布局实现横向排列 */
  gap: 20px; /* 设置卡片之间的间距 */
  overflow-x: auto; /* 当内容超出容器宽度时，显示横向滚动条 */
  padding-bottom: 15px; /* 为滚动条留出空间，避免遮挡卡片阴影 */
}

/* 单个书籍卡片样式 - 这是控制大小的关键 */
.product-card {
  width: 160px; /* **核心：设置一个固定的卡片宽度** */
  flex-shrink: 0; /* 防止卡片在 Flex 容器中被压缩 */
  background-color: #2a2a2a; /* 卡片背景色 */
  border: 1px solid #555;
  border-radius: 8px;
  overflow: hidden; /* 隐藏超出圆角的部分 */
  display: flex;
  flex-direction: column; /* 让图片和标题垂直排列 */
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
}

/* 书籍封面的容器 */
.product-image {
  width: 100%;
  height: 210px; /* **核心：设置一个固定的图片区域高度** */
}

/* 封面图片本身 */
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* **核心：让图片填满容器，同时保持比例（多余部分会被裁剪）** */
}

/* 书籍标题样式 */
.product-title {
  margin: 0;
  padding: 12px 10px;
  font-size: 14px;
  color: #ccc;
  text-align: center;
  /* 处理长标题，避免换行破坏布局 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}


/* (可选) 美化横向滚动条 */
.products-list::-webkit-scrollbar {
  height: 8px;
}
.products-list::-webkit-scrollbar-track {
  background: #2a2a2a;
  border-radius: 4px;
}
.products-list::-webkit-scrollbar-thumb {
  background: #555;
  border-radius: 4px;
}
.products-list::-webkit-scrollbar-thumb:hover {
  background: #777;
}
</style>