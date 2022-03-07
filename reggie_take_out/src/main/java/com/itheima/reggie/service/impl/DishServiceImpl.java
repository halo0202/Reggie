package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.dto.DishAddDto;
import com.itheima.reggie.common.dto.DishListDto;
import com.itheima.reggie.common.dto.DishUpdateDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

      @Autowired
      DishFlavorService dishFlavorService;
      @Autowired
    CategoryService categoryService;

    /**
     *  解释：
     *
     *  Student ： name  age    Student student = new Student("张三"，12);
     *
     *  People ： username age  Peolpe people = new People();
     *
     *   BeanUtils.copyProperties(student,people);
     *
     *    people
     *
     * @param dishAddDto
     * @return
     */

    @Transactional
    @Override
    public R<String> addDish(DishAddDto dishAddDto) {
        // 1.1 参数校验
        if (ObjectUtils.isEmpty(dishAddDto)){
            return R.error("数据有误！");
        }
        // 2.1.往菜品表添加菜品
        // 介绍一个工具类 ：BeanUtils  (spring 中提供的 ,apache commons , Google guava)
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishAddDto,dish);
        log.info("dish 数据{}",dish);
        // 主键返回 MP （主键是id 默认会返回 ，主键不叫id 在主键属性上加上 @TableId）
        this.save(dish);
         //2.2.往口味表添加口味
        List<DishFlavor> flavors = dishAddDto.getFlavors();
        flavors= flavors.stream().map((flaver)->{
            flaver.setDishId(dish.getId());
            return flaver;
        }).collect(Collectors.toList());

        // lambda -->函数式编程提供简化-->函数式编程解决什么问题？ 解决方法参数不可以是方法的问题！（面相对象思想： Function ）
        // stream  函数式编程的应用
        dishFlavorService.saveBatch(flavors);
        return R.success("添加成功！");
    }

    @Override
    public R<Page<DishListDto>> findDishPage(Long page, Long pageSize, String name) {

        // 1. 参数校验
        if (ObjectUtils.isEmpty(page) || page <= 0) {
            page = 1L;
        }
        if (ObjectUtils.isEmpty(pageSize) || pageSize <= 0) {
            pageSize = 10L;
        }

        //2、核心逻辑
        // 2.1 分页 service 中提供一个方法  page（参数1，参数2）
          // ① 分页条件
        Page<Dish> pageParam = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
         // ② 更具名称条件查询
         wrapper.like(!ObjectUtils.isEmpty(name),Dish::getName,name);
         // ③ 排序 更具更新时间 倒序 （降序）
        wrapper.orderByDesc(Dish::getUpdateTime);
        Page<Dish> result = this.page(pageParam, wrapper);
        // result 数据中封装菜品 只包含  分类id 不包含分类 名称！
        // 获取当前页数据集合
        List<Dish> records = result.getRecords();
        // 创建一个返回的集合
        List<DishListDto> listDtos = new ArrayList<>();
        // 遍历操作
        for (Dish record : records) {
            // 获取分类id
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            DishListDto dishListDto = new DishListDto();
            BeanUtils.copyProperties(record,dishListDto);
            dishListDto.setCategoryName(categoryName);
            listDtos.add(dishListDto);
        }
        // 创建返回数据
        Page<DishListDto>  targetResult = new Page<>();
        BeanUtils.copyProperties(result,targetResult);
        targetResult.setRecords(listDtos);
        // 3.数据返回
        return R.success(targetResult);
    }

    @Override
    public R<DishUpdateDto> findDishById(Long id) {
        // 1. 校验参数
         if (ObjectUtils.isEmpty(id)){
             return R.error("参数错误！");
         }
        //2. 执行逻辑： 获取菜品数据 ，获取口味数据 ，分类id数据
        // 2.1 获取菜品数据
        Dish dish = this.getById(id);
         // 2.2 获取口味数据
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        // 条件 菜品id ，去菜品口味表  跟据菜品id查询 菜品口味
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavorList = dishFlavorService.list(wrapper);

        //3.  构造返回数据
        DishUpdateDto dto = new DishUpdateDto();
        BeanUtils.copyProperties(dish,dto);
        dto.setFlavors(flavorList);

        return R.success(dto);
    }

    @Transactional
    @Override
    public R<String> updateDish(DishUpdateDto dto) {

        // 1. 参数校验


        //2 .核心逻辑： 更新 dish 数据  ； 更新 口味表数据 dush_flaver
        // 2.1 更新 dish表的数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto,dish);
        this.saveOrUpdate(dish);

        // 2.2 更新口味 ： 思路 ：先删除，在插入


        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());   // wehere dish_id = 1221
        dishFlavorService.remove(wrapper);  // delete from dish_flaver wehere dish_id = 1221
        // 添加
        List<DishFlavor> flavors = dto.getFlavors();
        List<DishFlavor>  rs=new ArrayList<>();
        for (DishFlavor flavor : flavors) {
             flavor.setDishId(dish.getId());
             rs.add(flavor);
        }
        dishFlavorService.saveBatch(rs);

        //3. 返回数据
        return R.success("更新成功！");
    }

    @Override
    public R<List<Dish>> findDishByCategoryId(Long categoryId,Integer status) {
        // 1.  省略！


        //2. 核心逻辑： 根据分类id查询 菜品    操作菜品表！

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,categoryId);  // where categoryId=11
        wrapper.eq(!ObjectUtils.isEmpty(status),Dish::getStatus,status);
        List<Dish> list = this.list(wrapper); // select * from dish where categoryId=11

        //3.
        return R.success(list);
    }


}
