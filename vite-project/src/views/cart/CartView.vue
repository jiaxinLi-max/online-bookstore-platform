
<!--<template>-->
<!--  <el-main class="product-list bgimage">-->
<!--    <el-card-->
<!--        v-for="product in products"-->
<!--        :key="product.cartItemId"-->
<!--    class="product-card"-->
<!--    >-->
<!--    <div class="product-image">-->
<!--      <img :src='product.cover' alt="Product Cover" class="product-image-style" />-->
<!--    </div>-->
<!--    <h3>{{ product.title }}</h3>-->
<!--    <p>点击查看详情</p>-->
<!--      <div class="quantity-controls">-->
<!--        <el-button-->
<!--            @click="updateQuantity(product.cartItemId, product.quantity - 1)"-->
<!--            :disabled="product.quantity <= 1"-->
<!--            size="mini"-->
<!--        >减少</el-button>-->

<!--        <span>{{ product.quantity }}</span>-->

<!--        <el-button-->
<!--            @click="updateQuantity(product.cartItemId, product.quantity + 1)"-->
<!--        size="mini"-->
<!--        >增加</el-button>-->
<!--      </div>-->
<!--    <el-button type="danger" @click="removeFromCart(userIdNumber, product.cartItemId)">删除</el-button> &lt;!&ndash; 删除按钮 &ndash;&gt;-->
<!--    </el-card>-->
<!--    <div class="button-group">-->
<!--      <el-button type="primary" @click="BackToAllProducts" class="go-to-products-button">返回所有商品</el-button>-->
<!--      <el-button type="primary" @click="dialogVisible = true" class="order-button">结算</el-button>-->
<!--    </div>-->
<!--    <el-dialog-->
<!--        title="填写订单信息"-->
<!--        class="order-form"-->
<!--        v-model="dialogVisible"-->
<!--        width="500px"-->
<!--        :before-close="handleCancel"-->
<!--    >-->
<!--      <el-form>-->
<!--        <el-row>-->
<!--          <el-form-item>-->
<!--            <label for="username">收货姓名</label>-->
<!--            <el-input id="username" v-model="name" placeholder="请输入姓名" />-->
<!--          </el-form-item>-->
<!--        </el-row>-->

<!--        <el-row>-->
<!--          <el-form-item>-->
<!--            <label v-if="!hasTelInput" for="tel">收货手机号</label>-->
<!--            <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>-->
<!--            <label v-else for="tel">收货手机号</label>-->
<!--            <el-input id="tel" v-model="telephone" :class="{'error-warn-input': (hasTelInput && !telLegal)}" placeholder="请输入手机号"/>-->
<!--          </el-form-item>-->
<!--        </el-row>-->

<!--        <el-row>-->
<!--          <el-form-item>-->
<!--            <label for="location">收货地址</label>-->
<!--            <el-input id="location" v-model="location" placeholder="请输入地址"/>-->
<!--          </el-form-item>-->
<!--        </el-row>-->
<!--      </el-form>-->

