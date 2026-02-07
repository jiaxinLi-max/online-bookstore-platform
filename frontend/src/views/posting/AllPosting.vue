<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <el-main class="posting-list bgimage">&ndash;&gt;-->
<!--&lt;!&ndash;    <el-button class="create-posting-button" @click="goToCreatePosting" style="margin-bottom: 20px;" v-if="role === 'CUSTOMER'">&ndash;&gt;-->
<!--&lt;!&ndash;      发笔记&ndash;&gt;-->
<!--&lt;!&ndash;    </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;    <div style="margin-bottom: 20px;">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-radio-group v-model="sortOption" @change="fetchPostings">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="time">按时间排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="likes">按点赞排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-radio-group>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->
<!--&lt;!&ndash;    <el-card&ndash;&gt;-->
<!--&lt;!&ndash;        v-for="posting in postings"&ndash;&gt;-->
<!--&lt;!&ndash;        :key="posting.id"&ndash;&gt;-->
<!--&lt;!&ndash;        class="posting-card"&ndash;&gt;-->
<!--&lt;!&ndash;        @click="goToPostingDetail(posting.id)"&ndash;&gt;-->
<!--&lt;!&ndash;    >&ndash;&gt;-->
<!--&lt;!&ndash;      <div class="posting-image">&ndash;&gt;-->
<!--&lt;!&ndash;        <img :src="Array.isArray(posting.covers) && posting.covers.length > 0 ? posting.covers[0] : ''" alt="Posting covers" />&ndash;&gt;-->
<!--&lt;!&ndash;      </div>&ndash;&gt;-->
<!--&lt;!&ndash;      <h3>{{ posting.title }}</h3>&ndash;&gt;-->
<!--&lt;!&ndash;      <p>点击查看详情</p>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;  </el-main>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script setup lang="ts">&ndash;&gt;-->
<!--&lt;!&ndash;// <script>部分无需修改，保持原样&ndash;&gt;-->
<!--&lt;!&ndash;import { ref, onMounted } from 'vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { useRouter } from 'vue-router';&ndash;&gt;-->
<!--&lt;!&ndash;import { Posting } from '../../api/posting.ts';&ndash;&gt;-->
<!--&lt;!&ndash;import { ElMessage } from "element-plus";&ndash;&gt;-->
<!--&lt;!&ndash;import { sortByTime, sortByLike } from '../../api/tools.ts'&ndash;&gt;-->

<!--&lt;!&ndash;const postings = ref<Posting[]>([]);&ndash;&gt;-->
<!--&lt;!&ndash;const router = useRouter();&ndash;&gt;-->
<!--&lt;!&ndash;const sortOption = ref<'time' | 'likes'>('time');&ndash;&gt;-->
<!--&lt;!&ndash;const role = sessionStorage.getItem("role");&ndash;&gt;-->

