<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EBuyPlt - 商品详情</title>

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
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            padding-top: 60px; /* Header height */
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
            margin-left: 15px;
            position: relative;
            cursor: pointer;
            transition: color 0.3s;
        }
        .action-btn:hover { color: #409EFF; }
        .badge-count {
            position: absolute; top: -5px; right: -8px; font-size: 10px; padding: 3px 6px;
        }

        /* --- Main Content --- */
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

        /* Carousel */
        .carousel-container {
            border: 1px solid #ebeef5;
            border-radius: 8px;
            overflow: hidden;
            height: 450px;
            background: #f9f9f9;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .carousel-item img {
            max-height: 450px;
            object-fit: contain;
            width: 100%;
        }

        /* Info Column */
        .info-container {
            padding-left: 30px;
            display: flex;
            flex-direction: column;
            height: 100%;
            justify-content: space-between;
        }

        .product-name { font-size: 28px; font-weight: 600; color: #303133; margin-bottom: 10px; }
        .product-id { font-size: 13px; color: #C0C4CC; margin-bottom: 25px; }

        .price-section {
            border-bottom: 1px solid #f0f2f5;
            padding-bottom: 20px;
            margin-bottom: 25px;
        }
        .price-text { color: #f56c6c; font-size: 36px; font-weight: bold; }
        .discount-badge { background: #f56c6c; color: #fff; padding: 4px 10px; border-radius: 12px; font-size: 14px; margin-left: 10px; vertical-align: middle;}

        .meta-row { margin-bottom: 12px; color: #606266; font-size: 14px; }
        .meta-label { color: #909399; width: 80px; display: inline-block; }
        .text-green { color: #67C23A; font-weight: bold; }
        .text-red { color: #F56C6C; font-weight: bold; }

        .action-buttons { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-top: 20px; }

        /* Section Divider */
        .section-divider { border-top: 1px solid #eee; margin: 40px 0; }

        .desc-title h3 { font-size: 20px; color: #303133; margin-bottom: 8px; }
        .title-underline { width: 60px; height: 4px; background: #409EFF; border-radius: 2px; margin-bottom: 20px; }
        .desc-content { font-size: 16px; color: #606266; line-height: 1.8; white-space: pre-line; }

        /* --- Comments Section --- */
        .comment-item {
            display: flex;
            gap: 15px;
            border-bottom: 1px solid #f0f2f5;
            padding-bottom: 20px;
            margin-bottom: 20px;
        }
        .comment-avatar {
            width: 40px; height: 40px; background-color: #409EFF; color: #fff;
            border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px;
        }
        .comment-header { display: flex; justify-content: space-between; margin-bottom: 5px; }
        .username { font-weight: bold; font-size: 14px; color: #303133; }
        .date { font-size: 12px; color: #909399; }
        .comment-text { font-size: 14px; color: #606266; line-height: 1.6; margin-bottom: 10px; }
        .reply-tag { color: #409EFF; font-weight: 500; margin-right: 5px; }

        .comment-actions span {
            font-size: 13px; color: #909399; cursor: pointer; margin-right: 15px; transition: color 0.2s;
        }
        .comment-actions span:hover { color: #409EFF; }

        .inline-reply-box { margin-top: 15px; background: #f9f9f9; padding: 10px; border-radius: 4px; }

        /* --- Drawer Items --- */
        .drawer-item { display: flex; align-items: center; gap: 10px; padding: 15px 0; border-bottom: 1px solid #f2f2f2; }
        .item-img { width: 64px; height: 64px; border-radius: 6px; object-fit: cover; border: 1px solid #eee; cursor: pointer;}
        .item-info { flex: 1; overflow: hidden; }
        .item-title { font-size: 14px; font-weight: bold; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; cursor: pointer; }
        .item-price { color: #f56c6c; font-weight: bold; }

        /* Address Selection */
        .address-card {
            border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px; margin-bottom: 10px; cursor: pointer;
        }
        .address-card.active { border-color: #409EFF; background-color: #ecf5ff; }
        .address-card:hover { border-color: #409EFF; }

        /* Utilities */
        .d-none { display: none !important; }
        .cursor-pointer { cursor: pointer; }
    </style>
</head>
<body>

<!-- Header -->
<header class="app-header">
    <div class="header-content">
        <a href="ShoppingnbView.jsp" class="logo">EBuyPlt</a>

        <div class="search-bar">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" class="form-control search-input" id="headerSearchInput" placeholder="搜索其他商品..." onkeypress="handleHeaderSearch(event)">
        </div>

        <div class="d-flex align-items-center">
            <a href="ShoppingnbView.jsp" class="btn btn-sm btn-outline-secondary me-2">继续购物</a>

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

    <!-- Loading Spinner -->
    <div id="pageLoading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="mt-2 text-muted">加载商品信息中...</p>
    </div>

    <!-- Product Wrapper -->
    <div id="productWrapper" class="product-detail-wrapper d-none">
        <!-- Part 1: Images & Info -->
        <div class="row">
            <!-- Carousel -->
            <div class="col-md-6 mb-4 mb-md-0">
                <div id="productCarousel" class="carousel slide carousel-container" data-bs-ride="false">
                    <div class="carousel-inner" id="carouselInner">
                        <!-- JS injected -->
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#productCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon bg-dark rounded-circle" aria-hidden="true"></span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#productCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon bg-dark rounded-circle" aria-hidden="true"></span>
                    </button>
                </div>
            </div>

            <!-- Info -->
            <div class="col-md-6">
                <div class="info-container">
                    <div>
                        <div class="d-flex align-items-center gap-2 mb-2">
                            <span class="badge bg-dark" id="pType">Type</span>
                            <span class="text-muted small">厂商: <span id="pProducer">Producer</span></span>
                        </div>
                        <h1 class="product-name" id="pName">Product Name</h1>
                        <p class="product-id">Product ID: <span id="pID">0</span></p>

                        <div class="price-section">
                            <span class="text-danger fw-bold fs-4">¥</span>
                            <span class="price-text" id="pPrice">0.00</span>
                            <span class="discount-badge d-none" id="pDiscountBadge">Discount</span>
                        </div>

                        <div class="meta-info">
                            <div class="meta-row">
                                <span class="meta-label">库存状态:</span>
                                <span id="pStockStatus">Loading...</span>
                            </div>
                            <div class="meta-row"><span class="meta-label">发布日期:</span> <span id="pReleaseDate"></span></div>
                            <div class="meta-row"><span class="meta-label">商品状态:</span> <span id="pStatus"></span></div>
                        </div>
                    </div>

                    <div class="action-buttons">
                        <div class="d-flex align-items-center mb-3">
                            <span class="me-3">数量</span>
                            <div class="input-group" style="width: 140px;">
                                <button class="btn btn-outline-secondary" onclick="changeBuyQty(-1)">-</button>
                                <input type="text" class="form-control text-center" id="buyQtyInput" value="1" readonly>
                                <button class="btn btn-outline-secondary" onclick="changeBuyQty(1)">+</button>
                            </div>
                        </div>
                        <div class="d-flex gap-3">
                            <button class="btn btn-primary btn-lg flex-grow-1" id="btnAddToCart" onclick="addProductToCart()">
                                <i class="fa-solid fa-cart-plus me-2"></i> 加入购物车
                            </button>
                            <button class="btn btn-outline-secondary btn-lg" id="btnFavorite" onclick="toggleProductFavorite()">
                                <i class="fa-regular fa-star me-2" id="favIcon"></i> <span id="favText">收藏</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="section-divider"></div>

        <!-- Part 2: Description -->
        <div class="description-section">
            <div class="desc-title">
                <h3>商品详情</h3>
                <div class="title-underline"></div>
            </div>
            <div class="desc-content" id="pInfo">暂无详细描述</div>
        </div>

        <div class="section-divider"></div>

        <!-- Part 3: Comments -->
        <div class="comments-section" id="commentsArea">
            <div class="desc-title">
                <h3>用户评价 (<span id="commentCount">0</span>)</h3>
                <div class="title-underline"></div>
            </div>

            <div class="mb-4">
                <textarea class="form-control mb-2" id="newCommentContent" rows="3" placeholder="写下你的评价..." maxlength="200"></textarea>
                <div class="text-end">
                    <button class="btn btn-primary" onclick="submitComment(null)">
                        <i class="fa-regular fa-comments me-1"></i> 发表评论
                    </button>
                </div>
            </div>

            <div id="commentListContainer">
                <!-- JS Injected -->
            </div>
            <div id="noCommentsMsg" class="text-center py-4 text-muted d-none">
                <i class="fa-regular fa-comment-dots fa-2x mb-2"></i>
                <p>暂无评价，快来抢沙发吧！</p>
            </div>
        </div>
    </div>

    <!-- Not Found State -->
    <div id="notFoundState" class="text-center py-5 d-none">
        <h3>未找到商品信息</h3>
        <a href="ShoppingnbView.jsp" class="btn btn-primary mt-3">返回首页</a>
    </div>
</main>

<!-- 购物车 Offcanvas -->
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

<!-- 收藏夹 Offcanvas -->
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

<!-- 结算 Modal -->
<div class="modal fade" id="checkoutModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">确认收货信息</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div id="addressSelectionArea">
                    <p class="fw-bold mb-2">请选择收货地址：</p>
                    <div id="existingAddressesList" style="max-height: 250px; overflow-y: auto;">
                        <!-- Addresses -->
                    </div>
                    <button class="btn btn-link p-0 mt-2" onclick="showNewAddressForm()">使用新地址 +</button>
                </div>

                <div id="newAddressFormArea" class="d-none">
                    <p class="fw-bold mb-2" id="addrFormTitle">新增收货地址：</p>
                    <form id="newAddrForm">
                        <div class="mb-2"><label class="form-label">收货人</label><input type="text" class="form-control" id="naName"></div>
                        <div class="mb-2"><label class="form-label">电话</label><input type="text" class="form-control" id="naPhone"></div>
                        <div class="mb-2"><label class="form-label">地址</label><textarea class="form-control" id="naAddr"></textarea></div>
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
    let currentPID = '';
    let productData = null;
    let buyQuantity = 1;

    let cartList = [];
    let favList = [];
    let commentList = [];

    // 结算相关
    let existingAddresses = [];
    let selectedAddressIndex = 0;
    let isAddingNewAddress = false;

    // ================= 初始化 =================
    $(document).ready(function() {
        // 1. 检查登录
        const uID = sessionStorage.getItem('uID');
        if (!uID) {
            Swal.fire('未登录', '请先登录', 'error').then(() => {
                window.location.href = 'index.jsp';
            });
            return;
        }
        currentUserID = uID;
        $('#menuUserID').text('用户ID: ' + uID);

        // 2. 获取 URL 参数 pID
        const urlParams = new URLSearchParams(window.location.search);
        currentPID = urlParams.get('pID');

        if (!currentPID) {
            Swal.fire('参数错误', '缺少商品ID', 'error').then(() => {
                window.location.href = 'ShoppingnbView.jsp';
            });
            return;
        }

        // 3. 加载数据
        fetchProductDetail(currentPID);
        fetchComments(currentPID);
        fetchCartCountOnly(); // 仅加载角标，点击时再加载详情
        fetchFavorites(); // 收藏状态需要用到
    });

    // ================= 功能函数 =================

    function getImageUrl(path) {
        if (!path) return 'https://via.placeholder.com/400x400?text=No+Image';
        let cleanPath = path.replace(/\\/g, '/');
        if (cleanPath.startsWith('http')) return cleanPath;
        if (!cleanPath.startsWith('/')) cleanPath = '/' + cleanPath;
        return BASE_URL + cleanPath;
    }

    function goToUserProfile() {
        window.location.href = 'UserProfileView.jsp?uID=' + currentUserID;
    }

    function handleLogout() {
        sessionStorage.removeItem('uID');
        window.location.href = 'index.jsp';
    }

    function handleHeaderSearch(e) {
        if (e.key === 'Enter') {
            const val = $('#headerSearchInput').val().trim();
            if (val) window.location.href = 'ShoppingnbView.jsp?q=' + encodeURIComponent(val);
        }
    }

    function goToDetail(pID) {
        if(String(pID) === String(currentPID)) return;
        window.location.href = 'ProductDetailView.jsp?pID=' + pID;
    }

    // ================= 1. 商品详情逻辑 =================
    function fetchProductDetail(id) {
        $('#pageLoading').removeClass('d-none');
        $('#productWrapper').addClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/ProductClick',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ pID: id }),
            success: function(res) {
                if (res.data && res.data.data) {
                    productData = res.data.data;
                    renderProductInfo(productData);
                    $('#productWrapper').removeClass('d-none');
                } else {
                    $('#notFoundState').removeClass('d-none');
                }
            },
            error: function() { $('#notFoundState').removeClass('d-none'); },
            complete: function() { $('#pageLoading').addClass('d-none'); }
        });
    }

    function renderProductInfo(p) {
        // 1. 轮播图
        const carouselInner = $('#carouselInner');
        carouselInner.empty();
        const images = p.pImagePaths || [];
        if (images.length === 0) {
            carouselInner.append('<div class="carousel-item active"><div class="d-flex justify-content-center align-items-center h-100 text-muted">暂无图片</div></div>');
        } else {
            images.forEach((img, idx) => {
                const activeClass = idx === 0 ? 'active' : '';
                const html = `
                    <div class="carousel-item ${activeClass}">
                        <img src="${getImageUrl(img)}" alt="Product Image">
                    </div>`;
                carouselInner.append(html);
            });
        }

        // 2. 信息
        $('#pType').text(p.pType);
        $('#pProducer').text(p.pProducer);
        $('#pName').text(p.pName);
        $('#pID').text(p.pID);
        $('#pPrice').text(p.pPrice);

        if (p.pDiscount && p.pDiscount > 0 && p.pDiscount !== '无') {
            $('#pDiscountBadge').text('Discount: ' + p.pDiscount).removeClass('d-none');
        }

        // 库存
        const stockEl = $('#pStockStatus');
        if (p.pInventory > 0) {
            stockEl.html(`<span class="text-green">有货 (剩余 ${p.pInventory})</span>`);
            $('#buyQtyInput').prop('disabled', false);
            $('#btnAddToCart').prop('disabled', false);
        } else {
            stockEl.html(`<span class="text-red">暂时缺货</span>`);
            $('#buyQtyInput').prop('disabled', true);
            $('#btnAddToCart').prop('disabled', true);
        }

        $('#pReleaseDate').text(p.pReleaseDate);
        $('#pStatus').text(p.pStatus);
        $('#pInfo').text(p.pInfo || '暂无详细描述');

        // 更新收藏按钮状态
        updateFavBtnState();
    }

    function changeBuyQty(delta) {
        if (!productData) return;
        let newQty = buyQuantity + delta;
        if (newQty < 1) newQty = 1;
        if (productData.pInventory > 0 && newQty > productData.pInventory) newQty = productData.pInventory;
        buyQuantity = newQty;
        $('#buyQtyInput').val(buyQuantity);
    }

    // ================= 2. 评论逻辑 =================
    function fetchComments(pid) {
        $.ajax({
            url: BASE_URL + '/api/GetAllProductComment',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ pID: pid }),
            success: function(res) {
                commentList = (res.data && res.data.data) ? res.data.data : [];
                renderComments();
            }
        });
    }

    function renderComments() {
        $('#commentCount').text(commentList.length);
        const container = $('#commentListContainer');
        container.empty();

        if (commentList.length === 0) {
            $('#noCommentsMsg').removeClass('d-none');
        } else {
            $('#noCommentsMsg').addClass('d-none');
            commentList.forEach(item => {
                const isReply = item.rReplyID && item.Recipient;
                const html = `
                    <div class="comment-item">
                        <div class="comment-avatar"><i class="fa-solid fa-user"></i></div>
                        <div class="flex-grow-1">
                            <div class="comment-header">
                                <span class="username">${item.Commenter}</span>
                                <span class="date">${item.cDate || ''}</span>
                            </div>
                            <div class="comment-text">
                                ${isReply ? `<span class="reply-tag">回复 @${item.Recipient} :</span>` : ''}
                                ${item.cContent}
                            </div>
                            <div class="comment-actions">
                                <span onclick="likeComment('${item.cID}')">
                                    <i class="fa-regular fa-thumbs-up"></i> ${item.cLikes > 0 ? item.cLikes : '点赞'}
                                </span>
                                <span onclick="toggleReplyBox('${item.cID}')">
                                    <i class="fa-regular fa-comment-dots"></i> 回复
                                </span>
                            </div>
                            <!-- Inline Reply Box -->
                            <div id="replyBox-${item.cID}" class="inline-reply-box d-none">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="replyInput-${item.cID}" placeholder="回复 @${item.Commenter}...">
                                    <button class="btn btn-primary" onclick="submitComment('${item.cID}')">
                                        <i class="fa-solid fa-paper-plane"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                container.append(html);
            });
        }
    }

    function toggleReplyBox(cid) {
        // 先关闭其他的
        $('.inline-reply-box').addClass('d-none');
        // 切换当前的
        const box = $(`#replyBox-${cid}`);
        if(box.hasClass('d-none')) {
            box.removeClass('d-none');
            $(`#replyInput-${cid}`).focus();
        } else {
            box.addClass('d-none');
        }
    }

    function submitComment(replyToID) {
        let content = '';
        if (replyToID) {
            content = $(`#replyInput-${replyToID}`).val();
        } else {
            content = $('#newCommentContent').val();
        }

        if (!content.trim()) {
            Swal.fire('提示', '请输入评论内容', 'warning');
            return;
        }

        const payload = {
            pID: currentPID,
            uID: currentUserID,
            cContent: content,
            rReplyID: replyToID ? replyToID : null
        };

        $.ajax({
            url: BASE_URL + '/api/SendProductComment',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function() {
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: '评论发表成功', showConfirmButton: false, timer: 1500
                });
                $('#newCommentContent').val('');
                fetchComments(currentPID);
            },
            error: function() {
                Swal.fire('错误', '评论发送失败', 'error');
            }
        });
    }

    function likeComment(cid) {
        $.ajax({
            url: BASE_URL + '/api/GiveLikesProductComment',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ cID: cid }),
            success: function() {
                fetchComments(currentPID); // 刷新点赞数
            }
        });
    }


    // ================= 3. 购物车逻辑 =================
    function addProductToCart() {
        if (productData.pInventory <= 0) return;
        addToCart(productData, buyQuantity);
    }

    function addToCart(item, qty) {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartAdd',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: item.pID, cAmount: qty }),
            success: function() {
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: '已加入购物车', showConfirmButton: false, timer: 1500
                });
                fetchCartCountOnly(); // 刷新角标
                // 如果抽屉开着，刷新内容
                const cartEl = document.getElementById('cartDrawer');
                if (cartEl.classList.contains('show')) fetchCartDetails();
            },
            error: function() { Swal.fire('错误', '操作失败', 'error'); }
        });
    }

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
                // 保留勾选状态逻辑 (略简化，每次刷新重置勾选，或者需要复杂映射)
                // 这里为了流畅，每次重置
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
                        <input class="form-check-input cart-check" type="checkbox"
                               onchange="updateCartSelection(${index}, this.checked)" ${item.isSelected ? 'checked' : ''}>
                        <img src="${getImageUrl(item.pImagesPath)}" class="item-img" onclick="goToDetail('${item.pID}')">
                        <div class="item-info">
                            <div class="item-title" onclick="goToDetail('${item.pID}')">${item.pName}</div>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="item-price">¥ ${item.pPrice}</span>
                                <div class="input-group input-group-sm" style="width: 80px;">
                                    <button class="btn btn-outline-secondary px-1" onclick="updateCartItemQty('${item.pID}', -1)">-</button>
                                    <span class="form-control text-center px-0 fs-6">${item.cAmount}</span>
                                    <button class="btn btn-outline-secondary px-1" onclick="updateCartItemQty('${item.pID}', 1)">+</button>
                                </div>
                            </div>
                        </div>
                        <button class="btn btn-link text-danger" onclick="removeCartItem('${item.pID}', ${item.cAmount})">
                            <i class="fa-regular fa-trash-can"></i>
                        </button>
                    </div>
                `;
                container.append(html);
            });
        }
        updateCartTotalUI();
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

    // 勾选逻辑
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

        const allChecked = cartList.length > 0 && selected.length === cartList.length;
        $('#checkAllCart').prop('checked', allChecked);
    }


    // ================= 4. 收藏夹逻辑 =================
    function fetchFavorites() {
        $.ajax({
            url: BASE_URL + '/api/FavouriteRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                favList = (res.data && res.data.data) ? res.data.data : [];
                updateFavBtnState();
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
                            <div class="item-price">¥ ${item.pPrice}</div>
                        </div>
                        <button class="btn btn-sm btn-primary rounded-circle" onclick="addToCart({pID:'${item.pID}'}, 1)">
                            <i class="fa-solid fa-cart-plus"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger rounded-circle" onclick="toggleFavorite('${item.pID}', true)">
                            <i class="fa-regular fa-trash-can"></i>
                        </button>
                    </div>
                `;
                container.append(html);
            });
        }
    }

    function isFavorite(id) {
        return favList.some(i => String(i.pID) === String(id));
    }

    function updateFavBtnState() {
        const btn = $('#btnFavorite');
        const icon = $('#favIcon');
        const text = $('#favText');

        if (isFavorite(currentPID)) {
            btn.removeClass('btn-outline-secondary').addClass('btn-warning text-white');
            icon.removeClass('fa-regular').addClass('fa-solid');
            text.text('已收藏');
        } else {
            btn.addClass('btn-outline-secondary').removeClass('btn-warning text-white');
            icon.addClass('fa-regular').removeClass('fa-solid');
            text.text('收藏');
        }
    }

    function toggleProductFavorite() {
        toggleFavorite(currentPID);
    }

    function toggleFavorite(pid, isFromDrawer = false) {
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
                    if(isFromDrawer) renderFavDrawer();
                });
            }
        });
    }

    // Override fetchFavorites to return promise for chaining
    const originalFetchFav = fetchFavorites;
    fetchFavorites = function() {
        return new Promise((resolve) => {
            $.ajax({
                url: BASE_URL + '/api/FavouriteRecords',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ uID: currentUserID }),
                success: function(res) {
                    favList = (res.data && res.data.data) ? res.data.data : [];
                    updateFavBtnState();
                    resolve();
                }
            });
        });
    }


    // ================= 5. 结算逻辑 =================
    function handleCheckout() {
        const selected = cartList.filter(i => i.isSelected);
        if (selected.length === 0) return;

        // 获取收货地址
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
                uContactPersonGender: '男', // 默认
                oPostalCode: '000000'
            };

            // 保存新地址 (fire and forget for this flow, usually should wait)
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
                // 检查反馈
                const feedback = res.data && res.data.data ? res.data.data : null;
                if (!feedback || !feedback.oOrderID) {
                    Swal.fire('错误', '订单生成失败', 'error');
                    return;
                }

                // 成功
                bootstrap.Modal.getInstance(document.getElementById('checkoutModal')).hide();
                bootstrap.Offcanvas.getInstance(document.getElementById('cartDrawer')).hide();

                // 支付确认
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
                        fetchCartDetails(); fetchCartCountOnly(); // 刷新购物车状态
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