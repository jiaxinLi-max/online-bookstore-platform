<template>
  <div class="all-columns-container bgimage">

    <div class="column-header">
      <h1 class="page-title">栏目荟萃</h1>
      <div class="header-divider"></div>
      <p class="page-intro">
        “我们试图将浩瀚的真理分类，不是为了在此止步，<br />
        而是为了让你在每一条通往智慧的路径上，都能找到对应的灯塔。”
      </p>
    </div>

    <div class="content-section">
      <div v-if="columns.length > 0" class="columns-grid">
        <div
            v-for="column in columns"
            :key="column.id"
            class="column-card"
            @click="goToColumnDetail(column.id)"
        >
          <div class="column-image-box">
            <el-carousel
                v-if="Array.isArray(column.covers) && column.covers.length > 0"
                height="180px"
                indicator-position="none"
                :autoplay="false"
                arrow="hover"
            >
              <el-carousel-item v-for="(coverUrl, index) in column.covers" :key="index">
                <img :src="coverUrl" alt="Column Cover" class="carousel-image" />
              </el-carousel-item>
            </el-carousel>

            <div v-else class="no-image-placeholder">
              <span class="placeholder-icon">📚</span>
              <span>暂无封面</span>
            </div>
          </div>

          <div class="column-info">
            <h3 class="column-theme">{{ column.theme }}</h3>
            <p class="column-description">{{ column.description }}</p>
            <div class="card-footer">
              <span class="read-more">进入栏目 <i class="el-icon-right"></i></span>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无栏目信息" class="custom-empty"></el-empty>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllColumns } from '../../api/columns';

// 恢复原本简单的数据结构
interface Column {
  id: number;
  theme: string;
  description: string;
  covers: string[];
}

const columns = ref<Column[]>([]);
const router = useRouter();

// 恢复原本的获取逻辑，不做任何 URL 处理
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
/* ======== 1. 全局容器与背景 ======== */
.bgimage {
  min-height: 100vh;
  background-color: #f5f7fa;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 40px;
}

/* ======== 2. 顶部 Header 区域 ======== */
.column-header {
  width: 100%;
  background: linear-gradient(to bottom, #2c2c2c 0%, #1a1a1a 100%);
  padding: 80px 20px 120px;
  text-align: center;
  position: relative;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
}

.column-header::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background-image: radial-gradient(circle at 50% 30%, rgba(255,255,255,0.03) 0%, transparent 60%);
  pointer-events: none;
}

.page-title {
  color: #e0c89a;
  font-size: 38px;
  margin: 0;
  font-weight: 700;
  letter-spacing: 2px;
  font-family: "Georgia", "Times New Roman", serif;
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
}

.header-divider {
  width: 60px;
  height: 3px;
  background-color: #e0c89a;
  margin: 20px auto;
  border-radius: 2px;
  opacity: 0.8;
}

.page-intro {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.75);
  max-width: 700px;
  margin: 0 auto;
  line-height: 1.8;
  font-weight: 300;
  font-style: italic;
}

/* ======== 3. 内容布局容器 ======== */
.content-section {
  width: 100%;
  max-width: 1280px;
  padding: 0 24px;
  margin-top: -60px; /* 向上偏移 */
  z-index: 2;
  position: relative;
}

.columns-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

/* ======== 4. 卡片样式重构 ======== */
.column-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  height: 360px;
  border: 1px solid #ebeef5;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.column-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12);
  border-color: rgba(224, 200, 154, 0.3);
}

/* 图片容器 */
.column-image-box {
  width: 100%;
  height: 180px; /* 这里的容器高度要和 el-carousel height="180px" 对应 */
  background: #f7f9fc;
  position: relative;
  overflow: hidden;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保证图片填满且不变形 */
  transition: transform 0.5s ease;
  display: block; /* 防止图片底部出现空白缝隙 */
}

.column-card:hover .carousel-image {
  transform: scale(1.08);
}

.no-image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  color: #909399;
  gap: 8px;
}
.placeholder-icon {
  font-size: 24px;
  opacity: 0.5;
}

/* 信息区域 */
.column-info {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
  text-align: left;
}

.column-theme {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 12px 0;
  color: #303133;
  font-family: "Georgia", serif;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.column-description {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: auto;
}

.card-footer {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px dashed #f0f0f0;
  text-align: right;
}

.read-more {
  font-size: 13px;
  color: #e0c89a;
  font-weight: 600;
  transition: color 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.column-card:hover .read-more {
  color: #c4a976;
}

.custom-empty {
  padding: 60px 0;
  background: #fff;
  border-radius: 12px;
  width: 100%;
  box-shadow: 0 4px 12px rgba(0,0,0,0.03);
}
</style>