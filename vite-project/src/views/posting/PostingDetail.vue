<template>
  <div class="post">
    <div class="posting-detail-container">
      <el-button type="primary" @click="backToAllPosting" class="back-button">
        <el-icon><ArrowLeft /></el-icon>
        返回帖子列表
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
            删除帖子
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
              <img :src="img" alt="帖子图片" class="carousel-image"/>
            </el-carousel-item>
          </el-carousel>
          <div class="content-text" v-html="content"></div>
        </div>

        <div v-if="linkedProducts.length > 0" class="linked-products">
          <h3>帖子中提到的书籍</h3>
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

      <div v-else class="loading-container">
        <el-icon class="loading-icon"><Loading /></el-icon>
        正在加载帖子详情...
      </div>
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
    if (res.data.code === '200') {
      const postData = res.data.data
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
    ElMessage.error('加载帖子详情失败')
  }
}

async function handleDelete() {
  try {
    const res = await deletePost(id)
    if (res.data.code === '200') {
      ElMessage.success('帖子删除成功')
      await router.push({name: 'AllPostings'})
    } else {
      console.error("Error in deleting post:", res.data.msg);
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function handleLike() {
  try {
    const res = await likePost(id, Number(curUserId))
    if (res.data.code === '200') {
      ElMessage.success(res.data.data)
      await getPost();
    }
  } catch (error) {
    ElMessage.error('点赞失败')
  }
}

async function handleDislike() {
  try {
    const res = await dislikePost(id, Number(curUserId))
    if (res.data.code === '200') {
      ElMessage.success(res.data.data)
      await getPost();
    }
  } catch (error) {
    ElMessage.error('点踩失败')
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
.posting-detail-container { max-width: 1200px; margin: 20px auto; padding: 20px; }
.post { background-image: url("../../assets/login.png"); }
.back-button { margin-bottom: 30px; }
.detail-card { padding: 30px; }
.post-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.user-info { display: flex; align-items: center; gap: 15px; }
.user-meta { display: flex; flex-direction: column; }
.username { margin: 0; font-size: 1.3em; }
.post-time { color: #888; font-size: 0.9em; }
.post-content { margin-top: 20px; }
.post-title { font-size: 2em; margin-bottom: 20px; color: #333; }
.covers-carousel { margin: 20px 0; }
.carousel-image { width: 100%; height: 100%; object-fit: contain; }
.content-text { font-size: 16px; line-height: 1.8; color: #444; }
.action-buttons { margin-top: 40px; display: flex; gap: 20px; }
.loading-container { text-align: center; padding: 50px; font-size: 18px; }
.loading-icon { animation: rotating 2s linear infinite; margin-right: 10px; }
@keyframes rotating { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
.delete-btn { margin-left: auto; }
</style>