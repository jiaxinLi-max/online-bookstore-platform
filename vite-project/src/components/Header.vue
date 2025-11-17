<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessageBox, ElBadge, ElTooltip, ElTag } from 'element-plus';
import { User, SwitchButton, Plus, ShoppingCart, Document, ChatSquare } from '@element-plus/icons-vue';
import { searchProduct } from '../api/product';
import { userInfo } from '../api/user';
import { Cart } from '../api/cart';
import { parseRole } from '../utils';

const router = useRouter();
const route = useRoute();

// 搜索
const searchKeyword = ref('');
const suggestions = ref<{ value: string }[]>([]);
const showSuggestions = ref(false);

async function fetchSuggestions(keyword: string) {
  if (!keyword.trim()) {
    suggestions.value = [];
    return;
  }
  try {
    const res = await searchProduct(keyword);
    suggestions.value = res.data.data.map((item: any) => ({ value: item.title || '' })).slice(0, 5);
  } catch {
    suggestions.value = [];
  }
}

function onInputChange() {
  if (searchKeyword.value.trim()) {
    fetchSuggestions(searchKeyword.value);
    showSuggestions.value = true;
  } else showSuggestions.value = false;
}

function onSelectSuggestion(item: { value: string }) {
  searchKeyword.value = item.value;
  showSuggestions.value = false;
  router.push({ name: 'SearchResults', params: { keyword: item.value } });
}

function onSearch() {
  if (!searchKeyword.value.trim()) return;
  showSuggestions.value = false;
  router.push({ name: 'SearchResults', params: { keyword: searchKeyword.value.trim() } });
}

function onKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter') onSearch();
}

// 用户信息
const username = sessionStorage.getItem('username');
const role = ref('');
const avatar = ref('');

