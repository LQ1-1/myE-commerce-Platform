<template>
    <div class="detail-container">
        <el-page-header @back="goBack" content="订单详情页" class="page-header" />

        <div class="content-wrapper">
            <!-- 1. 订单概览 -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>🧾 订单概览 (单号: {{ oOrderID }})</span>
                    </div>
                </template>
                <el-descriptions border :column="2">
                    <el-descriptions-item label="下单用户ID">{{ orderInfo.oOrdererID }}</el-descriptions-item>
                    <el-descriptions-item label="下单时间">{{ orderInfo.oDate }}</el-descriptions-item>
                    <el-descriptions-item label="当前状态">
                        <el-tag type="warning">{{ orderInfo.oStatus }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="收货人">{{ orderInfo.oReceiverName }}</el-descriptions-item>
                    <el-descriptions-item label="联系电话">{{ orderInfo.oContactPhone }}</el-descriptions-item>
                    <el-descriptions-item label="收货地址">{{ orderInfo.oDeliveryAddress }}</el-descriptions-item>
                    <el-descriptions-item label="备注">{{ orderInfo.oDeliveryNote }}</el-descriptions-item>
                </el-descriptions>
            </el-card>

            <!-- 2. 订单包含的商品 -->
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>🛒 包含商品列表 (点击商品ID查看详情)</span>
                    </div>
                </template>
                <el-table :data="productList" border stripe style="width: 100%" v-loading="loading">
                    <el-table-column prop="pID" label="商品ID" width="150">
                        <template #default="scope">
                            <el-link type="primary" @click="goToProductDetail(scope.row.pID)">{{ scope.row.pID
                                }}</el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="oPrice" label="成交单价" width="120">
                        <template #default="scope">¥{{ scope.row.oPrice }}</template>
                    </el-table-column>
                    <el-table-column prop="oAmount" label="数量" width="100" />
                    <el-table-column prop="oProductDeliveryStatus" label="发货状态" />
                    <el-table-column label="小计">
                        <template #default="scope">
                            ¥{{ (scope.row.oPrice * scope.row.oAmount).toFixed(2) }}
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

const route = useRoute()
const router = useRouter()
const BASE_URL = 'http://192.168.126.94:8082'

const oOrderID = ref(route.params.oOrderID)
const orderInfo = ref({})
const productList = ref([])
const loading = ref(false)

onMounted(() => {
    // 获取传递过来的订单头信息
    if (history.state && history.state.orderData) {
        orderInfo.value = JSON.parse(history.state.orderData)
    }
    fetchOrderProducts()
})

const goBack = () => router.back()

const fetchOrderProducts = async () => {
    loading.value = true
    try {
        // 接口需要传入 oOrderID
        const res = await axios.post(`${BASE_URL}/api/AdminGetOrderSpecificInfo`, { oOrderID: oOrderID.value })
        productList.value = res.data.data || []
    } catch (e) {
        ElMessage.error('获取订单商品失败')
    } finally {
        loading.value = false
    }
}

const goToProductDetail = (pID) => {
    router.push({
        name: 'AdminProductDetailView',
        params: { pID: pID }
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
</style>