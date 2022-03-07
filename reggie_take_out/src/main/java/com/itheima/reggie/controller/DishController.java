package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.DishAddDto;
import com.itheima.reggie.common.dto.DishListDto;
import com.itheima.reggie.common.dto.DishUpdateDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * dish 菜品
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> add(@RequestBody DishAddDto dishAddDto){
        return dishService.addDish(dishAddDto);
    }

    /**
     *  对菜品 分页条件查询
     */
    @GetMapping("/page")
    public R<Page<DishListDto>> findDishPage(Long page, Long pageSize, String name){
      return dishService.findDishPage(page,pageSize,name);
    }

    @GetMapping("/{id}")
    public R<DishUpdateDto> findDishById(@PathVariable("id") Long id){
       return dishService.findDishById(id);
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishUpdateDto dto){

        return dishService.updateDish(dto);
    }

    /**
     *  接口重构：
     *     ① 后台 不需要传递 状态
     *     ② 前台 需要传递状态
     * @param categoryId
     * @return
     */

    @GetMapping("/list")
    public R<List<Dish>> findDishByCategoryId(Long categoryId ,@RequestParam(required = false) Integer status){
       return dishService.findDishByCategoryId(categoryId,status);
    }


}
