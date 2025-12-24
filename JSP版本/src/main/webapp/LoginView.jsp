<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电子商务系统 - 登录</title>


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            background-color: #f0f2f5;
            /* 背景图 */
            background-image: url('https://picsum.photos/1920/1080');
            background-size: cover;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-card {
            width: 400px;
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            padding: 20px;
        }

        .main-title {
            position: absolute;
            top: 8%;
            left: 0;
            width: 100%;
            text-align: center;
            color: #FFD700;
            font-size: 3.5rem;
            font-weight: bold;
            text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.8);
            z-index: 10;
            letter-spacing: 2px;
            text-transform: uppercase;
        }

        .card-header {
            text-align: center;
            font-size: 20px;
            font-weight: bold;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #ebeef5;
        }

        .form-group {
            margin-bottom: 18px;
            position: relative;
        }


        .input-group-text {
            background-color: #fff;
            border-right: none;
            color: #909399;
        }

        .form-control {
            border-left: none;
        }

        .form-control:focus {
            box-shadow: none;
            border-color: #ced4da;
        }


        .input-group:focus-within .input-group-text,
        .input-group:focus-within .form-control {
            border-color: #409eff;
        }

        .login-button {
            width: 100%;
            font-weight: bold;
            padding: 10px;
            margin-top: 10px;
        }

        .form-footer {
            text-align: right;
            margin-top: 15px;
            font-size: 14px;
        }

        .form-footer a {
            text-decoration: none;
            cursor: pointer;
            display: inline-block;
            margin-left: 10px;
        }

        .link-primary { color: #409eff; }
        .link-warning { color: #e6a23c; }

        .hidden { display: none; }
        .error-text { color: #f56c6c; font-size: 12px; margin-top: 2px; display: none; }
    </style>
</head>
<body>


<h1 class="main-title">Ein tag wie gold</h1>

<div class="login-container">
    <div class="card login-card">
        <div class="card-header">
            <span id="titleText">电子商务系统</span>
        </div>

        <form id="loginForm" onsubmit="return false;">
            <!-- 公共区域 (账号)  -->
            <div class="form-group">
                <label class="form-label">账号</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                    <input type="text" class="form-control" id="uID" placeholder="请输入账号">
                </div>
                <div class="error-text" id="err-uID"></div>
            </div>

            <!-- 注册专用区域 (用户名) -->
            <div class="form-group register-only hidden">
                <label class="form-label">用户名</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-address-card"></i></span>
                    <input type="text" class="form-control" id="uNickName" placeholder="请输入用户名">
                </div>
                <div class="error-text" id="err-uNickName"></div>
            </div>

            <!-- 公共区域 (密码)  -->
            <div class="form-group">
                <label class="form-label">密码</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                    <input type="password" class="form-control" id="uPassword" placeholder="请输入密码">
                </div>
                <div class="error-text" id="err-uPassword"></div>
            </div>

            <!--  注册专用区域 (其他信息)  -->
            <div class="register-only hidden">
                <div class="form-group">
                    <label class="form-label">确认密码</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                        <input type="password" class="form-control" id="confirmPassword" placeholder="请再次输入密码">
                    </div>
                    <div class="error-text" id="err-confirmPassword"></div>
                </div>

                <div class="form-group">
                    <label class="form-label">手机号</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-mobile-screen"></i></span>
                        <input type="text" class="form-control" id="uPhone" placeholder="请输入手机号" maxlength="11">
                    </div>
                    <div class="error-text" id="err-uPhone"></div>
                </div>

                <div class="form-group">
                    <label class="form-label">Email</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                        <input type="email" class="form-control" id="uEmail" placeholder="请输入邮箱">
                    </div>
                    <div class="error-text" id="err-uEmail"></div>
                </div>

                <div class="form-group">
                    <label class="form-label">性别</label>
                    <select class="form-select" id="uGender">
                        <option value="" selected disabled>选择性别</option>
                        <option value="Male">男</option>
                        <option value="Female">女</option>
                    </select>
                    <div class="error-text" id="err-uGender"></div>
                </div>
            </div>

            <!-- 按钮区域  -->
            <button type="button" class="btn btn-primary login-button" id="submitBtn" onclick="handleSubmit()">
                登 录
            </button>

            <!--  底部链接区域 -->
            <div class="form-footer">
                <div id="loginLinks">
                    <a class="link-primary" onclick="switchToRegister('user')">没有账号？去注册</a>
                    <br>
                    <a class="link-warning" onclick="switchToRegister('merchant')" style="margin-top:5px; font-size:13px;">我是商户，申请入驻</a>
                </div>
                <div id="registerLinks" class="hidden">
                    <a class="link-primary" onclick="switchToLogin()">已有账号？去登录</a>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    // --- 状态变量 ---
    let isRegister = false;
    let registerType = 'user';

    //  切换逻辑
    function updateView() {
        const titleSpan = document.getElementById('titleText');
        const submitBtn = document.getElementById('submitBtn');
        const regFields = document.querySelectorAll('.register-only');
        const loginLinks = document.getElementById('loginLinks');
        const registerLinks = document.getElementById('registerLinks');
        const form = document.getElementById('loginForm');

        // 清除错误信息
        document.querySelectorAll('.error-text').forEach(el => el.style.display = 'none');

        if (!isRegister) {
            // 登录模式
            titleSpan.innerText = '电子商务系统';
            submitBtn.innerText = '登 录';
            regFields.forEach(el => el.classList.add('hidden'));
            loginLinks.classList.remove('hidden');
            registerLinks.classList.add('hidden');
        } else {
            // 注册模式
            titleSpan.innerText = registerType === 'merchant' ? '商户入驻注册' : '用户注册';
            submitBtn.innerText = '立 即 注 册';
            regFields.forEach(el => el.classList.remove('hidden'));
            loginLinks.classList.add('hidden');
            registerLinks.classList.remove('hidden');
        }
    }

    function switchToRegister(type) {
        isRegister = true;
        registerType = type;
        document.getElementById('loginForm').reset();
        updateView();
    }

    function switchToLogin() {
        isRegister = false;
        registerType = 'user';
        document.getElementById('loginForm').reset();
        updateView();
    }

    // 获取当前时间
    function getNowTime() {
        const date = new Date();
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
    }

    //验证逻辑
    function validateForm() {
        let isValid = true;
        const showError = (id, msg) => {
            const el = document.getElementById('err-' + id);
            el.innerText = msg;
            el.style.display = 'block';
            isValid = false;
        };
        const clearError = (id) => document.getElementById('err-' + id).style.display = 'none';

        // 获取值
        const uID = $('#uID').val();
        const uPassword = $('#uPassword').val();

        // 1. 账号验证
        if (!uID) showError('uID', '请输入账号');
        else if (uID.length < 3 || uID.length > 20) showError('uID', '长度在 3 到 20 个字符');
        else clearError('uID');

        // 2. 密码验证
        if (!uPassword) showError('uPassword', '请输入密码');
        else if (uPassword.length < 6) showError('uPassword', '密码长度不能少于 6 位');
        else clearError('uPassword');

        if (isRegister) {
            const uNickName = $('#uNickName').val();
            const confirmPass = $('#confirmPassword').val();
            const uPhone = $('#uPhone').val();
            const uEmail = $('#uEmail').val();
            const uGender = $('#uGender').val();

            // 用户名
            if (!uNickName) showError('uNickName', '请输入用户名'); else clearError('uNickName');

            // 确认密码
            if (!confirmPass) showError('confirmPassword', '请再次输入密码');
            else if (confirmPass !== uPassword) showError('confirmPassword', '两次输入密码不一致!');
            else clearError('confirmPassword');

            // 手机号 (正则+长度)
            if (!uPhone) showError('uPhone', '请输入手机号');
            else if (!/^\d+$/.test(uPhone)) showError('uPhone', '手机号只能由数字组成');
            else if (uPhone.length > 11) showError('uPhone', '手机号长度不能超过11位');
            else clearError('uPhone');

            // 邮箱
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!uEmail) showError('uEmail', '请输入邮箱');
            else if (!emailPattern.test(uEmail)) showError('uEmail', '邮箱格式不正确');
            else clearError('uEmail');

            // 性别
            if (!uGender) showError('uGender', '请选择性别'); else clearError('uGender');
        }

        return isValid;
    }

    //提交处理
    function handleSubmit() {
        if (!validateForm()) return;

        const btn = $('#submitBtn');
        btn.prop('disabled', true).text('处理中...');

        const uID = $('#uID').val();
        const uPassword = $('#uPassword').val();
        // SHA256 加密
        const hashedPassword = CryptoJS.SHA256(uPassword).toString();

        let url = '';
        let payload = {};

        // const baseUrl = '';

        const BASE_URL = 'http://localhost:8080';

        if (isRegister) {
            url =BASE_URL+'/Registration';
            const finalAccountType = registerType === 'merchant' ? '商户' : '普通用户';

            payload = {
                uID: uID,
                uNickName: $('#uNickName').val(),
                uPassword: hashedPassword,
                uPhone: $('#uPhone').val(),
                uEmail: $('#uEmail').val(),
                uGender: $('#uGender').val(),
                uRegisterDate: getNowTime(),
                uAccountType: finalAccountType,
                uAccountStatus: '正常'
            };
        } else {
            url = BASE_URL+'/Login_RequestBody';
            payload = {
                uID: uID,
                uPassword: hashedPassword
            };
        }

        console.log('Mode:', registerType, 'Payload:', payload);

        // 使用 Ajax 发送请求
        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(response) {
                const messageStr = (typeof response === 'object') ? JSON.stringify(response) : response;

                if (messageStr.includes('Registration Success')) {
                    Swal.fire('成功', messageStr, 'success').then(() => {
                        switchToLogin();
                    });
                } else if (messageStr.includes('Account Exist') || messageStr.includes('Registration Fail')) {
                    Swal.fire('错误', messageStr, 'error');
                } else if (messageStr.includes('管理员')) {
                    Swal.fire('成功', '欢迎, 用户 ' + uID, 'success').then(() => {
                        sessionStorage.setItem('uID', uID);
                        window.location.href = 'AdminView.jsp'; // 跳转到对应的 JSP 页面
                    });
                } else if (messageStr.includes('普通用户')) {
                    Swal.fire('成功', '欢迎, 用户 ' + uID, 'success').then(() => {
                        sessionStorage.setItem('uID', uID);
                        window.location.href = 'ShoppingnbView.jsp';
                    });
                } else if (messageStr.includes('商户')) {
                    Swal.fire('成功', '欢迎, 商户 ' + uID, 'success').then(() => {
                        sessionStorage.setItem('uID', uID);
                        window.location.href = 'MerchantView.jsp';
                    });
                } else if (messageStr.includes('无此类型账户') || messageStr.includes('No such Account') || messageStr.includes('Wrong Password')) {
                    Swal.fire('错误', messageStr, 'error');
                } else {
                    Swal.fire('提示', messageStr || '操作未完成', 'warning');
                }
            },
            error: function(xhr, status, error) {
                console.error('API Error:', error);
                Swal.fire('系统错误', '服务器内部错误或网络连接失败', 'error');
            },
            complete: function() {
                btn.prop('disabled', false).text(isRegister ? '立 即 注 册' : '登 录');
            }
        });
    }
</script>

</body>
</html>