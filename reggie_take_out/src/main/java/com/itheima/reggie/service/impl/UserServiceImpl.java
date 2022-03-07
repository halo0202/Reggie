package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 验证码登录逻辑：
     * ① 根据用户手机号 查询数据库 看用户是否存在
     * ② 存在 ，不做操作
     * ③ 不存在, 直接插入到数据
     *
     * @param map
     */

    @Override
    public R<User> login(Map map) {

        String phone = (String) map.get("phone");
        // ① 根据用户手机号 查询数据库 看用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User dbUser = this.getOne(wrapper);
       // 不存在, 直接插入到数据
        if (ObjectUtils.isEmpty(dbUser)) {
            // 插入操作
            dbUser = new User();
            dbUser.setPhone(phone);
            this.saveOrUpdate(dbUser);
        }
        // 数据脱敏
        dbUser.setIdNumber("");
        return R.success(dbUser);
    }
}