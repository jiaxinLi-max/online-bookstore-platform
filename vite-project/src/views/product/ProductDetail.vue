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
            {{ showCommentForm ? '收起评论框' : '发表评价' }}
          </el-button>
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
                  @click.prevent="handleCreateComment(null)"
                  :disabled="!commentContent || !commentScore"
                  type="primary"
                  plain
                  class="custom-black-button"
              >
                提交评价
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

    <el-card class="comments-section-card">
      <template #header>
        <div class="comments-header">
          <h2>用户评价</h2>
          <div class="sort-select-wrapper">
            <el-select v-model="sortOption" placeholder="排序方式" @change="fetchCommentsBySort" class="sort-select">
              <el-option label="按时间排序" value="time" />
              <el-option label="按点赞排序" value="like" />
            </el-select>
          </div>
        </div>
      </template>

      <div v-if="comments.length > 0" class="comment-list">
        <el-card v-for="comment in comments" :key="comment.id" class="comment-card" shadow="never">
          <div class="comment-main">
            <el-avatar :src="comment.avatar" class="comment-avatar" size="large" />
            <div class="comment-content">
              <h3 class="username">{{ comment.username }}</h3>
              <p class="comment-time">🕒 {{ formatTime(comment.time) }}</p>
              <div class="score-like-row">
                <span>评分：<el-rate v-model="comment.score" disabled size="small" /></span>
                <span class="like-count">👍 点赞数：{{ comment.likes }}</span>
              </div>
              <p class="comment-text-body">{{ comment.content }}</p>
            </div>
          </div>
          <div class="comment-actions">
            <!-- 【新增】点赞和删除按钮 -->
            <el-button type="warning" link @click.stop="handleLikeComment(comment.id)" v-if="role === 'CUSTOMER'">点赞</el-button>
            <el-button type="primary" link @click="openReplyDialog(comment)">查看/回复</el-button>
            <el-button type="danger" link @click.stop="handleDeleteComment(comment.id)" v-if="role === 'MANAGER' || comment.userId === currentUserId">删除</el-button>
          </div>
        </el-card>
      </div>
      <el-empty v-else description="暂无评价，快来抢沙发吧！"></el-empty>
    </el-card>

    <el-dialog v-model="isColumnDialogVisible" :title="selectedColumn.theme + ' 栏目下的所有书籍'" width="60%">
      <div v-loading="isColumnLoading">
        <div v-if="columnProducts.length > 0" class="dialog-book-list">
          <el-card v-for="p in columnProducts" :key="p.id" class="product-card-small" @click="goToProductDetail(p.id)" shadow="hover">
            <div class="product-image-small">
              <img :src="Array.isArray(p.cover) && p.cover.length > 0 ? p.cover[0] : ''" alt="Product Cover" />
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
          <el-select v-model="editForm.columnIds" multiple placeholder="请选择栏目" style="width: 100%;"><el-option v-for="column in allColumns" :key="column.id" :label="column.theme" :value="column.id" /></el-select>
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
          <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePictureCardPreview" multiple>
            <el-icon><Plus /></el-icon>
            <div>点击上传或修改封面</div>
          </el-upload>
        </el-form-item>
        <el-form-item><el-button @click.prevent="handleUpdateProduct" type="primary" plain>更新商品</el-button></el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog v-model="dialogVisible"><img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" /></el-dialog>

    <el-dialog v-model="replyDialogVisible" :title="`回复“${currentParentComment?.username}”的评论`" width="60%" top="5vh">
      <div class="parent-comment-in-dialog">
        <el-card><p>{{ currentParentComment?.content }}</p></el-card>
      </div>
      <el-divider>回复列表</el-divider>
      <div v-loading="subCommentsLoading" class="sub-comments-container">
        <div v-if="subComments.length > 0">
          <div v-for="reply in subComments" :key="reply.id" class="sub-comment-item">
            <el-avatar :src="reply.avatar" size="default" />
            <div class="sub-comment-content">
              <span class="sub-comment-username">{{ reply.username }}</span>
              <p class="sub-comment-text">{{ reply.content }}</p>
              <span class="sub-comment-time">{{ formatTime(reply.time) }}</span>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无回复"></el-empty>
      </div>
      <el-divider>发表你的回复</el-divider>
      <div class="reply-form">
        <el-input v-model="replyContent" type="textarea" :rows="3" placeholder="请输入回复内容..."/>
        <el-button type="primary" @click="handlePostReply" :disabled="!replyContent" style="margin-top: 10px;">提交回复</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { deleteTheProduct, getProduct, updateProductInfo, updateStockpile, getStockpile } from '../../api/product.ts';
