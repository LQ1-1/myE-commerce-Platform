<template>
    <div class="detail-layout">
        <!-- 顶部导航栏 (保持不变) -->
        <el-affix :offset="0">
            <el-header class="header">
                <div class="header-content">
                    <div class="logo" @click="goHome">EBuyPlt</div>
                    <div class="search-bar">
                        <el-input v-model="searchQuery" placeholder="搜索其他商品..." class="search-input"
                            :prefix-icon="Search" @keyup.enter="handleSearchRedirect" />
                    </div>
                    <div class="actions">
                        <el-button @click="goHome">继续购物</el-button>
                        <el-button :icon="Tickets" circle size="large" @click="router.push('/OrderListView')"
                            title="我的订单" />
                        <el-button :icon="Star" circle size="large" @click="openFavorites" title="我的收藏" />
                        <el-badge :value="cartCount" :hidden="cartCount === 0" class="item-badge">
                            <el-button :icon="ShoppingCart" circle size="large" @click="openCart" title="我的购物车" />
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

        <!-- 主内容区 -->
        <el-main class="main-container" v-loading="isLoading">
            <div v-if="product" class="product-detail-wrapper">
                <!-- 第一部分：上方的主图与基本信息 (保持不变) -->
                <el-row :gutter="40" class="main-info-row">
                    <el-col :xs="24" :md="12">
                        <div class="carousel-container">
                            <el-carousel trigger="click" height="450px" :autoplay="false" arrow="always"
                                indicator-position="outside">
                                <el-carousel-item v-for="(img, index) in product.pImagePaths" :key="index">
                                    <div class="img-box">
                                        <img :src="getProductImage(img)" class="carousel-img" />
                                    </div>
                                </el-carousel-item>
                                <el-carousel-item v-if="!product.pImagePaths || product.pImagePaths.length === 0">
                                    <div class="no-image">暂无图片</div>
                                </el-carousel-item>
                            </el-carousel>
                        </div>
                    </el-col>
                    <el-col :xs="24" :md="12">
                        <div class="info-container">
                            <div class="info-top">
                                <div class="info-header">
                                    <el-tag effect="dark" size="large">{{ product.pType }}</el-tag>
                                    <span class="producer">厂商: {{ product.pProducer }}</span>
                                </div>
                                <h1 class="product-name">{{ product.pName }}</h1>
                                <p class="product-id">Product ID: {{ product.pID }}</p>
                                <div class="price-section">
                                    <div class="price-box">
                                        <span class="currency">¥</span>
                                        <span class="amount">{{ product.pPrice }}</span>
                                    </div>
                                    <div v-if="product.pDiscount && product.pDiscount !== '无' && product.pDiscount > 0"
                                        class="discount-badge">
                                        Discount: {{ product.pDiscount }}
                                    </div>
                                </div>
                                <div class="meta-info">
                                    <div class="meta-row">
                                        <span class="label">库存状态:</span>
                                        <span :class="product.pInventory > 0 ? 'text-green' : 'text-red'">
                                            {{ product.pInventory > 0 ? `有货 (剩余 ${product.pInventory})` : '暂时缺货' }}
                                        </span>
                                    </div>
                                    <div class="meta-row"><span class="label">发布日期:</span> {{ product.pReleaseDate }}
                                    </div>
                                    <div class="meta-row"><span class="label">商品状态:</span> {{ product.pStatus }}</div>
                                </div>
                            </div>
                            <div class="action-buttons">
                                <div class="qty-wrapper">
                                    <span class="qty-label">数量</span>
                                    <el-input-number v-model="buyQuantity" :min="1"
                                        :max="product.pInventory > 0 ? product.pInventory : 1"
                                        :disabled="product.pInventory <= 0" size="large" />
                                </div>
                                <div class="btn-group">
                                    <el-button type="primary" size="large" :icon="ShoppingCart" class="add-btn"
                                        :disabled="product.pInventory <= 0" @click="addToCart(product, buyQuantity)">
                                        加入购物车
                                    </el-button>
                                    <el-button size="large" class="fav-btn"
                                        :type="isFavorite(product.pID) ? 'warning' : 'default'"
                                        :icon="isFavorite(product.pID) ? StarFilled : Star"
                                        @click="toggleFavorite(product)">
                                        {{ isFavorite(product.pID) ? '已收藏' : '收藏' }}
                                    </el-button>
                                </div>
                            </div>
                        </div>
                    </el-col>
                </el-row>

                <el-divider class="section-divider" />

                <!-- 第二部分：商品详情描述 -->
                <div class="description-section">
                    <div class="desc-title">
                        <h3>商品详情</h3>
                        <div class="title-underline"></div>
                    </div>
                    <div class="desc-content">
                        <p>{{ product.pInfo || '暂无详细描述' }}</p>
                    </div>
                </div>

                <el-divider class="section-divider" />

                <!-- 新增：第三部分：商品评价区 -->
                <div class="comments-section" id="comments">
                    <div class="desc-title">
                        <h3>用户评价 ({{ commentList.length }})</h3>
                        <div class="title-underline"></div>
                    </div>

                    <div class="comment-input-area">
                        <el-input v-model="newCommentContent" type="textarea" :rows="3" placeholder="写下你的评价..."
                            maxlength="200" show-word-limit resize="none" />
                        <div class="input-actions" style="margin-top: 10px; text-align: right;">
                            <el-button type="primary" :icon="ChatDotSquare" @click="submitComment(null)"
                                :loading="commentSubmitting">
                                发表评论
                            </el-button>
                        </div>
                    </div>

                    <!-- 评论列表 -->
                    <div class="comment-list" v-loading="commentLoading">
                        <div v-if="commentList.length === 0" class="no-comments">
                            <el-empty description="暂无评价，快来抢沙发吧！" :image-size="100" />
                        </div>
                        <div v-else v-for="item in commentList" :key="item.cID" class="comment-item">
                            <div class="comment-avatar">
                                <el-avatar :size="40" :icon="User" style="background-color: #409EFF;" />
                            </div>
                            <div class="comment-content-box">
                                <div class="comment-header">
                                    <span class="username">{{ item.Commenter }}</span>
                                    <span class="date">{{ formatTime(item.cDate) }}</span>
                                </div>

                                <div class="comment-text">
                                    <span v-if="item.rReplyID && item.Recipient" class="reply-tag">
                                        回复 @{{ item.Recipient }} :
                                    </span>
                                    {{ item.cContent }}
                                </div>

                                <div class="comment-actions">
                                    <span class="action-btn like-btn" @click="handleLike(item)">
                                        <el-icon>
                                            <ThumbUp />
                                        </el-icon>
                                        {{ item.cLikes > 0 ? item.cLikes : '点赞' }}
                                    </span>
                                    <span class="action-btn reply-btn" @click="toggleReplyBox(item.cID)">
                                        <el-icon>
                                            <ChatLineRound />
                                        </el-icon> 回复
                                    </span>
                                </div>

                                <!-- 回复输入框 (动态显示) -->
                                <div v-if="activeReplyID === item.cID" class="inline-reply-box">
                                    <el-input v-model="replyContent" size="small"
                                        :placeholder="`回复 @${item.Commenter}...`" class="reply-input">
                                        <template #append>
                                            <el-button :icon="Position" @click="submitComment(item.cID, item.cID)"
                                                :loading="commentSubmitting" />
                                        </template>
                                    </el-input>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <el-empty v-else-if="!isLoading" description="未找到商品信息">
                <el-button type="primary" @click="goHome">返回首页</el-button>
            </el-empty>
        </el-main>

        <!-- 购物车、收藏夹、结算弹窗 (保持不变) -->
        <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="480px">
            <div v-if="cart.length === 0" class="empty-cart">
                <el-empty description="购物车是空的" />
                <div class="drawer-link-box" style="text-align: center;">
                    <el-button type="primary" link @click="router.push('/OrderListView')">查看历史订单 ></el-button>
                </div>
            </div>
            <div v-else class="cart-container" v-loading="cartLoading">
                <div class="cart-header-actions"
                    style="display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #eee;">
                    <el-checkbox v-model="isAllSelected" @change="toggleSelectAll">全选</el-checkbox>
                    <el-button type="primary" link @click="router.push('/OrderListView')">我的订单 ></el-button>
                </div>
                <div class="cart-list">
                    <div v-for="item in cart" :key="item.pID" class="cart-item"
                        style="display: flex; align-items: center; gap: 10px;">
                        <el-checkbox v-model="item.isSelected" />
                        <div class="cart-content-wrapper clickable-item" @click="goToDetail(item.pID)"
                            style="flex: 1; display: flex; gap: 10px; align-items: center;">
                            <img :src="getProductImage(item.pImagesPath)" class="cart-item-img" />
                            <div class="cart-item-info">
                                <h4 style="margin: 0 0 5px 0;">{{ item.pName }}</h4>
                                <div class="cart-controls">
                                    <span class="cart-price">¥ {{ item.pPrice }}</span>
                                    <div class="qty-control">
                                        <el-button :icon="Remove" circle size="small"
                                            @click.stop="changeCartQuantity(item, -1)" />
                                        <span class="qty-text">{{ item.cAmount }}</span>
                                        <el-button :icon="CirclePlus" circle size="small"
                                            @click.stop="changeCartQuantity(item, 1)" />
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

        <!-- 结算流程弹窗 (保持不变) -->
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
                            <el-input v-model="newAddressForm.uContactPersonPhone" />
                        </el-form-item>
                        <el-form-item label="性别">
                            <el-radio-group v-model="newAddressForm.uContactPersonGender">
                                <el-radio label="男">男</el-radio>
                                <el-radio label="女">女</el-radio>
                            </el-radio-group>
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

        <!-- 收藏夹抽屉 (保持不变) -->
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
                            <span class="cart-price">¥ {{ item.pPrice }}</span>
                            <el-tag size="small">{{ item.pType }}</el-tag>
                        </div>
                    </div>
                    <div class="fav-actions">
                        <el-button type="primary" circle :icon="ShoppingCart" size="small"
                            @click.stop="addToCart(item, 1)" title="加入购物车" />
                        <el-button type="danger" circle :icon="Delete" size="small" @click.stop="toggleFavorite(item)"
                            title="删除" />
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
// 新增图标: ChatDotSquare, ChatLineRound, ThumbUp, Position
import {
    ShoppingCart, Search, User, Delete, CirclePlus, Remove, Star, StarFilled, Tickets,
    ChatDotSquare, ChatLineRound, ThumbUp, Position
} from '@element-plus/icons-vue'

