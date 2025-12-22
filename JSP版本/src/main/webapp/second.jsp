<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>第二个界面</title>
    <style>
        /* 给第二个界面换个背景色，方便区分 */
        body { background-color: #e0f7fa; padding: 50px; font-family: sans-serif; }
        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        button {
            padding: 10px 20px;
            background-color: #ff9800; /* 橙色按钮 */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="card">
    <h1>这是第二个界面 (Page 2)</h1>

    <!-- 跳转链接 -->
    <p><a href="index.jsp">⬅️ 返回首页 (index.jsp)</a></p>

    <hr>

    <p>在这个页面，我们依然可以请求同一个后台接口：</p>
    <button onclick="callApi()">API测试</button>

    <p id="msg" style="color: gray;">等待操作...</p>
</div>

<script>
    async function callApi() {
        // 这里的 URL 和 index.jsp 里用的一模一样
        // 因为后台接口是共享的，谁都可以调
        fetch("api/Test")
            .then(resp => resp.text())
            .then(data => {
                alert(data)
                document.getElementById("msg").innerText = "第二个页面收到数据：" + data;
                document.getElementById("msg").style.color = "green";
            });
    }
</script>

</body>
</html>