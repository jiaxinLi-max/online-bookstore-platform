<template>
  <el-main class="all-columns-container bgimage">
    <div class="page-header">
      <h1 class="page-title">所有栏目</h1>
    </div>

    <div v-if="columns.length > 0" class="columns-list">
      <el-card
          v-for="column in columns"
          :key="column.id"
          class="column-card"
          @click="goToColumnDetail(column.id)"
          shadow="hover"
      >
        <div class="column-image">
          <img :src="Array.isArray(column.covers) && column.covers.length > 0 ? column.covers[0] : ''" alt="Column Cover" />
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
}
.column-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.2);
}
.column-image {
  width: 100%;
  height: 180px;
}
.column-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  /* 多行文字溢出显示省略号 */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 显示两行 */
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