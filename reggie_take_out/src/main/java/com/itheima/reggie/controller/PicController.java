package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.common.exception.BuinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *  文件上传下载 控制器
 */
@RestController
@RequestMapping("/common")
public class PicController {

    @Value("${dish.pic.path}")
    private String path;  // D:\temp\pic


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        // 1、获取文件原始名称
        String originalFilename = file.getOriginalFilename(); // xxx.jpg
        // 2. 获取文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 3. 生成一个不重复文件名称
        String newFileName = UUID.randomUUID().toString();
        // 4. 构造一个带后缀名文件名
        StringBuilder stringBuilder = new StringBuilder();
        String newFileNameWithSuffix = stringBuilder.append(newFileName).append(suffix).toString();
        // 5、文件存贮  （D:\temp\pic）
        String filePath = new StringBuilder().append(path).append(newFileNameWithSuffix).toString();
        File dir = new File(path); // D:temp/pic
        // 6、如果目录不存在创建
        if (!dir.exists()){
            dir.mkdirs();
        }
        // 7、数据存储
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BuinessException("图片上传失败！");
        }
        return R.success(newFileNameWithSuffix);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(path + name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }







}
