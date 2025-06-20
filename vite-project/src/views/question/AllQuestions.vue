<template>
  <div class="daily-question-container">
    <el-card class="question-card">
      <div class="header">
        <h2>每日一问</h2>
        <p class="date">{{ currentDate }}</p>
      </div>

      <div v-if="question" class="question-content">
        <h3 class="question-title">{{ question.title }}</h3>
        <p class="question-body">{{ question.content }}</p>
      </div>
      <div v-else class="no-question">今日还未发布问题。</div>

      <div v-if="role === 'MANAGER'" class="admin-actions">
        <el-button type="primary" @click="goToCreate">发布新问题</el-button>
        <el-button type="success" @click="goToEdit" :disabled="!question">编辑</el-button>
        <el-button type="danger" @click="handleDelete" :disabled="!question">删除</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getTodayQuestion, deleteQuestion } from '../../api/question'; // 你需要定义对应接口
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const role = sessionStorage.getItem('role');

const question = ref<{ id: number, title: string, content: string } | null>(null);
const currentDate = new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric'
});

const fetchQuestion = async () => {
  try {
    const res = await getTodayQuestion();
    if (res.data.code === '200') {
      question.value = res.data.data;
    } else {
      question.value = null;
    }
  } catch (error) {
    ElMessage.error('获取问题失败');
  }
};

const goToCreate = () => {
  router.push({ name: 'CreateQuestion' });
};

const goToEdit = () => {
  if (question.value) {
    router.push({ name: 'EditQuestion', params: { id: question.value.id } });
  }
};

const handleDelete = async () => {
  if (!question.value) return;
  try {
    await ElMessageBox.confirm('确定要删除今天的问题吗？', '提示', {
      type: 'warning'
    });
    const res = await deleteQuestion(question.value.id);
    if (res.data.code === '200') {
      ElMessage.success('删除成功');
      question.value = null;
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (err) {
    // 用户取消删除
  }
};

onMounted(fetchQuestion);
</script>

<style scoped>
.daily-question-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fdfdfd;
}

.question-card {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.question-title {
  font-size: 1.5em;
  font-weight: bold;
  color: #333;
}

.question-body {
  font-size: 1.2em;
  margin-top: 10px;
  color: #555;
}

.no-question {
  font-style: italic;
  color: #888;
  margin: 20px 0;
}

.admin-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}
</style>
