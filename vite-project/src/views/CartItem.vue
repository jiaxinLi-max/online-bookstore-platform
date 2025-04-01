<template>
  <div class="cart-view">
    <h1>购物车</h1>
    <el-card v-for="item in cartItems" :key="item.cartItemId" class="cart-item">
      <CartItem :item="item" />
    </el-card>
    <div class="cart-summary">
      <p>总价: ¥{{ totalAmount }}</p>
      <el-button type="primary" @click="checkout">结算</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { getCartItems, updateCartItemQuantity, removeCartItem } from '@/api/cart.ts';
import CartItem from '@/components/CartItem.vue';

export default defineComponent({
  components: {
    CartItem,
  },
  setup() {
    const cartItems = ref<CartItem[]>([]);
    const totalAmount = ref(0);

    const fetchCartItems = async () => {
      const response = await getCartItems();
      cartItems.value = response.data.items;
      totalAmount.value = response.data.totalAmount;
    };

    const updateQuantity = async (cartItemId: string, quantity: number) => {
      await updateCartItemQuantity(cartItemId, quantity);
      fetchCartItems();
    };

    const removeItem = async (cartItemId: string) => {
      await removeCartItem(cartItemId);
      fetchCartItems();
    };

    const checkout = () => {
      // 调用结算 API
    };

    onMounted(fetchCartItems);

    return {
      cartItems,
      totalAmount,
      updateQuantity,
      removeItem,
      checkout,
    };
  },
});
</script>

<style scoped>
.cart-view {
  padding: 20px;
}
.cart-item {
  margin-bottom: 10px;
}
.cart-summary {
  margin-top: 20px;
}
</style>