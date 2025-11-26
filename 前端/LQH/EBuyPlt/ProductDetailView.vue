<template>
    <div class="detail-layout">
        <!-- 复用顶部导航 (为了能在详情页也使用购物车和搜索) -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <div class="logo" @click="goHome">EBuyPlt</div>

                    <div class="search-bar">
                        <!-- 在详情页搜索，回车后跳回主页进行搜索 -->
                        <el-input v-model="searchQuery" placeholder="搜索其他商品..." class="search-input"
                            :prefix-icon="Search" @keyup.enter="handleSearchRedirect" />
                    </div>

                    <div class="actions">
                        <el-button @click="goHome">返回列表</el-button>

                        <el-button :icon="Star" circle size="large" @click="openFavorites" title="我的收藏" />

                        <el-badge :value="cartCount" :hidden="cartCount === 0" class="item-badge">
                            <el-button :icon="ShoppingCart" circle size="large" @click="openCart" />
                        </el-badge>

                        <el-dropdown>
                            <el-avatar :icon="User" class="user-avatar" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item>用户ID: {{ currentUserID }}</el-dropdown-item>
                                    <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>
        </el-affix>

        <el-main class="main-container" v-loading="isLoading">
            <div v-if="product" class="product-detail-wrapper">
                <el-row :gutter="40">
                    <!-- 左侧：图片轮播 -->
                    <el-col :xs="24" :md="12">
                        <div class="carousel-container">
                            <el-carousel trigger="click" height="400px" :autoplay="false" arrow="always">
                                <!-- 遍历 pImagePaths 数组 -->
                                <el-carousel-item v-for="(img, index) in product.pImagePaths" :key="index">
                                    <!-- getProductImage 会处理相对路径 -->
                                    <img :src="getProductImage(img)" class="carousel-img" />
                                </el-carousel-item>

                                <!-- 处理没有图片的情况 -->
                                <el-carousel-item v-if="!product.pImagePaths || product.pImagePaths.length === 0">
                                    <div class="no-image">暂无图片</div>
                                </el-carousel-item>
                            </el-carousel>
                        </div>
                    </el-col>

                    <!-- 右侧：商品信息 -->
                    <el-col :xs="24" :md="12">
                        <div class="info-container">
                            <div class="info-header">
                                <el-tag effect="dark">{{ product.pType }}</el-tag>
                                <span class="producer">厂商: {{ product.pProducer }}</span>
                            </div>

                            <h1 class="product-name">{{ product.pName }}</h1>
                            <p class="product-id">Product ID: {{ product.pID }}</p>

                            <div class="price-box">
                                <span class="currency">¥</span>
                                <span class="amount">{{ product.pPrice }}</span>
                                <span v-if="product.pDiscount > 0" class="discount-tag">
                                    Discount: {{ product.pDiscount }}
                                </span>
                            </div>

                            <div class="meta-info">
                                <p><strong>库存状态:</strong>
                                    <span :class="product.pInventory > 0 ? 'text-green' : 'text-red'">
                                        {{ product.pInventory > 0 ? `有货 (剩余${product.pInventory})` : '缺货' }}
                                    </span>
                                </p>
                                <p><strong>发布日期:</strong> {{ product.pReleaseDate }}</p>
                                <p><strong>商品状态:</strong> {{ product.pStatus }}</p>
                            </div>

                            <div class="description-box">
                                <h3>商品详情</h3>
                                <p>{{ product.pInfo }}</p>
                            </div>

                            <div class="action-buttons">
                                <el-input-number v-model="buyQuantity" :min="1" :max="product.pInventory"
                                    :disabled="product.pInventory <= 0" style="margin-right: 15px;" />

                                <el-button type="primary" size="large" :icon="ShoppingCart"
                                    :disabled="product.pInventory <= 0" @click="addToCart(product, buyQuantity)">
                                    加入购物车
                                </el-button>

                                <el-button size="large" :icon="isFavorite(product.pID) ? StarFilled : Star"
                                    :type="isFavorite(product.pID) ? 'warning' : 'default'"
                                    @click="toggleFavorite(product)">
                                    {{ isFavorite(product.pID) ? '已收藏' : '收藏' }}
                                </el-button>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <el-empty v-else description="加载商品信息失败或商品不存在"></el-empty>
        </el-main>

        <!-- 购物车抽屉 (复用逻辑) -->
        <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="450px">
            <div v-if="cart.length === 0" class="empty-cart">
                <el-empty description="购物车是空的" />
            </div>
            <div v-else class="cart-list" v-loading="cartLoading">
                <div v-for="item in cart" :key="item.pID" class="cart-item">
                    <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" />
                    <div class="cart-item-info">
                        <h4>{{ item.pName }}</h4>
                        <div class="cart-controls">
                            <span class="price">¥ {{ item.pPrice }}</span>
                            <div class="qty-control">
                                <el-button :icon="Remove" circle size="small" @click="changeCartQuantity(item, -1)" />
                                <span class="qty-text">{{ item.cAmount }}</span>
                                <el-button :icon="CirclePlus" circle size="small" @click="addToCart(item, 1)" />
                            </div>
                        </div>
                    </div>
                    <el-button type="danger" link :icon="Delete" @click="removeLineFromCart(item)" />
                </div>
            </div>
            <template #footer>
                <div class="cart-footer">
                    <div class="total-text">总计: <span>¥ {{ cartTotal }}</span></div>
                    <el-button type="primary" size="large" class="checkout-btn">去结算</el-button>
                </div>
            </template>
        </el-drawer>

        <!-- 收藏夹抽屉 (复用逻辑) -->
        <el-drawer v-model="favVisible" title="我的收藏" direction="rtl" size="450px">
            <div v-if="favorites.length === 0" class="empty-cart">
                <el-empty description="暂无收藏" />
            </div>
            <div v-else class="cart-list" v-loading="favLoading">
                <div v-for="item in favorites" :key="item.pID" class="cart-item">
                    <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" />
                    <div class="cart-item-info">
                        <h4>{{ item.pName }}</h4>
                        <div class="cart-controls">
                            <span class="price">¥ {{ item.pPrice }}</span>
                        </div>
                    </div>
                    <div class="fav-actions">
                        <el-button type="primary" circle :icon="ShoppingCart" size="small"
                            @click="addToCart(item, 1)" />
                        <el-button type="danger" circle :icon="Delete" size="small" @click="toggleFavorite(item)" />
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Search, User, Delete, CirclePlus, Remove, Star, StarFilled } from '@element-plus/icons-vue'

