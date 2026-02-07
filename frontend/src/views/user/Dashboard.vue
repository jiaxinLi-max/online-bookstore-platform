<template>
  <div class="user-container">
    <div class="user-layout">
      <!-- 左侧：个人中心卡片 -->
      <aside class="user-aside">
        <!-- 头部：头像与核心信息 -->
        <div class="profile-card">
          <div class="avatar-wrapper">
            <el-avatar :src="avatar" :size="100" class="main-avatar" />
            <!-- 等级随数据库 grade 和 score 自动计算 -->
            <div class="level-badge">LV.{{ currentLevel }}</div>
          </div>
          <h2 class="username-text">{{ username }}</h2>

          <!-- 升级进度条模块 -->
          <div class="level-progress-box">
            <div class="progress-labels">
              <span class="current-lv">LV.{{ currentLevel }}</span>
              <span class="next-lv">LV.{{ currentLevel + 1 }}</span>
            </div>
            <el-progress
                :percentage="progressPercentage"
                :stroke-width="10"
                :color="customColors"
                :show-text="false"
            />
            <div class="progress-info">
              <span>本级进度：{{ progressPoints }} / 100</span>
              <span>当前积分：{{ score }}</span>
            </div>
          </div>
        </div>

        <!-- 签到模块 -->
        <div class="widget-card checkin-widget" v-if="role === 'CUSTOMER'">
          <div class="widget-header">
            <div class="header-left">
              <el-icon><Calendar /></el-icon>
              <span>日常签到</span>
              <el-tooltip
                  content="每次签到获得 1 积分，满 100 积分自动提升一级"
                  placement="top"
              >
                <el-icon class="help-icon"><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </div>
          <p class="last-checkin">最近：{{ latestCheck ? formatTime(latestCheck).split(' ')[0] : '从未签到' }}</p>
          <el-button
              class="action-btn sign-btn"
              :type="isChecked ? 'info' : 'primary'"
              :disabled="isChecked"
              round
              @click="handleCheckIn"
          >
            {{ isChecked ? '今日已打卡' : '立即签到 +1积分' }}
          </el-button>
        </div>

        <!-- 详细信息列表 -->
        <div class="widget-card info-widget">
          <div class="info-row">
            <span class="label">身份</span>
            <span class="value tag">{{ parseRole(role) }}</span>
          </div>
          <div class="info-row"><span class="label">姓名</span><span class="value">{{ name || '未填写' }}</span></div>
          <div class="info-row"><span class="label">电话</span><span class="value">{{ telephone || '未填写' }}</span></div>
          <div class="info-row"><span class="label">邮箱</span><span class="value">{{ email || '未填写' }}</span></div>
        </div>

        <!-- 快捷操作 -->
        <div class="aside-actions">
          <div class="action-card edit-card" @click="editDialogVisible = true">
            <div class="action-icon"><el-icon><EditPen /></el-icon></div>
            <div class="action-text"><span class="main-label">修改资料</span><span class="sub-label">完善个人信息</span></div>
            <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          </div>
          <div class="action-card address-card" @click="addressDialogVisible = true">
            <div class="action-icon"><el-icon><LocationInformation /></el-icon></div>
            <div class="action-text"><span class="main-label">地址管理</span><span class="sub-label">维护收货地址</span></div>
            <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          </div>
        </div>
      </aside>

      <!-- 右侧：内容区 -->
      <main class="user-main">
        <div class="content-card">
          <div class="card-header">
            <h3><el-icon><List /></el-icon> 历史订单</h3>
          </div>
          <el-table :data="historyOrders" stripe style="width: 100%" height="calc(100vh - 240px)" v-loading="loading">
            <el-table-column prop="id" label="订单编号" width="100" />
            <el-table-column label="下单时间" min-width="160">
              <template #default="scope"><div class="time-cell">{{ formatTime(scope.row.createTime) }}</div></template>
            </el-table-column>
            <el-table-column label="支付金额" width="120">
              <template #default="scope"><span class="amount">￥{{ scope.row.realAmount.toFixed(2) }}</span></template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <el-button type="primary" link @click="showOrderDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
            <template #empty><el-empty description="暂无历史订单" /></template>
          </el-table>
        </div>
      </main>
    </div>

    <!-- 弹窗：修改个人信息 -->
    <el-dialog v-model="editDialogVisible" title="完善个人资料" width="520px" class="modern-dialog" align-center>
      <div class="dialog-inner">
        <el-form :model="editForm" label-position="top" class="custom-form">
          <div class="avatar-upload-center">
            <el-upload action="#" class="avatar-uploader" :auto-upload="false" :show-file-list="false" :on-change="handleAvatarChange">
              <div class="avatar-hover-mask">
                <el-avatar :size="100" :src="editForm.avatar" />
                <div class="mask"><el-icon><Camera /></el-icon><span>更换头像</span></div>
              </div>
            </el-upload>
          </div>
          <el-row :gutter="20">
            <el-col :span="12"><el-form-item label="真实姓名"><el-input v-model="editForm.name" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="editForm.telephone" /></el-form-item></el-col>
          </el-row>
          <el-form-item label="电子邮箱"><el-input v-model="editForm.email" /></el-form-item>
          <el-form-item label="常用收货地址"><el-input v-model="editForm.location" type="textarea" rows="2" /></el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer-centered">
          <el-button @click="editDialogVisible = false" round>取消</el-button>
          <el-button type="primary" @click="submitAllUserInfo" round class="save-main-btn">保存修改</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 弹窗：地址簿管理 -->
    <el-dialog v-model="addressDialogVisible" title="地址簿管理" width="580px" class="address-book-dialog">
      <div class="address-manage-container">
        <div class="address-header-action">
          <span class="count-text">地址列表 ({{ editForm.addressBook.length }})</span>
          <el-tooltip content="新增地址" placement="top">
            <el-button type="primary" link @click="openAddressDialogForAdd" class="pure-plus-btn">
              <el-icon :size="28"><Plus /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
        <div class="modern-address-list">
          <div class="address-item-card" v-for="(addr, index) in editForm.addressBook" :key="index">
            <div class="card-left">
              <div class="user-info">
                <span>{{ addr.name }}</span><span>{{ addr.phone }}</span>
                <el-tag v-if="index === 0" size="small" type="success">默认</el-tag>
              </div>
              <div class="detail">{{ addr.address }}</div>
            </div>
            <div class="card-right">
              <el-button link type="primary" icon="Edit" @click="openAddressDialogForEdit(addr, index)">修改</el-button>
              <el-divider direction="vertical" />
              <el-button link type="danger" icon="Delete" @click="deleteAddress(index)">删除</el-button>
            </div>
          </div>
          <el-empty v-if="editForm.addressBook.length === 0" description="暂无地址" :image-size="60" />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer-centered">
          <el-button @click="addressDialogVisible = false" round>取消</el-button>
          <el-button type="primary" @click="submitAllUserInfo" round class="save-main-btn">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 弹窗：详细地址编辑 -->
    <el-dialog v-model="isAddressEditing" title="详细地址信息" width="380px" append-to-body>
      <el-form :model="addressForm" label-width="70px">
        <el-form-item label="收件人"><el-input v-model="addressForm.name" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="addressForm.phone" /></el-form-item>
        <el-form-item label="地址"><el-input type="textarea" rows="3" v-model="addressForm.address" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="isAddressEditing = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">确定</el-button>
      </template>
    </el-dialog>

    <!-- 弹窗：订单详情 -->
    <el-dialog v-model="orderDetailDialogVisible" title="订单详情" width="600px">
      <div v-if="selectedOrder">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ selectedOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="金额">￥{{ selectedOrder.realAmount.toFixed(2) }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>商品列表</el-divider>
        <el-table :data="historyProducts" stripe>
          <el-table-column label="封面" width="100">
            <template #default="scope"><el-image :src="scope.row.cover[0]" style="width:60px;height:60px" /></template>
          </el-table-column>
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="price" label="单价" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import {
  Calendar, List, Camera, Plus, Edit,
  Delete, EditPen, LocationInformation, ArrowRight, QuestionFilled
} from '@element-plus/icons-vue';

// 接口导入
import { userInfo, userInfoUpdate, addCredit, updateLevel, getHistoryOrders } from '../../api/user.ts';
import { checkIn, getCheckinHistory, getCheckinStatus } from "../../api/checkin.ts";
import { getCartItem } from "../../api/cart.ts";
import { getProduct } from "../../api/product.ts";
import { getImage } from "../../api/tools.ts";
import { parseRole } from "../../utils";

const username = sessionStorage.getItem("username");
const role = sessionStorage.getItem("role");
const userId = sessionStorage.getItem("userId");
const loading = ref(false);

// 用户基本信息
const name = ref('');
const avatar = ref('');
const telephone = ref('');
const location = ref('');
const email = ref('');

// --- 核心等级逻辑变量 ---
const score = ref(0); // 数据库积分
const grade = ref(0); // 数据库等级

// 计算属性：当前显示等级 = 数据库 grade + (score / 100)
const currentLevel = computed(() => {
  return grade.value + Math.floor(score.value / 100);
});

// 计算属性：当前等级内的进度值 (0-99)
const progressPoints = computed(() => score.value % 100);

// 计算属性：进度条百分比
const progressPercentage = computed(() => progressPoints.value);

const customColors = [
  { color: '#f56c6c', percentage: 20 },
  { color: '#e6a23c', percentage: 40 },
  { color: '#5cb87a', percentage: 60 },
  { color: '#1989fa', percentage: 80 },
  { color: '#6f7ad3', percentage: 100 },
];

const editForm = reactive({ name: '', avatar: '', telephone: '', location: '', email: '', addressBook: [] });
const addressForm = reactive({ name: '', phone: '', address: '' });
const editDialogVisible = ref(false);
const addressDialogVisible = ref(false);
const isAddressEditing = ref(false);
const editingAddressIndex = ref(null);
const isChecked = ref(false);
const latestCheck = ref('');
const historyOrders = ref([]);
const orderDetailDialogVisible = ref(false);
const selectedOrder = ref(null);
const historyProducts = ref([]);

// 格式化时间
const formatTime = (iso: string) => iso ? new Date(iso).toLocaleString() : '无';

// 获取用户信息
const getUserInfo = async () => {
  const res = await userInfo(username);
  if (res.data.code == 200) {
    const d = res.data.data;
    name.value = d.name;
    avatar.value = d.avatar;
    telephone.value = d.telephone;
    location.value = d.location;
    email.value = d.email;

    // 重点：同步数据库的 grade 和 score
    score.value = d.score || 0;
    grade.value = d.grade || 0;

    Object.assign(editForm, { ...d, addressBook: d.addressBook || [] });
  }
};

// 头像上传
const handleAvatarChange = async (file: UploadFile) => {
  if (file.raw) {
    const res = await getImage(file.raw);
    if (res?.data?.code === '200') { editForm.avatar = res.data.data; ElMessage.success('上传成功'); }
  }
};

// 签到逻辑
const handleCheckIn = async () => {
  const res = await checkIn(Number(userId));
  if (res.data.code == 200) {
    ElMessage.success('签到成功，获得1积分');
    await addCredit(Number(userId), 1);
    await updateLevel(Number(userId));
    // 重新拉取用户信息，更新界面上的 score 和 grade
    await getUserInfo();
    isChecked.value = true;
  }
};

// 保存资料
const submitAllUserInfo = async () => {
  const res = await userInfoUpdate({ username, ...editForm });
  if (res.data.code == 200) {
    ElMessage.success('保存成功');
    editDialogVisible.value = false; addressDialogVisible.value = false;
    await getUserInfo();
  }
};

// 地址管理逻辑
const saveAddress = () => {
  if (!addressForm.name || !addressForm.phone || !addressForm.address) return ElMessage.warning('请填写完整');
  if (editingAddressIndex.value !== null) editForm.addressBook[editingAddressIndex.value] = { ...addressForm };
  else editForm.addressBook.push({ ...addressForm });
  isAddressEditing.value = false;
};

const openAddressDialogForAdd = () => { editingAddressIndex.value = null; Object.assign(addressForm, { name:'', phone:'', address:'' }); isAddressEditing.value=true; };
const openAddressDialogForEdit = (addr, idx) => { editingAddressIndex.value = idx; Object.assign(addressForm, addr); isAddressEditing.value=true; };
const deleteAddress = (idx) => ElMessageBox.confirm('确定删除?').then(() => editForm.addressBook.splice(idx, 1));

// 订单逻辑
const fetchHistoryOrders = async () => {
  loading.value = true;
  const res = await getHistoryOrders(Number(userId));
  if (res.data.code == '200') historyOrders.value = res.data.data;
  loading.value = false;
};

const showOrderDetail = async (order) => {
  selectedOrder.value = order;
  historyProducts.value = [];
  for (const id of order.cartItemIds) {
    const cRes = await getCartItem(id);
    const pRes = await getProduct(cRes.data.data.productId);
    historyProducts.value.push(pRes.data.data);
  }
  orderDetailDialogVisible.value = true;
};

onMounted(async () => {
  await getUserInfo();
  const cRes = await getCheckinStatus(Number(userId)); isChecked.value = cRes.data.data;
  const historyRes = await getCheckinHistory(Number(userId));
  if (historyRes.data.data.length) latestCheck.value = historyRes.data.data.slice(-1)[0].checkinTime;
  await fetchHistoryOrders();
});
</script>

<style scoped>
/* 核心容器 */
.user-container { min-height: 100vh; background-color: #f4f7f9; padding: 20px; font-family: sans-serif; }
.user-layout { max-width: 1200px; margin: 0 auto; display: grid; grid-template-columns: 320px 1fr; gap: 24px; }

/* 侧边栏 */
.user-aside { display: flex; flex-direction: column; gap: 20px; }
.profile-card, .widget-card { background: white; border-radius: 16px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }

.avatar-wrapper { position: relative; display: inline-block; margin-bottom: 15px; }
.main-avatar { border: 4px solid #fff; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
.level-badge { position: absolute; bottom: 0; right: 0; background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); color: #fff; padding: 2px 8px; border-radius: 10px; font-size: 12px; font-weight: bold; border: 2px solid #fff; }

.level-progress-box { margin-top: 20px; padding: 15px; background: #f8fafc; border-radius: 12px; }
.progress-labels { display: flex; justify-content: space-between; margin-bottom: 8px; font-weight: bold; font-size: 12px; color: #475569; }
.progress-info { display: flex; justify-content: space-between; font-size: 11px; color: #94a3b8; margin-top: 8px; }

/* 快捷操作卡片 */
.aside-actions { display: flex; flex-direction: column; gap: 12px; }
.action-card { display: flex; align-items: center; padding: 14px 16px; border-radius: 14px; cursor: pointer; transition: 0.3s; border: 1px solid transparent; background: white; }
.edit-card { background: linear-gradient(135deg, #f0f7ff 0%, #e0efff 100%); border-color: #d0e4ff; }
.address-card { background: linear-gradient(135deg, #f5f3ff 0%, #ede9fe 100%); border-color: #ddd6fe; }
.action-card:hover { transform: translateY(-3px); box-shadow: 0 8px 15px rgba(0,0,0,0.08); }
.action-icon { width: 36px; height: 36px; border-radius: 10px; display: flex; justify-content: center; align-items: center; margin-right: 12px; }
.edit-card .action-icon { background: #409eff; color: #fff; }
.address-card .action-icon { background: #7c3aed; color: #fff; }
.action-text { display: flex; flex-direction: column; flex: 1; }
.main-label { font-size: 14px; font-weight: bold; color: #1e293b; }
.sub-label { font-size: 11px; color: #94a3b8; }

.widget-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.header-left { display: flex; align-items: center; gap: 8px; font-weight: 600; color: #334155; }
.help-icon { font-size: 14px; color: #94a3b8; cursor: help; margin-left: 2px; }

.info-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px dashed #f1f5f9; font-size: 14px; }
.info-row .tag { background: #eff6ff; color: #3b82f6; padding: 2px 8px; border-radius: 4px; }
.content-card { background: white; border-radius: 16px; padding: 24px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.amount { color: #ef4444; font-weight: bold; }

/* 弹窗样式 */
:deep(.modern-dialog) { border-radius: 20px; }
.avatar-upload-center { text-align: center; margin-bottom: 20px; }
.avatar-hover-mask { position: relative; border-radius: 50%; overflow: hidden; cursor: pointer; display: inline-block; }
.avatar-hover-mask .mask { position: absolute; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.4); display:flex; flex-direction:column; align-items:center; justify-content:center; color:#fff; opacity:0; transition:0.3s; }
.avatar-hover-mask:hover .mask { opacity:1; }

.address-header-action { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.pure-plus-btn:hover { transform: scale(1.2); }
.address-item-card { display: flex; justify-content: space-between; padding: 15px; background: #f8fafc; border-radius: 12px; margin-bottom: 10px; border: 1px solid #eee; }
.dialog-footer-centered { display: flex; justify-content: center; gap: 15px; padding: 10px 0; }
.save-main-btn { min-width: 120px; font-weight: bold; }
</style>