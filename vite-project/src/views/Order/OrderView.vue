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
                <span class="value code-font">{{ orderId || '读取中...' }}</span>
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
                  <el-image :src="product.cover" fit="cover">
                    <template #error><div class="image-slot"><el-icon><Picture /></el-icon></div></template>
                  </el-image>
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

            <!-- 立即支付按钮 -->
            <el-button
                type="primary"
                class="ultimate-pay-btn"
                :loading="isPaying"
                :disabled="orderClosed"
                @click="confirmOrder"
            >
              <el-icon v-if="!isPaying"><Wallet /></el-icon>
              {{ isPaying ? '正在支付...' : '立即支付' }}
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
        :close-on-click-modal="false"
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
import { onMounted, ref, onUnmounted } from 'vue';
import { addCredit, updateLevel } from '../../api/user.ts'
import { postOrder, getStatus, getOrderItems, getCartItems } from '../../api/cart.ts';
import type { Cart } from '../../api/cart.ts';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from 'vue-router';
import {
  DocumentChecked, ShoppingBag, CircleCheckFilled,
  Wallet, SuccessFilled, ArrowLeft, Picture
} from '@element-plus/icons-vue';

export default {
  components: {
    DocumentChecked, ShoppingBag, CircleCheckFilled,
    Wallet, SuccessFilled, ArrowLeft, Picture
  },
  setup() {
    const router = useRouter();
    const route = useRoute();

    // 基本数据
    const userId = sessionStorage.getItem('userId');
    const userIdNumber = Number(userId);
    const orderId = route.params.orderId as string;
    const createTime = route.params.createTime as string;
    const totalAmount = Number(route.params.totalAmount || 0);
    const realAmount = Number(route.params.realAmount || 0);

    // 状态控制
    const products = ref<Cart[]>([]);
    const isPaying = ref(false);
    const orderClosed = ref(false);
    let pollingTimer: any = null;

    // 格式化时间
    const formatTime = (isoString: string) => {
      if (!isoString) return '--';
      try {
        const date = new Date(isoString);
        return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
      } catch (e) { return isoString; }
    };

    // 轮询逻辑：检查后端订单支付状态
    const startPolling = () => {
      if (pollingTimer) clearInterval(pollingTimer);

      let attempts = 0;
      const maxAttempts = 100; // 约5分钟

      pollingTimer = setInterval(async () => {
        attempts++;
        if (attempts > maxAttempts) {
          stopPolling();
          isPaying.value = false;
          ElMessage.warning("支付确认超时，请刷新订单中心查看结果");
          return;
        }

        try {
          const res = await getStatus(Number(orderId));
          if (res.data.code === '200') {
            const status = res.data.data.status;
            // 根据后端实际返回的状态字符串判断
            if (status === 'TRADE_SUCCESS' || status === 'PAID') {
              stopPolling();
              handlePaymentSuccess();
            } else if (status === 'TRADE_CLOSED') {
              stopPolling();
              isPaying.value = false;
              ElMessage.error('交易已关闭或支付失败');
            }
          }
        } catch (error) {
          console.error("轮询异常:", error);
        }
      }, 3000); // 每3秒查询一次
    };

    const stopPolling = () => {
      if (pollingTimer) {
        clearInterval(pollingTimer);
        pollingTimer = null;
      }
    };

    const handlePaymentSuccess = async () => {
      ElMessage.success("支付成功！");
      try {
        // 计算积分并更新等级
        let updateScore = Math.floor(realAmount / 10);
        await addCredit(userIdNumber, updateScore);
        await updateLevel(userIdNumber);
      } catch (e) {
        console.error("更新会员信息失败", e);
      }
      isPaying.value = false;
      orderClosed.value = true;
    };

    // 点击立即支付
    const confirmOrder = async () => {
      if (!orderId || isNaN(Number(orderId))) {
        ElMessage.error("无效的订单ID，无法支付");
        return;
      }

      isPaying.value = true;
      try {
        // 发起支付，后端返回支付宝 Form 表单
        const returnUrl = window.location.origin + "/#/order-status"; // 支付后跳转回的地址
        const response = await postOrder(Number(orderId), returnUrl);

        if (response.data.code === '200') {
          const paymentForm = response.data.data.paymentForm;

          // 打开新窗口进行支付
          const payWindow = window.open('', '_blank');
          if (payWindow) {
            payWindow.document.open();
            payWindow.document.write(paymentForm);
            payWindow.document.close();

            ElMessage.success("支付页面已打开");
            startPolling(); // 开始不断询问后端：付钱了吗？
          } else {
            ElMessage.error("支付窗口被拦截，请在浏览器设置中允许弹窗");
            isPaying.value = false;
          }
        } else {
          ElMessage.error(response.data.msg || "发起支付失败");
          isPaying.value = false;
        }
      } catch (error) {
        console.error("支付请求错误:", error);
        ElMessage.error('无法连接到支付服务器，请检查网络');
        isPaying.value = false;
      }
    };

    const closeOrder = () => {
      stopPolling();
      router.push({ name: 'Cart' });
    }

    // 获取订单内的商品明细
    async function fetchOrderDetails() {
      if (!orderId || orderId === 'undefined') {
        console.warn("未发现订单ID");
        return;
      }
      try {
        // 1. 先获取该订单包含哪些 cartItemIds
        const resOrder = await getOrderItems(Number(orderId));
        const targetIds = resOrder.data.data.cartItemIds || [];

        // 2. 获取用户购物车全集，从中过滤出属于该订单的商品
        const resCart = await getCartItems(userIdNumber);
        if (resCart.data.code === '200') {
          const allItems = resCart.data.data.items || [];
          products.value = allItems.filter((item: any) => targetIds.includes(item.cartItemId));
        }
      } catch (error) {
        console.error('获取订单商品明细失败:', error);
      }
    }

    onMounted(() => {
      if (!orderId) {
        ElMessage.warning("订单信息已失效，请重新下单");
        router.push({ name: 'Cart' });
        return;
      }
      fetchOrderDetails();
    });

    onUnmounted(() => {
      stopPolling();
    });

    return {
      orderId, totalAmount, createTime, confirmOrder, orderClosed,
      userIdNumber, closeOrder, products, realAmount, formatTime, isPaying
    };
  }
};
</script>

