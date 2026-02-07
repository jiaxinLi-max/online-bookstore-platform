<template>
  <div class="product-detail">
    <!-- 左侧：图片 + 详情 可滑动 -->
    <div class="left-scroll">
      <!-- 1. 图片轮播 -->
      <!-- 1. 左侧小图 + 右侧大图 -->
      <div class="pic-and-info">
        <!-- 小图列表 -->
        <div class="thumb-list">
          <img
              v-for="(img, idx) in (product.cover || [])"
              :key="idx"
              :src="img"
              class="thumb-item"
              :class="{ active: idx === currentIndex }"
              @click="currentIndex = idx"
          />
        </div>

        <!-- 大图 -->
        <div class="big-img">
          <img :src="(product.cover && product.cover.length) ? product.cover[currentIndex] : ''" alt="书籍大图" />
        </div>
      </div>

      <!-- 2. 商品信息 -->
      <div class="product-meta">
        <h1>{{ product.title }}</h1>

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

      <!-- 3. 评论区 -->
      <!-- 评价输入框：左侧上方 -->
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

      <el-card class="comments-section-card">
        <template #header>
          <!-- 评论区头部 -->
          <div class="comments-header">
            <h2>用户评价</h2>
            <el-rate
                :model-value="Number(product.rate)"
                disabled
                show-score
                text-color="#ff6700"
                score-template="{value} 分"
            />
            <!-- 排序下拉 -->
            <el-dropdown trigger="click" @command="handleSort">
              <el-button size="small" class="btn-sort">
                默认排序<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="time">时间排序</el-dropdown-item>
                  <el-dropdown-item command="like">点赞排序</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button
                class="btn-camel"
                @click="showCommentForm = !showCommentForm"
            >
              {{ showCommentForm ? '收起评价' : '发表评价' }}
            </el-button>
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
                  <el-rate
                      :model-value="comment.score"
                      disabled
                      show-score
                      text-color="#ff6700"
                      score-template="{value} 分"
                      size="small"
                  />
                </div>
                <p class="comment-text-body">{{ comment.content }}</p>
              </div>
            </div>
            <div class="comment-actions">
              <!-- 图标点赞 + 数字（UI 不改你的属性/函数） -->
              <span class="like-box" v-if="role === 'CUSTOMER'" @click.stop="handleLikeComment(comment.id)">
  <span class="like-icon" :class="{ active: comment.likes }">
    <!-- 空心 -->
    <svg v-if="!comment.likes" viewBox="0 0 24 24" width="20" height="20">
      <path d="M12.1 8.64l-.1.1-.11-.11C10.14 6.78 7.1 6.86 5.36 8.6c-1.73 1.73-1.82 4.74-.09 6.47l6.36 6.36c.2.2.51.2.71 0l6.36-6.36c1.73-1.73 1.63-4.74-.1-6.47-1.73-1.73-4.77-1.82-6.5-.09z"
            fill="none" stroke="#bbb" stroke-width="2"/>
    </svg>

    <!-- 实心 -->
    <svg v-else viewBox="0 0 24 24" width="20" height="20">
      <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5
               2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09
               C13.09 3.81 14.76 3 16.5 3
               19.58 3 22 5.42 22 8.5
               c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
            fill="#e63946"/>
    </svg>
  </span>

  <span class="like-num">{{ comment.likes }}</span>
