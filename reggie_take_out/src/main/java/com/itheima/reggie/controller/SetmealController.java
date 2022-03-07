package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.SetmealAddDto;
import com.itheima.reggie.common.dto.SetmealDishFrontDto;
import com.itheima.reggie.common.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;


    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealAddDto dto){
        return setmealService.addSetmeal(dto);
    }
    @GetMapping("/page")
    public R<Page<SetmealDto>> findByPage(Long page, Long pageSize, String name){
        return setmealService.findByPage(page,pageSize,name);
    }
    @DeleteMapping
    public R<String> del(@RequestParam List<Long> ids){
        return setmealService.del(ids);
    }

    /**
     *   根据套餐分类的id （status）查询 套餐信息！
     * @param categoryId
     * @param status
     * @return
     */

    @GetMapping("/list")
    public  R<List<Setmeal>> getSetmealList(Long categoryId, Integer status){
      return     setmealService.getSetmealList(categoryId,status);
    }

    @GetMapping("/dish/{setmealId}")
    public R<List<SetmealDish>> getSetmealDishList(@PathVariable Long setmealId){
        return setmealService.getSetmealDishList(setmealId);
    }


}
