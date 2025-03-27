<script setup lang="ts">
import {ref, computed, watch, onMounted} from 'vue';
import { router } from '../../router';
import { userRegister } from "../../api/user.ts";
import {ElMessage, UploadFile} from 'element-plus'; // 确保引入 ElMessage
import { getImage } from '../../api/tools';
// import {Product} from "../../api/product.ts"; // 导入 getImage 函数

const identity = ref('');
const username = ref('');
const name = ref('');
const tel = ref('');
const location = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const avatar = ref('https://cn.bing.com/images/search?view=detailV2&ccid=1Wut6rTV&id=9B31B80562F73BE8D8E767384D64ECC5A7A4F566&thid=OIP.1Wut6rTVdRgHLAadQzGBsgHaHa&mediaurl=https%3a%2f%2fpreview.qiantucdn.com%2f58pic%2f20220301%2f00458PICz982a4w23M488_PIC2018_PIC2018.jpg!w1024_new_0_1&exph=1024&expw=1024&q=%e5%a4%b4%e5%83%8f%e6%a0%87%e5%bf%97&simid=608013507271276788&FORM=IRPRST&ck=0291D5F60F9FE1EDB538B995D8043054&selectedIndex=0&itb=0&idpp=overlayview&ajaxhist=0&ajaxserp=0');
const fileList = ref<UploadFile[]>([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);

// const products = ref<Product[]>([]);
// 各种输入验证
const hasTelInput = computed(() => tel.value !== '');
const hasIdentityChosen = computed(() => identity.value !== '');
const hasPasswordInput = computed(() => password.value !== '');
const hasConfirmPasswordInput = computed(() => confirmPassword.value !== '');
const hasAddressInput = computed(() => location.value !== '');
const hasImageFile = computed(() => avatar.value !== '');
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/;
const telLegal = computed(() => chinaMobileRegex.test(tel.value));
const isPasswordIdentical = computed(() => password.value === confirmPassword.value);
const registerDisabled = computed(() => {
  if (!hasIdentityChosen.value) {
    return true;
  }else {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput.value && hasAddressInput.value &&
        telLegal.value && isPasswordIdentical.value && hasImageFile.value);
  }

});


async function handleChange(file: UploadFile, fileListNew: UploadFile[]) {
  const rawFile = file.raw; // 获取原始文件对象
  if (!rawFile) {
    ElMessage.error('无法获取文件');
    return;
  }

  try {
    const res = await getImage(rawFile); // 调用上传接口
    console.log(res);
    if (res) {
      console.log("res.result",res.result); // 输出返回的链接

      if (res.code === '000') {
        avatar.value = res.result.trim(); // 去除可能的多余空格
        console.log('avatar URL:', avatar.value);

        // 更新 fileList 中的文件对象，添加 URL
        fileListNew[0].url = avatar.value;
        fileList.value = fileListNew;

        ElMessage.success('上传成功');
      } else {
        ElMessage.error('上传失败，请重试');
      }
    } else {
      ElMessage.error('响应格式错误，请重试');
    }
  } catch (error) {
    console.error('上传失败:', error);
    ElMessage.error('上传失败，请重试');
  }
}

// 处理图片预览
const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url || ''; // 使用 file.url 进行预览
  dialogVisible.value = true;
};

// 处理文件删除
const handleRemove = (file: UploadFile) => {
  fileList.value = fileList.value.filter(item => item.uid !== file.uid); // 从文件列表中移除
};

// 加载状态
const loading = ref(false);

// 注册按钮触发
async function handleRegister() {
  loading.value = true;
  try {
    const res = await userRegister({
      username: username.value,
      role: identity.value,
      name: name.value,
      telephone: tel.value,
      password: password.value,
      location: location.value,
      avatar: avatar.value,
      email: email.value,
    });

    // 处理注册结果
    if (res.data.code === '200') {
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      });
      router.push({ path: "/login" });
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      });
    }
  } catch (error) {
    ElMessage({
      message: "注册失败，请稍后再试",
      type: 'error',
      center: true,
    });
  } finally {
    loading.value = false;
  }
}



 // 获取所有书籍
 // async function get_getAllProducts() {
 //   try {
 //     const res = await getAllProduct(); // 调用从后端获取商店的函数
 //     if (res.data && Array.isArray(res.data.result)) {
 //       products.value = res.data.result; // 更新商店列表
 //     } else {
 //       console.error('获取数据失败：响应格式不符合预期');
 //     }
 //   } catch (error) {
 //     console.error('获取商店列表失败:', error);
 //   }
 // }

 // 在组件挂载时获取商店列表
 // onMounted(() => {
 //   get_getAllProducts();
 // });
