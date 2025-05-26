<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {userInfo, userInfoUpdate} from '../../api/user.ts'
import {parseRole} from "../../utils"
import {router} from '../../router'
import {ElMessage, UploadFile} from "element-plus";
import {getImage} from "../../api/tools.ts";
// import {UserFilled} from "@element-plus/icons-vue";
// import { getAllStore,  Store } from '../../api/store';

// const stores = ref<Store[]>([]);
const username = sessionStorage.getItem("username")
const role = sessionStorage.getItem("role")
const name = ref('')
const avatar = ref('')
// const storeName = ref('')
// const storeId=ref<number | null>(null)

// const tel = ref('')
// const address = ref('')
const telephone = ref('')
const location = ref('')
const email = ref('')
// const regTime = ref()

// const newName = ref('')

const displayInfoCard = ref(false)

const password = ref('')
const confirmPassword = ref('')
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

const credits = ref(0);
const level = ref(0);

const progressPercentage = computed(() => credits.value % 100);

const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordIdentical = computed(() => password.value == confirmPassword.value)
const changeDisabled = computed(() => {
  return !(hasConfirmPasswordInput.value && isPasswordIdentical.value)
})

const hasImageFile = computed(() => avatar.value !== '');
const MAX_SIZE = 1024 * 1024; // 1MB
const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/gif'];
const handleExceed = (files, fileList) => {
  ElMessage.warning('最多只能上传一张头像');
};

// async function fetchStores() {
//   try {
//     const res = await getAllStore(); // 调用 API 获取商店列表
//     stores.value = res.data.result; // 假设返回的数据格式正确
//   } catch (error) {
//     console.error('获取商店列表失败:', error);
//   }
// }

getUserInfo()
function getUserInfo() {
  userInfo(username).then(res => {
    if (res.data.code === '200') {
      console.log("获取成功");
      console.log("res", res.data);
      //username = res.data.username;
      name.value = res.data.data.name;
      telephone.value = res.data.data.telephone;
      // storeId.value = res.data.result.storeId;
      // address.value = res.data.result.address;
      location.value = res.data.data.location;
      avatar.value = res.data.data.avatar;
      email.value = res.data.data.email;
      credits.value = res.data.data.score;
      level.value = res.data.data.grade;
      // newName.value = name.value;
    } else {
      console.log("获取失败");
    }

    // 查找对应的商店名称
    // const store = stores.value.find(store => store.id === storeId.value);
    // storeName.value = store ? store.name : '未知商店'; // 更新 storeName
    // console.log(`所属商店: ${storeName.value}`);
  }).catch(error => {
    console.error('获取用户信息失败:', error);
  });
}

function updateInfo() {
  userInfoUpdate({
    username: username,
    password: undefined,
    name: name.value,
    avatar: avatar.value,
    role: undefined,
    telephone: telephone.value,
    email: email.value,
    location: location.value,
  }).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        customClass: 'customMessage',
        type: 'success',
        message: '更新成功！',
      })
      getUserInfo()
    } else {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}

function updatePassword() {
  userInfoUpdate({
    username: username,
    password: password.value,
    name: undefined,
    avatar: undefined,
    role: undefined,
    telephone: undefined,
    email: undefined,
    location: undefined,
  }).then(res => {
    if (res.data.code === '200') {
      password.value = ''
      confirmPassword.value = ''
      ElMessageBox.alert(
          `请重新登录`,
          '修改成功',
          {
            customClass: "customDialog",
            confirmButtonText: '跳转到登录',
            type: "success",
            showClose: false,
            roundButton: true,
            center: true
          }).then(() => router.push({path: "/login"}))
    } else {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
      password.value = ''
      confirmPassword.value = ''
    }
  })
}

async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
  const rawFile = file.raw; // 获取原始文件对象
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }
  if (!ALLOWED_TYPES.includes(rawFile.type)) {
    ElMessage.error('只允许上传 JPG/PNG/GIF 格式的文件');
    return;
  }

  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）');
    return;
  }

  try {
    const res = await getImage(rawFile); // 调用上传接口
    console.log("res.data",res.data);
    if (res) {
      //console.log("res.result",res.result); // 输出返回的链接

      if (res.code === '200') {
        //avatar.value = res.result.trim(); // 去除可能的多余空格
        avatar.value = res.data;
        console.log('avatar URL:', avatar.value);

        // 更新 fileList 中的文件对象，添加 URL
        fileListNew[0].url = avatar.value;
        fileList.value = fileListNew;

        ElMessage.success('上传成功');
      }
    } else {
      ElMessage.error('响应格式错误，请重试');
    }
  } catch (error) {
    console.error('上传失败:', error);
    ElMessage.error('上传失败，请重试');
  }
}

// 处理图片预览
const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || ''; // 使用 file.url 进行预览
  dialogVisible.value = true;
};

