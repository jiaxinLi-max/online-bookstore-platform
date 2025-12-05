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
<template>
  <div class="create_posting">
    <h1 class="create-posting-title">创建笔记</h1>
    <el-form ref="form" label-width="120px" class="posting-form">
      <el-form-item label="笔记标题" prop="postingTitle">
        <el-input v-model="title" placeholder="请输入笔记标题"></el-input>
      </el-form-item>

      <el-form-item label="笔记内容" prop="postingContent">
        <el-input
            type="textarea"
            v-model="content"
            placeholder="请输入内容"
            :rows="6"
        ></el-input>
      </el-form-item>

      <el-form-item label="关联商品" prop="productIds">
        <el-select
            v-model="selectedProductIds"
            multiple
            filterable
            placeholder="请搜索并选择要关联的商品"
        >
          <el-option
              v-for="item in products"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="笔记封面" prop="covers">
        <el-upload
            action="http://localhost:8080/api/images"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-preview="handlePictureCardPreview"
            multiple
        >
          <el-icon><Plus /></el-icon>
          <div>点击上传笔记封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>

      <el-form-item>
        <el-button
            @click.prevent="handleCreatePosting"
            :disabled="createDisabled"
            type="primary"
        >
          创建笔记
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage, UploadFile } from 'element-plus';
import { createPosting } from "../../api/posting";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
import { Product, getAllProduct } from "../../api/product.ts";

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
      ElMessage.success('创建笔记成功');
      title.value = '';
      content.value = '';
      covers.value = [];
      fileList.value = [];
      selectedProductIds.value = [];
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
.create_posting {
  background-color: #000; /* 黑色背景 */
  min-height: 100vh;
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.create-posting-title {
  color: white;
  font-size: 28px;
  margin-bottom: 30px;
}

.posting-form {
  width: 500px;
  background-color: #1f1f1f;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(255,255,255,0.1);
}

.el-form-item__label {
  color: #fff;
}

.dialog-image {
  max-width: 100%;
}
</style>
