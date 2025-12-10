<template>
  <div class="order-confirmation">
    <h2>订单确认</h2>

    <!-- 显示订单信息 -->
    <div class="order-details">
      <p><strong>订单 ID：</strong>{{ orderId }}</p>
      <p><strong>总金额：</strong>{{ totalAmount }}元</p>
      <p><strong>实际金额：</strong>{{ realAmount }}元</p>
      <p><strong>创建时间：</strong>{{ formatTime(createTime) }}</p>
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
import {postOrder, getStatus, Cart, getOrderItems, getCartItems } from '../../api/cart.ts';
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

    function formatTime(isoString: String) {
      // 1. 检查输入是否为空或无效，如果是则返回提示
      if (!isoString) {
        console.log('无')
        return '无';
      }

      // 2. 使用 ISO 字符串创建一个 Date 对象。
      //    JavaScript 的 Date 对象会自动将 UTC 时间转换为运行环境的本地时区。
      const date = new Date(isoString);

      // 3. 检查转换后的日期是否有效
      if (isNaN(date.getTime())) {
        console.log('无效日期')
        return '无效日期';
      }

      // 4. 从 Date 对象中获取年、月、日、时、分、秒
      const year = date.getFullYear();

      // getMonth() 返回的月份是从 0 开始的（0-11），所以需要加 1
      const month = String(date.getMonth() + 1).padStart(2, '0');

      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');

      // 5. 拼接成最终的字符串格式
      console.log('有效')
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
    }

    const confirmOrder = async () => {
      try {
        // 调用修改后的 postOrder 函数
        const response = await postOrder(Number(orderId), "http://localhost:3000/#/login"); // <-- 修改这一行

        if (response.data.code === '200') {
          console.log(response.data.data);

          const paymentForm = response.data.data.paymentForm;
          const payWindow = window.open('', '_blank');
          if (payWindow) {
            payWindow.document.open();
            payWindow.document.write(paymentForm);
            startPolling();
            payWindow.document.close();
          } else {
            ElMessage.error("无法打开新窗口，请检查浏览器设置");
          }
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
        const res2 = await getOrderItems(Number(orderId));
        console.log("getCart", res.data.data);
        console.log("getItems", res2.data.data)
        // 确认响应数据格式
        if (res.data.data && Array.isArray(res.data.data.items)) {
          // products.value = res.data.data.items; // 更新产品列表
          for (const item of res.data.data.items) {
            for (const id of res2.data.data.cartItemIds) {
              if (item.cartItemId === id) {
                products.value.push(item);
              }
            }
          }
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
      formatTime
    };
  }
};
</script>

<style scoped>
/* 页面整体容器 */
.order-confirmation {
  max-width: 800px;
  margin: 40px auto;
  padding: 28px 36px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.1);
}

/* 标题 */
.order-confirmation h2 {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
  color: #333;
}

/* 订单概要 */
.order-details {
  background: #fafafa;
  border: 1px solid #eee;
  padding: 18px 22px;
  border-radius: 14px;
  margin-bottom: 30px;
}

.order-details p {
  margin: 6px 0;
  font-size: 15px;
  color: #444;
}

.order-details strong {
  color: #222;
}

/* 商品卡片（更像电商订单） */
.product-card {
  display: flex;
  gap: 18px;
  padding: 14px;
  margin-bottom: 18px;
  border-radius: 14px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.product-image-style {
  width: 120px;
  height: 120px;
  border-radius: 10px;
  object-fit: cover;
}

.product-card h3 {
  margin: 4px 0;
  font-size: 17px;
  font-weight: 600;
  color: #333;
}

.product-card span {
  display: block;
  margin: 4px 0;
  font-size: 14px;
  color: #666;
}

/* 支付按钮 */
.order-confirmation .el-button {
  width: 100%;
  margin-top: 18px;
  font-size: 16px;
  padding: 12px 0;
  border-radius: 10px;
}

/* 支付结束弹窗美化 */
.afterDialog .el-dialog {
  border-radius: 18px !important;
}

.dialog-content {
  text-align: center;
  padding: 20px 0;
}

.message {
  display: block;
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.afterDialog .el-button {
  border-radius: 10px;
  padding: 10px 18px;
}

</style>