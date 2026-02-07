<template>
  <div class="login-wrapper">

    <!-- 左上角品牌 Logo + 英文副标题 -->
    <div class="brand">
      <div class="brand-cn">真相书局</div>
      <div class="brand-en">TRUTH BOOKSTORE</div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <h2 class="title">登录账户</h2>

      <el-form>
        <el-form-item>
          <el-input
              v-model="username"
              placeholder="用户名"
              clearable
              size="large"
          />
        </el-form-item>

        <el-form-item>
          <el-input
              v-model="password"
              placeholder="密码"
              type="password"
              clearable
              size="large"
          />
        </el-form-item>

        <el-button
            type="primary"
            class="login-btn"
            size="large"
            :disabled="loginDisabled"
            @click.prevent="handleLogin"
        >
          登录
        </el-button>

        <div class="extra-links">
          <router-link to="/register">没有账号？去注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>




<style scoped>
.login-wrapper {
  width: 100%;
  height: 100vh;
  position: relative;

  /* 侦探感渐变背景（深墨绿 → 黑） */
  background: linear-gradient(145deg, #1e2a27 0%, #0e0f0f 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

/*********** Logo 区域 ***********/
.brand {
  position: absolute;
  top: 40px;
  left: 60px;

  color: #e6d5a3; /* 暗金色 */
  font-family: "Cinzel", "Times New Roman", serif;
  text-shadow: 0 2px 6px rgba(0,0,0,0.4);

  user-select: none;
}

.brand-cn {
  font-size: 40px;
  font-weight: 700;
  letter-spacing: 4px;
}

.brand-en {
  margin-top: 4px;
  font-size: 14px;
  opacity: 0.8;
  letter-spacing: 3px;
}

/*********** 登录卡片 ***********/
.login-card {
  width: 380px;
  padding: 40px 45px;
  border-radius: 18px;

  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(14px);

  border: 1px solid rgba(255,255,255,0.15);
  box-shadow: 0 8px 40px rgba(0,0,0,0.3);

  animation: fadeIn 0.7s ease-out;
}

.title {
  text-align: center;
  color: #f0eee9;
  font-size: 22px;
  margin-bottom: 30px;
  font-weight: 600;
}

.login-btn {
  width: 100%;
  margin-top: 5px;
  border-radius: 8px;
  background-color: #c6a667;  /* 暗金色主按钮 */
  border-color: #c6a667;
}

.extra-links {
  margin-top: 15px;
  text-align: center;
}

.extra-links a {
  color: #d7caa8;
  font-size: 14px;
  text-decoration: none;
}

/* 淡入效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
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

