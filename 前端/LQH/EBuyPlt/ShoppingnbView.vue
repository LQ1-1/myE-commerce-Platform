<template>
    <div class="shop-layout">
        <!-- 顶部导航栏 -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <!-- 点击 Logo 重置回推荐页 -->
                    <div class="logo" @click="resetFilters">EBuyPlt</div>

                    <div class="search-bar">
                        <!-- 搜索框：按下回车触发搜索 -->
                        <el-input v-model="searchQuery" placeholder="搜索商品名称/描述..." class="search-input"
                            :prefix-icon="Search" clearable @keyup.enter="handleSearch" @clear="handleSearch" />
                    </div>

                    <div class="actions">
                        <el-button :type="showFilter ? 'primary' : 'default'" :icon="Filter"
                            @click="showFilter = !showFilter">
                            {{ showFilter ? '收起筛选' : '高级筛选' }}
                        </el-button>

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

        <el-main class="main-content" v-loading="isLoading">

            <!-- 高级筛选面板 -->
            <el-collapse-transition>
                <div v-show="showFilter" class="filter-panel">
                    <el-form :model="filterForm" label-width="80px" size="default">
                        <el-row :gutter="20">
                            <el-col :xs="24" :sm="12" :md="6">
                                <el-form-item label="商品ID">
                                    <el-input v-model="filterForm.pID" placeholder="输入ID" clearable />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="6">
                                <el-form-item label="生产厂商">
                                    <el-input v-model="filterForm.pProducer" placeholder="输入厂商名" clearable />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="6">
                                <el-form-item label="商品类型">
                                    <el-select v-model="filterForm.pType" placeholder="选择或输入类型" style="width: 100%"
                                        filterable allow-create default-first-option clearable>
                                        <el-option label="全部" value="" />
                                        <el-option v-for="cat in categoryList" :key="cat" :label="cat" :value="cat" />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="6">
                                <el-form-item label="详情描述">
                                    <el-input v-model="filterForm.pInfo" placeholder="描述关键词" clearable />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="10">
                                <el-form-item label="价格区间">
                                    <div class="range-group">
                                        <el-input-number v-model="filterForm.minPrice" :min="0" :controls="false"
                                            placeholder="Min" style="width: 100px" />
                                        <span class="range-separator">-</span>
                                        <el-input-number v-model="filterForm.maxPrice" :min="0" :controls="false"
                                            placeholder="Max" style="width: 100px" />
                                    </div>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="24" :md="14">
                                <el-form-item label="发布日期">
                                    <el-date-picker v-model="filterForm.dateRange" type="daterange" range-separator="至"
                                        start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD"
                                        style="width: 100%" />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <div class="filter-actions">
                            <el-button @click="resetFilters">重置</el-button>
                            <el-button type="primary" @click="handleSearch">应用筛选</el-button>
                        </div>
                    </el-form>
                </div>
            </el-collapse-transition>

            <!-- 快捷分类栏 (点击直接搜索该类型) -->
            <div class="category-bar">
                <el-tabs v-model="activeCategory" class="category-tabs" @tab-change="handleCategoryChange">
                    <el-tab-pane label="全部" name="全部" />
                    <!-- 可以在这里修改为你实际的分类 -->
                    <el-tab-pane label="电子产品" name="电子产品" />
                    <el-tab-pane label="书籍" name="书籍" />
                    <el-tab-pane label="服装" name="服装" />
                    <el-tab-pane label="家具" name="家具" />
                </el-tabs>
            </div>

            <!-- 商品展示区 -->
            <div class="product-section">
                <div class="section-header">
                    <!-- 根据当前是否是推荐模式，可以动态显示标题 -->
                    <h2 class="section-title">
                        {{ isRecommendMode ? '为您推荐' : '商品列表' }}
                    </h2>
                    <span class="product-count">共 {{ products.length }} 件商品</span>
                </div>

                <el-row :gutter="20">
                    <el-col v-for="product in products" :key="product.pID" :xs="24" :sm="12" :md="8" :lg="6"
                        class="product-col">
                        <el-card class="product-card" shadow="hover">
                            <!-- 点击图片跳转详情页 -->
                            <div class="image-wrapper" @click="goToDetail(product.pID)">
                                <img :src="getProductImage(product.pImagePath)" class="product-image" />
                                <el-tag class="category-tag" size="small" effect="dark">{{ product.pType }}</el-tag>
                                <div v-if="product.pInventory <= 0" class="out-of-stock-mask">缺货</div>
                                <div v-if="product.pDiscount && product.pDiscount !== '无'" class="discount-badge">
                                    {{ "Discount : " + product.pDiscount }}
                                </div>
                                <div class="fav-icon-wrapper" @click.stop="toggleFavorite(product)">
                                    <el-icon :size="22" :color="isFavorite(product.pID) ? '#E6A23C' : '#909399'">
                                        <component :is="isFavorite(product.pID) ? StarFilled : Star" />
                                    </el-icon>
                                </div>
                            </div>

                            <div class="card-body">
                                <h3 class="product-title" @click="goToDetail(product.pID)">
                                    {{ product.pName }}
                                </h3>
                                <div class="simple-info">
                                    <span
                                        :class="['status-dot', product.pStatus === '正常' || product.pStatus === '上架' ? 'online' : 'offline']"></span>
                                    {{ product.pStatus }}
                                    <span class="producer-text">| {{ product.pProducer }}</span>
                                </div>
                                <div class="card-footer">
                                    <span class="price">¥ {{ product.pPrice }}</span>
                                    <el-button type="primary" size="small" :disabled="product.pInventory <= 0"
                                        @click="addToCart(product, 1)">
                                        加入购物车
                                    </el-button>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>

                <el-empty v-if="!isLoading && products.length === 0" description="没有找到商品">
                    <el-button type="primary" @click="resetFilters">重置条件</el-button>
                </el-empty>
            </div>
        </el-main>

        <el-footer class="footer">
            <p>© 2023 EBuyPlt. Built with Element Plus.</p>
        </el-footer>

        <!-- 购物车抽屉 -->
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

        <!-- 收藏夹抽屉 -->
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
                            <el-tag size="small">{{ item.pType }}</el-tag>
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
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
    ShoppingCart, Search, User, Delete, CirclePlus, Remove, Filter, Star, StarFilled
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const BASE_URL = 'http://192.168.66.94:8082'
const router = useRouter()
const route = useRoute()

