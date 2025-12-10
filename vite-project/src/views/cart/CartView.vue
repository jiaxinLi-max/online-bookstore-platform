<template>
  <el-main class="cart-page">

    <!-- 顶部全选栏 -->
    <div class="cart-header">
      <el-checkbox v-model="checkAll" @change="toggleCheckAll">全选</el-checkbox>
      <div class="cart-header-right">
        已选 {{ selectedProducts.length }} 件
      </div>
    </div>

    <!-- 主体两栏布局 -->
    <div class="cart-layout">

      <!-- 左侧商品列表 -->
      <div class="cart-left">
        <div
            class="cart-item-card"
            v-for="product in products"
            :key="product.cartItemId"
        >
          <el-checkbox
              v-model="selectedProducts"
              :label="product.cartItemId"
              class="checkbox"
          />

          <img class="item-img" :src="product.cover" />

          <div class="item-info">
            <div class="item-title">{{ product.title }}</div>
            <div class="item-price">￥{{ product.price }}</div>
          </div>

          <!-- 数量选择器 -->
          <div class="quantity-box">
            <div class="qty-btn" @click="updateQuantity(product.cartItemId, product.quantity - 1)">−</div>
            <div class="qty-number">{{ product.quantity }}</div>
            <div class="qty-btn" @click="updateQuantity(product.cartItemId, product.quantity + 1)">＋</div>
          </div>

          <div class="delete-btn" @click="removeFromCart(userIdNumber, product.cartItemId)">删除</div>
        </div>
      </div>

      <!-- 右侧结算栏 -->
      <div class="cart-right">
        <div class="checkout-panel">
          <div class="checkout-title">订单摘要</div>

          <div class="checkout-row">
            <span>已选商品：</span>
            <span>{{ selectedProducts.length }} 件</span>
          </div>

          <div class="checkout-row total">
            <span>总金额：</span>
            <span class="price">￥{{ totalPrice.toFixed(2) }}</span>
          </div>

          <el-button
              type="primary"
              class="checkout-btn"
              :disabled="selectedProducts.length === 0"
              @click="handleCheckout"
          >
            结算
          </el-button>
        </div>
      </div>

    </div>
    <el-dialog
        v-model="dialogVisible"
        title="填写收货信息"
        width="480px"
    >
      <!-- 表单区 -->
      <div class="order-form">
        <label>收货人</label>
        <el-input v-model="name" />

        <label>手机号</label>
        <el-input v-model="telephone" />

        <label>地址</label>
        <el-input v-model="address" type="textarea" />
      </div>

      <template #footer>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" class="red-btn" :disabled="!ableToOrder" @click="generateOrder">
          提交订单
        </el-button>
      </template>
    </el-dialog>

  </el-main>
</template>



<script lang="ts">
import { computed, onMounted, ref,watch } from "vue";
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

    // 全选状态
    const checkAll = ref(false);
    const indeterminate = ref(false);

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

        if (cartRes.data.code == '200' && Array.isArray(cartRes.data.data.items)) {
          products.value = cartRes.data.data.items;
          console.log(products.value);
        } else { console.error("获取购物车数据失败"); }

        if (userRes.data.code == '200' && Array.isArray(userRes.data.data.addressBook)) {
          addressBook.value = userRes.data.data.addressBook;
        }
      } catch (error) { console.error("获取初始数据失败:", error); }
    }

    const removeFromCart = async (userId: number, cartItemId: number) => {
      try {
        const res = await removeItemFromCart(userId, cartItemId);
        if (res.data.code == '200') {
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


    // 切换全选/取消全选
    const toggleCheckAll = (value: boolean) => {
      if (value) {
        selectedProducts.value = products.value.map(p => p.cartItemId);
      } else {
        selectedProducts.value = [];
      }
    };

    // 监听单个勾选，更新全选状态
    watch(selectedProducts, (val) => {
      const total = products.value.length;
      const checked = val.length;
      checkAll.value = checked === total && total > 0;
      indeterminate.value = checked > 0 && checked < total;
    });


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
      toggleCheckAll,
      checkAll,
    };
  },
};
</script>

<style>


/* --- 图片 --- */
.item-image img {
  width: 88px;
  height: 88px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #ddd;
}

/* --- 文本信息 --- */
.item-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-title {
  font-size: 17px;
  font-weight: 600;
  color: #2b2b2b;
}

.item-price {
  font-size: 15px;
  color: #d9534f;
  font-weight: 700;
}



/* --- 弹窗表单 --- */
.order-form label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #444;
}


/* 整体页面 */
.cart-page {
  padding: 24px;
  min-height: 100vh;
  background: #f5f5f7;
}

