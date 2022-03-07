package com.itheima.reggie.common.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SetmealDishFrontDto {

  //  显示不需要的，但是后期加入购物有用！
    private Long id;

    //套餐id
    private Long setmealId;

    //菜品id
    private Long dishId;

    ///////////////////////////以上三个需要保留字段///////////////////////////

    //菜品名称 （冗余字段）
    private String name;

    //菜品原价
    private BigDecimal price;

    //份数
    private Integer copies;

    private Integer sort;

    //  菜品销量

    private Integer  sell;
}
