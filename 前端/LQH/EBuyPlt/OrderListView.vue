<template>
    <div class="order-layout">
        <!-- 顶部导航栏 -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <div class="logo" @click="router.push('/ShoppingnbView')">EBuyPlt 订单中心</div>
                    <div class="actions">
                        <el-button @click="router.push('/ShoppingnbView')">返回商城</el-button>

                        <!-- 收藏夹按钮 -->
                        <el-button :icon="Star" circle size="large" @click="openFavorites" title="我的收藏" />

                        <!-- 购物车按钮 (带角标) -->
                        <el-badge :value="cartCount" :hidden="cartCount === 0" class="item-badge">
                            <el-button :icon="ShoppingCart" circle size="large" @click="openCart" title="我的购物车" />
                        </el-badge>

                        <el-dropdown>
                            <el-avatar :icon="User" class="user-avatar" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item>用户ID: {{ currentUserID }}</el-dropdown-item>
                                    <el-dropdown-item @click="goToUserProfile">个人信息</el-dropdown-item> 
                                    <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>
        </el-affix>

        <el-main class="main-content" v-loading="loading">
            <div class="page-header">
                <h2>我的订单</h2>
                <el-button type="primary" link @click="fetchOrders">刷新列表</el-button>
            </div>

            <el-empty v-if="!loading && orderList.length === 0" description="暂无订单记录" />

            <div v-else class="order-list">
                <el-card v-for="order in orderList" :key="order.oOrderID" class="order-card" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <div class="header-left">
                                <span class="order-id">订单号: {{ order.oOrderID }}</span>
                                <span class="order-date">{{ order.oDate }}</span>
                            </div>
                            <div class="header-right">
                                <el-tag :type="getStatusTag(order.oStatus)" effect="dark">
                                    {{ translateStatus(order.oStatus) }}
                                </el-tag>
                            </div>
                        </div>
                    </template>

                    <!-- 订单商品列表 -->
                    <el-table :data="order.pProducts" style="width: 100%" border size="small">
                        <el-table-column prop="pID" label="商品ID" width="120" />
                        <el-table-column label="单价" width="120">
                            <template #default="scope">
                                ¥ {{ scope.row.oPrice }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="pAmount" label="数量" width="100" />
                        <el-table-column label="小计">
                            <template #default="scope">
                                <span class="subtotal">¥ {{ (scope.row.oPrice * scope.row.pAmount).toFixed(2) }}</span>
                            </template>
                        </el-table-column>
                    </el-table>

                    <div class="order-footer">
                        <div class="delivery-info" v-if="order.DeliveryInfo">
                            <p><strong>收货人:</strong> {{ order.DeliveryInfo.uContactPersonName }} ({{
                                order.DeliveryInfo.uContactPersonPhone }})</p>
                            <p><strong>地址:</strong> {{ order.DeliveryInfo.uDeliveryAddress }}</p>
                        </div>
                        <div class="order-actions">
                            <div class="total-amount">
                                订单总额: <span>¥ {{ calculateOrderTotal(order.pProducts).toFixed(2) }}</span>
                            </div>
                            <!-- 取消订单按钮 -->
                            <el-button v-if="order.oStatus !== 'Cancelled' && canCancel(order.oDate)" type="danger"
                                plain size="small" @click="handleCancelOrder(order.oOrderID)">
                                取消订单
                            </el-button>
                        </div>
                    </div>
                </el-card>
            </div>
        </el-main>

        <!-- 购物车抽屉 -->
        <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="500px">
            <div v-if="cart.length === 0" class="empty-cart">
                <el-empty description="购物车是空的" />
            </div>
            <div v-else class="cart-list" v-loading="cartLoading">
                <div class="cart-select-all">
                    <el-checkbox v-model="isAllSelected" @change="toggleSelectAll">全选</el-checkbox>
                </div>

                <div v-for="item in cart" :key="item.pID" class="cart-item">
                    <el-checkbox v-model="item.isSelected" class="item-checkbox" />
                    <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" @click="goToDetail(item.pID)" />
                    <div class="cart-item-info">
                        <h4 @click="goToDetail(item.pID)" class="item-link">{{ item.pName }}</h4>
                        <div class="cart-controls">
                            <span class="price">¥ {{ item.pPrice }}</span>
                            <div class="qty-control">
                                <el-button :icon="Remove" circle size="small" @click="changeCartQuantity(item, -1)" />
                                <span class="qty-text">{{ item.cAmount }}</span>
                                <el-button :icon="CirclePlus" circle size="small"
                                    @click="changeCartQuantity(item, 1)" />
                            </div>
                        </div>
                    </div>
                    <el-button type="danger" link :icon="Delete" @click="removeLineFromCart(item)" />
                </div>
            </div>

            <template #footer>
                <div class="cart-footer-bar">
                    <div class="total-info">
                        <span>已选 ({{ selectedCount }}):</span>
                        <span class="total-price">¥ {{ selectedTotal.toFixed(2) }}</span>
                    </div>
                    <el-button type="primary" size="large" @click="handleCheckout" :disabled="selectedCount === 0">
                        结算
                    </el-button>
                </div>
            </template>
        </el-drawer>

        <!-- 收藏夹抽屉 -->
        <el-drawer v-model="favVisible" title="我的收藏" direction="rtl" size="450px">
            <div v-loading="favLoading">
                <div v-if="favorites.length === 0">
                    <el-empty description="暂无收藏" />
                </div>
                <div v-else>
                    <div v-for="item in favorites" :key="item.pID" class="cart-item">
                        <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" />
                        <div class="cart-item-info">
                            <h4>{{ item.pName }}</h4>
                            <span class="price">¥ {{ item.pPrice }}</span>
                        </div>
                        <div class="fav-actions">
                            <el-button type="primary" circle :icon="ShoppingCart" size="small"
                                @click="addToCart(item)" />
                            <el-button type="danger" circle :icon="Delete" size="small" @click="toggleFavorite(item)" />
                        </div>
                    </div>
                </div>
            </div>
        </el-drawer>

        <!-- 结算确认弹窗 -->
        <el-dialog v-model="checkoutVisible" title="确认订单" width="500px">
            <p>您即将结算 {{ checkoutItems.length }} 件商品，共计 ¥ {{ selectedTotal.toFixed(2) }}</p>
            <div class="checkout-preview">
                <div v-for="item in checkoutItems" :key="item.pID" class="preview-item">
                    <span>{{ item.pName }} x {{ item.cAmount }}</span>
                    <span>¥ {{ (item.pPrice * item.cAmount).toFixed(2) }}</span>
                </div>
            </div>
            <template #footer>
                <el-button @click="checkoutVisible = false">取消</el-button>
                <el-button type="primary" @click="confirmOrderGeneration" :loading="checkoutLoading">确认支付</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ShoppingCart, User, Star, Delete, CirclePlus, Remove } from '@element-plus/icons-vue'

