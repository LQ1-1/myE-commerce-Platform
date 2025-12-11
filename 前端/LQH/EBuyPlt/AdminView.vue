<template>
    <div class="admin-container">
        <el-container class="layout-container">
            <!-- 侧边栏导航 (保持不变) -->
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
                        <el-menu-item index="UserAccountTable">用户账户信息 (可编辑)</el-menu-item>
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
                        <el-menu-item index="ProductTable">商品基础信息</el-menu-item>
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
                        <el-menu-item index="OrderFullInfoTable">订单完整详情</el-menu-item>
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
                    <!-- 1. 用户账户表 -->
                    <div v-if="activeMenu === 'UserAccountTable'">
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
                            <!-- 列表里显示注册日期 -->
                            <el-table-column prop="uRegisterDate" label="注册日期" width="180" />
                            <el-table-column label="操作" width="100" fixed="right">
                                <template #default="scope">
                                    <el-button type="primary" size="small"
                                        @click="openEditDialog(scope.row)">编辑</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                    <!-- 其他表格保持不变 ... -->
                    <div v-else-if="activeMenu === 'UserDeliveryInfoTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="uID" label="用户ID" width="120" />
                            <el-table-column prop="uContactPersonName" label="收货人" width="120" />
                            <el-table-column prop="uContactPersonPhone" label="联系电话" width="140" />
                            <el-table-column prop="uDeliveryAddress" label="收货地址" min-width="200" />
                            <el-table-column prop="oPostalCode" label="邮编" width="100" />
                            <el-table-column prop="oDeliveryNote" label="备注" />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'UserShoppingCartTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="uID" label="用户ID" />
                            <el-table-column prop="pID" label="商品ID" />
                            <el-table-column prop="cAmount" label="数量" />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'UserFavoritesTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="uID" label="用户ID" />
                            <el-table-column prop="pID" label="收藏商品ID" />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'ProductTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="pID" label="商品ID" width="100" fixed />
                            <el-table-column prop="pName" label="商品名称" width="150" show-overflow-tooltip />
                            <el-table-column prop="pType" label="类型" width="100" />
                            <el-table-column prop="pPrice" label="价格" width="100" />
                            <el-table-column prop="pInventory" label="库存" width="100" />
                            <el-table-column prop="pProducer" label="生产商" width="150" />
                            <el-table-column prop="pStatus" label="状态" width="100" />
                            <el-table-column prop="pReleaseDate" label="发布日期" width="120" />
                            <el-table-column prop="pInfo" label="简介" show-overflow-tooltip />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'MerchantsProductTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="uID" label="商户ID" />
                            <el-table-column prop="pID" label="商品ID" />
                        </el-table>
                    </div>

                    <!-- 7. 商品图片记录 (修改了图片加载逻辑) -->
                    <div v-else-if="activeMenu === 'ProductImagesTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="pID" label="商品ID" width="120" />
                            <el-table-column prop="pType" label="图片类型" width="120" />
                            <el-table-column label="图片预览" width="120">
                                <template #default="scope">
                                    <!-- 使用 getImageUrl 方法处理路径 -->
                                    <el-image style="width: 50px; height: 50px" :src="getImageUrl(scope.row.pImagePath)"
                                        :preview-src-list="[getImageUrl(scope.row.pImagePath)]" fit="cover"
                                        preview-teleported>
                                        <!-- 图片加载失败时的插槽 -->
                                        <template #error>
                                            <div class="image-slot">
                                                <el-icon><icon-picture /></el-icon>
                                            </div>
                                        </template>
                                    </el-image>
                                </template>
                            </el-table-column>
                            <el-table-column prop="pImagePath" label="路径" show-overflow-tooltip />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'ProductClicksInfoTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="pID" label="商品ID" />
                            <el-table-column prop="pClicksAmount" label="点击量" sortable />
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'OrderFullInfoTable'">
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
                        </el-table>
                    </div>

                    <div v-else-if="activeMenu === 'OrderProductInfoTable'">
                        <el-table :data="tableData" border stripe height="100%">
                            <el-table-column prop="oOrderID" label="订单号" width="180" />
                            <el-table-column prop="pID" label="商品ID" width="120" />
                            <el-table-column prop="oPrice" label="成交单价" width="120" />
                            <el-table-column prop="oAmount" label="数量" width="100" />
                        </el-table>
                    </div>

                    <el-empty v-else description="请选择左侧菜单查看数据" />

                </el-main>
            </el-container>
        </el-container>

        <!-- 编辑用户信息的弹窗 -->
        <el-dialog v-model="editDialogVisible" title="修改用户信息" width="500px">
            <el-form :model="editForm" label-width="80px">
                <el-form-item label="用户ID">
                    <el-input v-model="editForm.uID" disabled />
                </el-form-item>

                <!-- 新增：注册时间 (disabled 确保不可修改) -->
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
import { User, Goods, List, Picture as IconPicture } from '@element-plus/icons-vue'

const router = useRouter()
const BASE_URL = 'http://192.168.66.94:8082'

// 状态
const currentAdminID = ref('')
const activeMenu = ref('UserAccountTable')
const tableData = ref([])
const loading = ref(false)

// 编辑相关状态
const editDialogVisible = ref(false)
const editForm = reactive({
    uID: '',
    uNickName: '',
    uPassword: '',
    uPhone: '',
    uEmail: '',
    uGender: '',
    uRegisterDate: '', // 确保这里有定义
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
})

const handleLogout = () => {
    sessionStorage.removeItem('uID')
    router.push('/')
}

const handleMenuSelect = (index) => {
    activeMenu.value = index
    fetchData(index)
}

// 核心：获取数据方法
const fetchData = async (tableName) => {
    loading.value = true
    tableData.value = []
    try {
        const url = `${BASE_URL}/api/Admin${tableName}`
        const res = await axios.post(url, {})

        if (res.data && res.data.data) {
            tableData.value = res.data.data
        } else {
            ElMessage.warning('暂无数据或数据格式异常')
        }
    } catch (error) {
        console.error(error)
        ElMessage.error(`获取 ${tableName} 失败`)
    } finally {
        loading.value = false
    }
}

// --- 编辑用户逻辑 ---
const openEditDialog = (row) => {
    // 浅拷贝数据到表单
    Object.assign(editForm, row)
    editDialogVisible.value = true
}

const handleUpdateUser = async () => {
    try {
        // 注意：虽然表单里有 uRegisterDate，但后端更新接口通常只需要 ID 和要修改的字段
        // 因为 uRegisterDate 是 disabled 的，这里传过去也无所谓，只要后端逻辑不修改它即可
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

    // 1. 将反斜杠替换为正斜杠 (防止Windows路径问题)
    let cleanPath = path.replace(/\\/g, '/')

    // 2. 确保路径以 / 开头，防止拼接时 8082image 这种情况
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
}

.admin-main>div {
    height: 100%;
    background: #fff;
    padding: 10px;
    border-radius: 4px;
}

/* 简单的图片错误占位符样式 */
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