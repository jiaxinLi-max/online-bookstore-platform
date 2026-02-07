<template>
  <div class="create_product bgimage">
    <h1 class="create-product-title">创建商品</h1>
    <el-form ref="form" label-width="120px" class="product-form">
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="title" placeholder="请输入商品名称"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="商品价格" prop="productPrice">
        <el-input v-model="price" placeholder="请输入商品价格" type="number"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="商品评分" prop="productRate">
        <el-input v-model="rate" placeholder="请输入商品评分" type="number"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="商品描述" prop="productDescription">
        <el-input v-model="description" placeholder="请输入商品描述"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="商品详细说明" prop="productDetail">
        <el-input v-model="detail" placeholder="请输入商品详细说明"
                  style="width: 400px;"
        ></el-input>
      </el-form-item>

      <el-form-item label="所属栏目" prop="columnIds">
        <el-select
            v-model="selectedColumnIds"
            multiple
            placeholder="请选择商品所属的栏目"
            style="width: 400px;"
        >
          <el-option
              v-for="column in allColumns"
              :key="column.id"
              :label="column.theme"
              :value="column.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="规格说明" prop="specifications">
        <div v-for="(spec, index) in specifications" :key="index" class="specification-item">
          <el-input
              v-model="spec.item"
              placeholder="规格名称（如 作者、副标题）"
              style="width: 200px; margin-right: 10px;"
          ></el-input>
          <el-input
              v-model="spec.value"
              placeholder="规格值（如 Robert C. Martin）"
              style="width: 200px; margin-right: 10px;"
          ></el-input>
          <el-button type="danger" @click="removeSpecification(index)">删除</el-button>
        </div>
        <el-button type="primary" @click="addSpecification" style="margin-top: 10px;">添加规格</el-button>
      </el-form-item>

      <el-form-item label="商品封面" prop="covers">
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
          <div>点击上传商品封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>

      <el-form-item>
        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>
          创建商品
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import type { UploadFile } from 'element-plus';
import { createProduct } from "../../api/product";
import { getAllColumns } from "../../api/columns"; // 导入获取栏目的API
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
import { Specification } from "../../api/specification.ts";

// 商品基本信息
const title = ref('');
const description = ref('');
const detail = ref('');
const price = ref<number | null>(null);
const rate = ref<number | null>(null);
const specifications = ref<Specification[]>([]);
const MAX_SIZE = 1024 * 1024; // 1MB

// 【修改点1】: 封面改为数组
const cover = ref<string[]>([]);

// 【新增点1】: 栏目相关状态
const allColumns = ref<{id: number, theme: string}[]>([]); // 存储所有栏目
const selectedColumnIds = ref<number[]>([]); // 存储选中的栏目ID

// el-upload 相关状态
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

// 计算属性，检查输入状态
const createDisabled = computed(() => {
  return !title.value || !description.value || price.value === null || cover.value.length === 0;
});

// 添加和删除规格的函数
function addSpecification() {
  specifications.value.push({ item: '', value: '' });
}
function removeSpecification(index: number) {
  specifications.value.splice(index, 1);
}

// 【修改点2】: 更新handleChange以支持多图
async function handleChange(file: UploadFile, newFileList: UploadFile[]) {
  const rawFile = file.raw;
  if (!rawFile) return;
  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）');
    return;
  }

  try {
    const res = await getImage(rawFile);
    if (res && res.code === '200') {
      console.log("上传",res.data);
      cover.value.push(res.data); // 添加URL到数组
      file.url = res.data;
      fileList.value = newFileList;
      ElMessage.success('上传成功');
    } else {
      newFileList.pop(); // 移除上传失败的文件
      fileList.value = newFileList;
      ElMessage.error('上传失败');
    }
  } catch (error) {
    newFileList.pop();
    fileList.value = newFileList;
    ElMessage.error('上传异常');
  }
}

// 【修改点3】: 更新handleRemove以支持多图
const handleRemove = (file: UploadFile) => {
  const urlToRemove = file.url;
  // 从两个数组中都移除
  fileList.value = fileList.value.filter(item => item.uid !== file.uid);
  if (urlToRemove) {
    cover.value = cover.value.filter(url => url !== urlToRemove);
    console.log(urlToRemove);
    console.log(cover.value);
  }
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || '';
  dialogVisible.value = true;
};

async function handleCreateProduct() {
  const token = sessionStorage.getItem('token');
  if (!token) {
    ElMessage.error('请先登录!');
    return;
  }

  // 【修改点4】: 更新payload以包含covers和columnIds
  const payload = {
    title: title.value,
    price: price.value ?? 0,
    rate: rate.value ?? 0,
    description: description.value,
    cover: cover.value, // 发送图片URL数组
    detail: detail.value,
    specifications: specifications.value,
    columnIds: selectedColumnIds.value, // 发送栏目ID数组
    // amount和frozen如果不需要可以不传
  };

  try {
    const res = await createProduct(payload);
    if (res.data.code === '200') {
      ElMessage.success('创建商品成功');
      // 重置表单
      title.value = '';
      description.value = '';
      detail.value = '';
      price.value = null;
      rate.value = null;
      cover.value = [];
      fileList.value = [];
      specifications.value = [];
      selectedColumnIds.value = [];
    } else {
      ElMessage.error(res.data.message || '创建失败');
    }
  } catch (error) {
    console.log("error",error);
    ElMessage.error('创建商品失败');
  }
}

// 【新增点2】: 组件挂载时获取所有栏目
onMounted(async () => {
  try {
    const res = await getAllColumns();
    if (res.data.code === '200') {
      allColumns.value = res.data.data;
    } else {
      ElMessage.error("获取栏目列表失败");
    }
  } catch (error) {
    ElMessage.error("获取栏目列表失败");
  }
});
</script>

<style scoped>
/* 样式与CreateAdvertisement.vue保持一致 */
.create-product-title {
  text-align: center;
  margin-bottom: 20px;
  color: white;
}
.product-form {
  max-width: 600px;
  margin: auto;
}
.dialog-image {
  max-width: 100%;
}
.specification-item {
  display: flex;
  margin-bottom: 10px;
}
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  min-height: 100vh;
  padding: 20px;
}
:deep(.el-form-item__label) {
  color: white;
}
</style>