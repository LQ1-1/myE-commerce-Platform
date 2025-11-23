<template>
    <div class="shop-layout">
        <!-- 顶部导航栏 -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <div class="logo">EBuyPlt</div>

                    <div class="search-bar">
                        <el-input v-model="searchQuery" placeholder="搜索商品..." class="search-input" :prefix-icon="Search"
                            clearable />
                    </div>

                    <div class="actions">
                        <el-badge :value="cartCount" :hidden="cartCount === 0" class="item-badge">
                            <el-button :icon="ShoppingCart" circle size="large" @click="cartVisible = true" />
                        </el-badge>
                        <el-dropdown>
                            <el-avatar :icon="User" class="user-avatar" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item>我的订单</el-dropdown-item>
                                    <el-dropdown-item>个人设置</el-dropdown-item>
                                    <el-dropdown-item divided>退出登录</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>
        </el-affix>

        <el-main class="main-content" v-loading="isLoading">

            <!-- 分类筛选栏 (动态生成) -->
            <div class="category-bar">
                <el-tabs v-model="activeCategory" class="category-tabs">
                    <el-tab-pane label="全部" name="全部" />
                    <el-tab-pane v-for="cat in categoryList" :key="cat" :label="cat" :name="cat" />
                </el-tabs>
            </div>

            <!-- 商品展示区 -->
            <div class="product-section">
                <div class="section-header">
                    <h2 class="section-title">{{ activeCategory }}商品</h2>
                    <span class="product-count">共 {{ filteredProducts.length }} 件商品</span>
                </div>

                <el-row :gutter="20">
                    <el-col v-for="product in filteredProducts" :key="product.pID" :xs="24" :sm="12" :md="8" :lg="6"
                        class="product-col">
                        <el-card class="product-card" shadow="hover">
                            <!-- 点击图片查看详情 -->
                            <div class="image-wrapper" @click="showProductDetails(product)">
                                <img :src="getProductImage(product.pImagePath)" class="product-image" />

                                <el-tag class="category-tag" size="small" effect="dark">{{ product.pType }}</el-tag>

                                <!-- 缺货遮罩 -->
                                <div v-if="product.pInventory <= 0" class="out-of-stock-mask">缺货</div>

                                <!-- 折扣标签 (如果有折扣且不为无) -->
                                <div v-if="product.pDiscount && product.pDiscount !== '无'" class="discount-badge">
                                    {{"Discount : " +product.pDiscount}}
                                </div>
                            </div>

                            <div class="card-body">
                                <!-- 点击标题查看详情 -->
                                <h3 class="product-title" @click="showProductDetails(product)">{{ product.pName }}</h3>

                                <!-- 列表页只展示少部分信息，防止拥挤 -->
                                <div class="simple-info">
                                    <span
                                        :class="['status-dot', product.pStatus === '正常' ? 'online' : 'offline']"></span>
                                    {{ product.pStatus }}
                                </div>

                                <div class="card-footer">
                                    <span class="price">¥ {{ product.pPrice }}</span>
                                    <el-button type="primary" size="small" :disabled="product.pInventory <= 0"
                                        @click="addToCart(product)">
                                        {{ product.pInventory > 0 ? '加入购物车' : '缺货' }}
                                    </el-button>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>

                <el-empty v-if="!isLoading && filteredProducts.length === 0" description="暂无相关商品" />
            </div>
        </el-main>

        <!-- 底部 -->
        <el-footer class="footer">
            <p>© 2023 EBuyPlt Demo. Built with Element Plus.</p>
        </el-footer>

        <!-- 交互功能 1: 购物车抽屉 -->
        <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="400px">
            <div v-if="cart.length === 0" class="empty-cart">
                <el-empty description="购物车是空的" />
            </div>

            <div v-else class="cart-list">
                <div v-for="item in cart" :key="item.pID" class="cart-item">
                    <img :src="getProductImage(item.pImagePath)" class="cart-item-img" />
                    <div class="cart-item-info">
                        <h4>{{ item.pName }}</h4>
                        <div class="cart-controls">
                            <span class="price">¥ {{ item.pPrice }}</span>
                            <div class="qty-control">
                                <el-button :icon="Remove" circle size="small" @click="updateQuantity(item, -1)" />
                                <span class="qty-text">{{ item.quantity }}</span>
                                <el-button :icon="CirclePlus" circle size="small" @click="updateQuantity(item, 1)" />
                            </div>
                        </div>
                    </div>
                    <el-button type="danger" link :icon="Delete" @click="removeFromCart(item.pID)" />
                </div>
            </div>

            <template #footer>
                <div class="cart-footer">
                    <div class="total-text">总计: <span>¥ {{ cartTotal }}</span></div>
                    <el-button type="primary" size="large" class="checkout-btn">去结算</el-button>
                </div>
            </template>
        </el-drawer>

        <!-- 交互功能 2: 商品详情弹窗 (点击商品后显示) -->
        <el-dialog v-model="detailVisible" :title="selectedProduct.pName" width="500px" align-center>
            <div v-if="selectedProduct" class="product-detail-container">
                <img :src="getProductImage(selectedProduct.pImagePath)" class="detail-img" />

                <el-descriptions :column="1" border>
                    <el-descriptions-item label="生产厂商">{{ selectedProduct.pProducer }}</el-descriptions-item>
                    <el-descriptions-item label="发布日期">{{ selectedProduct.pReleaseDate }}</el-descriptions-item>
                    <el-descriptions-item label="当前库存">{{ selectedProduct.pInventory }}</el-descriptions-item>
                    <el-descriptions-item label="商品类型">
                        <el-tag size="small">{{ selectedProduct.pType }}</el-tag>
                    </el-descriptions-item>
                </el-descriptions>

                <div class="detail-desc">
                    <h4>商品介绍</h4>
                    <p>{{ selectedProduct.pInfo }}</p>
                </div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <span class="detail-price">¥ {{ selectedProduct.pPrice }}</span>
                    <el-button @click="detailVisible = false">关闭</el-button>
                    <el-button type="primary" @click="addToCart(selectedProduct); detailVisible = false">
                        加入购物车
                    </el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
    ShoppingCart, Search, User, Delete, CirclePlus, Remove
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// --- 配置项 ---
// 后端服务器地址，用于拼接图片路径和 API 请求
const BASE_URL = 'http://192.168.66.94:8082'

