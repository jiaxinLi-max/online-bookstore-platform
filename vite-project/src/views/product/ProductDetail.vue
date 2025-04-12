<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="product-detail">&ndash;&gt;-->
<!--&lt;!&ndash;    <h1>{{ product.title }}</h1>&ndash;&gt;-->
<!--&lt;!&ndash;    <img :src="product.cover" alt="Cover Image" class="cover-image" />&ndash;&gt;-->
<!--&lt;!&ndash;    <div class="price">价格: ¥{{ product.price }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;    <div class="rating">评分: {{ product.rate }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="description">描述: {{ product.description }}</p>&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="detail">详情: {{ product.detail }}</p>&ndash;&gt;-->

<!--&lt;!&ndash;&lt;!&ndash;    <h3>书籍信息</h3>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    <ul>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>作者: {{ specifications.author }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>副标题: {{ specifications.subtitle }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>ISBN: {{ specifications.isbn }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>装帧: {{ specifications.binding }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>页数: {{ specifications.pages }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>出版社: {{ specifications.publisher }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;      <li>出版日期: {{ specifications.publicationDate }}</li>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    </ul>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;    <p class="detail">详情: {{ product.detail }}</p>&ndash;&gt;-->

<!--&lt;!&ndash;    <h3>书籍信息</h3>&ndash;&gt;-->
<!--&lt;!&ndash;    <ul>&ndash;&gt;-->
<!--&lt;!&ndash;      <li v-for="(spec, index) in specifications" :key="index">&ndash;&gt;-->
<!--&lt;!&ndash;        {{ spec.item }}: {{ spec.value }}&ndash;&gt;-->
<!--&lt;!&ndash;      </li>&ndash;&gt;-->
<!--&lt;!&ndash;    </ul>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script lang="ts">&ndash;&gt;-->
<!--&lt;!&ndash;import { defineComponent, ref, onMounted } from 'vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { useRoute, useRouter } from 'vue-router';&ndash;&gt;-->
<!--&lt;!&ndash;import { getProduct,Product } from '../../api/product.ts';&ndash;&gt;-->
<!--&lt;!&ndash;import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口&ndash;&gt;-->
<!--&lt;!&ndash;const router = useRouter();&ndash;&gt;-->
<!--&lt;!&ndash;const specifications = ref<any[]>([]); // 规格信息的数组&ndash;&gt;-->
<!--&lt;!&ndash;export default defineComponent({&ndash;&gt;-->
<!--&lt;!&ndash;  name: 'ProductDetail',&ndash;&gt;-->
<!--&lt;!&ndash;  setup() {&ndash;&gt;-->
<!--&lt;!&ndash;    const route = useRoute();&ndash;&gt;-->
<!--&lt;!&ndash;    const product = ref({&ndash;&gt;-->
<!--&lt;!&ndash;      title: '',&ndash;&gt;-->
<!--&lt;!&ndash;      price: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      rate: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      description: '',&ndash;&gt;-->
<!--&lt;!&ndash;      cover: '',&ndash;&gt;-->
<!--&lt;!&ndash;      detail: '',&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    const specifications = ref({&ndash;&gt;-->
<!--&lt;!&ndash;      author: '',&ndash;&gt;-->
<!--&lt;!&ndash;      subtitle: '',&ndash;&gt;-->
<!--&lt;!&ndash;      isbn: '',&ndash;&gt;-->
<!--&lt;!&ndash;      binding: '',&ndash;&gt;-->
<!--&lt;!&ndash;      pages: 0,&ndash;&gt;-->
<!--&lt;!&ndash;      publisher: '',&ndash;&gt;-->
<!--&lt;!&ndash;      publicationDate: '',&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    const loadProductDetails = async (productId: number) => {&ndash;&gt;-->
<!--&lt;!&ndash;      try {&ndash;&gt;-->
<!--&lt;!&ndash;        const response = await getProduct(productId);&ndash;&gt;-->
<!--&lt;!&ndash;        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }&ndash;&gt;-->
<!--&lt;!&ndash;        console.log("productData",productData);&ndash;&gt;-->

<!--&lt;!&ndash;        // 更新产品数据&ndash;&gt;-->
<!--&lt;!&ndash;        product.value = {&ndash;&gt;-->
<!--&lt;!&ndash;          title: productData.title,&ndash;&gt;-->
<!--&lt;!&ndash;          price: productData.price,&ndash;&gt;-->
<!--&lt;!&ndash;          rate: productData.rate,&ndash;&gt;-->
<!--&lt;!&ndash;          description: productData.description,&ndash;&gt;-->
<!--&lt;!&ndash;          cover: productData.cover,&ndash;&gt;-->
<!--&lt;!&ndash;          detail: productData.detail,&ndash;&gt;-->
<!--&lt;!&ndash;        };&ndash;&gt;-->


<!--&lt;!&ndash;        // 更新规格数据&ndash;&gt;-->
<!--&lt;!&ndash;        // 更新规格数据&ndash;&gt;-->
<!--&lt;!&ndash;        // if (productData.specifications && productData.specifications.length > 0) {&ndash;&gt;-->
<!--&lt;!&ndash;        //   const specs = productData.specifications.reduce((acc: any, spec: any) => {&ndash;&gt;-->
<!--&lt;!&ndash;        //     // 使用 item 的值作为键，将 value 存入对应的字段&ndash;&gt;-->
<!--&lt;!&ndash;        //     acc[spec.item] = spec.value;&ndash;&gt;-->
<!--&lt;!&ndash;        //     return acc;&ndash;&gt;-->
<!--&lt;!&ndash;        //   }, {});&ndash;&gt;-->
<!--&lt;!&ndash;        //   specifications.value = {&ndash;&gt;-->
<!--&lt;!&ndash;        //     // author: specs['作者'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // subtitle: specs['副标题'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // isbn: specs['ISBN'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // binding: specs['装帧'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // pages: specs['页数'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // publisher: specs['出版社'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //     // publicationDate: specs['出版日期'] || '',&ndash;&gt;-->
<!--&lt;!&ndash;        //   };&ndash;&gt;-->
<!--&lt;!&ndash;        // }&ndash;&gt;-->
<!--&lt;!&ndash;        specifications.value = productData.specifications || []; // 直接使用返回的规格数组&ndash;&gt;-->

<!--&lt;!&ndash;    } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;        console.error('Error loading product details:', error);&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    };&ndash;&gt;-->
<!--&lt;!&ndash;    const productId = Number(route.params.productId);&ndash;&gt;-->

<!--&lt;!&ndash;    // 假设我们传递了产品 ID，例如 102&ndash;&gt;-->
<!--&lt;!&ndash;    onMounted(() => {&ndash;&gt;-->
<!--&lt;!&ndash;      loadProductDetails(productId);&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->

<!--&lt;!&ndash;    return {&ndash;&gt;-->
<!--&lt;!&ndash;      product,&ndash;&gt;-->
<!--&lt;!&ndash;      specifications,&ndash;&gt;-->
<!--&lt;!&ndash;    };&ndash;&gt;-->
<!--&lt;!&ndash;  },&ndash;&gt;-->
<!--&lt;!&ndash;});&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.product-detail {&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.cover-image {&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 100%;&ndash;&gt;-->
<!--&lt;!&ndash;  height: auto;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.price {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.rating {&ndash;&gt;-->
<!--&lt;!&ndash;  color: goldenrod;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.description, .detail {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 10px 0;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<template>-->
<!--  <div class="product-detail">-->
<!--    <div class="product-info">-->
<!--      <div class="product-image">-->
<!--        <img :src="product.cover" alt="Cover Image" class="cover-image" />-->
<!--      </div>-->
<!--      <div class="product-details">-->
<!--        <h1>{{ product.title }}</h1>-->
<!--        <div class="price">价格: ¥{{ product.price }}</div>-->
<!--        <div class="rating">评分: {{ product.rate }}</div>-->
<!--        <p class="description">描述: {{ product.description }}</p>-->
<!--        <p class="detail">详情: {{ product.detail }}</p>-->
<!--        <h3>书籍信息</h3>-->
<!--        <ul>-->
<!--          <li v-for="(spec, index) in specifications" :key="index">-->
<!--            {{ spec.item }}: {{ spec.value }}-->
<!--          </li>-->
<!--        </ul>-->
<!--        <p v-if="specifications.length === 0">没有书籍信息可显示。</p>-->
<!--      </div>-->
<!--    </div>-->
<!--    <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>-->
<!--    <el-button type="primary" @click="addToCart">加入购物车</el-button>-->
<!--  </div>-->

<!--</template>-->

<!--<script lang="ts">-->
<!--import { defineComponent, ref, onMounted } from 'vue';-->
<!--import { useRoute } from 'vue-router';-->
<!--import { getProduct } from '../../api/product.ts';-->
<!--import { addCart } from '../../api/cart.ts';-->
<!--import { Specification } from "../../api/specification.ts"; // 导入 Specification 接口-->

<!--const quantity = ref(1);-->
<!--const maxQuantity = ref(10); // 这里可以根据实际库存动态设置-->
<!--const route = useRoute();-->
<!--const productId = Number(route.params.productId);-->
<!--const addToCart = async () => {-->
<!--  try {-->
<!--    const userId = sessionStorage.getItem('userId');-->
<!--    console.log("inCart",userId);-->
<!--    // const response = await axios.post('/api/cart', {-->
<!--    //   userId: userId,-->
<!--    //   productId: props.product.id, // 使用传入的商品ID-->
<!--    //   quantity: quantity.value-->
<!--    // });-->
<!--    const response = await addCart(userId,productId,quantity.value);-->
<!--    if (response.data.code === 200) {-->
<!--      // 提示成功，或者进行其他操作-->
<!--      alert('商品已成功加入购物车');-->
<!--    } else {-->
<!--      alert(response.data.msg || '添加失败，请重试');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error('添加商品到购物车失败:', error);-->
<!--    alert('添加失败，请重试');-->
<!--  }-->
<!--};-->
<!--export default defineComponent({-->
<!--  name: 'ProductDetail',-->
<!--  setup() {-->
<!--    const route = useRoute();-->
<!--    const product = ref({-->
<!--      title: '',-->
<!--      price: 0,-->
<!--      rate: 0,-->
<!--      description: '',-->
<!--      cover: '',-->
<!--      detail: '',-->
<!--    });-->

<!--    // 定义 specifications 类型-->
<!--    const specifications = ref<Specification[]>([]);-->

<!--    const loadProductDetails = async (productId: number) => {-->
<!--      try {-->
<!--        const response = await getProduct(productId);-->
<!--        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }-->
<!--        console.log("productData", productData);-->

<!--        // 更新产品数据-->
<!--        product.value = {-->
<!--          title: productData.title,-->
<!--          price: productData.price,-->
<!--          rate: productData.rate,-->
<!--          description: productData.description,-->
<!--          cover: productData.cover,-->
<!--          detail: productData.detail,-->
<!--        };-->

<!--        // 更新规格数据，直接使用返回的规格数组-->
<!--        specifications.value = productData.specifications || []; // 赋值为数组-->

<!--      } catch (error) {-->
<!--        console.error('Error loading product details:', error);-->
<!--      }-->
<!--    };-->

<!--    const productId = Number(route.params.productId);-->

<!--    // 在组件挂载后加载产品详情-->
<!--    onMounted(() => {-->
<!--      loadProductDetails(productId);-->
<!--    });-->

<!--    return {-->
<!--      product,-->
<!--      specifications,-->
<!--    };-->
<!--  },-->
<!--});-->
<!--</script>-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.product-detail {&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.cover-image {&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 50%; /* 将最大宽度设置为100% */&ndash;&gt;-->
<!--&lt;!&ndash;  height: auto;    /* 自动调整高度以保持纵横比 */&ndash;&gt;-->
<!--&lt;!&ndash;  display: block;  /* 使图片块级显示，避免下方出现空隙 */&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 0 auto;  /* 可选：使图片在容器中居中显示 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.price {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.rating {&ndash;&gt;-->
<!--&lt;!&ndash;  color: goldenrod;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.description, .detail {&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 10px 0;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<style scoped>-->
<!--.product-detail {-->
<!--  padding: 20px;-->
<!--  display: flex;-->
<!--  justify-content: center; /* 水平居中 */-->
<!--  align-items: center; /* 垂直居中 */-->
<!--  flex-direction: column; /* 使内容垂直排列 */-->
<!--}-->

<!--.product-info {-->
<!--  display: flex;-->
<!--  justify-content: center; /* 水平居中 */-->
<!--  align-items: flex-start; /* 垂直起始对齐 */-->
<!--  width: 100%;-->
<!--  max-width: 1200px; /* 限制最大宽度 */-->
<!--  margin: 0 auto; /* 水平居中 */-->
<!--}-->

<!--.product-image {-->
<!--  flex: 1;-->
<!--  margin-right: 20px;-->
<!--}-->

<!--.product-image img {-->
<!--  max-width: 100%;-->
<!--  height: auto;-->
<!--  display: block;-->
<!--}-->

<!--.product-details {-->
<!--  flex: 2;-->
<!--  margin-left: 20px;-->
<!--}-->

<!--.price {-->
<!--  font-size: 20px;-->
<!--  font-weight: bold;-->
<!--}-->

<!--.rating {-->
<!--  color: goldenrod;-->
<!--}-->

<!--.description, .detail {-->
<!--  margin: 10px 0;-->
<!--}-->
<!--</style>-->
<template>
  <div class="product-detail">
    <div class="product-info">
      <div class="product-image">
        <img :src="product.cover" alt="Cover Image" class="cover-image" />
      </div>
      <div class="product-details">
        <h1>{{ product.title }}</h1>
        <div class="price">价格: ¥{{ product.price }}</div>
        <div class="rating">评分: {{ product.rate }}</div>
        <p class="description">描述: {{ product.description }}</p>
        <p class="detail">详情: {{ product.detail }}</p>

        <h3>书籍信息</h3>
        <ul>
          <li v-for="(spec, index) in specifications" :key="index">
            {{ spec.item }}: {{ spec.value }}
          </li>
        </ul>
        <p v-if="specifications.length === 0">没有书籍信息可显示。</p>
      </div>
    </div>
<!--    <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>-->
<!--    <el-button type="primary" @click="addToCart">加入购物车</el-button>-->
    <div class="action-area">
      <!-- 客户操作 -->
      <div v-if="role === 'CUSTOMER'" class="customer-actions">
        <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>
        <el-button type="primary" @click="addToCart">加入购物车</el-button>
      </div>

      <!-- 管理员操作 -->
      <div v-if="role === 'MANAGER'" class="manager-actions">
        <div>库存: {{ stockAmount }}</div>
        <div class="stock-control">
          <el-input-number v-model="newStock" :min="0" label="新库存"></el-input-number>
          <el-button type="warning" @click="updateStock">更新库存</el-button>
        </div>

        <div class="management-buttons">
          <el-button type="primary" @click="openEditDialog">更新信息</el-button>
          <el-button type="danger" @click="deleteProduct">删除商品</el-button>
        </div>
      </div>
    </div>
  </div>
  <el-dialog
      v-model="showEditDialog"
      title="修改商品信息"
      width="40%"
      class="edit-dialog"
  >
    <el-form ref="form" label-width="120px" class="product-form">
      <!-- 基本信息 -->
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="editForm.title" placeholder="请输入商品名称"></el-input>
      </el-form-item>

      <el-form-item label="商品价格" prop="productPrice">
        <el-input v-model="editForm.price" placeholder="请输入商品价格" type="number"></el-input>
      </el-form-item>

      <el-form-item label="商品评分" prop="productRate">
        <el-input v-model="editForm.rate" placeholder="请输入商品评分" type="number"></el-input>
      </el-form-item>

      <el-form-item label="商品描述" prop="productDescription">
        <el-input v-model="editForm.description" placeholder="请输入商品描述"></el-input>
      </el-form-item>

      <el-form-item label="商品详细说明" prop="productDetail">
        <el-input v-model="editForm.detail" placeholder="请输入商品详细说明"></el-input>
      </el-form-item>

      <!-- 规格说明类 -->
      <el-form-item label="规格说明" prop="specifications">
        <div v-for="(spec, index) in newSpecifications" :key="index" class="specification-item">
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
            :auto-upload="false"
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
        <el-button @click.prevent="handleUpdateProduct" type="primary" plain>
          更新商品
        </el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script lang="ts">
import {defineComponent, ref, onMounted} from 'vue';
import { useRoute } from 'vue-router';
import {
  deleteTheProduct,
  getProduct,
  updateProductInfo,
  updateStockpile,
  getStockpile,
} from '../../api/product.ts';
import { addCart } from '../../api/cart.ts';
import { Specification } from "../../api/specification.ts";
import {ElMessage, type UploadFile} from "element-plus";
import {Plus} from "@element-plus/icons-vue";
import {getImage} from "../../api/tools.ts"; // 导入 Specification 接口

export default defineComponent({
  name: 'ProductDetail',
  components: {Plus},
  methods: {updateProductInfo},
  setup() {
    const route = useRoute();
    const productId = Number(route.params.productId).toString();

    const role = ref(sessionStorage.getItem('role') || '');
    const newStock = ref(0);

    const stockAmount = ref(0);
    const stockFrozen = ref(0);

    const showEditDialog = ref(false);

    const fileList = ref<UploadFile[]>([]);
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);

    // 商品数据
    const product = ref({
      title: '',
      price: 0,
      rate: 0,
      description: '',
      cover: '',
      detail: '',
    });

    // 规格信息
    const specifications = ref<Specification[]>([]);
    const newSpecifications = ref<Specification[]>([]);
    const quantity = ref(1);
    const maxQuantity = ref(10); // 这里可以根据实际库存动态设置

    const editForm = ref({
      title: product.value.title,
      price: product.value.price,
      rate: product.value.rate,
      description: product.value.description,
      cover: product.value.cover,
      detail: product.value.detail,
    });

    // 加载产品详情
    const loadProductDetails = async (productId: string) => {
      try {
        const response = await getProduct(productId);
        const productData = response.data.data; // 假设响应的结构是 { code: 200, data: {...} }

        // 更新产品数据
        product.value = {
          title: productData.title,
          price: productData.price,
          rate: productData.rate,
          description: productData.description,
          cover: productData.cover,
          detail: productData.detail,
        };

        // 更新规格数据，直接使用返回的规格数组
        specifications.value = productData.specifications || []; // 赋值为数组

      } catch (error) {
        console.error('Error loading product details:', error);
      }
    };

    const openEditDialog = () => {
      showEditDialog.value = true;
      editForm.value = {
        title: product.value.title,
        price: product.value.price,
        rate: product.value.rate,
        description: product.value.description,
        cover: product.value.cover,
        detail: product.value.detail,
      };
      fileList.value = [];
      newSpecifications.value = specifications.value;
    };

    async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
      const rawFile = file.raw;
      if (!rawFile) {
        ElMessage.error('无法获取文件');
        return;
      }

      try {
        const res = await getImage(rawFile);
        if (res && res.code === '200') {
          editForm.value.cover = res.data;
          ElMessage.success('封面上传成功');
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

    async function handleUpdateProduct() {
      console.log("Update product!");
      const token = sessionStorage.getItem('token');
      if (!token) {
        ElMessage.error('请先登录!');
        return;
      }

      try {
        const res = await updateProductInfo({
          id: productId,
          title: editForm.value.title,
          price: editForm.value.price,
          rate: editForm.value.rate,
          description: editForm.value.description,
          cover: editForm.value.cover,
          detail: editForm.value.detail,
          specifications: newSpecifications.value,
        });
        if (res.data.code === '200') {
          ElMessage.success('更新商品成功');
          await loadProductDetails(productId);
          editForm.value.title = product.value.title;
          editForm.value.description = product.value.description;
          editForm.value.detail = product.value.detail;
          editForm.value.price = product.value.price;
          editForm.value.rate = product.value.rate;
          editForm.value.cover = product.value.cover;
          fileList.value = [];
          newSpecifications.value = specifications.value;
        } else {
          ElMessage.error(res.data.message);
          editForm.value.title = product.value.title;
          editForm.value.description = product.value.description;
          editForm.value.detail = product.value.detail;
          editForm.value.price = product.value.price;
          editForm.value.rate = product.value.rate;
          editForm.value.cover = product.value.cover;
          fileList.value = [];
          newSpecifications.value = specifications.value;
        }
        showEditDialog.value = false;
      } catch (error) {
        console.log("error",error);
        ElMessage.error('更新商品失败');
      }
    }

    function addSpecification() {
      newSpecifications.value.push({
        item: '',     // 新规格的名称
        value: '',    // 新规格的值
      });
    }

    function removeSpecification(index: number) {
      newSpecifications.value.splice(index, 1);
    }

    // 添加到购物车
    const addToCart = async () => {
      try {
        const userId = sessionStorage.getItem('userId');
        const response = await addCart(userId, productId, quantity.value);
        console.log("response.data.code",response.data.code);
        console.log(typeof response.data.code); // 打印类型
        if (response.data.code === "200") {
          alert('商品已成功加入购物车');
        } else {
          alert(response.data.msg || '添加失败?，请重试');
        }
      } catch (error) {
        console.error('添加商品到购物车失败:', error);
        alert('添加失败，请重试');
      }
    };

    // 在组件挂载后加载产品详情
    onMounted(() => {
      loadProductDetails(productId);

      getStock();
    });

    // const updateInfo = async () => {
    //   try {
    //     const response = await updateProductInfo({
    //       id: productId.toString(),
    //       title: product.value.title,
    //       price: product.value.price,
    //       rate: product.value.rate,
    //       description: product.value.description,
    //       cover: product.value.cover,
    //       detail: product.value.detail,
    //       specifications: specifications.value,
    //     });
    //     if (response.data.code === '200') {
    //       ElMessage.success('商品信息更新成功');
    //       console.log(response.data);
    //     }
    //   } catch (error) {
    //     console.error('更新商品信息失败:', error);
    //   }
    // };

    const deleteProduct = async () => {
      try {
        const response = await deleteTheProduct(productId.toString());
        if (response.data.code === '200') {
          ElMessage.success('商品删除成功');
          console.log(response.data);
        }
      } catch (error) {
        console.error('删除商品失败:', error);
      }
    };

    const updateStock = async () => {
      try {
        const response = await updateStockpile({
          productId: productId.toString(),
          amount: newStock.value,
        });
        if (response.data.code === '200') {
          stockAmount.value = newStock.value;
          ElMessage.success('库存更新成功');
          console.log(response.data);
        }
      } catch (error) {
        console.error('更新库存失败:', error);
      }
    };

    const getStock = async () => {
      try {
        const response = await getStockpile(productId.toString());
        if (response.data.code === '200') {
          stockAmount.value = response.data.data.amount;
          stockFrozen.value = response.data.data.frozen;
          console.log("StockAmount:" + response.data.data.amount);
          console.log("StockFrozen:" + response.data.data.frozen);
        }
      } catch (error) {
        console.error('获取库存信息失败:', error);
      }
    }

    return {
      product,
      specifications,
      quantity,
      maxQuantity,
      addToCart,
      role,
      newStock,
      deleteProduct,
      updateStock,
      getStock,
      stockAmount,
      showEditDialog,
      fileList,
      dialogImageUrl,
      dialogVisible,
      openEditDialog,
      handleChange,
      handlePictureCardPreview,
      handleRemove,
      handleUpdateProduct,
      editForm,
      addSpecification,
      removeSpecification,
      newSpecifications,
    };
  },
});
</script>

<style scoped>
html, body {
  height: 100%;
}
.product-detail {
  padding: 20px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  flex-direction: column; /* 使内容垂直排列 */
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
  color: black; /* 设置文字颜色为白色，确保可读性 */
}

.product-info {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: flex-start; /* 垂直起始对齐 */
  width: 100%;
  max-width: 1200px; /* 限制最大宽度 */
  margin: 0 auto;
  background-color: rgba(255,255, 255, 0.6); /* 可以给商品信息部分添加白色背景，以提升可读性 */
}

.product-image {
  flex: 1;
  margin-right: 20px;
}

.product-image img {
  max-width: 100%;
  height: auto;
  display: block;
}

.product-details {
  flex: 2;
  margin-left: 20px;
}

.price {
  font-size: 20px;
  font-weight: bold;
}

.rating {
  color: goldenrod;
}

.description, .detail {
  margin: 10px 0;
}

.action-area {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.manager-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
