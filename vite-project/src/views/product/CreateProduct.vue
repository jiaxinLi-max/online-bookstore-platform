<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="create_product">&ndash;&gt;-->
<!--&lt;!&ndash;    <h1 class="create-product-title">创建商品</h1>&ndash;&gt;-->
<!--&lt;!&ndash;    <el-form ref="form" label-width="120px" class="product-form">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="商品名称" prop="productName">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="name" placeholder="请输入商品名称"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="商品价格" prop="productPrice">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="商品评分" prop="productRate">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="商品描述" prop="productDescription">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="description" placeholder="请输入商品描述"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="商品详细说明" prop="productDetail">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="description" placeholder="请输入商品详细说明"></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->



<!--&lt;!&ndash;      <el-form-item label="商品封面" prop="cover">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-upload&ndash;&gt;-->
<!--&lt;!&ndash;            action="http://localhost:3000/api/images"&ndash;&gt;-->
<!--&lt;!&ndash;            list-type="picture-card"&ndash;&gt;-->
<!--&lt;!&ndash;            :auto-upload="true"&ndash;&gt;-->
<!--&lt;!&ndash;            :file-list="fileList"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-change="handleChange"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-remove="handleRemove"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-preview="handlePictureCardPreview"&ndash;&gt;-->
<!--&lt;!&ndash;        >&ndash;&gt;-->
<!--&lt;!&ndash;          <el-icon><Plus /></el-icon>&ndash;&gt;-->
<!--&lt;!&ndash;          <div>点击上传商品 Logo</div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-upload>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-dialog v-model="dialogVisible">&ndash;&gt;-->
<!--&lt;!&ndash;          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />&ndash;&gt;-->
<!--&lt;!&ndash;        </el-dialog>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="default" @click="fetchHelloWorld">测试 Hello World</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>&ndash;&gt;-->
<!--&lt;!&ndash;          创建商品&ndash;&gt;-->
<!--&lt;!&ndash;        </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-form>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script lang="ts" setup>&ndash;&gt;-->
<!--&lt;!&ndash;import { ref, computed } from 'vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { ElMessage } from 'element-plus';&ndash;&gt;-->
<!--&lt;!&ndash;import type { UploadFile } from 'element-plus';&ndash;&gt;-->
<!--&lt;!&ndash;import { createProduct } from "../../api/product";&ndash;&gt;-->
<!--&lt;!&ndash;import { Plus } from '@element-plus/icons-vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { getImage } from '../../api/tools';&ndash;&gt;-->
<!--&lt;!&ndash;import {useRoute, useRouter} from "vue-router";&ndash;&gt;-->

<!--&lt;!&ndash;const name = ref('');&ndash;&gt;-->
<!--&lt;!&ndash;const description = ref('');&ndash;&gt;-->
<!--&lt;!&ndash;const price = ref<number | null>(null);&ndash;&gt;-->
<!--&lt;!&ndash;const logoUrl = ref('');&ndash;&gt;-->
<!--&lt;!&ndash;const fileList = ref<UploadFile[]>([]);&ndash;&gt;-->
<!--&lt;!&ndash;const dialogImageUrl = ref('');&ndash;&gt;-->
<!--&lt;!&ndash;const dialogVisible = ref(false);&ndash;&gt;-->
<!--&lt;!&ndash;const route = useRoute();&ndash;&gt;-->
<!--&lt;!&ndash;const router = useRouter();&ndash;&gt;-->
<!--&lt;!&ndash;const storeId = Number(route.params.storeId);&ndash;&gt;-->
<!--&lt;!&ndash;// 计算属性，检查输入状态&ndash;&gt;-->
<!--&lt;!&ndash;const hasNameInput = computed(() => name.value !== '');&ndash;&gt;-->
<!--&lt;!&ndash;const hasDescriptionInput = computed(() => description.value !== '');&ndash;&gt;-->
<!--&lt;!&ndash;const hasPriceInput = computed(() => price.value !== null && price.value >= 0);&ndash;&gt;-->
<!--&lt;!&ndash;const hasImageFile = computed(() => logoUrl.value !== '');&ndash;&gt;-->

<!--&lt;!&ndash;const createDisabled = computed(() => {&ndash;&gt;-->
<!--&lt;!&ndash;  return !(hasNameInput.value && hasDescriptionInput.value && hasPriceInput.value && hasImageFile.value);&ndash;&gt;-->
<!--&lt;!&ndash;});&ndash;&gt;-->

