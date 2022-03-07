package com.itheima.reggie.common.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.SetmealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealAddDto {

    //分类id
    private Long categoryId;

    //套餐名称
    private String name;

    //套餐价格
    private BigDecimal price;


    private Integer status;

    //描述信息
    private String description;

    //图片
    private String image;

    private List<SetmealDish> setmealDishes = new ArrayList<>();



}