</script>

<template>
  <el-main class="main-frame bgimage">
    <el-card class="login-card">
      <div>
        <h1>创建一个新的账户</h1>

        <el-form>
          <el-row>
            <el-col :span="15">
              <el-form-item>
                <label for="name">昵称</label>
                <el-input id="name" v-model="name" placeholder="请输入昵称" />
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="8">
              <el-form-item>
                <label for="identity">身份</label>
                <el-select id="identity" v-model="identity" placeholder="请选择" style="width: 100%;">
                  <el-option value="CUSTOMER" label="顾客"/>
                  <el-option value="MANAGER" label="管理员"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item>
                <label v-if="!hasTelInput" for="tel">注册手机号</label>
                <label v-else-if="!telLegal" for="tel" class="error-warn">手机号不合法</label>
                <label v-else for="tel">注册手机号</label>
                <el-input id="tel" v-model="tel" :class="{'error-warn-input': (hasTelInput && !telLegal)}" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="15" v-if="identity !== 'STAFF'">
              <el-form-item>
                <label for="location">地址</label>
                <el-input id="location" v-model="location" placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

            <el-col :span="7" v-if="identity === 'STAFF'">
              <el-form-item>
                <label for="location">地址</label>
                <el-input id="location" v-model="location" placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

<!--            <el-col :span="1" v-if="identity === 'STAFF'"></el-col>-->

<!--            &lt;!&ndash; 所属商店选择 &ndash;&gt;-->
<!--            <el-col :span="7" v-if="identity === 'STAFF'">-->
<!--              <el-form-item :prop="hasStoreName ? 'storeId' : ''">-->
<!--                <label for="storeId">所属商店</label>-->
<!--                <el-select id="storeId" v-model="storeId" placeholder="请选择" style="width: 100%;">-->
<!--                  <el-option v-for="store in stores" :key="store.id" :value="store.id" :label="store.name" />-->
<!--                </el-select>-->

<!--              </el-form-item>-->
<!--            </el-col>-->
          </el-row>

          <el-form-item>
            <label for="password">密码</label>
            <el-input type="password" id="password" v-model="password" placeholder="••••••••"/>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasConfirmPasswordInput">确认密码</label>
            <label v-else-if="!isPasswordIdentical" class="error-warn">密码不一致</label>
            <label v-else>确认密码</label>
            <el-input type="password" id="confirm-password" v-model="confirmPassword" :class="{'error-warn-input': (hasConfirmPasswordInput && !isPasswordIdentical)}" placeholder="••••••••"/>
          </el-form-item>

          <el-form-item label="头像" prop="logo">
            <el-upload
                action="http://localhost:3000/api/images"
                list-type="picture-card"
                :auto-upload="true"
                :file-list="fileList"
                :on-change="handleChange"
                :on-remove="handleRemove"
                :on-preview="handlePictureCardPreview"
            >
              <el-icon><Plus /></el-icon>
              <div>点击上传头像</div>
            </el-upload>
            <el-dialog v-model="dialogVisible">
              <img class="dialog-image" :src="dialogImageUrl" alt="Logo Preview" />
            </el-dialog>
          </el-form-item>



          <span class="button-group">
            <el-button @click.prevent="handleRegister" :disabled="registerDisabled || loading" type="primary">
              <span v-if="loading">注册中...</span>
              <span v-else>创建账户</span>
            </el-button>

            <router-link to="/login" v-slot="{ navigate }">
              <el-button @click="navigate">去登录</el-button>
            </router-link>
          </span>
        </el-form>
      </div>
    </el-card>
  </el-main>
</template>

<style scoped>
.main-frame {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bgimage {
  background-image: url("../../assets/login.png");
}

.login-card {
  background: rgba(255, 215, 0, 0.8);
  width: 60%;
  padding: 10px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
</style>
