<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <!-- 标题根据状态动态显示：登录 / 用户注册 / 商户注册 -->
          <span>{{ titleText }}</span>
        </div>
      </template>

      <!-- 表单区域 -->
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px" size="large" status-icon>

        <!-- ================= 公共区域 (账号) ================= -->
        <el-form-item label="账号" prop="uID">
          <el-input v-model="formData.uID" placeholder="请输入账号" :prefix-icon="User" />
        </el-form-item>

        <!-- ================= 注册专用区域 (用户/商户通用) ================= -->
        <template v-if="isRegister">
          <!-- 用户名 -->
          <el-form-item label="用户名" prop="uNickName">
            <el-input v-model="formData.uNickName" placeholder="请输入用户名" :prefix-icon="UserFilled" />
          </el-form-item>
        </template>

        <!-- ================= 公共区域 (密码) ================= -->
        <el-form-item label="密码" prop="uPassword">
          <el-input v-model="formData.uPassword" type="password" placeholder="请输入密码" show-password
            :prefix-icon="Lock" />
        </el-form-item>

        <!-- ================= 注册专用区域 (其他信息) ================= -->
        <template v-if="isRegister">
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="formData.confirmPassword" type="password" placeholder="请再次输入密码" show-password
              :prefix-icon="Key" />
          </el-form-item>

          <!-- ⭐ 修改：添加 maxlength="11" 限制输入长度 -->
          <el-form-item label="手机号" prop="uPhone">
            <el-input v-model="formData.uPhone" placeholder="请输入手机号" :prefix-icon="Phone" maxlength="11" />
          </el-form-item>

          <el-form-item label="Email" prop="uEmail">
            <el-input v-model="formData.uEmail" placeholder="请输入邮箱" :prefix-icon="MessageBox" />
          </el-form-item>

          <el-form-item label="性别" prop="uGender">
            <el-select v-model="formData.uGender" placeholder="选择或输入性别" filterable allow-create default-first-option
              clearable style="width: 100%">
              <el-option label="Male" value="Male" />
              <el-option label="Female" value="Female" />
            </el-select>
          </el-form-item>
        </template>

        <!-- ================= 按钮区域 ================= -->
        <el-form-item>
          <el-button type="primary" :loading="isLoading" class="login-button" @click="handleSubmit">
            {{ isRegister ? '立 即 注 册' : '登 录' }}
          </el-button>
        </el-form-item>

        <!-- ================= 底部链接区域 ================= -->
        <div class="form-footer">
          <template v-if="!isRegister">
            <div class="footer-links">
              <el-link type="primary" :underline="false" @click="switchToRegister('user')">
                没有账号？去注册
              </el-link>
              <el-link type="warning" :underline="false" @click="switchToRegister('merchant')" class="merchant-link">
                我是商户，申请入驻
              </el-link>
            </div>
          </template>

          <template v-else>
            <el-link type="primary" :underline="false" @click="switchToLogin">
              已有账号？去登录
            </el-link>
          </template>
        </div>

      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { reactive, ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock, Key, UserFilled, Phone, MessageBox } from '@element-plus/icons-vue'
import CryptoJS from 'crypto-js'
import axios from 'axios'

const router = useRouter()
const formRef = ref(null)
const isLoading = ref(false)

// --- 状态控制 ---
const isRegister = ref(false)
const registerType = ref('user')

// 计算标题文字
const titleText = computed(() => {
  if (!isRegister.value) return '电子商务系统'
  return registerType.value === 'merchant' ? '商户入驻注册' : '用户注册'
})

// --- 表单数据 ---
const formData = reactive({
  uID: '',
  uNickName: '',
  uPassword: '',
  uPhone: '',
  uEmail: '',
  uGender: '',
  confirmPassword: ''
})

// --- 切换逻辑 ---
const switchToRegister = (type) => {
  isRegister.value = true
  registerType.value = type
  if (formRef.value) formRef.value.resetFields()
}

const switchToLogin = () => {
  isRegister.value = false
  registerType.value = 'user'
  if (formRef.value) formRef.value.resetFields()
}

// --- 验证规则 ---
const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== formData.uPassword) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = computed(() => {
  const baseRules = {
    uID: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    uPassword: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
    ]
  }

  if (isRegister.value) {
    return {
      ...baseRules,
      uNickName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }],
      // ⭐ 修改：添加了正则校验
      uPhone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }
      ],
      uEmail: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
      ],
      uGender: [{ required: true, message: '请选择或输入性别', trigger: 'change' }]
    }
  }
  return baseRules
})

// --- 工具函数 ---
const getNowTime = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// --- 提交处理 ---
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      isLoading.value = true

      try {
        const hashedPassword = CryptoJS.SHA256(formData.uPassword).toString()
        let payload = {}
        let url = ''

        if (isRegister.value) {
          url = 'http://192.168.66.94:8082/api/Registration'
          const finalAccountType = registerType.value === 'merchant' ? '商户' : '普通用户'

          payload = {
            uID: formData.uID,
            uNickName: formData.uNickName,
            uPassword: hashedPassword,
            uPhone: formData.uPhone,
            uEmail: formData.uEmail,
            uGender: formData.uGender,
            uRegisterDate: getNowTime(new Date()),
            uAccountType: finalAccountType,
            uAccountStatus: '正常'
          }
        } else {
          url = 'http://192.168.66.94:8082/api/Login_RequestBody'
          payload = {
            uID: formData.uID,
            uPassword: hashedPassword
          }
        }

        console.log('Mode:', registerType.value, 'Payload:', payload)

        const response = await axios.post(url, payload)
        const messageStr = response.data

        if (response.status === 200) {
          if (messageStr.includes('Registration Success')) {
            ElMessage.success(messageStr)
            switchToLogin()
          } else if (messageStr.includes('Account Exist')) {
            ElMessage.error(messageStr)
          } else if (messageStr.includes('Registration Fail')) {
            ElMessage.error(messageStr)
          } else if (messageStr.includes('管理员')) {
            ElMessage.success('欢迎, 用户' + formData.uID)
            sessionStorage.setItem('uID', formData.uID)
            router.push('/AdminView')
          } else if (messageStr.includes('普通用户')) {
            ElMessage.success('欢迎, 用户' + formData.uID)
            sessionStorage.setItem('uID', formData.uID)
            router.push('/ShoppingnbView')
          } else if (messageStr.includes('商户')) {
            ElMessage.success('欢迎, 商户' + formData.uID)
            sessionStorage.setItem('uID', formData.uID)
            router.push('/MerchantView')
          } else if (messageStr.includes('无此类型账户') || messageStr.includes('No such Account') || messageStr.includes('Wrong Password')) {
            ElMessage.error(messageStr)
          }
        } else {
          ElMessage.warning(response.data || '操作未完成')
        }

      } catch (error) {
        console.error('API Error:', error)
        ElMessage.error('服务器内部错误或网络连接失败')
      } finally {
        isLoading.value = false
      }
    } else {
      ElMessage.warning('请检查输入项是否完整')
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
  background-image: url('https://picsum.photos/1920/1080');
  background-size: cover;
}

.login-card {
  width: 400px;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.login-button {
  width: 100%;
  font-weight: bold;
}

.form-footer {
  text-align: right;
  margin-top: -10px;
}

.footer-links {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.merchant-link {
  font-size: 13px;
}
</style>