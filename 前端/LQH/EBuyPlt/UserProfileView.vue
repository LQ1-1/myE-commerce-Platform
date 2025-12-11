<template>
    <div class="user-profile-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>个人用户信息</span>
                    <el-button @click="goBack">返回</el-button>
                </div>
            </template>

            <el-form :model="userInfo" :rules="rules" ref="formRef" label-width="120px" v-loading="loading">
                <!-- ID (不可修改) -->
                <el-form-item label="用户ID" prop="uID">
                    <el-input v-model="userInfo.uID" disabled />
                </el-form-item>

                <!-- 昵称 -->
                <el-form-item label="昵称" prop="uNickName">
                    <el-input v-model="userInfo.uNickName" placeholder="请输入昵称" />
                </el-form-item>

                <!-- 电话 -->
                <el-form-item label="电话" prop="uPhone">
                    <el-input v-model="userInfo.uPhone" placeholder="请输入电话号码" />
                </el-form-item>

                <!-- 邮箱 -->
                <el-form-item label="邮箱" prop="uEmail">
                    <el-input v-model="userInfo.uEmail" placeholder="请输入邮箱" />
                </el-form-item>

                <!-- 性别 -->
                <el-form-item label="性别" prop="uGender">
                    <el-select v-model="userInfo.uGender" placeholder="请选择性别">
                        <el-option label="男" value="Male" />
                        <el-option label="女" value="Female" />
                    </el-select>
                </el-form-item>

                <!-- 注册时间 (不可修改) -->
                <el-form-item label="注册时间">
                    <el-input v-model="userInfo.uRegisterDate" disabled />
                </el-form-item>

                <!-- 账户类型 (不可修改) -->
                <el-form-item label="账户类型">
                    <el-tag type="info">{{ userInfo.uAccountType }}</el-tag>
                    <span class="tips">（不可修改）</span>
                </el-form-item>

                <!-- 账户状态 (不可修改) -->
                <el-form-item label="账户状态">
                    <el-tag :type="userInfo.uAccountStatus === '正常' ? 'success' : 'danger'">
                        {{ userInfo.uAccountStatus }}
                    </el-tag>
                    <span class="tips">（不可修改）</span>
                </el-form-item>

                <el-divider content-position="left">如需修改密码请填写下方</el-divider>

                <!-- 新密码：v-model 改为 userInfo.newPassword -->
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="userInfo.newPassword" type="password" show-password placeholder="不修改请留空" />
                </el-form-item>

                <!-- 确认密码：v-model 改为 userInfo.confirmPassword，v-if 也改 -->
                <el-form-item label="确认新密码" prop="confirmPassword" v-if="userInfo.newPassword">
                    <el-input v-model="userInfo.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
                </el-form-item>

                <!-- 按钮区域 -->
                <el-form-item>
                    <el-button type="primary" @click="submitForm">保存修改</el-button>
                    <el-button @click="resetForm">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import CryptoJS from 'crypto-js'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

// 后端地址
const BASE_URL = 'http://192.168.66.94:8082'

// 用户基本信息模型
const userInfo = reactive({
    uID: '',
    uNickName: '',
    uPassword: '', // 这里存储的是后端返回的 ******
    uPhone: '',
    uEmail: '',
    uGender: '',
    uRegisterDate: '',
    uAccountType: '',
    uAccountStatus: '',
    newPassword: '',
    confirmPassword: ''
})


// 自定义验证规则：确认密码
const validateConfirmPass = (rule, value, callback) => {
    // 注意：这里要对比 userInfo.newPassword
    if (userInfo.newPassword && value !== userInfo.newPassword) {
        callback(new Error('两次输入的密码不一致!'))
    } else {
        callback()
    }
}

// 表单验证规则
const rules = {
    uNickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
    newPassword: [{ min: 6, message: '新密码长度不能少于 6 位', trigger: 'blur' }],
    // 只有当输入了新密码时，才校验确认密码
    confirmPassword: [{ validator: validateConfirmPass, trigger: 'blur' }]
}

// 初始化
onMounted(() => {
    // 1. 尝试从路由参数获取 uID (例如 /userinfo?uID=1001)
    let targetID = route.query.uID
    // 2. 如果路由没传，尝试从 Session 获取当前登录人
    if (!targetID) {
        targetID = sessionStorage.getItem('uID')
    }

    if (targetID) {
        fetchUserInfo(targetID)
    } else {
        ElMessage.error('未找到用户ID参数')
        router.push('/') // 返回首页或登录页
    }
})

// 获取用户信息
const fetchUserInfo = async (id) => {
    loading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/GetUserAccountInfo`, {
            uID: id
        })

        if (res.data && res.data.code === 200 && res.data.data) { // 假设 ApiResult.success 返回 code 200
            const data = res.data.data
            // 赋值给响应式对象
            Object.assign(userInfo, data)
        } else {
            ElMessage.error(res.data.message || '获取用户信息失败')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('网络请求错误')
    } finally {
        loading.value = false
    }
}

// 提交表单
const submitForm = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true

            // 构造副本
            // 注意：解构会把 newPassword 和 confirmPassword 也带进去，
            // 如果后端严格限制多余字段，需要在发送前 delete 掉，
            // 但通常后端只读取它需要的字段，多传几个字段一般不影响。
            const payload = { ...userInfo }

            // --- 修改这里的判断逻辑 ---
            if (userInfo.newPassword) {
                // 使用 userInfo.newPassword
                payload.uPassword = CryptoJS.SHA256(userInfo.newPassword).toString()
            } else {
                payload.uPassword = userInfo.uPassword
            }

            try {
                const res = await axios.post(`${BASE_URL}/api/SetUserAccountInfo`, payload)

                if (res.data === 'Success' || res.data === 'success' || res.data.code === 200) {
                    ElMessage.success('用户信息更新成功')
                    // 清空密码框
                    userInfo.newPassword = ''
                    userInfo.confirmPassword = ''
                    // 重新获取信息
                    fetchUserInfo(userInfo.uID)
                } else {
                    ElMessage.error('更新失败，请重试')
                }
            } catch (error) {
                console.error(error)
                ElMessage.error('更新请求发生错误')
            } finally {
                loading.value = false
            }
        }
    })
}

// 重置
const resetForm = () => {
    fetchUserInfo(userInfo.uID)
    userInfo.newPassword = ''
    userInfo.confirmPassword = ''
}

// 返回
const goBack = () => {
    router.back()
}
</script>

<style scoped>
.user-profile-container {
    display: flex;
    justify-content: center;
    padding: 20px;
    background-color: #f5f7fa;
    min-height: 100vh;
}

.box-card {
    width: 800px;
    max-width: 100%;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.tips {
    margin-left: 10px;
    font-size: 12px;
    color: #909399;
}
</style>