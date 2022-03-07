package com.itheima.reggie.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class DishListDto {

     // 不可以丢，丢了以后后边编辑，删除 功能没法处理！

    private Long id;

    //菜品名称
    private String name;
    //菜品价格
    private BigDecimal price;
    //图片
    private String image;
    //0 停售 1 起售
    private Integer status;
    private LocalDateTime updateTime;
    // 扩展一个字段 （前端分类名称）
    private String categoryName;


}