<!--      <el-button @click="handleCancel">取消</el-button>-->
<!--      <el-button type="primary" @click="generateOrder" :disabled="!ableToOrder">支付</el-button>-->
<!--    </el-dialog>-->
<!--  </el-main>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--import {computed, onMounted, ref} from 'vue';-->
<!--import { getUserInfo } from "../../api/user.ts";-->
<!--import {Cart, getCartItems, removeItemFromCart, updateCartItemQuantity, placeOrder} from '../../api/cart.ts';-->
<!--import {ElMessage} from "element-plus";-->
<!--import { useRouter } from 'vue-router'; // 引入路由相关-->
<!--// 定义组件-->
<!--export default {-->
<!--  setup() {-->
<!--    const products = ref<Cart[]>([]); // 定义 products 为响应式变量-->
<!--    const userId = sessionStorage.getItem('userId'); // 获取用户 ID-->
<!--    const userIdNumber = Number(userId); // 将用户 ID 转换为数字-->
<!--    const router = useRouter(); // 获取路由实例-->

<!--    const dialogVisible = ref(false);-->

<!--    const name = ref<string>('');-->
<!--    const telephone = ref<string>('');-->
<!--    const location = ref<string>('');-->

<!--    const hasTelInput = computed(() => telephone.value !== '');-->
<!--    const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;-->
<!--    const telLegal = computed(() => chinaMobileRegex.test(telephone.value));-->
<!--    const hasNameInput = computed(() => name.value !== '');-->
<!--    const hasLocationInput = computed(() => location.value !== '');-->
<!--    const ableToOrder = computed(() => {-->
<!--      return hasTelInput.value && telLegal.value && hasNameInput.value && hasLocationInput.value;-->
<!--    });-->

<!--    let level = 0;-->

<!--    // 获取购物车中的商品-->
<!--    async function getAllInCart() {-->
<!--      if (!userId) {-->
<!--        console.error('用户未登录');-->
<!--        return;-->
<!--      }-->
<!--      try {-->
<!--        const res = await getCartItems(userIdNumber);-->
<!--        console.log("getAllInCart", res.data.data);-->
<!--        // 确认响应数据格式-->
<!--        if (res.data.data && Array.isArray(res.data.data.items)) {-->
<!--          products.value = res.data.data.items; // 更新产品列表-->
<!--        } else {-->
<!--          console.error('获取数据失败：响应格式不符合预期');-->
<!--        }-->
<!--      } catch (error) {-->
<!--        console.error('获取购物车列表失败:', error);-->
<!--      }-->
<!--    }-->

<!--    // 删除购物车中的商品-->
<!--    const removeFromCart = async (userId: number, cartItemId: number) => {-->
<!--      try {-->
<!--        const res = await removeItemFromCart(userId, cartItemId); // 调用 API 删除商品-->
<!--        if (res.data.code === '200') { // 检查响应状态-->
<!--          // 删除成功后重新获取购物车商品列表-->
<!--          await getAllInCart();-->
<!--        } else {-->
<!--          console.error('删除商品失败：响应格式不符合预期');-->
<!--        }-->
<!--      } catch (error) {-->
<!--        console.error('删除商品失败:', error);-->
<!--      }-->
<!--    };-->

<!--    async function getLevel() {-->
<!--      const res = await getUserInfo(Number(userId));-->
<!--      if (res.data.code === '200') {-->
<!--        level = res.data.data.grade;-->
<!--      }-->
<!--    }-->

<!--    // 修改购物车中商品的数量-->
<!--    // const updateQuantity = async (cartItemId: number, newQuantity: number) => {-->
<!--    //   if (newQuantity < 1) return; // 防止数量小于 1-->
<!--    //   try {-->
<!--    //     console.log("更新商品 ID:", cartItemId);-->
<!--    //     const res = await updateCartItemQuantity(userIdNumber, cartItemId, newQuantity); // 调用更新数量的 API-->
<!--    //     console.log("update", res);-->
<!--    //     if (res.data.code === '200') { // 检查响应状态-->
<!--    //       console.log("updateYes");-->
<!--    //       await getAllInCart(); // 更新商品列表-->
<!--    //     } else {-->
<!--    //       console.error('修改数量失败:', res.data.msg);-->
<!--    //     }-->
<!--    //   } catch (error) {-->
<!--    //     console.error('修改数量失败:', error);-->
<!--    //   }-->
<!--    // };-->
<!--    //不是json-->
<!--    const updateQuantity = async (cartItemId: number, newQuantity: number) => {-->
<!--      if (newQuantity < 1) return; // 防止数量小于 1-->
<!--      try {-->
<!--        console.log("更新商品 ID:", cartItemId);-->
<!--        const res = await updateCartItemQuantity(userIdNumber, cartItemId, newQuantity); // 调用更新数量的 API-->
<!--        console.log("update", res);-->
<!--        if (res.data.code === '200') { // 检查响应状态-->
<!--          console.log("updateYes");-->
<!--          await getAllInCart(); // 更新商品列表-->
<!--        }-->
<!--        // else {-->
<!--        //   console.error('修改数量失败:', res.data.msg);-->
<!--        // }-->
<!--        else  {-->
<!--          console.error('修改数量失败:', res.data.msg);-->
<!--          ElMessage({-->
<!--            message: res.data.msg,-->
<!--            type: 'error',-->
<!--            center: true,-->
<!--          })-->
<!--        }-->
<!--      } catch (error) {-->
<!--        console.error('修改数量失败:', error);-->
<!--      }-->
<!--    };-->

<!--    const BackToAllProducts = () => {-->
<!--      router.push('/home/all-products'); // 使用路由跳转-->
<!--    };-->

<!--    const generateOrder = async () => {-->
<!--      let orderId: number;-->
<!--      let totalAmount: number = 0;-->
<!--      let realAmount: number = 0;-->
<!--      let createTime: string;-->
<!--      const cartIds: number[] = [];-->

<!--      for (const product of products.value) {-->
<!--        cartIds.push(product.cartItemId);-->
<!--      }-->
<!--      console.log("商品信息:", cartIds);-->

<!--      if (!userId) {-->
<!--        console.error('用户未登录, 支付订单时');-->
<!--        return;-->
<!--      }-->

<!--      const formData = {-->
<!--        userId: Number(userId),-->
<!--        cartItemIds: cartIds,-->
<!--        shipping_address: {-->
<!--          name: name.value,-->
<!--          telephone: telephone.value,-->
<!--          address: location.value,-->
<!--        },-->
<!--        payment_method: 'ALIPAY',-->
<!--      }-->

<!--      try {-->
<!--        const res = await placeOrder(formData);-->
<!--        if (res.data.code === '200') {-->
<!--          orderId = res.data.data.id;-->
<!--          totalAmount = res.data.data.totalAmount;-->
<!--          realAmount = res.data.data.realAmount;-->
<!--          createTime = res.data.data.createTime;-->
<!--          console.log("订单ID:", res.data.data.id);-->
<!--          console.log("实际金额:", realAmount);-->

<!--          await router.push({-->
<!--            name: 'Order',-->
<!--            params: {-->
<!--              orderId: orderId,-->
<!--              totalAmount: totalAmount,-->
<!--              realAmount: realAmount,-->
<!--              createTime: createTime,-->
<!--            }-->
<!--          });-->
<!--        } else {-->
<!--          console.error('获取订单ID失败');-->
<!--        }-->
<!--      } catch (error) {-->
<!--        console.error('生成订单失败:', error);-->
<!--      }-->
<!--    };-->

<!--    const handleCancel = () => {-->
<!--      dialogVisible.value = false;-->
<!--    }-->

<!--    // 在组件挂载时获取购物车中的商品-->
<!--    onMounted(() => {-->
<!--      getAllInCart();-->
<!--      getLevel();-->
<!--    });-->

<!--    // 返回响应式变量和方法-->
<!--    return {-->
<!--      products,-->
<!--      updateQuantity,-->
<!--      removeFromCart,-->
<!--      BackToAllProducts,-->
<!--      userIdNumber,-->
<!--      generateOrder,-->
<!--      handleCancel,-->
<!--      dialogVisible,-->
<!--      name,-->
<!--      telephone,-->
<!--      location,-->
<!--      hasTelInput,-->
<!--      telLegal,-->
<!--      ableToOrder-->
<!--    };-->
<!--  }-->
<!--};-->
<!--</script>-->

