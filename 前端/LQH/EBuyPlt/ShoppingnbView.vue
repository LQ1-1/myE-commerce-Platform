<template>
    <div class="shop-layout">
        <!-- 顶部导航栏 -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <div class="logo" @click="resetFilters">EBuyPlt</div>

                    <div class="search-bar">
                        <el-input v-model="searchQuery" placeholder="搜索商品名称/描述..." class="search-input"
                            :prefix-icon="Search" clearable @keyup.enter="handleSearch" @clear="handleSearch" />
                    </div>

                    <div class="actions">
                        <el-button :type="showFilter ? 'primary' : 'default'" :icon="Filter"
                            @click="showFilter = !showFilter">
                            {{ showFilter ? '收起筛选' : '高级筛选' }}
                        </el-button>

                        <!-- 新增：订单列表跳转按钮 -->
                        <el-button :icon="Tickets" circle size="large" @click="router.push('/OrderListView')"
                            title="我的订单" />

                        <el-button :icon="Star" circle size="large" @click="openFavorites" title="我的收藏" />

                        <el-badge :value="cartCount" :hidden="cartCount === 0" class="item-badge">
                            <el-button :icon="ShoppingCart" circle size="large" @click="openCart" />
                        </el-badge>

                        <el-dropdown>
                            <el-avatar :icon="User" class="user-avatar" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item>用户ID: {{ currentUserID }}</el-dropdown-item>
                                    <el-dropdown-item @click="goToUserProfile">个人信息</el-dropdown-item>
                                    <el-dropdown-item @click="router.push('/OrderListView')">我的订单</el-dropdown-item>
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
                            <el-button type="primary" @click="applyAdvancedFilter">应用筛选</el-button>
                        </div>
                    </el-form>
                </div>
            </el-collapse-transition>

            <!-- 快捷分类栏 -->
            <div class="category-bar">
                <el-tabs v-model="activeCategory" class="category-tabs" @tab-change="handleCategoryChange">
                    <!-- 修改：将 "全部" 替换为 "推荐" -->
                    <el-tab-pane label="推荐" name="推荐" />

                    <!-- 循环渲染其他分类 -->
                    <el-tab-pane v-for="type in categoryList" :key="type" :label="type" :name="type" />
                </el-tabs>
            </div>

            <!-- 商品展示区 -->
            <div class="product-section">
                <div class="section-header">
                    <h2 class="section-title">
                        {{ isRecommendMode ? '为您推荐' : '商品列表' }}
                    </h2>
                    <span class="product-count">共 {{ products.length }} 件商品</span>
                </div>

                <el-row :gutter="20">
                    <el-col v-for="product in products" :key="product.pID" :xs="24" :sm="12" :md="8" :lg="6"
                        class="product-col">
                        <el-card class="product-card" shadow="hover">
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
        <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="480px">
            <div v-if="cart.length === 0" class="empty-cart">
                <el-empty description="购物车是空的" />
                <div class="drawer-link-box">
                    <el-button type="primary" link @click="router.push('/OrderListView')">查看历史订单 ></el-button>
                </div>
            </div>
            <div v-else class="cart-container" v-loading="cartLoading">
                <!-- 全选栏 -->
                <div class="cart-header-actions">
                    <el-checkbox v-model="isAllSelected" @change="toggleSelectAll">全选</el-checkbox>
                    <el-button type="primary" link @click="router.push('/OrderListView')">我的订单 ></el-button>
                </div>

                <div class="cart-list">
                    <div v-for="item in cart" :key="item.pID" class="cart-item">
                        <!-- 单选框 -->
                        <div class="checkbox-wrapper">
                            <el-checkbox v-model="item.isSelected" />
                        </div>

                        <!-- 商品信息 -->
                        <div class="cart-content-wrapper clickable-item" @click="goToDetail(item.pID)">
                            <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" />
                            <div class="cart-item-info">
                                <h4 class="cart-item-title">{{ item.pName }}</h4>
                                <div class="cart-controls">
                                    <span class="price">¥ {{ item.pPrice }}</span>
                                    <div class="qty-control">
                                        <el-button :icon="Remove" circle size="small"
                                            @click.stop="changeCartQuantity(item, -1)" />
                                        <span class="qty-text">{{ item.cAmount }}</span>
                                        <el-button :icon="CirclePlus" circle size="small"
                                            @click.stop="addToCart(item, 1)" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <el-button type="danger" link :icon="Delete" @click.stop="removeLineFromCart(item)" />
                    </div>
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

        <!-- 结算流程弹窗 -->
        <el-dialog v-model="checkoutVisible" title="确认收货信息" width="500px" destroy-on-close>
            <div v-loading="checkoutLoading">
                <div v-if="existingAddresses.length > 0 && !isAddingNewAddress">
                    <p class="dialog-tip">请选择收货地址：</p>
                    <div class="address-list">
                        <div v-for="(addr, index) in existingAddresses" :key="index"
                            :class="['address-card', selectedAddressIndex === index ? 'active' : '']"
                            @click="selectedAddressIndex = index">
                            <div class="addr-user">{{ addr.uContactPersonName }} ({{ addr.uContactPersonPhone }})</div>
                            <div class="addr-detail">{{ addr.uDeliveryAddress }}</div>
                            <div class="addr-note" v-if="addr.oDeliveryNote">备注: {{ addr.oDeliveryNote }}</div>
                        </div>
                    </div>
                    <el-button link type="primary" @click="isAddingNewAddress = true">使用新地址 +</el-button>
                </div>

                <div v-else>
                    <p class="dialog-tip">{{ existingAddresses.length === 0 ? '暂无收货记录，请填写：' : '新增收货地址：' }}</p>
                    <el-form :model="newAddressForm" label-width="80px" size="small">
                        <el-form-item label="收货人">
                            <el-input v-model="newAddressForm.uContactPersonName" />
                        </el-form-item>
                        <el-form-item label="电话">
                            <el-input v-model="newAddressForm.uContactPersonPhone" maxlength="11"
                                placeholder="请输入11位手机号"
                                @input="(val) => (newAddressForm.uContactPersonPhone = val.replace(/[^\d]/g, ''))" />
                        </el-form-item>
                        <el-form-item label="性别">
                            <el-select v-model="newAddressForm.uContactPersonGender" placeholder="选择或输入性别" filterable
                                allow-create default-first-option clearable style="width: 100%">
                                <el-option label="男" value="男" />
                                <el-option label="女" value="女" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="newAddressForm.uContactPersonEmail" />
                        </el-form-item>
                        <el-form-item label="地址">
                            <el-input v-model="newAddressForm.uDeliveryAddress" type="textarea" />
                        </el-form-item>
                        <el-form-item label="邮编">
                            <el-input v-model="newAddressForm.oPostalCode" />
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input v-model="newAddressForm.oDeliveryNote" />
                        </el-form-item>
                    </el-form>
                    <el-button link type="info" v-if="existingAddresses.length > 0"
                        @click="isAddingNewAddress = false">返回选择列表</el-button>
                </div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="checkoutVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmOrderGeneration" :loading="checkoutLoading">
                        确认订单
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 收藏夹抽屉 -->
        <el-drawer v-model="favVisible" title="我的收藏" direction="rtl" size="450px">
            <div v-if="favorites.length === 0" class="empty-cart">
                <el-empty description="暂无收藏" />
            </div>
            <div v-else class="cart-list" v-loading="favLoading">
                <div v-for="item in favorites" :key="item.pID" class="cart-item clickable-item"
                    @click="goToDetail(item.pID)">
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
                            @click.stop="addToCart(item, 1)" />
                        <el-button type="danger" circle :icon="Delete" size="small"
                            @click.stop="toggleFavorite(item)" />
                    </div>
                </div>
            </div>
        </el-drawer>

    </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
