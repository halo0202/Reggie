package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Override
    public R<Page<Orders>> ordersPage(Long page, Long pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        //参数校验
        if (ObjectUtils.isEmpty(page)||page<=0){
            page=1L;
        }
        if (ObjectUtils.isEmpty(pageSize)||pageSize<=0){
            pageSize=10L;
        }
        //核心业务处理
        //2.1 获取数据库数据 page,wrapper
        //2.2判断订单号与订单数据是否一致
        Page<Orders> pageParam = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!ObjectUtils.isEmpty(number),Orders::getNumber,number);
        Page<Orders> ordersPage = this.page(pageParam, wrapper);
        wrapper.between(Orders::getOrderTime,beginTime,endTime);
        //数据返回
        return R.success(ordersPage);
    }
}