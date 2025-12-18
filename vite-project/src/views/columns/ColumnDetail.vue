<template>
  <el-main class="column-detail-container bgimage">

    <div class="column-header">
      <h1 class="column-title">{{ columnInfo.theme }}</h1>
      <p class="column-description">{{ columnInfo.description || '暂无描述' }}</p>
      <div v-if="isAdmin" class="admin-actions">
        <el-button type="primary" @click="openEditDialog" size="small">更新信息</el-button>
        <el-button type="danger" @click="handleDeleteColumn" size="small">删除栏目</el-button>
      </div>
    </div>

    <div class="content-section">
      <h2 class="product-list-title">
        <span>栏目藏书</span>
        <span class="book-count" v-if="products.length">({{ products.length }}本)</span>
      </h2>

      <div v-if="products.length > 0" class="books-grid">
        <div
            v-for="product in products"
            :key="product.id"
            class="book-card"
            @click="goToProductDetail(product.id)"
        >
          <el-button
              v-if="isAdmin"
              class="remove-product-btn"
              type="danger"
              icon="Delete"
              circle
              size="small"
              @click.stop="handleRemoveProduct(product.id)"
          ></el-button>

          <div class="book-image-box">
            <img
                :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : 'https://via.placeholder.com/150'"
                alt="Product Cover"
                loading="lazy"
            />
          </div>

          <div class="book-info">
            <h3 class="book-title" :title="product.title">{{ product.title }}</h3>
            <div class="book-price">
              ￥{{ product.price || '未知' }}
            </div>
          </div>
        </div>
      </div>
      <el-empty v-else description="该栏目暂无书籍" :image-size="150"></el-empty>
    </div>

    <el-dialog v-model="showEditDialog" title="更新栏目信息" width="50%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="栏目主题">
          <el-input v-model="editForm.theme"></el-input>
        </el-form-item>
        <el-form-item label="栏目描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="栏目封面">
          <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePictureCardPreview" multiple>
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateColumn">确认更新</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogVisible">
      <img class="dialog-image" :src="dialogImageUrl" alt="Preview Image" />
    </el-dialog>
  </el-main>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAllColumns, getProductsByColumn, updateColumns, deleteColumns, removeProductFromColumn } from '../../api/columns';
import { ElMessage, ElMessageBox, type UploadFile, type UploadFiles } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import type { Product } from '../../api/product';
import {getImage} from "../../api/tools.ts";

const route = useRoute();
const router = useRouter();
const columnId = Number(route.params.id);

const columnInfo = ref({ id: columnId, theme: '加载中...', description: '', covers: [] as string[] });
const products = ref<Product[]>([]);
const isAdmin = computed(() => sessionStorage.getItem('role') === 'MANAGER');

const showEditDialog = ref(false);
const editForm = ref({ id: columnId, theme: '', description: '', covers: [] as string[] });
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const MAX_SIZE = 1024 * 1024; // 1MB

const fetchColumnData = async () => {
  try {
    const [productsRes, columnsRes] = await Promise.all([
      getProductsByColumn(columnId),
      getAllColumns()
    ]);
    if (productsRes.data.code === '200') {
      products.value = productsRes.data.data;
    }
    if (columnsRes.data.code === '200') {
      const currentColumn = columnsRes.data.data.find((c: any) => c.id === columnId);
      if (currentColumn) {
        columnInfo.value = currentColumn;
      }
    }
  } catch (error) {
    ElMessage.error('加载栏目数据失败');
    console.error(error);
  }
};

onMounted(fetchColumnData);

const goToProductDetail = (productId: number) => {
  router.push({ path: `/home/product/${productId}` });
};

