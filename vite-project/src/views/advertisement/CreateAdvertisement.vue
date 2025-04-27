

<template>
  <div class="create_product bgimage">
    <h1 class="create-product-title">创建广告</h1>
    <el-form ref="form" label-width="120px" class="product-form">
      <!-- 基本信息 -->
      <el-form-item label="广告标题" prop="advertisementName" >
        <el-input v-model="title" placeholder="请输入广告标题" ></el-input>
      </el-form-item>

      <el-form-item label="广告内容" prop="advertisementDescription">
        <el-input v-model="content" placeholder="请输入广告内容"></el-input>
      </el-form-item>

      <!-- 商品 ID -->
<!--      <el-form-item label="商品 ID" prop="productId">-->
<!--        <el-input v-model="productId" placeholder="请输入商品 ID"></el-input>-->
<!--      </el-form-item>-->
      <!-- 商品选择下拉框 -->
      <el-form-item label="选择商品" prop="productId">
        <el-select v-model="selectedProductId" placeholder="请选择商品" style="width: 200px; margin-bottom: 20px;">
          <el-option
              v-for="product in products"
              :key="product.id"
              :label="product.title"
              :value="product.id"
          />
        </el-select>
      </el-form-item>


      <!-- 商品封面 -->
      <el-form-item label="广告图片" prop="cover">
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
          <div>点击上传广告图片</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>

      <!-- 按钮 -->
<!--      <el-form-item>-->
<!--        <el-button @click.prevent="handleCreateAdvertisement" :disabled="createDisabled" type="primary" plain>-->
<!--          创建广告-->
<!--        </el-button>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button
            @click.prevent="handleCreateAdvertisement"
            :disabled="createDisabled"
            type="primary"
            plain
            class="custom-black-button"
        >
          创建广告
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed,onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import type { UploadFile } from 'element-plus';
import { createAdvertisement} from "../../api/advertisement.ts";
import {getAllProduct,Product} from "../../api/product.ts";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';

const title = ref('');
const imgUrl = ref('');
const content = ref('');
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const productId = ref('');
const products = ref<Product[]>([]); // 存储商品列表
const selectedProductId = ref<number | null>(null); // 修改为对应的类型

// 计算属性，检查输入状态
const createDisabled = computed(() => {
  return !title.value || !content.value  || !imgUrl.value;
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
      imgUrl.value = res.data; // 设置封面
      fileListNew[0].url = imgUrl.value; // 更新文件列表的URL
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

async function handleCreateAdvertisement() {
  const token = sessionStorage.getItem('token');
  if (!token) {
    ElMessage.error('请先登录!');
    return;
  }

  const payload = {
    title: title.value,
    content: content.value,
    imgUrl: imgUrl.value,
    //productId: productId.value,
    productId: selectedProductId.value, // 使用选中的商品 ID
  };

  try {
    const res = await createAdvertisement(payload);
    if (res.data.code === '200') {
      ElMessage.success('创建广告成功');
      // 重置输入框
      title.value = '';
      imgUrl.value = '';
      fileList.value = [];
      selectedProductId.value = null; // 重置已选择的商品
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.log("error",error);
    ElMessage.error('创建广告失败');
  }
}
// 获取所有商品数据
async function getAllProductsData() {
  try {
    const res = await getAllProduct();
    if (res.data && Array.isArray(res.data.data)) {
      products.value = res.data.data;
    } else {
      console.error('获取商品数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取商品列表失败:', error);
  }
}
onMounted(() => {
  getAllProductsData();
});
</script>

<style scoped>
.create-product-title {
  margin-left: 25%;
}

.dialog-image {
  max-width: 100%;
}

.specification-item {
  margin-bottom: 10px;
}
.custom-black-button {
  background-color: #000000 !important; /* 设置按钮背景为黑色 */
  border-color: #000000 !important; /* 设置按钮边框为黑色 */
  color: white !important; /* 设置按钮文字为白色 */
}

.custom-black-button:hover {
  background-color: #333333 !important; /* 鼠标悬停时的背景颜色 */
  border-color: #333333 !important; /* 鼠标悬停时的边框颜色 */
  color: white !important; /* 鼠标悬停时的文字颜色 */
}
.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/kenan.png");

    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    min-height: 100vh;

}
.create-product-title {
  margin-left: 25%;
  color: white; /* ← 加上这一句 */
}
::v-deep(.el-form-item__label) {
  color: white;
}
</style>