// --- 1. 状态定义 ---
const products = ref([]) // 存放从后端获取的商品数据
const isLoading = ref(false) // 加载状态
const searchQuery = ref('')
const activeCategory = ref('全部')

// 购物车相关
const cartVisible = ref(false)
const cart = ref([])

// 详情弹窗相关
const detailVisible = ref(false)
const selectedProduct = ref({}) // 当前选中的商品

// --- 2. API 交互逻辑 ---

// 获取推荐商品
const fetchProducts = async () => {
    isLoading.value = true
    try {
        const response = await axios.get(`${BASE_URL}/api/ProductRecommend`)

        // --- 核心修改部分 ---
        let productArray = []
        // 假设数据结构是 { code: 200, data: [...] }
        if (response.data && Array.isArray(response.data.data)) {
            // 成功取到嵌套在 data 字段中的数组
            productArray = response.data.data
        } else if (response.status === 200 && Array.isArray(response.data)) {
            // 如果后端直接返回裸数组（兼容旧逻辑）
            productArray = response.data
        }

        if (productArray.length > 0 || response.status === 200) {
            products.value = productArray
            console.log('获取商品成功:', products.value)
        } else {
            // 只有当响应状态码不是 200 或者数据结构仍然无法解析时，才报警告
            ElMessage.warning('获取商品数据为空或格式异常')
        }
        // --- 核心修改结束 ---

    } catch (error) {
        console.error('API Error:', error)
        ElMessage.error('获取商品列表失败，请检查网络')
    } finally {
        isLoading.value = false
    }
}

// 页面挂载时调用 API
onMounted(() => {
    fetchProducts()
})