const handleDeleteColumn = async () => {
  try {
    await ElMessageBox.confirm('此操作将永久删除该栏目，但不会删除栏目内的书籍。是否继续?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    const res = await deleteColumns(columnId);
    // 修正2: 使用 '200' 字符串进行判断
    if (res.data.code === '200') {
      ElMessage.success('删除成功!');
      router.push({ name: 'AllColumns' });
    } else {
      ElMessage.error(res.data.msg || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除操作失败');
    } else {
      ElMessage.info('已取消删除');
    }
  }
};

const openEditDialog = () => {
  editForm.value = JSON.parse(JSON.stringify(columnInfo.value));
  fileList.value = editForm.value.covers.map((url, index) => ({
    name: `cover${index}.jpg`,
    url: url,
    uid: Date.now() + index,
    status: 'success',
  }));
  showEditDialog.value = true;
};

const handleUpdateColumn = async () => {
  try {
    const res = await updateColumns(editForm.value);
    // 修正3: 使用 '200' 字符串进行判断
    if (res.data.code === '200') {
      ElMessage.success('更新成功!');
      showEditDialog.value = false;
      await fetchColumnData();
    } else {
      ElMessage.error(res.data.msg || '更新失败');
    }
  } catch (error) {
    ElMessage.error('更新操作失败');
  }
};

async function handleChange(file: UploadFile) {
  if (!editForm.value.covers) editForm.value.covers = [];
  const rawFile = file.raw;
  if (!rawFile) return;
  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）');
    return;
  }
  try {
    const res = await getImage(rawFile);
    if (res && res.code === '200') {
      console.log("上传");
      editForm.value.covers.push(res.data);
      ElMessage.success('新封面上传成功');
    } else { ElMessage.error('上传失败'); }
  } catch (error) { ElMessage.error('上传异常'); }
}

const handleRemove = (file: UploadFile) => {
  const urlToRemove = file.url;
  if (urlToRemove && editForm.value.covers) {
    console.log("删除", editForm.value.covers);
    editForm.value.covers = editForm.value.covers.filter(url => url !== urlToRemove);
    console.log(urlToRemove);
    console.log(editForm.value.covers);
  }
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};

const handleRemoveProduct = async (productId: number) => {
  try {
    await ElMessageBox.confirm('确定要将此商品移出该栏目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });

    const res = await removeProductFromColumn(productId, columnId);

    if (res.data.code == 200) {
      ElMessage.success('移除成功');
      // 从前端列表直接移除，实现页面刷新
      products.value = products.value.filter(p => p.id !== productId);
    } else {
      ElMessage.error(res.data.msg || '移除失败');
    }
  } catch(error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败');
    } else {
      ElMessage.info('已取消操作');
    }
  }
};
</script>

<!--<style scoped>-->
<!--/* ======== 核心背景 (保持不变) ======== */-->
<!--.bgimage {-->
<!--  min-height: 100vh;-->
<!--  background: linear-gradient(-->
<!--      135deg,-->
<!--      #1b1b1b 0%,-->
<!--      #2a2e33 50%,-->
<!--      #1f1f1f 100%-->
<!--  );-->
<!--  background-attachment: fixed;-->
<!--  padding: 0; /* Reset padding */-->
<!--}-->

<!--.column-detail-container {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--}-->

<!--/* ======== 头部区域优化 ======== */-->
<!--.column-header {-->
<!--  text-align: center;-->
<!--  color: white;-->
<!--  /* 稍微增加透明度，让深色背景透出来一点，更有质感 */-->
<!--  background-color: rgba(255, 255, 255, 0.1);-->
<!--  backdrop-filter: blur(10px); /* 可选：增加毛玻璃效果 */-->
<!--  padding: 30px 20px;-->
<!--  width: 100%;-->
<!--  border-bottom: 1px solid rgba(255,255,255,0.1);-->
<!--  margin-bottom: 30px;-->
<!--}-->
<!--.column-title { font-size: 2.2em; margin: 0 0 10px 0; font-weight: 700; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }-->
<!--.column-description { font-size: 1.1em; color: #ccc; max-width: 800px; margin: 0 auto; line-height: 1.6; }-->
<!--.admin-actions { margin-top: 20px; display: flex; gap: 10px; justify-content: center;}-->

<!--/* ======== 内容区容器 ======== */-->
<!--.content-section {-->
<!--  width: 100%;-->
<!--  max-width: 1200px; /* 限制最大宽度，防止在大屏上太散 */-->
<!--  padding: 0 20px 40px;-->
<!--}-->

<!--.product-list-title {-->
<!--  color: white;-->
<!--  font-size: 1.6em;-->
<!--  margin-bottom: 24px;-->
<!--  padding-left: 10px;-->
<!--  border-left: 4px solid #409eff; /* 左侧增加一个强调色条 */-->
<!--  display: flex;-->
<!--  align-items: baseline;-->
<!--  gap: 10px;-->
<!--}-->
<!--.book-count { font-size: 0.6em; color: #999; }-->


<!--/* ======== 书籍列表 (核心修改：Grid布局) ======== */-->
<!--.books-grid {-->
<!--  display: grid;-->
<!--  /* 关键：自动填充，最小宽 180px，与主页一致 */-->
<!--  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));-->
<!--  gap: 24px;-->
<!--  width: 100%;-->
<!--}-->

<!--/* ======== 书籍卡片 (核心修改：高对比度样式) ======== */-->
<!--.book-card {-->
<!--  background-color: #fff; /* 纯白背景，与深色底形成强烈对比 */-->
<!--  border-radius: 12px;-->
<!--  overflow: hidden;-->
<!--  cursor: pointer;-->
<!--  transition: all 0.3s ease;-->
<!--  position: relative;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  /* 在深色背景下，阴影需要更重一点才更有层次感 */-->
<!--  box-shadow: 0 4px 15px rgba(0,0,0,0.3);-->
<!--}-->

<!--.book-card:hover {-->
<!--  transform: translateY(-8px); /* 悬浮上移 */-->
<!--  box-shadow: 0 12px 28px rgba(0,0,0,0.5); /* 悬浮加重阴影 */-->
<!--}-->

<!--/* 图片容器：固定比例 */-->
<!--.book-image-box {-->
<!--  width: 100%;-->
<!--  aspect-ratio: 3 / 4;-->
<!--  height: 240px; /* 你可以根据需要调整 220–260px */-->
<!--  background: #f5f7fa;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.book-image-box img {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  object-fit: cover;-->
<!--  transition: transform 0.5s ease;-->
<!--}-->

<!--.book-card:hover .book-image-box img {-->
<!--  transform: scale(1.08); /* 图片微放大 */-->
<!--}-->

<!--/* 信息区域 */-->
<!--.book-info {-->
<!--  padding: 12px;-->
<!--  flex: 1; /* 撑满剩余空间 */-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  justify-content: space-between;-->
<!--  text-align: left; /* 左对齐 */-->
<!--}-->

<!--.book-title {-->
<!--  font-size: 15px;-->
<!--  font-weight: 600;-->
<!--  color: #333; /* 深色文字显示在白色卡片上 */-->
<!--  margin: 0 0 8px 0;-->
<!--  line-height: 1.4;-->
<!--  /* 限制两行省略 */-->
<!--  display: -webkit-box;-->
<!--  -webkit-line-clamp: 2;-->
<!--  -webkit-box-orient: vertical;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.book-price {-->
<!--  color: #f56c6c; /* 价格高亮色 */-->
<!--  font-weight: 700;-->
<!--  font-size: 16px;-->
<!--  margin-top: auto;-->
<!--}-->

<!--/* ======== 管理员删除按钮 (悬浮显示) ======== */-->
<!--.remove-product-btn {-->
<!--  position: absolute;-->
<!--  top: 8px;-->
<!--  right: 8px;-->
<!--  z-index: 10;-->
<!--  opacity: 0; /* 默认隐藏 */-->
<!--  transition: all 0.2s ease;-->
<!--  transform: scale(0.8);-->
<!--  box-shadow: 0 2px 8px rgba(0,0,0,0.2);-->
<!--}-->

<!--/* 只有当鼠标悬停在卡片上时才显示 */-->
<!--.book-card:hover .remove-product-btn {-->
<!--  opacity: 1;-->
<!--  transform: scale(1);-->
<!--}-->

<!--/* 调整 empty 状态在深色背景下的显示 */-->
<!--:deep(.el-empty__description) {-->
<!--  color: #ccc;-->
<!--}-->

<!--.dialog-image { max-width: 100%; }-->
<!--</style>-->
<!--<style scoped>-->
<!--/* ======== 1. 全局背景与容器 ======== */-->
<!--.bgimage {-->
<!--  min-height: 100vh;-->
<!--  background-color: #f0f2f5; /* 统一浅灰背景 */-->
<!--  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;-->
<!--  padding: 0;-->
<!--  padding-bottom: 60px;-->
<!--}-->

<!--.column-detail-container {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--}-->

<!--/* ======== 2. 头部区域优化 (白底黑字) ======== */-->
<!--.column-header {-->
<!--  text-align: center;-->
<!--  background-color: #ffffff; /* 改为纯白背景 */-->
<!--  padding: 40px 20px;-->
<!--  width: 100%;-->
<!--  margin-bottom: 30px;-->
<!--  box-shadow: 0 4px 20px rgba(0,0,0,0.05); /* 添加轻微阴影 */-->
<!--  border-bottom: 1px solid #e4e7ed;-->
<!--}-->

<!--.column-title {-->
<!--  font-size: 32px;-->
<!--  margin: 0 0 12px 0;-->
<!--  font-weight: 800;-->
<!--  color: #303133; /* 深色标题 */-->
<!--  letter-spacing: 1px;-->
<!--}-->

<!--.column-description {-->
<!--  font-size: 16px;-->
<!--  color: #606266; /* 次级灰色 */-->
<!--  max-width: 800px;-->
<!--  margin: 0 auto;-->
<!--  line-height: 1.8;-->
<!--}-->

<!--.admin-actions {-->
<!--  margin-top: 24px;-->
<!--  display: flex;-->
<!--  gap: 12px;-->
<!--  justify-content: center;-->
<!--}-->

<!--/* ======== 3. 内容区容器 ======== */-->
<!--.content-section {-->
<!--  width: 100%;-->
<!--  max-width: 1280px; /* 适配大屏 */-->
<!--  padding: 0 20px 40px;-->
<!--}-->

<!--.product-list-title {-->
<!--  color: #303133; /* 深色文字 */-->
<!--  font-size: 22px;-->
<!--  font-weight: 600;-->
<!--  margin-bottom: 24px;-->
<!--  padding-left: 12px;-->
<!--  border-left: 4px solid #409eff; /* 品牌蓝条 */-->
<!--  display: flex;-->
<!--  align-items: baseline;-->
<!--  gap: 10px;-->
<!--}-->

<!--.book-count {-->
<!--  font-size: 14px;-->
<!--  color: #909399;-->
<!--  font-weight: 400;-->
<!--}-->


<!--/* ======== 4. 书籍列表 (Grid布局) ======== */-->
<!--.books-grid {-->
<!--  display: grid;-->
<!--  /* 自动填充，最小宽 180px */-->
<!--  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));-->
<!--  gap: 24px;-->
<!--  width: 100%;-->
<!--}-->