// 处理文件删除
const handleRemove = (file: UploadFile) => {
  fileList.value = fileList.value.filter(item => item.uid !== file.uid); // 从文件列表中移除
};

onMounted(async () => {
  // await fetchStores(); // 首先获取商店列表
  await getUserInfo(); // 然后获取用户信息
});
</script>


<template>
  <el-main class="main-container">
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :src="avatar" :size="80">
        </el-avatar>
        <span class="avatar-text"> 欢迎您，{{ username }}</span>
      </div>

      <el-divider></el-divider>

      <el-descriptions
          :column="1"
          border
          title="个人信息"
      >
        <template #extra>
          <el-button type="primary"
                     @click="displayInfoCard = displayInfoCard === false;">
            <span v-if="displayInfoCard">修改密码</span>
            <span v-else>修改个人信息</span>
          </el-button>
        </template>

        <el-descriptions-item label="身份">
          <el-tag>{{ parseRole(role) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="用户名">
          <el-tag>{{ username }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="真实姓名">
          <el-tag>{{ name }}</el-tag>
        </el-descriptions-item>

<!--        <el-descriptions-item label="所属商店" v-if="role === 'STAFF'">-->
<!--          {{ storeName }}-->
<!--&lt;!&ndash;          {{ storeId }}&ndash;&gt;-->
<!--        </el-descriptions-item>-->

        <el-descriptions-item label="联系电话">
          {{ telephone }}
        </el-descriptions-item>

        <el-descriptions-item label="地址" v-if="role === 'CUSTOMER'">
          {{ location }}
        </el-descriptions-item>

<!--        <el-descriptions-item label="注册时间">-->
<!--          {{ regTime }}-->
<!--        </el-descriptions-item>-->

        <el-descriptions-item label="电子邮箱">
          {{ email }}
        </el-descriptions-item>

      </el-descriptions>

      <div class="progress-container" v-if="role === 'CUSTOMER'">
        <el-progress
            :percentage="progressPercentage"
            :text-inside="true"
            :stroke-width="24"
            style="flex: 1;"
        />
        <div class="level-display">
          等级 {{ level }}
        </div>
      </div>
      <div class="level-rules">
        <p>• 最高可升至10级，每级享受额外0.5折优惠</p>
        <p>• 每消费10元获取1积分，累计100积分可升1级</p>
      </div>
    </el-card>

    <el-card v-if="displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="name">真实姓名</label>
          <el-input type="text" id="name" v-model="name" disabled/>
        </el-form-item>

        <el-form-item>
          <label for="telephone">手机号</label>
          <el-input id="telephone" v-model="telephone"/>
        </el-form-item>

        <el-form-item v-if="role === 'CUSTOMER'">
          <label for="location">收货地址</label>
          <el-input id="location" type="textarea"
                    rows="4"
                    v-model="location" placeholder="中华门"></el-input>
        </el-form-item>

        <el-form-item>
          <label for="email">邮箱</label>
          <el-input id="email" v-model="email"/>
        </el-form-item>

        <el-form-item label="头像" prop="logo">
          <div v-if="fileList.length === 0">
            <el-upload
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

              <el-icon><Plus /></el-icon>
              <div>点击上传头像</div>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="!displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
          <el-button @click="updatePassword" :disabled="changeDisabled">
            修改
          </el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="password">密码</label>
          <el-input type="password" id="password" v-model="password" placeholder="•••••••••" required/>
        </el-form-item>
        <el-form-item>
          <label v-if="!hasConfirmPasswordInput" for="confirm_password">确认密码</label>
          <label v-else-if="!isPasswordIdentical" for="confirm_password" class="error-warn">密码不一致</label>
          <label v-else for="confirm_password">确认密码</label>

          <el-input type="password" id="confirm_password" v-model="confirmPassword"
                    :class="{'error-warn-input' :(hasConfirmPasswordInput && !isPasswordIdentical)}"
                    placeholder="•••••••••" required/>
        </el-form-item>
      </el-form>

    </el-card>
  </el-main>

</template>


<style scoped>

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.main-container {
  display: flex;
  flex-direction: row;
  padding: 15px;
  gap: 5px;
  justify-content: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.change-card {
  width: 66%;
  background: rgba(255, 215, 0, 0.8);
}

.avatar-area {
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 30px;
}

.avatar-text {
  font-size: x-large;
  font-weight: bolder;
  padding-right: 40px;
}

.main-container {
  background-image: url("../../assets/login.png");
}

.aside-card {
  background: rgba(255, 215, 0, 0.8);
}

.progress-container {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.level-display {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  min-width: 80px;
  text-align: center;
}

.level-rules {
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
  padding-left: 8px;
}

.level-rules p {
  margin: 4px 0;
}

</style>
