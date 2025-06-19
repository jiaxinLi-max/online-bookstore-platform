<!--<script setup lang="ts">-->
<!--import {ElForm, ElFormItem} from "element-plus"-->
<!--import {ref, computed} from 'vue'-->
<!--import {router} from '../../router'-->
<!--import {userInfo, userLogin} from "../../api/user.ts"-->

<!--// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）-->
<!--const tel = ref('')-->
<!--const password = ref('')-->

<!--// 电话号码是否为空-->
<!--const hasTelInput = computed(() => tel.value != '')-->
<!--// 密码是否为空-->
<!--const hasPasswordInput = computed(() => password.value != '')-->
<!--// 电话号码的规则-->
<!--const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/-->
<!--const telLegal = computed(() => chinaMobileRegex.test(tel.value))-->
<!--// 密码不设置特殊规则-->
<!--// 登录按钮可用性-->
<!--const loginDisabled = computed(() => {-->
<!--  return !(hasTelInput.value && telLegal.value && hasPasswordInput.value)-->
<!--})-->

<!--// 登录按钮触发-->
<!--function handleLogin() {-->
<!--  userLogin({-->
<!--    phone: tel.value,-->
<!--    password: password.value-->
<!--  }).then(res => {-->
<!--    if (res.data.code === '000') {-->
<!--      ElMessage({-->
<!--        message: "登录成功！",-->
<!--        type: 'success',-->
<!--        center: true,-->
<!--      })-->
<!--      const token = res.data.result-->
<!--      sessionStorage.setItem('token', token)-->

<!--      userInfo().then(res => {-->
<!--        sessionStorage.setItem('name', res.data.result.name)-->
<!--        sessionStorage.setItem('role', res.data.result.role)-->
<!--        //router.push({path: "/dashboard"})-->
<!--        router.push({ path: "/home/all-stores" }); // 确保这条路由存在-->
<!--      })-->
<!--    } else if (res.data.code === '400') {-->
<!--      ElMessage({-->
<!--        message: res.data.msg,-->
<!--        type: 'error',-->
<!--        center: true,-->
<!--      })-->
<!--      password.value = ''-->
<!--    }-->
<!--  })-->
<!--}-->
<!--</script>-->


<!--<template>-->
<!--  <el-main class="main-frame bgimage">-->
<!--    <el-card class="login-card">-->
<!--      <div>-->
<!--        <h1>登入您的账户</h1>-->
<!--        <el-form>-->
<!--          <el-form-item>-->
<!--            <label v-if="!hasTelInput" for="tel">注册手机号</label>-->
<!--            <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>-->
<!--            <label v-else for="tel">注册手机号</label>-->
<!--            <el-input id="tel" type="text" v-model="tel"-->
<!--                      required :class="{'error-warn-input' :(hasTelInput && !telLegal)}"-->
<!--                      placeholder="请输入手机号"/>-->
<!--          </el-form-item>-->

<!--          <el-form-item>-->
<!--            <label for="password">账户密码</label>-->
<!--            <el-input id="password" type="password" v-model="password"-->
<!--                      required-->
<!--                      placeholder="••••••••"/>-->
<!--          </el-form-item>-->

<!--          <span class="button-group">-->
<!--              <el-button @click.prevent="handleLogin" :disabled="loginDisabled"-->
<!--                         type="primary">登入</el-button>-->
<!--              <router-link to="/register" v-slot="{navigate}">-->
<!--                <el-button @click="navigate">去注册</el-button>-->
<!--              </router-link>-->
<!--          </span>-->
<!--        </el-form>-->
<!--      </div>-->
<!--    </el-card>-->
<!--  </el-main>-->
<!--</template>-->


<!--<style scoped>-->
<!--.main-frame {-->
<!--  width: 100%;-->
<!--  height: 100%;-->

<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--}-->

<!--.bgimage {-->
<!--  background-image: url("../../assets/shopping-1s-1084px.svg");-->
<!--}-->

<!--.login-card {-->
<!--  width: 60%;-->
<!--  padding: 10px;-->
<!--}-->

<!--.error-warn {-->
<!--  color: red;-->
<!--}-->

<!--.error-warn-input {-->
<!--  &#45;&#45;el-input-focus-border-color: red;-->
<!--}-->

<!--.button-group {-->
<!--  padding-top: 10px;-->
<!--  display: flex;-->
<!--  flex-direction: row;-->
<!--  gap: 30px;-->
<!--  align-items: center;-->
<!--  justify-content: right;-->
<!--}-->
<!--</style>-->
<template>
  <div class="main-frame bgimage">
    <div class="login-form">
      <h2>侦探小说网 登录</h2>
      <el-form>
        <el-form-item>
