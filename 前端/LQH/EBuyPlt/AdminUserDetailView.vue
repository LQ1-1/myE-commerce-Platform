<template>
    <div class="detail-container">
        <el-page-header @back="goBack" content="用户详情页" class="page-header" />

        <div class="content-wrapper">
            <!-- 1. 用户基本信息卡片 (保持不变) -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>👤 基本信息 (ID: {{ uID }})</span>
                    </div>
                </template>
                <el-descriptions border :column="3">
                    <el-descriptions-item label="昵称">{{ userInfo.uNickName }}</el-descriptions-item>
                    <el-descriptions-item label="电话">{{ userInfo.uPhone }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱">{{ userInfo.uEmail }}</el-descriptions-item>
                    <el-descriptions-item label="性别">{{ userInfo.uGender }}</el-descriptions-item>
                    <el-descriptions-item label="类型">
                        <el-tag>{{ userInfo.uAccountType }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="状态">{{ userInfo.uAccountStatus }}</el-descriptions-item>
                    <el-descriptions-item label="注册日期">{{ userInfo.uRegisterDate }}</el-descriptions-item>
                </el-descriptions>
            </el-card>

            <!-- 2. 收货地址列表 (修改：增加按钮和操作列) -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header"
                        style="display: flex; justify-content: space-between; align-items: center;">
                        <span>📍 收货地址记录</span>
                        <el-button type="primary" size="small" @click="openAddDialog">新增地址</el-button>
                    </div>
                </template>
                <el-table :data="deliveryList" border stripe style="width: 100%" v-loading="loadingDelivery">
                    <el-table-column type="index" label="序号" width="60" />
                    <el-table-column prop="uContactPersonName" label="收货人" width="100" />
                    <el-table-column prop="uContactPersonPhone" label="电话" width="120" />
                    <el-table-column prop="uDeliveryAddress" label="详细地址" min-width="150" />
                    <el-table-column prop="oPostalCode" label="邮编" width="80" />
                    <el-table-column prop="uContactPersonEmail" label="邮箱" width="120" show-overflow-tooltip />
                    <el-table-column prop="oDeliveryNote" label="备注" show-overflow-tooltip />

                    <!-- 新增操作列 -->
                    <el-table-column label="操作" width="150" fixed="right">
                        <template #default="scope">
                            <el-button link type="primary" size="small"
                                @click="openEditDialog(scope.row)">编辑</el-button>
                            <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>

            <!-- 3. 订单记录 (保持不变) -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>📦 订单记录 (点击查看详情)</span>
                    </div>
                </template>
                <el-table :data="orderList" border stripe style="width: 100%" v-loading="loadingOrder"
                    @row-click="handleOrderClick" class="clickable-row">
                    <el-table-column prop="oOrderID" label="订单号" width="180" />
                    <el-table-column prop="oDate" label="下单时间" width="180" />
                    <el-table-column prop="oStatus" label="状态" width="100">
                        <template #default="scope">
                            <el-tag>{{ scope.row.oStatus }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="oReceiverName" label="收件人" width="120" />
                    <el-table-column label="操作" width="100">
                        <template #default>
                            <el-icon>
                                <ArrowRight />
                            </el-icon> 查看
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
        </div>

        <!-- 4. 新增/编辑地址弹窗 -->
        <el-dialog v-model="dialogVisible" :title="isEditMode ? '编辑收货地址' : '新增收货地址'" width="500px" @close="resetForm">
            <el-form :model="addressForm" label-width="100px" :rules="rules" ref="addressFormRef">
                <el-form-item label="收货人" prop="uContactPersonName">
                    <el-input v-model="addressForm.uContactPersonName" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="联系电话" prop="uContactPersonPhone">
                    <el-input v-model="addressForm.uContactPersonPhone" placeholder="请输入电话" />
                </el-form-item>
                <el-form-item label="性别" prop="uContactPersonGender">
                    <el-select v-model="addressForm.uContactPersonGender" placeholder="请选择">
                        <el-option label="男" value="男" />
                        <el-option label="女" value="女" />
                    </el-select>
                </el-form-item>
                <el-form-item label="详细地址" prop="uDeliveryAddress">
                    <el-input v-model="addressForm.uDeliveryAddress" type="textarea" placeholder="请输入详细地址" />
                </el-form-item>
                <el-form-item label="邮箱" prop="uContactPersonEmail">
                    <el-input v-model="addressForm.uContactPersonEmail" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="邮编" prop="oPostalCode">
                    <el-input v-model="addressForm.oPostalCode" placeholder="请输入邮编" />
                </el-form-item>
                <el-form-item label="备注" prop="oDeliveryNote">
                    <el-input v-model="addressForm.oDeliveryNote" placeholder="请输入备注" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="submitAddress" :loading="submitting">确定</el-button>
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
import { ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const BASE_URL = 'http://192.168.126.94:8082'

const uID = ref('')
const userInfo = ref({})
const deliveryList = ref([])
const orderList = ref([])
const loadingDelivery = ref(false)
const loadingOrder = ref(false)

// === 弹窗相关状态 ===
const dialogVisible = ref(false)
const isEditMode = ref(false)
const submitting = ref(false)
const addressFormRef = ref(null)

// 表单数据模型
const addressForm = reactive({
    uID: '',
    uDIndex: 0,
    uContactPersonName: '',
    uContactPersonPhone: '',
    uContactPersonGender: '男',
    uDeliveryAddress: '',
    uContactPersonEmail: '',
    oPostalCode: '',
    oDeliveryNote: ''
})

// 表单验证规则
const rules = {
    uContactPersonName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
    uContactPersonPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
    uDeliveryAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

onMounted(() => {
    document.title ='User Detail-Admin';
    document.querySelector('link[rel="icon"]').href = '/myIcon.svg';
    // 1. 获取 uID
    if (route.params.uID) {
        uID.value = route.params.uID
    }
    // 2. 解析 state
    if (history.state && history.state.userData) {
        try {
            const data = JSON.parse(history.state.userData)
            userInfo.value = data
            if (!uID.value && data.uID) {
                uID.value = data.uID
            }
        } catch (e) {
            console.error("解析用户数据失败", e)
        }
    }

    if (!uID.value) {
        ElMessage.error("未获取到用户ID，无法查询详情")
        return
    }

    fetchDeliveryInfo()
    fetchOrderInfo()
})

const goBack = () => router.back()

// 获取收货地址
const fetchDeliveryInfo = async () => {
    loadingDelivery.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/AdminGetUserDeliveryInfo`, { uID: uID.value })
        if (res.data && res.data.data) {
            deliveryList.value = res.data.data
        } else {
            deliveryList.value = []
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('获取收货信息失败')
    } finally {
        loadingDelivery.value = false
    }
}

// 获取订单记录
const fetchOrderInfo = async () => {
    loadingOrder.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/AdminGetUserOrderInfo`, { uID: uID.value })
        if (res.data && res.data.data) {
            orderList.value = res.data.data
        } else {
            orderList.value = []
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('获取订单记录失败')
    } finally {
        loadingOrder.value = false
    }
}

// === 地址管理功能实现 ===

// 打开新增弹窗
const openAddDialog = () => {
    isEditMode.value = false
    // 重置数据
    Object.keys(addressForm).forEach(key => addressForm[key] = '')
    addressForm.uID = uID.value // 确保关联到当前用户
    addressForm.uContactPersonGender = '男'
    dialogVisible.value = true
}

// 打开编辑弹窗
const openEditDialog = (row) => {
    isEditMode.value = true
    // 填充数据 (使用 Object.assign 复制 row 数据到 form)
    Object.assign(addressForm, row)
    // 确保 uID 存在
    addressForm.uID = uID.value
    dialogVisible.value = true
}

// 重置表单验证状态
const resetForm = () => {
    if (addressFormRef.value) {
        addressFormRef.value.resetFields()
    }
}

// 提交地址表单 (新增或修改)
const submitAddress = async () => {
    if (!addressFormRef.value) return

    await addressFormRef.value.validate(async (valid) => {
        if (valid) {
            submitting.value = true
            try {
                let url = ''

                if (isEditMode.value) {
                    url = `${BASE_URL}/api/AdminUserDeliveryInfoTableUpdate`
                } else {
                    url = `${BASE_URL}/api/AdminUserDeliveryInfoTableAdd`
                }

                // 发送请求
                const res = await axios.post(url, addressForm)

                // 后端返回 "Success" 或 "Fail"
                if (res.data === 'Success' || res.data === 'success') {
                    ElMessage.success(isEditMode.value ? '修改成功' : '添加成功')
                    dialogVisible.value = false
                    fetchDeliveryInfo() // 刷新列表
                } else {
                    ElMessage.error('操作失败')
                }
            } catch (error) {
                console.error(error)
                ElMessage.error('请求发生错误')
            } finally {
                submitting.value = false
            }
        }
    })
}

// 删除收货地址
const handleDelete = (row) => {
    ElMessageBox.confirm(
        '确定要删除这条收货地址吗？',
        '警告',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(async () => {
        try {
            // 后端需要 uID 和 uDIndex
            const payload = {
                uID: row.uID,
                uDIndex: row.uDIndex
            }
            const res = await axios.post(`${BASE_URL}/api/AdminUserDeliveryInfoTableDelete`, payload)

            if (res.data === 'Success' || res.data === 'success') {
                ElMessage.success('删除成功')
                fetchDeliveryInfo() // 刷新列表
            } else {
                ElMessage.error('删除失败')
            }
        } catch (error) {
            console.error(error)
            ElMessage.error('删除请求出错')
        }
    }).catch(() => {
        // 取消删除
    })
}

// 点击订单行跳转
const handleOrderClick = (row) => {
    router.push({
        name: 'AdminOrderDetailView',
        params: { oOrderID: row.oOrderID },
        state: { orderData: JSON.stringify(row) }
    })
}
</script>

<style scoped>
.detail-container {
    padding: 20px;
    background: #f0f2f5;
    min-height: 100vh;
}

.page-header {
    background: #fff;
    padding: 15px;
    margin-bottom: 20px;
}

.box-card {
    margin-bottom: 20px;
}

.card-header {
    font-weight: bold;
}

.clickable-row {
    cursor: pointer;
}
</style>