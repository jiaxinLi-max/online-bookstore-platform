<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {userInfo, userInfoUpdate} from '../../api/user.ts'
import {parseRole, parseTime} from "../../utils"
import {router} from '../../router'
import {UserFilled} from "@element-plus/icons-vue";
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
const regTime = ref()

const newName = ref('')

const displayInfoCard = ref(false)

const password = ref('')
const confirmPassword = ref('')

const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordIdentical = computed(() => password.value == confirmPassword.value)
const changeDisabled = computed(() => {
  return !(hasConfirmPasswordInput.value && isPasswordIdentical.value)
})

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
    console.log("res", res.data);
    //username = res.data.username;
    name.value = res.data.result.name;
    telephone.value = res.data.result.phone;
    // storeId.value = res.data.result.storeId;
    // address.value = res.data.result.address;
    location.value = res.data.result.location;
    avatar.value = res.data.result.avatar;
    email.value = res.data.result.email;
    regTime.value = parseTime(res.data.result.createTime);
    newName.value = name.value;

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
    password: undefined,
    avatar: avatar.value,
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
    password: password.value,
    avatar: undefined,
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
onMounted(async () => {
  // await fetchStores(); // 首先获取商店列表
  await getUserInfo(); // 然后获取用户信息
});
</script>


<template>
  <el-main class="main-container">
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :icon="UserFilled" :size="80">
        </el-avatar>
        <span class="avatar-text"> 欢迎您，{{ name }}</span>
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

        <el-descriptions-item label="注册时间">
          {{ regTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form>
<!--        <el-form-item>-->
<!--          <label for="name">昵称</label>-->
<!--          <el-input type="text" id="name" v-model="newName"/>-->
<!--        </el-form-item>-->

        <el-form-item>
          <label for="telephone">手机号</label>
          <el-input id="telephone" v-model="telephone" disabled/>
        </el-form-item>

        <el-form-item v-if="role === 'CUSTOMER'">
          <label for="location">收货地址</label>
          <el-input id="location" type="textarea"
                    rows="4"
                    v-model="location" placeholder="中华门"></el-input>
        </el-form-item>

        <el-form-item>
          <label for="email">邮箱</label>
          <el-input id="email" v-model="email" disabled/>
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


</style>
