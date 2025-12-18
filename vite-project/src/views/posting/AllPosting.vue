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
<!--<template>-->
<!--  <el-main class="posting-list bgimage">-->
<!--    &lt;!&ndash; 发笔记按钮和排序按钮组保持不变 &ndash;&gt;-->
<!--    <el-button-->
<!--        class="create-posting-button"-->
<!--        @click="goToCreatePosting"-->
<!--        v-if="role === 'CUSTOMER'"-->
<!--    >-->
<!--      发笔记-->
<!--    </el-button>-->

<!--    <div class="sort-group">-->
<!--      <el-radio-group v-model="sortOption" @change="fetchPostings" class="sort-buttons">-->
<!--        <el-radio-button label="time">按时间排序</el-radio-button>-->
<!--        <el-radio-button label="likes">按点赞排序</el-radio-button>-->
<!--      </el-radio-group>-->
<!--    </div>-->

<!--    &lt;!&ndash; ===== 修改开始：这里是卡片容器 ===== &ndash;&gt;-->
<!--    <div class="cards-container">-->
<!--      <el-card-->
<!--          v-for="posting in postings"-->
<!--          :key="posting.id"-->
<!--          class="posting-card"-->
<!--          @click="goToPostingDetail(posting.id)"-->
<!--          :body-style="{ padding: '0px' }"-->
<!--      >-->
<!--        &lt;!&ndash; 图片部分 &ndash;&gt;-->
<!--        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">-->
<!--          <img :src="posting.covers[0]" alt="Posting cover" />-->
<!--        </div>-->

<!--        &lt;!&ndash; 文字信息部分 &ndash;&gt;-->
<!--        <div class="posting-info">-->
<!--          <h3>{{ posting.title }}</h3>-->
<!--          <div class="posting-footer">-->
<!--            &lt;!&ndash; 假设 posting 对象中有 likes 属性 &ndash;&gt;-->
<!--            <span class="likes-count">-->
<!--              <svg viewBox="0 0 24 24" width="16" height="16" fill="#ccc"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path></svg>-->
<!--              {{ posting.like }}-->
<!--            </span>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-card>-->
<!--    </div>-->
<!--    &lt;!&ndash; ===== 修改结束 ===== &ndash;&gt;-->

<!--  </el-main>-->
<!--</template>-->



<template>
  <el-main class="posting-list bgimage">
    <!-- 发笔记按钮保持不变 -->
    <el-button
        class="create-posting-button"
        @click="goToCreatePosting"
        v-if="role === 'CUSTOMER'"
    >
      发笔记
    </el-button>

    <!-- ===== 修改开始：将排序按钮改为下拉菜单 ===== -->
    <div class="sort-group">
      <el-dropdown @command="handleSortCommand" trigger="click">
        <!-- 这是新的下拉按钮，文字会根据当前选择动态变化 -->
        <el-button class="sort-dropdown-button">
          {{ currentSortText }}
          <el-icon class="el-icon--right"><ArrowDown /></el-icon>
        </el-button>

        <!-- 这是下拉菜单的内容 -->
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="time">按时间排序</el-dropdown-item>
            <el-dropdown-item command="likes">按点赞排序</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <!-- ===== 修改结束 ===== -->


    <!-- 卡片列表部分保持不变 -->
    <div class="cards-container">
      <el-card
          v-for="posting in postings"
          :key="posting.id"
          class="posting-card"
          @click="goToPostingDetail(posting.id)"
          :body-style="{ padding: '0px' }"
      >
        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">
          <img :src="posting.covers[0]" alt="Posting cover" />
        </div>
        <div class="posting-info">
          <h3>{{ posting.title }}</h3>
          <div class="posting-footer">
            <span class="likes-count">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="#ccc"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path></svg>
              {{ posting.like }}
            </span>
          </div>
        </div>
      </el-card>
    </div>

  </el-main>
</template>


<!--<template>-->
<!--  <el-main class="posting-list bgimage">-->
<!--    <el-button-->
<!--        class="create-posting-button"-->
<!--        @click="goToCreatePosting"-->
<!--        v-if="role === 'CUSTOMER'"-->
<!--    >-->
<!--      发笔记-->
<!--    </el-button>-->

<!--    <div class="sort-group">-->
<!--      <el-radio-group v-model="sortOption" @change="fetchPostings" class="sort-buttons">-->
<!--        <el-radio-button label="time">按时间排序</el-radio-button>-->
<!--        <el-radio-button label="likes">按点赞排序</el-radio-button>-->
<!--      </el-radio-group>-->
<!--    </div>-->

