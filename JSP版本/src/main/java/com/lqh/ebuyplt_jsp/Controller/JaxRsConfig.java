package com.lqh.ebuyplt_jsp.Controller;

import com.lqh.ebuyplt_jsp.Controller.JSPVersionController.BasicController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

// 配置JAX-RS的根路径为/api（所有接口URL都要带/api前缀）
@ApplicationPath("/api")
public class JaxRsConfig extends Application {
    // 注册需要被扫描的接口类（把BasicController加进来）
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(BasicController.class); // 核心：注册你的接口类
        return classes;
    }
}