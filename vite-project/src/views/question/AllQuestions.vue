<!--<template>-->
<!--  <div class="max-w-4xl mx-auto mt-8 p-6 bg-white rounded-xl shadow bgimage">-->
<!--    -->

<!--    <el-table :data="questions" border style="width: 100%">-->
<!--      <el-table-column prop="id" label="ID" width="60" />-->
<!--      <el-table-column prop="content" label="问题内容" />-->
<!--      <el-table-column-->
<!--          prop="createTime"-->
<!--          label="创建时间"-->
<!--          width="180"-->
<!--          :formatter="formatDate"-->
<!--      />-->
<!--      <el-table-column-->
<!--          prop="ddl"-->
<!--          label="截止时间"-->
<!--          width="180"-->
<!--          :formatter="formatDate"-->
<!--      />-->
<!--      <el-table-column label="状态" width="100">-->
<!--        <template #default="{ row }">-->
<!--          <el-tag :type="isExpired(row.ddl) ? 'danger' : 'success'">-->
<!--            {{ isExpired(row.ddl) ? '已截止' : '进行中' }}-->
<!--          </el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      &lt;!&ndash; 新增“回答状态”列 &ndash;&gt;-->
<!--      <el-table-column label="回答状态" width="100">-->
<!--        <template #default="{ row }">-->
<!--          <el-tag :type="row.answered ? 'success' : 'info'">-->
<!--            {{ row.answered ? '已回答' : '未回答' }}-->
<!--          </el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column label="操作" width="120">-->
<!--        <template #default="{ row }">-->
<!--          <el-button type="primary" size="small" @click="goToDetail(row.id)">-->
<!--            查看详情-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { ref, onMounted } from 'vue'-->
<!--import { ElMessage } from 'element-plus'-->
<!--import { useRouter } from 'vue-router'-->
<!--import { getAllQuestions, getAnsweredQuestions } from '../../api/question'-->

<!--interface Question {-->
<!--  id: number-->
<!--  content: string-->
<!--  createTime: string-->
<!--  ddl: string-->
<!--  answered?: boolean // 新增字段，表示是否已回答-->
<!--}-->

<!--const questions = ref<Question[]>([])-->
<!--const router = useRouter()-->

<!--const userId = Number(sessionStorage.getItem('userId'))-->
<!--import dayjs from 'dayjs'-->

<!--const formatDate = (row: Question, column: any, cellValue: string) => {-->
<!--  return dayjs(cellValue).format('YYYY年MM月DD日')-->
<!--}-->
<!--onMounted(async () => {-->
<!--  if (!userId) {-->
<!--    ElMessage.error('请先登录')-->
<!--    return-->
<!--  }-->
<!--  try {-->
<!--    // 同时请求所有问题和已回答问题-->
<!--    const [allRes, answeredRes] = await Promise.all([-->
<!--      getAllQuestions(),-->
<!--      getAnsweredQuestions(userId)-->
<!--    ])-->

<!--    if (allRes.data.code !== '200') {-->
<!--      ElMessage.error(allRes.data.msg || '获取所有问题失败')-->
<!--      return-->
<!--    }-->
<!--    if (answeredRes.data.code !== '200') {-->
<!--      ElMessage.error(answeredRes.data.msg || '获取已回答问题失败')-->
<!--      return-->
<!--    }-->

<!--    const allQuestions: Question[] = allRes.data.data-->
<!--    const answeredQuestions: Question[] = answeredRes.data.data-->

<!--    // 取出已回答的问题ID集合，方便判断-->
<!--    const answeredIds = new Set(answeredQuestions.map(q => q.id))-->

<!--    // 标记每个问题是否已回答-->
<!--    questions.value = allQuestions.map(q => ({-->
<!--      ...q,-->
<!--      answered: answeredIds.has(q.id)-->
<!--    }))-->

<!--  } catch (e) {-->
<!--    ElMessage.error('请求失败')-->
<!--    console.error(e)-->
<!--  }-->
<!--})-->

<!--const isExpired = (ddl: string): boolean => {-->
<!--  return new Date(ddl).getTime() < new Date().getTime()-->
<!--}-->

