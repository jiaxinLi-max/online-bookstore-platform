<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="create-question-page bgimage">&ndash;&gt;-->
<!--&lt;!&ndash;    <h1 class="page-title">创建问题</h1>&ndash;&gt;-->

<!--&lt;!&ndash;    <el-form label-width="100px" class="question-form">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="问题标题">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="title" placeholder="请输入问题标题" />&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item label="问题内容">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input&ndash;&gt;-->
<!--&lt;!&ndash;            type="textarea"&ndash;&gt;-->
<!--&lt;!&ndash;            v-model="content"&ndash;&gt;-->
<!--&lt;!&ndash;            placeholder="请输入详细描述"&ndash;&gt;-->
<!--&lt;!&ndash;            :rows="6"&ndash;&gt;-->
<!--&lt;!&ndash;        />&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button&ndash;&gt;-->
<!--&lt;!&ndash;            type="primary"&ndash;&gt;-->
<!--&lt;!&ndash;            class="btn-camel"&ndash;&gt;-->
<!--&lt;!&ndash;            :disabled="!title || !content"&ndash;&gt;-->
<!--&lt;!&ndash;            @click="submitQuestion"&ndash;&gt;-->
<!--&lt;!&ndash;        >&ndash;&gt;-->
<!--&lt;!&ndash;          提交问题&ndash;&gt;-->
<!--&lt;!&ndash;        </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-form>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script lang="ts" setup>&ndash;&gt;-->
<!--&lt;!&ndash;import { ref } from 'vue';&ndash;&gt;-->
<!--&lt;!&ndash;import { ElMessage } from 'element-plus';&ndash;&gt;-->
<!--&lt;!&ndash;import { useRouter } from 'vue-router';&ndash;&gt;-->
<!--&lt;!&ndash;import { createQuestion } from '../../api/question';&ndash;&gt;-->

<!--&lt;!&ndash;const router = useRouter();&ndash;&gt;-->
<!--&lt;!&ndash;const title = ref('');&ndash;&gt;-->
<!--&lt;!&ndash;const content = ref('');&ndash;&gt;-->