const BASE_URL = 'http://192.168.126.94:8082'
const route = useRoute()
const router = useRouter()

// 核心数据
const currentUserID = ref('')
const isLoading = ref(false)
const product = ref(null)
const searchQuery = ref('')
const buyQuantity = ref(1)

// 购物车与收藏状态
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

// --- 新增：评论相关状态 ---
const commentList = ref([])
const commentLoading = ref(false)
const newCommentContent = ref('') // 主评论框内容
const commentSubmitting = ref(false)
const activeReplyID = ref(null) // 当前正在回复的评论ID
const replyContent = ref('') // 回复框内容


// --- 生命周期 ---
onMounted(async () => {
    const storedUID = sessionStorage.getItem('uID')
    if (!storedUID) {
        ElMessage.error('请先登录')
        router.push('/')
        return
    }
    currentUserID.value = storedUID

    const pID = route.params.pID

    if (pID) {
        await Promise.all([
            fetchProductDetail(pID),
            fetchCart(),
            fetchFavorites(),
            fetchComments(pID) // 加载评论
        ])
    } else {
        ElMessage.error('参数错误：缺少商品ID')
        router.push('/ShoppingnbView')
    }
})

const goToDetail = (id) => {
    if (product.value && String(product.value.pID) === String(id)) {
        cartVisible.value = false
        favVisible.value = false
        return
    }
    cartVisible.value = false
    favVisible.value = false
    router.push({ name: 'ProductDetailView', params: { pID: id } })
}

