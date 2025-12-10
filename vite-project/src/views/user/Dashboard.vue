<template>
  <div class="user-wrapper">
    <!-- 左侧：个人信息 -->
    <div class="user-aside">
      <div class="avatar-section">
        <el-avatar :src="avatar" size="90" />
        <div class="user-name-level">
          <div class="username">{{ username }}</div>
          <div class="level">L{{ level }}</div>
        </div>
      </div>

      <div class="checkin-section" v-if="role === 'CUSTOMER'">
        <p>最近签到：{{ latestCheck || '暂无记录' }}</p>
        <el-button
            class="user-btn"
            :disabled="isChecked"
            @click="handleCheckIn"
        >
          {{ isChecked ? '已签到 ✅' : '今日签到 ✨' }}
        </el-button>
      </div>

      <div class="info-section">
        <div class="info-item"><span>身份：</span>{{ parseRole(role) }}</div>
        <div class="info-item"><span>真实姓名：</span>{{ name }}</div>
        <div class="info-item"><span>电话：</span>{{ telephone }}</div>
        <div class="info-item"><span>地址：</span>{{ location }}</div>
        <div class="info-item"><span>邮箱：</span>{{ email }}</div>
      </div>

      <div class="btn-row">
        <el-button class="user-btn" type="primary" @click="editDialogVisible = true">修改信息</el-button>
        <el-button class="user-btn" type="primary" @click="addressDialogVisible = true">管理地址簿</el-button>
      </div>

    </div>

    <!-- 右侧：历史订单 -->
    <div class="user-main">
      <h2 class="main-title">历史订单</h2>
      <el-table
          :data="historyOrders"
          stripe
          height="auto"
          max-height="calc(100vh - 120px)"
          empty-text="暂无历史订单"
      >
        <el-table-column prop="id" label="订单号" width="120" />
        <el-table-column label="下单时间">
          <template #default="scope">{{ formatTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额">
          <template #default="scope">￥{{ scope.row.totalAmount.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="realAmount" label="实付金额">
          <template #default="scope">￥{{ scope.row.realAmount.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" link @click="showOrderDetail(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 修改个人信息弹窗 -->
    <el-dialog v-model="editDialogVisible" title="修改个人信息" width="500px">
      <el-form>
        <el-form-item label="真实姓名"><el-input v-model="editForm.name" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="editForm.telephone" /></el-form-item>
        <el-form-item label="主地址"><el-input v-model="editForm.location" type="textarea" rows="2" /></el-form-item>
        <el-form-item label="电子邮箱"><el-input v-model="editForm.email" /></el-form-item>
        <el-form-item label="头像">
          <el-upload
              action="#"
              list-type="picture-card"
              :auto-upload="false"
              :file-list="fileList"
              :on-change="handleAvatarChange"
              :on-remove="handleAvatarRemove"
              :on-preview="handlePictureCardPreview"
              :limit="1"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAllUserInfo">保存修改</el-button>
      </template>
    </el-dialog>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="orderDetailDialogVisible" title="订单详情" width="600px">
      <div v-if="selectedOrder">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ selectedOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ formatTime(selectedOrder.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">￥{{ selectedOrder.totalAmount.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="实付金额">￥{{ selectedOrder.realAmount.toFixed(2) }}</el-descriptions-item>
        </el-descriptions>

        <el-divider>订单商品</el-divider>
        <el-table :data="historyProducts" stripe max-height="200px">
          <el-table-column label="商品封面" width="100">
            <template #default="scope">
              <el-image style="width: 80px; height: 80px" :src="scope.row.cover[0]" fit="cover" />
            </template>
          </el-table-column>
          <el-table-column prop="title" label="商品标题" />
          <el-table-column label="单价" width="100">
            <template #default="scope">￥{{ scope.row.price.toFixed(2) }}</template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button type="primary" @click="orderDetailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 地址簿管理弹窗 -->
    <el-dialog v-model="addressDialogVisible" title="地址簿管理" width="600px">
      <el-table :data="editForm.addressBook" stripe border style="width: 100%">
        <el-table-column label="收件人" prop="name" width="120" />
        <el-table-column label="电话" prop="phone" width="150" />
        <el-table-column label="地址" prop="address" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" @click="openAddressDialogForEdit(scope.row, scope.$index)">编辑</el-button>
            <el-button link type="danger" @click="deleteAddress(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="text-align: right; margin-top: 15px;">
        <el-button type="success" @click="openAddressDialogForAdd">新增地址</el-button>
        <el-button type="primary" @click="submitAllUserInfo">保存地址簿改动</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑地址弹窗 -->
    <el-dialog v-model="isAddressEditing" title="编辑地址" width="400px" append-to-body>
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="收件人"><el-input v-model="addressForm.name" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="addressForm.phone" /></el-form-item>
        <el-form-item label="地址"><el-input type="textarea" rows="2" v-model="addressForm.address" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="isAddressEditing = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { userInfo, userInfoUpdate, addCredit, updateLevel, getHistoryOrders } from '../../api/user.ts';
import { parseRole } from "../../utils";
import { router } from '../../router';
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import { getImage } from "../../api/tools.ts";
import { Plus } from "@element-plus/icons-vue";
import { checkIn, getCheckinHistory, getCheckinStatus } from "../../api/checkin.ts";
import { getCartItem } from "../../api/cart.ts";
import { getProduct } from "../../api/product.ts";

const username = sessionStorage.getItem("username");
const role = sessionStorage.getItem("role");
const userId = sessionStorage.getItem("userId");

// ---------------- 用户信息 ----------------
const name = ref('');
const avatar = ref('');
const telephone = ref('');
const location = ref('');
const email = ref('');
const credits = ref(0);
const level = ref(0);

const editForm = reactive({
  name: '',
  avatar: '',
  telephone: '',
  location: '',
  email: '',
  addressBook: [] as { name: string; phone: string; address: string }[]
});

// 弹窗控制
const editDialogVisible = ref(false);
const addressDialogVisible = ref(false);
const isAddressEditing = ref(false);
const editingAddressIndex = ref<number | null>(null);
const addressForm = reactive({ name: '', phone: '', address: '' });

// 头像上传
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

// 签到
const latestCheck = ref('');
const isChecked = ref(false);

// 历史订单
const historyOrders = ref([]);
const orderDetailDialogVisible = ref(false);
const selectedOrder = ref<any>(null);
const historyProducts = ref([]);

// ---------------- 工具函数 ----------------
function formatTime(isoString: string): string {
  if (!isoString) return '无';
  const date = new Date(isoString);
  if (isNaN(date.getTime())) return '无效日期';
  const y = date.getFullYear(), m = String(date.getMonth()+1).padStart(2,'0'), d = String(date.getDate()).padStart(2,'0');
  const h = String(date.getHours()).padStart(2,'0'), min = String(date.getMinutes()).padStart(2,'0'), s = String(date.getSeconds()).padStart(2,'0');
  return `${y}年${m}月${d}日 ${h}:${min}:${s}`;
}

// ---------------- 用户信息接口 ----------------
const getUserInfo = async () => {
  try {
    const res = await userInfo(username);
    if (res.data.code == 200) {
      const userData = res.data.data;
      name.value = userData.name;
      avatar.value = userData.avatar;
      telephone.value = userData.telephone;
      location.value = userData.location;
      email.value = userData.email;
      credits.value = userData.score;
      level.value = userData.grade;

      Object.assign(editForm, {
        name: userData.name,
        avatar: userData.avatar,
        telephone: userData.telephone,
        location: userData.location,
        email: userData.email,
        addressBook: userData.addressBook || []
      });

      fileList.value = userData.avatar ? [{ name: 'avatar.jpg', url: userData.avatar, uid: Date.now() }] : [];
    }
  } catch (error) { console.error(error); }
};

const handleAvatarChange = async (uploadFile: UploadFile, uploadFiles: UploadFile[]) => {
  const rawFile = uploadFile.raw;
  if (!rawFile) return;
  if (rawFile.size > 1024*1024) { ElMessage.error('文件超过1MB'); return; }
  try {
    const res = await getImage(rawFile);
    if (res?.data?.code === '200') {
      editForm.avatar = res.data.data;
      fileList.value = uploadFiles.slice(-1);
      ElMessage.success('头像上传成功，请保存个人信息。');
    }
  } catch { ElMessage.error('头像上传失败'); }
};

const handleAvatarRemove = () => { editForm.avatar=''; fileList.value=[]; ElMessage.info('已移除头像'); };
const handlePictureCardPreview = (file: UploadFile) => { dialogImageUrl.value=file.url!; dialogVisible.value=true; };

const submitAllUserInfo = async () => {
  try {
    const res = await userInfoUpdate({
      username,
      name: editForm.name,
      avatar: editForm.avatar,
      telephone: editForm.telephone,
      email: editForm.email,
      location: editForm.location,
      addressBook: editForm.addressBook
    });
    if (res.data.code==200){ ElMessage.success('更新成功'); await getUserInfo(); }
    else ElMessage.error(res.data.msg||'更新失败');
  } catch { ElMessage.error('操作失败'); }
};

// ---------------- 地址簿操作 ----------------
const openAddressDialogForAdd = () => {
  isAddressEditing.value = true;
  editingAddressIndex.value = null;
  Object.assign(addressForm, { name:'', phone:'', address:'' });
};

const openAddressDialogForEdit = (addr:any, index:number) => {
  isAddressEditing.value = true;
  editingAddressIndex.value = index;
  Object.assign(addressForm, addr);
};

const saveAddress = () => {
  if(!addressForm.name||!addressForm.phone||!addressForm.address){ ElMessage.warning('请填写完整地址'); return; }
  if(editingAddressIndex.value!==null) editForm.addressBook[editingAddressIndex.value] = {...addressForm};
  else editForm.addressBook.push({...addressForm});
  isAddressEditing.value=false;
  ElMessage.info('地址已在本地更新，请保存地址簿改动');
};

const deleteAddress = (index:number) => {
  ElMessageBox.confirm('确定删除吗?','警告',{type:'warning'})
      .then(()=>{ editForm.addressBook.splice(index,1); ElMessage.info('已删除，请保存地址簿改动'); })
      .catch(()=>{});
};

// ---------------- 签到 ----------------
const handleCheckIn = async () => {
  const res = await checkIn(Number(userId));
  if(res.data.code==200){
    ElMessage.success('签到成功，获得1积分');
    await addCredit(Number(userId),1);
    await updateLevel(Number(userId));
    await getUserInfo(); await getLatestCheckIn(); await checked();
  } else { ElMessage.error('今日已签到'); }
};

const getLatestCheckIn = async () => {
  const res = await getCheckinHistory(Number(userId));
  if(res.data.code=='200' && res.data.data.length>0){
    latestCheck.value=res.data.data[res.data.data.length-1].checkinTime;
  }
};

const checked = async () => {
  const res = await getCheckinStatus(Number(userId));
  if(res.data.code=='200'){ isChecked.value=res.data.data; }
};

// ---------------- 历史订单 ----------------
const fetchHistoryOrders = async () => {
  if(!userId) return;
  try{
    const res = await getHistoryOrders(Number(userId));
    if(res.data.code=='200' && Array.isArray(res.data.data)) historyOrders.value=res.data.data;
  } catch(e){ console.error(e); ElMessage.error('获取历史订单失败'); }
};

const getIteminCart = async (cartItemId:number) => {
  const res = await getCartItem(cartItemId);
  if(res.data.code=='200') return res.data.data.productId;
};

const getHisProduct = async (productId:number) => {
  const res = await getProduct(String(productId));
  if(res.data.code=='200') return res.data.data;
};

const showOrderDetail = async (order:any) => {
  historyProducts.value=[];
  selectedOrder.value=order;
  const cartItemIds = order.cartItemIds;
  if(cartItemIds && Array.isArray(cartItemIds)){
    for(const cartItemId of cartItemIds){
      const pid = await getIteminCart(cartItemId);
      if(pid){ const p = await getHisProduct(Number(pid)); if(p) historyProducts.value.push(p); }
    }
  }
  orderDetailDialogVisible.value=true;
};

onMounted(async ()=>{
  await getUserInfo();
  await checked();
  await getLatestCheckIn();
  await fetchHistoryOrders();
});
</script>


<style scoped>
/* ===================== 用户中心整体布局 ===================== */
.user-wrapper {
  display: flex;
  gap: 30px;
  padding: 40px;
  width: 100%;
  min-height: 100vh;
  background: #f0f2f5;
  font-family: "Helvetica Neue", Arial, sans-serif;
  box-sizing: border-box;
  color: #333;
  align-items: flex-start;
}

/* ===================== 左侧个人信息卡片 ===================== */
.user-aside {
  flex: 0 0 280px;
  max-width: 100%;
  background: #fff;
  border-radius: 20px;
  padding: 25px;
  display: flex;
  flex-direction: column;
  gap: 25px;
  box-shadow: 0 8px 25px rgba(0,0,0,0.08);
  overflow-y: auto;
  max-height: calc(100vh - 80px);
}

/* 头像和用户名布局 */
.avatar-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-name-level .username {
  font-size: 22px;
  font-weight: 700;
  color: #333;
}

.user-name-level .level {
  font-size: 14px;
  font-weight: 600;
  color: #ff6b6b;
  margin-top: 4px;
}

/* 信息列表美化 */
.info-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  font-size: 14px;
  background-color: #fff7f6;
  padding: 15px;
  border-radius: 12px;
  box-shadow: inset 0 0 0 1px #ffe3e3;
}

