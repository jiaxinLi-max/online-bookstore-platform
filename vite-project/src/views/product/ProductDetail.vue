<template>
  <div class="product-detail">
    <div class="product-info">
      <div class="product-image">
        <el-carousel
            v-if="Array.isArray(product.cover) && product.cover.length > 0"
            height="400px"
            indicator-position="outside"
            arrow="always"
            class="cover-carousel"
        >
          <el-carousel-item v-for="(img, idx) in product.cover" :key="idx">
            <img :src="img" class="carousel-image" alt="书籍图片" />
          </el-carousel-item>
        </el-carousel>
        <div v-else class="no-image-placeholder">暂无图片</div>
      </div>

      <div class="product-details">
        <h1>{{ product.title }}</h1>
        <div class="price">价格: ¥{{ product.price }}</div>
        <div class="rating">评分: {{ product.rate }}</div>
        <p class="description">描述: {{ product.description }}</p>
        <p class="detail">详情: {{ product.detail }}</p>

        <div v-if="associatedColumns.length > 0" class="columns-section">
          <h3>所属栏目</h3>
          <el-tag
              v-for="column in associatedColumns"
              :key="column.id"
              class="column-tag clickable"
              type="success"
              @click="showColumnProducts(column)"
          >
            {{ column.theme }}
          </el-tag>
        </div>

        <h3>书籍信息</h3>
        <ul>
          <li v-for="(spec, index) in specifications" :key="index">
            {{ spec.item }}: {{ spec.value }}
          </li>
        </ul>
        <p v-if="specifications.length === 0">没有书籍信息可显示。</p>
      </div>
    </div>

    <div class="action-area">
      <div v-if="role === 'CUSTOMER'" class="customer-actions-group">
        <div class="stock-display" v-if="stockAmount <= 10">库存紧张</div>
        <div class="action-row">
          <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>
          <el-button class="btn-camel" @click="addToCart">加入购物车</el-button>
        </div>
        <div class="comment-area">
          <el-button class="btn-camel" @click="showCommentForm = !showCommentForm">
            {{ showCommentForm ? '收起评论框' : '发表评论' }}
          </el-button>
          <el-button type="info" @click="viewAllComments">查看全部评价</el-button>
        </div>
        <div v-if="showCommentForm" class="create-comment-box">
          <el-form label-width="120px" class="comment-form">
            <el-form-item label="评价内容">
              <el-input v-model="commentContent" placeholder="请输入评价内容" />
            </el-form-item>
            <el-form-item label="评分">
              <el-rate
                  v-model="commentScore"
                  :allow-half="true"
                  show-text
                  :texts="['极差', '失望', '一般', '满意', '惊喜']"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                  @click.prevent="handleCreateComment"
                  :disabled="!commentContent || !commentScore"
                  type="primary"
                  plain
                  class="custom-black-button"
              >
                创建评价
              </el-button>
            </el-form-item>
          </el-form>
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

    <el-dialog v-model="isColumnDialogVisible" :title="selectedColumn.theme + ' 栏目下的所有书籍'" width="60%">
      <div v-loading="isColumnLoading">
        <div v-if="columnProducts.length > 0" class="dialog-book-list">
          <el-card v-for="p in columnProducts" :key="p.id" class="product-card-small" @click="goToProductDetail(p.id)" shadow="hover">
            <div class="product-image-small">
              <img :src="Array.isArray(p.cover) && p.cover.length > 0 ? p.cover[0] : p.cover" alt="Product Cover" />
            </div>
            <h4 class="product-title-small">{{ p.title }}</h4>
          </el-card>
        </div>
        <el-empty v-else description="该栏目下没有其他书籍"></el-empty>
      </div>
    </el-dialog>

    <el-dialog v-model="showEditDialog" title="修改商品信息" width="40%">
      <el-form ref="form" label-width="120px" class="product-form">
        <el-form-item label="商品名称" prop="productName"><el-input v-model="editForm.title"></el-input></el-form-item>
        <el-form-item label="商品价格" prop="productPrice"><el-input v-model="editForm.price" type="number"></el-input></el-form-item>
        <el-form-item label="商品评分" prop="productRate"><el-input v-model="editForm.rate" type="number"></el-input></el-form-item>
        <el-form-item label="商品描述" prop="productDescription"><el-input v-model="editForm.description"></el-input></el-form-item>
        <el-form-item label="商品详细说明" prop="productDetail"><el-input v-model="editForm.detail"></el-input></el-form-item>
        <el-form-item label="所属栏目" prop="columnIds">
          <el-select v-model="editForm.columnIds" multiple placeholder="请选择栏目" style="width: 100%;">
            <el-option v-for="column in allColumns" :key="column.id" :label="column.theme" :value="column.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格说明" prop="specifications">
          <div v-for="(spec, index) in newSpecifications" :key="index" class="specification-item">
            <el-input v-model="spec.item" placeholder="规格名称" style="width: 200px; margin-right: 10px;"></el-input>
            <el-input v-model="spec.value" placeholder="规格值" style="width: 200px; margin-right: 10px;"></el-input>
            <el-button type="danger" @click="removeSpecification(index)">删除</el-button>
          </div>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="addSpecification">添加规格</el-button></el-form-item>
        <el-form-item label="商品封面" prop="cover">
          <el-upload action="http://localhost:8080/api/images" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePictureCardPreview" multiple>
            <el-icon><Plus /></el-icon>
            <div>点击上传或修改封面</div>
          </el-upload>
        </el-form-item>
        <el-form-item><el-button @click.prevent="handleUpdateProduct" type="primary" plain>更新商品</el-button></el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog v-model="dialogVisible"><img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" /></el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { deleteTheProduct, getProduct, updateProductInfo, updateStockpile, getStockpile } from '../../api/product.ts';
