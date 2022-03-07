package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;

import java.util.List;

public interface CategoryService  extends IService<Category> {
    R<String> add(Category category);

    R<Page<Category>> findByPage(Long page, Long pageSize);

    R<String> del(Long id);

    R<String> updateCategory(Category category);

    R<List<Category>> findCategoryByType(Integer type);
}