watch(
    () => route.params.pID,
    (newID, oldID) => {
        if (newID && newID !== oldID) {
            fetchProductDetail(newID)
            fetchComments(newID) // 切换商品时刷新评论
            buyQuantity.value = 1
            window.scrollTo({ top: 0, behavior: 'smooth' })
        }
    }
)

const goToUserProfile = () => {
    router.push({
        path: '/UserProfileView',
        query: { uID: currentUserID.value }
    })
}

// --- API 方法 ---
const fetchProductDetail = async (id) => {
    isLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/ProductClick`, { pID: id })
        if (res.data && res.data.data) {
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

const getProductImage = (path) => {
    if (!path) return 'https://via.placeholder.com/400x400?text=No+Image'
    let cleanPath = path.replace(/\\/g, '/')
    if (cleanPath.startsWith('http')) return cleanPath
    if (!cleanPath.startsWith('/')) cleanPath = '/' + cleanPath
    return `${BASE_URL}${cleanPath}`
}

const goHome = () => router.push('/ShoppingnbView')

const handleSearchRedirect = () => {
    if (searchQuery.value.trim()) {
        router.push({ path: '/ShoppingnbView', query: { q: searchQuery.value } })
    }
}

const handleLogout = () => {
    sessionStorage.removeItem('uID')
    router.push('/')
}

// --- 新增：评论功能逻辑 ---

// 获取评论列表
const fetchComments = async (pid) => {
    const targetID = pid || product.value?.pID
    if (!targetID) return
    commentLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/GetAllProductComment`, { pID: targetID })
        if (res.data && res.data.data) {
            commentList.value = res.data.data
        } else {
            commentList.value = []
        }
    } catch (e) {
        console.error("加载评论失败", e)
        // ElMessage.warning('评论加载失败') // 可选，避免打扰用户
    } finally {
        commentLoading.value = false
    }
}