</span>



              <!-- 删除按钮完全不变 -->
              <el-button
                  type="danger"
                  link
                  @click.stop="handleDeleteComment(comment.id)"
                  v-if="role === 'MANAGER' || comment.userId === currentUserId"
              >删除</el-button>
            </div>
          </el-card>
        </div>
        <el-empty v-else description="暂无评价，快来抢沙发吧！"></el-empty>
      </el-card>
    </div>

    <!-- 右侧：固定购物车操作区 -->
    <div class="right-sticky">
      <div class="action-area">
        <!-- 顾客版 -->
        <div v-if="role === 'CUSTOMER'" class="customer-actions-group">
          <h2 class="right-title">{{ product.title }}</h2>
          <div class="price-big">{{ product.price }}</div>
          <div class="quantity-row">
            <el-input-number v-model="quantity" :min="1" :max="maxQuantity" label="选择数量"></el-input-number>
            <span v-if="stockAmount <= 10" class="stock-tips">库存紧张</span>
          </div>

          <el-card class="buy-card" shadow="never">
            <div class="action-row">

              <el-button class="add-to-cart-btn" @click="addToCart">加入购物车</el-button>
            </div>
          </el-card>
          <!-- 发表评价按钮及表单已移除 -->
        </div>

        <!-- 管理员版 -->
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
  </div>
  <el-dialog
      v-model="showEditDialog"
      title="更新商品信息"
      width="60%"
      :close-on-click-modal="false"
  >
    <el-form :model="editForm" label-width="100px">
      <el-form-item label="书籍标题">
        <el-input v-model="editForm.title"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="editForm.description"></el-input>
      </el-form-item>
      <el-form-item label="详情">
        <el-input type="textarea" v-model="editForm.detail"></el-input>
      </el-form-item>
      <el-form-item label="价格">
        <el-input-number v-model="editForm.price" :precision="2" :step="1" :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="所属栏目">
        <el-select v-model="editForm.columnIds" multiple placeholder="请选择所属栏目">
          <el-option
              v-for="column in allColumns"
              :key="column.id"
              :label="column.theme"
              :value="column.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- 书籍信息/规格 -->
      <el-form-item label="书籍信息">
        <div v-for="(spec, index) in newSpecifications" :key="index" class="specification-row" style="display: flex; margin-bottom: 10px; gap: 10px;">
          <el-input v-model="spec.item" placeholder="项目名 (如: 作者)"></el-input>
          <el-input v-model="spec.value" placeholder="项目值 (如: 张三)"></el-input>
          <el-button type="danger" @click="removeSpecification(index)" plain>删除</el-button>
        </div>
        <el-button @click="addSpecification" type="primary" plain>添加规格</el-button>
      </el-form-item>

      <!-- 封面上传 -->
      <el-form-item label="书籍封面">
        <el-upload
            v-model:file-list="fileList"
            action="#"
            list-type="picture-card"
            :http-request="() => {}"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleChange"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
      </el-form-item>

    </el-form>

    <!-- 对话框底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProduct">
          确认更新
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="dialogVisible">
    <img w-full :src="dialogImageUrl" alt="Preview Image" style="width: 100%;" />
  </el-dialog>
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

import { ArrowDown } from '@element-plus/icons-vue';

export default defineComponent({
  name: 'ProductDetail',
  //components: { Plus, ThumbFilled }, // <-- 注册 StarFilled，修复图标不显示问题
  components: { ArrowDown },
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
    const MAX_SIZE = 1024 * 1024;

    const handleSort = (cmd: 'time' | 'like') => {
      sortOption.value = cmd;
      fetchCommentsBySort();
    };
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
      console.log('点击更新商品');
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
        await ElMessageBox.confirm('确定要删除这条评价吗？', '提示', { type: 'warning' });
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

    const handleRemove = (file: UploadFile) => {
      const urlToRemove = file.url;
      fileList.value = fileList.value.filter(item => item.uid !== file.uid);
      if (urlToRemove) {
        editForm.value.cover = editForm.value.cover.filter(url => url !== urlToRemove);
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
    function addSpecification() { newSpecifications.value.push({item: " ", value: " "}); }
    function removeSpecification(index: number) { newSpecifications.value.splice(index, 1); }
    const currentIndex = ref(0)   // 默认显示第一张
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
      handleLikeComment, handleDeleteComment, currentUserId,
      currentIndex,
      handleSort,
    };
  },
});
</script>

<style scoped>
html, body { height: 100%; }
.product-detail{
  display: flex;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  background: none;          /* 背景图仍在最外层 */
  box-shadow: none;
  border-radius: 0;
}
.left-scroll{
  flex: 0 0 58%;        /* 原来 1 → 现在 52% */
  max-width: 58%;
  max-height: 100vh;
  overflow-y: auto;
  background-color: rgba(255,255,255,.6);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,.2);
}
.right-sticky{
  flex: 0 0 40%;        /* 原来 360px → 现在 46% */
  max-width: 40%;
  position: sticky;
  top: 20px;
  width: 360px;              /* 固定宽度，可自己调 */
}

