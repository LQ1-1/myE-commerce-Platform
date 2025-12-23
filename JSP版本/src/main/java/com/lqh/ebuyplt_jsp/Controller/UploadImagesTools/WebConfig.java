//package com.lqh.ebuyplt_1001p.Controller.UploadImagesTools;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.io.File;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer
//{
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry)
//    {
//        //获取当前项目的根目录
//        String projectRootPath = System.getProperty("user.dir");
//
//        //拼接路径
//        String uploadPath="file:"+projectRootPath+File.separator+"assets"+File.separator;
//
//        //建立映射关系,当前端访问localhost:8082/assets/images/***时，后端去src同级目录下去找文件
//        registry.addResourceHandler("/assets/**").addResourceLocations(uploadPath);
//
//        System.out.println("【静态资源映射】已配置: /images/** -> " + uploadPath);
//    }
//}