<!--<style scoped>-->
<!--.product-item {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  margin-bottom: 10px;-->
<!--}-->

<!--.product-item img {-->
<!--  width: 100px;-->
<!--  height: 100px;-->
<!--  margin-right: 10px;-->
<!--}-->

<!--.product-image-style {-->
<!--  width: 200px; /* 设置图片宽度 */-->
<!--  height: 100px; /* 设置图片高度 */-->
<!--  object-fit: cover; /* 确保图片填充整个区域 */-->
<!--  border-radius: 5px; /* 可选：添加圆角效果 */-->
<!--}-->

<!--.product-card {-->
<!--  width: 500px; /* 设置卡片宽度 */-->
<!--  max-height: 300px; /* 设置卡片最大高度 */-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  align-items: center; /* 水平方向居中 */-->
<!--  justify-content: center; /* 垂直方向居中 */-->
<!--  text-align: center;-->
<!--  padding: 10px;-->
<!--  box-sizing: border-box;-->
<!--  margin: 0 auto; /* 添加这行使卡片在父元素中水平居中 */-->
<!--}-->

<!--.button-group {-->
<!--  display: flex;-->
<!--  flex-direction: row;-->
<!--  align-items: center;-->
<!--  justify-content: space-between;-->
<!--  padding: 0 20px;-->
<!--}-->

