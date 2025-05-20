<template>
  <div class="order-confirmation">
    <h2>订单确认</h2>

    <!-- 显示订单信息 -->
    <div class="order-details">
      <p><strong>订单 ID：</strong>{{ orderId }}</p>
      <p><strong>总金额：</strong>{{ totalAmount }}元</p>
      <p><strong>实际金额：</strong>{{ realAmount }}元</p>
      <p><strong>创建时间：</strong>{{ createTime }}</p>
    </div>

    <el-card
        v-for="product in products"
        :key="product.cartItemId"
        class="product-card"
    >
      <div class="product-image">
        <img :src='product.cover' alt="Product Cover" class="product-image-style" />
      </div>
      <h3>{{ product.title }}</h3>
      <span> 数量: {{ product.quantity }} </span>
      <span> 单价: {{ product.price }} </span>
    </el-card>

    <el-button type="primary" @click="confirmOrder" v-if="!orderClosed">确认支付</el-button>
<!--    <div v-if="payFormHtml" ref="formContainer" v-html="payFormHtml"></div>-->
    <el-dialog class="afterDialog" v-model="orderClosed" width="40%">
      <div class="dialog-content">
        <span class="message">支付结束</span>
        <el-button type="primary" @click="closeOrder">退出支付</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue';
import { addCredit, updateLevel } from '../../api/user.ts'
import {postOrder, getStatus, Cart, getCartItems } from '../../api/cart.ts';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from 'vue-router';
import {parseRole} from "../../utils"; // 引入路由相关
// 定义组件
export default {
  methods: {parseRole},
  setup() {
    const products = ref<Cart[]>([]); // 定义 products 为响应式变量
    const userId = sessionStorage.getItem('userId'); // 获取用户 ID
    const userIdNumber = Number(userId); // 将用户 ID 转换为数字
    const router = useRouter(); // 获取路由实例
    const route = useRoute();
    // const payFormHtml = ref<string>(''); // 用来存储 Alipay 返回的 HTML 表单

    const orderId: string = route.params.orderId;
    const createTime: string = route.params.createTime;
    let pollingTimer: ReturnType<typeof setInterval> | null = null
    const totalAmount: number = Number(route.params.totalAmount);
    const orderClosed = ref<boolean>(false);
    const realAmount: number = Number(route.params.realAmount);

    // const formContainer = ref<HTMLElement | null>(null);

    const confirmOrder = async () => {
      try {
        const response = await postOrder(Number(orderId));
        if (response.data.code === '200') {
          console.log(response.data.data);
          // 设置 HTML 表单
          // payFormHtml.value = response.data.data.paymentForm;
          //
          // // 等待 DOM 渲染完成后触发表单提交
          // await nextTick(); // 确保 HTML 被插入页面
          // const formEl = formContainer.value?.querySelector('form') as HTMLFormElement;
          // if (formEl) {
          //   formEl.submit(); // 自动提交表单，跳转到支付宝
          // } else {
          //   console.warn('找不到表单元素');
          // }

          const paymentForm = response.data.data.paymentForm;

          const payWindow = window.open('', '_blank');
          payWindow.document.open();
          payWindow.document.write(paymentForm);
          startPolling();
          payWindow.document.close();
        } else {
          ElMessage.error("支付失败！");
          console.error("支付失败！:", response.data);
        }
      } catch (error) {
        ElMessage.error('支付失败，请稍后再试');
        console.error('支付失败', error);
      }
    };

    const startPolling = async () => {
      pollingTimer = setInterval(async () => {
        const res = await getStatus(Number(orderId));
        if (res.data.code === '200') {
          console.log(res.data.data);

          if (res.data.data.status === 'TRADE_SUCCESS') {
            clearInterval(pollingTimer);
            ElMessage.success("支付成功！");
            let updateScore = Math.floor(realAmount / 10);
            console.log(userId, updateScore);
            await addCredit(Number(userId), updateScore);
            await updateLevel(Number(userId));
            console.log('success');
            orderClosed.value = true;
          }
          else if (res.data.status === 'TRADE_CLOSED') {
            clearInterval(pollingTimer);
            ElMessage.error('支付关闭');
            console.log('closing order');
            orderClosed.value = true;
          }
        } else {
          console.error('获取失败:', res.data);
        }
      }, 3000);
    }

    const closeOrder = async () => {
      console.log('支付结束');

      router.push({name: 'Cart'});
    }

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

    onMounted(()=>{
      getAllInCart();
    });

    return {
      userIdNumber,
      orderId,
      totalAmount,
      createTime,
      confirmOrder,
      // payFormHtml,
      // formContainer,
      orderClosed,
      closeOrder,
      products,
      realAmount,
    };
  }
};
</script>

<style scoped>
.order-confirmation {
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  margin: 50px auto;
}

.order-details {
  margin-bottom: 20px;
}

.order-details p {
  font-size: 16px;
  margin: 5px 0;
}

.dialog-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.message {
  font-size: 24px;
  font-weight: bold;
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
</style>