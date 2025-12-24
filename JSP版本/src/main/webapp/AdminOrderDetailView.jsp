<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单详情页 - EBuyPlt 后台</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body {
            background-color: #f0f2f5;
            font-family: "Helvetica Neue", Helvetica, "PingFang SC", Arial, sans-serif;
            padding-bottom: 40px;
        }

        .detail-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* Page Header */
        .page-header {
            background: #fff;
            padding: 15px 20px;
            margin-bottom: 20px;
            border-radius: 4px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .page-title {
            font-size: 18px;
            font-weight: bold;
            color: #303133;
            margin: 0;
        }

        /* Cards */
        .box-card {
            background: #fff;
            border-radius: 4px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            margin-bottom: 20px;
            border: 1px solid #ebeef5;
        }

        .card-header {
            padding: 15px 20px;
            border-bottom: 1px solid #ebeef5;
            font-weight: bold;
            color: #303133;
            background-color: #fff;
            border-radius: 4px 4px 0 0;
        }

        .card-body {
            padding: 20px;
        }

        /* Description List Style (Mimicking Element Plus) */
        .desc-table {
            width: 100%;
            border-collapse: collapse;
            border: 1px solid #ebeef5;
        }

        .desc-table td, .desc-table th {
            padding: 12px 15px;
            border: 1px solid #ebeef5;
            font-size: 14px;
        }

        .desc-label {
            background-color: #fafafa;
            color: #606266;
            font-weight: 600;
            width: 15%;
        }

        .desc-content {
            color: #303133;
            width: 35%;
        }

        /* Utilities */
        .d-none { display: none !important; }
        .text-price { color: #f56c6c; font-weight: bold; }
        .cursor-pointer { cursor: pointer; }
    </style>
</head>
<body>

<div class="detail-container">
    <!-- 头部导航 -->
    <div class="page-header">
        <button class="btn btn-link text-decoration-none text-dark p-0" onclick="history.back()">
            <i class="fa-solid fa-arrow-left"></i> 返回
        </button>
        <div class="vr mx-2"></div>
        <h1 class="page-title">订单详情页</h1>
    </div>

    <!-- Loading State -->
    <div id="pageLoading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="mt-2 text-muted">加载订单信息中...</p>
    </div>

    <div id="contentWrapper" class="d-none">
        <!-- 1. 订单概览 -->
        <div class="box-card">
            <div class="card-header">
                <span>🧾 订单概览 (单号: <span id="displayOrderID"></span>)</span>
            </div>
            <div class="card-body p-0">
                <table class="desc-table">
                    <tbody>
                    <tr>
                        <td class="desc-label">下单用户ID</td>
                        <td class="desc-content" id="oOrdererID"></td>
                        <td class="desc-label">下单时间</td>
                        <td class="desc-content" id="oDate"></td>
                    </tr>
                    <tr>
                        <td class="desc-label">当前状态</td>
                        <td class="desc-content"><span id="oStatusBadge" class="badge"></span></td>
                        <td class="desc-label">收货人</td>
                        <td class="desc-content" id="oReceiverName"></td>
                    </tr>
                    <tr>
                        <td class="desc-label">联系电话</td>
                        <td class="desc-content" id="oContactPhone"></td>
                        <td class="desc-label">收货地址</td>
                        <td class="desc-content" id="oDeliveryAddress"></td>
                    </tr>
                    <tr>
                        <td class="desc-label">备注</td>
                        <td class="desc-content" colspan="3" id="oDeliveryNote"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 2. 包含商品列表 -->
        <div class="box-card">
            <div class="card-header">
                <span>🛒 包含商品列表 (点击商品ID查看详情)</span>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>商品ID</th>
                            <th>成交单价</th>
                            <th>数量</th>
                            <th>发货状态</th>
                            <th>小计</th>
                        </tr>
                        </thead>
                        <tbody id="productTableBody">
                        <!-- JS 动态填充 -->
                        </tbody>
                    </table>
                </div>
                <!-- 商品加载 Loading -->
                <div id="productLoading" class="text-center py-3 d-none">
                    <div class="spinner-border text-primary spinner-border-sm" role="status"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // 配置
    // const BASE_URL = 'http://localhost:8080/EBuyPlt_JSP_war';
    const BASE_URL = 'http://localhost:8080';

    let currentOrderID = '';

    // 初始化
    $(document).ready(function() {
        // 1. 获取 URL 参数 oOrderID
        const urlParams = new URLSearchParams(window.location.search);
        currentOrderID = urlParams.get('oOrderID');

        if (!currentOrderID) {
            Swal.fire('错误', '未提供订单号', 'error').then(() => {
                history.back();
            });
            return;
        }

        $('#displayOrderID').text(currentOrderID);

        // 2. 加载数据

        Promise.all([fetchOrderHeader(), fetchOrderProducts()])
            .then(() => {
                $('#pageLoading').addClass('d-none');
                $('#contentWrapper').removeClass('d-none');
            })
            .catch(err => {
                console.error(err);
                $('#pageLoading').addClass('d-none');
                $('#contentWrapper').removeClass('d-none');
            });
    });

    //  获取订单概览
    function fetchOrderHeader() {
        return new Promise((resolve, reject) => {
            // 使用搜索接口查询特定订单
            $.ajax({
                url: BASE_URL + '/api/AdminOrderInfoSearch',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    SearchInput: currentOrderID,
                    oStatuses: [],
                    DateF: "",
                    DateR: ""
                }),
                success: function(res) {
                    if (res.data && res.data.data && res.data.data.length > 0) {
                        // 找到完全匹配的订单
                        const order = res.data.data.find(o => String(o.oOrderID) === String(currentOrderID)) || res.data.data[0];
                        renderOrderHeader(order);
                        resolve();
                    } else {
                        $('#oDeliveryNote').text('未获取到订单头信息');
                        resolve(); // 不阻塞页面显示
                    }
                },
                error: function(err) {
                    // ElMessage.error('获取订单概览失败');
                    reject(err);
                }
            });
        });
    }

    function renderOrderHeader(info) {
        $('#oOrdererID').text(info.uID || '未知');


        $('#oDate').text(info.oDate);
        $('#oReceiverName').text(info.oReceiverName);
        $('#oContactPhone').text(info.oContactPhone);
        $('#oDeliveryAddress').text(info.oDeliveryAddress);
        $('#oDeliveryNote').text(info.oDeliveryNote || '无');

        const statusBadge = $('#oStatusBadge');
        statusBadge.text(info.oStatus);

        // 简单的状态颜色映射
        if (info.oStatus === '已完成' || info.oStatus === 'Paid') {
            statusBadge.addClass('bg-success');
        } else if (info.oStatus === '已取消' || info.oStatus === 'Cancelled') {
            statusBadge.addClass('bg-secondary');
        } else if (info.oStatus === '待发货') {
            statusBadge.addClass('bg-warning text-dark');
        } else {
            statusBadge.addClass('bg-primary');
        }
    }

    //  获取订单商品列表
    function fetchOrderProducts() {
        return new Promise((resolve, reject) => {
            $('#productLoading').removeClass('d-none');

            $.ajax({
                url: BASE_URL + '/api/AdminGetOrderSpecificInfo',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ oOrderID: currentOrderID }),
                success: function(res) {
                    const list = (res.data && res.data.data) ? res.data.data : [];
                    renderProductTable(list);
                    resolve();
                },
                error: function(err) {
                    Swal.fire('提示', '获取商品列表失败', 'error');
                    reject(err);
                },
                complete: function() {
                    $('#productLoading').addClass('d-none');
                }
            });
        });
    }

    function renderProductTable(list) {
        const tbody = $('#productTableBody');
        tbody.empty();

        if (list.length === 0) {
            tbody.html('<tr><td colspan="5" class="text-center text-muted">该订单下暂无商品信息</td></tr>');
            return;
        }

        list.forEach(item => {
            const subtotal = (item.oPrice * item.oAmount).toFixed(2);
            const html = `
                <tr>
                    <td>
                        <a href="#" class="text-decoration-none fw-bold text-primary" onclick="goToProductDetail('${item.pID}')">
                            ${item.pID}
                        </a>
                    </td>
                    <td>¥${item.oPrice}</td>
                    <td>${item.oAmount}</td>
                    <td><span class="badge bg-info text-dark">${item.oProductDeliveryStatus}</span></td>
                    <td class="text-price">¥${subtotal}</td>
                </tr>
            `;
            tbody.append(html);
        });
    }

    // 跳转逻辑
    function goToProductDetail(pID) {
        window.location.href = 'AdminProductDetailView.jsp?pID=' + pID;
    }

</script>

</body>
</html>