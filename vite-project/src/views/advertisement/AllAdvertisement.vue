
<!--<template>-->
<!--  <el-main class="advertisement-list bgimage">-->
<!--    &lt;!&ndash; 创建广告按钮 &ndash;&gt;-->
<!--    <div class="create-button-container" v-if="role === 'MANAGER'">-->
<!--      <el-button-->
<!--          class="create-advertisement-button"-->
<!--          @click="goToCreateAdvertisement"-->
<!--      >-->
<!--        创建广告-->
<!--      </el-button>-->
<!--    </div>-->

<!--    <el-card-->
<!--        v-for="advertisement in advertisements"-->
<!--        :key="advertisement.id"-->
<!--        class="advertisement-card"-->
<!--    >-->
<!--      <div class="advertisement-image">-->
<!--        <img :src="advertisement.imgUrl" alt="Advertisement Cover" @click="goToProductDetail(advertisement.productId)"/>-->
<!--      </div>-->
<!--      <h3>{{ advertisement.title }}</h3>-->
<!--      <p class="advertisement-content">{{ advertisement.content }}</p>-->
<!--      <p>点击查看详情</p>-->
<!--      <div v-if="role === 'MANAGER'" class="manager-actions">-->
<!--        <div class="management-buttons">-->
<!--          <el-button type="primary" @click="openEditDialog(advertisement.id)">编辑广告</el-button>-->
<!--          <el-button type="danger" @click="deleteAd(advertisement.id)">删除广告</el-button>-->
<!--        </div>-->
<!--      </div>-->
<!--      <el-dialog-->
<!--          v-model="showEditDialog"-->
<!--          title="修改广告"-->
<!--          width="40%"-->
<!--          class="edit-dialog"-->
<!--          :before-close="handleCancel"-->

<!--      >-->
<!--        <el-form ref="form" label-width="120px" class="advertisement-form">-->
<!--          <el-form-item label="广告标题" prop="advertisementName">-->
<!--            <el-input v-model="updateTitle" placeholder="请输入广告标题"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="广告内容" prop="advertisementDescription">-->
<!--            <el-input v-model="updateContent" placeholder="请输入广告内容"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="选择商品" prop="productId">-->
<!--            <el-select v-model="updateProductId" placeholder="请选择商品" style="width: 200px; margin-bottom: 20px;">-->
<!--              <el-option-->
<!--                  v-for="product in products"-->
<!--                  :key="product.id"-->
<!--                  :label="product.title"-->
<!--                  :value="product.id"-->
<!--              />-->
<!--            </el-select>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="广告图片" prop="cover">-->
<!--            <el-upload-->
<!--                action="http://localhost:8080/api/images"-->
<!--                list-type="picture-card"-->
<!--                :auto-upload="true"-->
<!--                :file-list="fileList"-->
<!--                :on-change="handleChange"-->
<!--                :on-remove="handleRemove"-->
<!--                :on-preview="handlePictureCardPreview"-->
<!--            >-->
<!--              <el-icon><Plus /></el-icon>-->
<!--              <div>点击上传广告图片</div>-->
<!--            </el-upload>-->
<!--            <el-dialog v-model="dialogVisible">-->
<!--              <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />-->
<!--            </el-dialog>-->
<!--          </el-form-item>-->

<!--          &lt;!&ndash; 按钮 &ndash;&gt;-->
<!--          <el-form-item>-->
<!--            <el-button @click.prevent="updateAd" type="primary" plain>-->
<!--              更新广告-->
<!--            </el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </el-dialog>-->
<!--    </el-card>-->
<!--  </el-main>-->
<!--</template>-->
<template>
  <el-main class="advertisement-list bgimage">
    <!-- 创建广告按钮单独一行，居中 -->
    <div class="create-button-container" v-if="role === 'MANAGER'">
      <el-button
          class="create-advertisement-button"
          @click="goToCreateAdvertisement"
      >
        创建广告
      </el-button>
    </div>

    <!-- 广告卡片单独容器 -->
    <div class="cards-container">
      <el-card
          v-for="advertisement in advertisements"
          :key="advertisement.id"
          class="advertisement-card"
      >
        <div class="advertisement-image">
          <img
              :src="advertisement.imageUrl"
              alt="Advertisement Cover"
              @click="goToProductDetail(advertisement.productId)"
          />
        </div>
        <h3>{{ advertisement.title }}</h3>
        <p class="advertisement-content">{{ advertisement.content }}</p>

        <div v-if="role === 'MANAGER'" class="manager-actions">
          <div class="management-buttons">
            <el-button type="primary" @click="openEditDialog(advertisement.id)">
              编辑广告
            </el-button>
            <el-button type="danger" @click="deleteAd(advertisement.id)">
              删除广告
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 编辑广告弹窗 -->
      <el-dialog
          v-model="showEditDialog"
          title="修改广告"
          width="40%"
          class="edit-dialog"
          :before-close="handleCancel"
      >
        <el-form ref="form" label-width="120px" class="advertisement-form">
          <el-form-item label="广告标题" prop="advertisementName">
            <el-input
                v-model="updateTitle"
                placeholder="请输入广告标题"
            ></el-input>
          </el-form-item>

          <el-form-item label="广告内容" prop="advertisementDescription">
            <el-input
                v-model="updateContent"
                placeholder="请输入广告内容"
            ></el-input>
          </el-form-item>

          <el-form-item label="选择商品" prop="productId">
            <el-select
                v-model="updateProductId"
                placeholder="请选择商品"
                style="width: 200px; margin-bottom: 20px;"
            >
              <el-option
                  v-for="product in products"
                  :key="product.id"
                  :label="product.title"
                  :value="product.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="广告图片" prop="cover">
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
              <div>点击上传广告图片</div>
            </el-upload>
            <el-dialog v-model="dialogVisible">
              <img
                  class="dialog-image"
                  :src="dialogImageUrl"
                  alt="Logo Preview"
              />
            </el-dialog>
          </el-form-item>

          <el-form-item>
            <el-button @click.prevent="updateAd" type="primary" plain>
              更新广告
            </el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </el-main>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllAdvertisement, Advertisement, updateInfo, deleteAdvertisement, updateAdvertisement } from "../../api/advertisement.ts";