<!--    <div class="cards-container">-->
<!--      <el-card-->
<!--          v-for="posting in postings"-->
<!--          :key="posting.id"-->
<!--          class="posting-card"-->
<!--          @click="goToPostingDetail(posting.id)"-->
<!--      >-->
<!--        <div class="posting-image" v-if="Array.isArray(posting.covers) && posting.covers.length > 0">-->
<!--          <img :src="posting.covers[0]" alt="Posting cover" />-->
<!--        </div>-->
<!--        <div class="posting-info">-->
<!--          <h3>{{ posting.title }}</h3>-->
<!--          <p>点击查看详情</p>-->
<!--        </div>-->
<!--      </el-card>-->
<!--    </div>-->
<!--  </el-main>-->
<!--</template>-->

<script setup lang="ts">

import { useRouter } from 'vue-router';
import { Posting } from '../../api/posting.ts';
import { ElMessage } from 'element-plus';
import { sortByTime, sortByLike } from '../../api/tools.ts'
import { ref, onMounted, computed } from 'vue';
import { ArrowDown } from '@element-plus/icons-vue';


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
// ===== 修改开始：新增两个部分 =====

// 1. 新增一个处理下拉菜单点击事件的函数
const handleSortCommand = (command: 'time' | 'likes') => {
  // 如果点击的不是当前已选的排序方式，则更新并重新获取数据
  if (sortOption.value !== command) {
    sortOption.value = command;
    fetchPostings();
  }
};

// 2. 新增一个计算属性，用于动态显示按钮上的文字
const currentSortText = computed(() => {
  return sortOption.value === 'time' ? '按时间排序' : '按点赞排序';
});

// ===== 修改结束 =====
onMounted(fetchPostings);
</script>
<!--<style scoped>-->
<!--.posting-list {-->
<!--  min-height: 100vh;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--  gap: 20px;-->
<!--  padding: 30px 0;-->
<!--  background-color: #121212; /* 整个背景黑色 */-->
<!--}-->

<!--/* 发笔记按钮 */-->
<!--.create-posting-button {-->
<!--  background-color: #4a4a4a; /* 浅灰色背景 */-->
<!--  color: #ffcc00;            /* 黄色文字 */-->
<!--  border: 1px solid #666;-->
<!--  border-radius: 6px;-->
<!--  padding: 8px 16px;-->
<!--  transition: background-color 0.3s, transform 0.2s;-->
<!--}-->
<!--.create-posting-button:hover {-->
<!--  background-color: #5a5a5a;-->
<!--}-->

