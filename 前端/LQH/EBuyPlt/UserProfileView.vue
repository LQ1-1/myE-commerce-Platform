<template>
    <div class="user-profile-container">
        <div class="content-wrapper">
            <!-- 卡片1：个人基础信息 -->
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
                        <el-select v-model="userInfo.uGender" placeholder="请选择或输入性别" filterable allow-create>
                            <el-option label="男" value="男" />
                            <el-option label="女" value="女" />
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

                    <!-- 新密码 -->
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input v-model="userInfo.newPassword" type="password" show-password placeholder="不修改请留空" />
                    </el-form-item>

                    <!-- 确认密码 -->
                    <el-form-item label="确认新密码" prop="confirmPassword" v-if="userInfo.newPassword">
                        <el-input v-model="userInfo.confirmPassword" type="password" show-password
                            placeholder="请再次输入新密码" />
                    </el-form-item>

                    <!-- 按钮区域 -->
                    <el-form-item>
                        <el-button type="primary" @click="submitForm">保存基础信息</el-button>
                        <el-button @click="resetForm">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-card>

            <!-- 卡片2：收货地址管理 (新增部分) -->
            <el-card class="box-card" style="margin-top: 20px;">
                <template #header>
                    <div class="card-header">
                        <span>收货地址管理</span>
                        <el-button type="primary" size="small" @click="openAddDialog">新增收货地址</el-button>
                    </div>
                </template>

                <el-table :data="deliveryList" v-loading="deliveryLoading" border style="width: 100%">
                    <el-table-column prop="uContactPersonName" label="收货人" width="120" />
                    <el-table-column prop="uContactPersonPhone" label="联系电话" width="140" />
                    <el-table-column prop="uDeliveryAddress" label="收货地址" min-width="200" />
                    <el-table-column prop="oPostalCode" label="邮编" width="100" />
                    <el-table-column prop="oDeliveryNote" label="备注" show-overflow-tooltip />
                    <el-table-column label="操作" width="100" fixed="right">
                        <template #default="scope">
                            <el-button size="small" type="primary" link @click="openEditDialog(scope.row)">
                                编辑
                            </el-button>
                            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
        </div>

        <!-- 弹窗：新增/编辑收货地址 -->
        <el-dialog v-model="dialogVisible" :title="isEditMode ? '编辑收货地址' : '新增收货地址'" width="500px"
            @close="resetDeliveryForm">
            <el-form :model="deliveryForm" :rules="deliveryRules" ref="deliveryFormRef" label-width="100px">
                <el-form-item label="收货人姓名" prop="uContactPersonName">
                    <el-input v-model="deliveryForm.uContactPersonName" placeholder="请输入收货人姓名" />
                </el-form-item>
                <el-form-item label="联系电话" prop="uContactPersonPhone">
                    <el-input v-model="deliveryForm.uContactPersonPhone" placeholder="请输入联系电话" />
                </el-form-item>
                <el-form-item label="收货地址" prop="uDeliveryAddress">
                    <el-input v-model="deliveryForm.uDeliveryAddress" type="textarea" placeholder="请输入详细地址" />
                </el-form-item>
                <el-form-item label="性别" prop="uContactPersonGender">
                    <el-select v-model="deliveryForm.uContactPersonGender" placeholder="请选择性别" filterable allow-create>
                        <el-option label="男" value="Male" />
                        <el-option label="女" value="Female" />
                    </el-select>
                </el-form-item>
                <el-form-item label="邮箱" prop="uContactPersonEmail">
                    <el-input v-model="deliveryForm.uContactPersonEmail" placeholder="请输入收货人邮箱" />
                </el-form-item>
                <el-form-item label="邮编" prop="oPostalCode">
                    <el-input v-model="deliveryForm.oPostalCode" placeholder="请输入邮政编码" />
                </el-form-item>
                <el-form-item label="备注" prop="oDeliveryNote">
                    <el-input v-model="deliveryForm.oDeliveryNote" placeholder="送货备注信息" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="submitDeliveryForm" :loading="submitLoading">确定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus' 
import CryptoJS from 'crypto-js'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

// 后端地址
const BASE_URL = 'http://192.168.66.94:8082'

// ================= 基础信息部分 =================
const userInfo = reactive({
    uID: '',
    uNickName: '',
    uPassword: '',
    uPhone: '',
    uEmail: '',
    uGender: '',
    uRegisterDate: '',
    uAccountType: '',
    uAccountStatus: '',
    newPassword: '',
    confirmPassword: ''
})

const validateConfirmPass = (rule, value, callback) => {
    if (userInfo.newPassword && value !== userInfo.newPassword) {
        callback(new Error('两次输入的密码不一致!'))
    } else {
        callback()
    }
}

const rules = {
    uNickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
    newPassword: [{ min: 6, message: '新密码长度不能少于 6 位', trigger: 'blur' }],
    confirmPassword: [{ validator: validateConfirmPass, trigger: 'blur' }]
}

// ================= 收货地址部分 =================
const deliveryList = ref([])
const deliveryLoading = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)
const submitLoading = ref(false)
const deliveryFormRef = ref(null)

// 收货地址表单模型
const deliveryForm = reactive({
    uID: '',
    uDIndex: 0, // 修改时需要，新增时不需要
    uDeliveryAddress: '',
    uContactPersonName: '',
    uContactPersonPhone: '',
    uContactPersonGender: '',
    uContactPersonEmail: '',
    oPostalCode: '',
    oDeliveryNote: ''
})

