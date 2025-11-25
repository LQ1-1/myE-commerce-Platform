<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <el-container class="main-container">
      <el-header class="app-header">
        <div class="header-content">
          <el-avatar icon="el-icon-store" class="logo-avatar"></el-avatar>
          <h1 class="app-title">商品交易与定价管理系统</h1>
          <div class="header-actions">
            <el-button type="text" @click="activeTab = 'buy'" :class="{ active: activeTab === 'buy' }">
              <el-icon><ShoppingCart /></el-icon> 购买商品
            </el-button>
            <el-button type="text" @click="activeTab = 'sell'" :class="{ active: activeTab === 'sell' }">
              <el-icon><Sell /></el-icon> 出售定价
            </el-button>
            <el-button type="text" @click="activeTab = 'inventory'">
              <el-icon><Box /></el-icon> 库存管理
            </el-button>
            <el-badge :value="cartItems.length" class="cart-badge">
              <el-button icon="el-icon-shopping-cart" circle @click="showCart = true"></el-button>
            </el-badge>
          </div>
        </div>
      </el-header>

      <!-- 主内容区域 -->
      <el-main class="app-main">
        <!-- 购买商品页面 -->
        <div v-if="activeTab === 'buy'" class="buy-page">
          <div class="page-header">
            <h2>商品购买</h2>
            <el-input 
              v-model="searchQuery" 
              placeholder="搜索商品名称/编号" 
              prefix-icon="el-icon-search"
              class="search-input"
            ></el-input>
          </div>

          <!-- 商品列表 -->
          <el-grid :column-num="4" :gutter="20" class="product-grid">
            <el-grid-item v-for="product in filteredBuyProducts" :key="product.id" class="product-card">
              <el-card :body-style="{ padding: '16px' }" shadow="hover">
                <div class="product-image">
                  <img :src="product.image" :alt="product.name" class="product-img">
                </div>
                <h3 class="product-name">{{ product.name }}</h3>
                <p class="product-desc">{{ product.description }}</p>
                <div class="product-meta">
                  <span class="product-price">¥{{ product.price.toFixed(2) }}</span>
                  <span class="product-stock" :class="{ low: product.stock < 10 }">
                    库存: {{ product.stock }}
                  </span>
                </div>
                <el-input-number 
                  v-model="product.quantity" 
                  :min="1" 
                  :max="product.stock" 
                  label="购买数量"
                  class="quantity-input"
                ></el-input-number>
                <el-button 
                  type="primary" 
                  icon="el-icon-shopping-cart-plus"
                  class="add-to-cart-btn"
                  @click="addToCart(product)"
                  :disabled="product.stock <= 0"
                >
                  加入购物车
                </el-button>
              </el-card>
            </el-grid-item>
          </el-grid>

          <!-- 无商品提示 -->
          <div v-if="filteredBuyProducts.length === 0" class="empty-state">
            <el-empty description="暂无商品数据"></el-empty>
          </div>
        </div>

        <!-- 出售定价页面 -->
        <div v-if="activeTab === 'sell'" class="sell-page">
          <div class="page-header">
            <h2>商品出售与定价</h2>
            <el-button type="primary" icon="el-icon-plus" @click="openAddProductDialog">
              添加新商品
            </el-button>
          </div>

          <!-- 商品定价表格 -->
          <el-table :data="sellProducts" border stripe class="sell-table">
            <el-table-column label="商品编号" prop="id" width="100"></el-table-column>
            <el-table-column label="商品图片" width="100">
              <template #default="scope">
                <el-image :src="scope.row.image" :preview-src-list="[scope.row.image]" width="50" height="50" fit="cover"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" prop="name"></el-table-column>
            <el-table-column label="成本价" prop="costPrice" width="120">
              <template #default="scope">
                ¥{{ scope.row.costPrice.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column label="售价" width="120">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.price" 
                  :min="scope.row.costPrice" 
                  :step="0.01" 
                  :precision="2"
                  @change="updateProductPrice(scope.row)"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="库存" width="120">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.stock" 
                  :min="0" 
                  @change="updateProductStock(scope.row)"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
              <template #default="scope">
                <el-button type="text" @click="openEditProductDialog(scope.row)">编辑</el-button>
                <el-button type="text" danger @click="deleteProduct(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 库存管理页面 -->
        <div v-if="activeTab === 'inventory'" class="inventory-page">
          <div class="page-header">
            <h2>库存管理</h2>
          </div>

          <el-table :data="allProducts" border stripe class="inventory-table">
            <el-table-column label="商品编号" prop="id" width="100"></el-table-column>
            <el-table-column label="商品图片" width="100">
              <template #default="scope">
                <el-image :src="scope.row.image" :preview-src-list="[scope.row.image]" width="50" height="50" fit="cover"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" prop="name"></el-table-column>
            <el-table-column label="售价" prop="price" width="120" formatter="formatPrice"></el-table-column>
            <el-table-column label="库存数量" prop="stock" width="120"></el-table-column>
            <el-table-column label="库存状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.stock > 50 ? 'success' : scope.row.stock > 10 ? 'warning' : 'danger'">
                  {{ scope.row.stock > 50 ? '充足' : scope.row.stock > 10 ? '紧张' : '短缺' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="最后更新" prop="updateTime" width="180"></el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>

    <!-- 购物车侧边栏 -->
    <el-drawer 
      title="我的购物车" 
      v-model:visible="showCart" 
      direction="rtl" 
      size="30%"
    >
      <div v-if="cartItems.length === 0" class="empty-cart">
        <el-empty description="购物车是空的"></el-empty>
      </div>
      <div v-else class="cart-container">
        <div class="cart-items">
          <div v-for="(item, index) in cartItems" :key="index" class="cart-item">
            <img :src="item.image" :alt="item.name" class="cart-item-img">
            <div class="cart-item-info">
              <h4>{{ item.name }}</h4>
              <div class="cart-item-meta">
                <span class="cart-item-price">¥{{ item.price.toFixed(2) }}</span>
                <span class="cart-item-quantity">x{{ item.quantity }}</span>
              </div>
            </div>
            <el-button 
              icon="el-icon-delete" 
              type="text" 
              danger 
              @click="removeFromCart(index)"
            ></el-button>
          </div>
        </div>
        <div class="cart-summary">
          <div class="cart-total">
            <span>总计:</span>
            <span class="total-price">¥{{ calculateTotal().toFixed(2) }}</span>
          </div>
          <el-button type="primary" class="checkout-btn" @click="checkout()">
            结算付款
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog 
      title="商品信息" 
      v-model:visible="showProductDialog" 
      width="600px"
    >
      <el-form :model="formProduct" :rules="productRules" ref="productFormRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="formProduct.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="formProduct.description" type="textarea" :rows="3" placeholder="请输入商品描述"></el-input>
        </el-form-item>
        <el-form-item label="成本价" prop="costPrice">
          <el-input-number 
            v-model="formProduct.costPrice" 
            :min="0.01" 
            :step="0.01" 
            :precision="2"
            placeholder="请输入成本价"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="售价" prop="price">
          <el-input-number 
            v-model="formProduct.price" 
            :min="formProduct.costPrice" 
            :step="0.01" 
            :precision="2"
            placeholder="请输入售价"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="库存数量" prop="stock">
          <el-input-number 
            v-model="formProduct.stock" 
            :min="0" 
            placeholder="请输入库存数量"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="商品图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleImageUpload"
            :before-upload="beforeImageUpload"
          >
            <img v-if="formProduct.image" :src="formProduct.image" class="upload-image">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showProductDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProduct()">确定</el-button>
      </template>
    </el-dialog>

    <!-- 结算成功提示 -->
    <el-dialog title="结算成功" v-model:visible="showCheckoutSuccess" width="400px">
      <div class="success-container">
        <el-icon class="success-icon"><CircleCheckFilled /></el-icon>
        <h3>购物结算成功！</h3>
        <p>您已成功购买以下商品，订单将尽快处理。</p>
        <el-button type="primary" @click="showCheckoutSuccess = false">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { 
  ShoppingCart, Sell, Box, CircleCheckFilled
} from '@element-plus/icons-vue';
import { ElMessage, ElEmpty, ElImage, ElTag, ElBadge } from 'element-plus';

// 状态管理
const activeTab = ref('buy'); // 当前激活的标签页
const searchQuery = ref(''); // 搜索关键词
const showCart = ref(false); // 购物车显示状态
const showProductDialog = ref(false); // 商品对话框显示状态
const showCheckoutSuccess = ref(false); // 结算成功提示
const productFormRef = ref(null); // 商品表单引用

// 商品数据
const buyProducts = ref([
  {
    id: 'P001',
    name: '智能手表',
    description: '多功能智能手表，支持心率监测、运动模式',
    price: 1299.00,
    costPrice: 899.00,
    stock: 35,
    image: 'https://picsum.photos/id/1/300/300',
    quantity: 1
  },
  {
    id: 'P002',
    name: '无线蓝牙耳机',
    description: '降噪无线蓝牙耳机，续航24小时',
    price: 899.00,
    costPrice: 599.00,
    stock: 52,
    image: 'https://picsum.photos/id/2/300/300',
    quantity: 1
  },
  {
    id: 'P003',
    name: '便携式充电宝',
    description: '20000mAh大容量充电宝，双向快充',
    price: 199.00,
    costPrice: 129.00,
    stock: 108,
    image: 'https://picsum.photos/id/3/300/300',
    quantity: 1
  },
  {
    id: 'P004',
    name: '高清摄像头',
    description: '1080P高清摄像头，自动对焦',
    price: 399.00,
    costPrice: 249.00,
    stock: 23,
    image: 'https://picsum.photos/id/4/300/300',
    quantity: 1
  },
  {
    id: 'P005',
    name: '机械键盘',
    description: '青轴机械键盘，RGB背光',
    price: 599.00,
    costPrice: 399.00,
    stock: 41,
    image: 'https://picsum.photos/id/5/300/300',
    quantity: 1
  },
  {
    id: 'P006',
    name: '无线鼠标',
    description: '静音无线鼠标，续航长久',
    price: 129.00,
    costPrice: 79.00,
    stock: 87,
    image: 'https://picsum.photos/id/6/300/300',
    quantity: 1
  }
]);

const sellProducts = ref([...buyProducts.value]); // 出售商品数据（与购买商品数据同步）
const cartItems = ref([]); // 购物车商品
const formProduct = ref({ // 商品表单数据
  id: '',
  name: '',
  description: '',
  price: 0,
  costPrice: 0,
  stock: 0,
  image: '',
  updateTime: ''
});

// 商品表单验证规则
const productRules = ref({
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  costPrice: [{ required: true, message: '请输入成本价', trigger: 'blur' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存数量', trigger: 'blur' }],
  image: [{ required: true, message: '请上传商品图片', trigger: 'change' }]
});

// 计算属性：过滤购买商品
const filteredBuyProducts = computed(() => {
  if (!searchQuery.value) return buyProducts.value;
  const query = searchQuery.value.toLowerCase();
  return buyProducts.value.filter(product => 
    product.name.toLowerCase().includes(query) || 
    product.id.toLowerCase().includes(query) ||
    product.description.toLowerCase().includes(query)
  );
});

// 计算属性：所有商品
const allProducts = computed(() => {
  return [...buyProducts.value].map(product => ({
    ...product,
    updateTime: new Date().toLocaleString()
  }));
});

// 生命周期钩子
onMounted(() => {
  // 初始化数据
  sellProducts.value = [...buyProducts.value];
});

// 格式化价格const formatPrice = (row, column) => {return `¥${row.price.toFixed(2)}`;};

// 添加到购物车
const addToCart = (product) => {
  const existingItem = cartItems.value.find(item => item.id === product.id);
  if (existingItem) {
    existingItem.quantity += product.quantity;
  } else {
    cartItems.value.push({ ...product });
  }
  ElMessage.success('已添加到购物车');
  // 重置选择数量
  product.quantity = 1;
};

// 从购物车移除
const removeFromCart = (index) => {
  cartItems.value.splice(index, 1);
  ElMessage.info('已从购物车移除');
};

// 计算购物车总价
const calculateTotal = () => {
  return cartItems.value.reduce((total, item) => {
    return total + (item.price * item.quantity);
  }, 0);
};

// 结算付款
const checkout = () => {
  // 模拟结算流程
  cartItems.value.forEach(item => {
    const productIndex = buyProducts.value.findIndex(p => p.id === item.id);
    if (productIndex !== -1) {
      buyProducts.value[productIndex].stock -= item.quantity;
      sellProducts.value[productIndex].stock -= item.quantity;
    }
  });
  cartItems.value = [];
  showCart.value = false;
  showCheckoutSuccess.value = true;
};

// 打开添加商品对话框
const openAddProductDialog = () => {
  formProduct.value = {
    id: `P${(buyProducts.value.length + 1).toString().padStart(3, '0')}`,
    name: '',
    description: '',
    price: 0,
    costPrice: 0,
    stock: 0,
    image: '',
    updateTime: ''
  };
  showProductDialog.value = true;
};

// 打开编辑商品对话框
const openEditProductDialog = (product) => {
  formProduct.value = { ...product };
  showProductDialog.value = true;
};

// 保存商品
const saveProduct = async () => {
  try {
    await productFormRef.value.validate();
    formProduct.value.updateTime = new Date().toLocaleString();
    
    const existingIndex = buyProducts.value.findIndex(p => p.id === formProduct.value.id);
    if (existingIndex !== -1) {
      // 编辑商品
      buyProducts.value[existingIndex] = { ...formProduct.value };
      sellProducts.value[existingIndex] = { ...formProduct.value };
      ElMessage.success('商品编辑成功');
    } else {
      // 添加新商品
      buyProducts.value.push({ ...formProduct.value });
      sellProducts.value.push({ ...formProduct.value });
      ElMessage.success('商品添加成功');
    }
    
    showProductDialog.value = false;
  } catch (error) {
    ElMessage.error('请完善商品信息');
  }
};

// 更新商品价格
const updateProductPrice = (product) => {
  const index = buyProducts.value.findIndex(p => p.id === product.id);
  if (index !== -1) {
    buyProducts.value[index].price = product.price;
  }
  ElMessage.success('价格更新成功');
};

// 更新商品库存
const updateProductStock = (product) => {
  const index = buyProducts.value.findIndex(p => p.id === product.id);
  if (index !== -1) {
    buyProducts.value[index].stock = product.stock;
  }
  ElMessage.success('库存更新成功');
};

// 删除商品
const deleteProduct = (id) => {
  const index = buyProducts.value.findIndex(p => p.id === id);
  if (index !== -1) {
    buyProducts.value.splice(index, 1);
    sellProducts.value.splice(index, 1);
    ElMessage.success('商品删除成功');
  }
};

// 图片上传处理
const handleImageUpload = () => {
  // 模拟上传成功，使用随机图片
  formProduct.value.image = `https://picsum.photos/id/${Math.floor(Math.random() * 100)}/300/300`;
};

// 图片上传前校验
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error('请上传图片文件');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB');
  }
  return isImage && isLt2M;
};
</script>

<style scoped>
/* 全局样式 */
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.main-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.app-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo-avatar {
  width: 40px;
  height: 40px;
  margin-right: 16px;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1989fa;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-actions .active {
  color: #1989fa;
  font-weight: 500;
}

.cart-badge {
  position: relative;
}

/* 主内容区域 */
.app-main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.search-input {
  width: 300px;
}

/* 购买页面样式 */
.buy-page {
  height: 100%;
}

.product-grid {
  margin-top: 20px;
}

.product-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-image {
  height: 200px;
  overflow: hidden;
  margin-bottom: 16px;
  border-radius: 8px;
}

.product-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-img:hover {
  transform: scale(1.05);
}

.product-name {
  font-size: 1rem;
  font-weight: 500;
  margin: 0 0 8px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-desc {
  font-size: 0.875rem;
  color: #666;
  margin: 0 0 16px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 40px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.product-price {
  font-size: 1.1rem;
  font-weight: 600;
  color: #f56c6c;
}

.product-stock {
  font-size: 0.875rem;
  color: #666;
}

.product-stock.low {
  color: #f56c6c;
}

.quantity-input {
  margin-bottom: 16px;
}

.add-to-cart-btn {
  width: 100%;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

/* 出售页面样式 */
.sell-page {
  height: 100%;
}

.sell-table {
  width: 100%;
}

/* 库存页面样式 */
.inventory-page {
  height: 100%;
}

.inventory-table {
  width: 100%;
}

/* 购物车样式 */
.empty-cart {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.cart-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.cart-items {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.cart-item-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 16px;
}

.cart-item-info {
  flex: 1;
}

.cart-item-name {
  font-weight: 500;
  margin-bottom: 8px;
}

.cart-item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cart-item-price {
  font-weight: 600;
  color: #f56c6c;
}

.cart-summary {
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-size: 1.1rem;
}

.total-price {
  font-weight: 600;
  color: #f56c6c;
  font-size: 1.25rem;
}

.checkout-btn {
  width: 100%;
}

/* 商品对话框样式 */
.avatar-uploader {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 150px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  overflow: hidden;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 结算成功样式 */
.success-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 0;
  text-align: center;
}

.success-icon {
  font-size: 48px;
  color: #67c23a;
  margin-bottom: 20px;
}

.success-container h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 10px;
}

.success-container p {
  color: #666;
  margin-bottom: 30px;
}

/* 响应式样式 */
@media (max-width: 1200px) {
  .product-grid {
    --el-grid-column-num: 3 !important;
  }
}

@media (max-width: 992px) {
  .product-grid {
    --el-grid-column-num: 2 !important;
  }
  .search-input {
    width: 200px;
  }
}

@media (max-width: 768px) {
  .product-grid {
    --el-grid-column-num: 1 !important;
  }
  .app-title {
    font-size: 1.25rem;
  }
  .header-actions {
    gap: 10px;
  }
  .el-drawer {
    width: 80% !important;
  }
}
</style>