<!--          <label v-if="!hasTelInput" for="tel">注册手机号</label>-->
<!--          <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>-->
<!--          <label v-else for="tel">注册手机号</label>-->
<!--          <el-input id="tel" type="text" v-model="tel"-->
<!--                    required :class="{'error-warn-input' :(hasTelInput && !telLegal)}"-->
<!--                    placeholder="请输入手机号"/>-->
          <label v-if="!hasUsernameInput" for="username">注册用户名</label>
<!--          <label v-else-if="!telLegal" for="tel" class="error-warn">用户名不合法</label>-->
          <label v-else for="username">注册用户名</label>
          <el-input id="username" type="text" v-model="username"
                    required :class="{'error-warn-input' :(hasUsernameInput)}"
                    placeholder="请输入用户名"/>
        </el-form-item>

        <el-form-item>
          <label for="password">账户密码</label>
          <el-input id="password" type="password" v-model="password"
                    required
                    placeholder="••••••••"/>
        </el-form-item>

        <span class="button-group">
          <el-button @click.prevent="handleLogin" :disabled="loginDisabled"
                     type="primary">登入</el-button>
          <router-link to="/register" v-slot="{navigate}">
            <el-button @click="navigate">去注册</el-button>
          </router-link>
        </span>
      </el-form>
    </div>
  </div>
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
  background-image: url("../../assets/login.png");
  background-position: center top;
  background-size: 1500px auto; /* 或根据需求调整为 cover 或百分比 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 关键属性：背景固定 */
  background-color: #7b6b4d; /* 深羊驼色兜底 */
  min-height: 100vh;
}

.login-form {
  background: rgba(255, 215, 0, 0.5);
  padding: 40px 40px;
  border-radius: 10px;
  width: 400px;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
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
<script setup lang="ts">
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage } from 'element-plus'
import { ref, computed } from 'vue'
import { router } from '../../router'
import { userInfo, userLogin } from '../../api/user.ts'
import { useRoute } from 'vue-router';
import { onMounted } from 'vue';

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const username = ref('')

const password = ref('')
const route = useRoute();

// 用户名是否为空
const hasUsernameInput = computed(() => username.value != '')
// 密码是否为空
const hasPasswordInput = computed(() => password.value != '')

// 登录按钮可用性
const loginDisabled = computed(() => {
  // return !(hasTelInput.value && telLegal.value && hasPasswordInput.value)
  return !(hasUsernameInput.value && hasPasswordInput.value)
})

// 登录按钮触发
function handleLogin() {
  userLogin({
    username: username.value,
    password: password.value
  }).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: "登录成功！",
        type: 'success',
        center: true,
      })
      const token = res.data.data
      sessionStorage.setItem('token', token)
      console.log('tokenLogin', token)
      sessionStorage.setItem('username', username.value)
      //sessionStorage.setItem('userId',userId.value)

      console.log("laile1");
      //router.push({ path: "/home/all-products" });
      console.log("7",username.value);
      userInfo(username.value).then(res => {
        console.log("laile2");
        //sessionStorage.setItem('username', res.data.result.username)
        sessionStorage.setItem('role', res.data.data.role)
        console.log("role_is:",res.data.data.role)
        //router.push({path: "/dashboard"})
        console.log("laile");
        //router.push({ path: "/home/all-products" }); // 确保这条路由存在
      })

      router.push({ path: "/home/all-products" });

    } else  {
      //console.log("error");
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
      password.value = ''
    }
  })
}

onMounted(() => {
  // 检查URL的查询参数中是否同时存在 out_trade_no 和 trade_no
  // 这是判断是否为支付宝回调的关键
  if (route.query.out_trade_no && route.query.trade_no) {
    console.log('Alipay return detected, closing window.');
    // 如果是，则直接关闭当前窗口
    window.close();
  }
});
</script>

<!--<style scoped>-->
<!--.login-container {-->
<!--  position: relative;-->
<!--  width: 100%;-->
<!--  height: 100vh;-->

<!--  background-image: url("../../assets/login.png");-->
<!--  background-size: cover;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--}-->

<!--.login-form {-->
<!--  background: rgba(255, 215, 0, 0.9);-->
<!--  padding: 40px 60px;-->
<!--  border-radius: 10px;-->
<!--  width: 400px;-->
<!--}-->

<!--h2 {-->
<!--  text-align: center;-->
<!--  color: #333;-->
<!--  margin-bottom: 30px;-->
<!--}-->

<!--.error-warn {-->
<!--  color: red;-->
<!--}-->

<!--.error-warn-input {-->
<!--  &#45;&#45;el-input-focus-border-color: red;-->
<!--}-->

<!--.button-group {-->
<!--  padding-top: 10px;-->
<!--  display: flex;-->
<!--  flex-direction: row;-->
<!--  gap: 30px;-->
<!--  align-items: center;-->
<!--  justify-content: right;-->
<!--}-->
<!--</style>-->