// 收货地址验证规则
const deliveryRules = {
    uContactPersonName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
    uContactPersonPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
    uDeliveryAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

// 初始化
onMounted(() => {
    let targetID = route.query.uID
    if (!targetID) {
        targetID = sessionStorage.getItem('uID')
    }

    if (targetID) {
        fetchUserInfo(targetID)
        // 获取到ID后，同时也获取收货地址列表
        fetchDeliveryList(targetID)
    } else {
        ElMessage.error('未找到用户ID参数')
        router.push('/')
    }
})

// 获取基础信息
const fetchUserInfo = async (id) => {
    loading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/GetUserAccountInfo`, { uID: id })
        if (res.data && res.data.code === 200 && res.data.data) {
            Object.assign(userInfo, res.data.data)
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

// 提交基础信息表单
const submitForm = async () => {
    if (!formRef.value) return
    await formRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true
            const payload = { ...userInfo }
            if (userInfo.newPassword) {
                payload.uPassword = CryptoJS.SHA256(userInfo.newPassword).toString()
            } else {
                payload.uPassword = userInfo.uPassword
            }
            try {
                const res = await axios.post(`${BASE_URL}/api/SetUserAccountInfo`, payload)
                if (res.data === 'Success' || res.data === 'success' || res.data.code === 200) {
                    ElMessage.success('用户信息更新成功')
                    userInfo.newPassword = ''
                    userInfo.confirmPassword = ''
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

const resetForm = () => {
    fetchUserInfo(userInfo.uID)
    userInfo.newPassword = ''
    userInfo.confirmPassword = ''
}

const goBack = () => {
    router.back()
}

// ================= 收货地址逻辑实现 =================

// 1. 获取收货地址列表
const fetchDeliveryList = async (uid) => {
    deliveryLoading.value = true
    try {
        // 请求参数只需要 uID
        const res = await axios.post(`${BASE_URL}/api/GetUserDeliveryInfo`, { uID: uid })
        if (res.data && res.data.code === 200) {
            deliveryList.value = res.data.data || []
        } else {
            // 如果列表为空或者没有数据，可以视为空数组，不一定是报错
            deliveryList.value = []
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('获取收货地址失败')
    } finally {
        deliveryLoading.value = false
    }
}

// 2. 打开新增弹窗
const openAddDialog = () => {
    isEditMode.value = false
    // 清空表单数据，但保留uID
    Object.keys(deliveryForm).forEach(key => deliveryForm[key] = '')
    deliveryForm.uID = userInfo.uID // 确保关联到当前用户
    deliveryForm.uContactPersonGender = 'Male' // 默认值
    dialogVisible.value = true
}

// 3. 打开编辑弹窗
const openEditDialog = (row) => {
    isEditMode.value = true
    // 填充表单
    Object.assign(deliveryForm, row)
    // 确保 uID 存在 (列表返回的数据里有 uID)
    dialogVisible.value = true
}

// 4. 重置弹窗表单
const resetDeliveryForm = () => {
    if (deliveryFormRef.value) {
        deliveryFormRef.value.resetFields()
    }
}

// 5. 提交收货地址 (新增或修改)
const submitDeliveryForm = async () => {
    if (!deliveryFormRef.value) return

    await deliveryFormRef.value.validate(async (valid) => {
        if (valid) {
            submitLoading.value = true
            try {
                let url = ''
                let successMsg = ''

                if (isEditMode.value) {
                    // 修改接口
                    url = `${BASE_URL}/api/SetUserDeliveryInfo`
                    successMsg = '修改地址成功'
                } else {
                    // 新增接口
                    url = `${BASE_URL}/api/AddUserDeliveryInfo`
                    successMsg = '添加地址成功'
                }

                // 发送请求
                const res = await axios.post(url, deliveryForm)

                // 后端返回 ApiResult 或 纯字符串 "Success"
                // 修改和新增返回的是 String: "Success" 或 "Fail"
                if (res.data === 'Success' || res.data === 'success') {
                    ElMessage.success(successMsg)
                    dialogVisible.value = false
                    // 刷新列表
                    fetchDeliveryList(userInfo.uID)
                } else {
                    ElMessage.error('操作失败')
                }
            } catch (error) {
                console.error(error)
                ElMessage.error('请求发生错误')
            } finally {
                submitLoading.value = false
            }
        }
    })
}

const handleDelete = (row) => {
    // 二次确认弹窗
    ElMessageBox.confirm(
        '确定要删除这条收货地址吗？此操作不可恢复。',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(async () => {
        try {
            // 构造后端需要的参数 UserDeliveryInfoTableItem
            // 关键字段是 uID 和 uDIndex
            const payload = {
                uID: row.uID,
                uDIndex: row.uDIndex
            }

            const res = await axios.post(`${BASE_URL}/api/DeleteUserDeliveryInfo`, payload)

            // 后端返回字符串 "Success" 或 "Fail"
            if (res.data === 'Success' || res.data === 'success') {
                ElMessage.success('删除成功')
                // 重新获取列表
                fetchDeliveryList(userInfo.uID)
            } else {
                ElMessage.error('删除失败')
            }
        } catch (error) {
            console.error(error)
            ElMessage.error('删除请求出错')
        }
    })
    .catch(() => {
        // 用户点击取消，不做任何操作
    })
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

.content-wrapper {
    width: 800px;
    max-width: 100%;
}

.box-card {
    width: 100%;
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