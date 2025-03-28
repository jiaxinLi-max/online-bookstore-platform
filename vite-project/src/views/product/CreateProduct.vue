<!--<template>-->
<!--  <div class="create_product">-->
<!--    <h1 class="create-product-title">创建商品</h1>-->
<!--    <el-form ref="form" label-width="120px" class="product-form">-->
<!--      <el-form-item label="商品名称" prop="productName">-->
<!--        <el-input v-model="name" placeholder="请输入商品名称"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品价格" prop="productPrice">-->
<!--        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品评分" prop="productRate">-->
<!--        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品描述" prop="productDescription">-->
<!--        <el-input v-model="description" placeholder="请输入商品描述"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品详细说明" prop="productDetail">-->
<!--        <el-input v-model="description" placeholder="请输入商品详细说明"></el-input>-->
<!--      </el-form-item>-->



<!--      <el-form-item label="商品封面" prop="cover">-->
<!--        <el-upload-->
<!--            action="http://localhost:3000/api/images"-->
<!--            list-type="picture-card"-->
<!--            :auto-upload="true"-->
<!--            :file-list="fileList"-->
<!--            :on-change="handleChange"-->
<!--            :on-remove="handleRemove"-->
<!--            :on-preview="handlePictureCardPreview"-->
<!--        >-->
<!--          <el-icon><Plus /></el-icon>-->
<!--          <div>点击上传商品 Logo</div>-->
<!--        </el-upload>-->
<!--        <el-dialog v-model="dialogVisible">-->
<!--          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />-->
<!--        </el-dialog>-->
<!--      </el-form-item>-->

<!--      <el-form-item>-->
<!--        <el-button type="default" @click="fetchHelloWorld">测试 Hello World</el-button>-->
<!--        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>-->
<!--          创建商品-->
<!--        </el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed } from 'vue';-->
<!--import { ElMessage } from 'element-plus';-->
<!--import type { UploadFile } from 'element-plus';-->
<!--import { createProduct } from "../../api/product";-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--import {useRoute, useRouter} from "vue-router";-->

<!--const name = ref('');-->
<!--const description = ref('');-->
<!--const price = ref<number | null>(null);-->
<!--const logoUrl = ref('');-->
<!--const fileList = ref<UploadFile[]>([]);-->
<!--const dialogImageUrl = ref('');-->
<!--const dialogVisible = ref(false);-->
<!--const route = useRoute();-->
<!--const router = useRouter();-->
<!--const storeId = Number(route.params.storeId);-->
<!--// 计算属性，检查输入状态-->
<!--const hasNameInput = computed(() => name.value !== '');-->
<!--const hasDescriptionInput = computed(() => description.value !== '');-->
<!--const hasPriceInput = computed(() => price.value !== null && price.value >= 0);-->
<!--const hasImageFile = computed(() => logoUrl.value !== '');-->

<!--const createDisabled = computed(() => {-->
<!--  return !(hasNameInput.value && hasDescriptionInput.value && hasPriceInput.value && hasImageFile.value);-->
<!--});-->

