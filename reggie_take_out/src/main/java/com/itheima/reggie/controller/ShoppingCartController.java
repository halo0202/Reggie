package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    //购物车添加
    @PostMapping("add")
    public R<String>add(@RequestBody ShoppingCart shoppingCart){

       return shoppingCartService.add(shoppingCart);
    }
    //查询购物车列表
    @GetMapping("list")
    public R<List<ShoppingCart>>shopingCatList(){
       return shoppingCartService.shopingCatList();
    }
    //清空
    @DeleteMapping("clean")
    public R<String>clean(){
      return shoppingCartService.clean();
    }
    //删除购物车
    @PostMapping("sub")
    public R<String>delShoppingCart(@RequestBody ShoppingCart Cart){

        return shoppingCartService.delShoppingCart(Cart);
    }
 }   