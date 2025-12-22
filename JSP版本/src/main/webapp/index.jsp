<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  注意：Tomcat 10+ (Tomcat 11) 使用 Jakarta EE。
  虽然这个简单的 JSP 没有引用 java 类，但如果以后要 import，
  请记得使用 jakarta.servlet.* 而不是 javax.servlet.*
--%>
<!DOCTYPE html>
<html>
<head>
    <title>JSP 项目启动成功</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 { color: #2c3e50; }
        p { color: #7f8c8d; }
        .info {
            margin-top: 20px;
            padding: 15px;
            background-color: #e8f6f3;
            border: 1px solid #a2d9ce;
            border-radius: 5px;
            color: #16a085;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>🎉 恭喜！</h1>
    <h3>你的 JSP 项目已成功运行</h3>

    <div class="info">
        当前时间：<%= new java.util.Date() %>
    </div>

    <div class="button-field">
        <button onclick="NextButtonClick()">Next</button>
    </div>

    <p>服务器版本：<%= application.getServerInfo() %></p>
    <p>Servlet 规范版本：<%= application.getMajorVersion() %>.<%= application.getMinorVersion() %></p>
</div>

<script>
    function NextButtonClick(){
        window.location.href="second.jsp";
    }
</script>

</body>
</html>