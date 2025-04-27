

<template>
  <div class="create_product bgimage">
    <h1 class="create-product-title">创建商品</h1>
    <el-form ref="form" label-width="120px" class="product-form">
      <!-- 基本信息 -->
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="title" placeholder="请输入商品名称"></el-input>
      </el-form-item>

      <el-form-item label="商品价格" prop="productPrice">
        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>
      </el-form-item>

      <el-form-item label="商品评分" prop="productRate">
        <el-input v-model="rate" placeholder="请输入商品评分" type="number"></el-input>
      </el-form-item>

      <el-form-item label="商品描述" prop="productDescription">
        <el-input v-model="description" placeholder="请输入商品描述"></el-input>
      </el-form-item>

      <el-form-item label="商品详细说明" prop="productDetail">
        <el-input v-model="detail" placeholder="请输入商品详细说明"></el-input>
      </el-form-item>

      <!-- 规格说明类 -->
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
<!--        <el-button type="primary" @click="addSpecification">添加规格</el-button>-->
      </el-form-item>

      <!-- 添加规格按钮另起一行 -->
      <el-form-item>
        <el-button type="primary" @click="addSpecification">添加规格</el-button>
      </el-form-item>

      <!-- 商品封面 -->
      <el-form-item label="商品封面" prop="cover">
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
          <div>点击上传商品封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>

      <!-- 按钮 -->
      <el-form-item>
        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>
          创建商品
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import type { UploadFile } from 'element-plus';
import { createProduct } from "../../api/product";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口

const title = ref('');
const description = ref('');
const detail = ref('');
const price = ref<number | null>(null);
const rate = ref<number | null>(null);
const cover = ref('');
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const amount = ref<number | null>(null); // 库存数量
const frozen = ref<number | null>(null); // 冻结库存数量
const specifications = ref<Specification[]>([]); // 用于存储规格说明

// 计算属性，检查输入状态
const createDisabled = computed(() => {
  return !title.value || !description.value || price.value === null || specifications.value.length === 0 || !cover.value;
});

const productId = ref(''); // 设置产品 ID

function addSpecification() {
  specifications.value.push({
    //id: productId, // 生成唯一的 id
    item: '',     // 新规格的名称
    value: '',    // 新规格的值
    //productId: productId.value, // 设置与产品的关联
  });
}

// 删除规格说明
function removeSpecification(index: number) {
  specifications.value.splice(index, 1); // 删除指定索引的规格
}

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

async function handleCreateProduct() {
  const token = sessionStorage.getItem('token');
  if (!token) {
    ElMessage.error('请先登录!');
    return;
  }

  const payload = {
    title: title.value,
    price: price.value ?? 0,
    rate: rate.value ?? 0,
    description: description.value,
    cover: cover.value,
    detail: detail.value,
    specifications: specifications.value, // 添加规格信息
    amount: amount.value ?? 0, // 库存数量
    frozen: frozen.value ?? 0, // 冻结库存数量
  };

  try {
    const res = await createProduct(payload);
    if (res.data.code === '200') {
      ElMessage.success('创建商品成功');
      // 重置输入框
      title.value = '';
      description.value = '';
      detail.value = '';
      price.value = null;
      rate.value = null;
      cover.value = '';
      fileList.value = [];
      amount.value = null; // 重置库存数量
      frozen.value = null; // 重置冻结库存数量
      specifications.value = []; // 重置规格
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.log("error",error);
    ElMessage.error('创建商品失败');
  }
}
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