// --- 状态定义 ---
const currentUserID = ref('')
const products = ref([])
const isLoading = ref(false)
const isRecommendMode = ref(false) // 标记当前是否处于推荐模式（用于UI显示）

// 搜索参数
const searchQuery = ref('')
const activeCategory = ref('全部')
const showFilter = ref(false)
const filterForm = reactive({
    pID: '',
    pProducer: '',
    pType: '',
    pInfo: '',
    minPrice: undefined,
    maxPrice: undefined,
    dateRange: null
})

// 购物车与收藏
const cartVisible = ref(false)
const cart = ref([])
const cartLoading = ref(false)
const favVisible = ref(false)
const favorites = ref([])
const favLoading = ref(false)

// --- 生命周期 ---
onMounted(() => {
    const storedUID = sessionStorage.getItem('uID')
    if (!storedUID) {
        ElMessage.error('请先登录')
        router.push('/')
        return
    }
    currentUserID.value = storedUID

    // --- 修改点：判断是否需要搜索 ---
    if (route.query.q) {
        searchQuery.value = route.query.q
        handleSearch() // 路由带参数，执行搜索
    } else {
        handleRecommend() // 默认无参数，执行推荐
    }

    fetchCart()
    fetchFavorites()
})

// --- 路由跳转 ---
const goToDetail = (id) => {
    router.push({ name: 'ProductDetailView', params: { pID:id } })
}

const handleLogout = () => {
    sessionStorage.removeItem('uID')
    router.push('/')
}

