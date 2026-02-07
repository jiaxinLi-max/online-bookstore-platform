<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="create_posting bgimage">&ndash;&gt;-->
<!--&lt;!&ndash;    <h1 class="create-posting-title">创建笔记</h1>&ndash;&gt;-->
<!--&lt;!&ndash;    <el-form ref="form" label-width="120px" class="posting-form">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="笔记标题" prop="postingTitle">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="title" placeholder="请输入笔记标题" style="width: 400px;"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="笔记内容" prop="postingContent">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="content" placeholder="请输入内容" style="width: 400px;"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="关联商品" prop="productIds">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-select&ndash;&gt;-->
<!--&lt;!&ndash;            v-model="selectedProductIds"&ndash;&gt;-->
<!--&lt;!&ndash;            multiple&ndash;&gt;-->
<!--&lt;!&ndash;            filterable&ndash;&gt;-->
<!--&lt;!&ndash;            placeholder="请搜索并选择要关联的商品"&ndash;&gt;-->
<!--&lt;!&ndash;            style="width: 400px;"&ndash;&gt;-->
<!--&lt;!&ndash;        >&ndash;&gt;-->
<!--&lt;!&ndash;          <el-option&ndash;&gt;-->
<!--&lt;!&ndash;              v-for="item in products"&ndash;&gt;-->
<!--&lt;!&ndash;              :key="item.id"&ndash;&gt;-->
<!--&lt;!&ndash;              :label="item.title"&ndash;&gt;-->
<!--&lt;!&ndash;              :value="item.id"&ndash;&gt;-->
<!--&lt;!&ndash;          />&ndash;&gt;-->
<!--&lt;!&ndash;        </el-select>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="笔记封面" prop="covers">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-upload&ndash;&gt;-->
<!--&lt;!&ndash;            action="http://localhost:8080/api/images"&ndash;&gt;-->
<!--&lt;!&ndash;            list-type="picture-card"&ndash;&gt;-->
<!--&lt;!&ndash;            :auto-upload="false"&ndash;&gt;-->
<!--&lt;!&ndash;            :file-list="fileList"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-change="handleChange"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-remove="handleRemove"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-preview="handlePictureCardPreview"&ndash;&gt;-->
<!--&lt;!&ndash;            multiple&ndash;&gt;-->
<!--&lt;!&ndash;        >&ndash;&gt;-->
<!--&lt;!&ndash;          <el-icon><Plus /></el-icon>&ndash;&gt;-->
<!--&lt;!&ndash;          <div>点击上传笔记封面</div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-upload>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-dialog v-model="dialogVisible">&ndash;&gt;-->
<!--&lt;!&ndash;          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />&ndash;&gt;-->
<!--&lt;!&ndash;        </el-dialog>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button @click.prevent="handleCreatePosting" :disabled="createDisabled" type="primary" plain>&ndash;&gt;-->
<!--&lt;!&ndash;          创建笔记&ndash;&gt;-->
<!--&lt;!&ndash;        </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-form>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed, onMounted } from 'vue';-->
<!--import { ElMessage, UploadFile } from 'element-plus';-->
<!--import { createPosting } from "../../api/posting";-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--import { Product, getAllProduct } from "../../api/product.ts";-->

<!--const title = ref('');-->
<!--const content = ref('');-->
<!--// 修正2: covers 的状态是一个字符串数组-->
<!--const cover = ref('');-->
<!--const covers = ref<string[]>([]);-->

<!--const fileList = ref<UploadFile[]>([]);-->
<!--const dialogImageUrl = ref('');-->
<!--const dialogVisible = ref(false);-->

<!--const products = ref<Product[]>([]);-->
<!--const selectedProductIds = ref<number[]>([]);-->

<!--const createDisabled = computed(() => {-->
<!--  return !title.value || !content.value || covers.value.length === 0;-->
<!--});-->
<!--const MAX_SIZE = 1024 * 1024; // 1MB-->

