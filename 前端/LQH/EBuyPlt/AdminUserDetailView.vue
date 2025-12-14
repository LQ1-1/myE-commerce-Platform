<template>
    <div class="detail-container">
        <el-page-header @back="goBack" content="用户详情页" class="page-header" />

        <div class="content-wrapper">
            <!-- 1. 用户基本信息卡片 -->
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

            <!-- 2. 收货地址列表 -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>📍 收货地址记录</span>
                    </div>
                </template>
                <el-table :data="deliveryList" border stripe style="width: 100%" v-loading="loadingDelivery">
                    <el-table-column type="index" label="序号" width="60" />
                    <el-table-column prop="uContactPersonName" label="收货人" width="120" />
                    <el-table-column prop="uContactPersonPhone" label="电话" width="120" />
                    <el-table-column prop="uDeliveryAddress" label="详细地址" />
                    <el-table-column prop="oPostalCode" label="邮编" width="100" />
                    <el-table-column prop="oDeliveryNote" label="备注" />
                </el-table>
            </el-card>

            <!-- 3. 订单记录 -->
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
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const BASE_URL = 'http://192.168.66.94:8082'

const uID = ref('')
const userInfo = ref({})
const deliveryList = ref([])
const orderList = ref([])
const loadingDelivery = ref(false)
const loadingOrder = ref(false)

onMounted(() => {
    // 1. 尝试从路由参数获取 uID
    if (route.params.uID) {
        uID.value = route.params.uID
    }

    // 2. 解析传递过来的 state 数据 (基本信息)
    if (history.state && history.state.userData) {
        try {
            const data = JSON.parse(history.state.userData)
            userInfo.value = data
            // 如果路由参数没取到，尝试从 state 数据里补救
            if (!uID.value && data.uID) {
                uID.value = data.uID
            }
        } catch (e) {
            console.error("解析用户数据失败", e)
        }
    }

    // 3. 调试日志：检查是否成功获取到了 uID
    console.log("当前页面获取到的 UserID:", uID.value)

    if (!uID.value) {
        ElMessage.error("未获取到用户ID，无法查询详情")
        return
    }

    // 4. 加载数据
    fetchDeliveryInfo()
    fetchOrderInfo()
})

const goBack = () => router.back()

// 获取收货地址
const fetchDeliveryInfo = async () => {
    loadingDelivery.value = true
    try {
        console.log("正在请求收货地址，参数:", { uID: uID.value })
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
        console.log("正在请求订单记录，参数:", { uID: uID.value })
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

// 点击订单行跳转
const handleOrderClick = (row) => {
    // 注意：这里 name 改为了 'AdminOrderDetailView' 以匹配之前 AdminView 中的命名习惯
    // 如果你的 router 中定义的 name 是 'AdminOrderDetail'，请改回 'AdminOrderDetail'
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