async function getUserInfo() {
  if (!username) return;
  try {
    const res = await userInfo(username);
    avatar.value = res.data.data.avatar || 'https://bpic.588ku.com/back_origin_min_pic/19/10/22/7d5760a4e3926576c237d950d5964db1.jpg';
    role.value = res.data.data.role;
    sessionStorage.setItem('userId', res.data.data.id);
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
}

onMounted(() => {
  getUserInfo();
});

// 购物车
const cart = ref<Cart[]>([]);
const cartItemCount = computed(() => cart.value.reduce((total, item) => total + item.quantity, 0));
function goToCart() { router.push({ path: '/cart' }); }

// 子导航显示
const showSubNav = computed(() => ['/home/all-products','/home/rankings','/home/all-columns'].includes(route.path));

// 悬浮缩放
const isShrink = ref(false);
const handleScroll = () => { isShrink.value = window.scrollY > 40; };
onMounted(() => window.addEventListener('scroll', handleScroll));
onUnmounted(() => window.removeEventListener('scroll', handleScroll));

// 退出
function logout() {
  ElMessageBox.confirm('是否要退出登录？','提示',{
    confirmButtonText: '是', cancelButtonText: '否', type: 'warning',
    center: true, showClose: false
  }).then(() => {
    sessionStorage.setItem('token','');
    router.push({ path: '/login' });
  });
}
</script>

<template>
  <!-- Header -->
  <header :class="['detective-header', { shrink: isShrink }]">
    <div class="header-inner">
      <!-- Logo -->
      <div class="logo" @click="router.push('/home/all-products')">真相书局 TRUTH BOOKSTORE</div>

      <!-- 搜索栏 -->
      <div class="search-wrapper">
        <input
            v-model="searchKeyword"
            @input="onInputChange"
            @keydown="onKeydown"
            class="search-bar"
            placeholder="输入书名、作者或线索搜索..."
        />
        <ul v-if="showSuggestions && suggestions.length" class="suggestions-list">
          <li v-for="(item, idx) in suggestions" :key="idx" class="suggestion-item" @click="onSelectSuggestion(item)">
            {{ item.value }}
          </li>
        </ul>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area">
        <el-tag class="role-tag">{{ parseRole(role) }}版</el-tag>

        <div v-if="role==='CUSTOMER'" class="icon-group">
          <el-tooltip content="真相笔记">
            <div class="icon-btn" @click="router.push('/home/all-postings')"><el-icon><ChatSquare /></el-icon></div>
          </el-tooltip>
          <el-tooltip content="真相解码">
            <div class="icon-btn" @click="router.push('/home/all-questions')"><el-icon><Document /></el-icon></div>
          </el-tooltip>
          <el-tooltip content="购物车">
            <div class="icon-btn" @click="goToCart">
              <el-badge :value="cartItemCount"><el-icon><ShoppingCart /></el-icon></el-badge>
            </div>
          </el-tooltip>
        </div>

        <div v-if="role==='MANAGER'" class="icon-group">
          <el-dropdown trigger="click">
            <span class="dropdown-trigger"><el-icon><Plus /></el-icon></span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/home/create-product')">创建书籍</el-dropdown-item>
                <el-dropdown-item @click="router.push('/home/create-columns')">创建栏目</el-dropdown-item>
                <el-dropdown-item @click="router.push('/home/create-question')">创建问题</el-dropdown-item>
                <el-dropdown-item @click="router.push('/home/all-advertisements')">广告管理</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <div class="avatar-area"><img :src="avatar" class="user-avatar" @click="router.push('/home/dashboard')" /></div>
        <div class="logout-area" @click="logout"><el-icon><SwitchButton /></el-icon></div>
      </div>
    </div>
  </header>

<!--  &lt;!&ndash; 子导航固定在 header 底部 &ndash;&gt;-->
<!--  <div v-if="showSubNav" class="sub-nav">-->
<!--    <div class="nav-item" @click="router.push('/home/all-products')">全部</div>-->
<!--    <div class="nav-item" @click="router.push('/home/all-columns')">栏目</div>-->
<!--    <div class="nav-item" @click="router.push('/home/rankings')">热销榜</div>-->
<!--  </div>-->

  <!-- 页面内容 -->
  <div class="page-content">
    <slot></slot>
  </div>
</template>

<style scoped>
/* Header */
.detective-header {
  position: fixed; top: 0; left: 0; width: 100%; z-index: 2000;
  background: rgba(18,18,18,0.75); backdrop-filter: blur(14px);
  transition: all 0.25s ease; padding: 22px 40px;
  box-shadow: 0 4px 18px rgba(0,0,0,0.42);
}
.detective-header.shrink { padding: 10px 40px; background: rgba(18,18,18,0.6); backdrop-filter: blur(8px); }

.header-inner {
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 改为靠左 */
  gap: 100px; /* 左右间距 */
}
.logo { font-size:22px; font-weight:600; color:#e7d3a0; letter-spacing:2px; cursor:pointer; user-select:none; transition: all .25s; }
.detective-header.shrink .logo { font-size:18px; opacity:0.85; }

.search-wrapper { flex:1; max-width:540px; margin-left:40px; position:relative; }
.search-bar { width:100%; padding:10px 14px; border-radius:14px; border:none;
  background: rgba(255,255,255,0.08); color:#efece6; outline:none; backdrop-filter: blur(12px);
  transition: all .3s; }
.detective-header.shrink .search-bar { transform: scale(0.92); opacity:0.9; }

/* 下拉补全 */
.suggestions-list { position:absolute; top:44px; left:0; right:0; background: rgba(10,10,10,0.9);
  border-radius:8px; max-height:260px; overflow-y:auto; z-index:3000; padding:6px 0; box-shadow:0 8px 30px rgba(0,0,0,0.5);}
.suggestion-item { padding:10px 14px; color:#efece6; cursor:pointer; }
.suggestion-item:hover { background: rgba(198,166,103,0.12); color:#fff; }

/* 右侧 */
.right-area { display:flex; align-items:center; gap:14px; margin-left: -20px;}
.role-tag { background:transparent; color:#d7caa8; border-color:transparent; font-weight:600; }
.icon-group { display:flex; gap:10px; align-items:center; }
.icon-btn { width:44px;height:44px;border-radius:10px;display:flex;align-items:center;justify-content:center;background: rgba(255,255,255,0.03);color:#efece6;cursor:pointer;transition: all .15s; }
.icon-btn:hover { background: rgba(198,166,103,0.12); transform: translateY(-2px); }

.avatar-area .user-avatar { width:40px;height:40px;border-radius:50%;object-fit:cover;border:2px solid rgba(255,255,255,0.06);cursor:pointer;transition: all .12s; box-shadow:0 2px 8px rgba(0,0,0,0.25);}
.avatar-area .user-avatar:hover { transform: translateY(-2px) scale(1.06); box-shadow:0 8px 20px rgba(198,166,103,0.14); }

.logout-area {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #efece6;
  border-radius: 8px;
  flex-shrink: 0;       /* 防止被挤压 */
}
.logout-area:hover { background: rgba(255,255,255,0.03); }

/* 子导航固定在 header 底部 */
.sub-nav {
  position: fixed; top: 80px; left:0; width:100%; display:flex; justify-content:center;
  gap:30px; background-color:#d7caa8; padding:10px 0; z-index:1900;
  box-shadow:0 1px 3px rgba(0,0,0,0.12);
}
.nav-item { padding:6px 14px; color:#3b3225; font-weight:600; font-size:15px; cursor:pointer; border-radius:6px; transition:.2s; }
.nav-item:hover { background-color:rgba(59,50,37,0.95); color:#fff; box-shadow:0 6px 18px rgba(198,166,103,0.12); }

/* 页面内容预留 Header + Sub-nav 高度 */
.page-content { padding-top: 80px; } /* header 80 + sub-nav 52 */


</style>
