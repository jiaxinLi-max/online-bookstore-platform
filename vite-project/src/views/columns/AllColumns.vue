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
  gap: 24px;
  padding: 20px;
}
.page-header {
  text-align: center;
  color: white;
}
.page-title {
  font-size: 2.5em;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
}
.columns-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
}
.column-card {
  width: 280px;
  height: 320px;
  cursor: pointer;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
  padding: 0;
}
.column-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.2);
}
.column-image {
  width: 100%;
  height: 180px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  overflow: hidden;
}
.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.no-image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #c0c4cc;
}
.column-info {
  padding: 14px;
  text-align: center;
}
.column-theme {
  font-size: 1.2em;
  font-weight: bold;
  margin: 0 0 8px 0;
}
.column-description {
  font-size: 0.9em;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-color: #7b6b4d;
  min-height: 100vh;
}
</style>