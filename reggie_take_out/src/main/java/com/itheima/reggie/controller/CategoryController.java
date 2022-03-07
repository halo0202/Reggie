package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> add(@RequestBody Category category){
      return categoryService.add(category);
    }

    @GetMapping("/page")
    public R<Page<Category>>  findByPage(Long page, Long pageSize){
       return categoryService.findByPage(page,pageSize);
    }

    @DeleteMapping
    public R<String> del(Long id){
       return  categoryService.del(id);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    /**
     *   接口适配梁总需求：
     *     ① 后台 ： 根据类型查询分类 （type 必须传递）
     *     ② 前台 ：所有的分类信息都展示出来  （不传type）
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> findCategoryByType(@RequestParam(required = false) Integer type ){
          return categoryService.findCategoryByType(type);
    }




}
