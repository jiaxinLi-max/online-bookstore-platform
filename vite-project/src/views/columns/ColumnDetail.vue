<template>
  <el-main class="column-detail-container bgimage">
    <div class="column-header">
      <h1 class="column-title">{{ columnInfo.theme }}</h1>
      <p class="column-description">{{ columnInfo.description }}</p>
      <div v-if="isAdmin" class="admin-actions">
        <el-button type="primary" @click="openEditDialog">更新栏目信息</el-button>
        <el-button type="danger" @click="handleDeleteColumn">删除该栏目</el-button>
      </div>
    </div>

    <h2 class="product-list-title">本栏目包含的书籍</h2>
    <div v-if="products.length > 0" class="products-list">
      <el-card
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToProductDetail(product.id)"
          shadow="hover"
      >
        <div class="product-image">
          <img :src="Array.isArray(product.cover) && product.cover.length > 0 ? product.cover[0] : ''" alt="Product Cover" />
        </div>
        <h3 class="product-title">{{ product.title }}</h3>
      </el-card>
    </div>
    <el-empty v-else description="该栏目下还没有书籍"></el-empty>

    <el-dialog
        v-model="showEditDialog"
        title="更新栏目信息"
        width="50%"
    >
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="栏目主题">
          <el-input v-model="editForm.theme"></el-input>
        </el-form-item>
        <el-form-item label="栏目描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="栏目封面">
          <el-upload
              action="http://localhost:8080/api/images"
              list-type="picture-card"
              :auto-upload="false"
              :file-list="fileList"
              :on-success="handleSuccess"
              :on-remove="handleRemove"
              :on-preview="handlePictureCardPreview"
              multiple
          >
            <el-icon><Plus /></el-icon>
            <div>上传新封面</div>
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
import { getAllColumns, getProductsByColumn, updateColumns, deleteColumns } from '../../api/columns';
import { ElMessage, ElMessageBox, type UploadFile, type UploadFiles } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import type { Product } from '../../api/product';

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

// 【关键修正】: 删除旧的 handleChange 函数, 实现新的 handleSuccess
const handleSuccess = (responseData: any, uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  if (responseData.code === '200') {
    editForm.value.covers.push(responseData.data);
    uploadFile.url = responseData.data;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(responseData.msg || '上传失败');
    const failedFileIndex = uploadFiles.findIndex(f => f.uid === uploadFile.uid);
    if(failedFileIndex > -1) {
      uploadFiles.splice(failedFileIndex, 1);
    }
  }
  fileList.value = uploadFiles;
};

const handleRemove = (file: UploadFile, newFileList: UploadFiles) => {
  const urlToRemove = file.url;
  if (urlToRemove) {
    editForm.value.covers = editForm.value.covers.filter(url => url !== urlToRemove);
  }
  fileList.value = newFileList;
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};
</script>

<style scoped>
.column-detail-container { display: flex; flex-direction: column; align-items: center; gap: 24px; padding: 20px; }
.column-header { text-align: center; color: white; background-color: rgba(0, 0, 0, 0.4); padding: 20px 40px; border-radius: 12px; width: 100%; max-width: 900px; }
.column-title { font-size: 2.5em; margin: 0 0 10px 0; }
.column-description { font-size: 1.1em; color: #f0f0f0; }
.admin-actions { margin-top: 20px; }
.product-list-title { color: white; font-size: 1.8em; }
.products-list { display: flex; flex-wrap: wrap; gap: 16px; justify-content: center; width: 100%; max-width: 960px; }
.product-card { width: 140px; height: 200px; cursor: pointer; background-color: rgba(255, 248, 220, 0.9); border-radius: 10px; transition: transform 0.2s ease; }
.product-card:hover { transform: scale(1.05); }
.product-image img { width: 100%; height: 150px; object-fit: cover; }
.product-title { font-size: 13px; padding: 8px 4px 0; text-align: center; }
.bgimage { background-image: url("../../assets/780.jpg"); background-attachment: fixed; background-size: cover; min-height: 100vh; }
.dialog-image { max-width: 100%; }
</style>