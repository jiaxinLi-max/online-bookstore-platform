<template>
  <div class="question-detail-container bgimage">
    <el-button type="primary" @click="backToList" class="back-button">
      <el-icon><ArrowLeft /></el-icon>
      返回问题列表
    </el-button>

    <el-card class="detail-card" v-if="question">
      <h2 class="question-title">{{ question.content }}</h2>

      <el-radio-group
          v-model="selectedAnswer"
          class="options-list"
          v-if="!isExpired"
          :disabled="submitted"
      >

        <el-radio
            v-for="(opt, index) in question.options"
            :key="index"
            :label="optionLabel(index)"
        >
        {{ optionLabel(index) }}. {{ opt }}
        </el-radio>
      </el-radio-group>

      <ul v-else class="options-list">
        <li v-for="(opt, index) in question.options" :key="index">{{ opt }}</li>
      </ul>

      <p class="question-time">发布时间：{{ formatDate(question.createTime) }}</p>
      <p class="question-time">截止时间：{{ formatDate(question.ddl) }}</p>

      <!-- 提交按钮 -->
      <el-button
          v-if="!isExpired"
          type="success"
          @click="submitAnswer"
          :disabled="!selectedAnswer || submitted"
          class="submit-btn"
      >
        提交答案
      </el-button>
      <div v-if="submitted && !isExpired" class="after-submit" style="margin-top: 20px;">
        <p><strong>正确答案：</strong> {{ question.answer }}</p>
        <p><strong>解析：</strong> {{ question.analysis || '暂无解析' }}</p>
        <p><strong>您的答案：</strong> {{ selectedAnswer }}</p>
      </div>

      <div v-if="isExpired" class="expired-info" style="margin-top: 20px;">
        <p><strong>正确答案：</strong> {{ question.answer }}</p>
        <p><strong>解析：</strong> {{ question.analysis || '暂无解析' }}</p>
        <p><strong>您的答案：</strong> {{ selectedAnswer || '未作答' }}</p>
      </div>

      <!-- 已过期显示解析 -->
      <div v-if="isExpired" class="expired-info">
        <p><strong>正确答案：</strong> {{ question.answer }}</p>
        <p><strong>解析：</strong> {{ question.analysis || '暂无解析' }}</p>
      </div>
    </el-card>

    <div v-else-if="notFound" class="not-found">
      <el-empty description="未找到该问题，可能已被删除" />
    </div>

    <div v-else class="loading-container">
      <el-icon class="loading-icon"><Loading /></el-icon>
      正在加载问题详情...
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getQuestionDetail, submitAnswera, getUserAnswera } from '../../api/question'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Loading } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { addCredit, updateLevel } from '../../api/user.ts'

const route = useRoute()
const router = useRouter()
const questionId = Number(route.params.id)
const userId = Number(sessionStorage.getItem('userId'))
const userId1 = sessionStorage.getItem("userId");
const question = ref<any | null>(null)
const notFound = ref(false)
const submitted = ref(false)
const selectedAnswer = ref<string>('')

const optionLabel = (index: number) => String.fromCharCode(65 + index) // A, B, C, ...

const formatDate = (dateStr: string) => {
  return dayjs(dateStr).format('YYYY年MM月DD日 HH:mm:ss')
}

const isExpired = computed(() => {
  if (!question.value) return false
  return dayjs().isAfter(dayjs(question.value.ddl))
})

const backToList = () => {
  router.push({ name: 'AllQuestions' })
}

const submitAnswer = async () => {
  try {
    const res = await submitAnswera({
      questionId,
      userId,
      answer: selectedAnswer.value
    })
    if (res.data.code === '200') {
      ElMessage.success('提交成功！')
      submitted.value = true
      console.log("quesanswer", question.value.answer)
      if (selectedAnswer.value === question.value.answer) {
        ElMessage.success('回答正确，已为您加分')
        // 加分（例如 +2 分）
        await addCredit(Number(userId1), 5);
        await updateLevel(Number(userId1));
      } else {
        ElMessage.warning('回答错误，本题不加分')
      }
    } else {
      ElMessage.warning(res.data.msg || '提交失败')
    }
  } catch (err) {
    ElMessage.error('提交失败，请稍后再试')
  }
}

const loadUserAnswer = async () => {
  try {
    const res = await getUserAnswera(questionId,userId )
    console.log("myanswer", res.data)

    if (res.data.code === "200" && res.data.data) {
      selectedAnswer.value = res.data.data  // 直接填入已答答案
      submitted.value = true
    }
  } catch (err) {
    // 用户未作答，不处理
  }
}

onMounted(async () => {
  try {
    const res = await getQuestionDetail(questionId)
    if (res.data.code === "200") {
      question.value = res.data.data
      await loadUserAnswer()  // 加载用户答案
    } else {
      notFound.value = true
    }
  } catch (err) {
    ElMessage.error('加载问题失败')
    notFound.value = true
  }
})
</script>
<style scoped>
.bgimage {
  min-height: 100vh;
  background-image: url("../../assets/780.jpg");
  background-size: cover;
  background-position: center top;
  background-repeat: no-repeat;
  padding: 20px; /* 让内容不会贴边 */
}
</style>