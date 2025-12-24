<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户详情页 - EBuyPlt 后台</title>

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
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .card-body {
            padding: 20px;
        }

        /* Description List Style */
        .desc-row {
            display: flex;
            flex-wrap: wrap;
            border: 1px solid #ebeef5;
            border-bottom: none;
        }
        .desc-item {
            display: flex;
            width: 33.33%; /* 3 columns */
            border-bottom: 1px solid #ebeef5;
        }
        .desc-label {
            background-color: #fafafa;
            color: #606266;
            padding: 12px 15px;
            width: 100px;
            font-weight: 500;
            border-right: 1px solid #ebeef5;
            display: flex;
            align-items: center;
        }
        .desc-content {
            padding: 12px 15px;
            color: #303133;
            flex: 1;
            display: flex;
            align-items: center;
            border-right: 1px solid #ebeef5;
        }
        .desc-item:last-child .desc-content {
            border-right: none;
        }

        /* Utilities */
        .d-none { display: none !important; }
        .cursor-pointer { cursor: pointer; }

        /* Table overrides */
        .table thead th {
            background-color: #fafafa;
            font-weight: 600;
            color: #606266;
        }
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
        <h1 class="page-title">用户详情页</h1>
    </div>

    <!-- 1. 用户基本信息卡片 -->
    <div class="box-card">
        <div class="card-header">
            <span><i class="fa-solid fa-user me-2"></i>基本信息 (ID: <span id="displayUID">...</span>)</span>
        </div>
        <div class="card-body p-0">
            <div id="userInfoLoading" class="text-center py-4">
                <div class="spinner-border text-primary" role="status"></div>
            </div>
            <div id="userInfoContent" class="d-none">
                <div class="desc-row">
                    <div class="desc-item">
                        <div class="desc-label">昵称</div>
                        <div class="desc-content" id="info_uNickName"></div>
                    </div>
                    <div class="desc-item">
                        <div class="desc-label">电话</div>
                        <div class="desc-content" id="info_uPhone"></div>
                    </div>
                    <div class="desc-item">
                        <div class="desc-label">邮箱</div>
                        <div class="desc-content" id="info_uEmail"></div>
                    </div>
                    <div class="desc-item">
                        <div class="desc-label">性别</div>
                        <div class="desc-content" id="info_uGender"></div>
                    </div>
                    <div class="desc-item">
                        <div class="desc-label">类型</div>
                        <div class="desc-content"><span class="badge bg-info text-dark" id="info_uAccountType"></span></div>
                    </div>
                    <div class="desc-item">
                        <div class="desc-label">状态</div>
                        <div class="desc-content" id="info_uAccountStatus"></div>
                    </div>
                    <div class="desc-item" style="width: 100%;">
                        <div class="desc-label">注册日期</div>
                        <div class="desc-content" id="info_uRegisterDate"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 2. 收货地址列表 -->
    <div class="box-card">
        <div class="card-header">
            <span><i class="fa-solid fa-location-dot me-2"></i>收货地址记录</span>
            <button class="btn btn-sm btn-primary" onclick="openAddDialog()">新增地址</button>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle mb-0">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th>收货人</th>
                        <th>电话</th>
                        <th>详细地址</th>
                        <th>邮编</th>
                        <th>邮箱</th>
                        <th>备注</th>
                        <th width="140">操作</th>
                    </tr>
                    </thead>
                    <tbody id="deliveryTableBody">
                    <!-- JS 填充 -->
                    </tbody>
                </table>
            </div>
            <div id="deliveryLoading" class="text-center py-3 d-none">
                <div class="spinner-border text-primary" role="status"></div>
            </div>
        </div>
    </div>

    <!-- 3. 订单记录 -->
    <div class="box-card">
        <div class="card-header">
            <span><i class="fa-solid fa-box me-2"></i>订单记录 (点击行查看详情)</span>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle mb-0">
                    <thead>
                    <tr>
                        <th>订单号</th>
                        <th>下单时间</th>
                        <th>状态</th>
                        <th>收件人</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="orderTableBody">
                    <!-- JS 填充 -->
                    </tbody>
                </table>
            </div>
            <div id="orderLoading" class="text-center py-3 d-none">
                <div class="spinner-border text-primary" role="status"></div>
            </div>
        </div>
    </div>
</div>

