package com.lqh.ebuyplt_jsp.Controller.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

// 全局过滤所有请求
@WebFilter("/*")
public class CustomCharacterEncodingFilter implements Filter {
    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可选：从配置读取编码（若需要）
        if (filterConfig.getInitParameter("encoding") != null) {
            encoding = filterConfig.getInitParameter("encoding");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 设置请求编码
        request.setCharacterEncoding(encoding);
        // 设置响应编码
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html;charset=" + encoding);
        // 继续执行后续过滤器/Servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 空实现即可
    }
}