<style scoped>
/* 保持你原来的样式不变 */
.order-confirm-page { background-color: #f7f7f9; min-height: 100vh; padding: 30px 20px; color: #1d1d1f; }
.confirm-container { max-width: 1080px; margin: 0 auto; animation: slideUp 0.6s cubic-bezier(0.23, 1, 0.32, 1); }
@keyframes slideUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; padding: 0 10px; }
.page-title { font-size: 22px; font-weight: 600; margin: 0; }
.confirm-layout { display: flex; gap: 30px; }
.layout-main { flex: 1; }
.layout-aside { width: 340px; }
.modern-card { background: #ffffff; border-radius: 24px; padding: 24px; margin-bottom: 24px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04); }
.card-title { display: flex; align-items: center; gap: 10px; font-size: 16px; font-weight: 700; margin-bottom: 20px; color: #3a3a3c; }
.order-receipt-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.receipt-item { display: flex; flex-direction: column; gap: 6px; }
.receipt-item .label { font-size: 13px; color: #86868b; }
.receipt-item .value { font-size: 15px; font-weight: 500; }
.code-font { font-family: 'Courier New', Courier, monospace; color: #ff6b6b; }
.safe-tag { color: #34c759; display: flex; align-items: center; gap: 4px; font-size: 14px; }
.product-row { display: flex; align-items: center; gap: 20px; padding: 16px 0; border-bottom: 1px solid #f2f2f7; }
.product-thumb { width: 70px; height: 70px; border-radius: 14px; overflow: hidden; background: #fbfbfd; flex-shrink: 0; }
.p-title { font-weight: 600; font-size: 15px; margin-bottom: 6px; }
.checkout-sticky-card { background: #ffffff; border-radius: 28px; padding: 30px; box-shadow: 0 10px 40px rgba(0,0,0,0.06); position: sticky; top: 30px; }
.discount-val { color: #34c759; font-weight: 600; }
.total-amount { color: #ff6b6b; }
.total-amount .number { font-size: 32px; font-weight: 800; }
.ultimate-pay-btn { width: 100%; height: 58px; border-radius: 18px; font-size: 17px; font-weight: 600; background: #1d1d1f !important; border: none !important; }
.status-result { text-align: center; padding: 20px 0; }
.status-icon-box { font-size: 60px; color: #34c759; margin-bottom: 15px; }
.wide-btn { width: 80%; height: 46px; border-radius: 12px; }
</style>