// 新增引入 Tickets 图标
import {
    ShoppingCart, Search, User, Delete, CirclePlus, Remove, Filter, Star, StarFilled, Tickets
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const BASE_URL = 'http://192.168.126.94:8082'
const router = useRouter()
const route = useRoute()

// 状态定义
const currentUserID = ref('')
const products = ref([])
const isLoading = ref(false)
const isRecommendMode = ref(false)
const searchQuery = ref('')
const activeCategory = ref('推荐')
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

// 结算相关状态
const checkoutVisible = ref(false)
const checkoutLoading = ref(false)
const existingAddresses = ref([])
const selectedAddressIndex = ref(0)
const isAddingNewAddress = ref(false)
const newAddressForm = reactive({
    uContactPersonName: '',
    uContactPersonPhone: '',
    uContactPersonGender: '男',
    uContactPersonEmail: '',
    uDeliveryAddress: '',
    oPostalCode: '',
    oDeliveryNote: ''
})

// 生命周期
onMounted(() => {
    const storedUID = sessionStorage.getItem('uID')
    if (!storedUID) {
        ElMessage.error('请先登录')
        router.push('/')
        return
    }
    currentUserID.value = storedUID
    fetchCategories()
    if (route.query.q) {
        searchQuery.value = route.query.q
        handleSearch()
    } else {
        handleRecommend()
    }
    fetchCart()
    fetchFavorites()
})

const goToUserProfile = () => {
    router.push({
        path: '/UserProfileView', // 这里的路径要和你路由配置里的一致
        query: { uID: currentUserID.value } // 将当前用户ID传过去
    })
}

// 路由跳转
const goToDetail = (id) => { router.push({ name: 'ProductDetailView', params: { pID: id } }) }
const handleLogout = () => { sessionStorage.removeItem('uID'); router.push('/') }

// 推荐逻辑
const handleRecommend = async () => {
    isLoading.value = true
    isRecommendMode.value = true
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

// 搜索逻辑
const handleSearch = async () => {
    isLoading.value = true
    isRecommendMode.value = false

    try {
        // 构造请求参数，完全信任 filterForm 中的数据
        const payload = {
            SearchDesciption: searchQuery.value,
            pID: filterForm.pID,

            // 【重点】直接取表单里的类型，不再看 activeCategory
            pType: filterForm.pType,

            pPrice_f: filterForm.minPrice || 0.0,
            pPrice_r: filterForm.maxPrice || 0.0,
            pProducer: filterForm.pProducer,
            pReleaseDate_f: filterForm.dateRange ? filterForm.dateRange[0] : "",
            pReleaseDate_r: filterForm.dateRange ? filterForm.dateRange[1] : "",
            pInfo: filterForm.pInfo
        }

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

const handleCategoryChange = (val) => {
    // 1. 如果点击的是“推荐”
    if (val === '推荐') {
        // 清空类型，进入推荐模式
        filterForm.pType = ''
        handleRecommend()
        return
    }

    // 2. 如果点击的是具体分类（如“电子产品”）
    // 【重点逻辑】：重置高级筛选的其他条件，确保导航栏点击是“纯净”的分类搜索
    filterForm.pID = ''
    filterForm.pProducer = ''
    filterForm.pInfo = ''
    filterForm.minPrice = undefined
    filterForm.maxPrice = undefined
    filterForm.dateRange = null

    // 把当前点击的 Tab 类型同步给表单
    filterForm.pType = val

    // 立即执行搜索
    handleSearch()
}

const applyAdvancedFilter = () => {
    // 【重点逻辑】：将导航栏状态置空，视觉上变成“未选择”
    // 注意：Element Plus Tabs 如果绑定值不在选项列表中，就会不显示下划线，刚好符合需求
    activeCategory.value = ''

    // 注意：这里不需要清空 filterForm，因为用户就是要用这里面的值去搜

    // 执行搜索
    handleSearch()
}

const resetFilters = () => {
    searchQuery.value = ''
    activeCategory.value = '推荐'
    filterForm.pID = ''
    filterForm.pProducer = ''
    filterForm.pType = ''
    filterForm.pInfo = ''
    filterForm.minPrice = undefined
    filterForm.maxPrice = undefined
    filterForm.dateRange = null
    handleRecommend()
}

const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/300x300?text=No+Image'
    if (path.startsWith('http')) return path
    const cleanPath = path.startsWith('/') ? path : '/' + path
    return `${BASE_URL}${cleanPath}`
}

// 购物车逻辑
const fetchCart = async () => {
    if (!currentUserID.value) return
    cartLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/ShoppingCartRecords`, { uID: currentUserID.value })
        const rawList = res.data && res.data.data ? res.data.data : []

        // 保留勾选状态逻辑
        const oldMap = new Map(cart.value.map(i => [i.pID, i.isSelected]))

        cart.value = rawList.map(item => ({
            ...item,
            isSelected: oldMap.has(item.pID) ? oldMap.get(item.pID) : false
        }))
    } catch (e) { console.error(e) }
    finally { cartLoading.value = false }
}

const isAllSelected = computed({
    get: () => cart.value.length > 0 && cart.value.every(item => item.isSelected),
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
    } catch (e) { console.error(e); ElMessage.error('操作失败') }
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
        } catch (e) { console.error(e); ElMessage.error('操作失败') }
    }
}

const removeLineFromCart = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/ShoppingCartRemove`, {
            uID: currentUserID.value, pID: item.pID, cAmount: item.cAmount
        })
        fetchCart()
    } catch (e) { console.error(e); ElMessage.error('移除失败') }
}

const openCart = () => { cartVisible.value = true; fetchCart() }

// 收藏夹逻辑
const fetchFavorites = async () => {
    if (!currentUserID.value) return
    favLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/FavouriteRecords`, { uID: currentUserID.value })
        favorites.value = res.data && res.data.data ? res.data.data : []
    } catch (e) { console.error(e) }
    finally { favLoading.value = false }
}

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
    } catch (e) { console.error(e); ElMessage.error('操作失败') }
}

const cartCount = computed(() => cart.value.reduce((total, item) => total + item.cAmount, 0))
// const categoryList = computed(() => ['电子产品', '日用品', '书籍', '服装', '食品'])

// 结算逻辑
const handleCheckout = async () => {
    if (!currentUserID.value) return ElMessage.error("未登录")
    if (selectedCount.value === 0) {
        return ElMessage.warning('请先勾选要结算的商品')
    }

    checkoutVisible.value = true
    checkoutLoading.value = true
    isAddingNewAddress.value = false

    newAddressForm.uContactPersonGender = ''
    newAddressForm.uContactPersonName = ''
    newAddressForm.uContactPersonPhone = ''
    newAddressForm.uContactPersonEmail = ''
    newAddressForm.uDeliveryAddress = ''
    newAddressForm.oPostalCode = ''
    newAddressForm.oDeliveryNote = ''

    try {
        const res = await axios.post(`${BASE_URL}/api/OrderConfirm_DeliveryCheck`, {
            uID: currentUserID.value
        })

        if (res.data && res.data.data && res.data.data.DeliveryInfos && res.data.data.DeliveryInfos.length > 0) {
            existingAddresses.value = res.data.data.DeliveryInfos
            selectedAddressIndex.value = 0
        } else {
            existingAddresses.value = []
            isAddingNewAddress.value = true
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('获取收货信息失败')
    } finally {
        checkoutLoading.value = false
    }
}

const confirmOrderGeneration = async () => {
    let finalDeliveryInfo = null

    if (isAddingNewAddress.value || existingAddresses.value.length === 0) {
        if (!newAddressForm.uContactPersonName || !newAddressForm.uDeliveryAddress || !newAddressForm.uContactPersonPhone) {
            return ElMessage.warning('请填写完整的收货信息')
        }

        finalDeliveryInfo = {
            uID: currentUserID.value,
            ...newAddressForm
        }

        try {
            checkoutLoading.value = true
            const addrRes = await axios.post(`${BASE_URL}/api/OrderConfirm_NewDeliveryRecord`, finalDeliveryInfo)
            // alert(addrRes.data);
            if (addrRes.data !== 'Request Accept') {
                throw new Error('地址保存失败')
            }
        } catch (e) {
            console.error(e)
            checkoutLoading.value = false
            return ElMessage.error('保存收货地址失败，请重试')
        }
    } else {
        finalDeliveryInfo = existingAddresses.value[selectedAddressIndex.value]
    }

    try {
        checkoutLoading.value = true

        const selectedItems = cart.value.filter(item => item.isSelected)

        const orderPayload = {
            uID: currentUserID.value,
            oDeliveryInfo: finalDeliveryInfo,
            pProducts: selectedItems.map(item => ({
                pID: String(item.pID),
                pAmount: Number(item.cAmount),
                oPrice: Number(item.pPrice)
            }))
        }

        const orderRes = await axios.post(`${BASE_URL}/api/OrderConfirm_OrderGenerate`, orderPayload)

        checkoutVisible.value = false

        const feedback = orderRes.data.data
        const failedItems = feedback.pProducts.filter(p => p.pFeedback === 'Inventory Insufficient')

        if (failedItems.length > 0) {
            ElMessage.error(`部分商品库存不足，订单创建失败`)
            return
        }

        // 本地移除已购买商品
        cart.value = cart.value.filter(item => !item.isSelected)

        const newOrderID = feedback.oOrderID
        if (!newOrderID) {
            ElMessage.error('订单生成异常，未获取到订单号')
            return
        }

        ElMessageBox.confirm(
            '订单已生成！是否立即支付？',
            '支付确认',
            {
                confirmButtonText: '立即支付',
                cancelButtonText: '稍后支付',
                type: 'success',
            }
        )
            .then(async () => {
                await updateOrderStatus(newOrderID, 'Paid')
            })
            .catch(() => {
                ElMessage.info('您可以稍后在订单中心支付')
            })

    } catch (e) {
        console.error(e)
        ElMessage.error('订单生成失败')
    } finally {
        checkoutLoading.value = false
        // 不自动刷新购物车以免拉回刚删除的商品(如果后端没删的话)
    }
}

const updateOrderStatus = async (orderID, statusStr) => {
    try {
        const res = await axios.post(`${BASE_URL}/api/OrderStatus_Update`, {
            oOrderID: orderID,
            NewStatus: statusStr
        })

        if (res.data === 'Update Accept') {
            ElMessage.success('支付成功！')
        } else {
            ElMessage.warning('支付状态更新失败')
        }
    } catch (e) {
        console.error(e)
        ElMessage.error('网络错误，支付失败')
    }
}

const categoryList = ref([]) // 改为响应式数组

const fetchCategories = async () => {
    try {
        // 根据你的描述，参数为空，且沿用该项目的 POST 风格
        const res = await axios.post(`${BASE_URL}/api/GetAllProductType`, {})

        // 假设后端返回结构为 { code: 200, data: ["电子产品", "服装", ...] }
        if (res.data && res.data.data) {
            categoryList.value = res.data.data
        }
    } catch (e) {
        console.error('获取商品分类失败', e)
        // 发生错误时可以给个默认值，或者保持为空
        // categoryList.value = ['电子产品', '书籍'] 
    }
}

</script>

<style scoped>
/* 样式保留 */
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

.footer {
    text-align: center;
    color: #909399;
    padding: 20px;
}

.clickable-item {
    cursor: pointer;
    transition: background-color 0.2s;
    padding: 10px;
    border-radius: 4px;
}

.clickable-item:hover {
    background-color: #f5f7fa;
}

.cart-footer-bar {
    border-top: 1px solid #ebeef5;
    padding-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.total-info {
    font-size: 16px;
    color: #333;
}

.total-price {
    font-size: 24px;
    color: #f56c6c;
    font-weight: bold;
    margin-left: 8px;
}

.dialog-tip {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 15px;
}

.address-list {
    max-height: 300px;
    overflow-y: auto;
    margin-bottom: 15px;
}

.address-card {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 10px;
    margin-bottom: 10px;
    cursor: pointer;
    transition: all 0.3s;
}

.address-card:hover {
    border-color: #409EFF;
    background-color: #ecf5ff;
}

.address-card.active {
    border-color: #409EFF;
    background-color: #ecf5ff;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.addr-user {
    font-weight: bold;
    margin-bottom: 5px;
}

.addr-detail {
    font-size: 13px;
    color: #606266;
    margin-bottom: 5px;
}

.addr-note {
    font-size: 12px;
    color: #909399;
}

/* 购物车新样式 */
.cart-header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    margin-bottom: 10px;
    border-bottom: 1px solid #eee;
}

.checkbox-wrapper {
    display: flex;
    align-items: center;
}

.cart-content-wrapper {
    display: flex;
    flex: 1;
    align-items: center;
    gap: 15px;
}

.cart-item-title {
    margin: 0 0 5px 0;
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 180px;
}

.drawer-link-box {
    text-align: center;
    margin-top: 10px;
}
</style>