import { getAllColumns, getProductsByColumn } from '../../api/columns.ts';
import { addCart } from '../../api/cart.ts';
import { Specification } from "../../api/specification.ts";
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import { getImage } from "../../api/tools.ts";
import { createComment } from '../../api/comment';
import type { Product } from '../../api/product';

export default defineComponent({
  name: 'ProductDetail',
  components: { Plus },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const productId = Array.isArray(route.params.productId) ? route.params.productId[0] : route.params.productId;

    const product = ref<Partial<Product>>({ cover: [], columnIds: [] });
    const specifications = ref<Specification[]>([]);
    const allColumns = ref<{id: number, theme: string}[]>([]);
    const quantity = ref(1);
    const maxQuantity = ref(10);
    const showCommentForm = ref(false);
    const commentContent = ref('');
    const commentScore = ref<number>(0);
    const role = ref(sessionStorage.getItem('role') || '');
    const newStock = ref(0);
    const stockAmount = ref(0);
    const showEditDialog = ref(false);
    const editForm = ref<Partial<Product>>({ cover: [], columnIds: [] });
    const newSpecifications = ref<Specification[]>([]);
    const fileList = ref<UploadFile[]>([]);
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);
    const isColumnDialogVisible = ref(false);
    const isColumnLoading = ref(false);
    const selectedColumn = ref<{id: number, theme: string}>({id: 0, theme: ''});
    const columnProducts = ref<Product[]>([]);

    const associatedColumns = computed(() => {
      if (!product.value.columnIds || !allColumns.value.length) return [];
      return allColumns.value.filter(col => product.value.columnIds!.includes(col.id));
    });

    const loadProductDetails = async (id: string) => {
      try {
        const response = await getProduct(id);
        const productData = response.data.data;
        product.value = productData;
        if (!Array.isArray(product.value.cover)) product.value.cover = [];
        specifications.value = productData.specifications || [];
      } catch (error) { console.error('加载商品详情失败:', error); }
    };

    const openEditDialog = () => {
      editForm.value = JSON.parse(JSON.stringify(product.value));
      if (!Array.isArray(editForm.value.cover)) editForm.value.cover = [];
      fileList.value = editForm.value.cover!.map((url, index) => ({
        name: `cover${index}.jpg`, url: url, uid: Date.now() + index, status: 'success',
      }));
      newSpecifications.value = JSON.parse(JSON.stringify(specifications.value));
      showEditDialog.value = true;
    };

    async function handleChange(file: UploadFile) {
      if (!editForm.value.cover) editForm.value.cover = [];
      const rawFile = file.raw;
      if (!rawFile) return;
      try {
        const res = await getImage(rawFile);
        if (res && res.code === '200') {
          console.log("上传");
          editForm.value.cover.push(res.data);
          ElMessage.success('新封面上传成功');
        } else { ElMessage.error('上传失败'); }
      } catch (error) { ElMessage.error('上传异常'); }
    }

    const handleRemove = (file: UploadFile) => {
      const urlToRemove = file.url;
      if (urlToRemove && editForm.value.cover) {
        console.log("删除", editForm.value.cover);
        editForm.value.cover = editForm.value.cover.filter(url => url !== urlToRemove);
        console.log(urlToRemove);
        console.log(editForm.value.cover);
      }
    };

    const handlePictureCardPreview = (file: UploadFile) => {
      dialogImageUrl.value = file.url || '';
      dialogVisible.value = true;
    };

    async function handleUpdateProduct() {
      if (!editForm.value.id) return;
      try {
        const res = await updateProductInfo(editForm.value as Product);
        if (res.data.code === '200') {
          ElMessage.success('更新商品成功');
          await loadProductDetails(productId);
          showEditDialog.value = false;
        } else { ElMessage.error(res.data.message || '更新失败'); }
      } catch (error) { ElMessage.error('更新商品失败'); }
    }

    const goToProductDetail = (id: number) => {
      isColumnDialogVisible.value = false;
      if (id === Number(productId)) { window.location.reload(); }
      else { router.push({ path: `/home/product/${id}` }); }
    };

    const showColumnProducts = async (column: {id: number, theme: string}) => {
      isColumnDialogVisible.value = true;
      isColumnLoading.value = true;
      selectedColumn.value = column;
      columnProducts.value = [];
      try {
        const res = await getProductsByColumn(column.id);
        if(res.data.code === '200') {
          columnProducts.value = res.data.data.filter((p: Product) => p.id !== Number(productId));
        } else { ElMessage.error("获取栏目内书籍失败"); }
      } catch (error) { ElMessage.error("获取栏目内书籍失败"); }
      finally { isColumnLoading.value = false; }
    };

    const addToCart = async () => {
      const userId = sessionStorage.getItem('userId');
      if (!userId) { ElMessage.error("请先登录"); return; }
      try {
        const res = await addCart(Number(userId), Number(productId), quantity.value);
        if (res.data.code === "200") { ElMessage.success('商品已成功加入购物车'); }
        else { ElMessage.error(res.data.msg || '添加失败'); }
      } catch (error) { ElMessage.error('添加失败'); }
    };

    const deleteProduct = async () => {
      try {
        await ElMessageBox.confirm('确定要删除这个商品吗？', '警告', { type: 'warning' });
        const response = await deleteTheProduct(productId.toString());
        if (response.data.code === '200') {
          ElMessage.success('商品删除成功');
          router.push('/home/all-products');
        } else { ElMessage.error(response.data.msg || '删除失败'); }
      } catch (error) {
        if(error !== 'cancel') console.error('删除商品失败:', error);
      }
    };

    const updateStock = async () => {
      try {
        const response = await updateStockpile({ productId: productId.toString(), amount: newStock.value });
        if (response.data.code === '200') {
          stockAmount.value = newStock.value;
          ElMessage.success('库存更新成功');
        } else { ElMessage.error(response.data.msg || '更新库存失败'); }
      } catch (error) { console.error('更新库存失败:', error); }
    };

    const getStock = async () => {
      try {
        const response = await getStockpile(productId);
        if(response.data.code === '200') stockAmount.value = response.data.data.amount;
      } catch(e) { console.error(e) }
    };

    const handleCreateComment = async () => { /* ... */ };
    const viewAllComments = () => { router.push({ name: 'ProductComments', params: { productId: productId } }); };
    function addSpecification() { newSpecifications.value.push({item: '', value: ''}); }
    function removeSpecification(index: number) { newSpecifications.value.splice(index, 1); }

    onMounted(async () => {
      if (productId) {
        await loadProductDetails(productId);
        await getStock();
      }
      try {
        const res = await getAllColumns();
        if (res.data.code === "200") {
          allColumns.value = res.data.data;
        }
      } catch (error) { console.error("获取栏目列表失败", error); }
    });

    // 修正3: 返回所有需要在模板中使用的变量和函数
    return {
      product, specifications, quantity, maxQuantity, addToCart, role, newStock, deleteProduct, updateStock, stockAmount, showEditDialog, fileList, dialogImageUrl, dialogVisible, editForm, newSpecifications, openEditDialog, handleChange, handlePictureCardPreview, handleRemove, handleUpdateProduct, addSpecification, removeSpecification, viewAllComments, showCommentForm, commentContent, commentScore, handleCreateComment, allColumns, associatedColumns, isColumnDialogVisible, isColumnLoading, selectedColumn, columnProducts, showColumnProducts, goToProductDetail
    };
  },
});
</script>

