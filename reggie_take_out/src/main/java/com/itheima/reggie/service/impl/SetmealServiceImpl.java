package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.SetmealAddDto;
import com.itheima.reggie.common.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;

    @Transactional
    @Override
    public R<String> addSetmeal(SetmealAddDto dto) {

        // 1.


        //2. 核心逻辑 ： 数据封装保存到两张表 ： setmeal  套餐表表   setmeal_dish 套餐菜品表

        // 2.1 套餐数据 剥离出来 保存到 套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto,setmeal);
        // 主键返回！
        this.saveOrUpdate(setmeal); // insert

        // 2.2 套餐菜品 数据剥离 保存到 套餐菜品表
        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        // 套餐菜品 列表 数据 有没有 套餐id 没有！ 设置套餐 id
        // 方式1  : Java 基础中 迭代其中不可以 自己迭代以后再放入自己！
      // List<SetmealDish>  res = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
          //  setmealDishes.add(setmealDish);
         //   res.add(setmealDish);
        }
        // 保存套餐菜品到数据库 使用 套餐菜品service  没有创建！
        setmealDishService.saveBatch(setmealDishes);  // insert
        //3.r
        return R.success("添加成功！");
    }

    @Override
    public R<Page<SetmealDto>> findByPage(Long page, Long pageSize, String name) {
        // 1. 参数校验
        if (ObjectUtils.isEmpty(page) || page <= 0) {
            page = 1L;
        }
        if (ObjectUtils.isEmpty(pageSize) || pageSize <= 0) {
            pageSize = 10L;
        }
        // 2、 核心业务 ：查询两张表！  套餐表 setmeal   分类表 ：category
        Page<Setmeal> pageParam = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        // 2.1. 名称条件
        wrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name);
        // 2. 修改时间 倒序
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> setmealPage = this.page(pageParam, wrapper);
        // 获取套餐 集合
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        // 遍历套餐集合
        for (Setmeal setmeal : records) {
            Long categoryId = setmeal.getCategoryId();
            // 根据分类名称查询分类信息，获取分类名称！
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            // 将数据封装到DTO
            SetmealDto setmealDto = new SetmealDto();
            setmealDto.setCategoryName(categoryName);
            BeanUtils.copyProperties(setmeal,setmealDto);
            setmealDtoList.add(setmealDto);
        }
        // 数据封装！
        Page<SetmealDto> result = new Page<>();
        BeanUtils.copyProperties(setmealPage,result);
        result.setRecords(setmealDtoList);
        return R.success(result);
    }

    @Override
    public R<String> del(List<Long> ids) {

        // 1. 参数校验 （自己做！）

        //2.  删除逻辑： 起售套餐不可以删除 ， 删除完了还要套餐菜品表数据 删除！
        // 2.1 构造条件！
        LambdaQueryWrapper<Setmeal> wrapper =new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        // 根据用传递id集合查询出套餐
        List<Setmeal> list = this.list(wrapper); // select * from setmeal where id in （1，2，3，）
        // 可以被删除套餐 id集合
        List<Long>  delIds = new ArrayList<>();
        for (Setmeal setmeal : list) {
            // 当我们套餐是 启用状态不可以删除！
            if(setmeal.getStatus()!=1){
                delIds.add(setmeal.getId());
            }
        }
        // 2.2 删除套餐
        this.removeByIds(delIds);    // 批量删除！
        // 2.3 删除套餐菜品表中数据！
        LambdaQueryWrapper<SetmealDish> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.in(SetmealDish::getSetmealId,delIds);
        setmealDishService.remove(delWrapper);
        //3. 数据返回
        return R.success("删除套餐成功！");
    }

    @Override
    public R<List<Setmeal>> getSetmealList(Long categoryId, Integer status) {

        // 参数校验


        // 业务逻辑处理 : 根据套餐分类id 查询套餐， 只查询在售状态的套餐

        LambdaQueryWrapper<Setmeal> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId);
        wrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> setmealList = this.list(wrapper);

        // 数据返回
        return R.success(setmealList);
    }

    @Override
    public R<List<SetmealDish>> getSetmealDishList(Long setmealId) {

        //

        // 2. 业务逻辑处理

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> list = setmealDishService.list(wrapper);

        //
        return R.success(list);
    }
}