<!--.order-form {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 20px;-->
<!--  margin-top: 10px;-->
<!--}-->
<!--.bgimage {-->

<!--  background-image: url("../../assets/780.jpg");-->
<!--  background-position: center top;-->
<!--  background-size: 1500px auto; /* 或根据需求调整为 cover 或百分比 */-->
<!--  background-repeat: no-repeat;-->
<!--  background-attachment: fixed; /* 关键属性：背景固定 */-->
<!--  background-color: #7b6b4d; /* 深羊驼色兜底 */-->
<!--  min-height: 100vh;-->
<!--}-->
<!--</style>-->
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
            <!-- 空插槽内容，避免显示 label -->
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

    <!-- 底部固定结算栏 -->
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

    <!-- 订单对话框 -->
        <el-dialog
            title="填写订单信息"
            class="order-form"
            v-model="dialogVisible"
            width="500px"
            :before-close="handleCancel"
        >
          <el-form>
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
                <label for="location">收货地址</label>
                <el-input id="location" v-model="location" placeholder="请输入地址"/>
              </el-form-item>
            </el-row>
          </el-form>

          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="generateOrder" :disabled="!ableToOrder">支付</el-button>
        </el-dialog>
  </el-main>
</template>

<script lang="ts">
import { computed, onMounted, ref } from "vue";
import { getUserInfo } from "../../api/user.ts";
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
    const location = ref<string>("");

    const hasTelInput = computed(() => telephone.value !== "");
    const chinaMobileRegex =
        /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;
    const telLegal = computed(() => chinaMobileRegex.test(telephone.value));
    const hasNameInput = computed(() => name.value !== "");
    const hasLocationInput = computed(() => location.value !== "");
    const ableToOrder = computed(() => {
      return (
          hasTelInput.value &&
          telLegal.value &&
          hasNameInput.value &&
          hasLocationInput.value
      );
    });

    const selectedProducts = ref<number[]>([]); // 选中的商品 cartItemId 数组

    async function getAllInCart() {
      if (!userId) {
        console.error("用户未登录");
        return;
      }
      try {
        const res = await getCartItems(userIdNumber);
        if (res.data.data && Array.isArray(res.data.data.items)) {
          products.value = res.data.data.items;
        } else {
          console.error("获取数据失败：响应格式不符合预期");
        }
      } catch (error) {
        console.error("获取购物车列表失败:", error);
      }
    }

    const removeFromCart = async (userId: number, cartItemId: number) => {
      try {
        const res = await removeItemFromCart(userId, cartItemId);
        if (res.data.code === "200") {
          await getAllInCart();
          // 删除后从选中中移除
          selectedProducts.value = selectedProducts.value.filter(
              (id) => id !== cartItemId
          );
        } else {
          console.error("删除商品失败：响应格式不符合预期");
        }
      } catch (error) {
        console.error("删除商品失败:", error);
      }
    };

    const updateQuantity = async (cartItemId: number, newQuantity: number) => {
      if (newQuantity < 1) return;
      try {
        const res = await updateCartItemQuantity(
            userIdNumber,
            cartItemId,
            newQuantity
        );
        if (res.data.code === "200") {
          await getAllInCart();
        } else {
          ElMessage({
            message: res.data.msg,
            type: "error",
            center: true,
          });
        }
      } catch (error) {
        console.error("修改数量失败:", error);
      }
    };

    const BackToAllProducts = () => {
      router.push("/home/all-products");
    };

    const totalPrice = computed(() => {
      return products.value
          .filter((p) => selectedProducts.value.includes(p.cartItemId))
          .reduce((acc, p) => acc + p.price * p.quantity, 0);
    });

    const handleCheckout = () => {
      if (selectedProducts.value.length === 0) return;
      // 过滤出选中的商品，覆盖当前 products 只显示结算商品，或直接调用结算逻辑
      products.value = products.value.filter((p) =>
          selectedProducts.value.includes(p.cartItemId)
      );
      dialogVisible.value = true;
    };


    const generateOrder = async () => {
      let orderId: number;
      let totalAmount: number = 0;
      let realAmount: number = 0;
      let createTime: string;
      const cartIds: number[] = [];

      for (const product of products.value) {
        cartIds.push(product.cartItemId);
      }
      console.log("商品信息:", cartIds);

      if (!userId) {
        console.error('用户未登录, 支付订单时');
        return;
      }

      const formData = {
        userId: Number(userId),
        cartItemIds: cartIds,
        shipping_address: {
          name: name.value,
          telephone: telephone.value,
          address: location.value,
        },
        payment_method: 'ALIPAY',
      }

      try {
        const res = await placeOrder(formData);
        if (res.data.code === '200') {
          orderId = res.data.data.id;
          totalAmount = res.data.data.totalAmount;
          realAmount = res.data.data.realAmount;
          createTime = res.data.data.createTime;
          console.log("订单ID:", res.data.data.id);
          console.log("实际金额:", realAmount);

          await router.push({
            name: 'Order',
            params: {
              orderId: orderId,
              totalAmount: totalAmount,
              realAmount: realAmount,
              createTime: createTime,
            }
          });
        } else {
          console.error('获取订单ID失败');
        }
      } catch (error) {
        console.error('生成订单失败:', error);
      }
    };


    const handleCancel = () => {
      dialogVisible.value = false;
    };

    onMounted(() => {
      getAllInCart();
    });

    return {
      products,
      updateQuantity,
      removeFromCart,
      BackToAllProducts,
      userIdNumber,
      generateOrder,
      handleCancel,
      dialogVisible,
      name,
      telephone,
      location,
      hasTelInput,
      telLegal,
      hasNameInput,
      hasLocationInput,
      ableToOrder,
      selectedProducts,
      totalPrice,
      handleCheckout,
    };
  },
};
</script>