import {ElMessage, type UploadFile} from "element-plus";
import {getImage} from "../../api/tools.ts";
import {Plus} from "@element-plus/icons-vue";
import {getAllProduct, Product} from "../../api/product.ts";

const advertisements = ref<Advertisement[]>([]);
const showEditDialog = ref<boolean>(false);
const router = useRouter();
const role=sessionStorage.getItem("role");
const updateId = ref<number>(0);
const updateTitle = ref<string>('');
const updateContent = ref<string>('');
const updateImgUrl = ref<string>('');
const updateProductId = ref<string>('');
const dialogImageUrl = ref<string>('');
const dialogVisible = ref<boolean>(false);
const fileList = ref<UploadFile[]>([]);
const products = ref<Product[]>([]); // 存储商品列表

// 获取所有商店数据
async function get_getAlladvertisements() {
  try {
    const res = await getAllAdvertisement();
    console.log("get_getAllAdvertisements",res);
    if (res.data && Array.isArray(res.data.data)) {
      advertisements.value = res.data.data;
      console.log("res.data.data",res.data.data);
      console.log(advertisements.value[0]);
    } else {
      console.error('获取数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取广告列表失败:', error);
  }
}

// 导航到商品详情
function goToProductDetail(productId: number) {
  router.push({ path: `/home/product/${productId}` });
}
// 导航到创建广告页面
function goToCreateAdvertisement() {
  router.push({ path: '/home/create-advertisement' }); // 确保路由路径正确
}

async function handleCancel() {
  showEditDialog.value = false;
  updateId.value = 0;
  updateTitle.value = '';
  updateContent.value = '';
  updateImgUrl.value = '';
  updateProductId.value = '';
  dialogVisible.value = false;
  dialogImageUrl.value = '';
  fileList.value = [];
}

async function updateAd() {
  try {
    console.log("Updating ad:", updateId.value);

    const response = await updateAdvertisement({
      id: updateId.value.toString(),
      title: updateTitle.value,
      content: updateContent.value,
      imgUrl: updateImgUrl.value,
      productId: updateProductId.value,
    });
    if (response.data.code === '200') {
      ElMessage.success('广告更新成功');
      console.log(response.data);

      handleCancel();
      await get_getAlladvertisements();
    }
  } catch (error) {
    console.error('广告更新失败:', error);
  }
}

async function deleteAd(advertisementId: number) {
  try {
    const response = await deleteAdvertisement(advertisementId);
    if (response.data.code === '200') {
      ElMessage.success('广告删除成功');
      console.log(response.data);

      await get_getAlladvertisements();
    }
  } catch (error) {
    console.error('广告删除失败:', error);
  }
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
      updateImgUrl.value = res.data;
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

const openEditDialog = (advertisementId: number) => {
  console.log("Editing:", advertisementId);

  updateId.value = advertisementId;
  showEditDialog.value = true;
  updateTitle.value = '';
  updateContent.value = '';
  updateImgUrl.value = '';
  updateProductId.value = '';
  dialogVisible.value = false;
  dialogImageUrl.value = '';
  fileList.value = [];
}

// 获取所有商品数据
async function getAllProductsData() {
  try {
    const res = await getAllProduct();
    if (res.data && Array.isArray(res.data.data)) {
      products.value = res.data.data;
    } else {
      console.error('获取商品数据失败：响应格式不符合预期');
    }
  } catch (error) {
    console.error('获取商品列表失败:', error);
  }
}

// 在组件挂载时获取商店数据
onMounted(() => {
  get_getAlladvertisements()
  getAllProductsData();
});
</script>

<style scoped>

.advertisement-list {
  min-height: 800px;
  padding: 20px;
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url("../../assets/780.jpg");
  background-size: cover;
  background-position: center;

  display: flex;
  flex-direction: column;
}

.create-button-container {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  justify-content: center;
  flex-grow: 1;
}

.advertisement-card {
  background: rgba(255, 255, 255, 0.85);
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  padding: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.advertisement-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
}

.advertisement-image img {
  width: 100%;
  max-width: 260px;
  height: 150px;
  border-radius: 10px;
  object-fit: cover;
  cursor: pointer;
  margin-bottom: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.advertisement-card h3 {
  font-size: 1.25rem;
  margin: 10px 0 6px 0;
  color: #333;
  text-align: center;
  font-weight: 600;
}

.advertisement-content {
  font-size: 0.9rem;
  color: #555;
  text-align: center;
  margin-bottom: 10px;
  min-height: 40px; /* 保持统一高度 */
}

.advertisement-card p:last-child {
  font-size: 0.85rem;
  color: #777;
  margin-bottom: 15px;
}

.manager-actions {
  width: 100%;
  display: flex;
  justify-content: center;
}

.management-buttons {
  display: flex;
  gap: 15px;
}

.management-buttons .el-button {
  flex: 1;
  font-size: 0.9rem;
  padding: 6px 12px;
}
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: 1500px auto; /* 或根据需求调整为 cover 或百分比 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 关键属性：背景固定 */
  background-color: #7b6b4d; /* 深羊驼色兜底 */
  min-height: 100vh;
}
</style>