<template>
  <el-main class="main-container">
    <!-- 左侧：个人信息 -->
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :src="avatar" :size="90" class="user-avatar"></el-avatar>
        <div class="user-info">
          <div class="username-row">
            <div class="username-large">{{ username }}</div>
            <el-progress
                :percentage="progressPercentage"
                :stroke-width="8"
                :show-text="false"
                class="level-progress-inline"
            />
            <span class="level-tag">L{{ level }}</span>
          </div>

          <div class="checkin-section" v-if="role === 'CUSTOMER'">
            <div class="checkin-info">
              <p>最近签到：{{ latestCheck || '暂无记录' }}</p>
            </div>
            <button class="checkin-button" v-if="!isChecked" @click="handleCheckIn">
              ✨ 今日签到
            </button>
            <button class="checkin-button checked" v-else disabled>
              ✅ 已签到
            </button>
          </div>
        </div>

      </div>
      <el-divider></el-divider>

      <el-descriptions :column="1" border title="个人信息">
        <template #extra>
          <div style="display: flex; gap: 10px;">
            <el-button type="primary" @click="editDialogVisible = true">修改个人信息</el-button>
            <el-button type="success" @click="addressDialogVisible = true">管理地址簿</el-button>
          </div>
        </template>
        <el-descriptions-item label="身份"><el-tag>{{ parseRole(role) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="用户名"><el-tag>{{ username }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="真实姓名"><el-tag>{{ name }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ telephone }}</el-descriptions-item>
        <el-descriptions-item label="主地址">{{ location }}</el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{ email }}</el-descriptions-item>
      </el-descriptions>

    </el-card>

    <!-- 右侧：历史订单 -->
    <el-card class="orders-card">
      <h3>我的历史订单</h3>
      <el-table :data="historyOrders" stripe style="width: 100%" height="500" empty-text="暂无历史订单">
        <el-table-column prop="id" label="订单号" width="100" sortable />
        <el-table-column label="下单时间">
          <template #default="scope">{{ formatTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="scope">￥{{ scope.row.totalAmount.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="realAmount" label="实付金额" width="120">
          <template #default="scope">￥{{ scope.row.realAmount.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="showOrderDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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
            <el-button link type="primary" @click="openAddressDialog(scope.row, scope.$index)">编辑</el-button>
            <el-button link type="danger" @click="deleteAddress(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="text-align: right; margin-top: 15px;">
        <el-button type="success" @click="openAddressDialog()">新增地址</el-button>
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
  </el-main>
</template>


<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue';
import { userInfo, userInfoUpdate, addCredit, updateLevel, getHistoryOrders } from '../../api/user.ts';
import { parseRole } from "../../utils";
import { router } from '../../router';
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import { getImage } from "../../api/tools.ts";
import { Plus } from "@element-plus/icons-vue";
import { checkIn, getCheckinHistory, getCheckinStatus } from "../../api/checkin.ts";
import { getCartItem } from  "../../api/cart.ts"
import { getProduct } from "../../api/product.ts"
const editDialogVisible = ref(false); // ✨ 新增控制修改信息弹窗
const username = sessionStorage.getItem("username");
const role = sessionStorage.getItem("role");
const userId = sessionStorage.getItem("userId");

// 用于展示的个人信息
const name = ref('');
const avatar = ref('');
const telephone = ref('');
const location = ref('');
const email = ref('');
const credits = ref(0);
const level = ref(0);

// 用于编辑的完整用户信息响应式对象
const editForm = reactive({
  name: '',
  avatar: '',
  telephone: '',
  location: '',
  email: '',
  addressBook: [] as { name: string; phone: string; address: string }[]
});

const password = ref('');
const confirmPassword = ref('');
const activeTab = ref('info');

// 地址簿相关
const addressDialogVisible = ref(false);
const isAddressEditing = ref(false);
const editingAddressIndex = ref<number | null>(null);
const addressForm = reactive({ name: '', phone: '', address: '' });

// 头像上传
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

// 签到相关
const latestCheck = ref('');
const isChecked = ref(false);

// 历史订单相关状态
const historyOrders = ref([]);
const orderDetailDialogVisible = ref(false);
const selectedOrder = ref<any>(null);


const progressPercentage = computed(() => credits.value % 100);

const historyProducts = ref([]);

const MAX_SIZE = 1024 * 1024; // 1MB

function formatTime(isoString: string): string {
  if (!isoString) {
    return '无';
  }
  const date = new Date(isoString);
  if (isNaN(date.getTime())) {
    return '无效日期';
  }
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
}

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

      if(userData.avatar) {
        fileList.value = [{ name: 'avatar.jpg', url: userData.avatar, uid: Date.now() }];
      } else {
        fileList.value = [];
      }
    }
  } catch (error) { console.error('获取用户信息失败:', error); }
};

