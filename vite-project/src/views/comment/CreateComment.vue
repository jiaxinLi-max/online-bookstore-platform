

<!--<template>-->
<!--  <div class="create_comment bgimage">-->
<!--    <h1 class="create-comment-title">创建评价</h1>-->
<!--    <el-form ref="form" label-width="120px" class="product-form">-->
<!--      &lt;!&ndash; 基本信息 &ndash;&gt;-->
<!--      <el-form-item label="评价内容" prop="commentContent" >-->
<!--        <el-input v-model="content" placeholder="请输入评价内容" ></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="评分" prop="score">-->
<!--        <el-rate-->
<!--            v-model="score"-->
<!--            :allow-half="true"-->
<!--            show-text-->
<!--            :texts="['极差', '失望', '一般', '满意', '惊喜']"-->
<!--        />-->
<!--      </el-form-item>-->



<!--      <el-form-item>-->
<!--        <el-button-->
<!--            @click.prevent="handleCreateComment"-->
<!--            :disabled="createDisabled"-->
<!--            type="primary"-->
<!--            plain-->
<!--            class="custom-black-button"-->
<!--        >-->
<!--          创建评价-->
<!--        </el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, computed,onMounted } from 'vue';-->
<!--import { ElMessage } from 'element-plus';-->
<!--import type { UploadFile } from 'element-plus';-->
<!--import { Plus } from '@element-plus/icons-vue';-->
<!--import { getImage } from '../../api/tools';-->
<!--import {useRoute, useRouter} from "vue-router";-->
<!--import {createComment} from "../../api/comment.ts";-->

<!--const content = ref('');-->
<!--const score = ref<number>(); // 修改为对应的类型-->
<!--console.log("CreateComment 页面加载了");-->
<!--// 计算属性，检查输入状态-->
<!--const createDisabled = computed(() => {-->
<!--  return !content.value || !score.value;-->
<!--});-->



<!--async function handleCreateComment() {-->
<!--  const token = sessionStorage.getItem('token');-->
<!--  if (!token) {-->
<!--    ElMessage.error('请先登录!');-->
<!--    return;-->
<!--  }-->

<!--  const route = useRoute();-->
<!--  const productId=Number(route.params.productId);-->


<!--  const payload = {-->
<!--    userId: Number(sessionStorage.getItem('userId')),-->
<!--    content: content.value,-->
<!--    score: score.value,-->
<!--    productId:productId,-->
<!--  };-->

<!--  try {-->
<!--    const res = await createComment(payload);-->
<!--    if (res.data.code === '200') {-->
<!--      ElMessage.success('创建评价成功');-->
<!--      // 重置输入框-->
<!--      content.value = '';-->
<!--      score.value = 0;-->
<!--    } else {-->
<!--      ElMessage.error(res.data.message);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.log("error",error);-->
<!--    ElMessage.error('创建评价失败');-->
<!--  }-->
<!--}-->

<!--</script>-->

<!--<style scoped>-->
<!--.create-comment-title {-->
<!--  margin-left: 25%;-->
<!--}-->

<!--.dialog-image {-->
<!--  max-width: 100%;-->
<!--}-->

<!--.custom-black-button {-->
<!--  background-color: #000000 !important; /* 设置按钮背景为黑色 */-->
<!--  border-color: #000000 !important; /* 设置按钮边框为黑色 */-->
<!--  color: white !important; /* 设置按钮文字为白色 */-->
<!--}-->

<!--.custom-black-button:hover {-->
<!--  background-color: #333333 !important; /* 鼠标悬停时的背景颜色 */-->
<!--  border-color: #333333 !important; /* 鼠标悬停时的边框颜色 */-->
<!--  color: white !important; /* 鼠标悬停时的文字颜色 */-->
<!--}-->
<!--.bgimage {-->
<!--  background-color: rgba(0, 0, 0, 0.3);-->
<!--  background-image: url("../../assets/kenan.png");-->

<!--  background-size: cover;-->
<!--  background-repeat: no-repeat;-->
<!--  background-position: center;-->
<!--  min-height: 100vh;-->

<!--}-->
<!--.create-product-title {-->
<!--  margin-left: 25%;-->
<!--  color: white; /* ← 加上这一句 */-->
<!--}-->
<!--::v-deep(.el-form-item__label) {-->
<!--  color: white;-->
<!--}-->
<!--</style>-->
<template>
  <div class="create_comment bgimage">
    <h1 class="create-comment-title">创建评价</h1>
    <el-form ref="form" label-width="120px" class="product-form">
      <!-- 评价内容 -->
      <el-form-item label="评价内容" prop="commentContent">
        <el-input v-model="content" placeholder="请输入评价内容" />
      </el-form-item>

      <!-- 评分 -->
      <el-form-item label="评分" prop="score">
        <el-rate
            v-model="score"
            :allow-half="true"
            show-text
            :texts="['极差', '失望', '一般', '满意', '惊喜']"
        />
      </el-form-item>


      <!-- 提交按钮 -->
      <el-form-item>
        <el-button
            @click.prevent="handleCreateComment"
            :disabled="createDisabled"
            type="primary"
            plain
            class="custom-black-button"
        >
          创建评价
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { createComment } from '../../api/comment'

// 顶层使用 useRoute 获取参数
const route = useRoute()
const productId = route.params.productId
console.log("productId", productId)
// 评价内容和评分
const content = ref('')
const score = ref<number>(0)

// 按钮禁用逻辑
const createDisabled = computed(() => !content.value || !score.value)

// 创建评价事件
async function handleCreateComment() {
  const token = sessionStorage.getItem('token')
  if (!token) {
    ElMessage.error('请先登录!')
    return
  }

  const payload = {
    userId: Number(sessionStorage.getItem('userId')),
    content: content.value,
    score: score.value,
    productId: productId
  }

  try {
    const res = await createComment(payload)
    if (res.data.code === '200') {

      ElMessage.success('创建评价成功')
      content.value = ''
      score.value = 0
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    console.error('error', error)
    ElMessage.error('创建评价失败')
  }
}
</script>

<style scoped>
.create-comment-title {
  margin-left: 25%;
}

.dialog-image {
  max-width: 100%;
}

.custom-black-button {
  background-color: #000000 !important;
  border-color: #000000 !important;
  color: white !important;
}

.custom-black-button:hover {
  background-color: #333333 !important;
  border-color: #333333 !important;
  color: white !important;
}

.bgimage {
  background-color: rgba(0, 0, 0, 0.3);
  background-image: url('../../assets/kenan.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  min-height: 100vh;
}

.create-product-title {
  margin-left: 25%;
  color: white;
}

::v-deep(.el-form-item__label) {
  color: white;
}
</style>
