<!--<template>-->
<!--  <el-main class="posting-list bgimage">-->
<!--    <el-button class="create-posting-button" @click="goToCreatePosting" style="margin-bottom: 20px;" v-if="role === 'CUSTOMER'">-->
<!--      发笔记-->
<!--    </el-button>-->
<!--    <div style="margin-bottom: 20px;">-->
<!--      <el-radio-group v-model="sortOption" @change="fetchPostings">-->
<!--        <el-radio-button label="time">按时间排序</el-radio-button>-->
<!--        <el-radio-button label="likes">按点赞排序</el-radio-button>-->
<!--      </el-radio-group>-->
<!--    </div>-->
<!--    <el-card-->
<!--        v-for="posting in postings"-->
<!--        :key="posting.id"-->
<!--        class="posting-card"-->
<!--        @click="goToPostingDetail(posting.id)"-->
<!--    >-->
<!--      <div class="posting-image">-->
<!--        <img :src="Array.isArray(posting.covers) && posting.covers.length > 0 ? posting.covers[0] : ''" alt="Posting covers" />-->
<!--      </div>-->
<!--      <h3>{{ posting.title }}</h3>-->
<!--      <p>点击查看详情</p>-->
<!--    </el-card>-->
<!--  </el-main>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--// <script>部分无需修改，保持原样-->
<!--import { ref, onMounted } from 'vue';-->
<!--import { useRouter } from 'vue-router';-->
<!--import { Posting } from '../../api/posting.ts';-->
<!--import { ElMessage } from "element-plus";-->
<!--import { sortByTime, sortByLike } from '../../api/tools.ts'-->

<!--const postings = ref<Posting[]>([]);-->
<!--const router = useRouter();-->
<!--const sortOption = ref<'time' | 'likes'>('time');-->
<!--const role = sessionStorage.getItem("role");-->