// 提交评论 (如果 replyToID 存在，则是回复)
// 提交评论 (如果 replyToID 存在，则是回复)
const submitComment = async (replyToID) => {
    const content = replyToID ? replyContent.value : newCommentContent.value

    if (!content.trim()) {
        ElMessage.warning('请输入评论内容')
        return
    }

    if (!currentUserID.value) {
        ElMessage.error('请登录后再评论')
        return
    }

    commentSubmitting.value = true
    try {
        const payload = {
            pID: product.value.pID,
            uID: currentUserID.value,
            cContent: content,
            rReplyID: replyToID ? replyToID : null
        }

        await axios.post(`${BASE_URL}/api/SendProductComment`, payload)

        ElMessage.success('评论发表成功')

        // 清空输入框并刷新列表
        if (replyToID) {
            replyContent.value = ''
            activeReplyID.value = null
        } else {
            newCommentContent.value = ''
        }
        await fetchComments()

    } catch (e) {
        console.error(e)
        ElMessage.error('评论发送失败')
    } finally {
        commentSubmitting.value = false
    }
}

// 点赞评论
const handleLike = async (item) => {
    try {
        await axios.post(`${BASE_URL}/api/GiveLikesProductComment`, { cID: item.cID })
        // 点赞接口无返回值，需要手动更新本地数据或重新获取
        // 简单起见，本地+1，或者重新拉取列表
        // item.cLikes += 1 // 乐观更新
        await fetchComments() // 重新拉取以确保数据一致
        ElMessage.success('点赞成功')
    } catch (e) {
        ElMessage.error('点赞失败')
    }
}

// 切换回复框显示
const toggleReplyBox = (cID) => {
    if (activeReplyID.value === cID) {
        activeReplyID.value = null // 关闭
    } else {
        activeReplyID.value = cID
        replyContent.value = '' // 清空之前的输入
    }
}

// 格式化时间 (后端返回的可能是字符串，简单处理一下)
const formatTime = (dateStr) => {
    if (!dateStr) return ''
    // 如果是标准格式，可以 format。如果已经是字符串直接返回
    return dateStr
}