.info-item {
  display: flex;
  justify-content: space-between;
  color: #555;
  font-weight: 500;
}

.info-item span {
  font-weight: 600;
  color: #333;
}

/* 左侧按钮统一风格 */
.user-btn {
  flex: 1;
  height: 42px;
  border-radius: 10px;
  font-weight: 600;
  background-color: #ff6b6b !important;
  border-color: #ff6b6b !important;
  color: #fff !important;
  transition: 0.3s;
}

.user-btn:hover {
  background-color: #ff5252 !important;
  border-color: #ff5252 !important;
}

.user-btn:disabled {
  opacity: 0.6;
  cursor: default;
}

/* 按钮容器：左右两个按钮在同一行 */
.btn-row {
  display: flex;
  gap: 15px;
}

/* ===================== 右侧历史订单 ===================== */
.user-main {
  flex: 1 1 600px;
  max-width: 100%;
  background: #ffffff;
  border-radius: 20px;
  padding: 25px;
  box-shadow: 0 8px 25px rgba(0,0,0,0.08);
  min-height: calc(100vh - 80px); /* 保持和左侧一致 */
}

.main-title {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
}

/* 表格字体颜色 */
.user-main .el-table th,
.user-main .el-table td {
  color: #333;
  background-color: #fafafa;
}