<!--&lt;!&ndash;async function submitQuestion() {&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await createQuestion({ title: title.value, content: content.value });&ndash;&gt;-->
<!--&lt;!&ndash;    if (res.data.code === '200') {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.success('创建成功');&ndash;&gt;-->
<!--&lt;!&ndash;      router.push('/'); // 可改成跳转到问题列表页&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error(res.data.message || '创建失败');&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (error) {&ndash;&gt;-->
<!--&lt;!&ndash;    console.error(error);&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('请求出错');&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.bgimage {&ndash;&gt;-->
<!--&lt;!&ndash;  background-image: url('../../assets/780.jpg');&ndash;&gt;-->
<!--&lt;!&ndash;  background-position: center top;&ndash;&gt;-->
<!--&lt;!&ndash;  background-size: cover;&ndash;&gt;-->
<!--&lt;!&ndash;  background-repeat: no-repeat;&ndash;&gt;-->
<!--&lt;!&ndash;  min-height: 100vh;&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 40px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.page-title {&ndash;&gt;-->
<!--&lt;!&ndash;  color: white;&ndash;&gt;-->
<!--&lt;!&ndash;  margin-bottom: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 2em;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.question-form {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: rgba(255, 255, 255, 0.8);&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 8px;&ndash;&gt;-->
<!--&lt;!&ndash;  max-width: 600px;&ndash;&gt;-->
<!--&lt;!&ndash;  margin: 0 auto;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.btn-camel {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #7b6b4d !important;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #ffffff !important;&ndash;&gt;-->
<!--&lt;!&ndash;  border-color: #7b6b4d !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.btn-camel:hover {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #5f543d !important;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<template>-->
<!--  <div class="max-w-2xl mx-auto mt-8 p-6 bg-white rounded-xl shadow bgimage">-->
<!--    <h2 class="text-xl font-semibold mb-4">创建新问题</h2>-->

<!--    <el-form :model="form" label-width="100px" label-position="left">-->
<!--      <el-form-item label="问题内容" required>-->
<!--        <el-input type="textarea" v-model="form.content" placeholder="请输入问题内容" />-->
<!--      </el-form-item>-->

<!--      <el-form-item label="选项列表" required>-->
<!--        <div v-for="(option, index) in form.options" :key="index" class="mb-2 flex items-center gap-2">-->
<!--          <el-input v-model="form.options[index]" placeholder="选项内容" class="flex-1" />-->
<!--          <el-button type="danger" icon="el-icon-minus" @click="removeOption(index)" />-->
<!--        </div>-->
<!--        <el-button type="primary" icon="el-icon-plus" @click="addOption">添加选项</el-button>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="正确答案" required>-->
<!--        <el-select v-model="form.answer" placeholder="请选择正确选项">-->
<!--          <el-option-->
<!--              v-for="(option, index) in form.options"-->
<!--              :key="index"-->
<!--              :label="`选项 ${optionLabel(index)}: ${option}`"-->
<!--              :value="optionLabel(index)"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="截止时间" required>-->
<!--        <el-date-picker-->
<!--            v-model="form.ddl"-->
<!--            type="datetime"-->
<!--            placeholder="选择截止时间"-->
<!--            format="YYYY-MM-DD HH:mm:ss"-->
<!--            value-format="YYYY-MM-DDTHH:mm:ss"-->
<!--        />-->
<!--      </el-form-item>-->

<!--      <el-form-item label="解析">-->
<!--        <el-input type="textarea" v-model="form.analysis" placeholder="可选，填写问题解析" />-->
<!--      </el-form-item>-->

<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="submit">提交问题</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { ref } from 'vue'-->
<!--import { ElMessage } from 'element-plus'-->
<!--import { createQuestion } from '../../api/question'-->

<!--const form = ref({-->
<!--  content: '',-->
<!--  options: [''],-->
<!--  answer: '',-->
<!--  ddl: '',-->
<!--  analysis: ''-->
<!--})-->

<!--// A/B/C/... 用于答案映射-->
<!--const optionLabel = (index: number) => String.fromCharCode(65 + index)-->

<!--const addOption = () => {-->
<!--  form.value.options.push('')-->
<!--}-->

<!--const removeOption = (index: number) => {-->
<!--  if (form.value.options.length <= 1) {-->
<!--    ElMessage.warning('至少保留一个选项')-->
<!--    return-->
<!--  }-->
<!--  form.value.options.splice(index, 1)-->
<!--}-->

<!--// 提交问题-->
<!--const submit = async () => {-->
<!--  if (!form.value.content || form.value.options.some(opt => !opt) || !form.value.answer || !form.value.ddl) {-->
<!--    ElMessage.error('请填写完整信息')-->
<!--    return-->
<!--  }-->

<!--  try {-->
<!--    const res = await createQuestion({-->
<!--      content: form.value.content,-->
<!--      options: form.value.options,-->
<!--      answer: form.value.answer,-->
<!--      ddl: form.value.ddl,-->
<!--      analysis: form.value.analysis-->
<!--    })-->
<!--    if (res.data.code === "200") {-->
<!--      ElMessage.success('创建成功')-->
<!--      // 清空表单-->
<!--      form.value = {-->
<!--        content: '',-->
<!--        options: [''],-->
<!--        answer: '',-->
<!--        ddl: '',-->
<!--        analysis: ''-->
<!--      }-->
<!--    } else {-->
<!--      ElMessage.error(res.data.msg || '创建失败')-->
<!--    }-->
<!--  } catch (e) {-->
<!--    ElMessage.error('提交失败')-->
<!--  }-->
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
  <div class="max-w-2xl mx-auto mt-8 p-6 bg-white rounded-xl shadow bgimage white-text">
    <h2 class="text-xl font-semibold mb-4">创建新问题</h2>

    <el-form :model="form" label-width="100px" label-position="left">
      <el-form-item label="问题内容" required>
        <el-input type="textarea" v-model="form.content" placeholder="请输入问题内容" />
      </el-form-item>

      <el-form-item label="选项列表" required>
        <div v-for="(option, index) in form.options" :key="index" class="mb-2 flex items-center gap-2">
          <el-input v-model="form.options[index]" placeholder="选项内容" class="flex-1" />
          <el-button type="danger" @click="removeOption(index)">
            <i class="el-icon-minus" style="margin-right: 4px"></i>
            删除
          </el-button>
        </div>
        <el-button type="primary" icon="el-icon-plus" @click="addOption">添加选项</el-button>
      </el-form-item>

      <el-form-item label="正确答案" required>
        <el-select v-model="form.answer" placeholder="请选择正确选项">
          <el-option
              v-for="(option, index) in form.options"
              :key="index"
              :label="`选项 ${optionLabel(index)}: ${option}`"
              :value="optionLabel(index)"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="截止时间" required>
        <el-date-picker
            v-model="form.ddl"
            type="datetime"
            placeholder="选择截止时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="解析">
        <el-input type="textarea" v-model="form.analysis" placeholder="可选，填写问题解析" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submit">提交问题</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createQuestion } from '../../api/question'

const form = ref({
  content: '',
  options: [''],
  answer: '',
  ddl: '',
  analysis: ''
})

const optionLabel = (index: number) => String.fromCharCode(65 + index)

const addOption = () => {
  form.value.options.push('')
}

const removeOption = (index: number) => {
  if (form.value.options.length <= 1) {
    ElMessage.warning('至少保留一个选项')
    return
  }
  form.value.options.splice(index, 1)
}

const submit = async () => {
  if (!form.value.content || form.value.options.some(opt => !opt) || !form.value.answer || !form.value.ddl) {
    ElMessage.error('请填写完整信息')
    return
  }

  try {
    const res = await createQuestion({
      content: form.value.content,
      options: form.value.options,
      answer: form.value.answer,
      ddl: form.value.ddl,
      analysis: form.value.analysis
    })
    if (res.data.code === "200") {
      ElMessage.success('创建成功')
      form.value = {
        content: '',
        options: [''],
        answer: '',
        ddl: '',
        analysis: ''
      }
    } else {
      ElMessage.error(res.data.msg || '创建失败')
    }
  } catch (e) {
    ElMessage.error('提交失败')
  }
}
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

/* 白色主题整体覆盖 */
.white-text {
  color: white;
}

/* 标签文字 */
.white-text :deep(.el-form-item__label) {
  color: white;
}

/* 输入框、文本框、选择器、时间选择器的外壳透明 */
.white-text :deep(.el-input),
.white-text :deep(.el-textarea),
.white-text :deep(.el-select),
.white-text :deep(.el-date-editor) {
  background-color: transparent;
  border-color: white;
  color: white;
}

/* 输入内部区域 */
.white-text :deep(.el-input__inner),
.white-text :deep(.el-textarea__inner),
.white-text :deep(.el-select .el-input__inner) {

  border-color: white;
  color: black;
}

/* 按钮样式 */
.white-text :deep(.el-button) {
  background-color: transparent;
  border: 1px solid white;
  color: white;
}
</style>