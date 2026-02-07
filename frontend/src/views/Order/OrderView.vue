<template>
  <div class="order-confirm-page">
    <div class="confirm-container">
      <!-- 顶部返回与标题 -->
      <div class="page-header">
        <el-button link icon="ArrowLeft" @click="closeOrder">返回购物车</el-button>
        <h2 class="page-title">确认订单支付</h2>
      </div>

      <div class="confirm-layout">
        <!-- 左侧：详细清单 -->
        <div class="layout-main">
          <!-- 订单状态/基本信息卡片 -->
          <section class="modern-card info-card">
            <div class="card-title">
              <el-icon><DocumentChecked /></el-icon>
              <span>收据摘要</span>
            </div>
            <div class="order-receipt-grid">
              <div class="receipt-item">
                <span class="label">订单编号</span>
                <span class="value code-font">{{ orderId }}</span>
              </div>
              <div class="receipt-item">
                <span class="label">下单时间</span>
                <span class="value">{{ formatTime(createTime) }}</span>
              </div>
              <div class="receipt-item">
                <span class="label">支付账户</span>
                <span class="value">{{ userIdNumber }} (个人账户)</span>
              </div>
              <div class="receipt-item">
                <span class="label">支付环境</span>
                <span class="value safe-tag"><el-icon><CircleCheckFilled /></el-icon> 安全加密</span>
              </div>
            </div>
          </section>

          <!-- 商品列表卡片 -->
          <section class="modern-card product-section">
            <div class="card-title">
              <el-icon><ShoppingBag /></el-icon>
              <span>商品清单</span>
              <span class="count-badge">{{ products.length }}</span>
            </div>
            <div class="product-scroll-area">
              <div v-for="product in products" :key="product.cartItemId" class="product-row">
                <div class="product-thumb">
                  <el-image :src="product.cover" fit="cover" />
                </div>
                <div class="product-info">
                  <div class="p-title">{{ product.title }}</div>
                  <div class="p-price-qty">
                    <span class="p-price">￥{{ product.price.toFixed(2) }}</span>
                    <span class="p-qty">x {{ product.quantity }}</span>
                  </div>
                </div>
                <div class="p-total-price">
                  ￥{{ (product.price * product.quantity).toFixed(2) }}
                </div>
              </div>
            </div>
          </section>
        </div>

        <!-- 右侧：结算中心 -->
        <aside class="layout-aside">
          <div class="checkout-sticky-card">
            <h3 class="summary-title">结算统计</h3>
            <div class="price-calc">
              <div class="calc-item">
                <span class="label">商品总额</span>
                <span class="value">￥{{ totalAmount.toFixed(2) }}</span>
              </div>
              <div class="calc-item">
                <span class="label">等级折扣</span>
                <span class="value discount-val">-￥{{ (totalAmount - realAmount).toFixed(2) }}</span>
              </div>
              <div class="calc-item">
                <span class="label">运费</span>
                <span class="value">包邮</span>
              </div>
            </div>

            <el-divider />

            <div class="final-price-row">
              <div class="total-label">实付款</div>
              <div class="total-amount">
                <span class="currency">￥</span>
                <span class="number">{{ realAmount.toFixed(2) }}</span>
              </div>
            </div>

            <el-button
                type="primary"
                class="ultimate-pay-btn"
                :loading="isPaying"
                @click="confirmOrder"
                v-if="!orderClosed"
            >
              <el-icon v-if="!isPaying"><Wallet /></el-icon>
              立即支付
            </el-button>

          </div>
        </aside>
      </div>
    </div>

    <!-- 支付状态反馈 -->
    <el-dialog
        v-model="orderClosed"
        width="420px"
        class="modern-status-dialog"
        :show-close="false"
        align-center
    >
      <div class="status-result">
        <div class="status-icon-box">
          <el-icon><SuccessFilled /></el-icon>
        </div>
        <h3>处理已完成</h3>
        <p>支付状态已同步至服务器，你可以前往个人中心查看订单明细。</p>
        <div class="dialog-btns">
          <el-button type="primary" @click="closeOrder" class="wide-btn">好的，知道了</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue';
import { addCredit, updateLevel } from '../../api/user.ts'
import { postOrder, getStatus, getOrderItems, getCartItems } from '../../api/cart.ts';
import type { Cart } from '../../api/cart.ts';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from 'vue-router';
import {
  Lock, Check, ArrowLeft, DocumentChecked,
  ShoppingBag, CircleCheckFilled, Wallet, SuccessFilled
} from '@element-plus/icons-vue';

