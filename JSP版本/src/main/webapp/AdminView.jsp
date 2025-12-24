<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EBuyPlt 管理后台</title>

    <!-- 1. Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 3. jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- 4. Bootstrap Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- 5. SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- 6. CryptoJS (如果修改密码需要加密) -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>

    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            overflow: hidden;
            background-color: #f0f2f5;
            font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
        }

        /* --- Layout --- */
        .admin-container {
            display: flex;
            height: 100%;
        }

        /* --- Sidebar --- */
        .aside-menu {
            width: 240px;
            background-color: #304156;
            color: #bfcbd9;
            display: flex;
            flex-direction: column;
            flex-shrink: 0;
            transition: width 0.3s;
        }

        .logo-area {
            height: 60px;
            background-color: #2b2f3a;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: #fff;
            border-bottom: 1px solid #1f2d3d;
        }
        .logo-area h3 { margin: 0; font-size: 18px; font-weight: bold; }
        .logo-area p { margin: 5px 0 0 0; font-size: 12px; color: #909399; }

        .menu-scroll {
            flex: 1;
            overflow-y: auto;
        }

        /* Menu Items */
        .menu-category {
            padding: 10px 20px;
            font-size: 14px;
            color: #fff;
            background-color: #1f2d3d;
            font-weight: bold;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .menu-item {
            padding: 12px 20px 12px 40px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s, color 0.3s;
            display: block;
            text-decoration: none;
            color: #bfcbd9;
        }
        .menu-item:hover { background-color: #263445; }
        .menu-item.active {
            color: #409EFF;
            background-color: #1f2d3d;
            border-right: 3px solid #409EFF;
        }

        /* --- Main Content --- */
        .main-wrapper {
            flex: 1;
            display: flex;
            flex-direction: column;
            min-width: 0; /* 防止表格撑爆 flex */
        }

        .admin-header {
            height: 50px;
            background-color: #fff;
            border-bottom: 1px solid #dcdfe6;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            box-shadow: 0 1px 4px rgba(0,21,41,0.08);
        }
        .breadcrumb { font-weight: bold; color: #303133; font-size: 14px; }

        .admin-main {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }

        /* --- Search Box --- */
        .search-box {
            background: #fff;
            padding: 15px 15px 0 15px;
            margin-bottom: 15px;
            border-radius: 4px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
        }

        /* --- Table Area --- */
        .table-container {
            background: #fff;
            padding: 10px;
            border-radius: 4px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
            flex: 1;
            overflow: auto;
            position: relative;
        }

        /* Utilities */
        .d-none { display: none !important; }
        .img-thumb { width: 40px; height: 40px; object-fit: cover; border-radius: 4px; border: 1px solid #eee; }
        .text-ellipsis { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 150px; display: inline-block; vertical-align: middle; }
    </style>
</head>
<body>

<div class="admin-container">
    <!-- 侧边栏 -->
    <div class="aside-menu">
        <div class="logo-area">
            <h3>EBuyPlt 管理后台</h3>
            <p>当前管理员: <span id="displayAdminID">...</span></p>
        </div>
        <div class="menu-scroll">
            <!-- 1. 用户管理 -->
            <div class="menu-category"><i class="fa-solid fa-user"></i> 用户管理</div>
            <a class="menu-item active" onclick="switchTab('UserAccountTable', this)">用户账户信息</a>
            <a class="menu-item" onclick="switchTab('UserDeliveryInfoTable', this)">用户收货地址</a>
            <a class="menu-item" onclick="switchTab('UserShoppingCartTable', this)">用户购物车</a>
            <a class="menu-item" onclick="switchTab('UserFavoritesTable', this)">用户收藏夹</a>

            <!-- 2. 商品管理 -->
            <div class="menu-category"><i class="fa-solid fa-box"></i> 商品管理</div>
            <a class="menu-item" onclick="switchTab('ProductTable', this)">商品基础信息</a>
            <a class="menu-item" onclick="switchTab('MerchantsProductTable', this)">商户上架记录</a>
            <a class="menu-item" onclick="switchTab('ProductImagesTable', this)">商品图片记录</a>
            <a class="menu-item" onclick="switchTab('ProductClicksInfoTable', this)">商品点击统计</a>

            <!-- 3. 订单管理 -->
            <div class="menu-category"><i class="fa-solid fa-list-check"></i> 订单管理</div>
            <a class="menu-item" onclick="switchTab('OrderFullInfoTable', this)">订单完整详情</a>
            <a class="menu-item" onclick="switchTab('OrderProductInfoTable', this)">订单商品明细</a>
        </div>
    </div>

    <!-- 右侧内容 -->
    <div class="main-wrapper">
        <header class="admin-header">
            <div class="header-left">
                <span class="breadcrumb" id="breadcrumbText">当前位置：用户账户信息</span>
            </div>
            <div class="header-right">
                <button class="btn btn-sm btn-danger" onclick="handleLogout()">退出登录</button>
            </div>
        </header>

        <main class="admin-main">

            <!-- ================== 搜索栏 (User) ================== -->
            <div id="search-UserAccountTable" class="search-box">
                <form id="form-user-search" onsubmit="return false;">
                    <div class="row g-3 mb-3">
                        <div class="col-12">
                            <label class="form-label fw-bold">关键字搜索</label>
                            <input type="text" class="form-control" id="u_SearchInput" placeholder="请输入任意关键字进行匹配...">
                        </div>
                    </div>
                    <div class="row g-2 mb-3 align-items-center">
                        <div class="col-md-2"><input type="text" class="form-control" id="u_ID" placeholder="ID"></div>
                        <div class="col-md-2"><input type="text" class="form-control" id="u_NickName" placeholder="昵称"></div>
                        <div class="col-md-2"><input type="text" class="form-control" id="u_Phone" placeholder="电话"></div>
                        <div class="col-md-3"><input type="text" class="form-control" id="u_Email" placeholder="邮箱"></div>
                        <div class="col-md-2"><input type="text" class="form-control" id="u_Gender" placeholder="性别"></div>
                    </div>
                    <div class="row g-2 mb-3 align-items-end">
                        <div class="col-md-3">
                            <label class="small text-muted">用户类型</label>
                            <select class="form-select" id="u_AccountTypes" multiple>
                                <option value="普通用户" selected>普通用户</option>
                                <option value="商户" selected>商户</option>
                                <option value="管理员" selected>管理员</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="small text-muted">账号状态</label>
                            <select class="form-select" id="u_AccountStatuses" multiple>
                                <option value="正常" selected>正常</option>
                                <option value="封禁" selected>封禁</option>
                                <option value="注销" selected>注销</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="small text-muted">注册日期范围</label>
                            <div class="input-group">
                                <input type="date" class="form-control" id="u_DateStart">
                                <span class="input-group-text">至</span>
                                <input type="date" class="form-control" id="u_DateEnd">
                            </div>
                        </div>
                        <div class="col-md-2 text-end">
                            <button class="btn btn-primary" onclick="handleSearch()"><i class="fa-solid fa-magnifying-glass"></i> 搜索</button>
                            <button class="btn btn-secondary" onclick="resetSearch()">重置</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- ================== 搜索栏 (Product) ================== -->
            <div id="search-ProductTable" class="search-box d-none">
                <form id="form-product-search" onsubmit="return false;">
                    <div class="row mb-3">
                        <div class="col-12">
                            <label class="form-label fw-bold">商品关键字</label>
                            <input type="text" class="form-control" id="p_SearchDesciption" placeholder="请输入商品的关键字...">
                        </div>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-md-3"><input type="text" class="form-control" id="p_ID" placeholder="商品ID"></div>
                        <div class="col-md-3"><input type="text" class="form-control" id="p_Producer" placeholder="生产商"></div>
                        <div class="col-md-3">
                            <select class="form-select" id="p_Type">
                                <option value="">全部类型</option>
                                <!-- JS填充 -->
                            </select>
                        </div>
                        <div class="col-md-3"><input type="text" class="form-control" id="p_Info" placeholder="描述关键词"></div>
                    </div>
                    <div class="row g-2 mb-3 align-items-end">
                        <div class="col-md-4">
                            <label class="small text-muted">价格区间</label>
                            <div class="input-group">
                                <input type="number" class="form-control" id="p_Price_f" placeholder="Min">
                                <span class="input-group-text">-</span>
                                <input type="number" class="form-control" id="p_Price_r" placeholder="Max">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="small text-muted">发布日期</label>
                            <div class="input-group">
                                <input type="date" class="form-control" id="p_DateStart">
                                <span class="input-group-text">至</span>
                                <input type="date" class="form-control" id="p_DateEnd">
                            </div>
                        </div>
                        <div class="col-md-4 text-end">
                            <button class="btn btn-primary" onclick="handleSearch()"><i class="fa-solid fa-magnifying-glass"></i> 搜索</button>
                            <button class="btn btn-secondary" onclick="resetSearch()">重置</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- ================== 搜索栏 (Order) ================== -->
            <div id="search-OrderFullInfoTable" class="search-box d-none">
                <form id="form-order-search" onsubmit="return false;">
                    <div class="row g-2 align-items-end mb-3">
                        <div class="col-md-4">
                            <label class="form-label">综合搜索</label>
                            <input type="text" class="form-control" id="o_SearchInput" placeholder="订单号/收件人/电话">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">订单状态</label>
                            <select class="form-select" id="o_Statuses" multiple size="1">
                                <option value="待发货">待发货</option>
                                <option value="已发货">已发货</option>
                                <option value="已完成">已完成</option>
                                <option value="已取消">已取消</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">下单日期</label>
                            <div class="input-group">
                                <input type="date" class="form-control" id="o_DateStart">
                                <span class="input-group-text">至</span>
                                <input type="date" class="form-control" id="o_DateEnd">
                            </div>
                        </div>
                        <div class="col-md-2 text-end">
                            <button class="btn btn-primary" onclick="handleSearch()"><i class="fa-solid fa-magnifying-glass"></i> 搜索</button>
                            <button class="btn btn-secondary" onclick="resetSearch()">重置</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- ================== 数据表格区域 ================== -->
            <div class="table-container">
                <!-- Loading 遮罩 -->
                <div id="tableLoading" class="position-absolute top-0 start-0 w-100 h-100 bg-white d-flex justify-content-center align-items-center d-none" style="z-index: 10; opacity: 0.8;">
                    <div class="spinner-border text-primary" role="status"></div>
                </div>

                <!-- 动态表格 -->
                <div class="table-responsive" style="height: 100%;">
                    <table class="table table-bordered table-striped table-hover align-middle" id="dynamicTable">
                        <thead class="table-light sticky-top">
                        <!-- JS 生成表头 -->
                        </thead>
                        <tbody>
                        <!-- JS 生成内容 -->
                        </tbody>
                    </table>
                </div>
                <div id="emptyData" class="text-center py-5 d-none text-muted">暂无数据</div>
            </div>

        </main>
    </div>
</div>

<!-- 编辑用户信息的弹窗 -->
<div class="modal fade" id="editUserModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">修改用户信息</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="editUserForm">
                    <div class="mb-3"><label class="form-label">用户ID</label><input type="text" class="form-control" id="e_uID" disabled></div>
                    <div class="mb-3"><label class="form-label">注册时间</label><input type="text" class="form-control" id="e_uRegisterDate" disabled></div>
                    <div class="mb-3"><label class="form-label">昵称</label><input type="text" class="form-control" id="e_uNickName"></div>
                    <div class="mb-3"><label class="form-label">密码</label><input type="password" class="form-control" id="e_uPassword" placeholder="不修改请保留原值(Hash)"></div>
                    <div class="mb-3"><label class="form-label">电话</label><input type="text" class="form-control" id="e_uPhone"></div>
                    <div class="mb-3"><label class="form-label">邮箱</label><input type="email" class="form-control" id="e_uEmail"></div>
                    <div class="mb-3">
                        <label class="form-label">性别</label>
                        <select class="form-select" id="e_uGender">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">账户类型</label>
                        <select class="form-select" id="e_uAccountType">
                            <option value="普通用户">普通用户</option>
                            <option value="商户">商户</option>
                            <option value="管理员">管理员</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">账户状态</label>
                        <select class="form-select" id="e_uAccountStatus">
                            <option value="正常">正常</option>
                            <option value="封禁">封禁</option>
                            <option value="注销">注销</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="handleUpdateUser()">确认修改</button>
            </div>
        </div>
    </div>
</div>

<script>
    // ================= 配置 =================
    const BASE_URL = 'http://192.168.126.94:8082';
    let currentAdminID = '';
    let activeMenu = 'UserAccountTable';
    let categoryList = [];

    // ================= 初始化 =================
    $(document).ready(function() {
        const uID = sessionStorage.getItem('uID');
        if (!uID) {
            Swal.fire('错误', '请先登录', 'error').then(() => {
                window.location.href = 'index.jsp';
            });
            return;
        }
        currentAdminID = uID;
        $('#displayAdminID').text(uID);

        // 加载分类供搜索使用
        fetchCategories();
        // 初始加载第一个Tab的数据
        fetchData('UserAccountTable');
    });

    // ================= 导航逻辑 =================
    function switchTab(menuName, element) {
        // 1. 更新菜单样式
        $('.menu-item').removeClass('active');
        $(element).addClass('active');

        // 2. 更新状态
        activeMenu = menuName;
        $('#breadcrumbText').text('当前位置：' + $(element).text());

        // 3. 控制搜索栏显隐
        $('.search-box').addClass('d-none');
        if (['UserAccountTable', 'ProductTable', 'OrderFullInfoTable'].includes(menuName)) {
            $('#search-' + menuName).removeClass('d-none');
        }

        // 4. 重置搜索并加载数据
        resetSearch(false); // 重置搜索框但不自动请求，由fetchData请求
        fetchData(menuName);
    }

    function handleLogout() {
        sessionStorage.removeItem('uID');
        window.location.href = 'index.jsp';
    }

    function goToUserDetail(uID) {
        window.location.href = 'AdminUserDetailView.jsp?uID=' + uID;
    }
    function goToProductDetail(pID) {
        window.location.href = 'AdminProductDetailView.jsp?pID=' + pID;
    }
    function goToOrderDetail(oOrderID) {
        window.location.href = 'AdminOrderDetailView.jsp?oOrderID=' + oOrderID;
    }

    // ================= 核心数据加载 =================
    function fetchData(tableName) {
        $('#tableLoading').removeClass('d-none');
        $('#emptyData').addClass('d-none');
        $('#dynamicTable thead').empty();
        $('#dynamicTable tbody').empty();

        const url = BASE_URL + '/api/Admin' + tableName;

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({}),
            success: function(res) {
                const data = (res.data && res.data.data) ? res.data.data : [];
                renderTable(data, tableName);
            },
            error: function() {
                Swal.fire('错误', '加载数据失败', 'error');
            },
            complete: function() {
                $('#tableLoading').addClass('d-none');
            }
        });
    }

    function fetchCategories() {
        $.ajax({
            url: BASE_URL + '/api/GetAllProductType',
            type: 'POST',
            data: JSON.stringify({}),
            contentType: 'application/json',
            success: function(res) {
                if(res.data && res.data.data) {
                    categoryList = res.data.data;
                    const select = $('#p_Type');
                    categoryList.forEach(c => select.append('<option value="'+c+'">'+c+'</option>'));
                }
            }
        });
    }

    // ================= 搜索逻辑 =================
    function handleSearch() {
        $('#tableLoading').removeClass('d-none');
        $('#dynamicTable tbody').empty();

        let url = '';
        let payload = {};

        if (activeMenu === 'UserAccountTable') {
            url = BASE_URL + '/api/AdminUserAccountTableSearch';
            payload = {
                SearchInput: $('#u_SearchInput').val(),
                uID: $('#u_ID').val(),
                uNickName: $('#u_NickName').val(),
                uPhone: $('#u_Phone').val(),
                uEmail: $('#u_Email').val(),
                uGender: $('#u_Gender').val(),
                uAccountTypes: $('#u_AccountTypes').val(),
                uAccountStatuses: $('#u_AccountStatuses').val(),
                DateL: $('#u_DateStart').val() || '',
                DateR: $('#u_DateEnd').val() || ''
            };
        } else if (activeMenu === 'ProductTable') {
            url = BASE_URL + '/api/AdminProductTableSearch';
            payload = {
                SearchDesciption: $('#p_SearchDesciption').val(),
                pID: $('#p_ID').val(),
                pProducer: $('#p_Producer').val(),
                pType: $('#p_Type').val(),
                pInfo: $('#p_Info').val(),
                pPrice_f: parseFloat($('#p_Price_f').val()) || 0,
                pPrice_r: parseFloat($('#p_Price_r').val()) || 0,
                pReleaseDate_f: $('#p_DateStart').val() || '',
                pReleaseDate_r: $('#p_DateEnd').val() || ''
            };
        } else if (activeMenu === 'OrderFullInfoTable') {
            url = BASE_URL + '/api/AdminOrderInfoSearch';
            payload = {
                SearchInput: $('#o_SearchInput').val(),
                oStatuses: $('#o_Statuses').val(),
                DateF: $('#o_DateStart').val() || '',
                DateR: $('#o_DateEnd').val() || ''
            };
        } else {
            // 没有搜索功能的页面直接刷新
            fetchData(activeMenu);
            return;
        }

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                const data = (res.data && res.data.data) ? res.data.data : [];
                renderTable(data, activeMenu);
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: '搜索完成', showConfirmButton: false, timer: 1500
                });
            },
            complete: function() { $('#tableLoading').addClass('d-none'); }
        });
    }

    function resetSearch(shouldFetch = true) {
        // 重置所有搜索表单
        $('#form-user-search')[0].reset();
        $('#u_AccountTypes').val(['普通用户', '商户', '管理员']);
        $('#u_AccountStatuses').val(['正常', '封禁', '注销']);

        $('#form-product-search')[0].reset();

        $('#form-order-search')[0].reset();
        $('#o_Statuses').val([]);

        if (shouldFetch) {
            fetchData(activeMenu);
        }
    }

    // ================= 表格渲染核心 =================
    function renderTable(data, tabName) {
        const thead = $('#dynamicTable thead');
        const tbody = $('#dynamicTable tbody');
        thead.empty();
        tbody.empty();

        if (!data || data.length === 0) {
            $('#emptyData').removeClass('d-none');
            return;
        } else {
            $('#emptyData').addClass('d-none');
        }

        let headerHtml = '<tr>';
        let rowBuilder = null;

        // 根据 tabName 定义表头和行构建器
        switch(tabName) {
            case 'UserAccountTable':
                headerHtml += '<th>ID</th><th>昵称</th><th>类型</th><th>状态</th><th>电话</th><th>邮箱</th><th>性别</th><th>注册日期</th><th>操作</th>';
                rowBuilder = (row) => `
                    <td>${row.uID}</td>
                    <td>${row.uNickName}</td>
                    <td><span class="badge ${row.uAccountType === '管理员' ? 'bg-danger' : 'bg-success'}">${row.uAccountType}</span></td>
                    <td><span class="badge ${row.uAccountStatus === '正常' ? 'bg-primary' : 'bg-secondary'}">${row.uAccountStatus}</span></td>
                    <td>${row.uPhone}</td>
                    <td>${row.uEmail}</td>
                    <td>${row.uGender}</td>
                    <td>${row.uRegisterDate}</td>
                    <td>
                        <button class="btn btn-sm btn-link text-primary p-0 me-2" onclick='openEditUserModal(${JSON.stringify(row)})'>编辑</button>
                        <button class="btn btn-sm btn-link text-success p-0" onclick="goToUserDetail('${row.uID}')">详情</button>
                    </td>`;
                break;

            case 'UserDeliveryInfoTable':
                headerHtml += '<th>#</th><th>用户ID</th><th>收货人</th><th>电话</th><th>收货地址</th><th>邮编</th><th>备注</th>';
                rowBuilder = (row, idx) => `
                    <td>${idx + 1}</td>
                    <td>${row.uID}</td>
                    <td>${row.uContactPersonName}</td>
                    <td>${row.uContactPersonPhone}</td>
                    <td>${row.uDeliveryAddress}</td>
                    <td>${row.oPostalCode}</td>
                    <td>${row.oDeliveryNote || ''}</td>`;
                break;

            case 'UserShoppingCartTable':
                headerHtml += '<th>用户ID</th><th>商品ID</th><th>数量</th>';
                rowBuilder = (row) => `<td>${row.uID}</td><td>${row.pID}</td><td>${row.cAmount}</td>`;
                break;

            case 'UserFavoritesTable':
                headerHtml += '<th>用户ID</th><th>商品ID</th>';
                rowBuilder = (row) => `<td>${row.uID}</td><td>${row.pID}</td>`;
                break;

            case 'ProductTable':
                headerHtml += '<th>ID</th><th>缩略图</th><th>名称</th><th>类型</th><th>价格</th><th>库存</th><th>生产商</th><th>状态</th><th>日期</th><th>简介</th><th>操作</th>';
                rowBuilder = (row) => `
                    <td>${row.pID}</td>
                    <td><img src="${getImageUrl(row.pImagePath)}" class="img-thumb"></td>
                    <td><span class="text-ellipsis" title="${row.pName}">${row.pName}</span></td>
                    <td>${row.pType}</td>
                    <td>${row.pPrice}</td>
                    <td>${row.pInventory}</td>
                    <td>${row.pProducer}</td>
                    <td>${row.pStatus}</td>
                    <td>${row.pReleaseDate}</td>
                    <td><span class="text-ellipsis" title="${row.pInfo}">${row.pInfo || ''}</span></td>
                    <td><button class="btn btn-sm btn-link text-success p-0" onclick="goToProductDetail('${row.pID}')">详情</button></td>`;
                break;

            case 'MerchantsProductTable':
                headerHtml += '<th>商户ID</th><th>商品ID</th>';
                rowBuilder = (row) => `<td>${row.uID}</td><td>${row.pID}</td>`;
                break;

            case 'ProductImagesTable':
                headerHtml += '<th>商品ID</th><th>类型</th><th>预览</th><th>路径</th>';
                rowBuilder = (row) => `
                    <td>${row.pID}</td>
                    <td>${row.pType}</td>
                    <td><img src="${getImageUrl(row.pImagePath)}" class="img-thumb"></td>
                    <td>${row.pImagePath}</td>`;
                break;

            case 'ProductClicksInfoTable':
                headerHtml += '<th>商品ID</th><th>点击量</th>';
                rowBuilder = (row) => `<td>${row.pID}</td><td>${row.pClicksAmount}</td>`;
                break;

            case 'OrderFullInfoTable':
                headerHtml += '<th>订单号</th><th>时间</th><th>状态</th><th>收件人</th><th>地址</th><th>电话</th><th>备注</th><th>操作</th>';
                rowBuilder = (row) => `
                    <td>${row.oOrderID}</td>
                    <td>${row.oDate}</td>
                    <td><span class="badge bg-secondary">${row.oStatus}</span></td>
                    <td>${row.oReceiverName}</td>
                    <td><span class="text-ellipsis" title="${row.oDeliveryAddress}">${row.oDeliveryAddress}</span></td>
                    <td>${row.oContactPhone}</td>
                    <td>${row.oDeliveryNote || ''}</td>
                    <td><button class="btn btn-sm btn-link text-success p-0" onclick="goToOrderDetail('${row.oOrderID}')">详情</button></td>`;
                break;

            case 'OrderProductInfoTable':
                headerHtml += '<th>订单号</th><th>商品ID</th><th>成交单价</th><th>数量</th><th>发货状态</th>';
                rowBuilder = (row) => `
                    <td>${row.oOrderID}</td>
                    <td>${row.pID}</td>
                    <td>${row.oPrice}</td>
                    <td>${row.oAmount}</td>
                    <td>${row.oProductDeliveryStatus}</td>`;
                break;
        }

        headerHtml += '</tr>';
        thead.html(headerHtml);

        data.forEach((row, index) => {
            const tr = $('<tr>').html(rowBuilder(row, index));
            tbody.append(tr);
        });
    }

    // ================= 用户编辑逻辑 =================
    function openEditUserModal(row) {
        $('#e_uID').val(row.uID);
        $('#e_uRegisterDate').val(row.uRegisterDate);
        $('#e_uNickName').val(row.uNickName);
        $('#e_uPassword').val(row.uPassword); // Hash
        $('#e_uPhone').val(row.uPhone);
        $('#e_uEmail').val(row.uEmail);
        $('#e_uGender').val(row.uGender);
        $('#e_uAccountType').val(row.uAccountType);
        $('#e_uAccountStatus').val(row.uAccountStatus);

        const modal = new bootstrap.Modal(document.getElementById('editUserModal'));
        modal.show();
    }

    function handleUpdateUser() {
        const payload = {
            uID: $('#e_uID').val(),
            uNickName: $('#e_uNickName').val(),
            uPassword: $('#e_uPassword').val(),
            uPhone: $('#e_uPhone').val(),
            uEmail: $('#e_uEmail').val(),
            uGender: $('#e_uGender').val(),
            uRegisterDate: $('#e_uRegisterDate').val(),
            uAccountType: $('#e_uAccountType').val(),
            uAccountStatus: $('#e_uAccountStatus').val()
        };

        $.ajax({
            url: BASE_URL + '/api/AdminUserAccountTableUpdate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                if (res === 'success') {
                    Swal.fire('成功', '更新成功', 'success');
                    bootstrap.Modal.getInstance(document.getElementById('editUserModal')).hide();
                    fetchData('UserAccountTable');
                } else {
                    Swal.fire('失败', res, 'error');
                }
            }
        });
    }

    // ================= 通用函数 =================
    function getImageUrl(path) {
        if (!path) return '';
        let clean = path.replace(/\\/g, '/');
        if (!clean.startsWith('/')) clean = '/' + clean;
        return BASE_URL + clean;
    }
</script>

</body>
</html>