// --- 购物车与收藏夹逻辑 (保持不变) ---
const fetchCart = async () => {
    if (!currentUserID.value) return
    cartLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/ShoppingCartRecords`, { uID: currentUserID.value })
        const rawList = res.data && res.data.data ? res.data.data : []
        const oldMap = new Map(cart.value.map(i => [i.pID, i.isSelected]))
        cart.value = rawList.map(item => ({
            ...item,
            isSelected: oldMap.has(item.pID) ? oldMap.get(item.pID) : false
        }))
    } catch (e) { console.error(e) }
    finally { cartLoading.value = false }
}

const isAllSelected = computed({
    get: () => cart.value.length > 0 && cart.value.every(i => i.isSelected),
    set: (val) => cart.value.forEach(i => i.isSelected = val)
})
const toggleSelectAll = (val) => cart.value.forEach(i => i.isSelected = val)
const selectedCount = computed(() => cart.value.filter(i => i.isSelected).length)
const selectedTotal = computed(() => {
    return cart.value.filter(i => i.isSelected).reduce((total, item) => total + (Number(item.pPrice) * Number(item.cAmount)), 0)
})

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

const openCart = () => { cartVisible.value = true; fetchCart() }
const openFavorites = () => { favVisible.value = true; fetchFavorites() }

const fetchFavorites = async () => {
    if (!currentUserID.value) return
    favLoading.value = true
    try {
        const res = await axios.post(`${BASE_URL}/api/FavouriteRecords`, { uID: currentUserID.value })
        favorites.value = res.data && res.data.data ? res.data.data : []
    } catch (e) { console.error(e) }
    finally { favLoading.value = false }
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

const cartCount = computed(() => cart.value.reduce((total, item) => total + item.cAmount, 0))

// --- 结算核心逻辑 (保持不变) ---
const handleCheckout = async () => {
    if (!currentUserID.value) return ElMessage.error("未登录")
    if (selectedCount.value === 0) return ElMessage.warning('请先勾选要结算的商品')
    checkoutVisible.value = true
    checkoutLoading.value = true
    isAddingNewAddress.value = false

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
            if (addrRes.data !== 'Request Accept') {
                throw new Error('地址保存失败')
            }
        } catch (e) {
            checkoutLoading.value = false
            return ElMessage.error('保存收货地址失败，请重试')
        }
    } else {
        finalDeliveryInfo = existingAddresses.value[selectedAddressIndex.value]
    }

    try {
        checkoutLoading.value = true

        const orderPayload = {
            uID: currentUserID.value,
            oDeliveryInfo: finalDeliveryInfo,
            pProducts: cart.value.filter(item => item.isSelected).map(item => ({
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

        cart.value = cart.value.filter(i => !i.isSelected)

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
        fetchCart()
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

</script>

<style scoped>
/* 保持原有 CSS 不变 */
.detail-layout {
    min-height: 100vh;
    background-color: #f5f7fa;
}

/* Header */
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

/* Main Container */
.main-container {
    max-width: 1100px;
    margin: 20px auto;
    padding: 0 20px;
}

.product-detail-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 40px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.main-info-row {
    margin-bottom: 20px;
}

/* 轮播图 */
.carousel-container {
    background: #fff;
    border-radius: 8px;
    border: 1px solid #ebeef5;
    overflow: hidden;
    height: 450px;
}

.img-box {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f9f9f9;
}

.carousel-img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

.no-image {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
}

/* 右侧信息栏布局调整 */
.info-container {
    padding-left: 30px;
    display: flex;
    flex-direction: column;
    height: 450px;
    /* 与轮播图等高 */
    justify-content: space-between;
    /* 顶部信息和底部按钮分离 */
}

/* 顶部信息块 */
.info-top {
    flex: 1;
}

.info-header {
    display: flex;
    align-items: center;
    gap: 15px;
    margin-bottom: 10px;
}

.producer {
    font-size: 14px;
    color: #909399;
}

.product-name {
    font-size: 28px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 10px 0;
    line-height: 1.3;
}

.product-id {
    font-size: 13px;
    color: #C0C4CC;
    margin-bottom: 25px;
}

.price-section {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 25px;
    padding-bottom: 20px;
    border-bottom: 1px solid #f0f2f5;
}

.price-box {
    color: #f56c6c;
}

.currency {
    font-size: 20px;
    font-weight: bold;
}

.amount {
    font-size: 36px;
    font-weight: bold;
    margin-left: 5px;
}

.discount-badge {
    background: #f56c6c;
    color: #fff;
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 14px;
}

.meta-info {
    margin-bottom: 10px;
}

.meta-row {
    font-size: 14px;
    color: #606266;
    margin-bottom: 12px;
    display: flex;
    align-items: center;
}

.meta-row .label {
    width: 80px;
    color: #909399;
}

.text-green {
    color: #67C23A;
    font-weight: bold;
}

.text-red {
    color: #F56C6C;
    font-weight: bold;
}

/* 底部操作区 */
.action-buttons {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-top: 10px;
}

.qty-wrapper {
    margin-bottom: 15px;
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 14px;
    color: #606266;
}

.btn-group {
    display: flex;
    gap: 15px;
}

.add-btn {
    flex: 2;
    font-weight: bold;
}

.fav-btn {
    flex: 1;
}

/* 分割线 */
.section-divider {
    margin: 40px 0;
}

/* 底部独立详情描述栏 */
.description-section {
    padding: 0 10px;
}

.desc-title {
    margin-bottom: 20px;
}

.desc-title h3 {
    font-size: 20px;
    color: #303133;
    margin: 0 0 8px 0;
}

.title-underline {
    width: 60px;
    height: 4px;
    background: #409EFF;
    border-radius: 2px;
}

.desc-content {
    font-size: 16px;
    color: #606266;
    line-height: 1.8;
    white-space: pre-line;
    /* 保留换行符 */
    min-height: 100px;
}

/* 抽屉内样式 */
.cart-list {
    height: calc(100vh - 200px);
    overflow-y: auto;
    padding-right: 5px;
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
    width: 64px;
    height: 64px;
    border-radius: 6px;
    object-fit: cover;
    border: 1px solid #eee;
}

.cart-item-info {
    flex: 1;
    overflow: hidden;
}

.cart-name {
    margin: 0 0 5px 0;
    font-size: 14px;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.cart-controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.cart-price {
    color: #f56c6c;
    font-weight: bold;
}

.qty-control {
    display: flex;
    align-items: center;
    gap: 8px;
}

.qty-text {
    width: 20px;
    text-align: center;
    font-size: 14px;
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
    align-items: center;
    color: #303133;
}

.total-price {
    font-size: 22px;
    color: #f56c6c;
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

/* 响应式 */
@media screen and (max-width: 768px) {
    .info-container {
        padding-left: 0;
        padding-top: 20px;
        height: auto;
    }

    .carousel-container {
        height: 300px;
    }

    .btn-group {
        flex-direction: column;
    }
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

/* 购物车底部栏 */
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

/* 结算弹窗样式 */
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

/* --- 新增：评论区样式 --- */
.comments-section {
    padding: 0 10px;
    margin-bottom: 40px;
}

.comment-input-area {
    margin-bottom: 30px;
}

.comment-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.comment-item {
    display: flex;
    gap: 15px;
    border-bottom: 1px solid #f0f2f5;
    padding-bottom: 20px;
}

.comment-content-box {
    flex: 1;
}

.comment-header {
    margin-bottom: 5px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.username {
    font-weight: bold;
    font-size: 14px;
    color: #303133;
}

.date {
    font-size: 12px;
    color: #909399;
}

.comment-text {
    font-size: 14px;
    color: #606266;
    line-height: 1.6;
    margin-bottom: 10px;
    word-break: break-all;
}

.reply-tag {
    color: #409EFF;
    margin-right: 5px;
    font-weight: 500;
}

.comment-actions {
    display: flex;
    gap: 20px;
}

.action-btn {
    font-size: 13px;
    color: #909399;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
    transition: color 0.2s;
}

.action-btn:hover {
    color: #409EFF;
}

.inline-reply-box {
    margin-top: 15px;
    background: #f9f9f9;
    padding: 10px;
    border-radius: 4px;
}
</style>