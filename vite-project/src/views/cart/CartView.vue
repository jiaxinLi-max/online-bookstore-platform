<template>
  <div>
    <h2>购物车</h2>
    <div v-for="product in products" :key="product.productId" class="product-item">
      <img :src="product.cover" alt="商品封面" />
      <div>
        <h3>{{ product.title }}</h3>
        <p>价格: {{ product.price }}元</p>
        <input type="number" v-model="product.quantity" min="1" />
        <button @click="addToCart(product)">添加到购物车</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { Product } from '../../api/product.ts';
import { Cart,addCart } from '../../api/cart.ts';
export default defineComponent({
  setup() {
    const products = ref([]); // 假设从后端获取商品列表

    const addToCart = async (product) => {
      const userId = sessionStorage.getItem(); // 假设用户ID为1
      try {
        await addCart(userId, product.productId, product.quantity);
        // 提示用户添加成功
      } catch (error) {
        console.error('添加商品到购物车失败', error);
      }
    };

    // 在这里可以添加获取商品列表的逻辑

    return {
      products,
      addToCart
    };
  },
});
</script>

<style scoped>
.product-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.product-item img {
  width: 100px;
  height: 100px;
  margin-right: 10px;
}
</style>