.right-sticky .action-area {
  background: none;              /* 不要背景 */
  box-shadow: none;              /* 不要阴影 */
  border: none !important;       /* 所有边框清零 */
  padding: 0;                    /* 把原内边距也清掉，后面单独给按钮卡片补 */
}
.stock-tips {
  font-size: 12px;
  color: #999;          /* 淘宝同款灰 */
  margin-left: 8px;     /* 和数量器隔一点 */
  white-space: nowrap;  /* 禁止换行 */
}
.buy-card {
  border-radius: 8px;
  padding: 12px 16px;
  margin: 12px 0;
  background-color: rgba(255,255,255,.75);
  box-shadow: 0 2px 6px rgba(0,0,0,.08);
}
/* 让内部一行两端对齐，更淘宝 */
.buy-card .action-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}
/* 微调小屏响应式 */
@media (max-width: 1024px){
  .product-detail{ flex-direction: column; }
  .right-sticky{ width: 100%; position: static; }
}
.pic-and-info{
  display: flex;
  gap: 12px;
}
.thumb-list{
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 80px;
}
.thumb-item{
  width: 100%;
  height: 80px;
  object-fit: cover;
  border: 2px solid transparent;
  cursor: pointer;
  border-radius: 4px;
}
.thumb-item.active{
  border-color: #ff6700;
}
.big-img{
  flex: 1;
  max-width: 420px;
}
.big-img img{
  width: 100%;
  height: 420px;
  object-fit: contain;
  border-radius: 6px;
}
.price-big{
  font-size: 32px;
  color: #e60023;
  font-weight: bold;
  margin-bottom: 10px;
}
.like-heart {
  cursor: pointer;
  font-size: 20px;
  transition: 0.15s;
}

.like-heart:hover {
  transform: scale(1.15);
}

.rating-bottom{
  margin: 20px 0;
  text-align: center;
  font-size: 16px;
}
.rating-text{ margin-left: 8px; }
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
.like-icon {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.like-icon:hover {
  transform: scale(1.15);
}

.like-icon.active svg {
  transform: scale(1.1);
}

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
/* -------------- 下拉按钮 -------------- */
.btn-sort {
  background-color: #ffffff !important;   /* 纯白底 */
  color: #606266 !important;            /* 字用 Element 默认灰 */
  border: none !important;              /* 彻底去掉边框 */
  padding: 6px 12px;                    /* 让文字和箭头更紧凑 */
  font-size: 14px;
  line-height: 1;
  box-shadow: none !important;
}

/* 评论区头部整体布局：评分居中偏左 */
.comments-header{
  display: flex;
  align-items: center;
  justify-content: flex-start;   /* 先靠左排列 */
  gap: 16px;
}
/* 让评分星星独占一块弹性宽度，实现“居中偏左” */
.comments-header h2 + .el-rate{
  flex: 1;                      /* 把剩余空间占满 */
  display: flex;
  justify-content: center;      /* 在其独占行里居中 */
  margin-left: -4%;             /* 再往左偏移一点，视觉效果“偏左” */
}
/* 在 scoped 下使用深度选择器，覆盖 Element Plus 下拉项的 hover/active/focus */
::v-deep(.el-dropdown-menu__item),
::v-deep(.el-dropdown-item) {
  background-color: #ffffff !important; /* 纯白底 */
  color: #606266 !important;            /* 默认灰字 */
}

/* 悬停 / 聚焦 / 激活 状态：浅羊驼底 + 深羊驼字 */
::v-deep(.el-dropdown-menu__item:hover),
::v-deep(.el-dropdown-item:hover),
::v-deep(.el-dropdown-menu__item:focus),
::v-deep(.el-dropdown-item:focus),
::v-deep(.el-dropdown-menu__item.is-active),
::v-deep(.el-dropdown-item.is-active),
::v-deep(.el-dropdown-menu__item.el-dropdown-menu__item--hover),
::v-deep(.el-dropdown-item.el-dropdown-item--hover) {
  background-color: #f5f1eb !important; /* 浅羊驼 */
  color: #7b6b4d !important;            /* 深羊驼 */
}

/* 禁用默认蓝色文字阴影/边框（如果有的话） */
::v-deep(.el-dropdown-menu__item),
::v-deep(.el-dropdown-item) {
  box-shadow: none !important;
  border-color: transparent !important;
}

/* 如果菜单项内含 Icon，让 Icon 跟随文字颜色 */
::v-deep(.el-dropdown-menu__item .el-icon),
::v-deep(.el-dropdown-item .el-icon) {
  color: currentColor !important;
  fill: currentColor !important;
}


 html, body { height: 100%; margin: 0; padding: 0; }

.product-detail{
  display: flex;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  max-width: 1400px;
  margin: 30px auto 50px; /* 增加上下边距 */
  background: none;
  box-shadow: none;
  border-radius: 0;
  min-height: calc(100vh - 100px); /* 保证最小高度 */
}

/* 左侧区域 */
.left-scroll{
  flex: 0 0 58%;
  max-width: 58%;
  max-height: calc(100vh - 100px); /* 调整最大高度 */
  overflow-y: auto;
  background-color: rgba(255,255,255,.6);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,.15);
  margin-top: 20px; /* 与右侧对齐 */
}