const BASE_URL = 'http://192.168.126.94:8082'
const router = useRouter()
const currentUserID = ref('')
const loading = ref(false)

// 订单数据
const orderList = ref([])

// 购物车与收藏
const cartVisible = ref(false)
const cart = ref([])
const cartLoading = ref(false)
const favVisible = ref(false)
const favorites = ref([])
const favLoading = ref(false)

// 结算相关
const checkoutVisible = ref(false)
const checkoutLoading = ref(false)
const checkoutItems = ref([])

onMounted(() => {
    const storedUID = sessionStorage.getItem('uID')
    if (!storedUID) {
        ElMessage.error('请先登录')
        router.push('/')
        return
    }
    currentUserID.value = storedUID

    // 初始化时加载所有必要数据
    fetchOrders()
    fetchCart()      // 修复：加载购物车数据
    fetchFavorites() // 修复：加载收藏数据
})

// --- 订单功能 ---
const fetchOrders = async () => {
    loading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/GetOrderRecords`, {
            uID: currentUserID.value
        })

        // 修复：增加可选链 ?. 防止数据为空时报错导致白屏
        if (res.data && res.data.data && res.data.data.OrderRecordList) {
            orderList.value = res.data.data.OrderRecordList.sort((a, b) => {
                return new Date(b.oDate) - new Date(a.oDate)
            })
        } else {
            orderList.value = []
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('获取订单记录失败')
    } finally {
        loading.value = false
    }
}

const calculateOrderTotal = (products) => {
    if (!products) return 0
    return products.reduce((sum, item) => sum + (item.oPrice * item.pAmount), 0)
}

const getStatusTag = (status) => {
    if (status === 'Paid' || status === 'Completed') return 'success'
    if (status === 'Cancelled') return 'info'
    return 'warning'
}

const translateStatus = (status) => {
    const map = {
        'Paid': '已支付',
        'Unpaid': '未支付',
        'Cancelled': '已取消',
        'Completed': '已完成'
    }
    return map[status] || status
}

const canCancel = (dateStr) => {
    const orderTime = new Date(dateStr).getTime()
    const now = new Date().getTime()
    const diffHours = (now - orderTime) / (1000 * 60 * 60)
    return diffHours < 1.0
}

const handleCancelOrder = (orderID) => {
    ElMessageBox.confirm(
        '确定要取消该订单吗？取消后库存将恢复。',
        '警告',
        { type: 'warning' }
    ).then(async () => {
        try {
            const payload = {
                uID: currentUserID.value,
                oOrderIDs: [{ oOrderID: orderID }]
            }
            const res = await axios.post(`${BASE_URL}/api/OrderCancelled`, payload)

            // 安全检查
            if (res.data && res.data.data && res.data.data.oOrderIDs && res.data.data.oOrderIDs.length > 0) {
                const feedback = res.data.data.oOrderIDs[0]
                if (feedback.FeedBack === 'Cancelled Accept') {
                    ElMessage.success('订单取消成功')
                    fetchOrders()
                } else {
                    ElMessage.error('取消失败：可能已超过取消时限')
                }
            } else {
                ElMessage.error('取消请求响应异常')
            }
        } catch (e) {
            console.error(e)
            ElMessage.error('请求异常')
        }
    })
}

const goToUserProfile = () => {
    router.push({
        path: '/UserProfileView', // 这里的路径要和你路由配置里的一致
        query: { uID: currentUserID.value } // 将当前用户ID传过去
    })
}

// --- 购物车功能 ---
const fetchCart = async () => {
    cartLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/ShoppingCartRecords`, { uID: currentUserID.value })
        const rawData = res.data && res.data.data ? res.data.data : []

        const oldSelectionMap = new Map(cart.value.map(i => [i.pID, i.isSelected]))

        cart.value = rawData.map(item => ({
            ...item,
            isSelected: oldSelectionMap.has(item.pID) ? oldSelectionMap.get(item.pID) : false
        }))

    } catch (e) {
        console.error(e)
    } finally {
        cartLoading.value = false
    }
}