<!--&lt;!&ndash;async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {&ndash;&gt;-->
<!--&lt;!&ndash;  const rawFile = file.raw;&ndash;&gt;-->
<!--&lt;!&ndash;  if (!rawFile) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('无法获取文件');&ndash;&gt;-->
<!--&lt;!&ndash;    return;&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await getImage(rawFile);&ndash;&gt;-->
<!--&lt;!&ndash;    if (res && res.code === '000') {&ndash;&gt;-->
<!--&lt;!&ndash;      logoUrl.value = res.result.trim();&ndash;&gt;-->
<!--&lt;!&ndash;      fileListNew[0].url = logoUrl.value;&ndash;&gt;-->
<!--&lt;!&ndash;      fileList.value = fileListNew;&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.success('上传成功');&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error('上传失败，请重试');&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('上传失败，请重试');&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;const handlePictureCardPreview = (file: UploadFile) => {&ndash;&gt;-->
<!--&lt;!&ndash;  dialogImageUrl.value = file.url || '';&ndash;&gt;-->
<!--&lt;!&ndash;  dialogVisible.value = true;&ndash;&gt;-->
<!--&lt;!&ndash;};&ndash;&gt;-->

<!--&lt;!&ndash;const handleRemove = (file: UploadFile) => {&ndash;&gt;-->
<!--&lt;!&ndash;  fileList.value = fileList.value.filter(item => item.uid !== file.uid);&ndash;&gt;-->
<!--&lt;!&ndash;};&ndash;&gt;-->

<!--&lt;!&ndash;async function handleCreateProduct() {&ndash;&gt;-->
<!--&lt;!&ndash;  const token = sessionStorage.getItem('token');&ndash;&gt;-->
<!--&lt;!&ndash;  if (!token) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('请先登录!');&ndash;&gt;-->
<!--&lt;!&ndash;    return;&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  const payload = {&ndash;&gt;-->
<!--&lt;!&ndash;    name: name.value,&ndash;&gt;-->
<!--&lt;!&ndash;    description: description.value,&ndash;&gt;-->
<!--&lt;!&ndash;    price: price.value,&ndash;&gt;-->
<!--&lt;!&ndash;    logoUrl: logoUrl.value&ndash;&gt;-->
<!--&lt;!&ndash;  };&ndash;&gt;-->

<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    console.log(payload);&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await createProduct(payload,storeId);&ndash;&gt;-->
<!--&lt;!&ndash;    if (res.data.code === '000') {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.success('创建商品成功');&ndash;&gt;-->
<!--&lt;!&ndash;      name.value = '';&ndash;&gt;-->
<!--&lt;!&ndash;      description.value = '';&ndash;&gt;-->
<!--&lt;!&ndash;      price.value = null;&ndash;&gt;-->
<!--&lt;!&ndash;      logoUrl.value = '';&ndash;&gt;-->
<!--&lt;!&ndash;      fileList.value = [];&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error(res.data.message);&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('创建商品失败');&ndash;&gt;-->
<!--&lt;!&ndash;    console.log(error.message);&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;async function fetchHelloWorld() {&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const response = await fetch('http://localhost:3000/api/products/hello', {&ndash;&gt;-->
<!--&lt;!&ndash;      method: 'POST',&ndash;&gt;-->
<!--&lt;!&ndash;      headers: {&ndash;&gt;-->
<!--&lt;!&ndash;        'Content-Type': 'application/json'&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    if (!response.ok) {&ndash;&gt;-->
<!--&lt;!&ndash;      throw new Error('网络响应不正常');&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->

<!--&lt;!&ndash;    const data = await response.text();&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.success(data);&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('获取 Hello World 失败');&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.create-product-title {&ndash;&gt;-->
<!--&lt;!&ndash;  margin-left: 25%;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.dialog-image {&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 100%;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->



<!--<template>-->
<!--  <div class="create_product">-->
<!--    <h1 class="create-product-title">创建商品</h1>-->
<!--    <el-form ref="form" label-width="120px" class="product-form">-->
<!--      &lt;!&ndash; 基本信息 &ndash;&gt;-->
<!--      <el-form-item label="商品名称" prop="productName">-->
<!--        <el-input v-model="title" placeholder="请输入商品名称"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品价格" prop="productPrice">-->
<!--        <el-input v-model="price" placeholder="请输入商品价格" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品评分" prop="productRate">-->
<!--        <el-input v-model="rate" placeholder="请输入商品评分" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品描述" prop="productDescription">-->
<!--        <el-input v-model="description" placeholder="请输入商品描述"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="商品详细说明" prop="productDetail">-->
<!--        <el-input v-model="detail" placeholder="请输入商品详细说明"></el-input>-->
<!--      </el-form-item>-->

<!--      &lt;!&ndash; 规格说明类 &ndash;&gt;-->
<!--      <el-form-item label="书名" prop="bookTitle">-->
<!--        <el-input v-model="bookTitle" placeholder="请输入书名"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="副标题" prop="subtitle">-->
<!--        <el-input v-model="subtitle" placeholder="请输入副标题"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="作者" prop="author">-->
<!--        <el-input v-model="author" placeholder="请输入作者"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="ISBN" prop="isbn">-->
<!--        <el-input v-model="isbn" placeholder="请输入ISBN"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="装帧方式" prop="binding">-->
<!--        <el-input v-model="binding" placeholder="请输入装帧方式"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="页数" prop="pages">-->
<!--        <el-input v-model="pages" placeholder="请输入页数" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="出版年月" prop="publicationDate">-->
<!--        <el-date-picker v-model="publicationDate" type="date" placeholder="选择出版日期"></el-date-picker>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="出版社" prop="publisher">-->
<!--        <el-input v-model="publisher" placeholder="请输入出版社"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="库存数量" prop="amount">-->
<!--        <el-input v-model="amount" placeholder="请输入库存数量" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="冻结库存数量" prop="frozen">-->
<!--        <el-input v-model="frozen" placeholder="请输入冻结库存数量" type="number"></el-input>-->
<!--      </el-form-item>-->

<!--      &lt;!&ndash; 商品封面 &ndash;&gt;-->
<!--      <el-form-item label="商品封面" prop="cover">-->
<!--        <el-upload-->
<!--            action="http://localhost:8080/api/images"-->
<!--            list-type="picture-card"-->
<!--            :auto-upload="true"-->
<!--            :file-list="fileList"-->
<!--            :on-change="handleChange"-->
<!--            :on-remove="handleRemove"-->
<!--            :on-preview="handlePictureCardPreview"-->
<!--        >-->
<!--          <el-icon><Plus /></el-icon>-->
<!--          <div>点击上传商品 封面</div>-->
<!--        </el-upload>-->
<!--        <el-dialog v-model="dialogVisible">-->
<!--          <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />-->
<!--        </el-dialog>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="规格说明" prop="specifications">-->
<!--        <div v-for="(spec, index) in specifications" :key="index" class="specification-item">-->
<!--          <el-input-->
<!--              v-model="spec.item"-->
<!--              placeholder="规格名称（如 作者、副标题）"-->
<!--              style="width: 200px; margin-right: 10px;"-->
<!--          ></el-input>-->
<!--          <el-input-->
<!--              v-model="spec.value"-->
<!--              placeholder="规格值（如 Robert C. Martin）"-->
<!--              style="width: 200px; margin-right: 10px;"-->
<!--          ></el-input>-->
<!--          <el-button type="danger" @click="removeSpecification(index)">删除</el-button>-->
<!--        </div>-->
<!--        <el-button type="primary" @click="addSpecification">添加规格</el-button>-->
<!--      </el-form-item>-->

<!--      &lt;!&ndash; 按钮 &ndash;&gt;-->
<!--      <el-form-item>-->
<!--&lt;!&ndash;        <el-button type="default" @click="fetchHelloWorld">测试 Hello World</el-button>&ndash;&gt;-->
<!--        <el-button @click.prevent="handleCreateProduct" :disabled="createDisabled" type="primary" plain>-->
<!--          创建商品-->
<!--        </el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed } from 'vue';-->
<!--import { ElMessage, ElDatePicker } from 'element-plus';-->
<!--import type { UploadFile } from 'element-plus';-->
<!--import { createProduct } from "../../api/product";-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--// import { useRoute, useRouter } from "vue-router";-->
<!-- import {Specification} from "../../api/specification.ts";-->

<!--const title = ref('');-->
<!--const description = ref('');-->
<!--const detail = ref('');-->
<!--const price = ref<number | null>(null);-->
<!--const rate = ref<number | null>(null);-->
<!--const bookTitle = ref('');-->
<!--const subtitle = ref('');-->
<!--const author = ref('');-->
<!--const isbn = ref('');-->
<!--const binding = ref('');-->
<!--const pages = ref<number | null>(null);-->
<!--const publicationDate = ref(new Date()); // 默认值为当前日期-->
<!--const publisher = ref('');-->
<!--const cover = ref('');-->
<!--const fileList = ref<UploadFile[]>([]);-->
<!--const dialogImageUrl = ref('');-->
<!--const dialogVisible = ref(false);-->
<!--// const route = useRoute();-->
<!--// const router = useRouter();-->
<!--const amount = ref<number | null>(null); // 库存数量-->
<!--const frozen = ref<number | null>(null); // 冻结库存数量-->

<!--// 计算属性，检查输入状态-->
<!--const hasTitleInput = computed(() => title.value !== '');-->
<!--const hasDescriptionInput = computed(() => description.value !== '');-->
<!--const hasPriceInput = computed(() => price.value !== null && price.value >= 0);-->
<!--const hasImageFile = computed(() => cover.value !== '');-->

<!--const createDisabled = computed(() => {-->
<!--  // return !(hasTitleInput.value && hasDescriptionInput.value && hasPriceInput.value && hasImageFile.value);-->
<!--  return false;-->

<!--});-->

<!--// const specifications = [-->
<!--//   {-->
<!--//     item: '作者',-->
<!--//     value: author.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: '副标题',-->
<!--//     value: subtitle.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: 'ISBN',-->
<!--//     value: isbn.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: '装帧',-->
<!--//     value: binding.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: '页数',-->
<!--//     value: pages.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: '出版社',-->
<!--//     value: publisher.value,-->
<!--//   },-->
<!--//   {-->
<!--//     item: '出版日期',-->
<!--//     value: publicationDate.value ? publicationDate.value.toISOString() : new Date().toISOString(),-->
<!--//   },-->
<!--// ];-->
<!--const specifications = ref<Specification[]>([]);-->
<!--function addSpecification() {-->
<!--  specifications.value.push({ item: '', value: '' }); // 添加一个新的空规格-->
<!--}-->

<!--function removeSpecification(index: number) {-->
<!--  specifications.value.splice(index, 1); // 删除指定索引的规格-->
<!--}-->
<!--async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {-->
<!--  const rawFile = file.raw;-->
<!--  if (!rawFile) {-->
<!--    ElMessage.error('无法获取文件');-->
<!--    return;-->
<!--  }-->

<!--  try {-->
<!--    const res = await getImage(rawFile);-->
<!--    if (res && res.code === '200') {-->
<!--      //cover.value = res.result.trim();-->
<!--      cover.value = res.data;-->
<!--      console.log("cover.value",cover.value);-->
<!--      fileListNew[0].url = cover.value;-->
<!--      fileList.value = fileListNew;-->
<!--      ElMessage.success('上传成功');-->
<!--    } else {-->
<!--      ElMessage.error('上传失败1，请重试');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.log("post_error",error);-->
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
<!--    title: title.value,-->
<!--    price: price.value??0,-->
<!--    rate: rate.value??0,-->
<!--    description: description.value,-->
<!--    cover: cover.value,-->
<!--    detail: detail.value,-->
<!--    // bookTitle: bookTitle.value,-->
<!--    // subtitle: subtitle.value,-->
<!--    // author: author.value,-->
<!--    // isbn: isbn.value,-->
<!--    // binding: binding.value,-->
<!--    // pages: pages.value??0,-->
<!--    // publicationDate: publicationDate.value ? publicationDate.value.toISOString() : new Date().toISOString(),-->
<!--    // publisher: publisher.value,-->
<!--    specifications: specifications, // 添加规格信息-->
<!--    amount: amount.value ?? 0, // 库存数量-->
<!--    frozen: frozen.value ?? 0, // 冻结库存数量-->
<!--  };-->

<!--  createProduct(payload).then((res) => {-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage({-->
<!--        message: '创建商品成功',-->
<!--        type: 'success',-->
<!--        center: true,-->
<!--      });-->
<!--      // 重置输入框-->
<!--      title.value = '';-->
<!--      description.value = '';-->
<!--      detail.value = '';-->
<!--      price.value = null;-->
<!--      rate.value = null;-->
<!--      // bookTitle.value = '';-->
<!--      // subtitle.value = '';-->
<!--      // author.value = '';-->
<!--      // isbn.value = '';-->
<!--      // binding.value = '';-->
<!--      // pages.value = null;-->
<!--      // publicationDate.value = new Date();-->
<!--      // publisher.value = '';-->
<!--      cover.value = '';-->
<!--      fileList.value = [];-->
<!--      amount.value = null; // 重置库存数量-->
<!--      frozen.value = null; // 重置冻结库存数量-->
<!--    }-->
<!--    else if (res.data.code === '400') {-->
<!--      ElMessage.error(res.data.message);-->
<!--    }-->
<!--  })-->
<!--}-->

<!--// async function fetchHelloWorld() {-->
<!--//   try {-->
<!--//     const response = await fetch('http://localhost:8080/api/products/hello', {-->
<!--//       method: 'POST',-->
<!--//       headers: {-->
<!--//         'Content-Type': 'application/json'-->
<!--//       }-->
<!--//     });-->
<!--//-->
<!--//     if (!response.ok) {-->
<!--//       throw new Error('网络响应不正常');-->
<!--//     }-->
<!--//-->
<!--//     const data = await response.text();-->
<!--//     ElMessage.success(data);-->
<!--//   } catch (error) {-->
<!--//     ElMessage.error('获取 Hello World 失败');-->
<!--//   }-->
<!--// }-->
<!--</script>-->

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
</style>