<!--/* ======== 5. 书籍卡片 (核心修改：适配浅色背景) ======== */-->
<!--.book-card {-->
<!--  background-color: #fff;-->
<!--  border-radius: 12px;-->
<!--  overflow: hidden;-->
<!--  cursor: pointer;-->
<!--  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);-->
<!--  position: relative;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->

<!--  /* 浅色背景下的柔和阴影 */-->
<!--  box-shadow: 0 4px 12px rgba(0,0,0,0.04);-->
<!--  border: 1px solid #ebeef5; /* 极淡边框 */-->
<!--}-->

<!--.book-card:hover {-->
<!--  transform: translateY(-8px); /* 悬浮上移 */-->
<!--  box-shadow: 0 12px 24px rgba(0,0,0,0.12); /* 加深阴影 */-->
<!--  border-color: #c6e2ff; /* 悬停边框变蓝 */-->
<!--}-->

<!--/* 图片容器 */-->
<!--.book-image-box {-->
<!--  width: 100%;-->
<!--  aspect-ratio: 3 / 4;-->
<!--  height: 240px;-->
<!--  background: #f5f7fa; /* 图片未加载时的占位色 */-->
<!--  overflow: hidden;-->
<!--  position: relative;-->
<!--}-->

<!--.book-image-box img {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  object-fit: cover;-->
<!--  transition: transform 0.5s ease;-->
<!--}-->