<!--async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {-->
<!--  const rawFile = file.raw;-->
<!--  if (!rawFile) {-->
<!--    ElMessage.error('无法获取文件');-->
<!--    return;-->
<!--  }-->

<!--  try {-->
<!--    const res = await getImage(rawFile);-->
<!--    if (res && res.code === '000') {-->
<!--      logoUrl.value = res.result.trim();-->
<!--      fileListNew[0].url = logoUrl.value;-->
<!--      fileList.value = fileListNew;-->
<!--      ElMessage.success('上传成功');-->
<!--    } else {-->
<!--      ElMessage.error('上传失败，请重试');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('上传失败，请重试');-->
<!--  }-->
<!--}-->

<!--const handlePictureCardPreview = (file: UploadFile) => {-->
<!--  dialogImageUrl.value = file.url || '';-->
<!--  dialogVisible.value = true;-->
<!--};-->

<!--const handleRemove = (file: UploadFile) => {-->
<!--  fileList.value = fileList.value.filter(item => item.uid !== file.uid);-->
<!--};-->

<!--async function handleCreateProduct() {-->
<!--  const token = sessionStorage.getItem('token');-->
<!--  if (!token) {-->
<!--    ElMessage.error('请先登录!');-->
<!--    return;-->
<!--  }-->

<!--  const payload = {-->
<!--    name: name.value,-->
<!--    description: description.value,-->
<!--    price: price.value,-->
<!--    logoUrl: logoUrl.value-->
<!--  };-->

<!--  try {-->
<!--    console.log(payload);-->
<!--    const res = await createProduct(payload,storeId);-->
<!--    if (res.data.code === '000') {-->
<!--      ElMessage.success('创建商品成功');-->
<!--      name.value = '';-->
<!--      description.value = '';-->
<!--      price.value = null;-->
<!--      logoUrl.value = '';-->
<!--      fileList.value = [];-->
<!--    } else {-->
<!--      ElMessage.error(res.data.message);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('创建商品失败');-->
<!--    console.log(error.message);-->
<!--  }-->
<!--}-->

<!--async function fetchHelloWorld() {-->
<!--  try {-->
<!--    const response = await fetch('http://localhost:3000/api/products/hello', {-->
<!--      method: 'POST',-->
<!--      headers: {-->
<!--        'Content-Type': 'application/json'-->
<!--      }-->
<!--    });-->

<!--    if (!response.ok) {-->
<!--      throw new Error('网络响应不正常');-->
<!--    }-->

<!--    const data = await response.text();-->
<!--    ElMessage.success(data);-->
<!--  } catch (error) {-->
<!--    ElMessage.error('获取 Hello World 失败');-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--.create-product-title {-->
<!--  margin-left: 25%;-->
<!--}-->

<!--.dialog-image {-->
<!--  max-width: 100%;-->
<!--}-->
<!--</style>-->



<template>
  <div class="create_product">
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
      <el-form-item label="书名" prop="bookTitle">
        <el-input v-model="bookTitle" placeholder="请输入书名"></el-input>
      </el-form-item>

      <el-form-item label="副标题" prop="subtitle">
        <el-input v-model="subtitle" placeholder="请输入副标题"></el-input>
      </el-form-item>

      <el-form-item label="作者" prop="author">
        <el-input v-model="author" placeholder="请输入作者"></el-input>
      </el-form-item>

      <el-form-item label="ISBN" prop="isbn">
        <el-input v-model="isbn" placeholder="请输入ISBN"></el-input>
      </el-form-item>

      <el-form-item label="装帧方式" prop="binding">
        <el-input v-model="binding" placeholder="请输入装帧方式"></el-input>
      </el-form-item>

      <el-form-item label="页数" prop="pages">
        <el-input v-model="pages" placeholder="请输入页数" type="number"></el-input>
      </el-form-item>

      <el-form-item label="出版年月" prop="publicationDate">
        <el-date-picker v-model="publicationDate" type="date" placeholder="选择出版日期"></el-date-picker>
      </el-form-item>

      <el-form-item label="出版社" prop="publisher">
        <el-input v-model="publisher" placeholder="请输入出版社"></el-input>
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
          <div>点击上传商品 封面</div>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
        </el-dialog>
      </el-form-item>

      <!-- 按钮 -->
      <el-form-item>
        <el-button type="default" @click="fetchHelloWorld">测试 Hello World</el-button>
        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>
          创建商品
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { ElMessage, ElDatePicker } from 'element-plus';
import type { UploadFile } from 'element-plus';
import { createProduct } from "../../api/product";
import { Plus } from '@element-plus/icons-vue';
import { getImage } from '../../api/tools';
// import { useRoute, useRouter } from "vue-router";
// import {Specification} from "../../api/specification.ts";

const title = ref('');
const description = ref('');
const detail = ref('');
const price = ref<number | null>(null);
const rate = ref<number | null>(null);
const bookTitle = ref('');
const subtitle = ref('');
const author = ref('');
const isbn = ref('');
const binding = ref('');
const pages = ref<number | null>(null);
const publicationDate = ref(new Date()); // 默认值为当前日期
const publisher = ref('');
const cover = ref('');
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
// const route = useRoute();
// const router = useRouter();


// 计算属性，检查输入状态
const hasTitleInput = computed(() => title.value !== '');
const hasDescriptionInput = computed(() => description.value !== '');
const hasPriceInput = computed(() => price.value !== null && price.value >= 0);
const hasImageFile = computed(() => cover.value !== '');

const createDisabled = computed(() => {
  return !(hasTitleInput.value && hasDescriptionInput.value && hasPriceInput.value && hasImageFile.value);

});

async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
  const rawFile = file.raw;
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }

  try {
    const res = await getImage(rawFile);
    if (res && res.code === '000') {
      cover.value = res.result.trim();
      fileListNew[0].url = cover.value;
      fileList.value = fileListNew;
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
    price: price.value??0,
    rate: rate.value??0,
    description: description.value,
    cover: cover.value,
    detail: detail.value,
    bookTitle: bookTitle.value,
    subtitle: subtitle.value,
    author: author.value,
    isbn: isbn.value,
    binding: binding.value,
    pages: pages.value??0,
    publicationDate: publicationDate.value ? publicationDate.value.toISOString() : new Date().toISOString(),
    publisher: publisher.value,

  };

  createProduct(payload).then((res) => {
    if (res.data.code === '000') {
      ElMessage({
        message: '创建商店成功',
        type: 'success',
        center: true,
      });
      // 重置输入框
      title.value = '';
      description.value = '';
      detail.value = '';
      price.value = null;
      rate.value = null;
      bookTitle.value = '';
      subtitle.value = '';
      author.value = '';
      isbn.value = '';
      binding.value = '';
      pages.value = null;
      publicationDate.value = new Date();
      publisher.value = '';
      cover.value = '';
      fileList.value = [];
    }
    else if (res.data.code === '400') {
      ElMessage.error(res.data.message);
    }
  })
}

async function fetchHelloWorld() {
  try {
    const response = await fetch('http://localhost:8080/api/products/hello', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('网络响应不正常');
    }

    const data = await response.text();
    ElMessage.success(data);
  } catch (error) {
    ElMessage.error('获取 Hello World 失败');
  }
}
</script>