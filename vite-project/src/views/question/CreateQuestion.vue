<template>
  <div class="create-question-page bgimage">
    <h1 class="page-title">创建问题</h1>

    <el-form label-width="100px" class="question-form">
      <el-form-item label="问题标题">
        <el-input v-model="title" placeholder="请输入问题标题" />
      </el-form-item>

      <el-form-item label="问题内容">
        <el-input
            type="textarea"
            v-model="content"
            placeholder="请输入详细描述"
            :rows="6"
        />
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            class="btn-camel"
            :disabled="!title || !content"
            @click="submitQuestion"
        >
          提交问题
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { createQuestion } from '../../api/question';

const router = useRouter();
const title = ref('');
const content = ref('');

async function submitQuestion() {
  try {
    const res = await createQuestion({ title: title.value, content: content.value });
    if (res.data.code === '200') {
      ElMessage.success('创建成功');
      router.push('/'); // 可改成跳转到问题列表页
    } else {
      ElMessage.error(res.data.message || '创建失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('请求出错');
  }
}
</script>

<style scoped>
.bgimage {
  background-image: url('../../assets/780.jpg');
  background-position: center top;
  background-size: cover;
  background-repeat: no-repeat;
  min-height: 100vh;
  padding: 40px;
}

.page-title {
  color: white;
  margin-bottom: 20px;
  font-size: 2em;
}

.question-form {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  margin: 0 auto;
}

.btn-camel {
  background-color: #7b6b4d !important;
  color: #ffffff !important;
  border-color: #7b6b4d !important;
}

.btn-camel:hover {
  background-color: #5f543d !important;
}
</style>