<style scoped>
html, body { height: 100%; }
.product-detail { padding: 20px; display: flex; justify-content: center; align-items: center; flex-direction: column; background-image: url("../../assets/780.jpg"); background-size: cover; background-position: center top; min-height: 100vh; color: black; }
.product-info { display: flex; justify-content: center; align-items: flex-start; width: 100%; max-width: 1200px; margin: 0 auto 20px auto; background-color: rgba(255, 255, 255, 0.6); padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }
.product-image { flex: 1; margin-right: 20px; }
.product-details { flex: 2; margin-left: 20px; }
.product-details h1 { font-size: 2.5em; margin-bottom: 10px; color: #333; }
.price { font-size: 24px; font-weight: bold; color: #e60023; margin-bottom: 10px; }
.rating { color: goldenrod; font-size: 1.1em; margin-bottom: 10px; }
.description, .detail { margin: 10px 0; line-height: 1.6; color: #555; }
.product-details h3 { margin-top: 20px; margin-bottom: 10px; color: #333; }
.product-details ul { list-style: none; padding: 0; }
.product-details li { margin-bottom: 5px; color: #666; }
.comment-area { display: flex; gap: 20px; margin-top: 20px; margin-bottom: 20px; }
.action-area { display: flex; justify-content: center; align-items: center; flex-direction: column; background-color: rgba(255, 255, 255, 0.6); padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); width: 100%; max-width: 1200px; }
.customer-actions-group, .manager-actions-group { display: flex; flex-direction: column; align-items: center; width: 100%; margin-bottom: 15px; }
.stock-display { font-size: 1.2em; font-weight: bold; color: white; text-shadow: 1px 1px 2px rgba(0,0,0,0.7); margin-bottom: 15px; }
.action-row { display: flex; align-items: center; gap: 20px; margin-bottom: 15px; }
.action-row .el-input-number { width: 150px; }
.action-row .el-button { padding: 10px 20px; font-size: 1em; }
.management-buttons { margin-top: 10px; }
.product-form .el-form-item { margin-bottom: 20px; }
.specification-item { display: flex; align-items: center; margin-bottom: 10px; gap: 10px; }
.dialog-image { max-width: 100%; height: auto; display: block; }
.create-comment-box { background-color: rgba(255, 255, 255, 0.7); padding: 20px; margin-top: 20px; border-radius: 8px; width: 100%; max-width: 800px; }
.custom-black-button { background-color: #000 !important; border-color: #000 !important; color: white !important; }
.comment-form .el-form-item__label { color: #333; }
.btn-camel { background-color: #7b6b4d !important; color: #ffffff !important; border-color: #7b6b4d !important; }
.btn-camel:hover { background-color: #5f543d !important; border-color: #5f543d !important; }
.cover-carousel { margin: 20px 0; border-radius: 8px; overflow: hidden; }
.carousel-image { width: 100%; height: 400px; object-fit: contain; border-radius: 8px; user-select: none; pointer-events: none; }
.no-image-placeholder { width: 100%; height: 400px; background-color: #f5f7fa; display: flex; align-items: center; justify-content: center; color: #909399; border-radius: 8px; }
.columns-section { margin-top: 20px; }
.column-tag.clickable { cursor: pointer; margin-right: 8px; margin-bottom: 8px; transition: all 0.2s; }
.column-tag.clickable:hover { transform: scale(1.08); }
.dialog-book-list { display: flex; flex-wrap: wrap; gap: 16px; justify-content: center; max-height: 60vh; overflow-y: auto; padding: 10px; }
.product-card-small { width: 140px; cursor: pointer; transition: transform 0.2s ease; }
.product-card-small:hover { transform: scale(1.05); }
.product-image-small img { width: 100%; height: 150px; object-fit: cover; }
.product-title-small { font-size: 13px; margin: 6px 0 0 0; text-align: center; }
</style>