/* 右侧固定区域 - 改为淘宝样式 */
.right-sticky{
  flex: 0 0 40%;
  max-width: 40%;
  position: sticky;
  top: 30px; /* 调整粘性位置 */
  width: 360px;
  margin-top: 20px;
}

.right-sticky .action-area {
  background: #fff;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,.08);
}

/* 右侧标题 */
.right-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 15px 0;
  line-height: 1.4;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 10px;
}

/* 价格区域 - 淘宝样式 */
.price-big {
  font-size: 28px;
  color: #fff !important; /* 白色文字 */
  font-weight: bold;
  margin-bottom: 20px;
  background-color: #333; /* 深灰色背景 */
  padding: 12px 16px;
  border-radius: 6px;
  text-align: center;
  width: 100%;
  box-sizing: border-box;
}
/* 购买卡片样式调整 */
.buy-card {
  border-radius: 8px;
  padding: 16px;
  margin: 16px 0;
  background-color: #f9f9f9;
  border: 1px solid #eee;
  box-shadow: 0 1px 3px rgba(0,0,0,.05);

  width: 600px;       /* 卡片变长 */
  max-width: 100%;    /* 自适应屏幕 */
  margin-left: 0;     /* 靠左对齐 */
}

/* 按钮容器 */
.buy-card .action-row {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  gap: 16px;
  width: 100%;
}

/* 数量选择器行 */
.quantity-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  margin-bottom: 10px;
}

/* 库存提示 */
.stock-tips {
  font-size: 12px;
  color: #ff5500; /* 橙色醒目 */
  margin-left: 8px;
  white-space: nowrap;
  font-weight: bold;
}

/* 加入购物车按钮样式 */
.add-to-cart-btn {
  width: 100%;           /* 长条效果 */
  background-color: #121212; /* 黑色背景 */
  color: #fff;           /* 白色文字 */
  border: none;
  border-radius: 6px;
  padding: 12px 0;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s;
}

.add-to-cart-btn:hover {
  background-color:  #ffcc00 !important;
  border-color:  #ffcc00 !important;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255,103,0,.2);
}


/* 管理员样式调整 */
.manager-actions-group {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
}

.stock-display {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
}

