<template>

    <!-- 有轮播图的版本 -->

  <div class="shop-layout">
    <!-- 顶部导航栏 -->
    <el-affix :offset="0">
      <el-header class="header">
        <div class="header-content">
          <div class="logo">VueShop</div>
          
          <div class="search-bar">
            <el-input 
              v-model="searchQuery" 
              placeholder="搜索商品..." 
              class="search-input"
              :prefix-icon="Search"
            />
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

    <el-main class="main-content">
      <!-- 轮播图区域 -->
      <el-carousel height="300px" class="banner">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <img :src="item.url" class="banner-img" />
          <div class="banner-text">{{ item.title }}</div>
        </el-carousel-item>
      </el-carousel>

      <!-- 商品展示区 -->
      <div class="product-section">
        <h2 class="section-title">热门商品</h2>
        <el-row :gutter="20">
          <el-col 
            v-for="product in products" 
            :key="product.id" 
            :xs="24" :sm="12" :md="8" :lg="6"
            class="product-col"
          >
            <el-card class="product-card" shadow="hover">
              <div class="image-wrapper">
                <img :src="product.img" class="product-image" />
              </div>
              <div class="card-body">
                <h3 class="product-title">{{ product.name }}</h3>
                <p class="product-desc">{{ product.desc }}</p>
                <div class="card-footer">
                  <span class="price">¥ {{ product.price }}</span>
                  <el-button type="primary" size="small" @click="addToCart(product)">
                    加入购物车
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-main>

    <!-- 底部 -->
    <el-footer class="footer">
      <p>© 2023 VueShop Demo. Built with Element Plus.</p>
    </el-footer>

    <!-- 购物车抽屉 (交互功能) -->
    <el-drawer v-model="cartVisible" title="我的购物车" direction="rtl" size="400px">
      <div v-if="cart.length === 0" class="empty-cart">
        <el-empty description="购物车是空的" />
      </div>
      
      <div v-else class="cart-list">
        <div v-for="item in cart" :key="item.id" class="cart-item">
          <img :src="item.img" class="cart-item-img" />
          <div class="cart-item-info">
            <h4>{{ item.name }}</h4>
            <div class="cart-controls">
              <span class="price">¥ {{ item.price }}</span>
              <div class="qty-control">
                <el-button :icon="Remove" circle size="small" @click="updateQuantity(item, -1)" />
                <span class="qty-text">{{ item.quantity }}</span>
                <el-button :icon="CirclePlus" circle size="small" @click="updateQuantity(item, 1)" />
              </div>
            </div>
          </div>
          <el-button type="danger" link :icon="Delete" @click="removeFromCart(item.id)" />
        </div>
      </div>
      
      <template #footer>
        <div class="cart-footer">
          <div class="total-text">总计: <span>¥ {{ cartTotal }}</span></div>
          <el-button type="primary" size="large" class="checkout-btn">去结算</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>







<script setup>
import { ref, computed, reactive } from 'vue'
import { 
  ShoppingCart, 
  Search, 
  User, 
  Delete, 
  CirclePlus, 
  Remove 
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// --- 1. 模拟数据 ---
// 轮播图数据
const banners = [
  { id: 1, url: 'https://via.placeholder.com/1200x300/409EFF/ffffff?text=Summer+Sale', title: '夏季大促' },
  { id: 2, url: 'https://via.placeholder.com/1200x300/67C23A/ffffff?text=New+Arrivals', title: '新品上市' },
]

// 商品列表数据
const products = reactive([
  { id: 1, name: '无线降噪耳机', price: 299, img: 'https://via.placeholder.com/300/333/fff?text=Headphone', desc: '超长续航，沉浸式体验' },
  { id: 2, name: '智能运动手表', price: 599, img: 'https://via.placeholder.com/300/444/fff?text=Watch', desc: '健康监测，防水设计' },
  { id: 3, name: '机械键盘', price: 399, img: 'https://via.placeholder.com/300/555/fff?text=Keyboard', desc: 'RGB背光，青轴手感' },
  { id: 4, name: '人体工学椅', price: 899, img: 'https://via.placeholder.com/300/666/fff?text=Chair', desc: '久坐不累，保护腰椎' },
  { id: 5, name: '4K显示器', price: 1299, img: 'https://via.placeholder.com/300/777/fff?text=Monitor', desc: '色彩精准，专业设计' },
  { id: 6, name: '智能音箱', price: 199, img: 'https://via.placeholder.com/300/888/fff?text=Speaker', desc: '语音控制，智能家居' },
])

// --- 2. 状态管理 ---
const searchQuery = ref('')
const cartVisible = ref(false) // 控制购物车抽屉显示
const cart = ref([]) // 购物车数组

// --- 3. 交互逻辑 ---

// 计算购物车总数量
const cartCount = computed(() => {
  return cart.value.reduce((total, item) => total + item.quantity, 0)
})

// 计算购物车总价
const cartTotal = computed(() => {
  return cart.value.reduce((total, item) => total + (item.price * item.quantity), 0)
})

// 加入购物车
const addToCart = (product) => {
  const existingItem = cart.value.find(item => item.id === product.id)
  
  if (existingItem) {
    existingItem.quantity++
  } else {
    cart.value.push({ ...product, quantity: 1 })
  }
  
  ElMessage.success(`已将 "${product.name}" 加入购物车`)
}

// 从购物车移除
const removeFromCart = (id) => {
  cart.value = cart.value.filter(item => item.id !== id)
}

// 增加/减少数量
const updateQuantity = (item, change) => {
  if (change === -1 && item.quantity <= 1) return
  item.quantity += change
}
</script>

<style scoped>
/* 布局样式 */
.shop-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* Header 样式 */
.header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  padding: 0;
  height: 60px;
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
.user-avatar {
  cursor: pointer;
  background-color: #409EFF;
}

/* Main 内容区样式 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  width: 100%;
}

/* 轮播图 */
.banner {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
}
.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.banner-text {
  position: absolute;
  bottom: 20px;
  left: 20px;
  color: white;
  font-size: 2rem;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}

/* 商品列表 */
.section-title {
  margin-bottom: 20px;
  font-size: 1.5rem;
  color: #303133;
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
.image-wrapper {
  height: 200px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f9f9f9;
}
.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
}
.product-desc {
  color: #909399;
  font-size: 12px;
  margin: 8px 0 15px;
  height: 36px; /* 固定高度保持对齐 */
  overflow: hidden;
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

/* 购物车抽屉样式 */
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
.cart-item-info h4 {
  margin: 0 0 5px;
  font-size: 14px;
}
.cart-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  font-weight: bold;
}
.checkout-btn {
  width: 100%;
}

/* Footer */
.footer {
  text-align: center;
  color: #909399;
  padding: 20px;
}
</style>