export default {
  components: {
    Lock, Check, ArrowLeft, DocumentChecked,
    ShoppingBag, CircleCheckFilled, Wallet, SuccessFilled
  },
  setup() {
    const products = ref<Cart[]>([]);
    const userId = sessionStorage.getItem('userId');
    const userIdNumber = Number(userId);
    const router = useRouter();
    const route = useRoute();

    const isPaying = ref(false);
    const orderId = route.params.orderId as string;
    const createTime = route.params.createTime as string;
    const totalAmount = Number(route.params.totalAmount);
    const realAmount = Number(route.params.realAmount);
    const orderClosed = ref<boolean>(false);
    let pollingTimer: ReturnType<typeof setInterval> | null = null;

    function formatTime(isoString: string) {
      if (!isoString) return '--';
      const date = new Date(isoString);
      return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
    }

    const confirmOrder = async () => {
      isPaying.value = true;
      try {
        const response = await postOrder(Number(orderId), "http://localhost:3000/#/login");

        if (response.data.code === '200') {
          const paymentForm = response.data.data.paymentForm;
          const payWindow = window.open('', '_blank');
          if (payWindow) {
            payWindow.document.open();
            payWindow.document.write(paymentForm);
            startPolling();
            payWindow.document.close();
            ElMessage.success("支付窗口已打开，请完成支付");
          } else {
            ElMessage.error("支付窗口被拦截，请允许弹窗后重试");
            isPaying.value = false;
          }
        } else {
          ElMessage.error(response.data.msg || "发起支付失败");
          isPaying.value = false;
        }
      } catch (error) {
        ElMessage.error('服务异常，请稍后再试');
        isPaying.value = false;
      }
    };

    const startPolling = () => {
      pollingTimer = setInterval(async () => {
        const res = await getStatus(Number(orderId));
        if (res.data.code === '200') {
          const status = res.data.data.status;
          if (status === 'TRADE_SUCCESS') {
            clearInterval(pollingTimer!);
            ElMessage.success("支付成功！");
            let updateScore = Math.floor(realAmount / 10);
            await addCredit(Number(userId), updateScore);
            await updateLevel(Number(userId));
            orderClosed.value = true;
          } else if (status === 'TRADE_CLOSED') {
            clearInterval(pollingTimer!);
            ElMessage.error('交易已取消或失效');
            orderClosed.value = true;
          }
        }
      }, 3000);
    }

    const closeOrder = () => {
      if (pollingTimer) clearInterval(pollingTimer);
      router.push({ name: 'Cart' });
    }

    async function getAllInCart() {
      if (!userId) return;
      try {
        const res = await getCartItems(userIdNumber);
        const res2 = await getOrderItems(Number(orderId));
        if (res.data.data && Array.isArray(res.data.data.items)) {
          const cartItemIds = res2.data.data.cartItemIds;
          products.value = res.data.data.items.filter((item: any) =>
              cartItemIds.includes(item.cartItemId)
          );
        }
      } catch (error) {
        console.error('获取明细失败:', error);
      }
    }

    onMounted(getAllInCart);

    return {
      orderId, totalAmount, createTime, confirmOrder, orderClosed, userIdNumber,
      closeOrder, products, realAmount, formatTime, isPaying
    };
  }
};
</script>

<style scoped>
.order-confirm-page {
  background-color: #f7f7f9;
  min-height: 100vh;
  padding: 30px 20px;
  color: #1d1d1f;
}

.confirm-container {
  max-width: 1080px;
  margin: 0 auto;
  animation: slideUp 0.6s cubic-bezier(0.23, 1, 0.32, 1);
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 0 10px;
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  margin: 0;
}

.confirm-layout {
  display: flex;
  gap: 30px;
}

.layout-main { flex: 1; }
.layout-aside { width: 340px; }

/* 现代卡片通用样式 */
.modern-card {
  background: #ffffff;
  border-radius: 24px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 20px;
  color: #3a3a3c;
}

.count-badge {
  background: #f2f2f7;
  color: #86868b;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

/* 订单摘要网格 */
.order-receipt-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.receipt-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.receipt-item .label {
  font-size: 13px;
  color: #86868b;
}

.receipt-item .value {
  font-size: 15px;
  font-weight: 500;
}

.code-font {
  font-family: 'Courier New', Courier, monospace;
  color: #ff6b6b;
}

.safe-tag {
  color: #34c759;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

/* 商品清单行 */
.product-row {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px 0;
  border-bottom: 1px solid #f2f2f7;
}

.product-row:last-child { border-bottom: none; }

.product-thumb {
  width: 70px;
  height: 70px;
  border-radius: 14px;
  overflow: hidden;
  background: #fbfbfd;
  flex-shrink: 0;
}

.product-info { flex: 1; }

.p-title {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.p-price-qty {
  font-size: 14px;
  color: #86868b;
}

.p-qty { margin-left: 12px; }

.p-total-price {
  font-weight: 700;
  font-size: 16px;
}

/* 侧边结算栏 */
.checkout-sticky-card {
  background: #ffffff;
  border-radius: 28px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.06);
  position: sticky;
  top: 30px;
}

.summary-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 25px;
}

.price-calc {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.calc-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.calc-item .label { color: #86868b; }

.discount-val { color: #34c759; font-weight: 600; }

.final-price-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin: 10px 0 25px;
}

.total-label {
  font-size: 16px;
  font-weight: 600;
}

.total-amount { color: #ff6b6b; }

.total-amount .currency { font-size: 18px; font-weight: 700; }

.total-amount .number { font-size: 32px; font-weight: 800; }

.ultimate-pay-btn {
  width: 100%;
  height: 58px;
  border-radius: 18px;
  font-size: 17px;
  font-weight: 600;
  background: #1d1d1f !important;
  border: none !important;
  display: flex;
  gap: 10px;
  transition: all 0.3s;
}

.ultimate-pay-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  background: #333 !important;
}

.payment-methods {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  font-size: 12px;
  color: #aeaeb2;
}

.payment-methods img {
  width: 24px;
  opacity: 0.8;
}

/* 状态弹窗 */
.status-result {
  text-align: center;
  padding: 20px 0;
}

.status-icon-box {
  font-size: 60px;
  color: #34c759;
  margin-bottom: 15px;
}

.status-result h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.status-result p {
  color: #86868b;
  margin-bottom: 25px;
  line-height: 1.5;
}

.wide-btn {
  width: 80%;
  height: 46px;
  border-radius: 12px;
}

@media (max-width: 850px) {
  .confirm-layout { flex-direction: column; }
  .layout-aside { width: 100%; }
}
</style>