<!--// 修正3: 使用带状态判断的 handleChange，解决重复上传问题-->
<!--async function handleChange(file: UploadFile, newFileList: UploadFile[]) {-->
<!--  const rawFile = file.raw;-->
<!--  if (!rawFile) {-->
<!--    ElMessage.error('无法获取文件');-->
<!--    return;-->
<!--  }-->

<!--  if (rawFile.size > MAX_SIZE) {-->
<!--    ElMessage.error('文件超过最大大小限制（1MB）');-->
<!--    return;-->
<!--  }-->

<!--  try {-->
<!--    const res = await getImage(rawFile); // 调用上传接口-->
<!--    if (res && res.code === '200') {-->
<!--      // 因为支持多图，所以将新的url添加到covers数组-->
<!--      covers.value.push(res.data);-->
<!--      file.url = res.data; // 更新当前文件的URL用于预览-->
<!--      fileList.value = newFileList; // 更新文件列表-->
<!--      ElMessage.success('上传成功');-->
<!--    } else {-->
<!--      ElMessage.error('上传失败，请重试');-->
<!--      // 从列表中移除上传失败的文件-->
<!--      fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('上传异常，请重试');-->
<!--    fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--  }-->
<!--}-->

<!--const handleRemove = (file: UploadFile) => {-->
<!--  // 从 fileList 和 covers 数组中同时移除-->
<!--  const newFileList = fileList.value.filter(item => item.uid !== file.uid);-->
<!--  fileList.value = newFileList;-->

<!--  // 假设file.url是上传成功后返回的url-->
<!--  if (file.url) {-->
<!--    const urlToRemove = file.url;-->
<!--    covers.value = covers.value.filter(url => url !== urlToRemove);-->
<!--  }-->
<!--};-->

<!--const handlePictureCardPreview = (file: UploadFile) => {-->
<!--  dialogImageUrl.value = file.url || '';-->
<!--  dialogVisible.value = true;-->
<!--};-->

<!--async function handleCreatePosting() {-->
<!--  const token = sessionStorage.getItem('token');-->
<!--  if (!token) { ElMessage.error('请先登录!'); return; }-->
<!--  const coverUrl = covers.value.length > 0 ? covers.value[0] : '';-->
<!--  console.log('coverUrl:', coverUrl);-->


<!--  // 修正4: payload 中的 covers 是一个数组-->
<!--  const payload = {-->
<!--    userId: Number(sessionStorage.getItem('userId')),-->
<!--    title: title.value,-->
<!--    content: content.value,-->
<!--    covers: covers.value,-->
<!--    cover:coverUrl,-->
<!--    productIds: selectedProductIds.value,-->
<!--  };-->

<!--  try {-->
<!--    const res = await createPosting(payload);-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage.success('创建笔记成功');-->
<!--      title.value = ''; content.value = '';-->
<!--      covers.value = []; fileList.value = [];-->
<!--      selectedProductIds.value = [];-->
<!--    } else { ElMessage.error(res.data.message); }-->
<!--  } catch (error) { ElMessage.error('创建笔记失败'); }-->
<!--}-->