import { getAllColumns, getProductsByColumn } from '../../api/columns.ts';
import { addCart } from '../../api/cart.ts';
import { getTheAllComment, getSubComments, createComment, deleteComment, likeComment, type Comment } from "../../api/comment.ts";
import { getUserInfo } from "../../api/user.ts";
import { sortByTime, sortByLike } from '../../api/tools.ts';
import { Specification } from "../../api/specification.ts";
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import { getImage } from "../../api/tools.ts";
import type { Product } from '../../api/product';

export default defineComponent({
  name: 'ProductDetail',
  components: { Plus },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const productId = Array.isArray(route.params.productId) ? route.params.productId[0] : route.params.productId;
    const currentUserId = Number(sessionStorage.getItem('userId'));

    // --- 原有状态 ---
    const product = ref<Partial<Product>>({ cover: [], columnIds: [] });
    const specifications = ref<Specification[]>([]);
    const allColumns = ref<{id: number, theme: string}[]>([]);
    const quantity = ref(1);
    const maxQuantity = ref(10);
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

    // --- 评价模块状态 ---
    const comments = ref<(Comment & { username: string; avatar: string; })[]>([]);
    const sortOption = ref<'time' | 'like'>('time');
    const showCommentForm = ref(false);
    const commentContent = ref('');
    const commentScore = ref<number>(0);
    const replyDialogVisible = ref(false);
    const subCommentsLoading = ref(false);
    const currentParentComment = ref<(Comment & { username: string; avatar: string; }) | null>(null);
    const subComments = ref<(Comment & { username: string; avatar: string; })[]>([]);
    const replyContent = ref('');

    // --- 计算属性 ---
    const associatedColumns = computed(() => {
      if (!product.value.columnIds || !allColumns.value.length) return [];
      return allColumns.value.filter(col => product.value.columnIds!.includes(col.id));
    });

    const editFormCovers = computed(() =>
        fileList.value.filter(f => f.status === 'success' && f.url).map(f => f.url!)
    );

    function formatTime(timeStr: string): string {
      const date = new Date(timeStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}年${month}月${day}日 ${hours}时${minutes}分`;
    }

    // --- 方法 ---
    const loadProductDetails = async (id: string) => {
      try {
        const response = await getProduct(id);
        if (response.data.code == 200) {
          const productData = response.data.data;
          product.value = productData;
          if (!Array.isArray(product.value.cover)) product.value.cover = [];
          specifications.value = productData.specifications || [];
        }
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

    const fetchComments = async () => {
      try {
        const res = await getTheAllComment(productId);
        if (res.data.code == 200 && Array.isArray(res.data.data)) {
          const rawComments: Comment[] = res.data.data;
          const enriched = await Promise.all(
              rawComments.map(async (comment) => {
                let username = '未知用户', avatar = '';
                try {
                  const userRes = await getUserInfo(comment.userId);
                  if (userRes.data.code == 200) {
                    username = userRes.data.data.username;
                    avatar = userRes.data.data.avatar;
                  }
                } catch (e) {}
                return { ...comment, username, avatar };
              })
          );
          comments.value = enriched;
        }
      } catch (err) { console.error('获取评论失败:', err); }
    };

    const fetchCommentsBySort = async () => {
      try {
        let res;
        if (sortOption.value === 'time') {
          res = await sortByTime('comment', Number(productId));
        } else {
          res = await sortByLike('comment', Number(productId));
        }
        if (res.data.code == 200 && Array.isArray(res.data.data)) {
          const rawComments: Comment[] = res.data.data;
          const enriched = await Promise.all(
              rawComments.map(async (comment) => {
                let username = '未知用户', avatar = '';
                try {
                  const userRes = await getUserInfo(comment.userId);
                  if (userRes.data.code == 200) {
                    username = userRes.data.data.username;
                    avatar = userRes.data.data.avatar;
                  }
                } catch (e) {}
                return { ...comment, username, avatar };
              })
          );
          comments.value = enriched;
        }
      } catch (err) { console.error('排序获取评论失败:', err); }
    };

    const openReplyDialog = async (parentComment: any) => {
      replyDialogVisible.value = true;
      subCommentsLoading.value = true;
      currentParentComment.value = parentComment;
      subComments.value = [];
      try {
        const res = await getSubComments(parentComment.id);
        if (res.data.code == 200 && Array.isArray(res.data.data)) {
          const rawSubComments: Comment[] = res.data.data;
          const enrichedSubComments = await Promise.all(
              rawSubComments.map(async (reply) => {
                let username = '未知用户', avatar = '';
                try {
                  const userRes = await getUserInfo(reply.userId);
                  if (userRes.data.code == 200) {
                    username = userRes.data.data.username;
                    avatar = userRes.data.data.avatar;
                  }
                } catch(e) {}
                return { ...reply, username, avatar };
              })
          );
          subComments.value = enrichedSubComments;
        }
      } catch(error) { console.error("获取子评论失败:", error); }
      finally { subCommentsLoading.value = false; }
    };

    const handleCreateComment = async (parentId: number | null) => {
      const token = sessionStorage.getItem('token');
      if (!token) { ElMessage.error('请先登录!'); return; }

      const payload = {
        userId: currentUserId,
        productId: Number(productId),
        content: parentId ? replyContent.value : commentContent.value,
        score: parentId ? 0 : commentScore.value,
        ...(parentId && { parentId: parentId })
      };

      try {
        const res = await createComment(payload);
        if (res.data.code == 200) {
          ElMessage.success(parentId ? '回复成功' : '评价成功');
          if (parentId && currentParentComment.value) {
            replyContent.value = '';
            await openReplyDialog(currentParentComment.value);
          } else {
            commentContent.value = '';
            commentScore.value = 0;
            showCommentForm.value = false;
            await fetchComments();
          }
        } else { ElMessage.error(res.data.message || "操作失败"); }
      } catch (error) { ElMessage.error('操作失败'); }
    };

    const handlePostReply = () => {
      if(currentParentComment.value) {
        handleCreateComment(currentParentComment.value.id);
      }
    };

    // --- 【新增】点赞和删除方法 ---
    const handleLikeComment = async (commentId: number) => {
      try {
        const res = await likeComment(commentId, currentUserId);
        if (res.data.code == 200) {
          ElMessage.success(res.data.data);
          await fetchComments(); // 刷新列表以更新点赞数
        } else {
          ElMessage.error(res.data.msg || "点赞失败");
        }
      } catch (error) {
        ElMessage.error("点赞操作失败");
      }
    };

    const handleDeleteComment = async (commentId: number) => {
      try {
        await ElMessageBox.confirm('确定要删除这条评价吗？', '警告', { type: 'warning' });
        const res = await deleteComment(commentId);
        if (res.data.code == 200) {
          ElMessage.success("删除成功");
          await fetchComments(); // 刷新列表
        } else {
          ElMessage.error(res.data.msg || "删除失败");
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error("删除操作失败");
        }
      }
    };

    // --- 原有其他方法 ---
    // 【修改点2】: 更新handleChange以支持多图
    async function handleChange(file: UploadFile, newFileList: UploadFile[]) {
      const rawFile = file.raw;
      if (!rawFile) return;

      try {
        const res = await getImage(rawFile);
        if (res && res.code === '200') {
          console.log("上传");
          editForm.value.cover.push(res.data); // 添加URL到数组
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
      const payload = {
        ...editForm.value,
        specifications: newSpecifications.value,
        cover: editForm.value.cover,
      };
      try {
        const res = await updateProductInfo(payload as Product);
        if (res.data.code == 200) {
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
        if(res.data.code == 200) {
          columnProducts.value = res.data.data.filter((p: any) => p.id !== Number(productId));
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
        if (response.data.code == 200) {
          ElMessage.success('商品删除成功');
          router.push('/home/all-products');
        } else { ElMessage.error(response.data.msg || '删除失败'); }
      } catch (error) { if(error !== 'cancel') console.error('删除商品失败:', error); }
    };
    const updateStock = async () => {
      try {
        const response = await updateStockpile({ productId: productId.toString(), amount: newStock.value });
        if (response.data.code == 200) {
          stockAmount.value = newStock.value;
          ElMessage.success('库存更新成功');
        } else { ElMessage.error(response.data.msg || '更新库存失败'); }
      } catch (error) { console.error('更新库存失败:', error); }
    };
    const getStock = async () => {
      try {
        const response = await getStockpile(productId);
        if(response.data.code == 200) stockAmount.value = response.data.data.amount;
      } catch(e) { console.error(e) }
    };
    function addSpecification() { newSpecifications.value.push({item: '', value: ''}); }
    function removeSpecification(index: number) { newSpecifications.value.splice(index, 1); }

    onMounted(async () => {
      if (productId) {
        await loadProductDetails(productId);
        await getStock();
        await fetchComments();
      }
      try {
        const res = await getAllColumns();
        if (res.data.code == 200) {
          allColumns.value = res.data.data;
        }
      } catch (error) { console.error("获取栏目列表失败", error); }
    });

    return {
      product, specifications, quantity, maxQuantity, addToCart, role, newStock, deleteProduct, updateStock, stockAmount, showEditDialog, fileList, dialogImageUrl, dialogVisible, editForm, newSpecifications, openEditDialog,
      handleChange, handlePictureCardPreview, handleRemove, handleUpdateProduct, addSpecification, removeSpecification,
      showCommentForm, commentContent, commentScore, handleCreateComment, allColumns, associatedColumns, isColumnDialogVisible, isColumnLoading, selectedColumn, columnProducts, showColumnProducts, goToProductDetail,
      comments, sortOption, formatTime, fetchComments, fetchCommentsBySort,
      replyDialogVisible, subCommentsLoading, currentParentComment, subComments, replyContent, openReplyDialog, handlePostReply,
      handleLikeComment, handleDeleteComment, currentUserId
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

/* 新增的评论区样式 */
.comments-section-card { width: 100%; max-width: 1200px; margin-top: 20px; background-color: rgba(255, 255, 255, 0.8); }
.comments-header { display: flex; justify-content: space-between; align-items: center; }
.sort-select-wrapper { margin-left: auto; }
.sort-select { width: 140px; }
.comment-list { display: flex; flex-direction: column; gap: 15px; padding: 10px 0; }
.comment-card { border-bottom: 1px solid #eee; padding-bottom: 15px; }
.comment-card:last-child { border-bottom: none; }
.comment-main { display: flex; gap: 15px; }
.comment-avatar { flex-shrink: 0; }
.comment-content { flex-grow: 1; }
.username { font-weight: bold; }
.comment-time { font-size: 0.8em; color: #909399; }
.score-like-row { display: flex; align-items: center; gap: 20px; margin: 5px 0; }
.comment-text-body { margin: 8px 0; }
.comment-actions { text-align: right; margin-top: 10px; }
.parent-comment-in-dialog { background-color: #f9f9f9; padding: 15px; border-radius: 8px; margin-bottom: 20px; }
.sub-comments-container { min-height: 150px; max-height: 40vh; overflow-y: auto; padding-right: 10px; }
.sub-comment-item { display: flex; gap: 10px; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #f0f0f0; }
.sub-comment-item:last-child { border-bottom: none; }
.sub-comment-content { flex: 1; }
.sub-comment-username { font-weight: bold; }
.sub-comment-text { margin: 4px 0; }
.sub-comment-time { font-size: 0.8em; color: #909399; text-align: right; }
.reply-form { margin-top: 20px; }
</style>