<!-- 4. 新增/编辑地址 Modal -->
<div class="modal fade" id="addressModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addressModalTitle">新增收货地址</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="addressForm">
                    <input type="hidden" id="d_uDIndex"> <!-- 隐藏索引 -->

                    <div class="mb-3">
                        <label class="form-label">收货人 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="d_name" placeholder="请输入姓名">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">联系电话 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="d_phone" placeholder="请输入电话">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">性别</label>
                        <select class="form-select" id="d_gender">
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">详细地址 <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="d_address" rows="2" placeholder="请输入详细地址"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">邮箱</label>
                        <input type="email" class="form-control" id="d_email" placeholder="请输入邮箱">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">邮编</label>
                        <input type="text" class="form-control" id="d_zip" placeholder="请输入邮编">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">备注</label>
                        <input type="text" class="form-control" id="d_note" placeholder="请输入备注">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btnSaveAddress" onclick="submitAddress()">确定</button>
            </div>
        </div>
    </div>
</div>

<script>
    // ================= 配置 =================
    const BASE_URL = 'http://192.168.126.94:8082';
    let currentUID = '';
    let isEditMode = false;

    // ================= 初始化 =================
    $(document).ready(function() {
        const urlParams = new URLSearchParams(window.location.search);
        currentUID = urlParams.get('uID');

        if (!currentUID) {
            Swal.fire('错误', '未获取到用户ID，无法查询详情', 'error').then(() => {
                history.back();
            });
            return;
        }

        $('#displayUID').text(currentUID);

        // 并行加载数据
        fetchUserInfo();
        fetchDeliveryInfo();
        fetchOrderInfo();
    });

    // ================= 1. 获取用户信息 =================
    function fetchUserInfo() {
        // 由于原代码是从 history.state 获取，这里为了稳健，直接调接口
        // 如果后端没有专门查单用户的接口，这里复用 GetUserAccountInfo
        $.ajax({
            url: BASE_URL + '/api/GetUserAccountInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUID }),
            success: function(res) {
                if (res.code === 200 && res.data) {
                    const d = res.data;
                    $('#info_uNickName').text(d.uNickName);
                    $('#info_uPhone').text(d.uPhone);
                    $('#info_uEmail').text(d.uEmail);
                    $('#info_uGender').text(d.uGender);
                    $('#info_uAccountType').text(d.uAccountType);
                    $('#info_uAccountStatus').html(`<span class="badge ${d.uAccountStatus === '正常' ? 'bg-success' : 'bg-danger'}">${d.uAccountStatus}</span>`);
                    $('#info_uRegisterDate').text(d.uRegisterDate);

                    $('#userInfoLoading').addClass('d-none');
                    $('#userInfoContent').removeClass('d-none');
                } else {
                    Swal.fire('错误', '用户信息加载失败', 'error');
                }
            },
            error: function() {
                $('#userInfoLoading').html('<span class="text-danger">加载失败</span>');
            }
        });
    }

    // ================= 2. 获取收货地址 =================
    function fetchDeliveryInfo() {
        $('#deliveryLoading').removeClass('d-none');
        $('#deliveryTableBody').empty();

        $.ajax({
            url: BASE_URL + '/api/AdminGetUserDeliveryInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUID }),
            success: function(res) {
                const list = (res.data && res.data.data) ? res.data.data : [];
                renderDeliveryTable(list);
            },
            complete: function() {
                $('#deliveryLoading').addClass('d-none');
            }
        });
    }

    function renderDeliveryTable(list) {
        const tbody = $('#deliveryTableBody');
        if (list.length === 0) {
            tbody.html('<tr><td colspan="8" class="text-center text-muted">暂无收货地址</td></tr>');
            return;
        }

        list.forEach((item, index) => {
            const rowData = encodeURIComponent(JSON.stringify(item));
            const html = `
                <tr>
                    <td>${index + 1}</td>
                    <td>${item.uContactPersonName}</td>
                    <td>${item.uContactPersonPhone}</td>
                    <td>${item.uDeliveryAddress}</td>
                    <td>${item.oPostalCode}</td>
                    <td><span class="d-inline-block text-truncate" style="max-width: 100px;">${item.uContactPersonEmail || ''}</span></td>
                    <td><span class="d-inline-block text-truncate" style="max-width: 100px;">${item.oDeliveryNote || ''}</span></td>
                    <td>
                        <button class="btn btn-sm btn-link text-primary p-0 me-2" onclick="openEditDialog('${rowData}')">编辑</button>
                        <button class="btn btn-sm btn-link text-danger p-0" onclick="handleDelete('${item.uDIndex}')">删除</button>
                    </td>
                </tr>
            `;
            tbody.append(html);
        });
    }

    // ================= 3. 获取订单记录 =================
    function fetchOrderInfo() {
        $('#orderLoading').removeClass('d-none');
        $('#orderTableBody').empty();

        $.ajax({
            url: BASE_URL + '/api/AdminGetUserOrderInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: currentUID }),
            success: function(res) {
                const list = (res.data && res.data.data) ? res.data.data : [];
                renderOrderTable(list);
            },
            complete: function() {
                $('#orderLoading').addClass('d-none');
            }
        });
    }

    function renderOrderTable(list) {
        const tbody = $('#orderTableBody');
        if (list.length === 0) {
            tbody.html('<tr><td colspan="5" class="text-center text-muted">暂无订单记录</td></tr>');
            return;
        }

        list.forEach(item => {
            const html = `
                <tr class="cursor-pointer" onclick="goToOrderDetail('${item.oOrderID}')">
                    <td>${item.oOrderID}</td>
                    <td>${item.oDate}</td>
                    <td><span class="badge bg-secondary">${item.oStatus}</span></td>
                    <td>${item.oReceiverName}</td>
                    <td><i class="fa-solid fa-angle-right text-muted"></i> 查看</td>
                </tr>
            `;
            tbody.append(html);
        });
    }

    // ================= 4. 地址管理逻辑 =================

    function openAddDialog() {
        isEditMode = false;
        $('#addressModalTitle').text('新增收货地址');
        $('#addressForm')[0].reset();
        $('#d_gender').val('男');
        const modal = new bootstrap.Modal(document.getElementById('addressModal'));
        modal.show();
    }

    function openEditDialog(encodedData) {
        isEditMode = true;
        $('#addressModalTitle').text('编辑收货地址');
        const data = JSON.parse(decodeURIComponent(encodedData));

        $('#d_uDIndex').val(data.uDIndex);
        $('#d_name').val(data.uContactPersonName);
        $('#d_phone').val(data.uContactPersonPhone);
        $('#d_gender').val(data.uContactPersonGender || '男');
        $('#d_address').val(data.uDeliveryAddress);
        $('#d_email').val(data.uContactPersonEmail);
        $('#d_zip').val(data.oPostalCode);
        $('#d_note').val(data.oDeliveryNote);

        const modal = new bootstrap.Modal(document.getElementById('addressModal'));
        modal.show();
    }

    function submitAddress() {
        const name = $('#d_name').val();
        const phone = $('#d_phone').val();
        const addr = $('#d_address').val();

        if(!name || !phone || !addr) {
            Swal.fire('提示', '请完善带 * 的必填项', 'warning');
            return;
        }

        const btn = $('#btnSaveAddress');
        btn.prop('disabled', true).text('提交中...');

        const payload = {
            uID: currentUID,
            uDIndex: isEditMode ? $('#d_uDIndex').val() : 0,
            uContactPersonName: name,
            uContactPersonPhone: phone,
            uDeliveryAddress: addr,
            uContactPersonGender: $('#d_gender').val(),
            uContactPersonEmail: $('#d_email').val(),
            oPostalCode: $('#d_zip').val(),
            oDeliveryNote: $('#d_note').val()
        };

        const url = isEditMode
            ? BASE_URL + '/api/AdminUserDeliveryInfoTableUpdate'
            : BASE_URL + '/api/AdminUserDeliveryInfoTableAdd';

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                if (res.data === 'Success' || res.data === 'success') {
                    Swal.fire('成功', isEditMode ? '修改成功' : '添加成功', 'success');
                    bootstrap.Modal.getInstance(document.getElementById('addressModal')).hide();
                    fetchDeliveryInfo();
                } else {
                    Swal.fire('失败', '操作失败', 'error');
                }
            },
            error: function() { Swal.fire('错误', '请求失败', 'error'); },
            complete: function() { btn.prop('disabled', false).text('确定'); }
        });
    }

    function handleDelete(index) {
        Swal.fire({
            title: '确定要删除这条收货地址吗？',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            confirmButtonText: '确定删除',
            cancelButtonText: '取消'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: BASE_URL + '/api/AdminUserDeliveryInfoTableDelete',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({ uID: currentUID, uDIndex: index }),
                    success: function(res) {
                        if (res.data === 'Success' || res.data === 'success') {
                            Swal.fire('已删除', '删除成功', 'success');
                            fetchDeliveryInfo();
                        } else {
                            Swal.fire('失败', '删除失败', 'error');
                        }
                    },
                    error: function() { Swal.fire('错误', '删除请求出错', 'error'); }
                });
            }
        });
    }

    // ================= 跳转逻辑 =================
    function goToOrderDetail(orderID) {
        window.location.href = 'AdminOrderDetailView.jsp?oOrderID=' + orderID;
    }

</script>

</body>
</html>