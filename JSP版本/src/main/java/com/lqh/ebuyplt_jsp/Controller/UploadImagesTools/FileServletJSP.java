package com.lqh.ebuyplt_jsp.Controller.UploadImagesTools;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/assets/*")
public class FileServletJSP extends HttpServlet {
    // 本地assets文件夹的根路径（和原WebConfig保持一致：项目根目录/assets/）
    private String localAssetsRoot;

    @Override
    public void init() throws ServletException
    {
        String projectRootPath = System.getProperty("user.dir");
        localAssetsRoot = projectRootPath + File.separator + "assets" + File.separator;

        System.out.println("【静态资源映射】已配置: /assets/** -> " + localAssetsRoot);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "请求路径不能为空");
            return;
        }

        File targetFile = new File(localAssetsRoot + pathInfo);

        if (!targetFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在：" + targetFile.getAbsolutePath());
            return;
        }
        if (targetFile.isDirectory()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "不能访问文件夹：" + targetFile.getAbsolutePath());
            return;
        }

        String mimeType = getServletContext().getMimeType(targetFile.getName());
        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        response.setContentLength((int) targetFile.length()); // 设置文件大小

        try (FileInputStream fis = new FileInputStream(targetFile);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "文件读取失败：" + e.getMessage());
        }
    }
}