package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    // 注入菜品的DAO
    @Autowired
    private DishMapper dishMapper;
    // 注入套餐DAO
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public R<String> add(Category category) {

        // 1. 参数校验
        if (ObjectUtils.isEmpty(category)){
            return R.error("参数有问题！");
        }
        // 2.执行核心逻辑 【项目经理 流程图 、组长】
        // saveOrUpdate 方法 根据主键自动识别 有主键属性 修改 没有主键属性 新增
        this.saveOrUpdate(category);

        // 3.数据返回
        R<String> success = R.success("新增成功！");
        return success;
    }

    @Override
    public R<Page<Category>> findByPage(Long page, Long pageSize) {

        // 参数校验
        if (ObjectUtils.isEmpty(page) || page<=0)
        {
            page =1L;
        }
        if (ObjectUtils.isEmpty(pageSize)|| pageSize<=0){
            pageSize =10L;
        }
        // 逻辑处理
        Page<Category> pageParam =new Page<>(page,pageSize);
        Page<Category> result = this.page(pageParam);

        // 数据返回
        R<Page<Category>> success = R.success(result);
        return success;
    }

    @Override
    public R<String> del(Long id) {
        // 1、参数校验
        if (ObjectUtils.isEmpty(id)){
            return  R.error("参数有问题！");
        }
        // 2、逻辑处理
        // 考虑条件： 未来处理删除，考虑我们删除内容和其他内容有没有关系！
        // 2.1 考虑和菜品关系
          // 2.1.1 有关系 不可以删除  （根据分类id 去 dish 表查询 结果为空 没有关系）
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Dish::getCategoryId,id);
        List<Dish> dishes = dishMapper.selectList(wrapper1);
        if (!ObjectUtils.isEmpty(dishes) && dishes.size()>=1){
           return R.error("当前分类有关联菜品不可以删除！");
        }
        //2.2 考虑和套餐关系
        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Setmeal::getCategoryId,id);
        List<Setmeal> setmeals = setmealMapper.selectList(wrapper2);
        if (!ObjectUtils.isEmpty(setmeals) && setmeals.size()>=1){
            return R.error("当前分类有关联套餐不可以删除！");
        }
        this.removeById(id);
        // 数据返回
        R<String>  result = R.success("删除成功！");
        return result;
    }

    @Override
    public R<String> updateCategory(Category category) {

        // 1.参数校验

        //2. 更新数据
        this.saveOrUpdate(category);

        // 3.返回

        return R.success("更新成功！");
    }

    @Override
    public R<List<Category>> findCategoryByType(Integer type) {
        // 1.参数校验
       /* if (ObjectUtils.isEmpty(type)){
            return R.error("参数错误");
        }*/
        //2.业务逻辑
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!ObjectUtils.isEmpty(type),Category::getType,type);
        List<Category> list = this.list(wrapper);
        //3. 数据返回
        return R.success(list);
    }
}
