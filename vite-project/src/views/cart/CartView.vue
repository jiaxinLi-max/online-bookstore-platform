<template>
  <el-main class="cart-container bgimage">
    <div class="product-list">
      <div
          v-for="product in products"
          :key="product.cartItemId"
          class="product-item"
      >
        <div class="left-section">
          <el-checkbox
              v-model="selectedProducts"
              :label="product.cartItemId"
              class="item-checkbox"
          >
          </el-checkbox>

          <div class="item-image">
            <img :src="product.cover" alt="商品封面" />
          </div>

          <div class="item-info">
            <div class="item-title">{{ product.title }}</div>
            <div class="item-price">￥{{ product.price }}</div>
          </div>
        </div>

        <div class="right-section">
          <div class="item-quantity">
            <el-button
                @click="updateQuantity(product.cartItemId, product.quantity - 1)"
                :disabled="product.quantity <= 1"
                size="small"
                icon="el-icon-minus"
            >-</el-button>
            <span class="quantity-text">{{ product.quantity }}</span>
            <el-button
                @click="updateQuantity(product.cartItemId, product.quantity + 1)"
                size="small"
                icon="el-icon-plus"
            >+</el-button>
          </div>

          <el-button
              type="danger"
              size="small"
              @click="removeFromCart(userIdNumber, product.cartItemId)"
              class="delete-btn"
          >
            删除
          </el-button>
        </div>
      </div>
    </div>

    <div class="checkout-bar">
      <div class="total-info">
        已选 {{ selectedProducts.length }} 件商品，
        合计：￥<span class="total-price">{{ totalPrice.toFixed(2) }}</span>
      </div>
      <el-button
          type="primary"
          :disabled="selectedProducts.length === 0"
          @click="handleCheckout"
          class="checkout-button"
      >结算所选</el-button>
    </div>

    <el-dialog
        title="填写订单信息"
        class="order-form"
        v-model="dialogVisible"
        width="500px"
        :before-close="handleCancel"
    >
      <el-form>
        <el-form-item v-if="addressBook.length > 0">
          <label>从地址簿选择</label>
          <el-select
              v-model="selectedAddressIndex"
              placeholder="请选择一个收货地址"
              @change="onAddressSelect"
              style="width: 100%;"
              clearable
          >
            <el-option
                v-for="(addr, index) in addressBook"
                :key="index"
                :label="`${addr.name} - ${addr.phone} - ${addr.address}`"
                :value="index"
            />
          </el-select>
        </el-form-item>

        <el-row>
          <el-form-item>
            <label for="username">收货姓名</label>
            <el-input id="username" v-model="name" placeholder="请输入姓名" />
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item>
            <label v-if="!hasTelInput" for="tel">收货手机号</label>
            <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>
            <label v-else for="tel">收货手机号</label>
            <el-input id="tel" v-model="telephone" :class="{'error-warn-input': (hasTelInput && !telLegal)}" placeholder="请输入手机号"/>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item>
            <label for="address">收货地址</label>
            <el-input id="address" v-model="address" placeholder="请输入地址"/>
          </el-form-item>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="generateOrder" :disabled="!ableToOrder">支付</el-button>
      </template>
    </el-dialog>
  </el-main>
</template>

<script lang="ts">
import { computed, onMounted, ref } from "vue";
import { getUserInfo, type Address } from "../../api/user.ts";
import {
  Cart,
  getCartItems,
  removeItemFromCart,
  updateCartItemQuantity,
  placeOrder,
} from "../../api/cart.ts";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