const handleAvatarChange = async (uploadFile: UploadFile, uploadFiles: UploadFile[]) => {
  const rawFile = uploadFile.raw;
  if (!rawFile) return;

  if (rawFile.size > MAX_SIZE) {
    ElMessage.error('文件超过最大大小限制（1MB）');
    return;
  }

  try {
    const res = await getImage(rawFile);

    if (res && res.data.code === '200') {
      editForm.avatar = res.data.data;
      fileList.value = uploadFiles.slice(-1);
      ElMessage.success('新头像上传成功，点击“更新个人信息”以保存。');
    } else {
      fileList.value = [];
      ElMessage.error(res.data.msg || '头像上传失败');
    }
  } catch (error) {
    fileList.value = [];
    console.error("头像上传异常:", error);
    ElMessage.error('头像上传异常');
  }
};

const handleAvatarRemove = () => {
  editForm.avatar = '';
  fileList.value = [];
  ElMessage.info('头像已移除。');
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!;
  dialogVisible.value = true;
};

const getIteminCart = async (cartItemId: number) => {
  const res = await getCartItem(cartItemId);
  if (res.data.code == '200') {
    return res.data.data.productId;
  }
  else {
    console.log("Unable to get cart item:", res.data);
  }
}

const submitAllUserInfo = async () => {
  try {
    const res = await userInfoUpdate({
      username: username,
      name: editForm.name,
      avatar: editForm.avatar,
      telephone: editForm.telephone,
      email: editForm.email,
      location: editForm.location,
      addressBook: editForm.addressBook
    });
    if (res.data.code == 200) {
      ElMessage.success('更新成功！');
      await getUserInfo();
    } else { ElMessage.error(res.data.msg || "更新失败"); }
  } catch(e) { ElMessage.error("更新操作失败"); }
};

const updatePassword = async () => {
  try {
    const res = await userInfoUpdate({ username: username, password: password.value });
    if (res.data.code === '200') {
      password.value = ''; confirmPassword.value = '';
      ElMessageBox.alert(`请重新登录`, '修改成功', {
        confirmButtonText: '跳转到登录', type: "success",
      }).then(() => router.push({path: "/login"}));
    } else {
      ElMessage.error(res.data.msg);
      password.value = ''; confirmPassword.value = '';
    }
  } catch (e) { ElMessage.error('修改密码失败'); }
};

const openAddressDialog = (addr: any | null = null, index: number | null = null) => {
  if (addr && index !== null) {
    isAddressEditing.value = true;
    editingAddressIndex.value = index;
    Object.assign(addressForm, addr);
  } else {
    isAddressEditing.value = false;
    editingAddressIndex.value = null;
    Object.assign(addressForm, { name: '', phone: '', address: '' });
  }
  addressDialogVisible.value = true;
};

const deleteAddress = (index: number) => {
  ElMessageBox.confirm('确定要删除这个地址吗?', '警告', { type: 'warning' })
      .then(() => {
        editForm.addressBook.splice(index, 1);
        ElMessage.info('地址已从列表移除，请点击“保存地址簿改动”按钮以生效。');
      }).catch(() => {});
};

const saveAddress = () => {
  if (!addressForm.name || !addressForm.phone || !addressForm.address) {
    ElMessage.warning("请填写完整的地址信息");
    return;
  }
  if (isAddressEditing.value && editingAddressIndex.value !== null) {
    editForm.addressBook[editingAddressIndex.value] = { ...addressForm };
  } else {
    editForm.addressBook.push({ ...addressForm });
  }
  addressDialogVisible.value = false;
  ElMessage.info('地址已在本地更新，请点击“保存地址簿改动”按钮提交。');
};