// --- 修改点：新增推荐逻辑 ---
const handleRecommend = async () => {
    isLoading.value = true
    isRecommendMode.value = true // 标记为推荐模式
    try {
        const res = await axios.post(`${BASE_URL}/api/ProductRecommend`, {
            uID: currentUserID.value
        })

        if (res.data && res.data.data) {
            products.value = res.data.data
        } else {
            products.value = []
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('获取推荐失败，请稍后再试')
    } finally {
        isLoading.value = false
    }
}

// --- 搜索逻辑 ---
const handleSearch = async () => {
    isLoading.value = true
    isRecommendMode.value = false // 标记为搜索模式
    try {
        const payload = {
            SearchDesciption: searchQuery.value,
            pID: filterForm.pID,
            pType: activeCategory.value !== '全部' ? activeCategory.value : filterForm.pType,
            pPrice_f: filterForm.minPrice || 0.0,
            pPrice_r: filterForm.maxPrice || 0.0,
            pProducer: filterForm.pProducer,
            pReleaseDate_f: filterForm.dateRange ? filterForm.dateRange[0] : "",
            pReleaseDate_r: filterForm.dateRange ? filterForm.dateRange[1] : "",
            pInfo: filterForm.pInfo
        }
        // alert(payload.SearchDesciption);

        const res = await axios.post(`${BASE_URL}/api/ProductSearch`, payload)

        if (res.data && res.data.data) {
            products.value = res.data.data
        } else {
            products.value = []
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('搜索失败')
    } finally {
        isLoading.value = false
    }
}

const handleCategoryChange = () => {
    // 切换分类 Tab 视为一种搜索筛选
    handleSearch()
}

// --- 修改点：重置逻辑 ---
const resetFilters = () => {
    searchQuery.value = ''
    activeCategory.value = '全部'
    filterForm.pID = ''
    filterForm.pProducer = ''
    filterForm.pType = ''
    filterForm.pInfo = ''
    filterForm.minPrice = undefined
    filterForm.maxPrice = undefined
    filterForm.dateRange = null
    
    // 重置后回到推荐页
    handleRecommend()
}

// --- API 通用与购物车/收藏逻辑 ---

const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/300x300?text=No+Image'
    if (path.startsWith('http')) return path
    const cleanPath = path.startsWith('/') ? path : '/' + path
    return `${BASE_URL}${cleanPath}`
}

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
            uID: currentUserID.value,
            pID: item.pID,
            cAmount: amount
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

const openCart = () => { cartVisible.value = true; fetchCart() }
const openFavorites = () => { favVisible.value = true; fetchFavorites() }

const isFavorite = (pID) => favorites.value.some(fav => fav.pID === pID)

const toggleFavorite = async (product) => {
    const exist = isFavorite(product.pID)
    try {
        if (exist) {
            await axios.post(`${BASE_URL}/api/FavouriteRemove`, { uID: currentUserID.value, pID: product.pID })
            ElMessage.success('已取消收藏')
        } else {
            await axios.post(`${BASE_URL}/api/FavouriteAdd`, { uID: currentUserID.value, pID: product.pID })
            ElMessage.success('已加入收藏')
        }
        fetchFavorites()
    } catch (e) { ElMessage.error('操作失败') }
}

const categoryList = computed(() => ['电子产品', '日用品', '书籍', '服装', '食品'])
const cartCount = computed(() => cart.value.reduce((total, item) => total + item.cAmount, 0))
const cartTotal = computed(() => cart.value.reduce((total, item) => total + (item.pPrice * item.cAmount), 0))
</script>

<style scoped>
/* 此处保留原有样式 */
.shop-layout {
    min-height: 100vh;
    background-color: #f5f7fa;
}

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

.main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    width: 100%;
}

.filter-panel {
    background: #fff;
    padding: 20px 20px 0 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.filter-actions {
    display: flex;
    justify-content: flex-end;
    padding-bottom: 20px;
}

.range-group {
    display: flex;
    align-items: center;
    gap: 5px;
}

.category-bar {
    background: #fff;
    padding: 10px 20px 0 20px;
    border-radius: 8px;
    margin-bottom: 20px;
}

.product-section {
    margin-top: 20px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 20px;
}

.section-title {
    margin: 0;
    font-size: 1.5rem;
    color: #303133;
}

.product-count {
    color: #909399;
    font-size: 14px;
}

.product-col {
    margin-bottom: 20px;
}

.product-card {
    transition: transform 0.3s;
    position: relative;
}

.product-card:hover {
    transform: translateY(-5px);
}

.image-wrapper {
    height: 200px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f9f9f9;
    position: relative;
    cursor: pointer;
}

.product-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.category-tag {
    position: absolute;
    top: 10px;
    left: 10px;
}

.discount-badge {
    position: absolute;
    top: 10px;
    right: 10px;
    background: #f56c6c;
    color: white;
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 12px;
}

.fav-icon-wrapper {
    position: absolute;
    bottom: 10px;
    right: 10px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 50%;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}

.out-of-stock-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    font-weight: bold;
    color: #909399;
}

.card-body {
    padding: 14px;
}

.product-title {
    margin: 0;
    font-size: 16px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: pointer;
}

.simple-info {
    font-size: 12px;
    color: #999;
    margin: 8px 0 10px;
    display: flex;
    align-items: center;
    gap: 5px;
}

.status-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    display: inline-block;
}

.status-dot.online {
    background-color: #67C23A;
}

.status-dot.offline {
    background-color: #909399;
}

.card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.price {
    color: #f56c6c;
    font-size: 18px;
    font-weight: bold;
}

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

.footer {
    text-align: center;
    color: #909399;
    padding: 20px;
}
</style>