.stock-control {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.management-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 响应式调整 */
@media (max-width: 1024px){
  .product-detail{
    flex-direction: column;
    margin: 20px auto;
  }
  .left-scroll, .right-sticky{
    max-width: 100%;
    width: 100%;
  }
  .right-sticky{
    position: static;
    margin-top: 30px;
  }
}

/* 图片部分保持原样 */
.pic-and-info{
  display: flex;
  gap: 12px;
  margin-bottom: 25px;
}

.thumb-list{
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 80px;
}

.thumb-item{
  width: 100%;
  height: 80px;
  object-fit: cover;
  border: 2px solid transparent;
  cursor: pointer;
  border-radius: 4px;
}

.thumb-item.active{
  border-color: #ff6700;
}

.big-img{
  flex: 1;
  max-width: 420px;
}

.big-img img{
  width: 100%;
  height: 420px;
  object-fit: contain;
  border-radius: 6px;
}

/* 商品信息区域 */
.product-meta {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 25px;
  border: 1px solid #e5e5e5;
}

.product-meta h1 {
  font-size: 24px;
  color: #333;
  margin: 0 0 15px 0;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 10px;
}

.product-meta .description,
.product-meta .detail {
  color: #666;
  line-height: 1.6;
  margin: 10px 0;
  padding: 8px 0;
  border-bottom: 1px dashed #eee;
}

/* 评论区样式调整 */
.comments-section-card {
  width: 100%;
  margin-top: 25px;
  background-color: #fff;
  border: 1px solid #e5e5e5;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

/* 保持原有其他样式不变 */
.like-heart {
  cursor: pointer;
  font-size: 20px;
  transition: 0.15s;
}

.like-heart:hover {
  transform: scale(1.15);
}

.rating-bottom{
  margin: 20px 0;
  text-align: center;
  font-size: 16px;
}
.rating-text{ margin-left: 8px; }

/* ... 其他已有样式保持不变 ... */
/* ======== 右侧：固定购物车操作区 (重构版) ======== */
.right-sticky {
  flex: 0 0 40%;
  max-width: 40%;
  position: sticky;
  top: 30px;
  width: 360px;
  margin-top: 20px;
}

/* 操作区外壳：增加层次感 */
.right-sticky .action-area {
  background: #ffffff;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  padding: 24px;
  /* 悬浮阴影，更有质感 */
  box-shadow: 0 10px 30px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 右侧标题 */
.right-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 10px 0;
  line-height: 1.4;
  padding-bottom: 15px;
  border-bottom: 1px solid #f2f2f2;
}

/* 价格区域 - 核心修改：去掉黑框，改用电商红 */
.price-big {
  font-family: "Helvetica Neue", Arial, sans-serif;
  font-size: 36px;       /* 字体加大 */
  color: #ff4d4f !important; /* 价格红 */
  font-weight: 800;
  margin-bottom: 5px;
  background-color: transparent; /* 去掉黑色背景 */
  padding: 0;
  border-radius: 0;
  text-align: left;
  width: auto;
  display: flex;
  align-items: baseline;
}

/* 给价格加一个人民币符号 */
.price-big::before {
  content: '¥';
  font-size: 20px;
  margin-right: 4px;
  font-weight: 600;
}

/* 购买卡片容器：更清爽 */
.buy-card {
  border-radius: 12px;
  padding: 20px;
  margin: 10px 0;
  /* 淡淡的灰色背景，区分层级 */
  background-color: #f8f9fa;
  border: 1px solid #eee;
  box-shadow: none;
  width: 100%;
  box-sizing: border-box;
}

.buy-card .action-row {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

/* 数量选择器行 */
.quantity-row {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;
  margin-bottom: 0;
}

/* 库存提示 */
.stock-tips {
  font-size: 13px;
  color: #ff9900;
  background: #fffbe6;
  padding: 2px 8px;
  border-radius: 4px;
  border: 1px solid #ffe58f;
}

/* 加入购物车按钮 - 核心修改：诱人的渐变色 + 投影 */
.add-to-cart-btn {
  width: 100%;
  height: 50px;
  /* 淘宝/京东风格的暖色渐变，极具购买欲 */
  background: linear-gradient(135deg, #ff9000 0%, #ff5000 100%);
  color: #fff;
  border: none;
  border-radius: 25px; /* 胶囊圆角，更亲切 */
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 8px 15px rgba(255, 80, 0, 0.25); /* 橙色光晕 */
}

/* 悬停效果：上浮 + 阴影加深 */
.add-to-cart-btn:hover {
  background: linear-gradient(135deg, #ff8000 0%, #ff4000 100%) !important;
  border-color: transparent !important;
  transform: translateY(-2px);
  box-shadow: 0 12px 20px rgba(255, 80, 0, 0.35);
}

/* 点击效果 */
.add-to-cart-btn:active {
  transform: translateY(1px);
  box-shadow: 0 4px 10px rgba(255, 80, 0, 0.2);
}

/* 管理员区域 */
.manager-actions-group {
  width: 100%;
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  border: 1px dashed #ccc; /* 虚线框区分管理员 */
  margin-top: 20px;
}
.stock-display {
  color: #666;
  font-size: 14px;
  background: #f0f0f0;
  text-shadow: none;
  font-weight: normal;
}
</style>


