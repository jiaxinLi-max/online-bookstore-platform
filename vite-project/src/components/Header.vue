<script setup lang="ts">
import { router } from '../router'
import { ElMessage } from 'element-plus';
import { searchProduct } from '../api/product';
import { useRouter } from 'vue-router';
import {parseRole, parseTime} from "../utils"
import { User, SwitchButton, Plus,ShoppingCart,Promotion, ChatSquare,Trophy } from "@element-plus/icons-vue"
import {userInfo} from "../api/user.ts";
 // 导入 Plus 图标
import {ref,  onMounted,computed} from 'vue';
import { Cart } from '../api/cart.ts';
const username = sessionStorage.getItem("username");
const role = ref<string>('');
console.log('roleHeader:', role);
const avatar = ref('')

const router = useRouter(); // 获取 router 实例

interface BookItem {
  value: string; // 用于 el-autocomplete 展示
  id: number;
}

const searchKeyword = ref('');
const suggestions = ref<BookItem[]>([]);

getUserInfo()
function getUserInfo() {
  userInfo(username).then(res => {
    console.log("resUserHeader", res.data);
    avatar.value = res.data.data.avatar;
    role.value = res.data.data.role;
    if(!avatar.value){
      avatar.value = 'https://bpic.588ku.com/back_origin_min_pic/19/10/22/7d5760a4e3926576c237d950d5964db1.jpg';
    }
    sessionStorage.setItem("userId", res.data.data.id);
    console.log("userId",res.data.data.id);
  }).catch(error => {
    console.error('获取用户信息失败:', error);
  });
}

function gotoAllPosting() {
  router.push({ name: 'AllPostings'});
}

// 异步获取匹配书籍
const querySearch = async (queryString: string, cb: (results: BookItem[]) => void) => {
  if (!queryString.trim()) {
    cb([]);
    return;
  }
  try {
    const res = await searchProduct(queryString.trim());
    const books = res.data.data || [];
    const results = books.map((book: any) => ({
      value: book.title,
      id: book.id
    }));
    suggestions.value = results;
    cb(results);
  } catch (err) {
    console.error('自动补全失败', err);
    cb([]);
  }
};

// 用户选中某一本书
const handleSelect = (item: BookItem) => {
  router.push({ path: `/home/product/${item.id}` });
};

// 用户点击按钮强制搜索（跳第一个）
const manualSearch = async () => {
  if (!searchKeyword.value.trim()) return;
  try {
    const res = await searchProduct(searchKeyword.value.trim());
    const books = res.data.data;
    if (books.length > 0) {
      router.push({ path: `/home/product/${books[0].id}` });
    } else {
      ElMessage.warning('未找到相关书籍');
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('搜索失败，请稍后再试');
  }
};
// 在组件挂载时获取用户信息
onMounted(() => {
  getUserInfo();
});
// 购物车状态
const cart = ref<Cart[]>([]);  // 存储购物车商品
function goToCart() {
  router.push({ path: '/cart' }); // 确保有一个 /cart 路由
  //console.log('goToCart',router);
}
// 计算购物车中商品总数
const cartItemCount = computed(() => {
  return cart.value.reduce((total, item) => total + item.quantity, 0);
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
        <router-link to="/home/all-products" v-slot="{navigate}" class="no-link">
          <h1 @click="navigate" class="header-text"> 番茄侦探小说城</h1>
          <div class="icon-label">返回主界面</div>
        </router-link>
      </el-col>

      <el-col :span="2">
        <el-tag class="role-tag custom-tag" size="large">{{ parseRole(role) }}版</el-tag>

      </el-col>

      <el-col :span="1" class="header-icon">
        <router-link to="/home/all-advertisements" class="no-link">
          <el-icon :size="35" color="white">
            <Promotion />
          </el-icon>
          <div class="icon-label">广告</div> <!-- 新增的文本标签 -->
        </router-link>
      </el-col>
      <el-col :span="1" class="header-icon">
        <router-link to="/home/rankings" class="no-link">
          <el-icon :size="35" color="white">
            <Trophy />
          </el-icon>
          <div class="icon-label">榜单</div>
        </router-link>
      </el-col>

      <el-col :span="1" class="header-icon">
        <router-link to="/home/all-postings" class="no-link">
          <el-icon :size="35" color="white">
            <ChatSquare />
          </el-icon>
          <div class="icon-label">帖子</div>
        </router-link>
      </el-col>

      <el-col :span="1">
      </el-col>

      <el-col :span="6" class="search-bar">
        <el-autocomplete
            v-model="searchKeyword"
            :fetch-suggestions="querySearch"
            placeholder="搜索书籍名称"
            size="large"
            @select="handleSelect"
            :trigger-on-focus="false"
            clearable
        >
          <template #append>
            <el-button @click="manualSearch" type="primary">搜索</el-button>
          </template>
        </el-autocomplete>
      </el-col>

      <el-col :span="6">
      </el-col>

      <template v-if="role === 'CUSTOMER'">
        <el-col :span="1" class="header-icon">
          <router-link to="/home/cart" class="no-link">
            <el-icon @click="goToCart" :size="35" color="white">
              <ShoppingCart /> <!-- 使用 Element Plus 的购物车图标 -->
            </el-icon>
          </router-link>
        </el-col>
      </template>
<!--      <el-col :span="1" class="header-icon">-->
<!--          <router-link to="/home/cart" class="no-link">-->
<!--&lt;!&ndash;            <button @click="goToCart">购物车 ({{ cartItemCount }})</button>&ndash;&gt;-->
<!--            <button @click="goToCart">购物车 </button>-->
<!--          </router-link>-->
<!--      </el-col>-->
      <template v-if="role === 'MANAGER'">
        <el-col :span="1" class="header-icon">
            <router-link to="/home/create-product" v-slot="{ navigate }">
              <el-icon @click="navigate" :size="35" color="white">
                <Plus />
              </el-icon>
            </router-link>
        </el-col>
      </template>

      <el-col :span="1" class="header-icon">
        <a @click="logout">
          <el-icon :size="35" color="white" ><SwitchButton /></el-icon>
        </a>
      </el-col>
      <el-col :span="1" class="header-icon">
        <router-link to="/home/dashboard" class="no-link">
          <!-- 动态绑定头像路径 -->
          <img :src="avatar" alt="User Avatar" class="user-avatar" />
        </router-link>
      </el-col>
    </el-row>
  </el-header>
</template>



<style scoped>
.custom-header {
  background-color: #3b3225; /* 深羊驼色 */
  display: flex;
  flex-direction: column;
}

.custom-tag {
  background-color: #3b3225;
  color: white;
  border-color: #3b3225;
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

.user-avatar {
  width: 40px; /* 设置头像宽度 */
  height: 40px; /* 设置头像高度 */
  border-radius: 50%; /* 设置为圆形 */
  object-fit: cover; /* 确保图片填充整个区域 */
  margin-top: 10px; /* 调整垂直位置 */
  margin-bottom: 10px; /* 调整垂直位置 */
}
.icon-label {
  color: white;
  font-size: 12px;
  text-align: center;
  margin-top: 2px;
}
.search-bar {
  margin-top: 12px; /* 向下移动 */
}
</style>
