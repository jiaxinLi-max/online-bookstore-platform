<script setup lang="ts">
import { router } from '../router'
import {parseRole, parseTime} from "../utils"
import { User, SwitchButton, Plus } from "@element-plus/icons-vue"
import {userInfo} from "../api/user.ts";
 // 导入 Plus 图标
import {ref,  onMounted} from 'vue';
const username = sessionStorage.getItem("username");
const role = sessionStorage.getItem('role')    // 登录的时候插入的
console.log('role:', role);
const avatar = ref('')
getUserInfo()
function getUserInfo() {
  userInfo(username).then(res => {
    console.log("resUserHeader", res.data);
    avatar.value = res.data.avatar;

  }).catch(error => {
    console.error('获取用户信息失败:', error);
  });
}
// 在组件挂载时获取用户信息
onMounted(() => {
  getUserInfo();
});
// 退出登录
function logout() {
  ElMessageBox.confirm(
      '是否要退出登录？',
      '提示',
      {
        customClass: "customDialog",
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: "warning",
        showClose: false,
        roundButton: true,
        center: true
      }
  ).then(() => {
    sessionStorage.setItem('token', '')
    router.push({ path: "/login" })
  })
}

</script>



<template>
  <el-header class="custom-header" height="20">
    <el-row :gutter="10">

      <el-col :span="3" class="header-icon">
        <router-link to="/dashboard" v-slot="{navigate}" class="no-link">
          <h1 @click="navigate" class="header-text"> 番茄侦探小说城</h1>
        </router-link>
      </el-col>

      <el-col :span="2">
        <el-tag class="role-tag custom-tag" size="large">{{ parseRole(role) }}版</el-tag>

      </el-col>

      <el-col :span="16">
      </el-col>


      <el-col :span="1" class="header-icon">
        <router-link to="/home/dashboard" class="no-link">
          <!-- 动态绑定头像路径 -->
          <img :src="avatar" alt="User Avatar" class="user-avatar" />
        </router-link>
      </el-col>

      <el-col :span="1" class="header-icon">
        <template v-if="role === 'MANAGER'">
          <router-link to="/home/create-product" v-slot="{ navigate }">
            <el-icon @click="navigate" :size="35" color="white">
              <Plus /> <!-- 假设您使用 Plus 图标 -->
            </el-icon>
          </router-link>
        </template>
      </el-col>



      <el-col :span="1" class="header-icon">
        <a @click="logout">
          <el-icon :size="35" color="white" ><SwitchButton /></el-icon>
        </a>
      </el-col>
    </el-row>
  </el-header>
</template>



<style scoped>
.custom-header {
  background-color: #000000;


  display: flex;
  flex-direction: column;
}
.custom-tag {
  background-color: #000000; /* 背景颜色改为黑色 */
  color: white; /* 文字颜色改为白色 */
  border-color: #000000; /* 边框颜色改为黑色 */
}

.no-link {
  text-decoration: none;
}

.role-tag {
  margin-top: 20px;
  font-size: 20px;
}

.header-text {
  color:white;
  font-size: x-large;
  min-width: max-content;
  margin-top: 15px;
  margin-bottom: 15px;
}

.header-icon {
  display: flex;
  flex-direction: column;
  align-items:center;
  justify-content: center;
}
</style>
