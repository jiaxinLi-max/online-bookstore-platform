<template>
  <el-main class="cart-page">
    <div class="cart-container">
      <!-- 顶部状态栏 -->
      <div class="cart-header">
        <div class="header-left">
          <el-checkbox v-model="checkAll" :indeterminate="indeterminate" @change="toggleCheckAll">
            <span class="select-all-text">全选 ({{ products.length }})</span>
          </el-checkbox>
        </div>
        <div class="header-right">
          您的等级：<el-tag size="small" type="warning" effect="dark">LV.{{ userLevel }}</el-tag>
          已选 <span class="highlight">{{ selectedProducts.length }}</span> 件
        </div>
      </div>

      <!-- 主体布局 -->
      <div class="cart-layout">
        <!-- 左侧：商品列表 -->
        <div class="cart-left">
          <div class="cart-item-card" v-for="product in products" :key="product.cartItemId"
               :class="{ 'is-selected': selectedProducts.includes(product.cartItemId) }">
            <div class="item-checkbox">
              <el-checkbox v-model="selectedProducts" :value="product.cartItemId" />
            </div>
            <img class="item-img" :src="product.cover" alt="商品封面" />
            <div class="item-main">
              <div class="item-title">{{ product.title }}</div>
              <div class="item-price">￥{{ product.price.toFixed(2) }}</div>
            </div>
            <div class="item-actions">
              <div class="quantity-box">
                <button class="qty-btn" @click="updateQuantity(product.cartItemId, product.quantity - 1)">−</button>
                <span class="qty-number">{{ product.quantity }}</span>
                <button class="qty-btn" @click="updateQuantity(product.cartItemId, product.quantity + 1)">＋</button>
              </div>
              <el-button link type="danger" @click="removeFromCart(userIdNumber, product.cartItemId)">删除</el-button>
            </div>
          </div>
          <el-empty v-if="products.length === 0" description="购物车还是空的" />
        </div>

        <!-- 右侧：结算面板 -->
        <div class="cart-right">
          <div class="checkout-panel">
            <h3 class="panel-title">订单摘要</h3>
            <div class="summary-row">
              <span>商品总额</span>
              <span>￥{{ totalPrice.toFixed(2) }}</span>
            </div>
            <div class="summary-row" v-if="discountRate < 1">
              <span>会员折扣 ({{ (discountRate * 10).toFixed(1) }}折)</span>
              <span class="discount-text">-￥{{ (totalPrice * (1 - discountRate)).toFixed(2) }}</span>
            </div>
            <el-divider />
            <div class="summary-row total">
              <span>实付预估</span>
              <span class="total-price">￥{{ (totalPrice * discountRate).toFixed(2) }}</span>
            </div>
            <el-button type="primary" class="main-checkout-btn" :disabled="selectedProducts.length === 0" @click="handleCheckout">
              去结算
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 结算弹窗 -->
    <el-dialog v-model="dialogVisible" title="确认收货信息" width="500px" align-center>
      <div class="checkout-form">
        <div class="form-group">
          <label class="form-label">从地址簿选择</label>
          <el-select v-model="selectedAddressIndex" placeholder="点击选择地址" style="width: 100%" @change="onAddressSelect" clearable>
            <el-option v-for="(addr, index) in addressBook" :key="index" :label="`${addr.name} - ${addr.phone}`" :value="index">
              <div class="addr-option">
                <div class="opt-head"><span class="opt-name">{{ addr.name }}</span><span class="opt-phone">{{ addr.phone }}</span></div>
                <div class="opt-desc">{{ addr.address }}</div>
              </div>
            </el-option>
          </el-select>
        </div>
        <el-divider><span class="divider-tip">收货详情</span></el-divider>
        <div class="form-row">
          <div class="form-group"><label class="form-label">收货人</label><el-input v-model="name" /></div>
          <div class="form-group"><label class="form-label">手机号</label><el-input v-model="telephone" /></div>
        </div>
        <div class="form-group"><label class="form-label">详细地址</label><el-input v-model="address" type="textarea" :rows="3" /></div>
      </div>
      <template #footer>
        <div class="dialog-actions">
          <el-button @click="dialogVisible = false" round>取消</el-button>
          <el-button type="primary" class="submit-order-btn" :disabled="!ableToOrder" @click="generateOrder" round>
            确认下单 (￥{{ (totalPrice * discountRate).toFixed(2) }})
          </el-button>
        </div>
      </template>
    </el-dialog>
  </el-main>
</template>

<script lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { getUserInfo, type Address } from "../../api/user.ts";
import { getCartItems, removeItemFromCart, updateCartItemQuantity, placeOrder } from "../../api/cart.ts";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