<!--const goToDetail = (questionId: number) => {-->
<!--  router.push({ name: 'QuestionDetail', params: { id: questionId } })-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--.bgimage {-->
<!--  min-height: 100vh;-->
<!--  background-image: url("../../assets/780.jpg");-->
<!--  background-size: cover;-->
<!--  background-position: center top;-->
<!--  background-repeat: no-repeat;-->
<!--  padding: 20px; /* 让内容不会贴边 */-->
<!--}-->
<!--</style>-->
<template>
  <div class="max-w-4xl mx-auto mt-8 p-6 bg-white rounded-xl shadow bgimage">
    <!-- 当前可参与 -->
    <h2 class="section-title">🟡 当前可参与的问题</h2>
    <el-table :data="availableQuestions" border style="width: 100%; margin-bottom: 40px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="content" label="问题内容" />
      <el-table-column prop="createTime" label="创建时间" width="180" :formatter="formatDate" />
      <el-table-column prop="ddl" label="截止时间" width="180" :formatter="formatDate" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="isExpired(row.ddl) ? 'danger' : 'success'">
            {{ isExpired(row.ddl) ? '已截止' : '进行中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="回答状态" width="100">
        <template #default="{ row }">
          <el-tag type="info">未回答</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="goToDetail(row.id)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 已参与 -->
    <h2 class="section-title">✅ 您参与过的问题</h2>
    <el-table :data="participatedQuestions" border style="width: 100%; margin-bottom: 40px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="content" label="问题内容" />
      <el-table-column prop="createTime" label="创建时间" width="180" :formatter="formatDate" />
      <el-table-column prop="ddl" label="截止时间" width="180" :formatter="formatDate" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="isExpired(row.ddl) ? 'danger' : 'success'">
            {{ isExpired(row.ddl) ? '已截止' : '进行中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="回答状态" width="100">
        <template #default="{ row }">
          <el-tag type="success">已回答</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="goToDetail(row.id)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 已截止 -->
    <h2 class="section-title">⏰ 已截止的问题</h2>
    <el-table :data="expiredQuestions" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="content" label="问题内容" />
      <el-table-column prop="createTime" label="创建时间" width="180" :formatter="formatDate" />
      <el-table-column prop="ddl" label="截止时间" width="180" :formatter="formatDate" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag type="danger">已截止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="回答状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.answered ? 'success' : 'info'">
            {{ row.answered ? '已回答' : '未回答' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="goToDetail(row.id)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getAllQuestions, getAnsweredQuestions } from '../../api/question'
import dayjs from 'dayjs'

interface Question {
  id: number
  content: string
  createTime: string
  ddl: string
  answered?: boolean
}

const questions = ref<Question[]>([])
const availableQuestions = ref<Question[]>([])
const participatedQuestions = ref<Question[]>([])
const expiredQuestions = ref<Question[]>([])

const router = useRouter()
const userId = Number(sessionStorage.getItem('userId'))

const formatDate = (row: Question, column: any, cellValue: string) => {
  return dayjs(cellValue).format('YYYY年MM月DD日')
}

const isExpired = (ddl: string): boolean => {
  return new Date(ddl).getTime() < new Date().getTime()
}

const goToDetail = (questionId: number) => {
  router.push({ name: 'QuestionDetail', params: { id: questionId } })
}

onMounted(async () => {
  if (!userId) {
    ElMessage.error('请先登录')
    return
  }

  try {
    const [allRes, answeredRes] = await Promise.all([
      getAllQuestions(),
      getAnsweredQuestions(userId)
    ])

    if (allRes.data.code !== '200') {
      ElMessage.error(allRes.data.msg || '获取所有问题失败')
      return
    }

    if (answeredRes.data.code !== '200') {
      ElMessage.error(answeredRes.data.msg || '获取已回答问题失败')
      return
    }

    const allQuestions: Question[] = allRes.data.data
    const answeredQuestions: Question[] = answeredRes.data.data
    const answeredIds = new Set(answeredQuestions.map(q => q.id))

    questions.value = allQuestions.map(q => ({
      ...q,
      answered: answeredIds.has(q.id)
    }))

    const now = new Date().getTime()

    availableQuestions.value = questions.value.filter(
        q => !q.answered && new Date(q.ddl).getTime() >= now
    )

    participatedQuestions.value = questions.value.filter(
        q => q.answered
    )

    expiredQuestions.value = questions.value.filter(
        q => new Date(q.ddl).getTime() < now
    )
  } catch (e) {
    ElMessage.error('请求失败')
    console.error(e)
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
  padding: 20px;
}
.section-title {
  color: white;
}
</style>
