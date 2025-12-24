<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品详情页 - EBuyPlt 后台</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body {
            background-color: #f0f2f5;
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
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

        /* Card Styles */
        .box-card {
            background: #fff;
            border-radius: 4px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
            border: 1px solid #ebeef5;
            margin-bottom: 20px;
        }

        .card-header {
            padding: 15px 20px;
            border-bottom: 1px solid #ebeef5;
            font-weight: bold;
            color: #303133;
            display: flex;
            align-items: center;
        }

        .card-body {
            padding: 20px;
        }

        /* Simulating Element Plus Descriptions with Table */
        .desc-table th {
            background-color: #fafafa;
            color: #606266;
            font-weight: 500;
            width: 15%;
            vertical-align: middle;
        }
        .desc-table td {
            color: #303133;
            width: 35%;
            vertical-align: middle;
        }

        .price-text {
            color: #f56c6c;
            font-size: 18px;
            font-weight: bold;
        }

        .info-box {
            padding: 15px;
            background: #f9f9f9;
            border-radius: 4px;
            line-height: 1.6;
            color: #606266;
            border: 1px solid #eee;
            min-height: 100px;
            white-space: pre-line;
        }

        /* Utilities */
        .d-none { display: none !important; }
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
        <h1 class="page-title">商品详情页</h1>
    </div>

    <!-- Loading State -->
    <div id="loadingState" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="mt-2 text-muted">加载商品信息中...</p>
    </div>

    <!-- Content Wrapper -->
    <div id="contentWrapper" class="d-none">
        <!-- Product Card -->
        <div class="box-card" id="productCard">
            <div class="card-header">
                <span>🎁 <span id="headerName"></span> (ID: <span id="headerID"></span>)</span>
                <span class="badge ms-3" id="headerStatus"></span>
            </div>
            <div class="card-body">
                <!-- Descriptions Table -->
                <table class="table table-bordered desc-table">
                    <tbody>
                    <tr>
                        <th>商品名称</th>
                        <td id="pName"></td>
                        <th>类型</th>
                        <td id="pType"></td>
                    </tr>
                    <tr>
                        <th>价格</th>
                        <td><span class="price-text" id="pPrice"></span></td>
                        <th>折扣</th>
                        <td id="pDiscount"></td>
                    </tr>
                    <tr>
                        <th>库存</th>
                        <td id="pInventory"></td>
                        <th>生产商</th>
                        <td id="pProducer"></td>
                    </tr>
                    <tr>
                        <th>发布日期</th>
                        <td id="pReleaseDate" colspan="3"></td>
                    </tr>
                    </tbody>
                </table>

                <div class="mt-4">
                    <h5 class="mb-3" style="font-size: 16px; font-weight: bold; color: #303133;">商品简介：</h5>
                    <div class="info-box" id="pInfo"></div>
                </div>
            </div>
        </div>

        <!-- Empty State -->
        <div id="emptyState" class="text-center py-5 d-none">
            <i class="fa-solid fa-box-open fa-4x text-muted mb-3"></i>
            <p class="text-muted">未找到商品信息</p>
        </div>
    </div>
</div>

<script>
    //配置
    // const BASE_URL = 'http://localhost:8080/EBuyPlt_JSP_war';
    const BASE_URL = 'http://localhost:8080';

    let currentPID = '';

    //初始化
    $(document).ready(function() {
        // 1. 获取 URL 参数 pID
        const urlParams = new URLSearchParams(window.location.search);
        currentPID = urlParams.get('pID');

        if (!currentPID) {
            Swal.fire('错误', '未提供商品ID', 'error').then(() => {
                history.back();
            });
            return;
        }

        //加载数据
        fetchProductInfo();
    });

    //数据逻辑
    function fetchProductInfo() {
        $('#loadingState').removeClass('d-none');
        $('#contentWrapper').addClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/AdminGetSpecificProductInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ pID: currentPID }),
            success: function(res) {
                if (res.data && res.data.length > 0) {
                    const info = res.data[0];
                    renderProduct(info);
                    $('#productCard').removeClass('d-none');
                    $('#emptyState').addClass('d-none');
                } else {
                    $('#productCard').addClass('d-none');
                    $('#emptyState').removeClass('d-none');
                }
            },
            error: function() {
                Swal.fire('错误', '获取商品详情失败', 'error');
                $('#productCard').addClass('d-none');
                $('#emptyState').removeClass('d-none');
            },
            complete: function() {
                $('#loadingState').addClass('d-none');
                $('#contentWrapper').removeClass('d-none');
            }
        });
    }

    function renderProduct(info) {
        // 头部信息
        $('#headerName').text(info.pName);
        $('#headerID').text(info.pID);

        // 状态 Tag
        const statusEl = $('#headerStatus');
        statusEl.text(info.pStatus);
        if (info.pStatus === '上架') {
            statusEl.removeClass('bg-danger').addClass('bg-success');
        } else {
            statusEl.removeClass('bg-success').addClass('bg-danger');
        }

        // 表格内容
        $('#pName').text(info.pName);
        $('#pType').text(info.pType);
        $('#pPrice').text('¥' + info.pPrice);
        $('#pDiscount').text(info.pDiscount || '无');
        $('#pInventory').text(info.pInventory);
        $('#pProducer').text(info.pProducer);
        $('#pReleaseDate').text(info.pReleaseDate);

        // 简介
        $('#pInfo').text(info.pInfo || '暂无描述');
    }
</script>

</body>
</html>