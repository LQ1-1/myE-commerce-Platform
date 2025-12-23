<template>
    <div class="admin-container">
        <el-container class="layout-container">
            <!-- 侧边栏导航 -->
            <el-aside width="240px" class="aside-menu">
                <div class="logo-area">
                    <h3>EBuyPlt 管理后台</h3>
                    <p>当前管理员: {{ currentAdminID }}</p>
                </div>

                <el-menu :default-active="activeMenu" class="el-menu-vertical" background-color="#304156"
                    text-color="#bfcbd9" active-text-color="#409EFF" @select="handleMenuSelect">
                    <el-sub-menu index="1">
                        <template #title>
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>用户管理</span>
                        </template>
                        <el-menu-item index="UserAccountTable">用户账户信息 (搜索/详情)</el-menu-item>
                        <el-menu-item index="UserDeliveryInfoTable">用户收货地址</el-menu-item>
                        <el-menu-item index="UserShoppingCartTable">用户购物车</el-menu-item>
                        <el-menu-item index="UserFavoritesTable">用户收藏夹</el-menu-item>
                    </el-sub-menu>

                    <el-sub-menu index="2">
                        <template #title>
                            <el-icon>
                                <Goods />
                            </el-icon>
                            <span>商品管理</span>
                        </template>
                        <el-menu-item index="ProductTable">商品基础信息 (搜索/详情)</el-menu-item>
                        <el-menu-item index="MerchantsProductTable">商户上架记录</el-menu-item>
                        <el-menu-item index="ProductImagesTable">商品图片记录</el-menu-item>
                        <el-menu-item index="ProductClicksInfoTable">商品点击统计</el-menu-item>
                    </el-sub-menu>

                    <el-sub-menu index="3">
                        <template #title>
                            <el-icon>
                                <List />
                            </el-icon>
                            <span>订单管理</span>
                        </template>
                        <el-menu-item index="OrderFullInfoTable">订单完整详情 (搜索/详情)</el-menu-item>
                        <el-menu-item index="OrderProductInfoTable">订单商品明细</el-menu-item>
                    </el-sub-menu>
                </el-menu>
            </el-aside>

            <!-- 右侧内容区 -->
            <el-container>
                <el-header class="admin-header">
                    <div class="header-left">
                        <span class="breadcrumb">当前位置：{{ currentMenuName }}</span>
                    </div>
                    <div class="header-right">
                        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
                    </div>
                </el-header>

                <el-main class="admin-main" v-loading="loading">

                    <!-- ================== 搜索栏区域 ================== -->

                    <!-- 1. 用户搜索栏 -->
                    <div v-if="activeMenu === 'UserAccountTable'" class="search-box">
                        <el-form :model="userSearchParams" size="small">
                            <!-- 第一行：综合关键字搜索 -->
                            <el-row :gutter="20" style="margin-bottom: 10px;">
                                <el-col :span="24">
                                    <el-form-item label="关键字搜索" style="margin-bottom: 0;">
                                        <el-input v-model="userSearchParams.SearchInput" placeholder="请输入任意关键字进行匹配..."
                                            clearable style="width: 100%;" />
                                    </el-form-item>
                                </el-col>
                            </el-row>

                            <!-- 第二行：详细字段 -->
                            <el-form-item style="margin-bottom: 10px;">
                                <el-row :gutter="10" align="middle">
                                    <el-col :span="4">
                                        <el-input v-model="userSearchParams.uID" placeholder="账号ID" clearable>
                                            <template #prepend>ID</template>
                                        </el-input>
                                    </el-col>
                                    <el-col :span="4">
                                        <el-input v-model="userSearchParams.uNickName" placeholder="昵称" clearable>
                                            <template #prepend>昵称</template>
                                        </el-input>
                                    </el-col>
                                    <el-col :span="5">
                                        <el-input v-model="userSearchParams.uPhone" placeholder="电话" clearable>
                                            <template #prepend>电话</template>
                                        </el-input>
                                    </el-col>
                                    <el-col :span="5">
                                        <el-input v-model="userSearchParams.uEmail" placeholder="邮箱" clearable>
                                            <template #prepend>邮箱</template>
                                        </el-input>
                                    </el-col>
                                    <el-col :span="4">
                                        <el-input v-model="userSearchParams.uGender" placeholder="手动输入" clearable>
                                            <template #prepend>性别</template>
                                        </el-input>
                                    </el-col>
                                </el-row>
                            </el-form-item>

                            <!-- 第三行：类型、状态、日期、按钮 -->
                            <div style="display: flex; flex-wrap: wrap; gap: 10px; align-items: center;">
                                <div style="display: flex; align-items: center;">
                                    <span style="font-size: 12px; color: #606266; margin-right: 8px;">用户类型:</span>
                                    <el-select v-model="userSearchParams.uAccountTypes" multiple placeholder="选择类型"
                                        style="width: 260px">
                                        <el-option label="普通用户" value="普通用户" />
                                        <el-option label="商户" value="商户" />
                                        <el-option label="管理员" value="管理员" />
                                    </el-select>
                                </div>

                                <div style="display: flex; align-items: center;">
                                    <span style="font-size: 12px; color: #606266; margin-right: 8px;">账号状态:</span>
                                    <el-select v-model="userSearchParams.uAccountStatuses" multiple placeholder="选择状态"
                                        style="width: 240px">
                                        <el-option label="正常" value="正常" />
                                        <el-option label="封禁" value="封禁" />
                                        <el-option label="注销" value="注销" />
                                    </el-select>
                                </div>

                                <el-date-picker v-model="userDateRange" type="daterange" range-separator="至"
                                    start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD"
                                    style="width: 240px" />

                                <div style="margin-left: auto;">
                                    <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                                    <el-button @click="resetSearch">重置</el-button>
                                </div>
                            </div>
                        </el-form>
                    </div>

                    <!-- 2. 商品搜索栏 (参照 ShoppingnbView 改造) -->
                    <div v-if="activeMenu === 'ProductTable'" class="search-box">
                        <el-form :model="productSearchParams" size="small" label-width="70px">
                            <!-- 顶部：综合搜索 -->
                            <el-row :gutter="20" style="margin-bottom: 10px;">
                                <el-col :span="24">
                                    <el-form-item label="搜索" style="margin-bottom: 0;">
                                        <el-input v-model="productSearchParams.SearchDesciption"
                                            placeholder="请输入商品的关键字..." clearable />
                                    </el-form-item>
                                </el-col>
                            </el-row>

                            <!-- 中间：详细筛选 -->
                            <el-row :gutter="20">
                                <el-col :xs="24" :sm="12" :md="6">
                                    <el-form-item label="商品ID">
                                        <el-input v-model="productSearchParams.pID" placeholder="输入ID" clearable />
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="6">
                                    <el-form-item label="生产厂商">
                                        <el-input v-model="productSearchParams.pProducer" placeholder="输入厂商名"
                                            clearable />
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="6">
                                    <el-form-item label="商品类型">
                                        <el-select v-model="productSearchParams.pType" placeholder="选择或输入"
                                            style="width: 100%" filterable allow-create default-first-option clearable>
                                            <el-option label="全部" value="" />
                                            <el-option v-for="cat in categoryList" :key="cat" :label="cat"
                                                :value="cat" />
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="6">
                                    <el-form-item label="详情描述">
                                        <el-input v-model="productSearchParams.pInfo" placeholder="描述关键词" clearable />
                                    </el-form-item>
                                </el-col>
                            </el-row>

                            <!-- 底部：价格、日期、按钮 -->
                            <el-row :gutter="20">
                                <el-col :xs="24" :sm="12" :md="8">
                                    <el-form-item label="价格区间">
                                        <div style="display: flex; align-items: center; gap: 5px;">
                                            <el-input-number v-model="productSearchParams.pPrice_f" :min="0"
                                                :controls="false" placeholder="Min" style="width: 100%" />
                                            <span>-</span>
                                            <el-input-number v-model="productSearchParams.pPrice_r" :min="0"
                                                :controls="false" placeholder="Max" style="width: 100%" />
                                        </div>
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="10">
                                    <el-form-item label="发布日期">
                                        <el-date-picker v-model="productDateRange" type="daterange" range-separator="至"
                                            start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD"
                                            style="width: 100%" />
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="24" :md="6" style="text-align: right;">
                                    <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                                    <el-button @click="resetSearch">重置</el-button>
                                </el-col>
                            </el-row>
                        </el-form>
                    </div>

                    <!-- 3. 订单搜索栏 -->
                    <div v-if="activeMenu === 'OrderFullInfoTable'" class="search-box">
                        <el-form :inline="true" :model="orderSearchParams" size="small">
                            <el-form-item label="综合搜索">
                                <el-input v-model="orderSearchParams.SearchInput" placeholder="订单号/收件人/电话" clearable />
                            </el-form-item>
                            <el-form-item label="订单状态">
                                <el-select v-model="orderSearchParams.oStatuses" multiple collapse-tags placeholder="状态"
                                    style="width: 150px">
                                    <el-option label="待发货" value="待发货" />
                                    <el-option label="已发货" value="已发货" />
                                    <el-option label="已完成" value="已完成" />
                                    <el-option label="已取消" value="已取消" />
                                </el-select>
                            </el-form-item>
                            <el-form-item label="下单日期">
                                <el-date-picker v-model="orderDateRange" type="daterange" range-separator="至"
                                    start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                                <el-button @click="resetSearch">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>

                    <!-- ================== 表格区域 ================== -->
                    <div class="table-content">

                        <!-- 1. 用户账户表 -->
                        <div v-if="activeMenu === 'UserAccountTable'" style="height: 100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="uID" label="用户ID" width="120" fixed />
                                <el-table-column prop="uNickName" label="昵称" width="120" />
                                <el-table-column prop="uAccountType" label="账户类型" width="100">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.uAccountType === '管理员' ? 'danger' : 'success'">
                                            {{ scope.row.uAccountType }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="uAccountStatus" label="状态" width="100">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.uAccountStatus === '正常' ? 'primary' : 'info'">
                                            {{ scope.row.uAccountStatus }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="uPhone" label="电话" width="120" />
                                <el-table-column prop="uEmail" label="邮箱" width="180" />
                                <el-table-column prop="uGender" label="性别" width="80" />
                                <el-table-column prop="uRegisterDate" label="注册日期" width="180" />
                                <el-table-column label="操作" width="160" fixed="right">
                                    <template #default="scope">
                                        <el-button type="primary" link size="small"
                                            @click="openEditDialog(scope.row)">编辑</el-button>
                                        <!-- 跳转详情 -->
                                        <el-button type="success" link size="small"
                                            @click="goToUserDetail(scope.row)">详情</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <!-- 5. 商品基础信息表 -->
                        <div v-else-if="activeMenu === 'ProductTable'" style="height: 100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="pID" label="商品ID" width="100" fixed />
                                <el-table-column label="缩略图" width="80">
                                    <template #default="scope">
                                        <el-image style="width: 40px; height: 40px"
                                            :src="getImageUrl(scope.row.pImagePath)"
                                            :preview-src-list="[getImageUrl(scope.row.pImagePath)]" fit="cover"
                                            preview-teleported>
                                            <template #error>
                                                <div class="image-slot"><el-icon>
                                                        <Picture />
                                                    </el-icon></div>
                                            </template>
                                        </el-image>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="pName" label="商品名称" width="150" show-overflow-tooltip />
                                <el-table-column prop="pType" label="类型" width="100" />
                                <el-table-column prop="pPrice" label="价格" width="100" />
                                <el-table-column prop="pInventory" label="库存" width="100" />
                                <el-table-column prop="pProducer" label="生产商" width="150" />
                                <el-table-column prop="pStatus" label="状态" width="100" />
                                <el-table-column prop="pReleaseDate" label="发布日期" width="120" />
                                <el-table-column prop="pInfo" label="简介" show-overflow-tooltip />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <!-- 跳转详情 -->
                                        <el-button type="success" link size="small"
                                            @click="goToProductDetail(scope.row)">详情</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <!-- 9. 订单完整详情表 -->
                        <div v-else-if="activeMenu === 'OrderFullInfoTable'" style="height: 100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="oOrderID" label="订单号" width="180" fixed />
                                <el-table-column prop="oDate" label="下单时间" width="180" />
                                <el-table-column prop="oStatus" label="状态" width="100">
                                    <template #default="scope">
                                        <el-tag>{{ scope.row.oStatus }}</el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="oReceiverName" label="收件人" width="120" />
                                <el-table-column prop="oDeliveryAddress" label="收件地址" min-width="200" />
                                <el-table-column prop="oContactPhone" label="联系电话" width="120" />
                                <el-table-column prop="oDeliveryNote" label="备注" />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <!-- 跳转详情 -->
                                        <el-button type="success" link size="small"
                                            @click="goToOrderDetail(scope.row)">详情</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <!-- 其他普通表格 -->
                        <div v-else-if="activeMenu === 'UserDeliveryInfoTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column type="index" label="#" width="50" />
                                <el-table-column prop="uID" label="用户ID" width="120" />
                                <el-table-column prop="uContactPersonName" label="收货人" width="120" />
                                <el-table-column prop="uContactPersonPhone" label="联系电话" width="140" />
                                <el-table-column prop="uDeliveryAddress" label="收货地址" min-width="200" />
                                <el-table-column prop="oPostalCode" label="邮编" width="100" />
                                <el-table-column prop="oDeliveryNote" label="备注" />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'UserShoppingCartTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="uID" label="用户ID" />
                                <el-table-column prop="pID" label="商品ID" />
                                <el-table-column prop="cAmount" label="数量" />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'UserFavoritesTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="uID" label="用户ID" />
                                <el-table-column prop="pID" label="收藏商品ID" />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'MerchantsProductTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="uID" label="商户ID" />
                                <el-table-column prop="pID" label="商品ID" />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'ProductImagesTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="pID" label="商品ID" width="120" />
                                <el-table-column prop="pType" label="图片类型" width="120" />
                                <el-table-column label="图片预览" width="120">
                                    <template #default="scope">
                                        <el-image style="width: 50px; height: 50px"
                                            :src="getImageUrl(scope.row.pImagePath)"
                                            :preview-src-list="[getImageUrl(scope.row.pImagePath)]" fit="cover"
                                            preview-teleported>
                                            <template #error>
                                                <div class="image-slot"><el-icon>
                                                        <Picture />
                                                    </el-icon></div>
                                            </template>
                                        </el-image>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="pImagePath" label="路径" show-overflow-tooltip />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'ProductClicksInfoTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="pID" label="商品ID" />
                                <el-table-column prop="pClicksAmount" label="点击量" sortable />
                            </el-table>
                        </div>

                        <div v-else-if="activeMenu === 'OrderProductInfoTable'" style="height:100%">
                            <el-table :data="tableData" border stripe height="100%">
                                <el-table-column prop="oOrderID" label="订单号" width="180" />
                                <el-table-column prop="pID" label="商品ID" width="120" />
                                <el-table-column prop="oPrice" label="成交单价" width="120" />
                                <el-table-column prop="oAmount" label="数量" width="100" />
                                <el-table-column prop="oProductDeliveryStatus" label="发货状态" />
                            </el-table>
                        </div>

                        <el-empty v-else description="请选择左侧菜单查看数据" />
                    </div>

                </el-main>
            </el-container>
        </el-container>

        <!-- 编辑用户信息的弹窗 -->
        <el-dialog v-model="editDialogVisible" title="修改用户信息" width="500px">
            <el-form :model="editForm" label-width="80px">
                <el-form-item label="用户ID">
                    <el-input v-model="editForm.uID" disabled />
                </el-form-item>
                <el-form-item label="注册时间">
                    <el-input v-model="editForm.uRegisterDate" disabled placeholder="注册时间不可修改" />
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="editForm.uNickName" />
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="editForm.uPassword" type="password" show-password placeholder="不修改请保留原值" />
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="editForm.uPhone" />
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="editForm.uEmail" />
                </el-form-item>
                <el-form-item label="性别">
                    <el-select v-model="editForm.uGender">
                        <el-option label="Male" value="Male" />
                        <el-option label="Female" value="Female" />
                    </el-select>
                </el-form-item>
                <el-form-item label="账户类型">
                    <el-select v-model="editForm.uAccountType">
                        <el-option label="普通用户" value="普通用户" />
                        <el-option label="商户" value="商户" />
                        <el-option label="管理员" value="管理员" />
                    </el-select>
                </el-form-item>
                <el-form-item label="账户状态">
                    <el-select v-model="editForm.uAccountStatus">
                        <el-option label="正常" value="正常" />
                        <el-option label="封禁" value="封禁" />
                        <el-option label="注销" value="注销" />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="handleUpdateUser">确认修改</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { User, Goods, List, Picture, Search } from '@element-plus/icons-vue'

const router = useRouter()
const BASE_URL = 'http://192.168.126.94:8082'

// 状态
const currentAdminID = ref('')
const activeMenu = ref('UserAccountTable')
const tableData = ref([])
const loading = ref(false)

// --- 搜索相关状态 ---

// 1. 用户搜索参数
const userSearchParams = reactive({
    SearchInput: '',
    uID: '',
    uNickName: '',
    uPhone: '',
    uEmail: '',
    uGender: '',
    uAccountTypes: ['普通用户', '商户', '管理员'],
    uAccountStatuses: ['正常', '封禁', '注销']
})
const userDateRange = ref([])

// 2. 商品搜索参数 (参照 ShoppingnbView 字段)
const productSearchParams = reactive({
    SearchDesciption: '', // 综合搜索
    pID: '',
    pProducer: '',
    pType: '',
    pInfo: '',
    pPrice_f: undefined, // 对应 filterForm.minPrice
    pPrice_r: undefined  // 对应 filterForm.maxPrice
})
const productDateRange = ref([])
const categoryList = ref([]) // 存储商品分类

// 3. 订单搜索参数
const orderSearchParams = reactive({
    SearchInput: '',
    oStatuses: []
})
const orderDateRange = ref([])

// 编辑用户状态
const editDialogVisible = ref(false)
const editForm = reactive({
    uID: '',
    uNickName: '',
    uPassword: '',
    uPhone: '',
    uEmail: '',
    uGender: '',
    uRegisterDate: '',
    uAccountType: '',
    uAccountStatus: ''
})

const currentMenuName = computed(() => {
    const map = {
        'UserAccountTable': '用户账户信息',
        'UserDeliveryInfoTable': '用户收货地址',
        'UserShoppingCartTable': '用户购物车',
        'UserFavoritesTable': '用户收藏夹',
        'ProductTable': '商品基础信息',
        'MerchantsProductTable': '商户上架记录',
        'ProductImagesTable': '商品图片记录',
        'ProductClicksInfoTable': '商品点击统计',
        'OrderFullInfoTable': '订单完整详情',
        'OrderProductInfoTable': '订单商品明细',
    }
    return map[activeMenu.value] || '后台首页'
})

onMounted(() => {
    const uID = sessionStorage.getItem('uID')
    if (!uID) {
        ElMessage.error('请先登录')
        router.push('/')
        return
    }
    currentAdminID.value = uID
    fetchData(activeMenu.value)
    fetchCategories() // 初始化获取商品分类
})

const handleLogout = () => {
    sessionStorage.removeItem('uID')
    router.push('/')
}

const handleMenuSelect = (index) => {
    activeMenu.value = index
    resetSearch(false)
    fetchData(index)
}

// 获取全量数据
const fetchData = async (tableName) => {
    loading.value = true
    tableData.value = []
    try {
        const url = `${BASE_URL}/api/Admin${tableName}`
        const res = await axios.post(url, {})

        if (res.data && res.data.data) {
            tableData.value = res.data.data
        } else {
            // ElMessage.warning('暂无数据')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error(`获取 ${tableName} 失败`)
    } finally {
        loading.value = false
    }
}

// 获取商品分类 (参照 ShoppingnbView)
const fetchCategories = async () => {
    try {
        const res = await axios.post(`${BASE_URL}/api/GetAllProductType`, {})
        if (res.data && res.data.data) {
            categoryList.value = res.data.data
        }
    } catch (e) {
        console.error('获取商品分类失败', e)
    }
}

// --- 搜索逻辑 ---
const handleSearch = async () => {
    loading.value = true
    tableData.value = []

    try {
        let url = ''
        let params = {}

        if (activeMenu.value === 'UserAccountTable') {
            url = `${BASE_URL}/api/AdminUserAccountTableSearch`
            params = {
                ...userSearchParams,
                DateL: userDateRange.value ? userDateRange.value[0] : '',
                DateR: userDateRange.value ? userDateRange.value[1] : ''
            }
        }
        else if (activeMenu.value === 'ProductTable') {
            url = `${BASE_URL}/api/AdminProductTableSearch`
            // 构造与 ShoppingnbView 逻辑一致的 payload
            params = {
                SearchDesciption: productSearchParams.SearchDesciption,
                pID: productSearchParams.pID,
                pType: productSearchParams.pType,
                pPrice_f: productSearchParams.pPrice_f || 0.0,
                pPrice_r: productSearchParams.pPrice_r || 0.0,
                pProducer: productSearchParams.pProducer,
                pReleaseDate_f: productDateRange.value ? productDateRange.value[0] : '',
                pReleaseDate_r: productDateRange.value ? productDateRange.value[1] : '',
                pInfo: productSearchParams.pInfo
            }
        }
        else if (activeMenu.value === 'OrderFullInfoTable') {
            url = `${BASE_URL}/api/AdminOrderInfoSearch`
            params = {
                ...orderSearchParams,
                DateF: orderDateRange.value ? orderDateRange.value[0] : '',
                DateR: orderDateRange.value ? orderDateRange.value[1] : ''
            }
        } else {
            fetchData(activeMenu.value)
            return
        }
        // alert(params.pProducer);

        const res = await axios.post(url, params)
        if (res.data && res.data.data) {
            tableData.value = res.data.data
            ElMessage.success('搜索完成')
        } else {
            ElMessage.info('未找到匹配数据')
        }

    } catch (error) {
        console.error(error)
        ElMessage.error('搜索请求失败')
    } finally {
        loading.value = false
    }
}

// --- 重置逻辑 ---
const resetSearch = (shouldFetch = true) => {
    // 重置用户搜索
    userSearchParams.SearchInput = ''
    userSearchParams.uID = ''
    userSearchParams.uNickName = ''
    userSearchParams.uPhone = ''
    userSearchParams.uEmail = ''
    userSearchParams.uGender = ''
    userSearchParams.uAccountTypes = ['普通用户', '商户', '管理员']
    userSearchParams.uAccountStatuses = ['正常', '封禁', '注销']
    userDateRange.value = []

    // 重置商品搜索
    productSearchParams.SearchDesciption = ''
    productSearchParams.pID = ''
    productSearchParams.pProducer = ''
    productSearchParams.pType = ''
    productSearchParams.pInfo = ''
    productSearchParams.pPrice_f = undefined
    productSearchParams.pPrice_r = undefined
    productDateRange.value = []

    // 重置订单搜索
    orderSearchParams.SearchInput = ''
    orderSearchParams.oStatuses = []
    orderDateRange.value = []

    if (shouldFetch) {
        fetchData(activeMenu.value)
    }
}

// --- 详情页跳转逻辑 ---
const goToUserDetail = (row) => {
    router.push({
        name: 'AdminUserDetailView',
        params: { uID: row.uID },
        state: { userData: JSON.stringify(row) }
    })
}

const goToOrderDetail = (row) => {
    router.push({
        name: 'AdminOrderDetailView',
        params: { oOrderID: row.oOrderID },
        state: { orderData: JSON.stringify(row) }
    })
}

const goToProductDetail = (row) => {
    router.push({
        name: 'AdminProductDetailView',
        params: { pID: row.pID }
    })
}

// --- 编辑用户逻辑 ---
const openEditDialog = (row) => {
    Object.assign(editForm, row)
    editDialogVisible.value = true
}

const handleUpdateUser = async () => {
    try {
        const res = await axios.post(`${BASE_URL}/api/AdminUserAccountTableUpdate`, editForm)
        if (res.data === 'success') {
            ElMessage.success('更新成功')
            editDialogVisible.value = false
            fetchData('UserAccountTable')
        } else {
            ElMessage.error('更新失败: ' + res.data)
        }
    } catch (error) {
        console.error(error)
        ElMessage.error('网络请求错误')
    }
}

// --- 辅助函数：处理图片URL ---
const getImageUrl = (path) => {
    if (!path) return ''
    let cleanPath = path.replace(/\\/g, '/')
    if (!cleanPath.startsWith('/')) {
        cleanPath = '/' + cleanPath
    }
    return `${BASE_URL}${cleanPath}`
}

</script>

<style scoped>
.admin-container {
    height: 100vh;
    width: 100vw;
    background-color: #f0f2f5;
}

.layout-container {
    height: 100%;
}

.aside-menu {
    background-color: #304156;
    color: white;
    display: flex;
    flex-direction: column;
}

.logo-area {
    height: 60px;
    background-color: #2b2f3a;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #fff;
}

.logo-area h3 {
    margin: 0;
    font-size: 18px;
}

.logo-area p {
    margin: 5px 0 0 0;
    font-size: 12px;
    color: #909399;
}

.el-menu-vertical {
    border-right: none;
    flex: 1;
}

.admin-header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    height: 50px;
}

.breadcrumb {
    font-weight: bold;
    color: #303133;
}

.admin-main {
    padding: 20px;
    background-color: #f0f2f5;
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

/* 搜索框样式 */
.search-box {
    background: #fff;
    padding: 18px 18px 0 18px;
    margin-bottom: 15px;
    border-radius: 4px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

/* 表格容器样式 - 占据剩余空间 */
.table-content {
    flex: 1;
    background: #fff;
    padding: 10px;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 20px;
}
</style>