const BASE_URL = 'http://192.168.66.94:8082'
const route = useRoute()
const router = useRouter()

const currentUserID = ref('')
const isLoading = ref(false)
const product = ref(null) // 存放商品详情对象 (ProductClick_jsonSend)
const searchQuery = ref('')
const buyQuantity = ref(1)

// 购物车与收藏
const cartVisible = ref(false)
const cart = ref([])
const cartLoading = ref(false)
const favVisible = ref(false)
const favorites = ref([])
const favLoading = ref(false)

onMounted(() => {
   const pID = route.params.pID
    
    if (pID) {
        // 2. 发起请求
        fetchProductDetail(pID)
    } else {
        ElMessage.error('参数错误：缺少商品ID')
        router.push('/ShoppingnbView') // 如果没有ID，跳回主页
    }

    // 后台加载购物车和收藏，为了按钮状态显示正确
    fetchCart()
    fetchFavorites()
})

// --- 核心逻辑：获取商品详情 (/api/ProductClick) ---
const fetchProductDetail = async (id) => {
    isLoading.value = true
    try {
        // 注意：这里发送给后端的参数名必须和后端接收的一致
        // 假设后端接口接收 {"pID": "..."}
        const payload = { pID: id }
        
        const res = await axios.post(`${BASE_URL}/api/ProductClick`, payload)

        if (res.data && res.data.data) {
            // 赋值给 product 响应式对象
            // 此时 product.value.pImagePaths 就是一个数组 ['/img/1.jpg', '/img/2.jpg']
            product.value = res.data.data 
        } else {
            ElMessage.error('未找到该商品信息')
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('加载详情失败')
    } finally {
        isLoading.value = false
    }
}

// --- 导航逻辑 ---
const goHome = () => {
    router.push('/ShoppingnbView')
}

const handleSearchRedirect = () => {
    if (searchQuery.value.trim()) {
        router.push({ path: '/ShoppingnbView', query: { q: searchQuery.value } })
    }
}

const handleLogout = () => {
    sessionStorage.removeItem('uID')
    router.push('/')
}

// --- 通用工具 ---
const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/400x400?text=No+Image'
    if (path.startsWith('http')) return path
    const cleanPath = path.startsWith('/') ? path : '/' + path
    return `${BASE_URL}${cleanPath}`
}

// --- 购物车与收藏逻辑 (与主页一致，为了功能完整性复制) ---
const fetchCart = async () => {
    if (!currentUserID.value) return
    cartLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/ShoppingCartRecords`, { uID: currentUserID.value })
        cart.value = res.data && res.data.data ? res.data.data : []
    } catch (e) { console.error(e) }
    finally { cartLoading.value = false }
}

const fetchFavorites = async () => {
    if (!currentUserID.value) return
    favLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/FavouriteRecords`, { uID: currentUserID.value })
        favorites.value = res.data && res.data.data ? res.data.data : []
    } catch (e) { console.error(e) }
    finally { favLoading.value = false }
}