export default {
  setup() {
    const products = ref<Cart[]>([]);
    const userId = sessionStorage.getItem("userId");
    const userIdNumber = Number(userId);
    const router = useRouter();
    const dialogVisible = ref(false);

    const name = ref<string>("");
    const telephone = ref<string>("");
    const address = ref<string>("");

    const addressBook = ref<Address[]>([]);
    const selectedAddressIndex = ref<number | null>(null);

    const hasTelInput = computed(() => telephone.value !== "");
    const chinaMobileRegex =
        /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;
    const telLegal = computed(() => chinaMobileRegex.test(telephone.value));
    const hasNameInput = computed(() => name.value !== "");
    const hasAddressInput = computed(() => address.value !== "");
    const ableToOrder = computed(() => {
      return (
          hasTelInput.value &&
          telLegal.value &&
          hasNameInput.value &&
          hasAddressInput.value
      );
    });

    const selectedProducts = ref<number[]>([]);

    async function fetchInitialData() {
      if (!userId) {
        console.error("用户未登录");
        return;
      }
      try {
        const [cartRes, userRes] = await Promise.all([
          getCartItems(userIdNumber),
          getUserInfo(userIdNumber)
        ]);

        if (cartRes.data.code == 200 && Array.isArray(cartRes.data.data.items)) {
          products.value = cartRes.data.data.items;
          console.log(product.value);
        } else { console.error("获取购物车数据失败"); }

        if (userRes.data.code == 200 && Array.isArray(userRes.data.data.addressBook)) {
          addressBook.value = userRes.data.data.addressBook;
        }
      } catch (error) { console.error("获取初始数据失败:", error); }
    }

    const removeFromCart = async (userId: number, cartItemId: number) => {
      try {
        const res = await removeItemFromCart(userId, cartItemId);
        if (res.data.code == 200) {
          await fetchInitialData();
          selectedProducts.value = selectedProducts.value.filter(id => id !== cartItemId);
        } else { console.error("删除商品失败"); }
      } catch (error) { console.error("删除商品失败:", error); }
    };

    const updateQuantity = async (cartItemId: number, newQuantity: number) => {
      if (newQuantity < 1) return;
      try {
        const res = await updateCartItemQuantity(userIdNumber, cartItemId, newQuantity);
        if (res.data.code == 200) {
          await fetchInitialData();
        } else { ElMessage.error(res.data.msg); }
      } catch (error) { console.error("修改数量失败:", error); }
    };

    const totalPrice = computed(() => {
      return products.value
          .filter((p) => selectedProducts.value.includes(p.cartItemId))
          .reduce((acc, p) => acc + p.price * p.quantity, 0);
    });

    const handleCheckout = () => {
      if (selectedProducts.value.length === 0) return;
      dialogVisible.value = true;
    };

    const generateOrder = async () => {
      if (!userId) { console.error('用户未登录, 支付订单时'); return; }

      const formData = {
        userId: Number(userId),
        cartItemIds: selectedProducts.value,
        shipping_address: {
          name: name.value,
          telephone: telephone.value, // 提交时使用 telephone
          address: address.value,
        },
        payment_method: 'ALIPAY',
      }

      try {
        const res = await placeOrder(formData);
        if (res.data.code === '200') {
          const { id, totalAmount, realAmount, createTime } = res.data.data;
          await router.push({
            name: 'Order',
            params: { orderId: id, totalAmount, realAmount, createTime }
          });
        } else { console.error('获取订单ID失败'); }
      } catch (error) { console.error('生成订单失败:', error); }
    };

    const handleCancel = () => {
      dialogVisible.value = false;
    };

    const onAddressSelect = (selectedIndex: number | '') => {
      if(typeof selectedIndex === 'number'){
        const selected = addressBook.value[selectedIndex];
        if (selected) {
          name.value = selected.name;
          telephone.value = selected.phone; // 从地址簿的 phone 赋值给表单的 telephone
          address.value = selected.address;
        }
      } else {
        name.value = '';
        telephone.value = '';
        address.value = '';
      }
    };

    onMounted(fetchInitialData);

    return {
      products,
      updateQuantity,
      removeFromCart,
      userIdNumber,
      generateOrder,
      handleCancel,
      dialogVisible,
      name,
      telephone,
      address,
      hasTelInput,
      telLegal,
      hasNameInput: hasNameInput,
      hasAddressInput: hasAddressInput,
      ableToOrder,
      selectedProducts,
      totalPrice,
      handleCheckout,
      addressBook,
      selectedAddressIndex,
      onAddressSelect,
    };
  },
};
</script>

<style>
.cart-container { padding: 20px; position: relative; min-height: 80vh; }
.product-list { display: flex; flex-direction: column; gap: 12px; padding-bottom: 100px; align-items: center; }
.product-item { display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.85); padding: 20px 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); backdrop-filter: blur(4px); max-width: 900px; width: 100%; }
.left-section { display: flex; align-items: center; gap: 12px; }
.item-image img { width: 80px; height: 80px; object-fit: cover; border-radius: 4px; }
.item-info { display: flex; flex-direction: column; justify-content: center; }
.item-title { font-size: 16px; font-weight: bold; color: #333; }
.item-price { font-size: 16px; color: #444; }
.right-section { display: flex; align-items: center; gap: 16px; }
.item-quantity { display: flex; align-items: center; gap: 8px; }
.quantity-text { min-width: 24px; text-align: center; font-size: 16px; }
.checkout-bar { position: fixed; bottom: 0; left: 0; right: 0; background-color: #fff; box-shadow: 0 -1px 10px rgb(0 0 0 / 0.1); padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; z-index: 100; border-top: 1px solid #eee; }
.total-info { font-size: 16px; color: #333; }
.total-price { color: #d9534f; font-weight: 700; font-size: 18px; }
.checkout-button { min-width: 120px; font-size: 16px; }
.bgimage { background-image: url("../../assets/780.jpg"); background-position: center top; background-size: 1500px auto; background-repeat: no-repeat; background-attachment: fixed; background-color: #7b6b4d; min-height: 100vh; }
.order-form label { display: block; margin-bottom: 5px; }
.error-warn { color: red; }
.error-warn-input { --el-input-focus-border-color: red; }
</style>