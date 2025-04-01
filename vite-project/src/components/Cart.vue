<!--<template>-->
<!--  <el-card class="cart-item">-->
<!--    <div class="item-info">-->
<!--      <img :src="item.cover" alt="商品封面" class="item-cover" />-->
<!--      <div class="item-details">-->
<!--        <h3>{{ item.title }}</h3>-->
<!--        <p>{{ item.description }}</p>-->
<!--        <p>单价: ¥{{ item.price }}</p>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="item-actions">-->
<!--      <el-input-number v-model="item.quantity" :min="1" @change="updateQuantity"></el-input-number>-->
<!--      <el-button type="danger" @click="removeItem">删除</el-button>-->
<!--    </div>-->
<!--  </el-card>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--import { defineComponent, PropType } from 'vue';-->
<!--import { CartItem } from '@/api/cart.ts';-->

<!--export default defineComponent({-->
<!--  props: {-->
<!--    item: {-->
<!--      type: Object as PropType<CartItem>,-->
<!--      required: true,-->
<!--    },-->
<!--  },-->
<!--  methods: {-->
<!--    updateQuantity() {-->
<!--      // 调用更新数量的 API-->
<!--    },-->
<!--    removeItem() {-->
<!--      // 调用删除商品的 API-->
<!--    },-->
<!--  },-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.cart-item {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--  padding: 10px;-->
<!--}-->
<!--.item-info {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--}-->
<!--.item-cover {-->
<!--  width: 100px;-->
<!--  height: auto;-->
<!--  margin-right: 10px;-->
<!--}-->
<!--.item-details {-->
<!--  flex: 1;-->
<!--}-->
<!--.item-actions {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--}-->
<!--</style>-->