<!--onMounted(async () => {-->
<!--  try {-->
<!--    const res = await getAllProduct();-->
<!--    if(res.data.code == 200) {-->
<!--      products.value = res.data.data;-->
<!--    }-->
<!--  } catch(error) {-->
<!--    console.error("获取商品列表失败:", error);-->
<!--  }-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.create-posting-title { margin-left: 25%; color: white; }-->
<!--.dialog-image { max-width: 100%; }-->
<!--.bgimage { background-color: rgba(0, 0, 0, 0.3); background-image: url("../../assets/780.jpg"); background-size: cover; background-repeat: no-repeat; background-position: center; min-height: 100vh; }-->
<!--::v-deep(.el-form-item__label) { color: white; }-->
<!--</style>-->
<!--<template>-->
<!--  <div class="create_posting">-->
<!--    <h1 class="create-posting-title">创建笔记</h1>-->
<!--    <el-form ref="form" label-width="120px" class="posting-form">-->
<!--      <el-form-item label="笔记标题" prop="postingTitle">-->
<!--        <el-input v-model="title" placeholder="请输入笔记标题"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="笔记内容" prop="postingContent">-->
<!--        <el-input-->
<!--            type="textarea"-->
<!--            v-model="content"-->
<!--            placeholder="请输入内容"-->
<!--            :rows="6"-->
<!--        ></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="关联商品" prop="productIds">-->
<!--        <el-select-->
<!--            v-model="selectedProductIds"-->
<!--            multiple-->
<!--            filterable-->
<!--            placeholder="请搜索并选择要关联的商品"-->
<!--        >-->
<!--          <el-option-->
<!--              v-for="item in products"-->
<!--              :key="item.id"-->
<!--              :label="item.title"-->
<!--              :value="item.id"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="笔记封面" prop="covers">-->
<!--        <el-upload-->
<!--            action="http://localhost:8080/api/images"-->
<!--            list-type="picture-card"-->
<!--            :auto-upload="false"-->
<!--            :file-list="fileList"-->
<!--            :on-change="handleChange"-->
<!--            :on-remove="handleRemove"-->
<!--            :on-preview="handlePictureCardPreview"-->
<!--            multiple-->
<!--        >-->
<!--          <el-icon><Plus /></el-icon>-->
<!--          <div>点击上传笔记封面</div>-->
<!--        </el-upload>-->
<!--        <el-dialog v-model="dialogVisible">-->
<!--          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />-->
<!--        </el-dialog>-->
<!--      </el-form-item>-->

<!--      <el-form-item>-->
<!--        <el-button-->
<!--            @click.prevent="handleCreatePosting"-->
<!--            :disabled="createDisabled"-->
<!--            type="primary"-->
<!--        >-->
<!--          创建笔记-->
<!--        </el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed, onMounted } from 'vue';-->
<!--import { ElMessage, UploadFile } from 'element-plus';-->
<!--import { createPosting } from "../../api/posting";-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--import { Product, getAllProduct } from "../../api/product.ts";-->

<!--const title = ref('');-->
<!--const content = ref('');-->
<!--const covers = ref<string[]>([]);-->
<!--const fileList = ref<UploadFile[]>([]);-->
<!--const dialogImageUrl = ref('');-->
<!--const dialogVisible = ref(false);-->

<!--const products = ref<Product[]>([]);-->
<!--const selectedProductIds = ref<number[]>([]);-->

<!--const createDisabled = computed(() => {-->
<!--  return !title.value || !content.value || covers.value.length === 0;-->
<!--});-->
<!--const MAX_SIZE = 1024 * 1024; // 1MB-->

<!--async function handleChange(file: UploadFile, newFileList: UploadFile[]) {-->
<!--  const rawFile = file.raw;-->
<!--  if (!rawFile) {-->
<!--    ElMessage.error('无法获取文件');-->
<!--    return;-->
<!--  }-->

<!--  if (rawFile.size > MAX_SIZE) {-->
<!--    ElMessage.error('文件超过最大大小限制（1MB）');-->
<!--    return;-->
<!--  }-->

<!--  try {-->
<!--    const res = await getImage(rawFile);-->
<!--    if (res && res.code === '200') {-->
<!--      covers.value.push(res.data);-->
<!--      file.url = res.data;-->
<!--      fileList.value = newFileList;-->
<!--      ElMessage.success('上传成功');-->
<!--    } else {-->
<!--      ElMessage.error('上传失败，请重试');-->
<!--      fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('上传异常，请重试');-->
<!--    fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--  }-->
<!--}-->

<!--const handleRemove = (file: UploadFile) => {-->
<!--  fileList.value = fileList.value.filter(item => item.uid !== file.uid);-->
<!--  if (file.url) covers.value = covers.value.filter(url => url !== file.url);-->
<!--};-->

<!--const handlePictureCardPreview = (file: UploadFile) => {-->
<!--  dialogImageUrl.value = file.url || '';-->
<!--  dialogVisible.value = true;-->
<!--};-->

<!--async function handleCreatePosting() {-->
<!--  const token = sessionStorage.getItem('token');-->
<!--  if (!token) { ElMessage.error('请先登录!'); return; }-->

<!--  const payload = {-->
<!--    userId: Number(sessionStorage.getItem('userId')),-->
<!--    title: title.value,-->
<!--    content: content.value,-->
<!--    covers: covers.value,-->
<!--    productIds: selectedProductIds.value,-->
<!--  };-->

<!--  try {-->
<!--    const res = await createPosting(payload);-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage.success('创建笔记成功');-->
<!--      title.value = '';-->
<!--      content.value = '';-->
<!--      covers.value = [];-->
<!--      fileList.value = [];-->
<!--      selectedProductIds.value = [];-->
<!--    } else { ElMessage.error(res.data.message); }-->
<!--  } catch (error) { ElMessage.error('创建笔记失败'); }-->
<!--}-->

<!--onMounted(async () => {-->
<!--  try {-->
<!--    const res = await getAllProduct();-->
<!--    if(res.data.code == 200) {-->
<!--      products.value = res.data.data;-->
<!--    }-->
<!--  } catch(error) {-->
<!--    console.error("获取商品列表失败:", error);-->
<!--  }-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.create_posting {-->
<!--  background-color: #000; /* 黑色背景 */-->
<!--  min-height: 100vh;-->
<!--  padding: 40px 0;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--}-->

<!--.create-posting-title {-->
<!--  color: white;-->
<!--  font-size: 28px;-->
<!--  margin-bottom: 30px;-->
<!--}-->

<!--.posting-form {-->
<!--  width: 500px;-->
<!--  background-color: #1f1f1f;-->
<!--  padding: 25px;-->
<!--  border-radius: 8px;-->
<!--  box-shadow: 0 0 15px rgba(255,255,255,0.1);-->
<!--}-->

<!--.el-form-item__label {-->
<!--  color: #fff;-->
<!--}-->

<!--.dialog-image {-->
<!--  max-width: 100%;-->
<!--}-->
<template>
  <div class="page-container">
    <div class="create-card">
      <div class="card-header">
        <h2>发布笔记</h2>
      </div>

      <div class="card-body">
        <!-- 左侧：图片上传区 -->
        <div class="left-section">
          <div class="upload-wrapper">
            <h3 class="section-label">图片编辑</h3>
            <div class="upload-box">
              <el-upload
                  action="http://localhost:8080/api/images"
                  list-type="picture-card"
                  :auto-upload="false"
                  :file-list="fileList"
                  :on-change="handleChange"
                  :on-remove="handleRemove"
                  :on-preview="handlePictureCardPreview"
                  multiple
                  class="custom-upload"
              >
                <div class="upload-placeholder">
                  <el-icon class="upload-icon"><Plus /></el-icon>
                  <span>上传图片</span>
                </div>
              </el-upload>
            </div>
            <p class="upload-tip">支持 JPG/PNG，单张不超过 1MB</p>
          </div>
        </div>

        <!-- 右侧：内容编辑区 -->
        <div class="right-section">
          <el-form ref="form" class="editor-form">

            <!-- 标题输入：大字体，无边框风格 -->
            <div class="input-group">
              <el-input
                  v-model="title"
                  placeholder="填写标题会有更多人看哦"
                  class="title-input"
                  maxlength="20"
                  show-word-limit
              ></el-input>
            </div>

            <!-- 正文输入：多行，舒适间距 -->
            <div class="input-group content-group">
              <el-input
                  type="textarea"
                  v-model="content"
                  placeholder="请输入正文内容..."
                  :rows="8"
                  class="content-input"
                  resize="none"
              ></el-input>
            </div>

            <el-divider border-style="dashed" />

            <!-- 关联商品：标签式选择 -->
            <div class="option-item">
              <span class="option-label">关联商品</span>
              <el-select
                  v-model="selectedProductIds"
                  multiple
                  filterable
                  placeholder="搜索并关联提到的书籍..."
                  class="product-select"
                  tag-type="info"
              >
                <el-option
                    v-for="item in products"
                    :key="item.id"
                    :label="item.title"
                    :value="item.id"
                />
              </el-select>
            </div>

            <!-- 底部发布按钮 -->
            <div class="submit-area">
              <el-button
                  @click.prevent="handleCreatePosting"
                  :disabled="createDisabled"
                  class="publish-btn"
                  round
              >
                发布笔记
              </el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 图片预览 Dialog -->
    <el-dialog v-model="dialogVisible" width="50%">
      <img class="dialog-image" :src="dialogImageUrl" alt="Preview" />
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // 引入路由
import { ElMessage, UploadFile } from 'element-plus';
import { createPosting } from "../../api/posting";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
import { Product, getAllProduct } from "../../api/product.ts";

const router = useRouter(); // 初始化路由

// --- 保持原有的逻辑和变量名不变 ---
const title = ref('');
const content = ref('');
const covers = ref<string[]>([]);
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

const products = ref<Product[]>([]);
const selectedProductIds = ref<number[]>([]);

const createDisabled = computed(() => {
  return !title.value || !content.value || covers.value.length === 0;
});
const MAX_SIZE = 1024 * 1024; // 1MB

async function handleChange(file: UploadFile, newFileList: UploadFile[]) {
  const rawFile = file.raw;
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }

  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）');
    return;
  }

  try {
    const res = await getImage(rawFile);
    if (res && res.code === '200') {
      covers.value.push(res.data);
      file.url = res.data;
      fileList.value = newFileList;
      ElMessage.success('上传成功');
    } else {
      ElMessage.error('上传失败，请重试');
      fileList.value = newFileList.filter(f => f.uid !== file.uid);
    }
  } catch (error) {
    ElMessage.error('上传异常，请重试');
    fileList.value = newFileList.filter(f => f.uid !== file.uid);
  }
}

const handleRemove = (file: UploadFile) => {
  fileList.value = fileList.value.filter(item => item.uid !== file.uid);
  if (file.url) covers.value = covers.value.filter(url => url !== file.url);
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};

async function handleCreatePosting() {
  const token = sessionStorage.getItem('token');
  if (!token) { ElMessage.error('请先登录!'); return; }

  const payload = {
    userId: Number(sessionStorage.getItem('userId')),
    title: title.value,
    content: content.value,
    covers: covers.value,
    productIds: selectedProductIds.value,
  };

  try {
    const res = await createPosting(payload);
    if (res.data.code === '200') {
      ElMessage.success('发布成功！');

      // 清空表单
      title.value = '';
      content.value = '';
      covers.value = [];
      fileList.value = [];
      selectedProductIds.value = [];

      // === 跳转到所有笔记页面 ===
      router.push('/home/all-postings');

    } else { ElMessage.error(res.data.message); }
  } catch (error) { ElMessage.error('创建笔记失败'); }
}

onMounted(async () => {
  try {
    const res = await getAllProduct();
    if(res.data.code == 200) {
      products.value = res.data.data;
    }
  } catch(error) {
    console.error("获取商品列表失败:", error);
  }
});
</script>

<style scoped>
/* ======== 1. 全局容器与背景 ======== */
.page-container {
  background-color: #f5f7fa; /* 统一浅灰背景 */
  min-height: 100vh;
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

/* ======== 2. 主卡片样式 ======== */
.create-card {
  background: #fff;
  width: 100%;
  max-width: 1100px; /* 宽大的卡片 */
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.card-header {
  padding: 20px 30px;
  border-bottom: 1px solid #eee;
}

.card-header h2 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.card-body {
  display: flex;
  flex-direction: row;
  min-height: 600px;
}

/* ======== 3. 左侧：图片区 ======== */
.left-section {
  flex: 0 0 60%; /* 左侧占 60% */
  border-right: 1px solid #eee;
  padding: 30px;
  background-color: #fafafa;
}

.section-label {
  font-size: 16px;
  color: #333;
  margin-bottom: 20px;
  font-weight: 500;
}

.upload-box {
  min-height: 300px;
}

/* 深度定制 Element Upload 样式 */
:deep(.el-upload--picture-card) {
  width: 148px;
  height: 148px;
  background-color: #fff;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  transition: all 0.3s;
}

:deep(.el-upload--picture-card:hover) {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  height: 100%;
}

.upload-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

/* ======== 4. 右侧：内容区 ======== */
.right-section {
  flex: 1;
  padding: 30px;
  display: flex;
  flex-direction: column;
}

.editor-form {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.input-group {
  margin-bottom: 20px;
}

/* 标题输入框 */
:deep(.title-input .el-input__wrapper) {
  box-shadow: none !important; /* 去掉边框 */
  border-bottom: 1px solid #eee; /* 只留底边框 */
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
  background: transparent;
}
:deep(.title-input .el-input__inner) {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
:deep(.title-input .el-input__inner::placeholder) {
  color: #999;
  font-weight: normal;
}

/* 正文输入框 */
.content-group {
  flex-grow: 1; /* 撑满剩余空间 */
}
:deep(.content-input .el-textarea__inner) {
  box-shadow: none !important;
  padding: 0;
  font-size: 15px;
  color: #333;
  line-height: 1.6;
  background: transparent;
  border: none;
  resize: none; /* 禁止拖动大小 */
}

/* 选项条目 */
.option-item {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  margin-top: 10px;
}

.option-label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  min-width: 60px;
}

.product-select {
  flex: 1;
}

/* ======== 5. 底部发布按钮 ======== */
.submit-area {
  margin-top: auto; /* 推到底部 */
}

.publish-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 1px;

  /* 黑金风格 */
  background-color: #2c2c2c;
  border-color: #2c2c2c;
  color: #e0c89a;
  font-weight: 600;
  transition: all 0.3s;
}

.publish-btn:hover {
  background-color: #454545;
  border-color: #454545;
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.publish-btn.is-disabled {
  background-color: #f2f2f2;
  border-color: #e0e0e0;
  color: #ccc;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .card-body {
    flex-direction: column;
  }
  .left-section, .right-section {
    flex: none;
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #eee;
  }
}

.dialog-image {
  width: 100%;
  display: block;
}
</style>
<!--</style>-->
<!--<template>-->
<!--  <div class="page-container">-->
<!--    <div class="create-card">-->
<!--      <div class="card-header">-->
<!--        <h2>发布笔记</h2>-->
<!--      </div>-->

<!--      <div class="card-body">-->
<!--        &lt;!&ndash; 左侧：图片上传区 &ndash;&gt;-->
<!--        <div class="left-section">-->
<!--          <div class="upload-wrapper">-->
<!--            <h3 class="section-label">图片编辑</h3>-->
<!--            <div class="upload-box">-->
<!--              <el-upload-->
<!--                  action="http://localhost:8080/api/images"-->
<!--                  list-type="picture-card"-->
<!--                  :auto-upload="false"-->
<!--                  :file-list="fileList"-->
<!--                  :on-change="handleChange"-->
<!--                  :on-remove="handleRemove"-->
<!--                  :on-preview="handlePictureCardPreview"-->
<!--                  multiple-->
<!--                  class="custom-upload"-->
<!--              >-->
<!--                <div class="upload-placeholder">-->
<!--                  <el-icon class="upload-icon"><Plus /></el-icon>-->
<!--                  <span>上传图片</span>-->
<!--                </div>-->
<!--              </el-upload>-->
<!--            </div>-->
<!--            <p class="upload-tip">支持 JPG/PNG，单张不超过 1MB</p>-->
<!--          </div>-->
<!--        </div>-->

<!--        &lt;!&ndash; 右侧：内容编辑区 &ndash;&gt;-->
<!--        <div class="right-section">-->
<!--          <el-form ref="form" class="editor-form">-->

<!--            &lt;!&ndash; 标题输入：大字体，无边框风格 &ndash;&gt;-->
<!--            <div class="input-group">-->
<!--              <el-input-->
<!--                  v-model="title"-->
<!--                  placeholder="填写标题会有更多人看哦"-->
<!--                  class="title-input"-->
<!--                  maxlength="20"-->
<!--                  show-word-limit-->
<!--              ></el-input>-->
<!--            </div>-->

<!--            &lt;!&ndash; 正文输入：多行，舒适间距 &ndash;&gt;-->
<!--            <div class="input-group content-group">-->
<!--              <el-input-->
<!--                  type="textarea"-->
<!--                  v-model="content"-->
<!--                  placeholder="请输入正文内容..."-->
<!--                  :rows="8"-->
<!--                  class="content-input"-->
<!--                  resize="none"-->
<!--              ></el-input>-->
<!--            </div>-->

<!--            <el-divider border-style="dashed" />-->

<!--            &lt;!&ndash; 关联商品：标签式选择 &ndash;&gt;-->
<!--            <div class="option-item">-->
<!--              <span class="option-label">关联商品</span>-->
<!--              <el-select-->
<!--                  v-model="selectedProductIds"-->
<!--                  multiple-->
<!--                  filterable-->
<!--                  placeholder="搜索并关联提到的书籍..."-->
<!--                  class="product-select"-->
<!--                  tag-type="info"-->
<!--              >-->
<!--                <el-option-->
<!--                    v-for="item in products"-->
<!--                    :key="item.id"-->
<!--                    :label="item.title"-->
<!--                    :value="item.id"-->
<!--                />-->
<!--              </el-select>-->
<!--            </div>-->

<!--            &lt;!&ndash; 底部发布按钮 &ndash;&gt;-->
<!--            <div class="submit-area">-->
<!--              <el-button-->
<!--                  @click.prevent="handleCreatePosting"-->
<!--                  :disabled="createDisabled"-->
<!--                  class="publish-btn"-->
<!--                  round-->
<!--              >-->
<!--                发布笔记-->
<!--              </el-button>-->
<!--            </div>-->
<!--          </el-form>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; 图片预览 Dialog &ndash;&gt;-->
<!--    <el-dialog v-model="dialogVisible" width="50%">-->
<!--      <img class="dialog-image" :src="dialogImageUrl" alt="Preview" />-->
<!--    </el-dialog>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed, onMounted } from 'vue';-->
<!--import { ElMessage, UploadFile } from 'element-plus';-->
<!--import { createPosting } from "../../api/posting";-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--import { Product, getAllProduct } from "../../api/product.ts";-->

<!--// -&#45;&#45; 保持原有的逻辑和变量名不变 -&#45;&#45;-->
<!--const title = ref('');-->
<!--const content = ref('');-->
<!--const covers = ref<string[]>([]);-->
<!--const fileList = ref<UploadFile[]>([]);-->
<!--const dialogImageUrl = ref('');-->
<!--const dialogVisible = ref(false);-->

<!--const products = ref<Product[]>([]);-->
<!--const selectedProductIds = ref<number[]>([]);-->

<!--const createDisabled = computed(() => {-->
<!--  return !title.value || !content.value || covers.value.length === 0;-->
<!--});-->
<!--const MAX_SIZE = 1024 * 1024; // 1MB-->

<!--async function handleChange(file: UploadFile, newFileList: UploadFile[]) {-->
<!--  const rawFile = file.raw;-->
<!--  if (!rawFile) {-->
<!--    ElMessage.error('无法获取文件');-->
<!--    return;-->
<!--  }-->

<!--  if (rawFile.size > MAX_SIZE) {-->
<!--    ElMessage.error('文件超过最大大小限制（1MB）');-->
<!--    return;-->
<!--  }-->

<!--  try {-->
<!--    const res = await getImage(rawFile);-->
<!--    if (res && res.code === '200') {-->
<!--      covers.value.push(res.data);-->
<!--      file.url = res.data;-->
<!--      fileList.value = newFileList;-->
<!--      ElMessage.success('上传成功');-->
<!--    } else {-->
<!--      ElMessage.error('上传失败，请重试');-->
<!--      fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('上传异常，请重试');-->
<!--    fileList.value = newFileList.filter(f => f.uid !== file.uid);-->
<!--  }-->
<!--}-->

<!--const handleRemove = (file: UploadFile) => {-->
<!--  fileList.value = fileList.value.filter(item => item.uid !== file.uid);-->
<!--  if (file.url) covers.value = covers.value.filter(url => url !== file.url);-->
<!--};-->

<!--const handlePictureCardPreview = (file: UploadFile) => {-->
<!--  dialogImageUrl.value = file.url || '';-->
<!--  dialogVisible.value = true;-->
<!--};-->

<!--async function handleCreatePosting() {-->
<!--  const token = sessionStorage.getItem('token');-->
<!--  if (!token) { ElMessage.error('请先登录!'); return; }-->

<!--  const payload = {-->
<!--    userId: Number(sessionStorage.getItem('userId')),-->
<!--    title: title.value,-->
<!--    content: content.value,-->
<!--    covers: covers.value,-->
<!--    productIds: selectedProductIds.value,-->
<!--  };-->

<!--  try {-->
<!--    const res = await createPosting(payload);-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage.success('发布成功！'); // 稍微改了下提示语，更亲切-->
<!--      title.value = '';-->
<!--      content.value = '';-->
<!--      covers.value = [];-->
<!--      fileList.value = [];-->
<!--      selectedProductIds.value = [];-->
<!--    } else { ElMessage.error(res.data.message); }-->
<!--  } catch (error) { ElMessage.error('创建笔记失败'); }-->
<!--}-->

<!--onMounted(async () => {-->
<!--  try {-->
<!--    const res = await getAllProduct();-->
<!--    if(res.data.code == 200) {-->
<!--      products.value = res.data.data;-->
<!--    }-->
<!--  } catch(error) {-->
<!--    console.error("获取商品列表失败:", error);-->
<!--  }-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--/* ======== 1. 全局容器与背景 ======== */-->
<!--.page-container {-->
<!--  background-color: #f5f7fa; /* 统一浅灰背景 */-->
<!--  min-height: 100vh;-->
<!--  padding: 40px 20px;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;-->
<!--}-->

<!--/* ======== 2. 主卡片样式 ======== */-->
<!--.create-card {-->
<!--  background: #fff;-->
<!--  width: 100%;-->
<!--  max-width: 1100px; /* 宽大的卡片 */-->
<!--  border-radius: 16px;-->
<!--  box-shadow: 0 4px 20px rgba(0,0,0,0.05);-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.card-header {-->
<!--  padding: 20px 30px;-->
<!--  border-bottom: 1px solid #eee;-->
<!--}-->

<!--.card-header h2 {-->
<!--  font-size: 18px;-->
<!--  font-weight: 600;-->
<!--  color: #333;-->
<!--  margin: 0;-->
<!--}-->

<!--.card-body {-->
<!--  display: flex;-->
<!--  flex-direction: row;-->
<!--  min-height: 600px;-->
<!--}-->

<!--/* ======== 3. 左侧：图片区 ======== */-->
<!--.left-section {-->
<!--  flex: 0 0 60%; /* 左侧占 60% */-->
<!--  border-right: 1px solid #eee;-->
<!--  padding: 30px;-->
<!--  background-color: #fafafa;-->
<!--}-->

<!--.section-label {-->
<!--  font-size: 16px;-->
<!--  color: #333;-->
<!--  margin-bottom: 20px;-->
<!--  font-weight: 500;-->
<!--}-->

<!--.upload-box {-->
<!--  min-height: 300px;-->
<!--}-->

<!--/* 深度定制 Element Upload 样式 */-->
<!--:deep(.el-upload&#45;&#45;picture-card) {-->
<!--  width: 148px;-->
<!--  height: 148px;-->
<!--  background-color: #fff;-->
<!--  border: 2px dashed #dcdfe6;-->
<!--  border-radius: 8px;-->
<!--  transition: all 0.3s;-->
<!--}-->

<!--:deep(.el-upload&#45;&#45;picture-card:hover) {-->
<!--  border-color: #409eff;-->
<!--  background-color: #ecf5ff;-->
<!--}-->

<!--.upload-placeholder {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--  color: #909399;-->
<!--  height: 100%;-->
<!--}-->

<!--.upload-icon {-->
<!--  font-size: 24px;-->
<!--  margin-bottom: 8px;-->
<!--}-->

<!--.upload-tip {-->
<!--  margin-top: 10px;-->
<!--  font-size: 12px;-->
<!--  color: #999;-->
<!--}-->

<!--/* ======== 4. 右侧：内容区 ======== */-->
<!--.right-section {-->
<!--  flex: 1;-->
<!--  padding: 30px;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--}-->

<!--.editor-form {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  height: 100%;-->
<!--}-->

<!--.input-group {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--/* 标题输入框 */-->
<!--:deep(.title-input .el-input__wrapper) {-->
<!--  box-shadow: none !important; /* 去掉边框 */-->
<!--  border-bottom: 1px solid #eee; /* 只留底边框 */-->
<!--  border-radius: 0;-->
<!--  padding-left: 0;-->
<!--  padding-right: 0;-->
<!--  background: transparent;-->
<!--}-->
<!--:deep(.title-input .el-input__inner) {-->
<!--  font-size: 20px;-->
<!--  font-weight: bold;-->
<!--  color: #333;-->
<!--}-->
<!--:deep(.title-input .el-input__inner::placeholder) {-->
<!--  color: #999;-->
<!--  font-weight: normal;-->
<!--}-->

<!--/* 正文输入框 */-->
<!--.content-group {-->
<!--  flex-grow: 1; /* 撑满剩余空间 */-->
<!--}-->
<!--:deep(.content-input .el-textarea__inner) {-->
<!--  box-shadow: none !important;-->
<!--  padding: 0;-->
<!--  font-size: 15px;-->
<!--  color: #333;-->
<!--  line-height: 1.6;-->
<!--  background: transparent;-->
<!--  border: none;-->
<!--  resize: none; /* 禁止拖动大小 */-->
<!--}-->

<!--/* 选项条目 */-->
<!--.option-item {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 15px;-->
<!--  margin-bottom: 20px;-->
<!--  margin-top: 10px;-->
<!--}-->

<!--.option-label {-->
<!--  font-size: 14px;-->
<!--  color: #333;-->
<!--  font-weight: 500;-->
<!--  min-width: 60px;-->
<!--}-->

<!--.product-select {-->
<!--  flex: 1;-->
<!--}-->

<!--/* ======== 5. 底部发布按钮 ======== */-->
<!--.submit-area {-->
<!--  margin-top: auto; /* 推到底部 */-->
<!--}-->

<!--.publish-btn {-->
<!--  width: 100%;-->
<!--  height: 44px;-->
<!--  font-size: 16px;-->
<!--  letter-spacing: 1px;-->

<!--  /* 黑金风格 */-->
<!--  background-color: #2c2c2c;-->
<!--  border-color: #2c2c2c;-->
<!--  color: #e0c89a;-->
<!--  font-weight: 600;-->
<!--  transition: all 0.3s;-->
<!--}-->

<!--.publish-btn:hover {-->
<!--  background-color: #454545;-->
<!--  border-color: #454545;-->
<!--  color: #fff;-->
<!--  transform: translateY(-2px);-->
<!--  box-shadow: 0 4px 12px rgba(0,0,0,0.15);-->
<!--}-->

<!--.publish-btn.is-disabled {-->
<!--  background-color: #f2f2f2;-->
<!--  border-color: #e0e0e0;-->
<!--  color: #ccc;-->
<!--}-->

<!--/* 响应式适配 */-->
<!--@media (max-width: 768px) {-->
<!--  .card-body {-->
<!--    flex-direction: column;-->
<!--  }-->
<!--  .left-section, .right-section {-->
<!--    flex: none;-->
<!--    width: 100%;-->
<!--    border-right: none;-->
<!--    border-bottom: 1px solid #eee;-->
<!--  }-->
<!--}-->

<!--.dialog-image {-->
<!--  width: 100%;-->
<!--  display: block;-->
<!--}-->
<!--</style>-->