<!--&lt;!&ndash;async function fetchPostings() {&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    let res;&ndash;&gt;-->
<!--&lt;!&ndash;    if (sortOption.value === 'time') {&ndash;&gt;-->
<!--&lt;!&ndash;      res = await sortByTime('post');&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      res = await sortByLike('post');&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;    if (res.data.code === '200' && Array.isArray(res.data.data)) {&ndash;&gt;-->
<!--&lt;!&ndash;      postings.value = res.data.data;&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      console.error('获取数据失败：响应格式不符合预期');&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;    console.error('获取帖子失败:', error);&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('获取笔记失败');&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;function goToPostingDetail(postingId: number) {&ndash;&gt;-->
<!--&lt;!&ndash;  router.push({ name: 'PostingDetail', params: { id: postingId } });&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;function goToCreatePosting() {&ndash;&gt;-->
<!--&lt;!&ndash;  router.push({ path: '/home/create-posting' });&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;onMounted(fetchPostings);&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;/* 样式部分保持不变 */&ndash;&gt;-->
<!--&lt;!&ndash;.posting-list { min-height: 800px; display: flex; flex-direction: column; align-items: center; gap: 20px; }&ndash;&gt;-->
<!--&lt;!&ndash;.posting-card { width: 70%; max-width: 800px; display: flex; align-items: center; gap: 20px; padding: 20px; cursor: pointer; transition: box-shadow 0.3s; box-sizing: border-box; background-color: rgba(255, 255, 255, 0.6); border: 1px solid rgba(255, 255, 255, 0.5); }&ndash;&gt;-->
<!--&lt;!&ndash;.posting-card:hover { box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }&ndash;&gt;-->
<!--&lt;!&ndash;.posting-image img { width: 150px; height: 100px; object-fit: cover; border-radius: 8px; flex-shrink: 0; }&ndash;&gt;-->
<!--&lt;!&ndash;.posting-card h3, .posting-card p { margin: 0; }&ndash;&gt;-->
<!--&lt;!&ndash;.bgimage { background-image: url("../../assets/780.jpg"); background-position: center top; background-size: 1500px auto; background-repeat: no-repeat; background-attachment: fixed; background-color: #7b6b4d; min-height: 100vh; }&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <el-main class="posting-list bgimage">&ndash;&gt;-->
<!--&lt;!&ndash;    &lt;!&ndash; 发笔记按钮和排序按钮组保持不变 &ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;    <el-button&ndash;&gt;-->
<!--&lt;!&ndash;        class="create-posting-button"&ndash;&gt;-->
<!--&lt;!&ndash;        @click="goToCreatePosting"&ndash;&gt;-->
<!--&lt;!&ndash;        v-if="role === 'CUSTOMER'"&ndash;&gt;-->
<!--&lt;!&ndash;    >&ndash;&gt;-->
<!--&lt;!&ndash;      发笔记&ndash;&gt;-->
<!--&lt;!&ndash;    </el-button>&ndash;&gt;-->

<!--&lt;!&ndash;    <div class="sort-group">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-radio-group v-model="sortOption" @change="fetchPostings" class="sort-buttons">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="time">按时间排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="likes">按点赞排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-radio-group>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->

<!--&lt;!&ndash;    &lt;!&ndash; ===== 修改开始：这里是卡片容器 ===== &ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;    <div class="cards-container">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-card&ndash;&gt;-->
<!--&lt;!&ndash;          v-for="posting in postings"&ndash;&gt;-->
<!--&lt;!&ndash;          :key="posting.id"&ndash;&gt;-->
<!--&lt;!&ndash;          class="posting-card"&ndash;&gt;-->
<!--&lt;!&ndash;          @click="goToPostingDetail(posting.id)"&ndash;&gt;-->
<!--&lt;!&ndash;          :body-style="{ padding: '0px' }"&ndash;&gt;-->
<!--&lt;!&ndash;      >&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash; 图片部分 &ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">&ndash;&gt;-->
<!--&lt;!&ndash;          <img :src="posting.covers[0]" alt="Posting cover" />&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->

<!--&lt;!&ndash;        &lt;!&ndash; 文字信息部分 &ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="posting-info">&ndash;&gt;-->
<!--&lt;!&ndash;          <h3>{{ posting.title }}</h3>&ndash;&gt;-->
<!--&lt;!&ndash;          <div class="posting-footer">&ndash;&gt;-->
<!--&lt;!&ndash;            &lt;!&ndash; 假设 posting 对象中有 likes 属性 &ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;            <span class="likes-count">&ndash;&gt;-->
<!--&lt;!&ndash;              <svg viewBox="0 0 24 24" width="16" height="16" fill="#ccc"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path></svg>&ndash;&gt;-->
<!--&lt;!&ndash;              {{ posting.like }}&ndash;&gt;-->
<!--&lt;!&ndash;            </span>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->
<!--&lt;!&ndash;    &lt;!&ndash; ===== 修改结束 ===== &ndash;&gt;&ndash;&gt;-->

<!--&lt;!&ndash;  </el-main>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->



<!--<template>-->
<!--  <el-main class="posting-list bgimage">-->
<!--    &lt;!&ndash; 发笔记按钮保持不变 &ndash;&gt;-->
<!--    <el-button-->
<!--        class="create-posting-button"-->
<!--        @click="goToCreatePosting"-->
<!--        v-if="role === 'CUSTOMER'"-->
<!--    >-->
<!--      发笔记-->
<!--    </el-button>-->

<!--    &lt;!&ndash; ===== 修改开始：将排序按钮改为下拉菜单 ===== &ndash;&gt;-->
<!--    <div class="sort-group">-->
<!--      <el-dropdown @command="handleSortCommand" trigger="click">-->
<!--        &lt;!&ndash; 这是新的下拉按钮，文字会根据当前选择动态变化 &ndash;&gt;-->
<!--        <el-button class="sort-dropdown-button">-->
<!--          {{ currentSortText }}-->
<!--          <el-icon class="el-icon&#45;&#45;right"><ArrowDown /></el-icon>-->
<!--        </el-button>-->

<!--        &lt;!&ndash; 这是下拉菜单的内容 &ndash;&gt;-->
<!--        <template #dropdown>-->
<!--          <el-dropdown-menu>-->
<!--            <el-dropdown-item command="time">按时间排序</el-dropdown-item>-->
<!--            <el-dropdown-item command="likes">按点赞排序</el-dropdown-item>-->
<!--          </el-dropdown-menu>-->
<!--        </template>-->
<!--      </el-dropdown>-->
<!--    </div>-->
<!--    &lt;!&ndash; ===== 修改结束 ===== &ndash;&gt;-->


<!--    &lt;!&ndash; 卡片列表部分保持不变 &ndash;&gt;-->
<!--    <div class="cards-container">-->
<!--      <el-card-->
<!--          v-for="posting in postings"-->
<!--          :key="posting.id"-->
<!--          class="posting-card"-->
<!--          @click="goToPostingDetail(posting.id)"-->
<!--          :body-style="{ padding: '0px' }"-->
<!--      >-->
<!--        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">-->
<!--          <img :src="posting.covers[0]" alt="Posting cover" />-->
<!--        </div>-->
<!--        <div class="posting-info">-->
<!--          <h3>{{ posting.title }}</h3>-->
<!--          <div class="posting-footer">-->
<!--            <span class="likes-count">-->
<!--              <svg viewBox="0 0 24 24" width="16" height="16" fill="#ccc"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path></svg>-->
<!--              {{ posting.like }}-->
<!--            </span>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-card>-->
<!--    </div>-->

<!--  </el-main>-->
<!--</template>-->


<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <el-main class="posting-list bgimage">&ndash;&gt;-->
<!--&lt;!&ndash;    <el-button&ndash;&gt;-->
<!--&lt;!&ndash;        class="create-posting-button"&ndash;&gt;-->
<!--&lt;!&ndash;        @click="goToCreatePosting"&ndash;&gt;-->
<!--&lt;!&ndash;        v-if="role === 'CUSTOMER'"&ndash;&gt;-->
<!--&lt;!&ndash;    >&ndash;&gt;-->
<!--&lt;!&ndash;      发笔记&ndash;&gt;-->
<!--&lt;!&ndash;    </el-button>&ndash;&gt;-->

<!--&lt;!&ndash;    <div class="sort-group">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-radio-group v-model="sortOption" @change="fetchPostings" class="sort-buttons">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="time">按时间排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-radio-button label="likes">按点赞排序</el-radio-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-radio-group>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->

<!--&lt;!&ndash;    <div class="cards-container">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-card&ndash;&gt;-->
<!--&lt;!&ndash;          v-for="posting in postings"&ndash;&gt;-->
<!--&lt;!&ndash;          :key="posting.id"&ndash;&gt;-->
<!--&lt;!&ndash;          class="posting-card"&ndash;&gt;-->
<!--&lt;!&ndash;          @click="goToPostingDetail(posting.id)"&ndash;&gt;-->
<!--&lt;!&ndash;      >&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">&ndash;&gt;-->
<!--&lt;!&ndash;          <img :src="posting.covers[0]" alt="Posting cover" />&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="posting-info">&ndash;&gt;-->
<!--&lt;!&ndash;          <h3>{{ posting.title }}</h3>&ndash;&gt;-->
<!--&lt;!&ndash;          <p>点击查看详情</p>&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->
<!--&lt;!&ndash;  </el-main>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--<script setup lang="ts">-->

<!--import { useRouter } from 'vue-router';-->
<!--import { Posting } from '../../api/posting.ts';-->
<!--import { ElMessage } from 'element-plus';-->
<!--import { sortByTime, sortByLike } from '../../api/tools.ts'-->
<!--import { ref, onMounted, computed } from 'vue';-->
<!--import { ArrowDown } from '@element-plus/icons-vue';-->


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
<!--// ===== 修改开始：新增两个部分 =====-->

<!--// 1. 新增一个处理下拉菜单点击事件的函数-->
<!--const handleSortCommand = (command: 'time' | 'likes') => {-->
<!--  // 如果点击的不是当前已选的排序方式，则更新并重新获取数据-->
<!--  if (sortOption.value !== command) {-->
<!--    sortOption.value = command;-->
<!--    fetchPostings();-->
<!--  }-->
<!--};-->

<!--// 2. 新增一个计算属性，用于动态显示按钮上的文字-->
<!--const currentSortText = computed(() => {-->
<!--  return sortOption.value === 'time' ? '按时间排序' : '按点赞排序';-->
<!--});-->

<!--// ===== 修改结束 =====-->
<!--onMounted(fetchPostings);-->
<!--</script>-->
<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.posting-list {&ndash;&gt;-->
<!--&lt;!&ndash;  min-height: 100vh;&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-direction: column;&ndash;&gt;-->
<!--&lt;!&ndash;  align-items: center;&ndash;&gt;-->
<!--&lt;!&ndash;  gap: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 30px 0;&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #121212; /* 整个背景黑色 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;/* 发笔记按钮 */&ndash;&gt;-->
<!--&lt;!&ndash;.create-posting-button {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #4a4a4a; /* 浅灰色背景 */&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00;            /* 黄色文字 */&ndash;&gt;-->
<!--&lt;!&ndash;  border: 1px solid #666;&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 6px;&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 8px 16px;&ndash;&gt;-->
<!--&lt;!&ndash;  transition: background-color 0.3s, transform 0.2s;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;.create-posting-button:hover {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;/* 排序按钮组 */&ndash;&gt;-->
<!--&lt;!&ndash;.sort-group {&ndash;&gt;-->
<!--&lt;!&ndash;  margin-bottom: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-radio-button {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #4a4a4a; /* 浅灰色背景 */&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00;            /* 黄色文字 */&ndash;&gt;-->
<!--&lt;!&ndash;  border: 1px solid #666;&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 6px;&ndash;&gt;-->
<!--&lt;!&ndash;  margin-right: 10px;&ndash;&gt;-->
<!--&lt;!&ndash;  transition: background-color 0.3s, color 0.3s;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-radio-button.is-active {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a; /* 激活态稍亮 */&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00;            /* 保持黄色文字 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-radio-button:hover {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;/* 卡片样式保持之前的深色风格 */&ndash;&gt;-->
<!--&lt;!&ndash;.cards-container {&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-direction: column;&ndash;&gt;-->
<!--&lt;!&ndash;  gap: 15px;&ndash;&gt;-->
<!--&lt;!&ndash;  width: 90%;&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 900px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.posting-card {&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  align-items: center;&ndash;&gt;-->
<!--&lt;!&ndash;  gap: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 15px 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  cursor: pointer;&ndash;&gt;-->
<!--&lt;!&ndash;  transition: transform 0.3s, box-shadow 0.3s;&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: rgba(30, 30, 30, 0.85);&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 10px;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #fff;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.posting-card:hover {&ndash;&gt;-->
<!--&lt;!&ndash;  transform: translateY(-5px);&ndash;&gt;-->
<!--&lt;!&ndash;  box-shadow: 0 6px 15px rgba(0,0,0,0.5);&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.posting-image img {&ndash;&gt;-->
<!--&lt;!&ndash;  width: 180px;&ndash;&gt;-->
<!--&lt;!&ndash;  height: 120px;&ndash;&gt;-->
<!--&lt;!&ndash;  object-fit: cover;&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 8px;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-shrink: 0;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.posting-info h3 {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 0;&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: 600;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00; /* 黄色标题 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.posting-info p {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 5px 0 0 0;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ccc;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;/* 去掉背景图，只保留黑色背景 */&ndash;&gt;-->
<!--&lt;!&ndash;.bgimage {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #121212;&ndash;&gt;-->
<!--&lt;!&ndash;  background-image: none;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;.sort-buttons :deep(.el-radio-button) .el-radio-button__inner {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #4a4a4a !important;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00 !important;&ndash;&gt;-->
<!--&lt;!&ndash;  border: 1px solid #666 !important;&ndash;&gt;-->
<!--&lt;!&ndash;  box-shadow: none !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.sort-buttons :deep(.el-radio-button.is-active) .el-radio-button__inner {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a !important;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00 !important;&ndash;&gt;-->
<!--&lt;!&ndash;  border-color: #666 !important;&ndash;&gt;-->
<!--&lt;!&ndash;  box-shadow: none !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.sort-buttons :deep(.el-radio-button:hover) .el-radio-button__inner {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a !important;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00 !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.sort-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5a5a5a !important;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffcc00 !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;</style>&ndash;&gt;-->

<!--<style scoped>-->
<!--/* 整体布局和按钮样式保持不变 */-->
<!--.posting-list {-->
<!--  min-height: 100vh;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--  gap: 20px;-->
<!--  padding: 30px 20px; /* 左右增加一些边距 */-->
<!--  background-color: #121212;-->
<!--}-->
<!--.create-posting-button, .sort-group {-->
<!--  margin-bottom: 20px;-->
<!--}-->
<!--.sort-buttons :deep(.el-radio-button) .el-radio-button__inner { background-color: #4a4a4a !important; color: #ffcc00 !important; border: 1px solid #666 !important; box-shadow: none !important; }-->
<!--.sort-buttons :deep(.el-radio-button.is-active) .el-radio-button__inner,-->
<!--.sort-buttons :deep(.el-radio-button:hover) .el-radio-button__inner,-->
<!--.sort-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) { background-color: #5a5a5a !important; color: #ffcc00 !important; }-->


<!--/* ===== 修改开始：卡片容器和卡片样式 ===== */-->

<!--/* 1. 卡片容器改为网格布局 */-->
<!--.cards-container {-->
<!--  width: 100%;-->
<!--  max-width: 1200px; /* 您可以根据需要调整最大宽度 */-->
<!--  display: grid;-->
<!--  /*-->
<!--   * 创建一个响应式网格：-->
<!--   * - auto-fill: 自动填充尽可能多的列。-->
<!--   * - minmax(250px, 1fr): 每列最小宽度250px，最大平分剩余空间。-->
<!--   */-->
<!--  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));-->
<!--  gap: 20px; /* 卡片之间的间距 */-->
<!--}-->

<!--/* 2. 单个卡片的样式调整 */-->
<!--.posting-card {-->
<!--  cursor: pointer;-->
<!--  border-radius: 12px;-->
<!--  /* 边框颜色可以稍微调深一点，以在浅色背景上更明显 */-->
<!--  border: 1px solid rgba(255, 255, 255, 0.2);-->
<!--  overflow: hidden;-->
<!--  transition: transform 0.3s, box-shadow 0.3s, background-color 0.3s;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->

<!--  /*-->
<!--   * 核心修改：使用半透明的白色/浅灰色-->
<!--   * rgba(255, 255, 255, 0.15) 表示白色，透明度为 15%-->
<!--   * 这样就会形成一层朦胧的、有点白的灰色效果-->
<!--   * 您可以调整最后一个值 (0.15) 来改变明亮/透明程度-->
<!--   * 比如 0.1 更透明，0.25 更不透明/更白-->
<!--   */-->
<!--  background-color: rgba(255, 255, 255, 0.15);-->

<!--  /* 毛玻璃效果在浅色透明背景上效果依然出色 */-->
<!--  backdrop-filter: blur(15px); /* 可以适当增强模糊半径 */-->
<!--  -webkit-backdrop-filter: blur(15px);-->
<!--}-->

<!--.posting-card:hover {-->
<!--  transform: translateY(-5px);-->
<!--  box-shadow: 0 8px 20px rgba(0,0,0,0.6);-->
<!--  /* 鼠标悬停时，背景稍微变亮一些 */-->
<!--  background-color: rgba(0, 0, 0, 0.6);-->
<!--}-->

<!--/* 3. 卡片图片样式 */-->
<!--.posting-image img {-->
<!--  width: 100%;-->
<!--  height: auto; /* 高度自适应，保持图片原始比例 */-->
<!--  display: block; /* 消除图片下方的微小间隙 */-->
<!--  aspect-ratio: 3 / 4; /* 您可以调整这个比例，比如 1/1 正方形， 16/9 宽屏 */-->
<!--  object-fit: cover; /* 保证图片填满容器，可能会裁剪 */-->
<!--}-->

<!--/* 4. 卡片文字信息区域 */-->
<!--.posting-info {-->
<!--  padding: 12px 15px;-->
<!--  color: #fff;-->
<!--}-->

<!--.posting-info h3 {-->
<!--  margin: 0 0 8px 0;-->
<!--  font-size: 16px;-->
<!--  font-weight: 600;-->
<!--  color: #ffcc00; /* 保持黄色标题 */-->
<!--  /* 防止标题过长换行破坏布局 */-->
<!--  white-space: nowrap;-->
<!--  overflow: hidden;-->
<!--  text-overflow: ellipsis;-->
<!--}-->

<!--/* 5. 新增的点赞区域样式 */-->
<!--.posting-footer {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: flex-start; /* 从左侧开始 */-->
<!--}-->

<!--.likes-count {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 6px; /* 图标和数字的间距 */-->
<!--  font-size: 14px;-->
<!--  color: #ccc;-->
<!--}-->
<!--/* ===== 修改结束 ===== */-->
<!--.sort-group {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.sort-dropdown-button {-->
<!--  background-color: #4a4a4a;-->
<!--  color: #ffcc00;-->
<!--  border: 1px solid #666;-->
<!--  padding: 8px 16px;-->
<!--  border-radius: 6px;-->
<!--}-->

<!--.sort-dropdown-button:hover,-->
<!--.sort-dropdown-button:focus {-->
<!--  background-color: #5a5a5a;-->
<!--  color: #ffcc00;-->
<!--  border-color: #666;-->
<!--}-->

<!--/*-->
<!-- * 使用 :deep() 或 :global() 来确保下拉菜单面板的样式也能生效-->
<!-- * 因为它在DOM中可能不是当前组件的直接子元素-->
<!-- */-->
<!--:deep(.el-dropdown-menu) {-->
<!--  background-color: #2d2d2d !important; /* 下拉菜单背景色 */-->
<!--  border: 1px solid #666 !important;-->
<!--}-->

<!--:deep(.el-dropdown-menu__item) {-->
<!--  color: #ccc !important; /* 菜单项文字颜色 */-->
<!--}-->

<!--:deep(.el-dropdown-menu__item:hover) {-->
<!--  background-color: #5a5a5a !important; /* 悬停背景色 */-->
<!--  color: #ffcc00 !important; /* 悬停文字颜色 */-->
<!--}-->
<!--</style>-->
<!--<template>-->
<!--  <el-main class="bgimage">-->
<!--    &lt;!&ndash; 顶部操作栏：包含发笔记按钮和排序下拉 &ndash;&gt;-->
<!--    <div class="page-header">-->
<!--      <div class="header-left">-->
<!--        <h2 class="page-title">社区笔记</h2>-->
<!--        <span class="page-subtitle">分享发现生活之美</span>-->
<!--      </div>-->

<!--      <div class="header-right">-->
<!--        &lt;!&ndash; 发笔记按钮 &ndash;&gt;-->
<!--        <el-button-->
<!--            class="create-btn"-->
<!--            type="primary"-->
<!--            round-->
<!--            @click="goToCreatePosting"-->
<!--            v-if="role === 'CUSTOMER'"-->
<!--        >-->
<!--          <el-icon style="margin-right: 4px"><Edit /></el-icon>-->
<!--          发笔记-->
<!--        </el-button>-->

<!--        &lt;!&ndash; 排序下拉 &ndash;&gt;-->
<!--        <el-dropdown @command="handleSortCommand" trigger="click">-->
<!--          <el-button class="sort-btn" round>-->
<!--            {{ currentSortText }}-->
<!--            <el-icon class="el-icon&#45;&#45;right"><ArrowDown /></el-icon>-->
<!--          </el-button>-->
<!--          <template #dropdown>-->
<!--            <el-dropdown-menu>-->
<!--              <el-dropdown-item command="time">按时间排序</el-dropdown-item>-->
<!--              <el-dropdown-item command="likes">按点赞排序</el-dropdown-item>-->
<!--            </el-dropdown-menu>-->
<!--          </template>-->
<!--        </el-dropdown>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; 笔记卡片列表容器 &ndash;&gt;-->
<!--    <div class="cards-container">-->
<!--      <div-->
<!--          v-for="posting in postings"-->
<!--          :key="posting.id"-->
<!--          class="posting-card"-->
<!--          @click="goToPostingDetail(posting.id)"-->
<!--      >-->
<!--        &lt;!&ndash; 图片区域 &ndash;&gt;-->
<!--        <div class="card-image-box">-->
<!--          <img-->
<!--              :src="(Array.isArray(posting.covers) && posting.covers.length > 0) ? posting.covers[0] : ''"-->
<!--              alt="Posting cover"-->
<!--              loading="lazy"-->
<!--          />-->
<!--        </div>-->

<!--        &lt;!&ndash; 信息区域 &ndash;&gt;-->
<!--        <div class="card-info">-->
<!--          <h3 class="posting-title">{{ posting.title }}</h3>-->

<!--          <div class="posting-footer">-->
<!--            &lt;!&ndash; 这里可以加作者头像，如果有的话，目前只显示点赞 &ndash;&gt;-->
<!--            <div class="likes-wrapper">-->
<!--              &lt;!&ndash; 爱心图标 &ndash;&gt;-->
<!--              <svg viewBox="0 0 24 24" width="16" height="16" :fill="posting.like > 0 ? '#f56c6c' : '#999'">-->
<!--                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>-->
<!--              </svg>-->
<!--              <span class="likes-count">{{ posting.like }}</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; 空状态提示 (可选) &ndash;&gt;-->
<!--    <el-empty v-if="!postings.length" description="暂无笔记，快来发布第一条吧！" />-->

<!--  </el-main>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { useRouter } from 'vue-router';-->
<!--import { Posting } from '../../api/posting.ts';-->
<!--import { ElMessage } from 'element-plus';-->
<!--import { sortByTime, sortByLike } from '../../api/tools.ts'-->
<!--import { ref, onMounted, computed } from 'vue';-->
<!--import { ArrowDown, Edit } from '@element-plus/icons-vue';-->

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

<!--const handleSortCommand = (command: 'time' | 'likes') => {-->
<!--  if (sortOption.value !== command) {-->
<!--    sortOption.value = command;-->
<!--    fetchPostings();-->
<!--  }-->
<!--};-->

<!--const currentSortText = computed(() => {-->
<!--  return sortOption.value === 'time' ? '最新发布' : '最多点赞';-->
<!--});-->

<!--onMounted(fetchPostings);-->
<!--</script>-->

<!--<style scoped>-->
<!--/* ======== 基础背景 (与商品页一致) ======== */-->
<!--.bgimage {-->
<!--  background: #f0f2f5;-->
<!--  min-height: 100vh;-->
<!--  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;-->
<!--  padding: 0 0 60px 0;-->
<!--}-->

<!--/* ======== 顶部 Header 区域 ======== */-->
<!--.page-header {-->
<!--  max-width: 1440px;-->
<!--  margin: 20px auto 30px; /* 上下边距 */-->
<!--  padding: 0 20px;-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--}-->

<!--.header-left {-->
<!--  display: flex;-->
<!--  align-items: baseline;-->
<!--  gap: 12px;-->
<!--}-->

<!--.page-title {-->
<!--  font-size: 24px;-->
<!--  color: #333;-->
<!--  margin: 0;-->
<!--  font-weight: 700;-->
<!--}-->

<!--.page-subtitle {-->
<!--  font-size: 14px;-->
<!--  color: #999;-->
<!--}-->

<!--.header-right {-->
<!--  display: flex;-->
<!--  gap: 16px;-->
<!--  align-items: center;-->
<!--}-->

<!--/* 按钮样式优化 */-->
<!--.create-btn {-->
<!--  background: linear-gradient(90deg, #409eff, #337ecc);-->
<!--  border: none;-->
<!--  font-weight: 600;-->
<!--  box-shadow: 0 4px 12px rgba(64,158,255,0.3);-->
<!--  transition: transform 0.2s;-->
<!--}-->
<!--.create-btn:hover {-->
<!--  transform: translateY(-2px);-->
<!--  box-shadow: 0 6px 16px rgba(64,158,255,0.4);-->
<!--}-->

<!--.sort-btn {-->
<!--  background: #fff;-->
<!--  color: #606266;-->
<!--  border: 1px solid #dcdfe6;-->
<!--}-->
<!--.sort-btn:hover {-->
<!--  color: #409eff;-->
<!--  border-color: #c6e2ff;-->
<!--  background-color: #ecf5ff;-->
<!--}-->

<!--/* ======== 网格布局 (核心) ======== */-->
<!--.cards-container {-->
<!--  max-width: 1440px;-->
<!--  margin: 0 auto;-->
<!--  padding: 0 20px;-->
<!--  display: grid;-->
<!--  /*-->
<!--   * 类似商品页，但笔记通常需要稍微宽一点点展示标题-->
<!--   * 这里设置最小宽度 220px-->
<!--   */-->
<!--  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));-->
<!--  gap: 24px;-->
<!--}-->

<!--/* ======== 笔记卡片样式 (类似商品卡片) ======== */-->
<!--.posting-card {-->
<!--  background: #fff;-->
<!--  border-radius: 12px;-->
<!--  overflow: hidden;-->
<!--  cursor: pointer;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  transition: all 0.3s ease;-->
<!--  border: 1px solid transparent;-->
<!--  position: relative;-->
<!--}-->

<!--.posting-card:hover {-->
<!--  transform: translateY(-6px);-->
<!--  box-shadow: 0 12px 24px rgba(0,0,0,0.08); /* 柔和的阴影 */-->
<!--  border-color: #e0e0e0;-->
<!--}-->

<!--/* 图片容器 */-->
<!--.card-image-box {-->
<!--  width: 100%;-->
<!--  aspect-ratio: 3 / 4; /* 保持您喜欢的比例 */-->
<!--  background: #f9f9f9;-->
<!--  overflow: hidden;-->
<!--  position: relative;-->
<!--}-->

<!--.card-image-box img {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  object-fit: cover;-->
<!--  transition: transform 0.5s ease;-->
<!--}-->

<!--.posting-card:hover .card-image-box img {-->
<!--  transform: scale(1.08); /* 悬停放大效果 */-->
<!--}-->

<!--/* 内容区域 */-->
<!--.card-info {-->
<!--  padding: 14px;-->
<!--  flex: 1;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  justify-content: space-between;-->
<!--}-->

<!--.posting-title {-->
<!--  font-size: 16px;-->
<!--  font-weight: 600;-->
<!--  color: #333;-->
<!--  margin: 0 0 10px 0;-->
<!--  line-height: 1.5;-->

<!--  /* 多行省略 */-->
<!--  display: -webkit-box;-->
<!--  -webkit-line-clamp: 2;-->
<!--  -webkit-box-orient: vertical;-->
<!--  overflow: hidden;-->
<!--}-->

<!--/* 底部点赞区 */-->
<!--.posting-footer {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: space-between;-->
<!--  margin-top: auto;-->
<!--}-->

<!--.likes-wrapper {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 6px;-->
<!--  color: #999;-->
<!--  font-size: 13px;-->
<!--}-->

<!--.likes-count {-->
<!--  font-weight: 500;-->
<!--  color: #666;-->
<!--}-->

<!--/* Element Dropdown 样式覆盖 (如果有需要) */-->
<!--:deep(.el-dropdown-menu__item:not(.is-disabled):focus) {-->
<!--  background-color: #ecf5ff;-->
<!--  color: #409eff;-->
<!--}-->
<!--</style>-->
<template>
  <el-main class="bgimage">
    <!-- 顶部操作栏 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">社区笔记</h2>
        <span class="page-subtitle">分享发现生活之美</span>
      </div>

      <div class="header-right">
        <!-- 发笔记按钮：黑金风格 -->
        <el-button
            class="create-btn"
            type="primary"
            round
            @click="goToCreatePosting"
            v-if="role === 'CUSTOMER'"
        >
          <el-icon style="margin-right: 4px"><Edit /></el-icon>
          发笔记
        </el-button>

        <!-- 排序下拉：黑金风格 -->
        <el-dropdown @command="handleSortCommand" trigger="click" popper-class="custom-dropdown">
          <el-button class="sort-btn" round>
            {{ currentSortText }}
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="time">按时间排序</el-dropdown-item>
              <el-dropdown-item command="likes">按点赞排序</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 笔记卡片列表容器 -->
    <div class="cards-container">
      <div
          v-for="posting in postings"
          :key="posting.id"
          class="posting-card"
          @click="goToPostingDetail(posting.id)"
      >
        <!-- 图片区域 -->
        <div class="card-image-box">
          <img
              :src="(Array.isArray(posting.covers) && posting.covers.length > 0) ? posting.covers[0] : ''"
              alt="Posting cover"
              loading="lazy"
          />
        </div>

        <!-- 信息区域 -->
        <div class="card-info">
          <h3 class="posting-title">{{ posting.title }}</h3>

          <div class="posting-footer">
            <div class="likes-wrapper">
              <!-- 爱心图标 -->
              <svg viewBox="0 0 24 24" width="16" height="16" :fill="posting.like > 0 ? '#d4af37' : '#999'">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>
              </svg>
              <span class="likes-count">{{ posting.like }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态提示 -->
    <el-empty v-if="!postings.length" description="暂无笔记，快来发布第一条吧！" />
  </el-main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { Posting } from '../../api/posting.ts';
import { ElMessage } from 'element-plus';
import { sortByTime, sortByLike } from '../../api/tools.ts'
import { ref, onMounted, computed } from 'vue';
import { ArrowDown, Edit } from '@element-plus/icons-vue';

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

const handleSortCommand = (command: 'time' | 'likes') => {
  if (sortOption.value !== command) {
    sortOption.value = command;
    fetchPostings();
  }
};

const currentSortText = computed(() => {
  return sortOption.value === 'time' ? '最新发布' : '最多点赞';
});

onMounted(fetchPostings);
</script>

<style scoped>
/* ======== 基础背景 ======== */
.bgimage {
  background: #f0f2f5;
  min-height: 100vh;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  padding: 0 0 60px 0;
}

/* ======== 顶部 Header 区域 ======== */
.page-header {
  max-width: 1440px;
  margin: 20px auto 30px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.page-title {
  font-size: 24px;
  color: #2c2c2c; /* 深灰标题 */
  margin: 0;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.page-subtitle {
  font-size: 14px;
  color: #999;
}

.header-right {
  display: flex;
  gap: 16px;
  align-items: center;
}

/* ======== 按钮样式优化 (黑金风格) ======== */

/* 1. 发笔记按钮 */
.create-btn {
  /* 背景改为深炭灰/黑色，呼应Header */
  background: #2c2c2c;
  border: 1px solid #2c2c2c;
  /* 文字使用淡金色 */
  color: #e0c89a;
  font-weight: 600;
  transition: all 0.3s ease;
}

.create-btn:hover {
  /* 悬停时背景稍微变亮 */
  background: #454545;
  border-color: #454545;
  color: #fff; /* 文字变白，增加对比 */
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

/* 2. 排序按钮 */
.sort-btn {
  background: #fff;
  color: #2c2c2c;
  border: 1px solid #dcdfe6;
  font-weight: 500;
  transition: all 0.3s ease;
}

.sort-btn:hover, .sort-btn:focus {
  /* 悬停时反转：黑底金字 */
  background-color: #2c2c2c;
  color: #e0c89a;
  border-color: #2c2c2c;
}

/* ======== 网格布局 ======== */
.cards-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
}

/* ======== 笔记卡片样式 ======== */
.posting-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  position: relative;
  /* 增加一点初始阴影 */
  box-shadow: 0 2px 8px rgba(0,0,0,0.02);
}

.posting-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.08);
  /* 悬停时边框微亮 */
  border-color: #e0e0e0;
}

/* 图片容器 */
.card-image-box {
  width: 100%;
  aspect-ratio: 3 / 4;
  background: #f9f9f9;
  overflow: hidden;
  position: relative;
}

.card-image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.posting-card:hover .card-image-box img {
  transform: scale(1.08);
}

/* 内容区域 */
.card-info {
  padding: 14px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.posting-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 底部点赞区 */
.posting-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
}

.likes-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #999;
  font-size: 13px;
}

.likes-count {
  font-weight: 500;
  color: #666;
}

/* ======== 覆盖 Dropdown 下拉菜单样式 ======== */
/* 注意：因为 dropdown 挂载在 body 上，需要用全局样式或深度选择器 */
:deep(.el-dropdown-menu__item:not(.is-disabled):focus),
:deep(.el-dropdown-menu__item:hover) {
  /* 悬停背景改为浅灰色，文字改为深色，不再是蓝色 */
  background-color: #f5f5f5 !important;
  color: #2c2c2c !important;
}
</style>