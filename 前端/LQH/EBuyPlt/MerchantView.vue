<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <el-container class="main-container">
      <el-header class="app-header">
        <div class="header-content">
          <!-- 左侧：Logo和标题 -->
          <div class="brand">
            <h1 class="app-title">EBuyPlt 商户管理中心</h1>
          </div>

          <!-- 右侧：功能区 + 用户信息 -->
          <div class="header-right-section">
            <!-- 导航菜单 -->
            <div class="nav-actions">
              <el-button type="text" @click="switchTab('inventory')" :class="{ active: activeTab === 'inventory' }">
                <el-icon>
                  <Goods />
                </el-icon> 商品管理
              </el-button>
              <el-button type="text" @click="switchTab('add')" :class="{ active: activeTab === 'add' }">
                <el-icon>
                  <Plus />
                </el-icon> 新品上架
              </el-button>
              <el-button type="text" @click="switchTab('sales')" :class="{ active: activeTab === 'sales' }">
                <el-icon>
                  <List />
                </el-icon> 销售记录
              </el-button>
            </div>

            <!-- 用户信息与退出 -->
            <el-divider direction="vertical" class="header-divider" />

            <!-- 修复开始：补全了丢失的 el-dropdown 开始标签和触发显示区域 -->
            <el-dropdown trigger="click" @command="handleUserCommand">
              <span class="el-dropdown-link user-profile">
                <el-avatar :size="32" :icon="UserFilled" class="user-avatar" />
                <span class="username">{{ currentMerchantID }}</span>
                <el-icon class="el-icon--right">
                  <ArrowDown />
                </el-icon>
              </span>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item disabled>商户ID: {{ currentMerchantID }}</el-dropdown-item>
                   <el-dropdown-item @click="goToUserProfile">个人信息</el-dropdown-item> 
                  <el-dropdown-item divided command="logout" style="color: #f56c6c;">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <!-- 主内容区域 -->
      <el-main class="app-main">

        <!-- 1. 商品管理（首页） -->
        <div v-if="activeTab === 'inventory'" class="tab-page">
          <div class="page-header">
            <h2>我的商品库存</h2>
          </div>

          <el-skeleton :loading="loading" animated>
            <template #template>
              <el-grid :column-num="4" :gutter="20">
                <el-grid-item v-for="i in 4" :key="i"><el-skeleton-item variant="image"
                    style="width: 100%; height: 240px" /></el-grid-item>
              </el-grid>
            </template>
            <template #default>

              <!-- 情况1：请求出错 -->
              <div v-if="fetchError" class="empty-state">
                <el-empty description="获取商品列表失败" :image-size="120">
                  <template #description>
                    <p style="color: #f56c6c;">数据获取异常，请检查网络或联系管理员</p>
                  </template>
                  <el-button type="danger" plain icon="Refresh" @click="fetchProductList">重新加载</el-button>
                </el-empty>
              </div>

              <!-- 情况2：请求成功但没有数据 -->
              <div v-else-if="products.length === 0" class="empty-state">
                <el-empty description="目前上架记录为空">
                  <el-button type="primary" @click="activeTab = 'add'">去上架新商品</el-button>
                </el-empty>
              </div>

              <!-- 情况3：正常显示商品列表 -->
              <el-row v-else :gutter="20">
                <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in products" :key="item.pID" class="mb-20">
                  <el-card :body-style="{ padding: '0px' }" shadow="hover" class="product-card"
                    @click="openProductDetail(item)">
                    <div class="image-wrapper">
                      <el-image :src="getImageUrl(item.pThumbnail)" fit="cover" class="card-image">
                        <template #error>
                          <div class="image-slot"><el-icon>
                              <Picture />
                            </el-icon></div>
                        </template>
                      </el-image>
                      <div class="status-tag">
                        <el-tag :type="item.pInventory > 0 ? 'success' : 'danger'" effect="dark">
                          {{ item.pInventory > 0 ? '上架' : '缺货' }}
                        </el-tag>
                      </div>
                    </div>
                    <div class="card-content">
                      <h3 class="product-title" :title="item.pName">{{ item.pName }}</h3>
                      <div class="price-row">
                        <span class="price">¥{{ item.pPrice }}</span>
                        <span class="inventory">库存: {{ item.pInventory }}</span>
                      </div>
                      <p class="desc">{{ item.pInfo || '暂无描述' }}</p>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </template>
          </el-skeleton>
        </div>

        <!-- 2. 新品上架 -->
        <div v-if="activeTab === 'add'" class="tab-page">
          <div class="page-header">
            <h2>发布新商品</h2>
          </div>
          <div class="form-container">
            <el-form :model="addForm" :rules="rules" ref="addFormRef" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="商品名称" prop="pName">
                    <el-input v-model="addForm.pName" placeholder="请输入商品名称"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="商品类型" prop="pType">
                    <el-input v-model="addForm.pType" placeholder="例如：电子产品"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="售价 (¥)" prop="pPrice">
                    <el-input-number v-model="addForm.pPrice" :precision="2" :step="0.1" :min="0"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="折扣 (0-1)" prop="pDiscount">
                    <el-input-number v-model="addForm.pDiscount" :precision="2" :step="0.05" :min="0"
                      :max="1"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="初始库存" prop="pInventory">
                    <el-input-number v-model="addForm.pInventory" :step="1" :min="0"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="生产商" prop="pProducer">
                <el-input v-model="addForm.pProducer"></el-input>
              </el-form-item>

              <el-form-item label="发布日期" prop="pReleaseDate">
                <el-date-picker v-model="addForm.pReleaseDate" type="date" placeholder="选择日期"
                  value-format="YYYY-MM-DD"></el-date-picker>
              </el-form-item>

              <el-form-item label="商品简介" prop="pInfo">
                <el-input type="textarea" v-model="addForm.pInfo" rows="3"></el-input>
              </el-form-item>

              <!-- 图片上传区域 -->
              <el-divider content-position="left">图片上传</el-divider>

              <el-form-item label="商品缩略图" required>
                <el-upload class="avatar-uploader" action="#" :auto-upload="false" :show-file-list="false"
                  :on-change="handleThumbnailChange">
                  <img v-if="thumbnailFileUrl" :src="thumbnailFileUrl" class="upload-preview" />
                  <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                  </el-icon>
                </el-upload>
                <div class="el-upload__tip">只能上传1张封面图</div>
              </el-form-item>

              <el-form-item label="商品展示图">
                <el-upload action="#" list-type="picture-card" :auto-upload="false" :on-change="handleGalleryChange"
                  :on-remove="handleGalleryRemove" multiple>
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-upload>
                <div class="el-upload__tip">可上传多张详情展示图</div>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="submitNewProduct" :loading="submitting">立即上架</el-button>
                <el-button @click="resetAddForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <!-- 3. 销售记录 -->
        <div v-if="activeTab === 'sales'" class="tab-page">
          <div class="page-header">
            <h2>销售记录</h2>
          </div>

          <el-table :data="salesList" border stripe style="width: 100%" v-loading="loading">
            <el-table-column prop="oOrderID" label="订单编号" width="180" />
            <el-table-column label="商品ID" width="120">
              <template #default="scope">
                {{ scope.row.OrderProductInfoTableItem?.pID }}
              </template>
            </el-table-column>
            <el-table-column label="交易金额" width="120">
              <template #default="scope">
                <span style="color: #f56c6c">¥{{ scope.row.OrderProductInfoTableItem?.oPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column label="购买数量" width="100">
              <template #default="scope">
                {{ scope.row.OrderProductInfoTableItem?.pAmount }}
              </template>
            </el-table-column>
            <el-table-column label="买家信息" min-width="100">
              <template #default="scope">
                <div>姓名: {{ scope.row.DeliveryInfoItem?.uContactPersonName }}</div>
                <div>电话: {{ scope.row.DeliveryInfoItem?.uContactPersonPhone }}</div>
                <div>地址: {{ scope.row.DeliveryInfoItem?.uDeliveryAddress }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="oDate" label="下单时间" width="180" />
            <el-table-column prop="oStatus" label="订单状态" width="100">
              <template #default="scope">
                <el-tag>{{ scope.row.oStatus }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

      </el-main>
    </el-container>

    <!-- 详情/编辑弹窗 -->
    <el-dialog v-model="showDetailDialog" title="商品详情与编辑" width="850px" :before-close="handleCloseDetail">
      <el-form v-if="editingProduct" :model="editingProduct" label-width="100px">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-form-item label="商品ID">
              <el-input v-model="editingProduct.pID" disabled></el-input>
            </el-form-item>

            <!-- 第一行：名称与类型 -->
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="商品名称">
                  <el-input v-model="editingProduct.pName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品类型">
                  <el-input v-model="editingProduct.pType"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <!-- 第二行：价格、折扣、库存（这里加了 style="width: 100%"） -->
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="价格">
                  <el-input-number v-model="editingProduct.pPrice" :precision="2" :step="0.1" style="width: 100%">
                  </el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="折扣(0-1)">
                  <el-input-number v-model="editingProduct.pDiscount" :precision="2" :step="0.05" :min="0" :max="1"
                    style="width: 100%">
                  </el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="库存">
                  <el-input-number v-model="editingProduct.pInventory" style="width: 100%">
                  </el-input-number>
                </el-form-item>
              </el-col>
            </el-row>

            <!-- 第三行：生产商与上架日期（日期已禁用） -->
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="生产商">
                  <el-input v-model="editingProduct.pProducer"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上架日期">
                  <!-- disabled 属性确保日期不可修改，原样回传 -->
                  <el-input v-model="editingProduct.pReleaseDate" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="状态">
              <el-select v-model="editingProduct.pStatus" style="width: 100%">
                <el-option label="上架" value="上架"></el-option>
                <el-option label="缺货" value="缺货"></el-option>
                <el-option label="下架" value="下架"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="简介">
              <el-input type="textarea" v-model="editingProduct.pInfo" rows="4"></el-input>
            </el-form-item>
          </el-tab-pane>

          <!-- 重点修改区域：图片管理 -->
          <el-tab-pane label="图片管理">
            <p class="tip-text">提示：移动鼠标到图片上可显示删除按钮；点击 + 号上传新图。</p>

            <div class="edit-image-section">
              <h4>商品缩略图 (封面)</h4>
              <div class="thumbnail-area">
                <!-- 1. 有图时：显示图片+删除遮罩 -->
                <div v-if="editingProduct.pThumbnail" class="image-preview-wrapper">
                  <el-image :src="getImageUrl(editingProduct.pThumbnail)" fit="cover" class="preview-img" />
                  <div class="image-mask">
                    <el-icon class="delete-icon" @click="handleRemoveEditThumbnail">
                      <Delete />
                    </el-icon>
                  </div>
                </div>
                <!-- 2. 无图时：显示上传框 -->
                <el-upload v-else class="avatar-uploader" action="#" :auto-upload="false" :show-file-list="false"
                  :on-change="handleEditThumbnailChange">
                  <el-icon class="avatar-uploader-icon">
                    <Plus />
                  </el-icon>
                </el-upload>
              </div>

              <h4>商品展示图 (详情)</h4>
              <div class="gallery-list">
                <!-- 3. 遍历现有图片，每个图片都有遮罩删除 -->
                <div v-for="(img, index) in editingProduct.pShowcaseImageList" :key="index"
                  class="image-preview-wrapper gallery-item">
                  <el-image :src="getImageUrl(img)" fit="cover" class="preview-img" />
                  <div class="image-mask">
                    <el-icon class="delete-icon" @click="handleRemoveEditShowcaseItem(index)">
                      <Delete />
                    </el-icon>
                  </div>
                </div>

                <!-- 4. 始终显示的添加按钮 -->
                <el-upload class="gallery-uploader" action="#" list-type="picture-card" :auto-upload="false"
                  :show-file-list="false" :on-change="handleEditGalleryChange">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-upload>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <el-button @click="showDetailDialog = false">取消</el-button>
        <el-button type="primary" @click="updateProductInfo" :loading="updating">保存修改</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import {
  Goods, Plus, List, Picture, UserFilled, ArrowDown, Delete
} from '@element-plus/icons-vue';

// ================= 配置 =================
const API_BASE_URL = 'http://192.168.126.94:8082';

// ================= 状态变量 =================
const router = useRouter();
const currentMerchantID = ref('');

const activeTab = ref('inventory');
const loading = ref(false);
const fetchError = ref(false);
const submitting = ref(false);
const updating = ref(false);
const products = ref([]);
const salesList = ref([]);
const showDetailDialog = ref(false);
const editingProduct = ref(null);
const newThumbnailFile = ref(null);
const newGalleryFiles = ref([]);
// 上架表单相关
const addFormRef = ref(null);
const thumbnailFile = ref(null);
const thumbnailFileUrl = ref('');
const galleryFiles = ref([]);

const addForm = reactive({
  pName: '',
  pType: '',
  pDiscount: 1.0,
  pPrice: 0.0,
  pProducer: '',
  pReleaseDate: '',
  pInfo: '',
  pInventory: 0,
  pStatus: '上架'
});

const rules = {
  pName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  pPrice: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  pInventory: [{ required: true, message: '请输入库存', trigger: 'blur' }]
};

// ================= 生命周期 =================
onMounted(() => {
  const uID = sessionStorage.getItem('uID');
  if (!uID) {
    ElMessage.warning('请先登录');
    router.push('/');
    return;
  }
  currentMerchantID.value = uID;
  fetchProductList();
});

// ================= 方法 =================

const handleUserCommand = (command) => {
  if (command === 'logout') {
    sessionStorage.removeItem('uID');
    ElMessage.success('已退出登录');
    router.push('/');
  }
};

const switchTab = (tab) => {
  activeTab.value = tab;
  if (tab === 'inventory') fetchProductList();
  if (tab === 'sales') fetchSalesRecord();
};

const getImageUrl = (path) => {
  if (!path) return '';
  // 处理 http 网络图片和 blob 本地预览图片
  if (path.startsWith('http') || path.startsWith('blob:')) return path;
  return `${API_BASE_URL}${path}`;
};

const goToUserProfile = () => {
  router.push({
    path: '/UserProfileView', // 这里的路径要和你路由配置里的一致
    query: { uID: currentMerchantID.value } // 将当前用户ID传过去
  })
}

// ---------------- 1. 获取商品列表 ----------------
const fetchProductList = async () => {
  loading.value = true;
  fetchError.value = false;
  try {
    const res = await axios.post(`${API_BASE_URL}/api/ProductAllInfo`, {
      uID: currentMerchantID.value
    });
    if (res.data.code === 200 || res.data.AllInfo) {
      const data = res.data.data || res.data;
      products.value = data.AllInfo || [];
    }
  } catch (error) {
    console.error(error);
    fetchError.value = true;
    ElMessage.error('获取商品列表失败');
  } finally {
    loading.value = false;
  }
};

// ---------------- 2. 上架新商品 ----------------

const handleThumbnailChange = (file) => {
  thumbnailFile.value = file.raw;
  thumbnailFileUrl.value = URL.createObjectURL(file.raw);
};

const handleGalleryChange = (file, fileList) => {
  galleryFiles.value = fileList.map(f => f.raw);
};

const handleGalleryRemove = (file, fileList) => {
  galleryFiles.value = fileList.map(f => f.raw);
};

const submitNewProduct = async () => {
  if (!addFormRef.value) return;
  await addFormRef.value.validate(async (valid) => {
    if (valid) {
      if (!thumbnailFile.value) {
        ElMessage.warning('请上传商品缩略图');
        return;
      }

      submitting.value = true;
      try {
        const payload = {
          ...addForm,
          uID: currentMerchantID.value
        };

        const infoRes = await axios.post(`${API_BASE_URL}/api/ProductOnSale`, payload);

        const resultData = infoRes.data.data || infoRes.data;
        if (resultData && resultData.pID) {
          const newPid = resultData.pID;

          const formData = new FormData();
          formData.append('uID', currentMerchantID.value);
          formData.append('pID', newPid);
          formData.append('cover', thumbnailFile.value);

          galleryFiles.value.forEach(file => {
            formData.append('gallery', file);
          });

          const imgRes = await axios.post(`${API_BASE_URL}/api/ProductImagesUpload`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });

          if (imgRes.data === 'OnSaleSuccess' || imgRes.data.includes('Success')) {
            ElMessage.success('商品上架成功！');
            resetAddForm();
            activeTab.value = 'inventory';
            fetchProductList();
          } else {
            ElMessage.warning('文本信息上传成功，但图片上传可能失败，请检查。');
          }
        } else {
          ElMessage.error('商品信息创建失败');
        }
      } catch (error) {
        console.error(error);
        ElMessage.error('上架过程发生错误');
      } finally {
        submitting.value = false;
      }
    }
  });
};

const resetAddForm = () => {
  addFormRef.value.resetFields();
  thumbnailFile.value = null;
  thumbnailFileUrl.value = '';
  galleryFiles.value = [];
};

// ---------------- 3. 销售记录 ----------------
const fetchSalesRecord = async () => {
  loading.value = true;
  try {
    const res = await axios.post(`${API_BASE_URL}/api/ProductSaledInfo`, {
      uID: currentMerchantID.value
    });
    const data = res.data.data || res.data;
    salesList.value = data.SaledItemList || [];
  } catch (error) {
    ElMessage.error('获取销售记录失败');
  } finally {
    loading.value = false;
  }
};

// ---------------- 4. 编辑/更新商品 (含图片处理) ----------------
const openProductDetail = (product) => {

  // 1. 深拷贝对象
  const copy = JSON.parse(JSON.stringify(product));

  copy.oldThumbnailPicURL = copy.pThumbnail;

  // 同理，初始化旧展示图列表（确保是数组）
  copy.oldShowcaseImagesURL = copy.pShowcaseImageList ? [...copy.pShowcaseImageList] : [];
  // ==================================================

  editingProduct.value = copy;

  // 2. 重置新文件容器
  newThumbnailFile.value = null;
  newGalleryFiles.value = [];

  showDetailDialog.value = true;
};

const handleCloseDetail = () => {
  showDetailDialog.value = false;
  editingProduct.value = null;
};

// 4.1 删除缩略图
const handleRemoveEditThumbnail = () => {
  editingProduct.value.pThumbnail = '';
};

// 4.2 添加/预览缩略图
const handleEditThumbnailChange = (file) => {
  newThumbnailFile.value = file.raw;
  const blobUrl = URL.createObjectURL(file.raw);
  editingProduct.value.pThumbnail = blobUrl;
  // 注意：实际更新到后端可能需要 formData，此处仅演示前端预览逻辑
};

// 4.3 删除展示图
const handleRemoveEditShowcaseItem = (index) => {
  editingProduct.value.pShowcaseImageList.splice(index, 1);
};

// 4.4 添加/预览展示图
const handleEditGalleryChange = (file) => {
  newGalleryFiles.value.push(file.raw);
  const blobUrl = URL.createObjectURL(file.raw);
  if (!editingProduct.value.pShowcaseImageList) {
    editingProduct.value.pShowcaseImageList = [];
  }
  editingProduct.value.pShowcaseImageList.push(blobUrl);
};

const updateProductInfo = async () => {
  // === 【校验 1】强制必须有缩略图 ===
  // 如果界面上 pThumbnail 为空（说明用户把旧的删了，又没传新的），则阻止提交
  if (!editingProduct.value.pThumbnail) {
    ElMessage.warning('商品必须包含一张缩略图，请上传后再保存。');
    return;
  }

  updating.value = true;
  try {
    const formData = new FormData();
    const product = editingProduct.value;

    // 1. 基本信息
    formData.append('pID', product.pID);
    formData.append('pName', product.pName);
    formData.append('pType', product.pType);
    formData.append('pDiscount', product.pDiscount);
    formData.append('pPrice', product.pPrice);
    formData.append('pProducer', product.pProducer || '');
    formData.append('pReleaseDate', product.pReleaseDate || '');
    formData.append('pInfo', product.pInfo || '');
    formData.append('pInventory', product.pInventory);
    formData.append('pStatus', product.pStatus);

    // ===============================================
    // 2. 缩略图逻辑 (根据你的要求修改)
    // ===============================================

    // 旧路径 (始终传)
    const oldUrl = product.oldThumbnailPicURL || '';
    formData.append('oldThumbnailPicURL', oldUrl);

    // 计算 newThumbnailPicURL 的值
    let newUrlStr = '';

    // 判断有没有新文件
    if (newThumbnailFile.value) {
      // 有新文件 -> newThumbnailPicURL 留空
      newUrlStr = '';

      // 放入文件对象
      formData.append('newThumbnailItem.pImgType', '缩略图');
      formData.append('newThumbnailItem.fileData', newThumbnailFile.value);
    } else {
      // 没有新文件 -> newThumbnailPicURL = 旧路径
      newUrlStr = oldUrl;
    }

    // 将计算好的 newThumbnailPicURL 放入表单
    formData.append('newThumbnailPicURL', newUrlStr);


    // ===============================================
    // 3. 展示图逻辑 (保持不变)
    // ===============================================
    if (product.oldShowcaseImagesURL && product.oldShowcaseImagesURL.length > 0) {
      product.oldShowcaseImagesURL.forEach(url => {
        formData.append('oldShowcaseImagesURL', url);
      });
    }
    if (product.pShowcaseImageList && product.pShowcaseImageList.length > 0) {
      const remainingOldImages = product.pShowcaseImageList.filter(url => !url.startsWith('blob:'));
      remainingOldImages.forEach(url => {
        formData.append('newShowcaseImagesURL', url);
      });
    }
    if (newGalleryFiles.value && newGalleryFiles.value.length > 0) {
      newGalleryFiles.value.forEach((file, index) => {
        formData.append(`pShowcaseImageList[${index}].pImgType`, '展示图');
        formData.append(`pShowcaseImageList[${index}].pImagePath`, file);
      });
    }

    // 4. 发送请求
    const res = await axios.post(`${API_BASE_URL}/api/ProductInfoUpdate`, formData);
    // alert(res.data);
    if (res.data === 'Success' || (typeof res.data === 'string' && res.data.includes('Success'))) {
      ElMessage.success('商品信息更新成功');
      showDetailDialog.value = false;
      fetchProductList();
    } else {
      ElMessage.error('更新失败: ' + res.data);
    }

  } catch (error) {
    console.error('Update Error:', error);
    ElMessage.error('更新请求发生异常');
  } finally {
    updating.value = false;
  }
};

</script>

<style scoped>
.app-container {
  height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.main-container {
  height: 100%;
}

/* Header Styles */
.app-header {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  padding: 0 20px;
  height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.brand {
  display: flex;
  align-items: center;
}

.logo-avatar {
  background-color: #409EFF;
  margin-right: 12px;
}

.app-title {
  font-size: 18px;
  color: #303133;
  margin: 0;
}

/* 右侧区域布局 */
.header-right-section {
  display: flex;
  align-items: center;
}

.nav-actions {
  display: flex;
  gap: 20px;
  margin-right: 10px;
}

.nav-actions .el-button {
  font-size: 16px;
  color: #606266;
}

.nav-actions .el-button.active {
  color: #409EFF;
  font-weight: bold;
}

.header-divider {
  margin: 0 20px;
  height: 20px;
}

/* 用户信息下拉 */
.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #303133;
}

.user-avatar {
  margin-right: 8px;
  background-color: #409EFF;
}

.username {
  font-weight: 500;
  margin-right: 4px;
}

/* Main Content */
.app-main {
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
  margin: 0;
  font-size: 20px;
  color: #303133;
}

/* Product Grid */
.product-card {
  cursor: pointer;
  transition: transform 0.2s;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.image-wrapper {
  position: relative;
  height: 180px;
  width: 100%;
  background-color: #f5f7fa;
}

.card-image {
  width: 100%;
  height: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #909399;
  font-size: 30px;
}

.status-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.card-content {
  padding: 14px;
}

.product-title {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.inventory {
  font-size: 12px;
  color: #909399;
}

.desc {
  font-size: 13px;
  color: #606266;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 36px;
}

.empty-state {
  padding: 50px 0;
  text-align: center;
}

.mb-20 {
  margin-bottom: 20px;
}

/* Add Form */
.form-container {
  background: #fff;
  padding: 30px;
  border-radius: 4px;
  max-width: 800px;
  margin: 0 auto;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-preview {
  width: 120px;
  height: 120px;
  display: block;
}

/* Edit Dialog Styles - 新增/更新的部分 */
.edit-image-section h4 {
  margin: 15px 0 10px;
  color: #606266;
}

/* 统一的图片预览容器 */
.image-preview-wrapper {
  position: relative;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #cdd0d6;
  display: inline-block;
  box-sizing: border-box;
}

/* 缩略图特定尺寸 */
.thumbnail-area .image-preview-wrapper {
  width: 148px;
  height: 148px;
}

/* 展示图列表布局 */
.gallery-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.gallery-item {
  width: 148px;
  height: 148px;
}

/* 图片填满容器 */
.preview-img {
  width: 100%;
  height: 100%;
  display: block;
}

/* 遮罩层 - 默认隐藏 */
.image-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  cursor: pointer;
}

/* 鼠标悬停显示遮罩 */
.image-preview-wrapper:hover .image-mask {
  opacity: 1;
}

/* 删除图标样式 */
.delete-icon {
  font-size: 24px;
  color: #fff;
}

.delete-icon:hover {
  color: #f56c6c;
}

/* 调整展示图上传框的样式，使其与图片对齐 */
.gallery-uploader :deep(.el-upload--picture-card) {
  width: 148px;
  height: 148px;
  line-height: 148px;
}

.tip-text {
  color: #e6a23c;
  font-size: 12px;
}
</style>