<!--/* 排序按钮组 */-->
<!--.sort-group {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.el-radio-button {-->
<!--  background-color: #4a4a4a; /* 浅灰色背景 */-->
<!--  color: #ffcc00;            /* 黄色文字 */-->
<!--  border: 1px solid #666;-->
<!--  border-radius: 6px;-->
<!--  margin-right: 10px;-->
<!--  transition: background-color 0.3s, color 0.3s;-->
<!--}-->

<!--.el-radio-button.is-active {-->
<!--  background-color: #5a5a5a; /* 激活态稍亮 */-->
<!--  color: #ffcc00;            /* 保持黄色文字 */-->
<!--}-->

<!--.el-radio-button:hover {-->
<!--  background-color: #5a5a5a;-->
<!--}-->

<!--/* 卡片样式保持之前的深色风格 */-->
<!--.cards-container {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 15px;-->
<!--  width: 90%;-->
<!--  max-width: 900px;-->
<!--}-->

<!--.posting-card {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 20px;-->
<!--  padding: 15px 20px;-->
<!--  cursor: pointer;-->
<!--  transition: transform 0.3s, box-shadow 0.3s;-->
<!--  background-color: rgba(30, 30, 30, 0.85);-->
<!--  border-radius: 10px;-->
<!--  color: #fff;-->
<!--}-->

<!--.posting-card:hover {-->
<!--  transform: translateY(-5px);-->
<!--  box-shadow: 0 6px 15px rgba(0,0,0,0.5);-->
<!--}-->

<!--.posting-image img {-->
<!--  width: 180px;-->
<!--  height: 120px;-->
<!--  object-fit: cover;-->
<!--  border-radius: 8px;-->
<!--  flex-shrink: 0;-->
<!--}-->

<!--.posting-info h3 {-->
<!--  margin: 0;-->
<!--  font-size: 20px;-->
<!--  font-weight: 600;-->
<!--  color: #ffcc00; /* 黄色标题 */-->
<!--}-->

<!--.posting-info p {-->
<!--  margin: 5px 0 0 0;-->
<!--  color: #ccc;-->
<!--}-->

<!--/* 去掉背景图，只保留黑色背景 */-->
<!--.bgimage {-->
<!--  background-color: #121212;-->
<!--  background-image: none;-->
<!--}-->
<!--.sort-buttons :deep(.el-radio-button) .el-radio-button__inner {-->
<!--  background-color: #4a4a4a !important;-->
<!--  color: #ffcc00 !important;-->
<!--  border: 1px solid #666 !important;-->
<!--  box-shadow: none !important;-->
<!--}-->

<!--.sort-buttons :deep(.el-radio-button.is-active) .el-radio-button__inner {-->
<!--  background-color: #5a5a5a !important;-->
<!--  color: #ffcc00 !important;-->
<!--  border-color: #666 !important;-->
<!--  box-shadow: none !important;-->
<!--}-->

<!--.sort-buttons :deep(.el-radio-button:hover) .el-radio-button__inner {-->
<!--  background-color: #5a5a5a !important;-->
<!--  color: #ffcc00 !important;-->
<!--}-->

<!--.sort-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {-->
<!--  background-color: #5a5a5a !important;-->
<!--  color: #ffcc00 !important;-->
<!--}-->

<!--</style>-->

<style scoped>
/* 整体布局和按钮样式保持不变 */
.posting-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 30px 20px; /* 左右增加一些边距 */
  background-color: #121212;
}
.create-posting-button, .sort-group {
  margin-bottom: 20px;
}
.sort-buttons :deep(.el-radio-button) .el-radio-button__inner { background-color: #4a4a4a !important; color: #ffcc00 !important; border: 1px solid #666 !important; box-shadow: none !important; }
.sort-buttons :deep(.el-radio-button.is-active) .el-radio-button__inner,
.sort-buttons :deep(.el-radio-button:hover) .el-radio-button__inner,
.sort-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) { background-color: #5a5a5a !important; color: #ffcc00 !important; }


/* ===== 修改开始：卡片容器和卡片样式 ===== */

/* 1. 卡片容器改为网格布局 */
.cards-container {
  width: 100%;
  max-width: 1200px; /* 您可以根据需要调整最大宽度 */
  display: grid;
  /*
   * 创建一个响应式网格：
   * - auto-fill: 自动填充尽可能多的列。
   * - minmax(250px, 1fr): 每列最小宽度250px，最大平分剩余空间。
   */
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px; /* 卡片之间的间距 */
}

/* 2. 单个卡片的样式调整 */
.posting-card {
  cursor: pointer;
  border-radius: 12px;
  /* 边框颜色可以稍微调深一点，以在浅色背景上更明显 */
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s, background-color 0.3s;
  display: flex;
  flex-direction: column;

  /*
   * 核心修改：使用半透明的白色/浅灰色
   * rgba(255, 255, 255, 0.15) 表示白色，透明度为 15%
   * 这样就会形成一层朦胧的、有点白的灰色效果
   * 您可以调整最后一个值 (0.15) 来改变明亮/透明程度
   * 比如 0.1 更透明，0.25 更不透明/更白
   */
  background-color: rgba(255, 255, 255, 0.15);

  /* 毛玻璃效果在浅色透明背景上效果依然出色 */
  backdrop-filter: blur(15px); /* 可以适当增强模糊半径 */
  -webkit-backdrop-filter: blur(15px);
}

.posting-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.6);
  /* 鼠标悬停时，背景稍微变亮一些 */
  background-color: rgba(0, 0, 0, 0.6);
}

/* 3. 卡片图片样式 */
.posting-image img {
  width: 100%;
  height: auto; /* 高度自适应，保持图片原始比例 */
  display: block; /* 消除图片下方的微小间隙 */
  aspect-ratio: 3 / 4; /* 您可以调整这个比例，比如 1/1 正方形， 16/9 宽屏 */
  object-fit: cover; /* 保证图片填满容器，可能会裁剪 */
}

/* 4. 卡片文字信息区域 */
.posting-info {
  padding: 12px 15px;
  color: #fff;
}

.posting-info h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #ffcc00; /* 保持黄色标题 */
  /* 防止标题过长换行破坏布局 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 5. 新增的点赞区域样式 */
.posting-footer {
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 从左侧开始 */
}

.likes-count {
  display: flex;
  align-items: center;
  gap: 6px; /* 图标和数字的间距 */
  font-size: 14px;
  color: #ccc;
}
/* ===== 修改结束 ===== */
.sort-group {
  margin-bottom: 20px;
}

.sort-dropdown-button {
  background-color: #4a4a4a;
  color: #ffcc00;
  border: 1px solid #666;
  padding: 8px 16px;
  border-radius: 6px;
}

.sort-dropdown-button:hover,
.sort-dropdown-button:focus {
  background-color: #5a5a5a;
  color: #ffcc00;
  border-color: #666;
}

/*
 * 使用 :deep() 或 :global() 来确保下拉菜单面板的样式也能生效
 * 因为它在DOM中可能不是当前组件的直接子元素
 */
:deep(.el-dropdown-menu) {
  background-color: #2d2d2d !important; /* 下拉菜单背景色 */
  border: 1px solid #666 !important;
}

:deep(.el-dropdown-menu__item) {
  color: #ccc !important; /* 菜单项文字颜色 */
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: #5a5a5a !important; /* 悬停背景色 */
  color: #ffcc00 !important; /* 悬停文字颜色 */
}
</style>