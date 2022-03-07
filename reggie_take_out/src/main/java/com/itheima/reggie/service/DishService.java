package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.DishAddDto;
import com.itheima.reggie.common.dto.DishListDto;
import com.itheima.reggie.common.dto.DishUpdateDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService  extends IService<Dish> {
    R<String> addDish(DishAddDto dishAddDto);

    R<Page<DishListDto>> findDishPage(Long page, Long pageSize, String name);

    R<DishUpdateDto> findDishById(Long id);

    R<String> updateDish(DishUpdateDto dto);

    R<List<Dish>> findDishByCategoryId(Long categoryId ,Integer status);
}