.user-main .el-table th {
  font-weight: 600;
}

.user-main .el-button[type="primary"] {
  border-radius: 8px !important;
}

/* ===================== 弹窗整体美化 ===================== */
.el-dialog {
  border-radius: 24px !important;        /* 大圆角 */
  background: #ffffff !important;        /* 白色背景 */
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15) !important; /* 浮动阴影 */
  color: #333 !important;
  overflow: hidden;
  transition: all 0.3s ease;
}

/* 弹窗头部 */
.el-dialog__header {
  font-size: 20px;
  font-weight: 700;
  color: #333;
  border-bottom: none !important;       /* 去掉默认边框 */
  background: linear-gradient(90deg, #ff9a9e 0%, #fad0c4 100%);
  color: #fff !important;
  padding: 20px 25px;
  border-radius: 24px 24px 0 0 !important;
}

/* 弹窗主体 */
.el-dialog__body {
  padding: 25px 30px;
  background-color: #fffaf7;            /* 柔和背景色 */
  border-radius: 0 0 24px 24px;
}

/* 弹窗底部 */
.el-dialog__footer {
  border-top: none !important;          /* 去掉默认分割线 */
  padding: 20px 25px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background-color: #fffaf7;
}

/* 表单输入元素圆角和阴影 */
.el-dialog input,
.el-dialog textarea,
.el-dialog .el-input,
.el-dialog .el-textarea {
  background-color: #fff !important;
  border-radius: 12px !important;
  border: 1px solid #ddd !important;
  box-shadow: inset 0 2px 5px rgba(0,0,0,0.05);
  padding: 8px 12px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.el-dialog input:hover,
.el-dialog textarea:hover,
.el-dialog .el-input:hover,
.el-dialog .el-textarea:hover {
  border-color: #ff6b6b !important;
  box-shadow: inset 0 2px 6px rgba(255,107,107,0.2);
}

/* 上传组件美化 */
.el-dialog .el-upload .el-upload-dragger,
.el-dialog .el-upload-list__item {
  background-color: #fff !important;
  border-radius: 12px !important;
  border: 1px dashed #ddd !important;
  transition: border-color 0.3s;
}

.el-dialog .el-upload:hover {
  border-color: #ff6b6b !important;
}

/* 弹窗按钮渐变色风格 */
.el-dialog .el-button[type="primary"] {
  border-radius: 12px !important;
  font-weight: 600;
  background: linear-gradient(90deg, #ff6b6b, #ff5252) !important;
  border: none !important;
  color: #fff !important;
  transition: all 0.3s;
}

.el-dialog .el-button[type="primary"]:hover {
  background: linear-gradient(90deg, #ff5252, #ff3b3b) !important;
}

.el-dialog .el-button[type="success"] {
  border-radius: 12px !important;
  background: linear-gradient(90deg, #4caf50, #43a047) !important;
  color: #fff !important;
  border: none;
}

.el-dialog .el-button[type="danger"] {
  border-radius: 12px !important;
  background: linear-gradient(90deg, #f56c6c, #f44336) !important;
  color: #fff !important;
  border: none;
}

</style>