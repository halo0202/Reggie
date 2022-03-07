package com.itheima.reggie.service.impl;



import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.beans.beancontext.BeanContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    //添加购物车
    @Override
    public R<String> add(ShoppingCart shoppingCart) {

        //参数校验
        if (ObjectUtils.isEmpty(shoppingCart)){
            return R.error("数据有误");
        }
        Long currentId = BaseContext.getCurrentId();
        //核心业务处理
        shoppingCart.setUserId(currentId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        wrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getDishId()),ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(!ObjectUtils.isEmpty(shoppingCart.getSetmealId()),ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart cart = this.getOne(wrapper);
        if (cart!=null){
            cart.setNumber(cart.getNumber()+1);
            cart.setCreateTime(LocalDateTime.now());
            BaseContext.getCurrentId();

        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            cart = shoppingCart;
            BaseContext.getCurrentId();
        }

        this.saveOrUpdate(cart);

        //数据返回
        return R.success("购物车添加成功");
    }
    //购物车列表
    @Override
    public R<List<ShoppingCart>> shopingCatList() {
        //参数校验

        //核心业务处理
        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        //判断用户id登入时间是否超时
        if (ObjectUtils.isEmpty(currentId)){
            return R.error("用户登入超时");
        }
        LambdaQueryWrapper<ShoppingCart> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> shoppingCartList = this.list(wrapper);
        //数据返回
        return R.success(shoppingCartList);
    }
    //清空
    @Override
    public R<String> clean() {
        //参数校验

        //核心业务处理
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        boolean remove = this.remove(wrapper);
        //数据返回
        return R.success("删除成功");
    }

    @Override
    public R<String> delShoppingCart(ShoppingCart cart) {
        //参数校验
        if (ObjectUtils.isEmpty(cart)){
            return R.error("数据有误");
        }
        //业务逻辑分析
        //2.1确定当前线程id
        //2.2形参id与当前id匹配
        Long currentId = BaseContext.getCurrentId();
        cart.setUserId(currentId);
        //2.3判断当前是菜品还是套餐id
        //拿到后再与数据库校对
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        //菜品
        if (cart.getDishId()!=null){
            wrapper.eq(ShoppingCart::getDishId,cart.getDishId());
        }
        //套餐
        if (cart.getSetmealId()!=null){
            wrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }
        ShoppingCart dbshoppingCart = this.getOne(wrapper);
        //2.4判断封账数据是否存在
        if(ObjectUtils.isEmpty(dbshoppingCart)){
         return R.error("该数据不存在");
        }
        Integer number = dbshoppingCart.getNumber();
        //2.5如果为菜品,number为1,则设置唯0,若大于1,则减1;
        //2.5如果为套餐,number唯1,则设置为0,若大于1,则减1;
        if (number==1){
            dbshoppingCart.setNumber(0);
            //数据库中删除这条数据
            this.removeById(dbshoppingCart.getId());
        }else{
            dbshoppingCart.setNumber(number-1);
            //修改数据库数据
            this.updateById(dbshoppingCart);
        }

        //数据返回

        return R.success("删除成功");
    }

}