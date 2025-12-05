<template>
  <el-main class="all-columns-container bgimage">
<!--    <div class="page-header">-->
<!--      <h1 class="page-title">所有栏目</h1>-->
<!--    </div>-->

    <div v-if="columns.length > 0" class="columns-list">
      <el-card
          v-for="column in columns"
          :key="column.id"
          class="column-card"
          @click="goToColumnDetail(column.id)"
          shadow="hover"
      >
        <el-carousel
            v-if="Array.isArray(column.covers) && column.covers.length > 0"
            class="column-image"
            height="180px"
            indicator-position="none"
            :autoplay="false"
            arrow="hover"
        >
          <el-carousel-item v-for="(coverUrl, index) in column.covers" :key="index">
            <img :src="coverUrl" alt="Column Cover" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
        <div v-else class="column-image no-image-placeholder">
          <span>暂无封面</span>
        </div>

        <div class="column-info">
          <h3 class="column-theme">{{ column.theme }}</h3>
          <p class="column-description">{{ column.description }}</p>
        </div>
      </el-card>
    </div>

    <el-empty v-else description="暂无栏目信息"></el-empty>
  </el-main>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllColumns } from '../../api/columns';

interface Column {
  id: number;
  theme: string;
  description: string;
  covers: string[];
}

const columns = ref<Column[]>([]);
const router = useRouter();

const fetchAllColumns = async () => {
  try {
    const res = await getAllColumns();
    if (res.data.code === '200') {
      columns.value = res.data.data;
    }
  } catch (error) {
    console.error("获取所有栏目失败:", error);
  }
};

const goToColumnDetail = (columnId: number) => {
  router.push({ path: `/home/columns/${columnId}` });
};

onMounted(() => {
  fetchAllColumns();
});
</script>

<style scoped>
.all-columns-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 32px;
  padding: 40px 20px;
  backdrop-filter: blur(3px);
}

/* 背景：更柔和、不抢眼 */
.bgimage {
  background: linear-gradient(
      135deg,
      #1b1b1b 0%,
      #2a2e33 50%,
      #1f1f1f 100%
  );
  min-height: 100vh;
}


/* 列表布局保持一致 */
.columns-list {
  display: flex;
  flex-wrap: wrap;
  gap: 26px;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
}

/* 🔥 卡片改成 “悬疑风磨砂玻璃” 风格 */
.column-card {
  width: 280px;
  height: 330px;
  cursor: pointer;
  border-radius: 14px;
  padding: 0;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  transition: all 0.25s ease;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.35);
}

/* hover 发光，突出神秘感 */
.column-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 26px rgba(0, 0, 0, 0.55);
  border-color: rgba(255, 255, 255, 0.25);
}

/* 封面轮播区 */
.column-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
  border-radius: 0;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 栏目信息区域 */
.column-info {
  flex: 1;
  padding: 16px;
  text-align: left;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 标题：强化白色，类似书籍页风格 */
.column-theme {
  font-size: 1.22em;
  font-weight: 600;
  margin: 0 0 10px 0;
  color: #fff;
  letter-spacing: 0.5px;
}

/* 描述：微透明，沉稳 */
.column-description {
  font-size: 0.95em;
  line-height: 1.45;
  color: rgba(230, 230, 230, 0.75);
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  display: -webkit-box;
}

/* 没有封面时 */
.no-image-placeholder {
  background: rgba(255, 255, 255, 0.15);
  color: rgba(230, 230, 230, 0.7);
}

</style>