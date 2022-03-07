package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.SetmealAddDto;
import com.itheima.reggie.common.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<String> addSetmeal(SetmealAddDto dto);

    R<Page<SetmealDto>> findByPage(Long page, Long pageSize, String name);

    R<String> del(List<Long> ids);

    R<List<Setmeal>> getSetmealList(Long categoryId, Integer status);

    R<List<SetmealDish>> getSetmealDishList(Long setmealId);

}
