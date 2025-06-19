<template>
  <div class="create_columns bgimage">
    <div
        class="header-wrapper"
        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;"
    >
      <el-button
          class="custom-black-button"
          type="primary"
          plain
          @click="goBack"
      >
        返回所有栏目
      </el-button>

      <h1 class="create-title" style="margin: 0 auto;">创建栏目</h1>
    </div>
    <el-form ref="form" label-width="120px" class="product-form">
      <el-form-item label="栏目主题" prop="theme">
        <el-input v-model="theme" placeholder="请输入栏目主题"></el-input>
      </el-form-item>

      <el-form-item label="栏目描述" prop="description">
        <el-input
            v-model="description"
            type="textarea"
            :rows="3"
            placeholder="请输入栏目描述"
        ></el-input>
      </el-form-item>

      <el-form-item label="栏目封面" prop="covers">
        <el-upload
            action="http://localhost:8080/api/images"
            list-type="picture-card"
            :auto-upload="true"
            :file-list="fileList"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-preview="handlePictureCardPreview"
            multiple
        >
          <el-icon><Plus /></el-icon>
          <div>点击上传栏目封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Cover Preview" />
        </el-dialog>
      </el-form-item>

      <el-form-item>
        <el-button
            @click.prevent="handleCreateColumn"
            :disabled="createDisabled"
            type="primary"
            plain
            class="custom-black-button"
        >
          创建栏目
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import type { UploadFile } from 'element-plus';
import { createColumns } from "../../api/columns";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools'; // 假设 getImage 是一个可复用的工具函数
import { useRouter } from 'vue-router';

// 表单数据
const theme = ref('');
const description = ref('');
const covers = ref<string[]>([]); // 存储多个封面图片的URL

// el-upload 相关状态
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

const router = useRouter();

// 计算属性，检查输入状态
const createDisabled = computed(() => {
  return !theme.value || !description.value || covers.value.length === 0;
});

// 文件状态改变时的钩子，处理上传逻辑
async function handleChange(file: UploadFile, newFileList: UploadFile[]) {
  const rawFile = file.raw;
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }

  try {
    const res = await getImage(rawFile); // 调用上传接口
    if (res && res.code === '200') {
      // 因为支持多图，所以将新的url添加到covers数组
      covers.value.push(res.data);
      file.url = res.data; // 更新当前文件的URL用于预览
      fileList.value = newFileList; // 更新文件列表
      ElMessage.success('上传成功');
    } else {
      ElMessage.error('上传失败，请重试');
      // 从列表中移除上传失败的文件
      fileList.value = newFileList.filter(f => f.uid !== file.uid);
    }
  } catch (error) {
    ElMessage.error('上传异常，请重试');
    fileList.value = newFileList.filter(f => f.uid !== file.uid);
  }
}

// 预览图片
const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};

// 移除图片
const handleRemove = (file: UploadFile) => {
  // 从 fileList 和 covers 数组中同时移除
  const newFileList = fileList.value.filter(item => item.uid !== file.uid);
  fileList.value = newFileList;

  // 假设file.url是上传成功后返回的url
  if (file.url) {
    const urlToRemove = file.url;
    covers.value = covers.value.filter(url => url !== urlToRemove);
  }
};

// 创建栏目
async function handleCreateColumn() {
  const payload = {
    theme: theme.value,
    description: description.value,
    covers: covers.value,
  };

  try {
    const res = await createColumns(payload);
    if (res.data.code === "200") {
      ElMessage.success('创建栏目成功');
      // 重置表单
      theme.value = '';
      description.value = '';
      covers.value = [];
      fileList.value = [];
    } else {
      ElMessage.error('创建栏目失败');
    }
  } catch (error) {
    console.log("error", error);
    ElMessage.error('创建栏目失败，请检查网络或联系管理员');
  }
}

// 返回上一页
function goBack() {
  router.push('/home/all-columns'); // 假设这是所有栏目的列表页路由
}
</script>

<style scoped>
.create-title {
  margin: 0 auto; /* 保持居中 */
  color: white; /* 标题文字颜色 */
}

.dialog-image {
  max-width: 100%;
}

.custom-black-button {
  background-color: #000000 !important;
  border-color: #000000 !important;
  color: white !important;
}

.custom-black-button:hover {
  background-color: #333333 !important;
  border-color: #333333 !important;
}

.bgimage {
  background-image: url("../../assets/780.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  min-height: 100vh;
  padding: 20px; /* 增加一些内边距 */
}

/* 使用 ::v-deep 或 :deep() 来穿透设置el-form-item的标签颜色 */
:deep(.el-form-item__label) {
  color: white;
}
</style>