const addToCart = async (item, amount = 1) => {
    if (item.pInventory !== undefined && Number(item.pInventory) <= 0) {
        ElMessage.warning('商品已缺货')
        return
    }
    try {
        await axios.post(`${BASE_URL}/api/ShoppingCartAdd`, {
            uID: currentUserID.value, pID: item.pID, cAmount: amount
        })
        ElMessage.success('已加入购物车')
        fetchCart()
    } catch (e) { ElMessage.error('操作失败') }
}

const changeCartQuantity = async (item, change) => {
    if (change > 0) {
        await addToCart(item, change)
    } else {
        if (item.cAmount <= 1) {
            await removeLineFromCart(item)
            return
        }
        try {
            await axios.post(`${BASE_URL}/api/ShoppingCartRemove`, {
                uID: currentUserID.value, pID: item.pID, cAmount: Math.abs(change)
            })
            fetchCart()
        } catch (e) { ElMessage.error('操作失败') }
    }
}

const removeLineFromCart = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/ShoppingCartRemove`, {
            uID: currentUserID.value, pID: item.pID, cAmount: item.cAmount
        })
        fetchCart()
    } catch (e) { ElMessage.error('移除失败') }
}

const isFavorite = (pID) => favorites.value.some(fav => fav.pID === pID)

const toggleFavorite = async (item) => {
    const exist = isFavorite(item.pID)
    try {
        if (exist) {
            await axios.post(`${BASE_URL}/api/FavouriteRemove`, { uID: currentUserID.value, pID: item.pID })
            ElMessage.success('已取消收藏')
        } else {
            await axios.post(`${BASE_URL}/api/FavouriteAdd`, { uID: currentUserID.value, pID: item.pID })
            ElMessage.success('已加入收藏')
        }
        fetchFavorites()
    } catch (e) { ElMessage.error('操作失败') }
}

const openCart = () => { cartVisible.value = true; fetchCart() }
const openFavorites = () => { favVisible.value = true; fetchFavorites() }

const cartCount = computed(() => cart.value.reduce((total, item) => total + item.cAmount, 0))
const cartTotal = computed(() => cart.value.reduce((total, item) => total + (item.pPrice * item.cAmount), 0))
</script>

<style scoped>
/* 复用部分 Header 样式 */
.header {
    background: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 0;
    z-index: 100;
}

.header-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
}

.logo {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
    cursor: pointer;
}

.search-bar {
    width: 30%;
    max-width: 400px;
}

.actions {
    display: flex;
    align-items: center;
    gap: 15px;
}

/* 详情页特有样式 */
.main-container {
    max-width: 1100px;
    margin: 20px auto;
    padding: 0 20px;
}

.product-detail-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 30px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

/* 轮播图 */
.carousel-container {
    background: #f9f9f9;
    border-radius: 8px;
    overflow: hidden;
    height: 400px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.carousel-img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}

.no-image {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
}

/* 信息区 */
.info-container {
    padding-left: 20px;
}

.info-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
}

.producer {
    font-size: 14px;
    color: #909399;
}

.product-name {
    font-size: 28px;
    margin: 0 0 10px 0;
    color: #303133;
}

.product-id {
    font-size: 13px;
    color: #C0C4CC;
    margin-bottom: 20px;
}

.price-box {
    color: #f56c6c;
    margin-bottom: 20px;
    display: flex;
    align-items: flex-end;
}

.currency {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 4px;
}

.amount {
    font-size: 32px;
    font-weight: bold;
    margin-left: 5px;
}

.discount-tag {
    margin-left: 15px;
    background: #f56c6c;
    color: #fff;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 4px;
    height: 20px;
    line-height: 20px;
    margin-bottom: 8px;
}

.meta-info {
    font-size: 14px;
    color: #606266;
    line-height: 1.8;
    margin-bottom: 25px;
    border-bottom: 1px solid #ebeef5;
    padding-bottom: 20px;
}

.text-green {
    color: #67C23A;
}

.text-red {
    color: #F56C6C;
}

.description-box h3 {
    margin: 0 0 10px 0;
    font-size: 16px;
}

.description-box p {
    color: #606266;
    line-height: 1.6;
    margin-bottom: 30px;
}

.action-buttons {
    display: flex;
    align-items: center;
    gap: 15px;
}

/* 抽屉复用样式 */
.cart-list {
    height: calc(100vh - 200px);
    overflow-y: auto;
}

.cart-item {
    display: flex;
    align-items: center;
    gap: 15px;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;
}

.cart-item-img {
    width: 60px;
    height: 60px;
    border-radius: 4px;
    object-fit: cover;
}

.cart-item-info {
    flex: 1;
}

.cart-controls {
    margin-top: 5px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.cart-footer {
    border-top: 1px solid #e4e7ed;
    padding-top: 20px;
}

.total-text {
    font-size: 18px;
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
    font-weight: bold;
}

.checkout-btn {
    width: 100%;
}

.fav-actions {
    display: flex;
    flex-direction: column;
    gap: 8px;
}
</style>