<style >
.cart-container {
  padding: 20px;
  position: relative;
  min-height: 80vh;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-bottom: 100px;

  /* 新增：子元素垂直居中 */
  align-items: center;
}

.product-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.85);
  padding: 20px 24px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(4px);

  /* 新增限制最大宽度 */
  max-width: 900px;
  width: 100%; /* 宽度自适应，不超过 max-width */
}


/* 左侧区域：复选框 + 图片 + 商品信息 */
.left-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 商品图片 */
.item-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

/* 商品信息垂直排列 */
.item-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.item-price {
  font-size: 16px;
  color: #444;
}

/* 右侧区域：数量和删除按钮 */
.right-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 数量调节按钮 */
.item-quantity {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quantity-text {
  min-width: 24px;
  text-align: center;
  font-size: 16px;
}

/* 删除按钮对齐 */
.delete-btn {
  /* 保持默认即可 */
}

/* 底部结算栏 */
.checkout-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  box-shadow: 0 -1px 10px rgb(0 0 0 / 0.1);
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 100;
  border-top: 1px solid #eee;
}

.total-info {
  font-size: 16px;
  color: #333;
}

.total-price {
  color: #444;
  font-weight: 700;
  font-size: 18px;
}

.checkout-button {
  min-width: 120px;
  font-size: 16px;
}

/* 背景图 */
.bgimage {
  background-image: url("../../assets/780.jpg");
  background-position: center top;
  background-size: 1500px auto;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-color: #7b6b4d;
  min-height: 100vh;
}

</style>
