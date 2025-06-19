

<template>
  <div class="create_posting bgimage">
    <h1 class="create-posting-title">创建帖子</h1>
    <el-form ref="form" label-width="120px" class="posting-form">
      <!-- 基本信息 -->
      <el-form-item label="帖子标题" prop="postingTitle">
        <el-input v-model="title" placeholder="请输入帖子标题"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="帖子内容" prop="postingContent">
        <el-input v-model="content" placeholder="请输入内容"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <!-- 商品封面 -->
      <el-form-item label="帖子封面" prop="cover">
        <el-upload
            action="http://localhost:8080/api/images"
            list-type="picture-card"
            :auto-upload="true"
            :file-list="fileList"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-preview="handlePictureCardPreview"
        >
          <el-icon><Plus /></el-icon>
          <div>点击上传帖子封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>


      <!-- 按钮 -->
      <el-form-item>
        <el-button @click.prevent="handleCreatePosting" :disabled="createDisabled" type="primary" plain>
          创建帖子
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed ,onMounted} from 'vue';
import {ElMessage, type UploadFile} from 'element-plus';

import { createPosting } from "../../api/posting";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
import {Product,getAllProduct} from "../../api/product.ts";

const title = ref('');
const content = ref('');
const cover = ref('');

const products = ref<Product[]>([]); // 存储商品列表

const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

// 计算属性，检查输入状态
const createDisabled = computed(() => {
  return !title.value || !content.value || !cover.value;
});

async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
  const rawFile = file.raw;
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }

  try {
    const res = await getImage(rawFile);
    if (res && res.code === '200') {
      cover.value = res.data; // 设置封面
      fileListNew[0].url = cover.value; // 更新文件列表的URL
      fileList.value = fileListNew; // 更新文件列表
      ElMessage.success('上传成功');
    } else {
      ElMessage.error('上传失败，请重试');
    }
  } catch (error) {
    ElMessage.error('上传失败，请重试');
  }
}

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};

const handleRemove = (file: UploadFile) => {
  fileList.value = fileList.value.filter(item => item.uid !== file.uid);
};

async function handleCreatePosting() {
  const token = sessionStorage.getItem('token');
  if (!token) {
    ElMessage.error('请先登录!');
    return;
  }

  const payload = {
    userId: Number(sessionStorage.getItem('userId')),
    title: title.value,
    content: content.value,
    cover: cover.value,
  };

  try {
    const res = await createPosting(payload);
    if (res.data.code === '200') {
      ElMessage.success('创建帖子成功');
      // 重置输入框
      title.value = '';
      content.value = '';
      cover.value = '';
      fileList.value = [];
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.log("error",error);
    ElMessage.error('创建帖子失败');
  }
}

</script>

<style scoped>
.create-posting-title {
  margin-left: 25%;
}

.dialog-image {
  max-width: 100%;
}

.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/780.jpg");

  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  min-height: 100vh;

}
.create-posting-title {
  margin-left: 25%;
  color: white; /* ← 加上这一句 */
}
::v-deep(.el-form-item__label) {
  color: white;
}
</style>
