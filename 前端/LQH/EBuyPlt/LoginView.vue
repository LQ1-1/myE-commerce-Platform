<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>{{ isRegister ? '用户注册' : '电子商务系统' }}</span>
        </div>
      </template>

      <!-- 表单区域 -->
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px" size="large" status-icon>

        <!-- ================= 公共区域 (登录/注册都显示) ================= -->

        <!-- 账号输入框 -->
        <el-form-item label="账号" prop="uID">
          <el-input v-model="formData.uID" placeholder="请输入账号" :prefix-icon="User" />
        </el-form-item>

        <!-- ================= 注册专用区域 (仅注册显示) ================= -->

        <template v-if="isRegister">
          <!-- 用户名称 -->
          <el-form-item label="用户名" prop="uNickName">
            <el-input v-model="formData.uNickName" placeholder="请输入用户名" :prefix-icon="UserFilled" />
          </el-form-item>
        </template>

        <!-- ================= 公共区域 (密码) ================= -->

        <!-- 密码输入框 -->
        <el-form-item label="密码" prop="uPassword">
          <el-input v-model="formData.uPassword" type="password" placeholder="请输入密码" show-password
            :prefix-icon="Lock" />
        </el-form-item>

        <!-- ================= 注册专用区域 (其他详细信息) ================= -->

        <template v-if="isRegister">
          <!-- 确认密码 -->
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="formData.confirmPassword" type="password" placeholder="请再次输入密码" show-password
              :prefix-icon="Key" />
          </el-form-item>

          <!-- 手机号 -->
          <el-form-item label="手机号" prop="uPhone">
            <el-input v-model="formData.uPhone" placeholder="请输入手机号" :prefix-icon="Phone" />
          </el-form-item>

          <!-- Email -->
          <el-form-item label="Email" prop="uEmail">
            <el-input v-model="formData.uEmail" placeholder="请输入邮箱" :prefix-icon="MessageBox" />
          </el-form-item>

          <!-- 性别 -->
          <el-form-item label="性别" prop="uGender">
            <el-select v-model="formData.uGender" placeholder="选择或输入性别" filterable allow-create default-first-option
              clearable style="width: 100%">
              <el-option label="Male" value="Male" />
              <el-option label="Female" value="Female" />
            </el-select>
          </el-form-item>
        </template>

        <!-- ================= 按钮区域 ================= -->

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" :loading="isLoading" class="login-button" @click="handleSubmit">
            {{ isRegister ? '立即注册' : '登 录' }}
          </el-button>
        </el-form-item>

        <!-- 底部切换链接 -->
        <div class="form-footer">
          <el-link type="primary" :underline="false" @click="toggleMode">
            {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
          </el-link>
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

// --- 状态定义 ---
const formRef = ref(null)
const isLoading = ref(false)
const isRegister = ref(false)

// 表单数据
const formData = reactive({
  uID: '',
  uNickName: '',
  uPassword: '',
  uPhone: '',
  uEmail: '',
  uGender: '',
  uRegisterDate: '',
  uAccountType: '',
  uAccountStatus: '',
  confirmPassword: ''
})

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

// 动态计算规则：登录时只校验 uID 和 uPassword
const rules = computed(() => {
  // 基础规则（登录和注册都需要）
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

  // 注册特有规则（仅在 isRegister 为 true 时生效）
  if (isRegister.value) {
    return {
      ...baseRules,
      uNickName: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, validator: validatePass2, trigger: 'blur' }
      ],
      uPhone: [
        { required: true, message: '请输入手机号', trigger: 'blur' }
        // 可以加正则校验: { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不对', trigger: 'blur' }
      ],
      uEmail: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
      ],
      uGender: [
        { required: true, message: '请选择或输入性别', trigger: 'change' }
      ]
    }
  }

  // 登录模式只返回基础规则
  return baseRules
})

// --- 核心逻辑 ---

const toggleMode = () => {
  isRegister.value = !isRegister.value
  if (formRef.value) {
    formRef.value.resetFields() // 切换时清空校验红字和输入内容
  }
}

//获取router实例
const router = useRouter() 

const getNowTime = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从 0 开始，需 +1
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

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
          // --- 注册模式 ---
          url = 'http://192.168.66.94:8082/api/Registration'

          payload = {
            uID: formData.uID,             // 账号
            uNickName: formData.uNickName, // 用户名
            uPassword: hashedPassword,
            uPhone: formData.uPhone,
            uEmail: formData.uEmail,
            uGender: formData.uGender,
            uRegisterDate: getNowTime(new Date()),
            uAccountType: '普通用户',
            uAccountStatus: '正常'
          }
        } else {
          // --- 登录模式 ---
          url = 'http://192.168.66.94:8082/api/Login_RequestBody'
          payload = {
            uID: formData.uID,          // 登录使用账号(uID)
            uPassword: hashedPassword
          }
        }

        console.log('发送 Payload:', payload) // 调试用

        // 直接payload发送就可以
        const response = await axios.post(url, payload)

        console.log('后端响应:', response.data)

        if (response.status === 200) {
          const messageStr = response.data
          // alert(messageStr)

          if (messageStr.includes('Registration Success')) {
            //注册成功
            ElMessage.success(messageStr)
            toggleMode()  // 切换模式（去登录）
          } else if (messageStr.includes('Account Exist')) {
            //账号已经存在
            ElMessage.error(messageStr)
          } else if (messageStr.includes('Registration Fail')) {
            //注册失败,后端的原因
            ElMessage.error(messageStr)
          } else if (messageStr.includes('管理员')) {
            ElMessage.success('欢迎,用户'+formData.uID)
            sessionStorage.setItem('uID', formData.uID) 
            // router.push('/admin')//跳转到管理员界面
          } else if (messageStr.includes('普通用户')) {
            //欢迎
            ElMessage.success('欢迎,用户'+formData.uID)
            sessionStorage.setItem('uID', formData.uID) //将账号信息带入界面
            router.push('/ShoppingnbView')//跳转到普通用户界面
          } else if (messageStr.includes('商户')) {
            ElMessage.success('欢迎,用户'+formData.uID)
            sessionStorage.setItem('uID', formData.uID) //将账号信息带入界面
            router.push('/MerchantView')//跳转到商户界面
          } else if (messageStr.includes('无此类型账户')) {
            ElMessage.error(messageStr)
          }else if(messageStr.includes('No such Account')){
            ElMessage.error(messageStr)
          }else if(messageStr.includes('Wrong Password')){
            ElMessage.error(messageStr)
          }
        } else {
          ElMessage.warning(response.data || '操作未完成')
        }

      } catch (error) {
        console.error('API Error:', error)
        const errorMsg = error.response?.data || '网络连接错误'
        ElMessage.error(typeof errorMsg === 'string' ? errorMsg : '服务器内部错误')
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
/* 样式保持不变 */
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
</style>