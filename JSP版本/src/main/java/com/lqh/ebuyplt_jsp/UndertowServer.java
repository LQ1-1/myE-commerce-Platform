package com.lqh.ebuyplt_jsp;

import io.undertow.Undertow;
import io.undertow.Handlers;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.server.handlers.CorsHandler; // 新增跨域处理器导入
import java.io.File;
import java.util.Collections;

public class UndertowServer {
    public static void main(String[] args) throws Exception {
        // 基础配置（保持不变）
        int port = 8080;
        String contextPath = "/";
        String webAppDir = "src/main/webapp";
        File webAppRoot = new File(webAppDir);

        if (!webAppRoot.exists()) {
            System.err.println("❌ Web根目录不存在：" + webAppRoot.getAbsolutePath());
            return;
        }

        // 1. 创建Undertow Servlet容器
        ServletContainer servletContainer = ServletContainer.Factory.newInstance();

        // 2. 部署webapp目录（自动扫描WEB-INF/web.xml）
        DeploymentInfo deploymentInfo = Servlets.deployment()
                .setClassLoader(UndertowServer.class.getClassLoader())
                .setContextPath(contextPath)
                .setDeploymentName("EBuyPlt_JSP")
                .setResourceManager(new FileResourceManager(webAppRoot, 1024))
                .setDefaultEncoding("UTF-8");

        // 3. 部署+启动
        DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
        deploymentManager.deploy();

        PathHandler rootHandler = Handlers.path()
                .addPrefixPath(contextPath, deploymentManager.start());

        // ========== 新增：Undertow跨域处理器（兜底解决403） ==========
        CorsHandler corsHandler = new CorsHandler(Collections.singletonList("*"))
                .setAllowedMethods("GET, POST, OPTIONS")
                .setAllowedHeaders("Content-Type, Accept")
                .setAllowCredentials(true)
                .setHandler(rootHandler);

        // 4. 启动服务器（使用corsHandler包装rootHandler）
        Undertow server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setHandler(corsHandler) // 替换为corsHandler
                .build();

        server.start();
        System.out.println("✅ 服务器启动成功！");
        System.out.println("🌐 地址：http://localhost:" + port);
        System.out.println("📌 你的登录接口：http://localhost:" + port + "/Login_RequestBody");
        System.out.println("📌 你的测试接口：http://localhost:" + port + "/Hello");
    }
}