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

<style scoped>
/* ======== 核心背景 (保持不变) ======== */
.bgimage {
  min-height: 100vh;
  background: linear-gradient(
      135deg,
      #1b1b1b 0%,
      #2a2e33 50%,
      #1f1f1f 100%
  );
  background-attachment: fixed;
  padding: 0; /* Reset padding */
}

.column-detail-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* ======== 头部区域优化 ======== */
.column-header {
  text-align: center;
  color: white;
  /* 稍微增加透明度，让深色背景透出来一点，更有质感 */
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px); /* 可选：增加毛玻璃效果 */
  padding: 30px 20px;
  width: 100%;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  margin-bottom: 30px;
}
.column-title { font-size: 2.2em; margin: 0 0 10px 0; font-weight: 700; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }
.column-description { font-size: 1.1em; color: #ccc; max-width: 800px; margin: 0 auto; line-height: 1.6; }
.admin-actions { margin-top: 20px; display: flex; gap: 10px; justify-content: center;}

/* ======== 内容区容器 ======== */
.content-section {
  width: 100%;
  max-width: 1200px; /* 限制最大宽度，防止在大屏上太散 */
  padding: 0 20px 40px;
}

.product-list-title {
  color: white;
  font-size: 1.6em;
  margin-bottom: 24px;
  padding-left: 10px;
  border-left: 4px solid #409eff; /* 左侧增加一个强调色条 */
  display: flex;
  align-items: baseline;
  gap: 10px;
}
.book-count { font-size: 0.6em; color: #999; }


/* ======== 书籍列表 (核心修改：Grid布局) ======== */
.books-grid {
  display: grid;
  /* 关键：自动填充，最小宽 180px，与主页一致 */
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 24px;
  width: 100%;
}

/* ======== 书籍卡片 (核心修改：高对比度样式) ======== */
.book-card {
  background-color: #fff; /* 纯白背景，与深色底形成强烈对比 */
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: flex;
  flex-direction: column;
  /* 在深色背景下，阴影需要更重一点才更有层次感 */
  box-shadow: 0 4px 15px rgba(0,0,0,0.3);
}

.book-card:hover {
  transform: translateY(-8px); /* 悬浮上移 */
  box-shadow: 0 12px 28px rgba(0,0,0,0.5); /* 悬浮加重阴影 */
}

/* 图片容器：固定比例 */
.book-image-box {
  width: 100%;
  aspect-ratio: 3 / 4;
  height: 240px; /* 你可以根据需要调整 220–260px */
  background: #f5f7fa;
  overflow: hidden;
}

.book-image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.book-card:hover .book-image-box img {
  transform: scale(1.08); /* 图片微放大 */
}

/* 信息区域 */
.book-info {
  padding: 12px;
  flex: 1; /* 撑满剩余空间 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: left; /* 左对齐 */
}

.book-title {
  font-size: 15px;
  font-weight: 600;
  color: #333; /* 深色文字显示在白色卡片上 */
  margin: 0 0 8px 0;
  line-height: 1.4;
  /* 限制两行省略 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-price {
  color: #f56c6c; /* 价格高亮色 */
  font-weight: 700;
  font-size: 16px;
  margin-top: auto;
}

/* ======== 管理员删除按钮 (悬浮显示) ======== */
.remove-product-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 10;
  opacity: 0; /* 默认隐藏 */
  transition: all 0.2s ease;
  transform: scale(0.8);
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

/* 只有当鼠标悬停在卡片上时才显示 */
.book-card:hover .remove-product-btn {
  opacity: 1;
  transform: scale(1);
}

/* 调整 empty 状态在深色背景下的显示 */
:deep(.el-empty__description) {
  color: #ccc;
}

.dialog-image { max-width: 100%; }
</style>