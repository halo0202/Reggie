package com.itheima.reggie.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SetmealDto {

    private Long id;  // 为了后面修改删除准备！

    //套餐名称
    private String name;

    //套餐价格
    private BigDecimal price;

    //状态 0:停用 1:启用
    private Integer status;

    //图片
    private String image;

    private LocalDateTime updateTime;

    private String categoryName;  // 必须与前端一致！

}
