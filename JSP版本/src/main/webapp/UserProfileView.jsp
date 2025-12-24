<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人中心 - EBuyPlt</title>


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>

    <style>
        body {
            background-color: #f5f7fa;
            font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
            padding-bottom: 40px;
        }

        .user-profile-container {
            display: flex;
            justify-content: center;
            padding-top: 30px;
        }

        .content-wrapper {
            width: 800px;
            max-width: 95%;
        }

        .card {
            border: none;
            box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
            margin-bottom: 20px;
            border-radius: 8px;
        }

        .card-header {
            background-color: #fff;
            border-bottom: 1px solid #ebeef5;
            padding: 18px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 16px;
            font-weight: bold;
            color: #303133;
            border-radius: 8px 8px 0 0;
        }

        .card-body {
            padding: 20px;
        }

        .form-label {
            font-weight: 500;
            color: #606266;
        }

        .tips {
            font-size: 12px;
            color: #909399;
            margin-left: 10px;
        }

        .section-divider {
            margin: 30px 0 20px 0;
            border-top: 1px solid #dcdfe6;
            position: relative;
        }

        .section-divider-text {
            position: absolute;
            top: -12px;
            left: 20px;
            background: #fff;
            padding: 0 10px;
            color: #303133;
            font-weight: 500;
            font-size: 14px;
        }

        /* Table styles */
        .table thead th {
            background-color: #f5f7fa;
            color: #909399;
            font-weight: 500;
        }
    </style>
</head>
<body>

<!-- 顶部简单的导航栏 -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary" href="ShoppingnbView.jsp">EBuyPlt</a>
        <div class="ms-auto">
            <a href="ShoppingnbView.jsp" class="btn btn-outline-secondary btn-sm">返回商城</a>
        </div>
    </div>
</nav>

<div class="user-profile-container">
    <div class="content-wrapper">

        <!-- 卡片1：个人基础信息 -->
        <div class="card">
            <div class="card-header">
                <span>个人用户信息</span>
                <button class="btn btn-sm btn-outline-secondary" onclick="history.back()">返回</button>
            </div>
            <div class="card-body">
                <form id="userInfoForm">
                    <!-- 基本信息行 -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">用户ID</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="uID" disabled>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">昵称 <span class="text-danger">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="uNickName" placeholder="请输入昵称">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">电话</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="uPhone" placeholder="请输入电话号码">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">邮箱</label>
                        <div class="col-sm-9">
                            <input type="email" class="form-control" id="uEmail" placeholder="请输入邮箱">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">性别</label>
                        <div class="col-sm-9">
                            <select class="form-select" id="uGender">
                                <option value="" selected disabled>请选择</option>
                                <option value="男">男</option>
                                <option value="女">女</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">注册时间</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="uRegisterDate" disabled>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">账户类型</label>
                        <div class="col-sm-9 d-flex align-items-center">
                            <span class="badge bg-secondary me-2" id="uAccountType">类型</span>
                            <span class="tips">（不可修改）</span>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">账户状态</label>
                        <div class="col-sm-9 d-flex align-items-center">
                            <span class="badge bg-success" id="uAccountStatus">正常</span>
                            <span class="tips">（不可修改）</span>
                        </div>
                    </div>

                    <!-- 密码修改区 -->
                    <div class="section-divider">
                        <span class="section-divider-text">如需修改密码请填写下方</span>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label text-end">新密码</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="newPassword" placeholder="不修改请留空">
                        </div>
                    </div>

                    <div class="row mb-3" id="confirmPassRow" style="display:none;">
                        <label class="col-sm-3 col-form-label text-end">确认新密码</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="confirmPassword" placeholder="请再次输入新密码">
                        </div>
                    </div>

                    <div class="row mt-4">
                        <div class="col-sm-9 offset-sm-3">
                            <button type="button" class="btn btn-primary" id="btnSaveInfo" onclick="submitUserInfo()">保存基础信息</button>
                            <button type="button" class="btn btn-secondary ms-2" onclick="fetchUserInfo(currentUserID)">重置</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- 卡片2：收货地址管理 -->
        <div class="card">
            <div class="card-header">
                <span>收货地址管理</span>
                <button class="btn btn-sm btn-primary" onclick="openAddDialog()">新增收货地址</button>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover align-middle">
                        <thead>
                        <tr>
                            <th>收货人</th>
                            <th>联系电话</th>
                            <th>收货地址</th>
                            <th>邮编</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="deliveryTableBody">
                        <!-- JS动态填充 -->
                        </tbody>
                    </table>
                </div>
                <div id="loadingDelivery" class="text-center py-3 d-none">
                    <div class="spinner-border text-primary" role="status"></div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- 弹窗：新增/编辑收货地址 -->
