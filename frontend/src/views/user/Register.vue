<template>
  <div class="login-wrapper">
    <!-- 左上角品牌 Logo + 英文副标题（与登录页保持一致） -->
    <div class="brand">
      <div class="brand-cn">真相书局</div>
      <div class="brand-en">TRUTH BOOKSTORE</div>
    </div>

    <!-- 注册卡片（风格与登录页一致） -->
    <div class="login-card register-card">
      <h2 class="title">创建一个新的账户</h2>

      <el-form label-position="top" :model="{}">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名">
              <el-input v-model="username" placeholder="请输入用户名" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="真实姓名">
              <el-input v-model="name" placeholder="请输入真实姓名" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="邮箱（可选）">
              <el-input v-model="email" placeholder="请输入邮箱" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="身份">
              <el-select v-model="identity" placeholder="请选择">
                <el-option value="CUSTOMER" label="顾客" />
                <el-option value="MANAGER" label="管理员" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="telLabel">
              <el-input
                  v-model="tel"
                  :class="{ 'error-warn-input': (hasTelInput && !telLegal) }"
                  placeholder="请输入手机号（可选）"
                  clearable
              />
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="identity !== 'MANAGER'">
            <el-form-item label="地址（可选）">
              <el-input v-model="location" placeholder="请输入地址" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="密码">
              <el-input v-model="password" placeholder="••••••••" show-password />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="confirmLabel">
              <el-input
                  v-model="confirmPassword"
                  placeholder="再次输入密码"
                  show-password
                  :class="{ 'error-warn-input': (hasConfirmPasswordInput && !isPasswordIdentical) }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="头像">
              <div v-if="fileList.length === 0">
                <el-upload
                    class="avatar-uploader"
                    action="http://localhost:8080/api/images"
                    list-type="picture-card"
                    :auto-upload="true"
                    :file-list="fileList"
                    :on-change="handleChange"
                    :on-remove="handleRemove"
                    :on-preview="handlePictureCardPreview"
                    :limit="1"
                    :on-exceed="handleExceed"
                >
                  <div class="upload-placeholder">
                    <div class="plus">+</div>
                    <div class="hint">点击上传头像</div>
                  </div>
                </el-upload>
              </div>

              <div v-else class="avatar-preview">
                <el-image
                    :src="fileList[0].url"
                    fit="cover"
                    class="avatar-image"
                    style="width: 150px; height: 150px;"
                />
                <div style="margin-top:8px;">
                  <el-button type="danger" size="small" @click="handleRemove(fileList[0])">
                    删除头像
                  </el-button>
                </div>
              </div>

              <el-dialog v-model="dialogVisible" width="50%">
                <img class="dialog-image" :src="dialogImageUrl" alt="头像预览" />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="button-group">
          <el-button
              type="primary"
              :disabled="registerDisabled || loading"
              @click.prevent="handleRegister"
          >
            <span v-if="loading">注册中...</span>
            <span v-else>创建账户</span>
          </el-button>

          <router-link to="/login">
            <el-button>去登录</el-button>
          </router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
/* --- 保留并复用你原来的注册逻辑 --- */
import { ref, computed } from 'vue'
import { router } from '../../router'
import { userRegister } from '../../api/user.ts'
import { ElMessage } from 'element-plus'
import { getImage } from '../../api/tools'
import type { UploadFile } from 'element-plus'

/* 状态变量（与你提供的保持一致） */
const identity = ref('')
const username = ref('')
const name = ref('')
const tel = ref('')
const location = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const avatar = ref('')
const fileList = ref<UploadFile[]>([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)

/* 验证逻辑 */
const hasUserNameInput = computed(() => username.value !== '')
const hasNameInput = computed(() => name.value !== '')
const hasTelInput = computed(() => tel.value !== '')
const hasIdentityChosen = computed(() => identity.value !== '')
const hasPasswordInput = computed(() => password.value !== '')
const hasConfirmPasswordInput = computed(() => confirmPassword.value !== '')
const hasAddressInput = computed(() => location.value !== '')
const hasImageFile = computed(() => !!avatar.value)
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(tel.value))
const isPasswordIdentical = computed(() => password.value === confirmPassword.value)
const registerDisabled = computed(() => {
  if (!hasIdentityChosen.value) return true
  // 对 CUSTOMER / MANAGER 同样要求用户名、姓名、密码和确认密码匹配
  return !(hasUserNameInput.value && hasNameInput.value && hasPasswordInput.value && hasConfirmPasswordInput.value && isPasswordIdentical.value)
})

/* 上传限制与处理 */
const MAX_SIZE = 1024 * 1024
const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/gif']

const handleExceed = () => {
  ElMessage.warning('最多只能上传一张头像')
}