const handleCheckIn = async () => {
  const res = await checkIn(Number(userId));
  if (res.data.code == 200) {
    ElMessage.success('签到成功，获得1积分！');
    await addCredit(Number(userId), 1);
    await updateLevel(Number(userId));
    getUserInfo();
    await getLatestCheckIn();
    await checked();
  }
  else { ElMessage.error('今日已签到！'); }
};

const getLatestCheckIn = async () => {
  const res = await getCheckinHistory(Number(userId));
  if (res.data.code === '200' && res.data.data.length > 0) {
    const length = res.data.data.length;
    latestCheck.value = res.data.data[length-1].checkinTime;
  }
};

const checked = async () => {
  const res = await getCheckinStatus(Number(userId));
  if (res.data.code === '200') {
    isChecked.value = res.data.data;
  }
};

const fetchHistoryOrders = async () => {
  if (!userId) return;
  try {
    const res = await getHistoryOrders(Number(userId));
    if (res.data.code === '200' && Array.isArray(res.data.data)) {
      historyOrders.value = res.data.data;
    } else {
      ElMessage.error('获取历史订单失败');
    }
  } catch (error) {
    console.error("获取历史订单失败:", error);
    ElMessage.error('获取历史订单失败');
  }
};

const getHisProduct = async (productId: number) => {
  const res = await getProduct(String(productId)); // Ensure productId is a string
  if (res.data.code === '200') {
    return res.data.data;
  }
  else {console.log("Unable to get product: ", productId);}
}

const showOrderDetail = async (order: any) => {
  historyProducts.value = [];
  selectedOrder.value = order;
  const cartItemIds = selectedOrder.value.cartItemIds;
  if (cartItemIds && Array.isArray(cartItemIds)) {
    for (const cartItemId of cartItemIds) {
      const productId = await getIteminCart(cartItemId);
      if (productId) {
        const product = await getHisProduct(Number(productId));
        if (product) {
          historyProducts.value.push(product);
        }
      }
    }
  }
  orderDetailDialogVisible.value = true;
};


onMounted(async () => {
  await getUserInfo();
  await checked();
  await getLatestCheckIn();
  await fetchHistoryOrders();
});
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: row;
  padding: 40px; /* ✅ 增加内边距 */
  gap: 30px;
  background-image: url("../../assets/login.png");
  background-size: cover;
}

.aside-card {
  width: 32%;
  max-width: 380px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.orders-card {
  flex: 1;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}


.avatar-area {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar-text {
  font-size: 16px;
  font-weight: 500;
}

.level-display {
  margin-top: 10px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
}

.user-top {
  display: flex;
  align-items: center;
  gap: 12px; /* ✅ 用户名与等级之间的间距 */
}

.username {
  font-weight: bold; /* ✅ 用户名加粗 */
  font-size: 18px;
  color: #333;
}

.user-level {
  font-size: 14px;
  color: #999;
}

/* ✅ 修改进度条样式 */
.level-progress {
  margin-top: 6px;
}

/* 进度条底色为黑色，自己的等级条为红色 */
.level-progress ::v-deep(.el-progress-bar__outer) {
  background-color: black !important; /* 底色黑 */
}

.level-progress ::v-deep(.el-progress-bar__inner) {
  background-color: red !important; /* 自己的等级条红色 */
}
.username-large {
  font-size: 26px;
  font-weight: 700;
  color: #333;
  letter-spacing: 0.5px;
  margin-bottom: 8px;
}

.progress-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.level-tag {
  background: linear-gradient(135deg, #ffd700, #ff8c00);
  color: white;
  font-weight: bold;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(255, 140, 0, 0.3);
}

.fancy-checkin {
  margin-top: 12px;
  background: linear-gradient(90deg, #ffb347, #ffcc33);
  border: none;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  padding: 10px 24px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(255, 204, 51, 0.4);
}

.fancy-checkin:hover {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 6px 14px rgba(255, 204, 51, 0.6);
}

.user-avatar {
  border: 3px solid #ffcc33;
  box-shadow: 0 0 10px rgba(255, 204, 51, 0.5);
}

.username-row {
  display: flex;
  align-items: center;
  gap: 12px; /* 用户名、进度条、等级之间的距离 */
}

.level-progress-inline {
  width: 120px; /* 控制进度条宽度 */
}

.level-tag {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}


</style>