<div class="modal fade" id="deliveryModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deliveryModalTitle">新增收货地址</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="deliveryForm">
                    <!-- 隐藏域: Index 和 ID -->
                    <input type="hidden" id="d_uDIndex">

                    <div class="mb-3">
                        <label class="form-label">收货人姓名 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="d_name" placeholder="请输入收货人姓名">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">联系电话 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="d_phone" placeholder="请输入联系电话">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">收货地址 <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="d_address" rows="2" placeholder="请输入详细地址"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">性别</label>
                        <select class="form-select" id="d_gender">
                            <option value="Male">男</option>
                            <option value="Female">女</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">邮箱</label>
                        <input type="email" class="form-control" id="d_email" placeholder="请输入收货人邮箱">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">邮编</label>
                        <input type="text" class="form-control" id="d_zip" placeholder="请输入邮政编码">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">备注</label>
                        <input type="text" class="form-control" id="d_note" placeholder="送货备注信息">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btnSaveDelivery" onclick="submitDeliveryForm()">确定</button>
            </div>
        </div>
    </div>
</div>

<script>
    //配置
    // const BASE_URL = 'http://localhost:8080/EBuyPlt_JSP_war';
    const BASE_URL = 'http://localhost:8080';

    // 全局变量
    let currentUserID = '';
    let currentUserPasswordHash = ''; // 存储原始密码hash，用于不修改密码时提交
    let isEditMode = false;

    //初始化
    $(document).ready(function() {
        // 1. 获取 URL 参数 uID，如果没有则尝试 sessionStorage
        const urlParams = new URLSearchParams(window.location.search);
        let targetID = urlParams.get('uID');
        if (!targetID) {
            targetID = sessionStorage.getItem('uID');
        }

        if (targetID) {
            currentUserID = targetID;
            fetchUserInfo(targetID);
            fetchDeliveryList(targetID);
        } else {
            Swal.fire('错误', '未找到用户ID参数', 'error').then(() => {
                window.location.href = 'index.jsp'; // 回到首页
            });
        }

        // 监听新密码输入，显示确认密码框
        $('#newPassword').on('input', function() {
            if($(this).val()) {
                $('#confirmPassRow').slideDown();
            } else {
                $('#confirmPassRow').slideUp();
            }
        });
    });

    //用户基础信息逻辑

    function fetchUserInfo(id) {
        // loading state
        $('#btnSaveInfo').prop('disabled', true).text('加载中...');

        $.ajax({
            url: BASE_URL + '/api/GetUserAccountInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: id }),
            success: function(res) {
                if (res.code === 200 && res.data) {
                    const data = res.data;
                    $('#uID').val(data.uID);
                    $('#uNickName').val(data.uNickName);
                    $('#uPhone').val(data.uPhone);
                    $('#uEmail').val(data.uEmail);
                    $('#uGender').val(data.uGender); // "男" 或 "女"
                    $('#uRegisterDate').val(data.uRegisterDate);

                    $('#uAccountType').text(data.uAccountType);

                    const status = data.uAccountStatus;
                    const statusEl = $('#uAccountStatus');
                    statusEl.text(status);
                    if(status === '正常') {
                        statusEl.removeClass('bg-danger').addClass('bg-success');
                    } else {
                        statusEl.removeClass('bg-success').addClass('bg-danger');
                    }

                    // 保存原始密码hash，如果用户不改密码，提交时需要用到
                    currentUserPasswordHash = data.uPassword;

                    // 重置密码框
                    $('#newPassword').val('');
                    $('#confirmPassword').val('');
                    $('#confirmPassRow').hide();
                } else {
                    Swal.fire('错误', res.message || '获取用户信息失败', 'error');
                }
            },
            error: function() {
                Swal.fire('错误', '网络请求错误', 'error');
            },
            complete: function() {
                $('#btnSaveInfo').prop('disabled', false).text('保存基础信息');
            }
        });
    }

    function submitUserInfo() {
        const nickName = $('#uNickName').val();
        if(!nickName) {
            Swal.fire('提示', '请输入昵称', 'warning');
            return;
        }

        // 密码处理
        const newPass = $('#newPassword').val();
        const confirmPass = $('#confirmPassword').val();
        let finalPasswordHash = currentUserPasswordHash;

        if (newPass) {
            if (newPass.length < 6) {
                Swal.fire('提示', '新密码长度不能少于 6 位', 'warning');
                return;
            }
            if (newPass !== confirmPass) {
                Swal.fire('提示', '两次输入的密码不一致!', 'warning');
                return;
            }
            // SHA256 加密
            finalPasswordHash = CryptoJS.SHA256(newPass).toString();
        }

        const payload = {
            uID: $('#uID').val(),
            uNickName: nickName,
            uPhone: $('#uPhone').val(),
            uEmail: $('#uEmail').val(),
            uGender: $('#uGender').val(),
            uRegisterDate: $('#uRegisterDate').val(),
            uAccountType: $('#uAccountType').text(),
            uAccountStatus: $('#uAccountStatus').text(),
            uPassword: finalPasswordHash
        };

        $('#btnSaveInfo').prop('disabled', true).text('保存中...');

        $.ajax({
            url: BASE_URL + '/api/SetUserAccountInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                if (res === 'Success' || res === 'success' || (res.code && res.code === 200)) {
                    Swal.fire('成功', '用户信息更新成功', 'success');
                    // 刷新数据（特别是如果改了密码，更新本地hash）
                    fetchUserInfo(currentUserID);
                } else {
                    Swal.fire('失败', '更新失败，请重试', 'error');
                }
            },
            error: function() {
                Swal.fire('错误', '更新请求发生错误', 'error');
            },
            complete: function() {
                $('#btnSaveInfo').prop('disabled', false).text('保存基础信息');
            }
        });
    }

    //收货地址逻辑

    function fetchDeliveryList(uid) {
        $('#deliveryTableBody').empty();
        $('#loadingDelivery').removeClass('d-none');

        $.ajax({
            url: BASE_URL + '/api/GetUserDeliveryInfo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ uID: uid }),
            success: function(res) {
                const list = (res.code === 200 && res.data) ? res.data : [];
                renderDeliveryTable(list);
            },
            complete: function() {
                $('#loadingDelivery').addClass('d-none');
            }
        });
    }

    function renderDeliveryTable(list) {
        const tbody = $('#deliveryTableBody');
        tbody.empty();

        if (list.length === 0) {
            tbody.append('<tr><td colspan="6" class="text-center text-muted">暂无收货地址</td></tr>');
            return;
        }

        list.forEach(item => {
            const itemData = encodeURIComponent(JSON.stringify(item));

            const html = `
                <tr>
                    <td>${item.uContactPersonName || ''}</td>
                    <td>${item.uContactPersonPhone || ''}</td>
                    <td>${item.uDeliveryAddress || ''}</td>
                    <td>${item.oPostalCode || ''}</td>
                    <td class="text-truncate" style="max-width: 150px;" title="${item.oDeliveryNote || ''}">${item.oDeliveryNote || ''}</td>
                    <td>
                        <button class="btn btn-sm btn-link text-primary p-0 me-2" onclick="openEditDialog('${itemData}')">编辑</button>
                        <button class="btn btn-sm btn-link text-danger p-0" onclick="deleteDelivery('${item.uID}', ${item.uDIndex})">删除</button>
                    </td>
                </tr>
            `;
            tbody.append(html);
        });
    }

    // 打开新增弹窗
    function openAddDialog() {
        isEditMode = false;
        $('#deliveryModalTitle').text('新增收货地址');
        $('#deliveryForm')[0].reset();

        // 默认值
        $('#d_gender').val('Male');

        const modal = new bootstrap.Modal(document.getElementById('deliveryModal'));
        modal.show();
    }

    // 打开编辑弹窗
    function openEditDialog(itemDataEncoded) {
        isEditMode = true;
        $('#deliveryModalTitle').text('编辑收货地址');

        const item = JSON.parse(decodeURIComponent(itemDataEncoded));

        // 填充表单
        $('#d_uDIndex').val(item.uDIndex);
        $('#d_name').val(item.uContactPersonName);
        $('#d_phone').val(item.uContactPersonPhone);
        $('#d_address').val(item.uDeliveryAddress);
        $('#d_gender').val(item.uContactPersonGender || 'Male');
        $('#d_email').val(item.uContactPersonEmail);
        $('#d_zip').val(item.oPostalCode);
        $('#d_note').val(item.oDeliveryNote);

        const modal = new bootstrap.Modal(document.getElementById('deliveryModal'));
        modal.show();
    }

    // 提交收货地址
    function submitDeliveryForm() {
        const name = $('#d_name').val();
        const phone = $('#d_phone').val();
        const address = $('#d_address').val();

        if(!name || !phone || !address) {
            Swal.fire('提示', '请完善带 * 的必填项', 'warning');
            return;
        }

        const btn = $('#btnSaveDelivery');
        btn.prop('disabled', true).text('提交中...');

        const payload = {
            uID: currentUserID,
            uDIndex: isEditMode ? parseInt($('#d_uDIndex').val()) : 0, // 新增时通常由后端生成或为0
            uContactPersonName: name,
            uContactPersonPhone: phone,
            uDeliveryAddress: address,
            uContactPersonGender: $('#d_gender').val(),
            uContactPersonEmail: $('#d_email').val(),
            oPostalCode: $('#d_zip').val(),
            oDeliveryNote: $('#d_note').val()
        };

        const url = isEditMode ?
            (BASE_URL + '/api/SetUserDeliveryInfo') :
            (BASE_URL + '/api/AddUserDeliveryInfo');

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                if (res === 'Success' || res === 'success') {
                    Swal.fire('成功', isEditMode ? '修改地址成功' : '添加地址成功', 'success');
                    // 关闭弹窗
                    const modalEl = document.getElementById('deliveryModal');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                    // 刷新列表
                    fetchDeliveryList(currentUserID);
                } else {
                    Swal.fire('失败', '操作失败', 'error');
                }
            },
            error: function() {
                Swal.fire('错误', '请求发生错误', 'error');
            },
            complete: function() {
                btn.prop('disabled', false).text('确定');
            }
        });
    }

    // 删除收货地址
    function deleteDelivery(uid, index) {
        Swal.fire({
            title: '确定要删除这条收货地址吗？',
            text: "此操作不可恢复。",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: '确定删除',
            cancelButtonText: '取消'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: BASE_URL + '/api/DeleteUserDeliveryInfo',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({ uID: uid, uDIndex: index }),
                    success: function(res) {
                        if (res === 'Success' || res === 'success') {
                            Swal.fire('已删除', '删除成功', 'success');
                            fetchDeliveryList(currentUserID);
                        } else {
                            Swal.fire('失败', '删除失败', 'error');
                        }
                    },
                    error: function() {
                        Swal.fire('错误', '删除请求出错', 'error');
                    }
                });
            }
        });
    }

</script>

</body>
</html>