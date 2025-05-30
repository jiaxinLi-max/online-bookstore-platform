
<template>
  <el-main class="posting-list bgimage">
    <div class="back-button-wrapper">
      <el-button type="primary" icon="ArrowLeft" @click="goToAllProduct">返回主界面</el-button>
    </div>
    <!-- 创建帖子按钮 -->
    <el-button
        class="create-posting-button"
        @click="goToCreatePosting"
        style="margin-bottom: 20px;"
        v-if="role === 'CUSTOMER'"
    >
      发帖子
    </el-button>

    <!-- 排序按钮 -->
    <div style="margin-bottom: 20px;">
      <el-radio-group v-model="sortOption" @change="fetchPostings">
        <el-radio-button label="time">按时间排序</el-radio-button>
        <el-radio-button label="likes">按点赞排序</el-radio-button>
      </el-radio-group>
    </div>

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
import { sortByTime, sortByLike } from '../../api/tools.ts'

const postings = ref<Posting[]>([]);

const router = useRouter();
const sortOption = ref<'time' | 'likes'>('time') // 排序方式：默认按时间
const role = sessionStorage.getItem("role");
//
// async function get_getAllpostings() {
//   try {
//     const res = await getAllPosting();
//     //console.log("get_getAllproducts",res);
//     if (res.data && Array.isArray(res.data.data)) {
//       postings.value = res.data.data;
//       //console.log(res.data);
//     } else {
//       console.error('获取数据失败：响应格式不符合预期');
//     }
//   } catch (error) {
//     console.error('获取帖子列表失败:', error);
//   }
// }
async function fetchPostings() {
  try {
    let res
    if (sortOption.value === 'time') {
      res = await sortByTime('post')
    } else {
      res = await sortByLike('post')
    }

    if (res.data && Array.isArray(res.data.data)) {
      postings.value = res.data.data
    } else {
      console.error('获取数据失败：响应格式不符合预期')
    }
  } catch (error) {
    console.error('获取帖子失败:', error)
    ElMessage.error('获取帖子失败')
  }
}

// 导航到商店详情
function goToPostingDetail(postingId: number) {
  router.push({ name: 'PostingDetail', params: { id: postingId } });
}
function goToCreatePosting() {
  router.push({ path: '/home/create-posting' }); // 确保路由路径正确
}
function goToAllProduct() {
  router.push({ path: `/home/all-products` });
}

// 在组件挂载时获取商店数据
// onMounted(() => {
//   get_getAllpostings();
// });
onMounted(() => {
  fetchPostings()
})
</script>

<style scoped>
.posting-list {
  min-height: 800px;
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: center;     /* 居中显示每个帖子卡片 */
  gap: 20px;
}

.posting-card {
  width: 70%; /* 或者 100%，你可以根据需要调整 */
  max-width: 800px;
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  box-sizing: border-box;
  background-color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.posting-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.posting-image img {
  width: 150px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.posting-card h3,
.posting-card p {
  margin: 0;
}

.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: 1500px auto; /* 或根据需求调整为 cover 或百分比 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 关键属性：背景固定 */
  background-color: #7b6b4d; /* 深羊驼色兜底 */
  min-height: 100vh;
}
</style>
