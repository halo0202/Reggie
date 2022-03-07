package com.itheima.reggie.controller;

import com.itheima.reggie.entity.Category;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;  // springmvc

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/common")
public class TestFileUploadController {


    // javaweb  : http 协议规定 ：
    //  一个请求发出去以后，必须有一个响应（哪怕是一个错误响应）


    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile uploadFile, HttpServletResponse response) throws IOException {

        // 都是将前端传递文件转成 字节，输入流，resource
        byte[] bytes = uploadFile.getBytes();
        InputStream inputStream = uploadFile.getInputStream();
        Resource resource = uploadFile.getResource();
        // 上传文件类型
        String contentType = uploadFile.getContentType();
        System.out.println("文件类型：" + contentType);
        // 上传文件大小
        long size = uploadFile.getSize();
        System.out.println("文件大小：" + size + "字节");
        //  文件名称  前端提交的 input表单项 name
        String name = uploadFile.getName();
        System.out.println("文件名称" + name);
        // 查看文件是否为空
        boolean empty = uploadFile.isEmpty();
        System.out.println("文件内容是否为空" + empty);


        // 获取原始文件名
        String originalFilename = uploadFile.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFilename);
        // transferTo 存贮到哪个位置
        File file = new File("D:\\" + originalFilename);
        uploadFile.transferTo(file);
        response.setCharacterEncoding("UTF-8");
        return "success！"; // 返回视图：页面，文本，json

    }

    @PostMapping("/uploads")
    @ResponseBody
    public String upload(MultipartFile[] uploadFiles) throws IOException {

        for (MultipartFile uploadFile : uploadFiles) {
            String originalFilename = uploadFile.getOriginalFilename();
            System.out.println(originalFilename);
        }

        return "success!"; // 返回视图：页面，文本，json

    }

    @PostMapping("/uploads1")
    @ResponseBody
    public String upload(@RequestParam List<MultipartFile> uploadFiles) throws IOException {

        for (MultipartFile uploadFile : uploadFiles) {
            String originalFilename = uploadFile.getOriginalFilename();
            System.out.println(originalFilename);
        }

        return "success!"; // 返回视图：页面，文本，json

    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {

        // 回忆
        // PrintWriter writer = response.getWriter();  // 字符流
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream(); // 字节流

        // 1. 创建文件
        File file = new File("D:\\" + name);
        // 2. 文件转成字节流向浏览器输出 [实际开发有工具类！]
        // 2.1 文件转文件包装流
        FileInputStream fileInputStream = new FileInputStream(file);

        // 告诉浏览器以图片格式解析
        response.setContentType("image/jpeg");

        int len = 0;
        byte[] bytes = new byte[1024];  // 1KB
        while ((len = fileInputStream.read(bytes)) != -1) {
            // 像浏览器输出字节！
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }

        // 核心代码！（像浏览器输出字节---》文件输入流---》字节---》缓冲）
        //  outputStream.write(b);


        // 使用工具类 commons-io 工具类

        // 1\我要知道我干什么？
        // 2\谁能帮助我干这件事
        // 3\开始干活 （已知条件 ，求结果）

        // 文件复制
        //   FileUtils.copyFile();
        // 1、文件转字节
      /*  FileUtils.readFileToByteArray();

        // 2. 字节转文件
        FileUtils.writeByteArrayToFile();

        // 3. 流转成字节
        IOUtils.readFully();
        // 4. 字节转成流*/


        List<Category> categories = new ArrayList<>();
        List<Category> collect = categories.stream().map((category) -> {
            category.setCreateUser(1L);
            return category;
        }).collect(Collectors.toList());

    }


}
