package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.SMSUtils;
import com.itheima.reggie.common.ValidateCodeUtils;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        // 发送短信验证码 ：借助阿里云短信服务 （1、引入阿里依赖 2、使用工具类）
        String phone = user.getPhone();
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //  把验证码存入到 Session 中！
        session.setAttribute("code", code.toString());
        // SMSUtils.sendMessage(phone,code.toString());  // 问题：验证发送失败，自动登录！
        System.out.println("验证码是：" + code.toString());
        return R.success("发送验证码成功！");

    }
    // 知识点： 接收前端传递json数据2种方式 ① 创建一个对象 接收json  ② 使用Map集合收 （key-value）
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        // 前端传递的用户输入验证码
        String code = (String) map.get("code");
        // 从session获取验证码
        String codeInSession = (String) session.getAttribute("code");
        if (code.equals(codeInSession)){
            R<User> login = userService.login(map);
            User data = login.getData();
            if (!ObjectUtils.isEmpty(data)){
                Long id = data.getId();
                session.setAttribute("user",id);
                // 移除验证码！
                session.removeAttribute("code");
                return login;  //
            }
        }
        return R.error("登录失败！");
    }
    //退出登入接口
    @PostMapping("loginout")
    public R<String>loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}