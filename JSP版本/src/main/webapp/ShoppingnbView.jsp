<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EBuyPlt - 商城首页</title>

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

    <style>
        body {
            background-color: #f5f7fa;
            font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
            padding-top: 60px;
        }

        /* --- Header --- */
        .app-header {
            background-color: #fff;
            height: 60px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1020;
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
            text-decoration: none;
        }

        .search-bar { width: 30%; max-width: 400px; position: relative; }
        .search-icon { position: absolute; left: 10px; top: 10px; color: #a8abb2; }
        .search-input { padding-left: 35px; border-radius: 20px; }

        .action-btn {
            border: none;
            background: none;
            font-size: 20px;
            color: #606266;
            margin-left: 10px;
            position: relative;
            cursor: pointer;
            transition: color 0.3s;
        }
        .action-btn:hover { color: #409EFF; }
        .badge-count {
            position: absolute; top: -5px; right: -8px; font-size: 10px; padding: 3px 6px;
        }

        /* --- Filter Panel --- */
        .filter-panel {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
            display: none; /* Default hidden */
        }
        .form-label { font-size: 14px; color: #606266; }

        /* --- Category Tabs --- */
        .category-bar {
            background: #fff;
            padding: 0 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            overflow-x: auto;
        }
        .nav-tabs { border-bottom: none; }
        .nav-link {
            border: none;
            color: #303133;
            padding: 15px 20px;
            font-size: 15px;
            cursor: pointer;
        }
        .nav-link.active {
            color: #409EFF;
            border-bottom: 2px solid #409EFF;
            background: none;
            font-weight: bold;
        }
        .nav-link:hover { color: #409EFF; }

        /* --- Product Grid --- */
        .product-card {
            background: #fff;
            border-radius: 8px;
            border: none;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            transition: transform 0.3s, box-shadow 0.3s;
            margin-bottom: 20px;
            overflow: hidden;
            position: relative;
        }
        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 16px rgba(0,0,0,0.1);
        }

        .image-wrapper {
            height: 200px;
            position: relative;
            background: #f9f9f9;
            cursor: pointer;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .product-image { width: 100%; height: 100%; object-fit: cover; }

        .category-tag {
            position: absolute; top: 10px; left: 10px;
            background: rgba(0,0,0,0.6); color: #fff;
            padding: 2px 8px; border-radius: 4px; font-size: 12px;
        }
        .discount-badge {
            position: absolute; top: 10px; right: 10px;
            background: #f56c6c; color: #fff;
            padding: 2px 8px; border-radius: 10px; font-size: 12px;
        }
        .out-of-stock-mask {
            position: absolute; top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(255, 255, 255, 0.8);
            display: flex; align-items: center; justify-content: center;
            font-size: 20px; font-weight: bold; color: #909399; z-index: 2;
        }
        .fav-icon-wrapper {
            position: absolute; bottom: 10px; right: 10px;
            background: rgba(255,255,255,0.9); border-radius: 50%;
            width: 32px; height: 32px;
            display: flex; align-items: center; justify-content: center;
            cursor: pointer; z-index: 3;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .card-body { padding: 14px; }
        .product-title {
            font-size: 16px; font-weight: bold; color: #303133;
            margin-bottom: 8px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
            cursor: pointer;
        }
        .product-title:hover { color: #409EFF; }

        .simple-info { font-size: 12px; color: #999; margin-bottom: 10px; display: flex; align-items: center; gap: 5px; }
        .status-dot { width: 8px; height: 8px; border-radius: 50%; display: inline-block; }
        .status-dot.online { background-color: #67C23A; }
        .status-dot.offline { background-color: #909399; }

        .card-footer-custom {
            display: flex; justify-content: space-between; align-items: center;
        }
        .price { color: #f56c6c; font-size: 18px; font-weight: bold; }

        /* --- Footer --- */
        .footer { text-align: center; padding: 20px; color: #909399; font-size: 14px; }

        /* --- Drawer Items --- */
        .drawer-item { display: flex; align-items: center; padding: 15px 0; border-bottom: 1px solid #f2f2f2; }
        .item-img { width: 60px; height: 60px; border-radius: 4px; object-fit: cover; margin: 0 10px; cursor: pointer; border: 1px solid #eee;}
        .item-info { flex: 1; overflow: hidden; }
        .item-title { font-size: 14px; font-weight: bold; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; cursor: pointer; }

        /* Address Card */
        .address-card {
            border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px; margin-bottom: 10px; cursor: pointer; transition: 0.2s;
        }
        .address-card:hover, .address-card.active { border-color: #409EFF; background-color: #ecf5ff; }

        /* Utilities */
        .d-none { display: none !important; }
        .main-container { max-width: 1200px; margin: 20px auto; padding: 0 20px; min-height: calc(100vh - 140px); }
    </style>
</head>
<body>

<!-- Header -->
<header class="app-header">
    <div class="header-content">
        <a href="#" onclick="resetFilters(); return false;" class="logo">EBuyPlt</a>

        <div class="search-bar">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" class="form-control search-input" id="searchInput" placeholder="搜索商品名称/描述..." onkeypress="handleSearchEnter(event)">
        </div>

        <div class="d-flex align-items-center">
            <button class="btn btn-outline-primary btn-sm me-3" onclick="toggleFilterPanel()">
                <i class="fa-solid fa-filter"></i> <span id="filterBtnText">高级筛选</span>
            </button>

            <button class="action-btn" onclick="window.location.href='OrderListView.jsp'" title="我的订单">
                <i class="fa-solid fa-receipt"></i>
            </button>

            <button class="action-btn" onclick="openFavorites()" title="我的收藏">
                <i class="fa-regular fa-star"></i>
            </button>

            <button class="action-btn" onclick="openCart()" title="我的购物车">
                <i class="fa-solid fa-cart-shopping"></i>
                <span class="badge rounded-pill bg-danger badge-count d-none" id="headerCartCount">0</span>
            </button>

            <!-- User Dropdown -->
            <div class="dropdown ms-3">
                <a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle" id="userDropdown" data-bs-toggle="dropdown">
                    <div class="rounded-circle bg-light d-flex justify-content-center align-items-center text-secondary" style="width: 32px; height: 32px; border: 1px solid #dcdfe6;">
                        <i class="fa-solid fa-user"></i>
                    </div>
                </a>
                <ul class="dropdown-menu dropdown-menu-end shadow">
                    <li><span class="dropdown-item-text text-muted" id="menuUserID">ID: Loading...</span></li>
                    <li><a class="dropdown-item" href="#" onclick="goToUserProfile()">个人信息</a></li>
                    <li><a class="dropdown-item" href="OrderListView.jsp">我的订单</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item text-danger" href="#" onclick="handleLogout()">退出登录</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>

<!-- Main Content -->
<main class="main-container">
    <!-- Filter Panel -->
    <div class="filter-panel" id="filterPanel">
        <form id="filterForm">
            <div class="row g-3">
                <div class="col-md-3 col-sm-6">
                    <label class="form-label">商品ID</label>
                    <input type="text" class="form-control" id="f_pID" placeholder="输入ID">
                </div>
                <div class="col-md-3 col-sm-6">
                    <label class="form-label">生产厂商</label>
                    <input type="text" class="form-control" id="f_pProducer" placeholder="输入厂商名">
                </div>
                <div class="col-md-3 col-sm-6">
                    <label class="form-label">商品类型</label>
                    <select class="form-select" id="f_pType">
                        <option value="">全部</option>
                        <!-- JS populated -->
                    </select>
                </div>
                <div class="col-md-3 col-sm-6">
                    <label class="form-label">详情描述</label>
                    <input type="text" class="form-control" id="f_pInfo" placeholder="描述关键词">
                </div>
                <div class="col-md-5 col-sm-12">
                    <label class="form-label">价格区间</label>
                    <div class="input-group">
                        <input type="number" class="form-control" id="f_minPrice" placeholder="Min">
                        <span class="input-group-text">-</span>
                        <input type="number" class="form-control" id="f_maxPrice" placeholder="Max">
                    </div>
                </div>
                <div class="col-md-7 col-sm-12">
                    <label class="form-label">发布日期</label>
                    <div class="input-group">
                        <input type="date" class="form-control" id="f_startDate">
                        <span class="input-group-text">至</span>
                        <input type="date" class="form-control" id="f_endDate">
                    </div>
                </div>
            </div>
            <div class="d-flex justify-content-end mt-3">
                <button type="button" class="btn btn-secondary me-2" onclick="resetFilters()">重置</button>
                <button type="button" class="btn btn-primary" onclick="applyAdvancedFilter()">应用筛选</button>
            </div>
        </form>
    </div>

    <!-- Category Tabs -->
    <div class="category-bar">
        <ul class="nav nav-tabs" id="categoryTabs">
            <li class="nav-item">
                <a class="nav-link active" onclick="handleCategoryChange('推荐', this)">推荐</a>
            </li>
            <!-- JS Populated categories -->
        </ul>
    </div>

    <!-- Products -->
    <div class="d-flex justify-content-between align-items-end mb-3">
        <h2 class="m-0" id="sectionTitle">为您推荐</h2>
        <span class="text-muted small">共 <span id="productCount">0</span> 件商品</span>
    </div>

    <div id="productLoading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="mt-2 text-muted">正在加载商品...</p>
    </div>

    <div class="row" id="productGrid">
        <!-- Products injected here -->
    </div>

    <div id="productEmpty" class="text-center py-5 d-none">
        <i class="fa-solid fa-box-open fa-3x text-muted mb-3"></i>
        <p class="text-muted">没有找到商品</p>
        <button class="btn btn-primary" onclick="resetFilters()">重置条件</button>
    </div>
</main>

<footer class="footer">
    <p>© 2023 EBuyPlt. Built with Bootstrap 5.</p>
</footer>

<!-- Cart Drawer -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="cartDrawer" style="width: 480px;">
    <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title">我的购物车</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
    </div>
    <div class="offcanvas-body p-0 d-flex flex-column">
        <div class="p-3 border-bottom bg-light d-flex justify-content-between">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="checkAllCart" onchange="toggleSelectAllCart(this)">
                <label class="form-check-label" for="checkAllCart">全选</label>
            </div>
            <a href="OrderListView.jsp" class="text-decoration-none">我的订单 ></a>
        </div>
        <div id="cartItemsList" class="flex-grow-1 overflow-auto p-3">
            <!-- JS Items -->
        </div>
        <div id="cartEmpty" class="text-center py-5 d-none">
            <p class="text-muted">购物车是空的</p>
            <a href="OrderListView.jsp" class="btn btn-sm btn-outline-primary">查看历史订单</a>
        </div>
        <div class="border-top p-3">
            <div class="d-flex justify-content-between mb-3 fs-5">
                <span>已选 (<span id="selCount">0</span>):</span>
                <span class="text-danger fw-bold">¥ <span id="selTotal">0.00</span></span>
            </div>
            <button class="btn btn-primary w-100 btn-lg" id="btnCheckout" onclick="handleCheckout()" disabled>结算</button>
        </div>
    </div>
</div>

<!-- Favorites Drawer -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="favDrawer" style="width: 450px;">
    <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title">我的收藏</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
    </div>
    <div class="offcanvas-body p-0">
        <div id="favListContainer" class="p-3"></div>
        <div id="favEmpty" class="text-center py-5 d-none">暂无收藏</div>
    </div>
</div>

<!-- Checkout Modal -->
<div class="modal fade" id="checkoutModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">确认收货信息</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <!-- Address List -->
                <div id="addressSelectionArea">
                    <p class="fw-bold mb-2">请选择收货地址：</p>
                    <div id="existingAddressesList" style="max-height: 250px; overflow-y: auto;">
                        <!-- Addresses -->
                    </div>
                    <button class="btn btn-link p-0 mt-2" onclick="showNewAddressForm()">使用新地址 +</button>
                </div>

                <!-- New Address Form -->
                <div id="newAddressFormArea" class="d-none">
                    <p class="fw-bold mb-2" id="addrFormTitle">新增收货地址：</p>
                    <form id="newAddrForm">
                        <div class="mb-2"><label class="form-label">收货人</label><input type="text" class="form-control" id="naName"></div>
                        <div class="mb-2"><label class="form-label">电话</label><input type="text" class="form-control" id="naPhone" maxlength="11"></div>
                        <div class="mb-2"><label class="form-label">性别</label>
                            <select class="form-select" id="naGender"><option value="男">男</option><option value="女">女</option></select>
                        </div>
                        <div class="mb-2"><label class="form-label">邮箱</label><input type="text" class="form-control" id="naEmail"></div>
                        <div class="mb-2"><label class="form-label">地址</label><textarea class="form-control" id="naAddr"></textarea></div>
                        <div class="mb-2"><label class="form-label">邮编</label><input type="text" class="form-control" id="naZip"></div>
                        <div class="mb-2"><label class="form-label">备注</label><input type="text" class="form-control" id="naNote"></div>
                    </form>
                    <button class="btn btn-link ps-0" onclick="hideNewAddressForm()">返回选择列表</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btnConfirmOrder" onclick="confirmOrderGeneration()">确认订单</button>
            </div>
        </div>
    </div>
</div>

<script>
    // ================= 配置 =================
    const BASE_URL = 'http://192.168.126.94:8082';

    // 全局变量
    let currentUserID = '';
    let productList = [];
    let cartList = [];
    let favList = [];
    let categoryList = [];
    let isRecommendMode = true;

    // 结算相关
    let existingAddresses = [];
    let selectedAddressIndex = 0;
    let isAddingNewAddress = false;

    // ================= 初始化 =================
    $(document).ready(function() {
        const uID = sessionStorage.getItem('uID');
        if (!uID) {
            Swal.fire('未登录', '请先登录', 'error').then(() => {
                window.location.href = 'index.jsp';
            });
            return;
        }
        currentUserID = uID;
        $('#menuUserID').text('用户ID: ' + uID);

        // 加载初始数据
        fetchCategories();

        // 检查 URL 参数是否有搜索词
        const urlParams = new URLSearchParams(window.location.search);
        const q = urlParams.get('q');
        if(q) {
            $('#searchInput').val(q);
            handleSearch();
        } else {
            handleRecommend();
        }

        fetchCartCountOnly();
        fetchFavorites();
    });

    // ================= 导航与通用 =================
    function goToUserProfile() { window.location.href = 'UserProfileView.jsp?uID=' + currentUserID; }
    function handleLogout() { sessionStorage.removeItem('uID'); window.location.href = 'index.jsp'; }
    function getImageUrl(path) {
        if (!path) return 'https://via.placeholder.com/300x300?text=No+Image';
        let cleanPath = path.replace(/\\/g, '/');
        if (cleanPath.startsWith('http')) return cleanPath;
        if (!cleanPath.startsWith('/')) cleanPath = '/' + cleanPath;
        return BASE_URL + cleanPath;
    }
    function goToDetail(pID) { window.location.href = 'ProductDetailView.jsp?pID=' + pID; }

    // ================= 1. 筛选与分类 =================
    function fetchCategories() {
        $.ajax({
            url: BASE_URL + '/api/GetAllProductType',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({}),
            success: function(res) {
                if (res.data && res.data.data) {
                    categoryList = res.data.data;
                    renderCategories();
                }
            }
        });
    }

    function renderCategories() {
        // 渲染 Tab
        const tabContainer = $('#categoryTabs');
        const selectContainer = $('#f_pType');

        categoryList.forEach(cat => {
            // Tab
            tabContainer.append(`
                <li class="nav-item">
                    <a class="nav-link" onclick="handleCategoryChange('${cat}', this)">${cat}</a>
                </li>
            `);
            // Filter Select
            selectContainer.append(`<option value="${cat}">${cat}</option>`);
        });
    }

    function toggleFilterPanel() {
        $('#filterPanel').slideToggle();
        const btnText = $('#filterBtnText');
        btnText.text(btnText.text() === '高级筛选' ? '收起筛选' : '高级筛选');
    }

    // ================= 2. 核心查询逻辑 =================
    function handleSearchEnter(e) { if(e.key === 'Enter') handleSearch(); }

    function handleSearch() {
        isRecommendMode = false;
        $('#sectionTitle').text('商品列表');
        $('#categoryTabs .nav-link').removeClass('active'); // 清除 Tab 选中态

        const query = $('#searchInput').val().trim();
        const payload = {
            SearchDesciption: query,
            pID: $('#f_pID').val(),
            pProducer: $('#f_pProducer').val(),
            pType: $('#f_pType').val(),
            pInfo: $('#f_pInfo').val(),
            pPrice_f: parseFloat($('#f_minPrice').val()) || 0,
            pPrice_r: parseFloat($('#f_maxPrice').val()) || 0,
            pReleaseDate_f: $('#f_startDate').val() || "",
            pReleaseDate_r: $('#f_endDate').val() || ""
        };

        fetchProducts('/api/ProductSearch', payload);
    }

    function handleRecommend() {
        isRecommendMode = true;
        $('#sectionTitle').text('为您推荐');
        $('#searchInput').val('');
        // 清空筛选
        $('#filterForm')[0].reset();

        fetchProducts('/api/ProductRecommend', { uID: currentUserID });
    }

    function handleCategoryChange(catName, el) {
        // 更新 Tab 样式
        $('#categoryTabs .nav-link').removeClass('active');
        $(el).addClass('active');

        if (catName === '推荐') {
            handleRecommend();
        } else {
            // 设置类型并清空其他筛选
            $('#filterForm')[0].reset();
            $('#f_pType').val(catName);
            handleSearch();
        }
    }

    function resetFilters() {
        $('#filterForm')[0].reset();
        $('#searchInput').val('');
        $('#categoryTabs .nav-link').removeClass('active');
        $('#categoryTabs .nav-link:first').addClass('active');
        handleRecommend();
    }

    function applyAdvancedFilter() {
        handleSearch();
    }

    function fetchProducts(url, payload) {
        $('#productLoading').removeClass('d-none');
        $('#productGrid, #productEmpty').addClass('d-none');

        $.ajax({
            url: BASE_URL + url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                productList = (res.data && res.data.data) ? res.data.data : [];
                renderProducts();
            },
            complete: function() { $('#productLoading').addClass('d-none'); }
        });
    }

    function renderProducts() {
        const container = $('#productGrid');
        container.empty();
        $('#productCount').text(productList.length);

        if (productList.length === 0) {
            $('#productEmpty').removeClass('d-none');
            return;
        }

        productList.forEach(p => {
            const isFav = isFavorite(p.pID);
            const favColor = isFav ? '#E6A23C' : '#909399';
            const favIconClass = isFav ? 'fa-solid' : 'fa-regular';
            const hasStock = p.pInventory > 0;
            const stockMask = !hasStock ? '<div class="out-of-stock-mask">缺货</div>' : '';
            const discountBadge = (p.pDiscount && p.pDiscount !== '无') ? `<div class="discount-badge">Discount: ${p.pDiscount}</div>` : '';
            const statusClass = (p.pStatus === '正常' || p.pStatus === '上架') ? 'online' : 'offline';
            const btnDisabled = !hasStock ? 'disabled' : '';

            const html = `
                <div class="col-lg-3 col-md-4 col-sm-6">
                    <div class="product-card">
                        <div class="image-wrapper" onclick="goToDetail('${p.pID}')">
                            <img src="${getImageUrl(p.pImagePath)}" class="product-image">
                            <span class="category-tag">${p.pType}</span>
                            ${discountBadge}
                            ${stockMask}
                            <div class="fav-icon-wrapper" onclick="event.stopPropagation(); toggleFavorite('${p.pID}')">
                                <i class="${favIconClass} fa-star" style="color: ${favColor}"></i>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="product-title" onclick="goToDetail('${p.pID}')" title="${p.pName}">${p.pName}</div>
                            <div class="simple-info">
                                <span class="status-dot ${statusClass}"></span> ${p.pStatus}
                                <span class="ms-1">| ${p.pProducer}</span>
                            </div>
                            <div class="card-footer-custom">
                                <span class="price">¥ ${p.pPrice}</span>
                                <button class="btn btn-sm btn-primary" ${btnDisabled} onclick="addToCart('${p.pID}', 1)">加入购物车</button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            container.append(html);
        });
        $('#productGrid').removeClass('d-none');
    }

    // ================= 3. 购物车逻辑 =================
    function fetchCartCountOnly() {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                const list = (res.data && res.data) ? res.data : [];
                const count = list.reduce((sum, item) => sum + item.cAmount, 0);
                const badge = $('#headerCartCount');
                badge.text(count);
                if (count > 0) badge.removeClass('d-none'); else badge.addClass('d-none');
            }
        });
    }

    function openCart() {
        const drawer = new bootstrap.Offcanvas(document.getElementById('cartDrawer'));
        drawer.show();
        fetchCartDetails();
    }

    function fetchCartDetails() {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                const serverList = (res.data && res.data) ? res.data : [];
                cartList = serverList.map(item => ({...item, isSelected: false}));
                renderCartDrawer();
            }
        });
    }

    function renderCartDrawer() {
        const container = $('#cartItemsList');
        container.empty();

        if (cartList.length === 0) {
            $('#cartEmpty').removeClass('d-none');
        } else {
            $('#cartEmpty').addClass('d-none');
            cartList.forEach((item, index) => {
                const html = `
                    <div class="drawer-item">
                        <input class="form-check-input me-2 cart-check" type="checkbox"
                               onchange="updateCartSelection(${index}, this.checked)" ${item.isSelected ? 'checked' : ''}>
                        <img src="${getImageUrl(item.pImagesPath)}" class="item-img" onclick="goToDetail('${item.pID}')">
                        <div class="item-info">
                            <div class="item-title" onclick="goToDetail('${item.pID}')">${item.pName}</div>
                            <div class="d-flex justify-content-between align-items-center mt-1">
                                <span class="price fs-6">¥ ${item.pPrice}</span>
                                <div class="input-group input-group-sm" style="width: 80px;">
                                    <button class="btn btn-outline-secondary px-1" onclick="updateCartItemQty('${item.pID}', -1)">-</button>
                                    <span class="form-control text-center px-0">${item.cAmount}</span>
                                    <button class="btn btn-outline-secondary px-1" onclick="updateCartItemQty('${item.pID}', 1)">+</button>
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-link text-danger ms-2" onclick="removeCartItem('${item.pID}', ${item.cAmount})">
                            <i class="fa-regular fa-trash-can"></i>
                        </button>
                    </div>
                `;
                container.append(html);
            });
        }
        updateCartTotalUI();
    }

    function addToCart(pid, qty) {
        // Find product to check inventory
        const p = productList.find(x => x.pID == pid);
        if(p && p.pInventory <= 0) return;

        $.ajax({
            url: BASE_URL + '/api/ShoppingCartAdd',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pid, cAmount: qty }),
            success: function() {
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: '已加入购物车', showConfirmButton: false, timer: 1500
                });
                fetchCartCountOnly();
            }
        });
    }

    function updateCartItemQty(pid, delta) {
        const endpoint = delta > 0 ? '/api/ShoppingCartAdd' : '/api/ShoppingCartRemove';
        $.ajax({
            url: BASE_URL + endpoint,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pid, cAmount: 1 }),
            success: function() { fetchCartDetails(); fetchCartCountOnly(); }
        });
    }

    function removeCartItem(pid, amount) {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartRemove',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pid, cAmount: amount }),
            success: function() { fetchCartDetails(); fetchCartCountOnly(); }
        });
    }

    function toggleSelectAllCart(el) {
        const val = el.checked;
        cartList.forEach(item => item.isSelected = val);
        renderCartDrawer();
    }

    function updateCartSelection(index, checked) {
        cartList[index].isSelected = checked;
        updateCartTotalUI();
    }

    function updateCartTotalUI() {
        const selected = cartList.filter(i => i.isSelected);
        const total = selected.reduce((sum, item) => sum + (item.pPrice * item.cAmount), 0);
        $('#selCount').text(selected.length);
        $('#selTotal').text(total.toFixed(2));
        $('#btnCheckout').prop('disabled', selected.length === 0);
        $('#checkAllCart').prop('checked', cartList.length > 0 && selected.length === cartList.length);
    }

    // ================= 4. 收藏夹逻辑 =================
    function fetchFavorites() {
        return new Promise((resolve) => {
            $.ajax({
                url: BASE_URL + '/api/FavouriteRecords',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ uID: currentUserID }),
                success: function(res) {
                    favList = (res.data && res.data.data) ? res.data.data : [];
                    renderProducts(); // Refresh heart icons
                    resolve();
                }
            });
        });
    }

    function isFavorite(id) { return favList.some(i => String(i.pID) === String(id)); }

    function toggleFavorite(pid) {
        const exist = isFavorite(pid);
        const endpoint = exist ? '/api/FavouriteRemove' : '/api/FavouriteAdd';

        $.ajax({
            url: BASE_URL + endpoint,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pid }),
            success: function() {
                const msg = exist ? '已取消收藏' : '已加入收藏';
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: msg, showConfirmButton: false, timer: 1500
                });
                fetchFavorites().then(() => {
                    // Update UI immediately if drawer is open
                    const drawer = document.getElementById('favDrawer');
                    if(drawer.classList.contains('show')) renderFavDrawer();
                });
            }
        });
    }

    function openFavorites() {
        const drawer = new bootstrap.Offcanvas(document.getElementById('favDrawer'));
        drawer.show();
        renderFavDrawer();
    }

    function renderFavDrawer() {
        const container = $('#favListContainer');
        container.empty();
        if (favList.length === 0) {
            $('#favEmpty').removeClass('d-none');
        } else {
            $('#favEmpty').addClass('d-none');
            favList.forEach(item => {
                const html = `
                    <div class="drawer-item">
                        <img src="${getImageUrl(item.pImagesPath)}" class="item-img" onclick="goToDetail('${item.pID}')">
                        <div class="item-info">
                            <div class="item-title" onclick="goToDetail('${item.pID}')">${item.pName}</div>
                            <div class="price fs-6">¥ ${item.pPrice}</div>
                        </div>
                        <button class="btn btn-sm btn-primary rounded-circle" onclick="addToCart('${item.pID}', 1)">
                            <i class="fa-solid fa-cart-plus"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger rounded-circle ms-2" onclick="toggleFavorite('${item.pID}')">
                            <i class="fa-regular fa-trash-can"></i>
                        </button>
                    </div>
                `;
                container.append(html);
            });
        }
    }

    // ================= 5. 结算逻辑 (与详情页类似) =================
    function handleCheckout() {
        const btn = $('#btnCheckout');
        btn.prop('disabled', true);

        $.ajax({
            url: BASE_URL + '/api/OrderConfirm_DeliveryCheck',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                if (res.data && res.data.data && res.data.data.DeliveryInfos) {
                    existingAddresses = res.data.data.DeliveryInfos;
                } else {
                    existingAddresses = [];
                }
                renderCheckoutModal();
                const modal = new bootstrap.Modal(document.getElementById('checkoutModal'));
                modal.show();
            },
            complete: function() { btn.prop('disabled', false); }
        });
    }

    function renderCheckoutModal() {
        const listContainer = $('#existingAddressesList');
        listContainer.empty();
        selectedAddressIndex = 0;

        if (existingAddresses.length > 0) {
            existingAddresses.forEach((addr, idx) => {
                const html = `
                    <div class="address-card ${idx === 0 ? 'active' : ''}" onclick="selectAddress(${idx}, this)">
                        <div class="fw-bold">${addr.uContactPersonName} (${addr.uContactPersonPhone})</div>
                        <div class="small text-muted">${addr.uDeliveryAddress}</div>
                        ${addr.oDeliveryNote ? `<div class="small text-muted fst-italic">备注: ${addr.oDeliveryNote}</div>` : ''}
                    </div>
                `;
                listContainer.append(html);
            });
            hideNewAddressForm();
        } else {
            showNewAddressForm();
            $('#existingAddressesList').html('<p class="text-muted small">暂无收货记录</p>');
        }
    }

    function selectAddress(idx, el) {
        selectedAddressIndex = idx;
        $('.address-card').removeClass('active');
        $(el).addClass('active');
    }

    function showNewAddressForm() {
        isAddingNewAddress = true;
        $('#addressSelectionArea').addClass('d-none');
        $('#newAddressFormArea').removeClass('d-none');
    }

    function hideNewAddressForm() {
        isAddingNewAddress = false;
        $('#newAddressFormArea').addClass('d-none');
        $('#addressSelectionArea').removeClass('d-none');
    }

    function confirmOrderGeneration() {
        let finalInfo = null;

        if (isAddingNewAddress || existingAddresses.length === 0) {
            const name = $('#naName').val();
            const phone = $('#naPhone').val();
            const addr = $('#naAddr').val();

            if(!name || !phone || !addr) {
                Swal.fire('提示', '请填写完整收货信息', 'warning');
                return;
            }

            finalInfo = {
                uID: currentUserID,
                uContactPersonName: name,
                uContactPersonPhone: phone,
                uDeliveryAddress: addr,
                oDeliveryNote: $('#naNote').val() || '',
                uContactPersonGender: $('#naGender').val(),
                uContactPersonEmail: $('#naEmail').val(),
                oPostalCode: $('#naZip').val()
            };

            $.ajax({ url: BASE_URL + '/api/OrderConfirm_NewDeliveryRecord', type: 'POST', contentType: 'application/json', data: JSON.stringify(finalInfo) });

        } else {
            finalInfo = existingAddresses[selectedAddressIndex];
        }

        const btn = $('#btnConfirmOrder');
        btn.prop('disabled', true);

        const payload = {
            uID: currentUserID,
            oDeliveryInfo: finalInfo,
            pProducts: cartList.filter(i => i.isSelected).map(i => ({
                pID: String(i.pID),
                pAmount: Number(i.cAmount),
                oPrice: Number(i.pPrice)
            }))
        };

        $.ajax({
            url: BASE_URL + '/api/OrderConfirm_OrderGenerate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                const feedback = res.data && res.data.data ? res.data.data : null;
                if (!feedback || !feedback.oOrderID) {
                    Swal.fire('错误', '订单生成失败(库存不足或其他原因)', 'error');
                    return;
                }

                bootstrap.Modal.getInstance(document.getElementById('checkoutModal')).hide();
                bootstrap.Offcanvas.getInstance(document.getElementById('cartDrawer')).hide();

                Swal.fire({
                    title: '订单已生成!',
                    text: '是否立即支付?',
                    icon: 'success',
                    showCancelButton: true,
                    confirmButtonText: '立即支付',
                    cancelButtonText: '稍后支付'
                }).then((result) => {
                    if (result.isConfirmed) {
                        payOrder(feedback.oOrderID);
                    } else {
                        fetchCartDetails(); fetchCartCountOnly();
                    }
                });
            },
            error: function() { Swal.fire('错误', '请求失败', 'error'); },
            complete: function() { btn.prop('disabled', false); }
        });
    }

    function payOrder(orderID) {
        $.ajax({
            url: BASE_URL + '/api/OrderStatus_Update',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ oOrderID: orderID, NewStatus: 'Paid' }),
            success: function(res) {
                if (res.data === 'Update Accept') {
                    Swal.fire('成功', '支付成功！', 'success').then(() => {
                        window.location.href = 'OrderListView.jsp';
                    });
                } else {
                    Swal.fire('提示', '支付状态更新失败', 'warning');
                }
            }
        });
    }
</script>

</body>
</html>