export default {
  setup() {
    const products = ref<any[]>([]);
    const userId = sessionStorage.getItem("userId");
    const userIdNumber = Number(userId);
    const router = useRouter();
    const dialogVisible = ref(false);
    const userLevel = ref(0); // 实时获取等级

    const name = ref("");
    const telephone = ref("");
    const address = ref("");
    const addressBook = ref<Address[]>([]);
    const selectedAddressIndex = ref<number | null>(null);
    const selectedProducts = ref<number[]>([]);
    const checkAll = ref(false);
    const indeterminate = ref(false);

    // 折扣算法
    const discountRate = computed(() => {
      if (userLevel.value >= 3) return 0.90;
      if (userLevel.value === 2) return 0.95;
      if (userLevel.value === 1) return 0.98;
      return 1.0;
    });

    const ableToOrder = computed(() => {
      return name.value && /^1[3-9]\d{9}$/.test(telephone.value) && address.value;
    });

    const totalPrice = computed(() => {
      return products.value
          .filter((p) => selectedProducts.value.includes(p.cartItemId))
          .reduce((acc, p) => acc + p.price * p.quantity, 0);
    });

    async function fetchInitialData() {
      if (!userId) return;
      try {
        const [cartRes, userRes] = await Promise.all([
          getCartItems(userIdNumber),
          getUserInfo(userIdNumber)
        ]);
        if (cartRes.data.code == '200') products.value = cartRes.data.data.items || [];
        if (userRes.data.code == '200') {
          addressBook.value = userRes.data.data.addressBook || [];
          userLevel.value = userRes.data.data.grade || 0; // 核心：在这里获取后端等级
          sessionStorage.setItem("level", userLevel.value.toString()); // 存入session供后面页面使用
        }
      } catch (error) { console.error(error); }
    }

    const generateOrder = async () => {
      try {
        const finalRealAmount = totalPrice.value * discountRate.value;
        const res = await placeOrder({
          userId: userIdNumber,
          cartItemIds: selectedProducts.value,
          shipping_address: { name: name.value, telephone: telephone.value, address: address.value },
          payment_method: 'ALIPAY',
        });

        if (res.data.code == 200) {
          ElMessage.success('下单成功');
          dialogVisible.value = false;
          router.push({
            name: 'Order',
            params: {
              orderId: res.data.data.id,
              totalAmount: totalPrice.value,
              realAmount: finalRealAmount, // 强制传递前端算好的折扣价
              createTime: res.data.data.createTime
            }
          });
        }
      } catch (error) { ElMessage.error('下单失败'); }
    };

    const updateQuantity = async (id: number, qty: number) => {
      if (qty < 1) return;
      const res = await updateCartItemQuantity(userIdNumber, id, qty);
      if (res.data.code == 200) products.value.find(p => p.cartItemId === id)!.quantity = qty;
    };

    const removeFromCart = (uId: number, cId: number) => {
      ElMessageBox.confirm('移除商品?').then(async () => {
        const res = await removeItemFromCart(uId, cId);
        if (res.data.code == '200') {
          products.value = products.value.filter(p => p.cartItemId !== cId);
          selectedProducts.value = selectedProducts.value.filter(i => i !== cId);
        }
      });
    };

    const onAddressSelect = (idx: any) => {
      if (idx !== null && addressBook.value[idx]) {
        const a = addressBook.value[idx];
        name.value = a.name; telephone.value = a.phone || ''; address.value = a.address;
      }
    };

    const toggleCheckAll = (val: any) => { selectedProducts.value = val ? products.value.map(p => p.cartItemId) : []; };
    watch(selectedProducts, (val) => {
      checkAll.value = products.value.length > 0 && val.length === products.value.length;
      indeterminate.value = val.length > 0 && val.length < products.value.length;
    });

    onMounted(fetchInitialData);
    return {
      products, updateQuantity, removeFromCart, generateOrder, dialogVisible, name, telephone,
      address, ableToOrder, selectedProducts, totalPrice, handleCheckout: () => { dialogVisible.value = true; },
      addressBook, selectedAddressIndex, onAddressSelect, toggleCheckAll, checkAll, indeterminate, userLevel, discountRate
    };
  }
};
</script>

<style scoped>
/* 保持原有样式，新增一个折扣颜色 */
.discount-text { color: #52c41a; font-weight: bold; }
.cart-page { background-color: #f5f5f7; min-height: 100vh; padding: 40px 20px; }
.cart-container { max-width: 1100px; margin: 0 auto; }
.cart-header { display: flex; justify-content: space-between; align-items: center; padding-bottom: 20px; }
.highlight { color: #ff6b6b; font-weight: 600; }
.cart-layout { display: flex; gap: 30px; }
.cart-left { flex: 1; }
.cart-item-card { background: #fff; border-radius: 20px; padding: 20px; margin-bottom: 16px; display: flex; align-items: center; gap: 20px; }
.item-img { width: 100px; height: 100px; object-fit: cover; border-radius: 12px; }
.item-main { flex: 1; }
.item-title { font-size: 17px; font-weight: 600; }
.item-price { color: #ff6b6b; font-weight: 700; }
.quantity-box { display: flex; align-items: center; background: #f5f5f7; border-radius: 10px; padding: 4px; }
.qty-btn { width: 30px; height: 30px; border: none; background: none; cursor: pointer; }
.qty-number { width: 40px; text-align: center; font-weight: 600; }
.cart-right { width: 320px; }
.checkout-panel { background: #fff; border-radius: 24px; padding: 30px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); position: sticky; top: 20px; }
.summary-row { display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 15px; }
.summary-row.total { font-weight: 700; font-size: 18px; color: #1d1d1f; }
.total-price { color: #ff6b6b; font-size: 24px; }
.main-checkout-btn { width: 100%; height: 50px; border-radius: 15px; background: #ff6b6b !important; border: none !important; }
.submit-order-btn { background: #ff6b6b !important; border: none !important; }
</style>