async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
  const rawFile = (file.raw as File) || null
  if (!rawFile) {
    ElMessage.error('无法获取文件')
    return
  }
  if (!ALLOWED_TYPES.includes(rawFile.type)) {
    ElMessage.error('只允许上传 JPG/PNG/GIF 格式的文件')
    return
  }
  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）')
    return
  }

  try {
    const res = await getImage(rawFile)
    // 兼容你后端返回格式：你之前使用 res.data/res.code 等，下面按常见格式处理
    if (res && (res.code === '200' || res.data)) {
      // 尝试读取 res.data 或 res.data.result 等
      const url = res.data ? res.data : (res.result || '')
      avatar.value = url
      fileListNew[0].url = avatar.value
      fileList.value = fileListNew
      ElMessage.success('上传成功')
    } else if (res.data && res.data.code === '200') {
      avatar.value = res.data.data
      fileListNew[0].url = avatar.value
      fileList.value = fileListNew
      ElMessage.success('上传成功')
    } else {
      ElMessage.error('响应格式错误，请重试')
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
  }
}

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = (file.url as string) || ''
  dialogVisible.value = true
}

const handleRemove = (file: UploadFile) => {
  fileList.value = fileList.value.filter(item => item.uid !== file.uid)
  avatar.value = ''
}

/* 注册提交 */
const loading = ref(false)
async function handleRegister() {
  loading.value = true
  try {
    const res = await userRegister({
      username: username.value,
      role: identity.value,
      name: name.value,
      telephone: tel.value,
      password: password.value,
      location: location.value,
      avatar: avatar.value,
      email: email.value,
    })
    if (res.data && res.data.code === '200') {
      ElMessage.success('注册成功！请登录账号')
      router.push({ path: '/login' })
    } else {
      ElMessage.error(res.data?.msg || '注册失败')
    }
  } catch (error) {
    console.error('注册错误', error)
    ElMessage.error('注册失败，请稍后再试')
  } finally {
    loading.value = false
  }
}

/* 辅助文本（手机号/确认密码 label 动态显示） */
const telLabel = computed(() => {
  if (!hasTelInput.value) return '注册手机号（可选）'
  if (!telLegal.value) return '手机号不合法'
  return '注册手机号（可选）'
})
const confirmLabel = computed(() => {
  if (!hasConfirmPasswordInput.value) return '确认密码'
  if (!isPasswordIdentical.value) return '密码不一致'
  return '确认密码'
})
</script>

<style scoped>
/* 与登录页一致的整体布局与配色（深色侦探风 + 暗金点缀） */
.login-wrapper {
  width: 100%;
  height: 100vh;
  position: relative;
  background: linear-gradient(145deg, #1e2a27 0%, #0e0f0f 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  box-sizing: border-box;
}

/* 左上角品牌（与登录页一致） */
.brand {
  position: absolute;
  top: 40px;
  left: 60px;
  color: #e6d5a3;
  font-family: "Cinzel", "Times New Roman", serif;
  text-shadow: 0 2px 6px rgba(0,0,0,0.4);
  user-select: none;
}
.brand-cn { font-size: 40px; font-weight: 700; letter-spacing: 4px; }
.brand-en { margin-top: 4px; font-size: 14px; opacity: 0.8; letter-spacing: 3px; }

/* 卡片（与登录页保持一致） */
.login-card {
  width: 920px; /* 注册表单较宽 */
  max-width: calc(100% - 80px);
  padding: 36px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255,255,255,0.08);
  box-shadow: 0 10px 40px rgba(0,0,0,0.35);
  color: #f2efe9;
}

/* 标题 */
.title {
  text-align: left;
  color: #f0eee9;
  font-size: 22px;
  margin-bottom: 18px;
  font-weight: 600;
}

/* 输入框样式（和登录页输入框风格匹配）*/
.el-input .el-input__wrapper,
.el-select .el-input__wrapper {
  background: rgba(255,255,255,0.02);
  border: 1px solid rgba(255,255,255,0.10);
  color: #efece6;
}
.el-input .el-input__inner::placeholder { color: rgba(255,255,255,0.45); }
.el-input .el-input__wrapper:hover { border-color: rgba(198,166,103,0.7); }
.el-input .el-input__wrapper.is-focus { border-color: #c6a667; box-shadow: 0 0 6px rgba(198,166,103,0.12); }

/* 上传区域 */
.avatar-uploader .upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: rgba(255,255,255,0.65);
}
.avatar-uploader .plus {
  font-size: 28px;
  color: rgba(198,166,103,0.95);
}
.avatar-image {
  border-radius: 8px;
  border: 1px solid rgba(198,166,103,0.4);
}

/* 错误样式 */
.error-warn { color: #ff6b6b; }
.error-warn-input { --el-input-focus-border-color: #ff6b6b !important; border-color: #ff6b6b !important; }

/* 按钮组（右对齐，与登录页一致的暗金主色）*/
.button-group {
  margin-top: 18px;
  display: flex;
  gap: 16px;
  justify-content: flex-end;
}
.el-button--primary {
  background: #c6a667;
  border-color: #c6a667;
  color: #0f0f0f;
  font-weight: 700;
}
.el-button--primary:hover { background: #d9b86f; }

/* 对话框图片 */
.dialog-image { width: 100%; border-radius: 8px; }

/* 响应式：窄屏下堆叠 */
@media (max-width: 900px) {
  .login-card { width: 100%; padding: 20px; }
  .brand { left: 24px; top: 20px; }
}
</style>