<!--.book-card:hover .book-image-box img {-->
<!--  transform: scale(1.08); /* 图片微放大 */-->
<!--}-->

<!--/* 信息区域 */-->
<!--.book-info {-->
<!--  padding: 14px;-->
<!--  flex: 1;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  justify-content: space-between;-->
<!--  text-align: left;-->
<!--}-->

<!--.book-title {-->
<!--  font-size: 15px;-->
<!--  font-weight: 600;-->
<!--  color: #333;-->
<!--  margin: 0 0 8px 0;-->
<!--  line-height: 1.4;-->
<!--  /* 限制两行省略 */-->
<!--  display: -webkit-box;-->
<!--  -webkit-line-clamp: 2;-->
<!--  -webkit-box-orient: vertical;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.book-price {-->
<!--  color: #f56c6c; /* 价格红 */-->
<!--  font-weight: 700;-->
<!--  font-size: 16px;-->
<!--  margin-top: auto;-->
<!--}-->

<!--/* ======== 6. 管理员删除按钮 ======== */-->
<!--.remove-product-btn {-->
<!--  position: absolute;-->
<!--  top: 8px;-->
<!--  right: 8px;-->
<!--  z-index: 10;-->
<!--  opacity: 0;-->
<!--  transition: all 0.2s ease;-->
<!--  transform: scale(0.8);-->
<!--  box-shadow: 0 2px 8px rgba(0,0,0,0.15);-->
<!--}-->

<!--.book-card:hover .remove-product-btn {-->
<!--  opacity: 1;-->
<!--  transform: scale(1);-->
<!--}-->

<!--/* 空状态样式适配 */-->
<!--:deep(.el-empty__description) {-->
<!--  color: #909399;-->
<!--}-->

<!--.dialog-image { max-width: 100%; }-->
<!--</style>-->

<style scoped>
/* ======== 1. 全局背景 ======== */
.bgimage {
  min-height: 100vh;
  background-color: #f5f7fa; /* 下半部分保持浅灰，突出书籍 */
  padding: 0;
  padding-bottom: 60px;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.column-detail-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

/* ======== 2. 头部区域重设计 (Dark Premium Header) ======== */
.column-header {
  width: 100%;
  /* 使用深色渐变，呼应顶部的 Truth Bookstore 导航栏 */
  background: linear-gradient(to bottom, #2c2c2c 0%, #1a1a1a 100%);
  padding: 60px 20px 80px; /* 底部留多一点空间，为了视觉平衡 */
  text-align: center;
  position: relative;
  border-bottom: 1px solid #444;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
}

/* 装饰背景文字 (可选，增加层次感) */
.column-header::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background-image: radial-gradient(circle at 50% 30%, rgba(255,255,255,0.03) 0%, transparent 60%);
  pointer-events: none;
}

.column-title {
  /* 使用金色/米色，在黑底上显得高级 */
  color: #e0c89a;
  font-size: 42px;
  margin: 0 0 16px 0;
  font-weight: 700;
  letter-spacing: 2px;
  font-family: "Georgia", "Times New Roman", serif; /* 衬线体更有书店感觉 */
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
  position: relative;
  display: inline-block;
}

/* 标题下方的装饰线 */
.column-title::after {
  content: "";
  display: block;
  width: 60px;
  height: 3px;
  background-color: #e0c89a;
  margin: 15px auto 0;
  border-radius: 2px;
}

.column-description {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7); /* 半透明白色 */
  max-width: 700px;
  margin: 20px auto 0;
  line-height: 1.8;
  font-weight: 300;
}

.admin-actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
  justify-content: center;
}

