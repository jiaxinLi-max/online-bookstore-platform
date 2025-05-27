
<template>
  <div class="product-detail">
    <div class="product-info">
      <div class="product-image" style="position: relative;">
        <img :src="product.cover" alt="Cover Image" class="cover-image" />
        <div v-if="role === 'CUSTOMER'" class="add-to-cart-container">
          <el-button
              type="default"
              @click="addToCart"
              class="add-to-cart-button"
              plain
          >
            加入购物车
          </el-button>
        </div>
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
    <!-- 评论按钮 -->
    <div class="comment-buttons">
      <el-button type="primary" @click="goToCreateComment">发表评论</el-button>
      <el-button type="warning" @click="viewAllComments">查看全部评价</el-button>
    </div>
<!--    <el-button type="success" @click="goToCreateComment">发表评论</el-button>-->
<!--    <el-button type="info" @click="viewAllComments">查看全部评价</el-button>-->


    <div class="action-area">


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
import {getImage} from "../../api/tools.ts"; // 导入 Specification 接口

export default defineComponent({
  name: 'ProductDetail',
  components: {Plus},
  methods: {updateProductInfo},
  setup() {
    const route = useRoute();
    const router = useRouter();
    function goToCreateComment() {
      const productId = route.params.productId;
      //router.push({ path: `/home/product/${productId}/create-comment` });
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
          ElMessage.success('商品已成功加入购物车');
        } else {
          ElMessage.error(response.data.msg || '添加失败?，请重试');
        }
      } catch (error) {
        console.error('添加商品到购物车失败:', error);
        ElMessage.error('添加失败，请重试');
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
.cover-image {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
}

/* 加入购物车按钮容器：在图片正下方，左对齐，留出距离 */
.add-to-cart-container {
  margin-top: 18px; /* 图片和按钮之间的间距 */
  text-align: left;
  margin-left: 120px;
}

/* 加入购物车按钮样式：白底黑字 */
.add-to-cart-button {
  background-color: white;
  color: black;
  border: 1px solid #ccc;
  padding: 4px 12px;
  font-size: 14px;
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
.comment-buttons {
  margin-top: 20px;
  display: flex;
  gap: 12px; /* 控制两个按钮之间的间距 */
}

.comment-buttons .el-button {
  padding: 8px 16px;
  font-weight: 500;
}
</style>
