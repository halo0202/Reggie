package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;

import java.time.LocalDateTime;

public interface OrderService extends IService<Orders> {
    R<Page<Orders>> ordersPage(Long page, Long pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime);
}