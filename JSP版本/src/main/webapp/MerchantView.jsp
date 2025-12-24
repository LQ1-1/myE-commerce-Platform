<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EBuyPlt 商户管理中心</title>

    <!-- 1. Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 3. jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- 4. Bootstrap Bundle JS (含 Popper) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- 5. SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        body {
            background-color: #f0f2f5;
            height: 100vh;
            display: flex;
            flex-direction: column;
            margin: 0;
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        }

        /* --- Header --- */
        .app-header {
            background-color: #fff;
            border-bottom: 1px solid #dcdfe6;
            height: 60px;
            padding: 0 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
        }

        .app-title {
            font-size: 18px;
            color: #303133;
            margin: 0;
            font-weight: bold;
        }

        .nav-actions .btn-nav {
            border: none;
            background: none;
            color: #606266;
            font-size: 16px;
            margin-right: 15px;
            text-decoration: none;
            cursor: pointer;
            transition: color 0.3s;
        }

        .nav-actions .btn-nav:hover, .nav-actions .btn-nav.active {
            color: #409EFF;
            font-weight: bold;
        }

        .user-profile {
            cursor: pointer;
            display: flex;
            align-items: center;
            color: #303133;
        }

        .header-divider {
            border-left: 1px solid #dcdfe6;
            height: 20px;
            margin: 0 20px;
        }

        /* --- Main Content --- */
        .app-main {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
        }

        .page-header {
            margin-bottom: 20px;
            border-bottom: 1px solid #ebeef5;
            padding-bottom: 10px;
        }

        .page-header h2 {
            font-size: 20px;
            color: #303133;
            margin: 0;
        }

        /* --- Product Card --- */
        .product-card {
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            transition: transform 0.2s, box-shadow 0.2s;
            cursor: pointer;
            height: 100%;
            border: none;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .image-wrapper {
            height: 180px;
            width: 100%;
            background-color: #f5f7fa;
            position: relative;
            overflow: hidden;
        }

        .card-image {
            width: 100%;
            height: 100%;
            object-fit: cover;
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
            height: 38px;
        }

        /* --- Add Form --- */
        .form-container {
            background: #fff;
            padding: 30px;
            border-radius: 4px;
            max-width: 800px;
            margin: 0 auto;
            box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
        }

        /* --- Image Uploader UI --- */
        .upload-box {
            border: 1px dashed #d9d9d9;
            border-radius: 6px;
            cursor: pointer;
            width: 120px;
            height: 120px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 28px;
            color: #8c939d;
            position: relative;
            overflow: hidden;
            background-color: #fafafa;
        }

        .upload-box:hover {
            border-color: #409EFF;
            color: #409EFF;
        }

        .preview-img-full {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        /* --- Gallery in Modal --- */
        .gallery-list {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .image-preview-wrapper {
            position: relative;
            width: 148px;
            height: 148px;
            border-radius: 6px;
            overflow: hidden;
            border: 1px solid #cdd0d6;
        }

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

        .image-preview-wrapper:hover .image-mask {
            opacity: 1;
        }

        .delete-icon {
            color: #fff;
            font-size: 20px;
        }

        .delete-icon:hover {
            color: #f56c6c;
        }

        /* Helpers */
        .d-none { display: none !important; }
        .text-danger { color: #f56c6c !important; }
        .text-success { color: #67c23a !important; }
    </style>
</head>
<body>

<!-- 顶部导航栏 -->
<header class="app-header">
    <div class="d-flex align-items-center">
        <h1 class="app-title">EBuyPlt 商户管理中心</h1>
    </div>

    <div class="d-flex align-items-center">
        <!-- 导航菜单 -->
        <div class="nav-actions">
            <button class="btn-nav active" id="nav-inventory" onclick="switchTab('inventory')">
                <i class="fa-solid fa-box"></i> 商品管理
            </button>
            <button class="btn-nav" id="nav-add" onclick="switchTab('add')">
                <i class="fa-solid fa-plus"></i> 新品上架
            </button>
            <button class="btn-nav" id="nav-sales" onclick="switchTab('sales')">
                <i class="fa-solid fa-list"></i> 销售记录
            </button>
        </div>

        <div class="header-divider"></div>

        <!-- 用户下拉菜单 -->
        <div class="dropdown">
            <div class="user-profile dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                <div style="width: 32px; height: 32px; background-color: #409EFF; border-radius: 50%; color: white; display: flex; align-items: center; justify-content: center; margin-right: 8px;">
                    <i class="fa-solid fa-user"></i>
                </div>
                <span class="username" id="displayUsername">Merchant</span>
            </div>
            <ul class="dropdown-menu dropdown-menu-end">
                <li><span class="dropdown-item-text text-muted" id="menuMerchantID">商户ID: Loading...</span></li>
                <li><a class="dropdown-item" href="#" onclick="goToUserProfile()">个人信息</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-danger" href="#" onclick="handleLogout()">退出登录</a></li>
            </ul>
        </div>
    </div>
</header>

<!-- 主内容区域 -->
<main class="app-main">

    <!-- 1. 商品库存 View -->
    <div id="view-inventory">
        <div class="page-header">
            <h2>我的商品库存</h2>
        </div>

        <div id="inventory-loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2 text-muted">加载商品中...</p>
        </div>

        <div id="inventory-error" class="text-center py-5 d-none">
            <i class="fa-solid fa-triangle-exclamation text-danger fa-3x mb-3"></i>
            <p>获取数据失败，请检查网络</p>
            <button class="btn btn-outline-danger" onclick="fetchProductList()">重新加载</button>
        </div>

        <div id="inventory-empty" class="text-center py-5 d-none">
            <i class="fa-solid fa-inbox text-muted fa-3x mb-3"></i>
            <p>目前上架记录为空</p>
            <button class="btn btn-primary" onclick="switchTab('add')">去上架新商品</button>
        </div>

        <!-- 商品列表容器 -->
        <div id="inventory-list" class="row g-4 d-none">
            <!-- JS 动态插入 -->
        </div>
    </div>

    <!-- 2. 新品上架 View -->
    <div id="view-add" class="d-none">
        <div class="page-header">
            <h2>发布新商品</h2>
        </div>
        <div class="form-container">
            <form id="addForm" onsubmit="return false;">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label">商品名称 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="add-pName" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">商品类型</label>
                        <input type="text" class="form-control" id="add-pType" placeholder="例如：电子产品">
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-4">
                        <label class="form-label">售价 (¥) <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="add-pPrice" step="0.1" min="0" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">折扣 (0-1)</label>
                        <input type="number" class="form-control" id="add-pDiscount" step="0.05" min="0" max="1" value="1.0">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">初始库存 <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="add-pInventory" step="1" min="0" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">生产商</label>
                    <input type="text" class="form-control" id="add-pProducer">
                </div>

                <div class="mb-3">
                    <label class="form-label">发布日期</label>
                    <input type="date" class="form-control" id="add-pReleaseDate">
                </div>

                <div class="mb-3">
                    <label class="form-label">商品简介</label>
                    <textarea class="form-control" id="add-pInfo" rows="3"></textarea>
                </div>

                <hr>
                <h5 class="mb-3">图片上传</h5>

                <div class="mb-3">
                    <label class="form-label">商品缩略图 (封面) <span class="text-danger">*</span></label>
                    <div class="upload-box" onclick="$('#file-thumbnail').click()">
                        <img id="preview-thumbnail" class="preview-img-full d-none">
                        <i id="icon-thumbnail" class="fa-solid fa-plus"></i>
                    </div>
                    <input type="file" id="file-thumbnail" class="d-none" accept="image/*" onchange="previewImage(this, 'preview-thumbnail', 'icon-thumbnail')">
                    <div class="form-text">只能上传1张封面图</div>
                </div>

                <div class="mb-3">
                    <label class="form-label">商品展示图 (多张)</label>
                    <div class="d-flex flex-wrap gap-2" id="add-gallery-container">
                        <!-- 动态预览区 -->
                        <div class="upload-box" onclick="$('#file-gallery').click()">
                            <i class="fa-solid fa-plus"></i>
                        </div>
                    </div>
                    <input type="file" id="file-gallery" class="d-none" accept="image/*" multiple onchange="handleAddGallery(this)">
                </div>

                <div class="mt-4">
                    <button class="btn btn-primary" id="btn-submit-add" onclick="submitNewProduct()">立即上架</button>
                    <button class="btn btn-secondary" onclick="resetAddForm()">重置</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 3. 销售记录 View -->
    <div id="view-sales" class="d-none">
        <div class="page-header">
            <h2>销售记录</h2>
        </div>

        <div class="table-responsive bg-white p-3 rounded shadow-sm">
            <table class="table table-striped table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th>订单编号</th>
                    <th>商品ID</th>
                    <th>交易金额</th>
                    <th>数量</th>
                    <th>买家信息</th>
                    <th>下单时间</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody id="sales-table-body">
                <!-- JS 动态插入 -->
                </tbody>
            </table>
            <div id="sales-loading" class="text-center py-3">
                <span class="spinner-border spinner-border-sm"></span> 加载中...
            </div>
        </div>
    </div>
</main>

<!-- 编辑/详情 模态框 -->
<div class="modal fade" id="detailModal" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">商品详情与编辑</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <ul class="nav nav-tabs mb-3" id="detailTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="basic-tab" data-bs-toggle="tab" data-bs-target="#basic-info-pane" type="button">基本信息</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="image-tab" data-bs-toggle="tab" data-bs-target="#image-pane" type="button">图片管理</button>
                    </li>
                </ul>
                <div class="tab-content">
                    <!-- 基本信息 Tab -->
                    <div class="tab-pane fade show active" id="basic-info-pane" role="tabpanel">
                        <form id="editForm">
                            <div class="mb-3 row">
                                <label class="col-sm-2 col-form-label">商品ID</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control-plaintext" id="edit-pID" readonly>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">商品名称</label>
                                    <input type="text" class="form-control" id="edit-pName">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">商品类型</label>
                                    <input type="text" class="form-control" id="edit-pType">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-4">
                                    <label class="form-label">价格</label>
                                    <input type="number" class="form-control" id="edit-pPrice" step="0.1">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">折扣</label>
                                    <input type="number" class="form-control" id="edit-pDiscount" step="0.05" max="1" min="0">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">库存</label>
                                    <input type="number" class="form-control" id="edit-pInventory">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">生产商</label>
                                    <input type="text" class="form-control" id="edit-pProducer">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">上架日期</label>
                                    <input type="text" class="form-control" id="edit-pReleaseDate" disabled>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">状态</label>
                                <select class="form-select" id="edit-pStatus">
                                    <option value="上架">上架</option>
                                    <option value="缺货">缺货</option>
                                    <option value="下架">下架</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">简介</label>
                                <textarea class="form-control" id="edit-pInfo" rows="4"></textarea>
                            </div>
                        </form>
                    </div>

                    <!-- 图片管理 Tab -->
                    <div class="tab-pane fade" id="image-pane" role="tabpanel">
                        <div class="alert alert-warning py-2" style="font-size: 13px;">提示：移动鼠标到图片上可显示删除按钮；点击 + 号上传新图。</div>

                        <h6 class="text-muted">商品缩略图 (封面)</h6>
                        <div class="mb-4">
                            <!-- 缩略图容器 -->
                            <div id="edit-thumbnail-container" class="image-preview-wrapper d-none">
                                <img id="edit-thumbnail-img" src="" class="preview-img-full">
                                <div class="image-mask" onclick="removeEditThumbnail()">
                                    <i class="fa-solid fa-trash-can delete-icon"></i>
                                </div>
                            </div>
                            <!-- 上传新缩略图按钮 -->
                            <div id="edit-thumbnail-uploader" class="upload-box d-none" onclick="$('#edit-file-thumbnail').click()">
                                <i class="fa-solid fa-plus"></i>
                            </div>
                            <input type="file" id="edit-file-thumbnail" class="d-none" accept="image/*" onchange="handleEditThumbnailChange(this)">
                        </div>

                        <h6 class="text-muted">商品展示图 (详情)</h6>
                        <div class="gallery-list" id="edit-gallery-list">
                            <!-- JS 动态填充展示图 -->
                            <div class="upload-box" style="width: 148px; height: 148px;" onclick="$('#edit-file-gallery').click()">
                                <i class="fa-solid fa-plus"></i>
                            </div>
                        </div>
                        <input type="file" id="edit-file-gallery" class="d-none" accept="image/*" onchange="handleEditGalleryChange(this)">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btn-save-edit" onclick="updateProductInfo()">保存修改</button>
            </div>
        </div>
    </div>
</div>

<script>
    // ================= 配置 =================
    const API_BASE_URL = 'http://192.168.126.94:8082';

    // ================= 全局变量 =================
    let currentMerchantID = '';
    let currentEditingProduct = null; // 当前正在编辑的商品原始数据副本

    // 编辑相关临时状态
    let editNewThumbnailFile = null;  // 新上传的缩略图文件
    let editNewGalleryFiles = [];     // 新上传的展示图文件数组
    let editCurrentGalleryUrls = [];  // 当前保留的旧展示图URL列表

    // 上架相关临时状态
    let addGalleryFiles = []; // 上架时的展示图文件数组

    // ================= 初始化 =================
    $(document).ready(function() {
        const uID = sessionStorage.getItem('uID');
        if (!uID) {
            Swal.fire('未登录', '请先登录', 'warning').then(() => {
                window.location.href = 'index.jsp'; // 假设登录页是 index.jsp
            });
            return;
        }
        currentMerchantID = uID;
        $('#displayUsername').text(uID);
        $('#menuMerchantID').text('商户ID: ' + uID);

        // 默认加载库存
        fetchProductList();
    });

    // ================= 导航逻辑 =================
    function switchTab(tabName) {
        // 更新按钮状态
        $('.nav-actions .btn-nav').removeClass('active');
        $('#nav-' + tabName).addClass('active');

        // 隐藏所有视图
        $('#view-inventory, #view-add, #view-sales').addClass('d-none');

        // 显示目标视图
        $('#view-' + tabName).removeClass('d-none');

        // 数据加载逻辑
        if (tabName === 'inventory') fetchProductList();
        if (tabName === 'sales') fetchSalesRecord();
    }

    function handleLogout() {
        sessionStorage.removeItem('uID');
        Swal.fire('已退出', '您已安全退出系统', 'success').then(() => {
            window.location.href = 'index.jsp';
        });
    }

    function goToUserProfile() {
        window.location.href = 'UserProfileView.jsp?uID=' + currentMerchantID;
    }

    // ================= 工具函数 =================
    function getImageUrl(path) {
        if (!path) return '';
        if (path.startsWith('http') || path.startsWith('blob:')) return path;
        return API_BASE_URL + path;
    }

    // ================= 1. 库存管理逻辑 =================
    function fetchProductList() {
        $('#inventory-loading').removeClass('d-none');
        $('#inventory-list').addClass('d-none').empty();
        $('#inventory-error, #inventory-empty').addClass('d-none');

        $.ajax({
            url: API_BASE_URL + '/api/ProductAllInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentMerchantID }),
            success: function(res) {
                const data = res.data || res;
                const products = data.AllInfo || [];

                if (!products || products.length === 0) {
                    $('#inventory-empty').removeClass('d-none');
                } else {
                    renderProductList(products);
                    $('#inventory-list').removeClass('d-none');
                }
            },
            error: function(err) {
                console.error(err);
                $('#inventory-error').removeClass('d-none');
            },
            complete: function() {
                $('#inventory-loading').addClass('d-none');
            }
        });
    }

    function renderProductList(products) {
        const container = $('#inventory-list');
        products.forEach(item => {
            const statusClass = item.pInventory > 0 ? 'bg-success' : 'bg-danger';
            const statusText = item.pInventory > 0 ? '上架' : '缺货';
            const imgUrl = getImageUrl(item.pThumbnail);

            // 存储数据到DOM以便点击时获取，避免再次请求
            // 注意：属性名称都转为小写了，data-product
            const cardHtml = `
                <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="product-card" onclick='openProductDetail(${JSON.stringify(item)})'>
                        <div class="image-wrapper">
                            <img src="${imgUrl}" class="card-image" onerror="this.src='https://via.placeholder.com/300x200?text=No+Image'">
                            <span class="badge ${statusClass} status-tag">${statusText}</span>
                        </div>
                        <div class="card-content">
                            <h3 class="product-title" title="${item.pName}">${item.pName}</h3>
                            <div class="price-row">
                                <span class="price">¥${item.pPrice}</span>
                                <span class="inventory">库存: ${item.pInventory}</span>
                            </div>
                            <p class="desc">${item.pInfo || '暂无描述'}</p>
                        </div>
                    </div>
                </div>
            `;
            container.append(cardHtml);
        });
    }

    // ================= 2. 新品上架逻辑 =================

    // 预览单张图片
    function previewImage(input, imgId, iconId) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#' + imgId).attr('src', e.target.result).removeClass('d-none');
                $('#' + iconId).addClass('d-none');
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    // 处理上架页面的多图选择预览
    function handleAddGallery(input) {
        if (input.files && input.files.length > 0) {
            Array.from(input.files).forEach(file => {
                addGalleryFiles.push(file); // 加入全局数组
                const reader = new FileReader();
                reader.onload = function(e) {
                    const html = `
                        <div class="upload-box" style="width: 100px; height: 100px; cursor: default;">
                            <img src="${e.target.result}" class="preview-img-full">
                        </div>
                    `;
                    $('#add-gallery-container').prepend(html);
                }
                reader.readAsDataURL(file);
            });
        }
    }

    function resetAddForm() {
        $('#addForm')[0].reset();
        $('#preview-thumbnail').addClass('d-none').attr('src', '');
        $('#icon-thumbnail').removeClass('d-none');
        // 清空展示图预览 (保留最后的+号按钮)
        $('#add-gallery-container .upload-box:not(:last-child)').remove();
        addGalleryFiles = [];
    }

    function submitNewProduct() {
        const fileInput = $('#file-thumbnail')[0];
        if (!fileInput.files.length) {
            Swal.fire('提示', '请上传商品缩略图', 'warning');
            return;
        }

        // 基本表单验证
        if (!$('#add-pName').val() || !$('#add-pPrice').val() || !$('#add-pInventory').val()) {
            Swal.fire('提示', '请完善带 * 的必填项', 'warning');
            return;
        }

        $('#btn-submit-add').prop('disabled', true).text('提交中...');

        // 1. 先提交文本信息
        const payload = {
            uID: currentMerchantID,
            pName: $('#add-pName').val(),
            pType: $('#add-pType').val(),
            pPrice: parseFloat($('#add-pPrice').val()),
            pDiscount: parseFloat($('#add-pDiscount').val()),
            pInventory: parseInt($('#add-pInventory').val()),
            pProducer: $('#add-pProducer').val(),
            pReleaseDate: $('#add-pReleaseDate').val(),
            pInfo: $('#add-pInfo').val(),
            pStatus: '上架'
        };

        $.ajax({
            url: API_BASE_URL + '/api/ProductOnSale',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                const data = res.data || res;
                if (data && data.pID) {
                    const newPid = data.pID;
                    uploadProductImages(newPid);
                } else {
                    Swal.fire('错误', '商品信息创建失败', 'error');
                    $('#btn-submit-add').prop('disabled', false).text('立即上架');
                }
            },
            error: function(err) {
                console.error(err);
                Swal.fire('错误', '服务器异常', 'error');
                $('#btn-submit-add').prop('disabled', false).text('立即上架');
            }
        });
    }

    function uploadProductImages(pID) {
        const formData = new FormData();
        formData.append('uID', currentMerchantID);
        formData.append('pID', pID);

        // 缩略图
        formData.append('cover', $('#file-thumbnail')[0].files[0]);

        // 展示图
        addGalleryFiles.forEach(file => {
            formData.append('gallery', file);
        });

        $.ajax({
            url: API_BASE_URL + '/api/ProductImagesUpload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(res) {
                if (res === 'OnSaleSuccess' || (typeof res === 'string' && res.includes('Success'))) {
                    Swal.fire('成功', '商品上架成功！', 'success');
                    resetAddForm();
                    switchTab('inventory');
                } else {
                    Swal.fire('警告', '文本上传成功，但图片可能失败', 'warning');
                }
            },
            complete: function() {
                $('#btn-submit-add').prop('disabled', false).text('立即上架');
            }
        });
    }

    // ================= 3. 销售记录逻辑 =================
    function fetchSalesRecord() {
        $('#sales-loading').removeClass('d-none');
        $('#sales-table-body').empty();

        $.ajax({
            url: API_BASE_URL + '/api/ProductSaledInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentMerchantID }),
            success: function(res) {
                const data = res.data || res;
                const list = data.SaledItemList || [];
                renderSalesTable(list);
            },
            complete: function() {
                $('#sales-loading').addClass('d-none');
            }
        });
    }

    function renderSalesTable(list) {
        const tbody = $('#sales-table-body');
        if(list.length === 0) {
            tbody.html('<tr><td colspan="7" class="text-center">暂无记录</td></tr>');
            return;
        }

        list.forEach(row => {
            const productInfo = row.OrderProductInfoTableItem || {};
            const deliveryInfo = row.DeliveryInfoItem || {};

            const html = `
                <tr>
                    <td>${row.oOrderID}</td>
                    <td>${productInfo.pID}</td>
                    <td class="text-danger fw-bold">¥${productInfo.oPrice}</td>
                    <td>${productInfo.pAmount}</td>
                    <td>
                        <small>
                        ${deliveryInfo.uContactPersonName}<br>
                        ${deliveryInfo.uContactPersonPhone}<br>
                        ${deliveryInfo.uDeliveryAddress}
                        </small>
                    </td>
                    <td>${row.oDate}</td>
                    <td><span class="badge bg-secondary">${row.oStatus}</span></td>
                </tr>
            `;
            tbody.append(html);
        });
    }

    // ================= 4. 编辑/详情逻辑 (最复杂部分) =================

    // 打开模态框并填充数据
    function openProductDetail(product) {
        currentEditingProduct = JSON.parse(JSON.stringify(product));

        // 重置编辑状态
        editNewThumbnailFile = null;
        editNewGalleryFiles = [];
        editCurrentGalleryUrls = currentEditingProduct.pShowcaseImageList ? [...currentEditingProduct.pShowcaseImageList] : [];

        // 填充基本信息
        $('#edit-pID').val(product.pID);
        $('#edit-pName').val(product.pName);
        $('#edit-pType').val(product.pType);
        $('#edit-pPrice').val(product.pPrice);
        $('#edit-pDiscount').val(product.pDiscount);
        $('#edit-pInventory').val(product.pInventory);
        $('#edit-pProducer').val(product.pProducer);
        $('#edit-pReleaseDate').val(product.pReleaseDate);
        $('#edit-pStatus').val(product.pStatus);
        $('#edit-pInfo').val(product.pInfo);

        // 渲染图片管理区域
        renderEditImages();

        // 切换回第一个Tab
        var firstTabEl = document.querySelector('#detailTabs button[data-bs-target="#basic-info-pane"]');
        var tab = new bootstrap.Tab(firstTabEl);
        tab.show();

        // 显示模态框
        const modal = new bootstrap.Modal(document.getElementById('detailModal'));
        modal.show();
    }

    function renderEditImages() {
        // 1. 缩略图逻辑
        // 如果有新文件，显示新文件预览
        if (editNewThumbnailFile) {
            const blobUrl = URL.createObjectURL(editNewThumbnailFile);
            $('#edit-thumbnail-img').attr('src', blobUrl);
            $('#edit-thumbnail-container').removeClass('d-none');
            $('#edit-thumbnail-uploader').addClass('d-none');
        }
        // 否则如果原始数据有缩略图且没被标记删除
        else if (currentEditingProduct.pThumbnail) {
            $('#edit-thumbnail-img').attr('src', getImageUrl(currentEditingProduct.pThumbnail));
            $('#edit-thumbnail-container').removeClass('d-none');
            $('#edit-thumbnail-uploader').addClass('d-none');
        }
        // 否则显示上传按钮
        else {
            $('#edit-thumbnail-container').addClass('d-none');
            $('#edit-thumbnail-uploader').removeClass('d-none');
        }

        // 2. 展示图逻辑
        const galleryContainer = $('#edit-gallery-list');
        // 保留最后一个元素（上传按钮），清空前面的图片
        galleryContainer.children('.image-preview-wrapper').remove();

        // 渲染旧图片 (editCurrentGalleryUrls)
        editCurrentGalleryUrls.forEach((url, index) => {
            const html = `
                <div class="image-preview-wrapper">
                    <img src="${getImageUrl(url)}" class="preview-img-full">
                    <div class="image-mask" onclick="removeEditGalleryItem('old', ${index})">
                        <i class="fa-solid fa-trash-can delete-icon"></i>
                    </div>
                </div>
            `;
            galleryContainer.prepend(html);
        });

        // 渲染新上传的图片 (editNewGalleryFiles)
        // 注意：反向遍历插入，保持顺序
        for (let i = editNewGalleryFiles.length - 1; i >= 0; i--) {
            const file = editNewGalleryFiles[i];
            const blobUrl = URL.createObjectURL(file);
            const html = `
                <div class="image-preview-wrapper">
                    <img src="${blobUrl}" class="preview-img-full">
                    <div class="image-mask" onclick="removeEditGalleryItem('new', ${i})">
                        <i class="fa-solid fa-trash-can delete-icon"></i>
                    </div>
                </div>
            `;
            galleryContainer.append(html); // new files append after old ones, but before the add button (handled by flex order logic usually, but here prepend/append mix is tricky. Let's simplify: Just clear all and rebuild including button)
        }

        // 重新排序DOM：旧图 -> 新图 -> 按钮
        // 为了简化，上面直接操作了 DOM。如果顺序乱了，可以清空全部，先插旧，再插新，再插按钮。
        // 这里采用更稳妥的完全重绘
        galleryContainer.empty();

        editCurrentGalleryUrls.forEach((url, index) => {
            galleryContainer.append(`
                <div class="image-preview-wrapper">
                    <img src="${getImageUrl(url)}" class="preview-img-full">
                    <div class="image-mask" onclick="removeEditGalleryItem('old', ${index})">
                        <i class="fa-solid fa-trash-can delete-icon"></i>
                    </div>
                </div>
            `);
        });

        editNewGalleryFiles.forEach((file, index) => {
            galleryContainer.append(`
                <div class="image-preview-wrapper">
                    <img src="${URL.createObjectURL(file)}" class="preview-img-full">
                    <div class="image-mask" onclick="removeEditGalleryItem('new', ${index})">
                        <i class="fa-solid fa-trash-can delete-icon"></i>
                    </div>
                </div>
            `);
        });

        // 最后添加上传按钮
        galleryContainer.append(`
            <div class="upload-box" style="width: 148px; height: 148px;" onclick="$('#edit-file-gallery').click()">
                <i class="fa-solid fa-plus"></i>
            </div>
        `);
    }

    // 缩略图操作
    function removeEditThumbnail() {
        if (editNewThumbnailFile) {
            editNewThumbnailFile = null; // 取消新上传的
            // 如果原本有图，恢复原本的图？还是说用户意图是删除？
            // 逻辑：如果用户删除了新上传的，应该回到“无图”或者“原图”状态。
            // 简单处理：视为删除当前显示的。
            // 如果本来有原图，这里删除后，意味着 pThumbnail 为空。
        }
        currentEditingProduct.pThumbnail = ''; // 标记为空
        renderEditImages();
    }

    function handleEditThumbnailChange(input) {
        if (input.files && input.files[0]) {
            editNewThumbnailFile = input.files[0];
            // 只要有新文件，逻辑上视为有了缩略图
            // currentEditingProduct.pThumbnail = 'placeholder';
            renderEditImages();
        }
    }

    // 展示图操作
    function removeEditGalleryItem(type, index) {
        if (type === 'old') {
            editCurrentGalleryUrls.splice(index, 1);
        } else {
            editNewGalleryFiles.splice(index, 1);
        }
        renderEditImages();
    }

    function handleEditGalleryChange(input) {
        if (input.files && input.files[0]) {
            editNewGalleryFiles.push(input.files[0]);
            renderEditImages();
        }
    }

    // 提交更新
    function updateProductInfo() {
        // 校验缩略图
        // 必须存在：要么有新文件，要么有旧URL
        const hasOld = !!currentEditingProduct.pThumbnail;
        const hasNew = !!editNewThumbnailFile;

        if (!hasOld && !hasNew) {
            Swal.fire('提示', '商品必须包含一张缩略图', 'warning');
            return;
        }

        $('#btn-save-edit').prop('disabled', true).text('保存中...');

        const formData = new FormData();

        // 1. 基本信息
        formData.append('pID', $('#edit-pID').val());
        formData.append('pName', $('#edit-pName').val());
        formData.append('pType', $('#edit-pType').val());
        formData.append('pDiscount', $('#edit-pDiscount').val());
        formData.append('pPrice', $('#edit-pPrice').val());
        formData.append('pProducer', $('#edit-pProducer').val());
        formData.append('pReleaseDate', $('#edit-pReleaseDate').val());
        formData.append('pInfo', $('#edit-pInfo').val());
        formData.append('pInventory', $('#edit-pInventory').val());
        formData.append('pStatus', $('#edit-pStatus').val());

        // 2. 缩略图逻辑
        // oldThumbnailPicURL 始终传递原始的URL（如果没有就是空串）
        // 这里的逻辑有点绕，根据Vue代码：
        // 如果有新文件 -> newThumbnailPicURL 为空，上传文件
        // 如果没新文件 -> newThumbnailPicURL = oldThumbnailPicURL

        const originalUrl = currentEditingProduct.oldThumbnailPicURL || currentEditingProduct.pThumbnail || '';
        formData.append('oldThumbnailPicURL', originalUrl); // 后端可能只用这个做参考

        if (editNewThumbnailFile) {
            formData.append('newThumbnailPicURL', ''); // 告诉后端有新文件
            formData.append('newThumbnailItem.pImgType', '缩略图');
            formData.append('newThumbnailItem.fileData', editNewThumbnailFile);
        } else {
            // 没有新文件，保留旧的
            formData.append('newThumbnailPicURL', originalUrl);
        }

        // 3. 展示图逻辑
        // 3.1 旧图处理
        if (currentEditingProduct.oldShowcaseImagesURL) {
            // Vue代码似乎传了所有原始的作为 oldShowcaseImagesURL，然后传剩余的作为 newShowcaseImagesURL(如果是字符串)
            // 这里我们简化逻辑，匹配后端通常行为：
            // 传入 "newShowcaseImagesURL" 列表，包含所有要在数据库保留的旧图URL
            editCurrentGalleryUrls.forEach(url => {
                // 注意：有些后端可能需要区分 old 和 new 字段，这里照搬 Vue 逻辑
                formData.append('newShowcaseImagesURL', url);
            });
        }

        // 3.2 新图上传
        editNewGalleryFiles.forEach((file, index) => {
            formData.append(`pShowcaseImageList[${index}].pImgType`, '展示图');
            formData.append(`pShowcaseImageList[${index}].pImagePath`, file);
        });

        // 发送请求
        $.ajax({
            url: API_BASE_URL + '/api/ProductInfoUpdate',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(res) {
                if (res === 'Success' || (typeof res === 'string' && res.includes('Success'))) {
                    Swal.fire('成功', '商品信息更新成功', 'success');
                    // 关闭模态框
                    const modalEl = document.getElementById('detailModal');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                    // 刷新列表
                    fetchProductList();
                } else {
                    Swal.fire('错误', '更新失败: ' + res, 'error');
                }
            },
            error: function(err) {
                console.error(err);
                Swal.fire('错误', '更新请求异常', 'error');
            },
            complete: function() {
                $('#btn-save-edit').prop('disabled', false).text('保存修改');
            }
        });
    }
</script>

</body>
</html>