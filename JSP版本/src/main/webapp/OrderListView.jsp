<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EBuyPlt 订单中心</title>

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
            font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
        }

        /* Header Styles */
        .app-header {
            background-color: #fff;
            height: 60px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            position: sticky;
            top: 0;
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
            font-size: 20px;
            font-weight: bold;
            color: #409EFF;
            cursor: pointer;
            text-decoration: none;
        }

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

        .action-btn:hover {
            color: #409EFF;
        }

        .badge-count {
            position: absolute;
            top: -5px;
            right: -8px;
            font-size: 10px;
            padding: 3px 6px;
        }

        /* Main Content */
        .main-content {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
            min-height: calc(100vh - 100px);
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .page-header h2 {
            margin: 0;
            font-size: 24px;
            color: #303133;
        }

        /* Order Card */
        .order-card {
            background: #fff;
            border-radius: 4px;
            border: 1px solid #ebeef5;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            margin-bottom: 20px;
            transition: box-shadow 0.3s;
        }

        .order-card:hover {
            box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
        }

        .card-header {
            padding: 15px 20px;
            border-bottom: 1px solid #ebeef5;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #fafafa;
        }

        .order-id { font-weight: bold; margin-right: 15px; color: #303133; }
        .order-date { color: #909399; font-size: 14px; }

        .card-body { padding: 0; }

        .order-footer {
            padding: 15px 20px;
            border-top: 1px solid #ebeef5;
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
        }

        .delivery-info { font-size: 13px; color: #606266; }
        .delivery-info p { margin: 2px 0; }

        .total-amount {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
            text-align: right;
        }
        .total-amount span, .subtotal { color: #f56c6c; }

        /* Cart & Fav Drawer Items */
        .drawer-item {
            display: flex;
            align-items: center;
            padding: 15px 0;
            border-bottom: 1px solid #f2f2f2;
        }

        .item-img {
            width: 60px;
            height: 60px;
            border-radius: 4px;
            object-fit: cover;
            margin: 0 10px;
            cursor: pointer;
        }

        .item-info { flex: 1; overflow: hidden; }
        .item-title {
            font-size: 14px;
            font-weight: bold;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 5px;
            cursor: pointer;
        }
        .item-title:hover { color: #409EFF; }

        .qty-group {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .qty-val { width: 30px; text-align: center; font-size: 14px; }

        .cart-footer {
            border-top: 1px solid #ebeef5;
            padding: 15px;
            background: #fff;
        }

        .price-text { color: #f56c6c; font-weight: bold; }

        /* Utilities */
        .d-none { display: none !important; }
        .cursor-pointer { cursor: pointer; }
    </style>
</head>
<body>

<!-- 顶部导航栏 -->
<header class="app-header">
    <div class="header-content">
        <a href="ShoppingnbView.jsp" class="logo">EBuyPlt 订单中心</a>

        <div class="d-flex align-items-center">
            <a href="ShoppingnbView.jsp" class="btn btn-link text-decoration-none text-secondary">返回商城</a>

            <!-- 收藏夹按钮 -->
            <button class="action-btn" onclick="openFavorites()" title="我的收藏">
                <i class="fa-regular fa-star"></i>
            </button>

            <!-- 购物车按钮 -->
            <button class="action-btn" onclick="openCart()" title="我的购物车">
                <i class="fa-solid fa-cart-shopping"></i>
                <span class="badge rounded-pill bg-danger badge-count d-none" id="headerCartCount">0</span>
            </button>

            <!-- 用户头像下拉 -->
            <div class="dropdown ms-3">
                <a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    <div class="rounded-circle bg-light d-flex justify-content-center align-items-center text-secondary" style="width: 32px; height: 32px; border: 1px solid #dcdfe6;">
                        <i class="fa-solid fa-user"></i>
                    </div>
                </a>
                <ul class="dropdown-menu dropdown-menu-end text-small shadow" aria-labelledby="userDropdown">
                    <li><span class="dropdown-item-text text-muted" id="dropdownUserID">ID: Loading...</span></li>
                    <li><a class="dropdown-item" href="#" onclick="goToUserProfile()">个人信息</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item text-danger" href="#" onclick="handleLogout()">退出登录</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>

<!-- 主内容区：订单列表 -->
<main class="main-content">
    <div class="page-header">
        <h2>我的订单</h2>
        <button class="btn btn-link text-primary text-decoration-none" onclick="fetchOrders()">刷新列表</button>
    </div>

    <!-- Loading / Empty / List Container -->
    <div id="orderContainer">
        <!-- Loading State -->
        <div id="orderLoading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>

        <!-- Empty State -->
        <div id="orderEmpty" class="text-center py-5 d-none">
            <i class="fa-solid fa-clipboard-list fa-4x text-muted mb-3"></i>
            <p class="text-muted">暂无订单记录</p>
        </div>

        <!-- Order List -->
        <div id="orderList"></div>
    </div>
</main>

<!-- 购物车抽屉 (Offcanvas) -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="cartDrawer" aria-labelledby="cartDrawerLabel" style="width: 500px;">
    <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title" id="cartDrawerLabel">我的购物车</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body p-0 position-relative">
        <div id="cartLoading" class="text-center py-5 d-none">
            <div class="spinner-border text-primary" role="status"></div>
        </div>

        <div id="cartContent">
            <div class="p-3 border-bottom bg-light">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="checkAllCart" onchange="toggleSelectAll(this)">
                    <label class="form-check-label" for="checkAllCart">全选</label>
                </div>
            </div>
            <div id="cartItemsContainer" class="p-3">
                <!-- JS Injected Items -->
            </div>
            <!-- Empty Cart -->
            <div id="cartEmpty" class="text-center py-5 d-none">
                <i class="fa-solid fa-cart-arrow-down fa-3x text-muted mb-3"></i>
                <p class="text-muted">购物车是空的</p>
            </div>
        </div>
    </div>
    <div class="cart-footer d-flex justify-content-between align-items-center">
        <div>
            <span>已选 (<span id="selectedCount">0</span>): </span>
            <span class="price-text fs-5">¥ <span id="selectedTotal">0.00</span></span>
        </div>
        <button class="btn btn-primary px-4" id="btnCheckout" onclick="handleCheckout()" disabled>结算</button>
    </div>
</div>

<!-- 收藏夹抽屉 (Offcanvas) -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="favDrawer" aria-labelledby="favDrawerLabel" style="width: 450px;">
    <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title" id="favDrawerLabel">我的收藏</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body p-0">
        <div id="favLoading" class="text-center py-5 d-none">
            <div class="spinner-border text-primary" role="status"></div>
        </div>
        <div id="favItemsContainer" class="p-3"></div>
        <div id="favEmpty" class="text-center py-5 d-none">
            <i class="fa-regular fa-star fa-3x text-muted mb-3"></i>
            <p class="text-muted">暂无收藏</p>
        </div>
    </div>
</div>

<!-- 结算确认 Modal -->
<div class="modal fade" id="checkoutModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">确认订单</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>您即将结算 <span id="checkoutCount" class="fw-bold">0</span> 件商品，共计 <span class="price-text">¥ <span id="checkoutTotal">0.00</span></span></p>
                <div class="bg-light p-3 rounded" style="max-height: 200px; overflow-y: auto;" id="checkoutPreviewList">
                    <!-- Items -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btnConfirmPay" onclick="confirmOrderGeneration()">确认支付</button>
            </div>
        </div>
    </div>
</div>

<script>
    // ================= 配置 =================
    const BASE_URL = 'http://192.168.126.94:8082';
    let currentUserID = '';

    // 全局数据缓存
    let globalCartList = [];
    let selectedCartItems = [];

    // ================= 初始化 =================
    $(document).ready(function() {
        const uID = sessionStorage.getItem('uID');
        if (!uID) {
            Swal.fire('提示', '请先登录', 'warning').then(() => {
                window.location.href = 'index.jsp';
            });
            return;
        }
        currentUserID = uID;
        $('#dropdownUserID').text('用户ID: ' + uID);

        // 初始化加载
        fetchOrders();
        fetchCartCountOnly(); // 仅加载数量更新角标
    });

    // ================= 1. 订单功能 =================
    function fetchOrders() {
        $('#orderLoading').removeClass('d-none');
        $('#orderList, #orderEmpty').addClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/GetOrderRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                const list = (res.data && res.data.OrderRecordList) ? res.data.OrderRecordList : [];
                if (list.length === 0) {
                    $('#orderEmpty').removeClass('d-none');
                } else {
                    // 按日期降序
                    list.sort((a, b) => new Date(b.oDate) - new Date(a.oDate));
                    renderOrders(list);
                    $('#orderList').removeClass('d-none');
                }
            },
            error: function() {
                Swal.fire('错误', '获取订单记录失败', 'error');
            },
            complete: function() {
                $('#orderLoading').addClass('d-none');
            }
        });
    }

    function renderOrders(orders) {
        const container = $('#orderList');
        container.empty();

        orders.forEach(order => {
            const statusInfo = getStatusInfo(order.oStatus);
            const totalAmount = calculateOrderTotal(order.pProducts);
            const canCancel = checkCanCancel(order.oDate, order.oStatus);

            // 配送信息
            const deliveryInfo = order.DeliveryInfo || {};
            const contact = deliveryInfo.uContactPersonName || '';
            const phone = deliveryInfo.uContactPersonPhone || '';
            const address = deliveryInfo.uDeliveryAddress || '';

            // 构建商品行
            let productRows = '';
            (order.pProducts || []).forEach(p => {
                const subTotal = (p.oPrice * p.pAmount).toFixed(2);
                productRows += `
                    <tr>
                        <td>${p.pID}</td>
                        <td>¥ ${p.oPrice}</td>
                        <td>${p.pAmount}</td>
                        <td class="subtotal">¥ ${subTotal}</td>
                    </tr>
                `;
            });

            const html = `
                <div class="order-card">
                    <div class="card-header">
                        <div>
                            <span class="order-id">订单号: ${order.oOrderID}</span>
                            <span class="order-date">${order.oDate}</span>
                        </div>
                        <span class="badge ${statusInfo.cls}">${statusInfo.text}</span>
                    </div>
                    <div class="card-body">
                        <table class="table table-bordered table-sm mb-0 align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th width="30%">商品ID</th>
                                    <th width="20%">单价</th>
                                    <th width="20%">数量</th>
                                    <th width="30%">小计</th>
                                </tr>
                            </thead>
                            <tbody>${productRows}</tbody>
                        </table>
                    </div>
                    <div class="order-footer">
                        <div class="delivery-info">
                            <p><strong>收货人:</strong> ${contact} (${phone})</p>
                            <p><strong>地址:</strong> ${address}</p>
                        </div>
                        <div class="text-end">
                            <div class="total-amount">订单总额: <span>¥ ${totalAmount.toFixed(2)}</span></div>
                            ${canCancel ? `<button class="btn btn-sm btn-outline-danger" onclick="handleCancelOrder('${order.oOrderID}')">取消订单</button>` : ''}
                        </div>
                    </div>
                </div>
            `;
            container.append(html);
        });
    }

    // 辅助函数
    function calculateOrderTotal(products) {
        if (!products) return 0;
        return products.reduce((sum, item) => sum + (item.oPrice * item.pAmount), 0);
    }

    function getStatusInfo(status) {
        switch(status) {
            case 'Paid': return { cls: 'bg-success', text: '已支付' };
            case 'Completed': return { cls: 'bg-success', text: '已完成' };
            case 'Cancelled': return { cls: 'bg-secondary', text: '已取消' };
            default: return { cls: 'bg-warning text-dark', text: status === 'Unpaid' ? '未支付' : status };
        }
    }

    function checkCanCancel(dateStr, status) {
        if (status === 'Cancelled') return false;
        const orderTime = new Date(dateStr).getTime();
        const now = new Date().getTime();
        // 1小时内可取消
        return (now - orderTime) < (60 * 60 * 1000);
    }

    function handleCancelOrder(orderID) {
        Swal.fire({
            title: '确定要取消该订单吗？',
            text: "取消后库存将恢复。",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: '确定取消',
            cancelButtonText: '再想想'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: BASE_URL + '/api/OrderCancelled',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        uID: currentUserID,
                        oOrderIDs: [{ oOrderID: orderID }]
                    }),
                    success: function(res) {
                        // 简单判断
                        if (res && res.data && res.data.oOrderIDs && res.data.oOrderIDs[0].FeedBack === 'Cancelled Accept') {
                            Swal.fire('已取消', '订单取消成功', 'success');
                            fetchOrders();
                        } else {
                            Swal.fire('失败', '取消失败：可能已超过取消时限', 'error');
                        }
                    },
                    error: function() {
                        Swal.fire('错误', '请求异常', 'error');
                    }
                });
            }
        });
    }

    // ================= 2. 购物车功能 =================

    // 打开抽屉时加载
    function openCart() {
        const drawer = new bootstrap.Offcanvas(document.getElementById('cartDrawer'));
        drawer.show();
        fetchCart();
    }

    // 仅更新角标 (不打开抽屉)
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

    function fetchCart() {
        $('#cartLoading').removeClass('d-none');
        $('#cartContent, #cartEmpty').addClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/ShoppingCartRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                globalCartList = (res.data && res.data) ? res.data : [];

                // 更新角标
                const count = globalCartList.reduce((sum, item) => sum + item.cAmount, 0);
                $('#headerCartCount').text(count).toggleClass('d-none', count === 0);

                if (globalCartList.length === 0) {
                    $('#cartEmpty').removeClass('d-none');
                } else {
                    renderCartItems();
                    $('#cartContent').removeClass('d-none');
                }
                updateCartCalculations(); // 重置选中状态和金额
            },
            complete: function() {
                $('#cartLoading').addClass('d-none');
            }
        });
    }

    function renderCartItems() {
        const container = $('#cartItemsContainer');
        container.empty();

        globalCartList.forEach(item => {
            const imgUrl = getProductImage(item.pImagesPath);
            const html = `
                <div class="drawer-item" data-pid="${item.pID}" data-price="${item.pPrice}" data-name="${item.pName}" data-amount="${item.cAmount}">
                    <div class="form-check d-flex align-items-center">
                        <input class="form-check-input cart-item-check" type="checkbox" onchange="updateCartCalculations()">
                    </div>
                    <img src="${imgUrl}" class="item-img" onclick="goToDetail('${item.pID}')">
                    <div class="item-info">
                        <div class="item-title" onclick="goToDetail('${item.pID}')">${item.pName}</div>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="price-text">¥ ${item.pPrice}</span>
                            <div class="qty-group">
                                <button class="btn btn-sm btn-outline-secondary py-0 px-2" onclick="changeQty('${item.pID}', -1)">-</button>
                                <span class="qty-val">${item.cAmount}</span>
                                <button class="btn btn-sm btn-outline-secondary py-0 px-2" onclick="changeQty('${item.pID}', 1)">+</button>
                            </div>
                        </div>
                    </div>
                    <button class="btn btn-link text-danger ms-2" onclick="removeItem('${item.pID}', ${item.cAmount})">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </div>
            `;
            container.append(html);
        });

        // 重置全选按钮
        $('#checkAllCart').prop('checked', false);
    }

    function toggleSelectAll(checkbox) {
        $('.cart-item-check').prop('checked', checkbox.checked);
        updateCartCalculations();
    }

    function updateCartCalculations() {
        let total = 0;
        let count = 0;
        selectedCartItems = [];

        $('.drawer-item').each(function() {
            const el = $(this);
            const checkbox = el.find('.cart-item-check');

            if (checkbox.is(':checked')) {
                const price = parseFloat(el.data('price'));
                const amount = parseInt(el.data('amount'));
                const pid = el.data('pid');
                const name = el.data('name');

                total += price * amount;
                count++; // 这里的 count 是选中了多少“种”商品，原Vue代码 selectedCount 逻辑也是 filter(selected).length

                selectedCartItems.push({
                    pID: pid,
                    pName: name,
                    pPrice: price,
                    cAmount: amount
                });
            }
        });

        $('#selectedCount').text(count);
        $('#selectedTotal').text(total.toFixed(2));
        $('#btnCheckout').prop('disabled', count === 0);

        // 联动全选按钮状态
        const allChecks = $('.cart-item-check');
        const allChecked = allChecks.length > 0 && allChecks.length === allChecks.filter(':checked').length;
        $('#checkAllCart').prop('checked', allChecked);
    }

    function changeQty(pID, delta) {
        // 先乐观更新 UI 或直接调接口刷新
        const endpoint = delta > 0 ? '/api/ShoppingCartAdd' : '/api/ShoppingCartRemove';
        $.ajax({
            url: BASE_URL + endpoint,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pID, cAmount: 1 }),
            success: function() {
                fetchCart(); // 刷新列表
            }
        });
    }

    function removeItem(pID, amount) {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartRemove',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pID, cAmount: amount }),
            success: function() {
                fetchCart();
            }
        });
    }

    // ================= 3. 收藏夹功能 =================
    function openFavorites() {
        const drawer = new bootstrap.Offcanvas(document.getElementById('favDrawer'));
        drawer.show();

        $('#favLoading').removeClass('d-none');
        $('#favItemsContainer, #favEmpty').addClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/FavouriteRecords',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID }),
            success: function(res) {
                const list = res.data || [];
                const container = $('#favItemsContainer');
                container.empty();

                if (list.length === 0) {
                    $('#favEmpty').removeClass('d-none');
                } else {
                    list.forEach(item => {
                        const imgUrl = getProductImage(item.pImagesPath);
                        const html = `
                            <div class="drawer-item">
                                <img src="${imgUrl}" class="item-img" onclick="goToDetail('${item.pID}')">
                                <div class="item-info">
                                    <div class="item-title" onclick="goToDetail('${item.pID}')">${item.pName}</div>
                                    <div class="price-text">¥ ${item.pPrice}</div>
                                </div>
                                <div class="d-flex gap-2 ms-2">
                                    <button class="btn btn-sm btn-primary rounded-circle" onclick="favToCart('${item.pID}')" title="加入购物车">
                                        <i class="fa-solid fa-cart-plus"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger rounded-circle" onclick="removeFav('${item.pID}')" title="删除">
                                        <i class="fa-regular fa-trash-can"></i>
                                    </button>
                                </div>
                            </div>
                        `;
                        container.append(html);
                    });
                    $('#favItemsContainer').removeClass('d-none');
                }
            },
            complete: function() {
                $('#favLoading').addClass('d-none');
            }
        });
    }

    function favToCart(pID) {
        $.ajax({
            url: BASE_URL + '/api/ShoppingCartAdd',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pID, cAmount: 1 }),
            success: function() {
                Swal.fire({
                    toast: true, position: 'top-end', icon: 'success',
                    title: '已加入购物车', showConfirmButton: false, timer: 1500
                });
                fetchCartCountOnly(); // 刷新角标
            }
        });
    }

    function removeFav(pID) {
        $.ajax({
            url: BASE_URL + '/api/FavouriteRemove',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUserID, pID: pID }),
            success: function() {
                openFavorites(); // 刷新
            }
        });
    }

    // ================= 4. 结算逻辑 =================
    function handleCheckout() {
        if (selectedCartItems.length === 0) return;

        // 填充 Modal
        const preview = $('#checkoutPreviewList');
        preview.empty();
        let total = 0;

        selectedCartItems.forEach(item => {
            const sub = item.pPrice * item.cAmount;
            total += sub;
            preview.append(`
                <div class="d-flex justify-content-between mb-2 border-bottom pb-1" style="font-size:14px;">
                    <div>${item.pName} <span class="text-muted">x${item.cAmount}</span></div>
                    <div class="fw-bold">¥ ${sub.toFixed(2)}</div>
                </div>
            `);
        });

        $('#checkoutCount').text(selectedCartItems.length);
        $('#checkoutTotal').text(total.toFixed(2));

        const modal = new bootstrap.Modal(document.getElementById('checkoutModal'));
        modal.show();
    }

    function confirmOrderGeneration() {
        const btn = $('#btnConfirmPay');
        btn.prop('disabled', true).text('处理中...');

        const payload = {
            uID: currentUserID,
            oDeliveryInfo: {
                uContactPersonName: "默认用户",
                uDeliveryAddress: "默认地址",
                uContactPersonPhone: "00000000000"
            },
            pProducts: selectedCartItems.map(item => ({
                pID: String(item.pID),
                pAmount: Number(item.cAmount),
                oPrice: Number(item.pPrice)
            }))
        };

        $.ajax({
            url: BASE_URL + '/api/OrderConfirm_OrderGenerate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function() {
                // 关闭 Modal
                const modalEl = document.getElementById('checkoutModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                modal.hide();

                // 关闭购物车抽屉
                const cartEl = document.getElementById('cartDrawer');
                const cartDrawer = bootstrap.Offcanvas.getInstance(cartEl);
                if(cartDrawer) cartDrawer.hide();

                Swal.fire('成功', '订单生成成功', 'success');

                // 刷新数据
                fetchOrders();
                fetchCartCountOnly(); // 购物车应该变空了(或者部分变空)，此处为了简单直接更新角标
            },
            error: function() {
                Swal.fire('失败', '订单生成失败', 'error');
            },
            complete: function() {
                btn.prop('disabled', false).text('确认支付');
            }
        });
    }

    // ================= 5. 通用函数 =================
    function getProductImage(path) {
        if (!path) return 'https://via.placeholder.com/60';
        if (path.startsWith('http')) return path;
        const clean = path.startsWith('/') ? path : '/' + path;
        return BASE_URL + clean;
    }

    function goToDetail(pID) {
        window.location.href = 'ProductDetailView.jsp?pID=' + pID;
    }

    function goToUserProfile() {
        window.location.href = 'UserProfileView.jsp?uID=' + currentUserID;
    }

    function handleLogout() {
        sessionStorage.removeItem('uID');
        window.location.href = 'index.jsp';
    }
</script>

</body>
</html>