<script setup lang="ts">
import {ref, computed, watch, onMounted} from 'vue';
import { router } from '../../router';
import { userRegister } from "../../api/user.ts";
import { ElMessage } from 'element-plus'; // 确保引入 ElMessage

const name = ref('');
const identity = ref('');
const tel = ref('');
const address = ref('');
const password = ref('');
const confirmPassword = ref('');
const storeId = ref<number | null>(null); // 将商店ID初始化为空字符串
const storeName = ref(''); // 商店名称

// 商店列表
// const stores = [
//   { id: '1', name: '商店1' },
//   { id: '2', name: '商店2' },
//   { id: '3', name: '商店3' },
// ];
 import { getAllStore, createStore, Store } from '../../api/store';

 const stores = ref<Store[]>([]);
// 各种输入验证
const hasTelInput = computed(() => tel.value !== '');
const hasPasswordInput = computed(() => password.value !== '');
const hasConfirmPasswordInput = computed(() => confirmPassword.value !== '');
const hasAddressInput = computed(() => address.value !== '');
const hasIdentityChosen = computed(() => identity.value !== '');
const hasStoreName = computed(() => identity.value === 'STAFF' ? storeId.value !== null : true); // 修改为检查空字符串

const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;
const telLegal = computed(() => chinaMobileRegex.test(tel.value));
const isPasswordIdentical = computed(() => password.value === confirmPassword.value);
const registerDisabled = computed(() => {
  if (!hasIdentityChosen.value) {
    return true;
  } else if (identity.value === 'CUSTOMER') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput.value && hasAddressInput.value &&
        telLegal.value && isPasswordIdentical.value);
  } else if (identity.value === 'STAFF') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput.value && hasAddressInput.value &&
        hasStoreName.value && telLegal.value && isPasswordIdentical.value);
  }
});

// 加载状态
const loading = ref(false);

// 注册按钮触发
async function handleRegister() {
  loading.value = true;
  try {
    const res = await userRegister({
      role: identity.value,
      name: name.value,
      phone: tel.value,
      password: password.value,
      address: address.value,
      storeId: storeId.value !== null ? storeId.value : undefined,

    });

    // 处理注册结果
    if (res.data.code === '000') {
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      });
      router.push({ path: "/login" });
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      });
    }
  } catch (error) {
    ElMessage({
      message: "注册失败，请稍后再试",
      type: 'error',
      center: true,
    });
  } finally {
    loading.value = false;
  }
}

// // 监听 storeId 的变化，更新 storeName
// watch(storeId, (newId) => {
//   const store = stores.find(store => store.id === newId?.toString());
//   storeName.value = store ? store.name : '';
// });

 // 监听 storeId 的变化，更新 storeName
 watch(storeId, (newId) => {
   const store = stores.value.find(store => store.id === newId);
   storeName.value = store ? store.name : '';
 });

 // 获取所有商店
 async function get_getAllStores() {
   try {
     const res = await getAllStore(); // 调用从后端获取商店的函数
     if (res.data && Array.isArray(res.data.result)) {
       stores.value = res.data.result; // 更新商店列表
     } else {
       console.error('获取数据失败：响应格式不符合预期');
     }
   } catch (error) {
     console.error('获取商店列表失败:', error);
   }
 }

 // 在组件挂载时获取商店列表
 onMounted(() => {
   get_getAllStores();
 });
</script>

<template>
  <el-main class="main-frame bgimage">
    <el-card class="login-card">
      <div>
        <h1>创建一个新的账户</h1>

        <el-form>
          <el-row>
            <el-col :span="15">
              <el-form-item>
                <label for="name">昵称</label>
                <el-input id="name" v-model="name" placeholder="请输入昵称" />
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="8">
              <el-form-item>
                <label for="identity">身份</label>
                <el-select id="identity" v-model="identity" placeholder="请选择" style="width: 100%;">
                  <el-option value="CUSTOMER" label="顾客"/>
                  <el-option value="STAFF" label="商家"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item>
                <label v-if="!hasTelInput" for="tel">注册手机号</label>
                <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>
                <label v-else for="tel">注册手机号</label>
                <el-input id="tel" v-model="tel" :class="{'error-warn-input': (hasTelInput && !telLegal)}" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="15" v-if="identity !== 'STAFF'">
              <el-form-item>
                <label for="address">地址</label>
                <el-input id="address" v-model="address" placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

            <el-col :span="7" v-if="identity === 'STAFF'">
              <el-form-item>
                <label for="address">地址</label>
                <el-input id="address" v-model="address" placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

            <el-col :span="1" v-if="identity === 'STAFF'"></el-col>

            <!-- 所属商店选择 -->
            <el-col :span="7" v-if="identity === 'STAFF'">
              <el-form-item :prop="hasStoreName ? 'storeId' : ''">
                <label for="storeId">所属商店</label>
                <el-select id="storeId" v-model="storeId" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="store in stores" :key="store.id" :value="store.id" :label="store.name" />
                </el-select>

              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <label for="password">密码</label>
            <el-input type="password" id="password" v-model="password" placeholder="••••••••"/>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasConfirmPasswordInput">确认密码</label>
            <label v-else-if="!isPasswordIdentical" class="error-warn">密码不一致</label>
            <label v-else>确认密码</label>
            <el-input type="password" id="confirm-password" v-model="confirmPassword" :class="{'error-warn-input': (hasConfirmPasswordInput && !isPasswordIdentical)}" placeholder="••••••••"/>
          </el-form-item>

          <span class="button-group">
            <el-button @click.prevent="handleRegister" :disabled="registerDisabled || loading" type="primary">
              <span v-if="loading">注册中...</span>
              <span v-else>创建账户</span>
            </el-button>

            <router-link to="/login" v-slot="{ navigate }">
              <el-button @click="navigate">去登录</el-button>
            </router-link>
          </span>
        </el-form>
      </div>
    </el-card>
  </el-main>
</template>

<style scoped>
.main-frame {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bgimage {
  background-image: url("../../assets/shopping-1s-1084px.svg");
}

.login-card {
  width: 60%;
  padding: 10px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
</style>
