package com.itheima.reggie.common.dto;

import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  dto 存在目的就是为了业务解耦合
 */
@Data
public class DishAddDto {

    //菜品名称
    private String name;

    //菜品分类id
    private Long categoryId;

    //菜品价格
    private BigDecimal price;

    //商品码
    private String code;

    //图片
    private String image;

    //描述信息
    private String description;

    //0 停售 1 起售
    private Integer status;

    private List<DishFlavor> flavors = new ArrayList<>();
}
