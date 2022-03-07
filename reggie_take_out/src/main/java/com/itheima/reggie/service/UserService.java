package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    R<User> login(Map map);
}