// 图片路径处理函数
const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/300x300?text=No+Image'
    // 如果路径已经是 http 开头（网络图片），直接返回
    if (path.startsWith('http') || path.startsWith('https')) {
        return path
    }

    const serverUrl = 'http://192.168.66.94:8082'

    // 4. 处理斜杠：如果 path 不是以 '/' 开头，则为其添加一个前导斜杠
    const cleanPath = path.startsWith('/') ? path : '/' + path

    // 5. 拼接返回
    return `${serverUrl}${cleanPath}`
}

// --- 3. 计算属性与筛选 ---

// 动态计算所有存在的分类 (去重)
const categoryList = computed(() => {
    const types = products.value.map(p => p.pType)
    return [...new Set(types)] // ES6 Set 去重
})

// 过滤逻辑
const filteredProducts = computed(() => {
    return products.value.filter(product => {
        const categoryMatch = activeCategory.value === '全部' || product.pType === activeCategory.value
        const searchMatch = product.pName.toLowerCase().includes(searchQuery.value.toLowerCase())
        return categoryMatch && searchMatch
    })
})

// 购物车计算
const cartCount = computed(() => cart.value.reduce((total, item) => total + item.quantity, 0))
const cartTotal = computed(() => cart.value.reduce((total, item) => total + (item.pPrice * item.quantity), 0))

// --- 4. 交互逻辑 ---

// 打开详情弹窗
const showProductDetails = (product) => {
    selectedProduct.value = product
    detailVisible.value = true
}

// 加入购物车
const addToCart = (product) => {
    // 确保从后端拿到的库存是数字类型
    const inventory = Number(product.pInventory); 
    
    // 状态和库存检查
    if (inventory <= 0 || product.pStatus !== '上架') {
        ElMessage.warning('该商品暂时缺货或状态异常')
        return
    }

    const existingItem = cart.value.find(item => item.pID === product.pID)

    // 检查库存限制：确保将购物车数量与库存的数字形式进行比较
    if (existingItem && existingItem.quantity >= inventory) { // 使用转换后的 inventory
        ElMessage.warning('购物车数量已达库存上限')
        return
    }

    if (existingItem) {
        existingItem.quantity++
    } else {
        cart.value.push({ ...product, quantity: 1 })
    }
    ElMessage.success(`已将 "${product.pName}" 加入购物车`)
}

const removeFromCart = (id) => {
    cart.value = cart.value.filter(item => item.pID !== id)
}

const updateQuantity = (item, change) => {
    if (change === -1 && item.quantity <= 1) return
    if (change === 1 && item.quantity >= item.pInventory) {
        ElMessage.warning('库存不足')
        return
    }
    item.quantity += change
}
</script>

<style scoped>
/* 布局样式 */
.shop-layout {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.header {
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 0;
    height: 60px;
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
    width: 40%;
    max-width: 400px;
}

.actions {
    display: flex;
    align-items: center;
    gap: 20px;
}

.main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    width: 100%;
}

/* 分类栏 */
.category-bar {
    background-color: #fff;
    padding: 10px 20px 0 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.category-tabs :deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: 500;
}

/* 商品卡片 */
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
    border: none;
}

.product-card:hover {
    transform: translateY(-5px);
}

/* 图片区域 */
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
    background-color: #f56c6c;
    color: white;
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 12px;
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
    z-index: 10;
}

/* 卡片内容 */
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
    transition: color 0.2s;
}

.product-title:hover {
    color: #409EFF;
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

/* 详情弹窗样式 */
.product-detail-container {
    text-align: center;
}

.detail-img {
    max-width: 100%;
    max-height: 300px;
    border-radius: 8px;
    margin-bottom: 20px;
}

.detail-desc {
    margin-top: 20px;
    text-align: left;
    background: #f9f9f9;
    padding: 15px;
    border-radius: 4px;
}

.detail-desc h4 {
    margin: 0 0 10px;
    color: #303133;
}

.detail-desc p {
    margin: 0;
    color: #606266;
    line-height: 1.6;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 15px;
}

.detail-price {
    font-size: 24px;
    color: #f56c6c;
    font-weight: bold;
    margin-right: auto;
}

/* 购物车 */
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

.footer {
    text-align: center;
    color: #909399;
    padding: 20px;
}
</style>