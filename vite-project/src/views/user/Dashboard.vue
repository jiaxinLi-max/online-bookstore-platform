<template>
  <el-main class="main-container">
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :src="avatar" :size="80"></el-avatar>
        <span class="avatar-text"> 欢迎您，{{ username }}</span>
      </div>
      <el-divider></el-divider>
      <el-descriptions :column="1" border title="个人信息">
        <template #extra>
          <el-button type="primary" @click="activeTab = 'info'">修改个人信息</el-button>
        </template>
        <el-descriptions-item label="身份"><el-tag>{{ parseRole(role) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="用户名"><el-tag>{{ username }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="真实姓名"><el-tag>{{ name }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ telephone }}</el-descriptions-item>
        <el-descriptions-item label="主地址">{{ location }}</el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{ email }}</el-descriptions-item>
      </el-descriptions>
      <div class="progress-container" v-if="role === 'CUSTOMER'">
        <el-progress :percentage="progressPercentage" :text-inside="true" :stroke-width="24" style="flex: 1;" />
        <div class="level-display">等级 {{ level }}</div>
      </div>
      <div class="level-rules" v-if="role === 'CUSTOMER'">
        <p>• 最高可升至10级，每级享受额外0.5折优惠</p>
        <p>• 每消费10元获取1积分，累计100积分可升1级</p>
        <p>• 每日可签到一次，获取1积分</p>
      </div>
      <div class="latestCheck-container" v-if="role === 'CUSTOMER'">
        <p>最近签到日期：{{ latestCheck }}</p>
      </div>
      <div class="checkInButton" v-if="(role === 'CUSTOMER') && !isChecked" >
        <el-button @click="handleCheckIn">签到</el-button>
      </div>
    </el-card>

    <el-card class="change-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="修改个人信息" name="info"></el-tab-pane>
        <el-tab-pane label="修改密码" name="password"></el-tab-pane>
        <el-tab-pane label="我的地址簿" name="addressBook"></el-tab-pane>
      </el-tabs>

      <div v-if="activeTab === 'info'">
        <el-form>
          <el-form-item label="真实姓名"><el-input v-model="editForm.name" /></el-form-item>
          <el-form-item label="联系电话"><el-input v-model="editForm.telephone" /></el-form-item>
          <el-form-item label="主地址"><el-input v-model="editForm.location" type="textarea" rows="2" /></el-form-item>
          <el-form-item label="电子邮箱"><el-input v-model="editForm.email" /></el-form-item>
          <el-form-item label="头像">
            <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleAvatarChange" :on-remove="handleAvatarRemove" :on-preview="handlePictureCardPreview" :limit="1">
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item><el-button type="primary" @click="submitAllUserInfo">更新个人信息</el-button></el-form-item>
        </el-form>
      </div>

      <div v-if="activeTab === 'password'">
        <el-form>
          <el-form-item label="新密码"><el-input type="password" v-model="password" show-password /></el-form-item>
          <el-form-item label="确认新密码">
            <el-input type="password" v-model="confirmPassword" show-password />
            <p v-if="password !== confirmPassword && confirmPassword" class="error-warn">两次密码输入不一致</p>
          </el-form-item>
          <el-form-item><el-button type="primary" @click="updatePassword" :disabled="!password || password !== confirmPassword">确认修改密码</el-button></el-form-item>
        </el-form>
      </div>

      <div v-if="activeTab === 'addressBook'">
        <el-button type="success" @click="openAddressDialog()" style="margin-bottom: 20px;">添加新地址</el-button>
        <el-table :data="editForm.addressBook" stripe style="width: 100%" height="250">
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="phone" label="电话" width="120" />
          <el-table-column prop="address" label="地址" show-overflow-tooltip />
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button type="primary" link size="small" @click="openAddressDialog(scope.row, scope.$index)">编辑</el-button>
              <el-button type="danger" link size="small" @click="deleteAddress(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" @click="submitAllUserInfo" style="margin-top: 20px;">保存地址簿改动</el-button>
      </div>
    </el-card>

    <el-dialog v-model="addressDialogVisible" :title="isAddressEditing ? '编辑地址' : '新增地址'" width="400px">
      <el-form :model="addressForm" label-position="top">
        <el-form-item label="收货人姓名"><el-input v-model="addressForm.name" /></el-form-item>
        <el-form-item label="手机号码"><el-input v-model="addressForm.phone" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="addressForm.address" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogVisible"><img class="dialog-image" :src="dialogImageUrl" alt="Preview" /></el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue';
import { userInfo, userInfoUpdate, addCredit, updateLevel } from '../../api/user.ts';
import { parseRole } from "../../utils";
import { router } from '../../router';
import { ElMessage, ElMessageBox, type UploadFile } from "element-plus";
import { getImage } from "../../api/tools.ts";
import { Plus } from "@element-plus/icons-vue";
import { checkIn, getCheckinHistory, getCheckinStatus } from "../../api/checkin.ts";

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
  addressBook: [] as { name: string; telephone: string; address: string }[]
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

const progressPercentage = computed(() => credits.value % 100);

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

const handleAvatarChange = async (file: UploadFile) => {
  if (file.status === 'ready') {
    try {
      const res = await getImage(file.raw!);
      if(res.data.code == 200) {
        editForm.avatar = res.data.data;
        ElMessage.success("新头像已上传，点击“更新”按钮保存");
      } else { ElMessage.error("头像上传失败"); }
    } catch(e) { ElMessage.error("头像上传异常"); }
  }
};

const handleAvatarRemove = () => {
  editForm.avatar = '';
  fileList.value = [];
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!;
  dialogVisible.value = true;
};

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

onMounted(async () => {
  await getUserInfo();
  await checked();
  await getLatestCheckIn();
});
</script>

<style scoped>
.main-container { display: flex; flex-direction: row; padding: 15px; gap: 15px; justify-content: center; background-image: url("../../assets/login.png"); }
.aside-card, .change-card { background: rgba(255, 255, 255, 0.9); border-radius: 8px; }
.aside-card { width: 33%; max-width: 400px; }
.change-card { width: 66%; max-width: 800px; }
.avatar-area { display: flex; flex-direction: column; align-items: center; gap: 10px; margin-bottom: 10px; }
.avatar-text { font-size: large; font-weight: bold; }
.progress-container { display: flex; align-items: center; gap: 20px; margin-top: 20px; }
.level-display { font-size: 24px; font-weight: bold; color: #409eff; min-width: 80px; text-align: center; }
.level-rules { font-size: 13px; color: #909399; line-height: 1.6; padding: 8px; margin-top: 10px; background-color: #f7f7f7; border-radius: 4px;}
.level-rules p { margin: 4px 0; }
.latestCheck-container { margin-top: 10px; text-align: center; color: #666; }
.checkInButton { text-align: center; margin-top: 15px; }
.error-warn { color: red; }
.dialog-image { max-width: 100%; }
.address-item { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #ebeef5; }
.address-item:last-child { border-bottom: none; }
.address-info p { margin: 2px 0; }
</style>