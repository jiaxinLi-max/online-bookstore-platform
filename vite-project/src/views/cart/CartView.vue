<!--<template>-->
<!--  <div>-->
<!--    <h2>购物车</h2>-->
<!--    <div v-for="product in products" :key="product.productId" class="product-item">-->
<!--      <img :src="product.cover" alt="商品封面" />-->
<!--      <div>-->
<!--        <h3>{{ product.title }}</h3>-->
<!--        <p>价格: {{ product.price }}元</p>-->
<!--        <input type="number" v-model="product.quantity" min="1" />-->
<!--        <button @click="addToCart(product)">添加到购物车</button>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--import { onMounted, ref } from 'vue';-->

<!--import { Cart,addCart,removeFromCart,getCartItems } from '../../api/cart.ts';-->

<!--const products = ref<Cart[]>([]);-->
<!--async function getAllInCart() {-->
<!--  const userId=sessionStorage.getItem('userId');-->
<!--  console.log("inCart",userId);-->
<!--  try {-->
<!--    const res = await getCartItems(userId);-->
<!--    console.log("getAllInCart",res);-->
<!--    if (res.data && Array.isArray(res.data.data)) {-->
<!--      products.value = res.data.data;-->
<!--      console.log(res.data);-->
<!--    } else {-->
<!--      console.error('获取数据失败：响应格式不符合预期');-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error('获取购物车列表失败:', error);-->
<!--  }-->
<!--}-->
<!--// 在组件挂载时获取商店数据-->
<!--onMounted(() => {-->
<!--  getAllInCart();-->
<!--});-->
<!--// export default defineComponent({-->
<!--//   setup() {-->
<!--//     const products = ref([]); // 假设从后端获取商品列表-->
<!--//-->
<!--//     const addToCart = async (product) => {-->
<!--//       const userId = sessionStorage.getItem(); // 假设用户ID为1-->
<!--//       try {-->
<!--//         await addCart(userId, product.productId, product.quantity);-->
<!--//         // 提示用户添加成功-->
<!--//       } catch (error) {-->
<!--//         console.error('添加商品到购物车失败', error);-->
<!--//       }-->
<!--//     };-->
<!--//-->
<!--//     // 在这里可以添加获取商品列表的逻辑-->
<!--//-->
<!--//     return {-->
<!--//       products,-->
<!--//       addToCart-->
<!--//     };-->
<!--//   },-->
<!--// });-->
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
<!--</style>-->





<!--<template>-->
<!--  <div>-->
<!--    <h2>购物车</h2>-->
<!--    <div v-for="product in products" :key="product.productId" class="product-item">-->
<!--      <img :src="product.cover" alt="商品封面" />-->
<!--      <div>-->
<!--        <h3>{{ product.title }}</h3>-->
<!--        <p>价格: {{ product.price }}元</p>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
<template>
  <el-main class="product-list bgimage">
    <el-card
        v-for="product in products"
        :key="product.cartItemId"
    class="product-card"
    >
    <div class="product-image">
      <img :src='product.cover' alt="Product Cover" class="product-image-style" />
    </div>
    <h3>{{ product.title }}</h3>
    <p>点击查看详情</p>
      <div class="quantity-controls">
        <el-button
            @click="updateQuantity(product.cartItemId, product.quantity - 1)"
            :disabled="product.quantity <= 1"
            size="mini"
        >减少</el-button>

        <span>{{ product.quantity }}</span>

        <el-button
            @click="updateQuantity(product.cartItemId, product.quantity + 1)"
        size="mini"
        >增加</el-button>
      </div>
    <el-button type="danger" @click="removeFromCart(userIdNumber, product.cartItemId)">删除</el-button> <!-- 删除按钮 -->
    </el-card>
    <div class="button-group">
      <el-button type="primary" @click="BackToAllProducts" class="go-to-products-button">返回所有商品</el-button>
      <el-button type="primary" @click="dialogVisible = true" class="order-button">结算</el-button>
    </div>
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
      <el-button type="primary" @click="generateOrder">支付</el-button>
    </el-dialog>
  </el-main>
</template>

<script lang="ts">
import {computed, onMounted, ref} from 'vue';
import {Cart, getCartItems, removeItemFromCart, updateCartItemQuantity, placeOrder} from '../../api/cart.ts';
import {ElMessage} from "element-plus";
import { useRoute, useRouter } from 'vue-router'; // 引入路由相关
// 定义组件
export default {
  setup() {
    const products = ref<Cart[]>([]); // 定义 products 为响应式变量
    const userId = sessionStorage.getItem('userId'); // 获取用户 ID
    const userIdNumber = Number(userId); // 将用户 ID 转换为数字
    const router = useRouter(); // 获取路由实例

    const dialogVisible = ref(false);

    const name = ref<string>('');
    const telephone = ref<string>('');
    const location = ref<string>('');

    const hasTelInput = computed(() => telephone.value !== '');
    const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;
    const telLegal = computed(() => chinaMobileRegex.test(telephone.value));

    // 获取购物车中的商品
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

    // 删除购物车中的商品
    const removeFromCart = async (userId: number, cartItemId: number) => {
      try {
        const res = await removeItemFromCart(userId, cartItemId); // 调用 API 删除商品
        if (res.data.code === '200') { // 检查响应状态
          // 删除成功后重新获取购物车商品列表
          await getAllInCart();
        } else {
          console.error('删除商品失败：响应格式不符合预期');
        }
      } catch (error) {
        console.error('删除商品失败:', error);
      }
    };

    // 修改购物车中商品的数量
    // const updateQuantity = async (cartItemId: number, newQuantity: number) => {
    //   if (newQuantity < 1) return; // 防止数量小于 1
    //   try {
    //     console.log("更新商品 ID:", cartItemId);
    //     const res = await updateCartItemQuantity(userIdNumber, cartItemId, newQuantity); // 调用更新数量的 API
    //     console.log("update", res);
    //     if (res.data.code === '200') { // 检查响应状态
    //       console.log("updateYes");
    //       await getAllInCart(); // 更新商品列表
    //     } else {
    //       console.error('修改数量失败:', res.data.msg);
    //     }
    //   } catch (error) {
    //     console.error('修改数量失败:', error);
    //   }
    // };
    //不是json
    const updateQuantity = async (cartItemId: number, newQuantity: number) => {
      if (newQuantity < 1) return; // 防止数量小于 1
      try {
        console.log("更新商品 ID:", cartItemId);
        const res = await updateCartItemQuantity(userIdNumber, cartItemId, newQuantity); // 调用更新数量的 API
        console.log("update", res);
        if (res.data.code === '200') { // 检查响应状态
          console.log("updateYes");
          await getAllInCart(); // 更新商品列表
        }
        // else {
        //   console.error('修改数量失败:', res.data.msg);
        // }
        else  {
          console.error('修改数量失败:', res.data.msg);
          ElMessage({
            message: res.data.msg,
            type: 'error',
            center: true,
          })
        }
      } catch (error) {
        console.error('修改数量失败:', error);
      }
    };

    const BackToAllProducts = () => {
      router.push('/home/all-products'); // 使用路由跳转
    };

    const generateOrder = async () => {
      let orderId: number;
      let totalAmount: number = 0;
      let createTime: string;
      const cartIds: string[] = [];

      for (const product of products.value) {
        cartIds.push(product.cartItemId.toString());
      }
      console.log("商品信息:", cartIds);

      if (!userId) {
        console.error('用户未登录, 支付订单时');
        return;
      }

      try {
        const res = await placeOrder(cartIds, {
          name: name.value,
          telephone: telephone.value,
          location: location.value,
        });
        if (res.data.code === '200') {
          orderId = res.data.data.orderId;
          totalAmount = res.data.data.totalAmount;
          createTime = res.data.data.createTime;
          console.log("订单ID:", res.data.data.orderId);

          await router.push({
            name: 'order',
            params: {
              orderId: orderId,
              totalAmount: totalAmount,
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
    }

    // 在组件挂载时获取购物车中的商品
    onMounted(() => {
      getAllInCart();
    });

    // 返回响应式变量和方法
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
    };
  }
};
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

.product-image-style {
  width: 200px; /* 设置图片宽度 */
  height: 100px; /* 设置图片高度 */
  object-fit: cover; /* 确保图片填充整个区域 */
  border-radius: 5px; /* 可选：添加圆角效果 */
}

.product-card {
  width: 500px; /* 设置卡片宽度 */
  max-height: 300px; /* 设置卡片最大高度 */
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平方向居中 */
  justify-content: center; /* 垂直方向居中 */
  text-align: center;
  padding: 10px;
  box-sizing: border-box;
  margin: 0 auto; /* 添加这行使卡片在父元素中水平居中 */
}

.button-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.order-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 10px;
}
.bgimage {

  background-image: url("../../assets/kenan.png");
  background-size: cover;
  background-position: center top;
  min-height: 100vh;
}
</style>
