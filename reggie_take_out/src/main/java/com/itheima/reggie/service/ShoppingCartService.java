package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    //购物车添加
    R<String> add(ShoppingCart shoppingCart);
    //列表显示
    R<List<ShoppingCart>>shopingCatList();
    //清空
    R<String> clean();
    //删除购物车商品
    R<String> delShoppingCart(ShoppingCart cart);
}