/* 左右布局 */
.cart-layout {
  display: flex;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 左边 70% */
.cart-left {
  flex: 1;
}

/* 右边固定栏 30% */
.cart-right {
  width: 320px;
  position: sticky;
  top: 80px; /* 跟随滚动 不遮挡导航 */
  align-self: flex-start;
}

/* --- 右侧结算框 --- */


.checkout-row {
  font-size: 15px;
  color: #555;
  margin-bottom: 6px;
}

/* 按钮 */
.checkout-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
  border-radius: 6px;
}

.cart-page {
  background: #fafafa;
  padding: 30px 0;
}

/* 顶部 全选 */
.cart-header {
  width: 85%;
  margin: 0 auto 20px;
  display: flex;
  justify-content: space-between;
  font-size: 15px;
}

/* 主体布局 */
.cart-layout {
  width: 85%;
  margin: 0 auto;
  display: flex;
  gap: 30px; /* 左右留白！！ */
}

/* 左侧列表 */
.cart-left {
  flex: 1;
}

/* 商品卡片 */
.cart-item-card {
  display: flex;
  align-items: center;
  gap: 15px;
  background: white;
  border-radius: 16px;
  padding: 18px;
  margin-bottom: 16px;
  box-shadow: 0 3px 10px rgba(0,0,0,0.06);
}

.item-img {
  width: 90px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.item-price {
  margin-top: 8px;
  font-size: 16px;
  color: #ff6b6b;
  font-weight: bold;
}

/* 数量输入器（仿淘宝） */
.quantity-box {
  display: flex;
  border: 1px solid #d9d9d9;
  border-radius: 10px;
  overflow: hidden;
  width: 110px;
  height: 36px;
}

.qty-btn {
  width: 36px;
  text-align: center;
  line-height: 36px;
  cursor: pointer;
  user-select: none;
  background: #f6f6f6;
}

.qty-btn:hover {
  background: #e8e8e8;
}

.qty-number {
  flex: 1;
  text-align: center;
  line-height: 36px;
  background: white;
}

/* 删除按钮 */
.delete-btn {
  color: #888;
  cursor: pointer;
}

.delete-btn:hover {
  color: #ff4d4f;
}

/* 右侧结算栏 */
.cart-right {
  width: 300px;
  flex-shrink: 0;
}

.checkout-panel {
  background: white;
  padding: 24px;
  border-radius: 18px;
  box-shadow: 0 3px 12px rgba(0,0,0,0.08);
  position: sticky;
  top: 40px;
}

.checkout-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.checkout-row {
  display: flex;
  justify-content: space-between;
  font-size: 15px;
  margin: 12px 0;
}

.checkout-row.total {
  font-size: 17px;
  font-weight: 600;
}

.checkout-row .price {
  color: #ff6b6b;
}

.checkout-btn {
  width: 100%;
  margin-top: 18px;
  height: 40px;
  border-radius: 10px;
  background-color: #ff6b6b !important;
  border-color: #ff6b6b !important;
  color: white !important;
}

/* 优雅弹窗整体 */
.el-dialog {
  border-radius: 18px !important;
  padding-bottom: 10px;
  background: #ffffff;
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.15);
  animation: popup-fade 0.25s ease-out;
}

/* 弹出动画 */
@keyframes popup-fade {
  from {
    opacity: 0;
    transform: scale(0.96);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 标题样式 */
.el-dialog__header {
  margin: 0;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.el-dialog__title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

/* 内容区域 */
.el-dialog__body {
  padding: 20px 26px 10px 26px;
}

/* 表单整体 */
.order-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-form label {
  font-size: 14px;
  font-weight: 500;
  color: #555;
  margin-bottom: 4px;
}

/* input 统一美化 */
.order-form .el-input,
.order-form .el-textarea {
  width: 100%;
}

.el-input__wrapper,
.el-textarea__inner {
  border-radius: 10px !important;
}

/* 底部按钮区 */
.el-dialog__footer {
  padding: 14px 24px 20px;
  border-top: 1px solid #f0f0f0;
}

.el-dialog__footer .el-button {
  border-radius: 8px;
  padding: 8px 20px;
}

/* 提交按钮优雅颜色 */
.order-confirmation .el-button--primary {
  background-color: #ff6b6b !important;
  border-color: #ff6b6b !important;
}

.order-confirmation .el-button--primary:hover {
  background-color: #ff5252 !important;
}

.red-btn {
  background-color: #ff6b6b !important;
  border-color: #ff6b6b !important;
  color: white !important;
}

.red-btn:hover {
  background-color: #ff5252 !important;
  border-color: #ff5252 !important;
}


</style>