const openCart = () => { cartVisible.value = true; fetchCart() }

const isAllSelected = computed({
    get: () => cart.value.length > 0 && cart.value.every(i => i.isSelected),
    set: (val) => {
        cart.value.forEach(item => item.isSelected = val)
    }
})

const toggleSelectAll = (val) => {
    cart.value.forEach(item => item.isSelected = val)
}

const selectedCount = computed(() => cart.value.filter(i => i.isSelected).length)

const selectedTotal = computed(() => {
    return cart.value
        .filter(item => item.isSelected)
        .reduce((total, item) => total + (Number(item.pPrice) * Number(item.cAmount)), 0)
})

const changeCartQuantity = async (item, change) => {
    try {
        if (change > 0) {
            await axios.post(`${BASE_URL}/api/ShoppingCartAdd`, { uID: currentUserID.value, pID: item.pID, cAmount: 1 })
        } else {
            await axios.post(`${BASE_URL}/api/ShoppingCartRemove`, { uID: currentUserID.value, pID: item.pID, cAmount: 1 })
        }
        fetchCart()
    } catch (e) {
        console.error(e)
        ElMessage.error('操作失败')
    }
}

const removeLineFromCart = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/ShoppingCartRemove`, { uID: currentUserID.value, pID: item.pID, cAmount: item.cAmount })
        fetchCart()
    } catch (e) {
        console.error(e)
        ElMessage.error('操作失败')
    }
}

// --- 结算逻辑 ---
const handleCheckout = async () => {
    checkoutItems.value = cart.value.filter(item => item.isSelected)
    if (checkoutItems.value.length === 0) return
    checkoutVisible.value = true
}

const confirmOrderGeneration = async () => {
    checkoutLoading.value = true
    try {
        const orderPayload = {
            uID: currentUserID.value,
            oDeliveryInfo: {
                uContactPersonName: "默认用户",
                uDeliveryAddress: "默认地址",
                uContactPersonPhone: "00000000000"
            },
            pProducts: checkoutItems.value.map(item => ({
                pID: String(item.pID),
                pAmount: Number(item.cAmount),
                oPrice: Number(item.pPrice)
            }))
        }

        await axios.post(`${BASE_URL}/api/OrderConfirm_OrderGenerate`, orderPayload)

        ElMessage.success('订单生成成功')
        checkoutVisible.value = false
        cartVisible.value = false

        // 刷新订单列表和购物车
        fetchOrders()
        fetchCart()

    } catch (e) {
        console.error(e)
        ElMessage.error('订单生成失败')
    } finally {
        checkoutLoading.value = false
    }
}

// --- 通用 & 收藏夹 ---
const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/100'
    if (path.startsWith('http')) return path
    const clean = path.startsWith('/') ? path : '/' + path
    return `${BASE_URL}${clean}`
}

const goToDetail = (id) => router.push({ name: 'ProductDetailView', params: { pID: id } })
const handleLogout = () => { sessionStorage.removeItem('uID'); router.push('/') }
const openFavorites = () => { favVisible.value = true; fetchFavorites() }

const fetchFavorites = async () => {
    favLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/FavouriteRecords`, { uID: currentUserID.value })
        favorites.value = res.data.data || []
    } catch (e) {
        console.error(e)
    } finally {
        favLoading.value = false
    }
}