/* ======== 3. 内容区容器 ======== */
.content-section {
  width: 100%;
  max-width: 1280px;
  padding: 0 30px 60px;
  /* 核心布局技巧：让内容区向上位移，压在深色 Header 上一点点，或者紧贴 */
  margin-top: -40px;
  z-index: 2;
  position: relative;
}

.product-list-title {
  /* 这里的标题改为深色背景条或者隐形，为了配合布局，这里做成简约风格 */
  color: #333;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  padding: 15px 20px;
  background: #fff; /* 白色条 */
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  display: flex;
  align-items: baseline;
  gap: 10px;
}

.book-count {
  font-size: 14px;
  color: #909399;
  font-weight: normal;
}

/* ======== 4. 书籍列表 (Grid布局) ======== */
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(190px, 1fr)); /* 稍微加宽一点 */
  gap: 24px;
  width: 100%;
}

/* ======== 5. 书籍卡片 ======== */
.book-card {
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1); /* 弹性动效 */
  position: relative;
  display: flex;
  flex-direction: column;
  border: 1px solid #f0f0f0;
  /* 默认阴影 */
  box-shadow: 0 4px 10px rgba(0,0,0,0.03);
}

.book-card:hover {
  transform: translateY(-8px);
  /* 悬浮阴影加深 */
  box-shadow: 0 15px 30px rgba(0,0,0,0.1);
  border-color: transparent;
}

/* 图片容器 */
.book-image-box {
  width: 100%;
  aspect-ratio: 3 / 4;
  height: 250px;
  background: #f7f9fc;
  overflow: hidden;
  position: relative;
}

.book-image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.book-card:hover .book-image-box img {
  transform: scale(1.08);
}

/* 信息区域 */
.book-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: left;
}

.book-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px 0;
  line-height: 1.5;

  /* 两行省略 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-price {
  color: #d32f2f; /* 沉稳的红色 */
  font-weight: 700;
  font-size: 17px;
  margin-top: auto;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

/* ======== 6. 管理员删除按钮 ======== */
.remove-product-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
  opacity: 0;
  transition: all 0.2s ease;
  transform: scale(0.8);
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.book-card:hover .remove-product-btn {
  opacity: 1;
  transform: scale(1);
}

/* Dialog 图片 */
.dialog-image { max-width: 100%; }

/* 空状态适配 */
:deep(.el-empty) {
  padding: 60px 0;
}
</style>