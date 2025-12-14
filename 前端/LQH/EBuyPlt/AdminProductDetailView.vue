<template>
    <div class="detail-container">
        <el-page-header @back="goBack" content="商品详情页" class="page-header" />

        <div class="content-wrapper" v-loading="loading">
            <el-card class="box-card" v-if="productInfo">
                <template #header>
                    <div class="card-header">
                        <span>🎁 {{ productInfo.pName }} (ID: {{ productInfo.pID }})</span>
                        <el-tag :type="productInfo.pStatus === '上架' ? 'success' : 'danger'" style="margin-left: 10px;">
                            {{ productInfo.pStatus }}
                        </el-tag>
                    </div>
                </template>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-descriptions border :column="2" size="large">
                            <el-descriptions-item label="商品名称">{{ productInfo.pName }}</el-descriptions-item>
                            <el-descriptions-item label="类型">{{ productInfo.pType }}</el-descriptions-item>
                            <el-descriptions-item label="价格">
                                <span style="color: red; font-size: 18px;">¥{{ productInfo.pPrice }}</span>
                            </el-descriptions-item>
                            <el-descriptions-item label="折扣">{{ productInfo.pDiscount }}</el-descriptions-item>
                            <el-descriptions-item label="库存">{{ productInfo.pInventory }}</el-descriptions-item>
                            <el-descriptions-item label="生产商">{{ productInfo.pProducer }}</el-descriptions-item>
                            <el-descriptions-item label="发布日期">{{ productInfo.pReleaseDate }}</el-descriptions-item>
                        </el-descriptions>

                        <div style="margin-top: 20px;">
                            <h4>商品简介：</h4>
                            <div class="info-box">{{ productInfo.pInfo }}</div>
                        </div>
                    </el-col>
                </el-row>
            </el-card>
            <el-empty v-else description="未找到商品信息" />
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
const BASE_URL = 'http://192.168.66.94:8082'

const pID = ref(route.params.pID)
const productInfo = ref(null)
const loading = ref(false)

onMounted(() => {
    fetchProductInfo()
})

const goBack = () => router.back()

const fetchProductInfo = async () => {
    loading.value = true
    try {
        // 调用新接口 AdminGetSpecificProductInfo
        const res = await axios.post(`${BASE_URL}/api/AdminGetSpecificProductInfo`, { pID: pID.value })
        // 接口返回的是 ArrayList, 我们取第一个
        if (res.data.data && res.data.data.length > 0) {
            productInfo.value = res.data.data[0]
        }
    } catch (e) {
        ElMessage.error('获取商品详情失败')
    } finally {
        loading.value = false
    }
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

.info-box {
    padding: 15px;
    background: #f9f9f9;
    border-radius: 4px;
    line-height: 1.6;
    color: #606266;
}
</style>