const toggleFavorite = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/FavouriteRemove`, { uID: currentUserID.value, pID: item.pID })
        fetchFavorites()
    } catch (e) {
        console.error(e)
    }
}

// 这里的 addToCart 是为了收藏夹使用的快捷加入购物车
const addToCart = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/ShoppingCartAdd`, { uID: currentUserID.value, pID: item.pID, cAmount: 1 })
        ElMessage.success('已加入购物车')
        // 加入后顺便刷新下购物车数据，保证角标更新
        fetchCart()
    } catch (e) {
        console.error(e)
    }
}

const cartCount = computed(() => cart.value.reduce((total, item) => total + item.cAmount, 0))

</script>

<style scoped>
.order-layout {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.header {
    background: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    z-index: 100;
}

.header-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
}

.logo {
    font-size: 20px;
    font-weight: bold;
    color: #409EFF;
    cursor: pointer;
}

.actions {
    display: flex;
    align-items: center;
    gap: 15px;
}

.main-content {
    max-width: 1200px;
    margin: 20px auto;
    padding: 0 20px;
}

.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.order-card {
    margin-bottom: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-left {
    display: flex;
    gap: 20px;
    color: #606266;
}

.order-footer {
    margin-top: 15px;
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
}

.delivery-info {
    font-size: 13px;
    color: #909399;
}

.delivery-info p {
    margin: 2px 0;
}

.order-actions {
    text-align: right;
}

.total-amount {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
}

.total-amount span {
    color: #f56c6c;
}

.subtotal {
    color: #f56c6c;
    font-weight: bold;
}

/* 购物车样式 */
.cart-select-all {
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 10px;
}

.cart-item {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 15px;
    padding-bottom: 15px;
    border-bottom: 1px solid #f2f2f2;
}

.item-checkbox {
    margin-right: 5px;
}

.cart-item-img {
    width: 60px;
    height: 60px;
    border-radius: 4px;
    cursor: pointer;
    object-fit: cover;
}

.cart-item-info {
    flex: 1;
}

.item-link {
    cursor: pointer;
    margin: 0 0 5px 0;
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 150px;
}

.item-link:hover {
    color: #409EFF;
}

.cart-controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.qty-control {
    display: flex;
    align-items: center;
    gap: 5px;
}

.price {
    color: #f56c6c;
    font-weight: bold;
}

.cart-footer-bar {
    border-top: 1px solid #ebeef5;
    padding-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.total-price {
    font-size: 20px;
    color: #f56c6c;
    font-weight: bold;
    margin-left: 10px;
}

.checkout-preview {
    background: #f9f9f9;
    padding: 10px;
    border-radius: 4px;
    margin: 10px 0;
    max-height: 200px;
    overflow-y: auto;
}

.preview-item {
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    margin-bottom: 5px;
}
</style>