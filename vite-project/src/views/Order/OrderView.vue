<template>

</template>

<script lang="ts">
import { onMounted, ref } from 'vue';
import { Cart, getCartItems } from '../../api/cart.ts';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from 'vue-router'; // 引入路由相关

// 定义组件
export default {
  setup() {
    const products = ref<Cart[]>([]); // 定义 products 为响应式变量
    const userId = sessionStorage.getItem('userId'); // 获取用户 ID
    const userIdNumber = Number(userId); // 将用户 ID 转换为数字
    const router = useRouter(); // 获取路由实例

    async function getAllInCart() {
      if (!userId) {
        console.error('用户未登录');
        return;
      }
      try {
        const res = await getCartItems(userIdNumber);
        console.log("getAllInCart", res.data.data);
        // 确认响应数据格式
        if (res.data.data && Array.isArray(res.data.data.items)) {
          products.value = res.data.data.items; // 更新产品列表
        } else {
          console.error('获取数据失败：响应格式不符合预期');
        }
      } catch (error) {
        console.error('获取购物车列表失败:', error);
      }
    }

    onMounted(() => {
      getAllInCart();
    })

    return {
      products,
      userIdNumber,
    }
  }
};
</script>

<style scoped>

</style>