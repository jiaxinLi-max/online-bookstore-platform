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

    <div class="comment-area">
      <el-button type="success" @click="goToCreateComment">发表评论</el-button>
      <el-button type="info" @click="viewAllComments">查看全部评价</el-button>
    </div>

    <div class="action-area">
      <div v-if="role === 'CUSTOMER'" class="customer-actions-group">
        <div class="stock-display" v-if="stockAmount <= 10">库存紧张</div>
        <div class="action-row">
          <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>
          <el-button type="primary" @click="addToCart">加入购物车</el-button>
        </div>
      </div>

      <div v-if="role === 'MANAGER'" class="manager-actions-group">
        <div class="stock-display">库存: {{ stockAmount }}</div>
        <div class="action-row stock-control">
          <el-input-number v-model="newStock" :min="0" label="新库存"></el-input-number>
          <el-button type="warning" @click="updateStock">更新库存</el-button>
        </div>

        <div class="action-row management-buttons">
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
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="addSpecification">添加规格</el-button>
      </el-form-item>

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
import { useRouter, useRoute } from 'vue-router';
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
import {getImage} from "../../api/tools.ts";

export default defineComponent({
  name: 'ProductDetail',
  components: {Plus},
  methods: {updateProductInfo},
  setup() {
    const route = useRoute();
    const router = useRouter();
    function goToCreateComment() {
      const productId = route.params.productId;
      router.push({ name: 'CreateComment', params: { productId } });
    }
    function viewAllComments () {
      router.push({
        name: 'ProductComments',
        params: { productId: route.params.productId }
      })
    }
    const productId = Number(route.params.productId).toString();

    const role = ref(sessionStorage.getItem('role') || '');
    const newStock = ref(0);

    const stockAmount = ref(0);
    const stockFrozen = ref(0);

    const showEditDialog = ref(false);

    const fileList = ref<UploadFile[]>([]);
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);

    const product = ref({
      title: '',
      price: 0,
      rate: 0,
      description: '',
      cover: '',
      detail: '',
    });

    const specifications = ref<Specification[]>([]);
    const newSpecifications = ref<Specification[]>([]);
    const quantity = ref(1);
    const maxQuantity = ref(10);

    const editForm = ref({
      title: product.value.title,
      price: product.value.price,
      rate: product.value.rate,
      description: product.value.description,
      cover: product.value.cover,
      detail: product.value.detail,
    });

    const loadProductDetails = async (productId: string) => {
      try {
        const response = await getProduct(productId);
        const productData = response.data.data;

        product.value = {
          title: productData.title,
          price: productData.price,
          rate: productData.rate,
          description: productData.description,
          cover: productData.cover,
          detail: productData.detail,
        };

        specifications.value = productData.specifications || [];

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
      newSpecifications.value = JSON.parse(JSON.stringify(specifications.value)); // 深拷贝防止直接修改
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
          showEditDialog.value = false;
        } else {
          ElMessage.error(res.data.message);
        }
      } catch (error) {
        console.error("error",error);
        ElMessage.error('更新商品失败');
      }
    }

    function addSpecification() {
      newSpecifications.value.push({
        item: '',
        value: '',
      });
    }

    function removeSpecification(index: number) {
      newSpecifications.value.splice(index, 1);
    }

    const addToCart = async () => {
      try {
        const userId = sessionStorage.getItem('userId');
        const response = await addCart(userId, productId, quantity.value);
        if (response.data.code === "200") {
          ElMessage.success('商品已成功加入购物车');
        } else {
          ElMessage.error(response.data.msg || '添加失败，请重试');
        }
      } catch (error) {
        console.error('添加商品到购物车失败:', error);
        ElMessage.error('添加失败，请重试');
      }
    };

    onMounted(() => {
      loadProductDetails(productId);
      getStock();
    });

    const deleteProduct = async () => {
      try {
        const response = await deleteTheProduct(productId.toString());
        if (response.data.code === '200') {
          ElMessage.success('商品删除成功');
        } else {
          ElMessage.error(response.data.msg || '删除失败，请重试');
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
        } else {
          ElMessage.error(response.data.msg || '更新库存失败，请重试');
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
        } else {
          ElMessage.error(response.data.msg || '获取库存信息失败');
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
      goToCreateComment,
      viewAllComments,
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
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
  color: black;
}

.product-info {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto 20px auto; /* 增加下方外边距 */
  background-color: rgba(255, 255, 255, 0.6);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.product-image {
  flex: 1;
  margin-right: 20px;
}

.product-image img {
  max-width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
}
.cover-image {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
}

.product-details {
  flex: 2;
  margin-left: 20px;
}

.product-details h1 {
  font-size: 2.5em;
  margin-bottom: 10px;
  color: #333;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #e60023; /* 醒目的价格颜色 */
  margin-bottom: 10px;
}

.rating {
  color: goldenrod;
  font-size: 1.1em;
  margin-bottom: 10px;
}

.description, .detail {
  margin: 10px 0;
  line-height: 1.6;
  color: #555;
}

.product-details h3 {
  margin-top: 20px;
  margin-bottom: 10px;
  color: #333;
}

.product-details ul {
  list-style: none;
  padding: 0;
}

.product-details li {
  margin-bottom: 5px;
  color: #666;
}

/* 新增的评论区和操作区样式 */
.comment-area {
  display: flex;
  gap: 20px; /* 增加按钮间距 */
  margin-top: 20px;
  margin-bottom: 20px; /* 与下方操作区域的间距 */
}

.action-area {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-color: rgba(255, 255, 255, 0.6);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 1200px;
}

.customer-actions-group,
.manager-actions-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%; /* 使其占据父容器的宽度 */
  margin-bottom: 15px; /* 增加组之间的垂直间距 */
}

.stock-display {
  font-size: 1.2em;
  font-weight: bold;
  color: white; /* 库存字体改为白色 */
  text-shadow: 1px 1px 2px rgba(0,0,0,0.7); /* 增加文字阴影，提高在复杂背景下的可读性 */
  margin-bottom: 15px; /* 与下方按钮组的间距 */
}

.action-row {
  display: flex;
  align-items: center;
  gap: 20px; /* 增加行内元素间距 */
  margin-bottom: 15px; /* 每行之间的垂直间距 */
}

.action-row .el-input-number {
  width: 150px; /* 调整输入框宽度 */
}

.action-row .el-button {
  padding: 10px 20px;
  font-size: 1em;
}

.management-buttons {
  margin-top: 10px; /* 管理按钮组与上方库存控制组的间距 */
}

/* El-dialog 中的表单项间距 */
.product-form .el-form-item {
  margin-bottom: 20px;
}

.specification-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  gap: 10px; /* 规格项内部元素间距 */
}

.dialog-image {
  max-width: 100%;
  height: auto;
  display: block;
}
</style>