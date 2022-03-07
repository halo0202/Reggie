package com.itheima.reggie.common.dto;

import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishUpdateDto {

     // 不可以丢，丢了以后后边编辑，删除 功能没法处理！

    private Long id;

    private Long categoryId;

    //菜品名称
    private String name;
    //菜品价格
    private BigDecimal price;
    //图片
    private String image;
    //0 停售 1 起售
    private Integer status;
    private LocalDateTime updateTime;
    private String description;
    // 口味列表
    List<DishFlavor> flavors = new ArrayList<>();


}
