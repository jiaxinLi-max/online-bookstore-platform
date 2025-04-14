<template>
  <div class="order-confirmation">
    <h2>订单确认</h2>

    <!-- 显示订单信息 -->
    <div class="order-details">
      <p><strong>订单 ID：</strong>{{ orderId }}</p>
      <p><strong>总金额：</strong>{{ totalAmount }}元</p>
      <p><strong>创建时间：</strong>{{ createTime }}</p>
    </div>

    <el-button type="primary" @click="confirmOrder">确认支付</el-button>
    <div v-if="payFormHtml" v-html="payFormHtml"></div>
  </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue';
import { postOrder } from '../../api/cart.ts';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from 'vue-router'; // 引入路由相关

// 定义组件
export default {
  setup() {
    const userId = sessionStorage.getItem('userId'); // 获取用户 ID
    const userIdNumber = Number(userId); // 将用户 ID 转换为数字
    const router = useRouter(); // 获取路由实例
    const route = useRoute();
    const payFormHtml = ref<string>(''); // 用来存储 Alipay 返回的 HTML 表单

    const orderId: string = route.params.orderId;
    const totalAmount: number = route.params.totalAmount;
    const createTime: string = route.params.createTime;

    const confirmOrder = async () => {
      try {
        // 调用 postOrder 方法发起支付请求
        const response = await postOrder(Number(orderId)); // 传递订单 ID
        if (response.data.code === '200') {
          console.log("支付成功！:", response.data.data.OrderId, response.data.data.TotalAmount);
          payFormHtml.value = response.data.data.paymentForm;
        }
      } catch (error) {
        ElMessage.error('支付失败，请稍后再试');
        console.error('支付失败', error);
      }
    };

    return {
      userIdNumber,
      orderId,
      totalAmount,
      createTime,
      confirmOrder,
      payFormHtml,
    }
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
</style>