<!--async function fetchPostings() {-->
<!--  try {-->
<!--    let res;-->
<!--    if (sortOption.value === 'time') {-->
<!--      res = await sortByTime('post');-->
<!--    } else {-->
<!--      res = await sortByLike('post');-->
<!--    }-->
<!--    if (res.data.code === '200' && Array.isArray(res.data.data)) {-->
<!--      postings.value = res.data.data;-->
<!--    } else {-->
<!--      console.error('获取数据失败：响应格式不符合预期');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error('获取帖子失败:', error);-->
<!--    ElMessage.error('获取笔记失败');-->
<!--  }-->
<!--}-->
<!--function goToPostingDetail(postingId: number) {-->
<!--  router.push({ name: 'PostingDetail', params: { id: postingId } });-->
<!--}-->
<!--function goToCreatePosting() {-->
<!--  router.push({ path: '/home/create-posting' });-->
<!--}-->
<!--onMounted(fetchPostings);-->
<!--</script>-->

<!--<style scoped>-->
<!--/* 样式部分保持不变 */-->
<!--.posting-list { min-height: 800px; display: flex; flex-direction: column; align-items: center; gap: 20px; }-->
<!--.posting-card { width: 70%; max-width: 800px; display: flex; align-items: center; gap: 20px; padding: 20px; cursor: pointer; transition: box-shadow 0.3s; box-sizing: border-box; background-color: rgba(255, 255, 255, 0.6); border: 1px solid rgba(255, 255, 255, 0.5); }-->
<!--.posting-card:hover { box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }-->
<!--.posting-image img { width: 150px; height: 100px; object-fit: cover; border-radius: 8px; flex-shrink: 0; }-->
<!--.posting-card h3, .posting-card p { margin: 0; }-->
<!--.bgimage { background-image: url("../../assets/780.jpg"); background-position: center top; background-size: 1500px auto; background-repeat: no-repeat; background-attachment: fixed; background-color: #7b6b4d; min-height: 100vh; }-->
<!--</style>-->
<template>
  <el-main class="posting-list bgimage">
    <el-button
        class="create-posting-button"
        @click="goToCreatePosting"
        v-if="role === 'CUSTOMER'"
    >
      发笔记
    </el-button>

    <div class="sort-group">
      <el-radio-group v-model="sortOption" @change="fetchPostings" class="sort-buttons">
        <el-radio-button label="time">按时间排序</el-radio-button>
        <el-radio-button label="likes">按点赞排序</el-radio-button>
      </el-radio-group>
    </div>

    <div class="cards-container">
      <el-card
          v-for="posting in postings"
          :key="posting.id"
          class="posting-card"
          @click="goToPostingDetail(posting.id)"
      >
        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">
          <img :src="posting.covers[0]" alt="Posting cover" />
        </div>
        <div class="posting-info">
          <h3>{{ posting.title }}</h3>
          <p>点击查看详情</p>
        </div>
      </el-card>
    </div>
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Posting } from '../../api/posting.ts';
import { ElMessage } from 'element-plus';
import { sortByTime, sortByLike } from '../../api/tools.ts'

const postings = ref<Posting[]>([]);
const router = useRouter();
const sortOption = ref<'time' | 'likes'>('time');
const role = sessionStorage.getItem("role");

async function fetchPostings() {
  try {
    let res;
    if (sortOption.value === 'time') {
      res = await sortByTime('post');
    } else {
      res = await sortByLike('post');
    }
    if (res.data.code === '200' && Array.isArray(res.data.data)) {
      postings.value = res.data.data;
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取帖子失败:', error);
    ElMessage.error('获取笔记失败');
  }
}

function goToPostingDetail(postingId: number) {
  router.push({ name: 'PostingDetail', params: { id: postingId } });
}

function goToCreatePosting() {
  router.push({ path: '/home/create-posting' });
}

onMounted(fetchPostings);
</script>
<style scoped>
.posting-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 30px 0;
  background-color: #121212; /* 整个背景黑色 */
}

/* 发笔记按钮 */
.create-posting-button {
  background-color: #4a4a4a; /* 浅灰色背景 */
  color: #ffcc00;            /* 黄色文字 */
  border: 1px solid #666;
  border-radius: 6px;
  padding: 8px 16px;
  transition: background-color 0.3s, transform 0.2s;
}
.create-posting-button:hover {
  background-color: #5a5a5a;
}

/* 排序按钮组 */
.sort-group {
  margin-bottom: 20px;
}

.el-radio-button {
  background-color: #4a4a4a; /* 浅灰色背景 */
  color: #ffcc00;            /* 黄色文字 */
  border: 1px solid #666;
  border-radius: 6px;
  margin-right: 10px;
  transition: background-color 0.3s, color 0.3s;
}

.el-radio-button.is-active {
  background-color: #5a5a5a; /* 激活态稍亮 */
  color: #ffcc00;            /* 保持黄色文字 */
}

.el-radio-button:hover {
  background-color: #5a5a5a;
}

/* 卡片样式保持之前的深色风格 */
.cards-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 90%;
  max-width: 900px;
}

.posting-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px 20px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  background-color: rgba(30, 30, 30, 0.85);
  border-radius: 10px;
  color: #fff;
}

.posting-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.5);
}

.posting-image img {
  width: 180px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.posting-info h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #ffcc00; /* 黄色标题 */
}

.posting-info p {
  margin: 5px 0 0 0;
  color: #ccc;
}

/* 去掉背景图，只保留黑色背景 */
.bgimage {
  background-color: #121212;
  background-image: none;
}
.sort-buttons :deep(.el-radio-button) .el-radio-button__inner {
  background-color: #4a4a4a !important;
  color: #ffcc00 !important;
  border: 1px solid #666 !important;
  box-shadow: none !important;
}

.sort-buttons :deep(.el-radio-button.is-active) .el-radio-button__inner {
  background-color: #5a5a5a !important;
  color: #ffcc00 !important;
  border-color: #666 !important;
  box-shadow: none !important;
}

.sort-buttons :deep(.el-radio-button:hover) .el-radio-button__inner {
  background-color: #5a5a5a !important;
  color: #ffcc00 !important;
}

.sort-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #5a5a5a !important;
  color: #ffcc00 !important;
}

</style>

