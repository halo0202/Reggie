package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    // 以前注入 EmployeeMapper 现在不需要， ServiceImpl 类已经替我们注入

    @Override
    public R<Employee> login(Employee employee) {
        // 核心登录业务！
        //1 、参数校验
        // 1.1 校验对象是否为空
        if (ObjectUtils.isEmpty(employee)) {
            return R.error("登录失败！");
        }
        // 1.2 校验对象中属性 （用户名和密码）
        if (ObjectUtils.isEmpty(employee.getUsername()) || ObjectUtils.isEmpty(employee.getPassword())) {
            return R.error("登录失败！");
        }
        // 2、核心业务
        // 2.1 密码加密
        // 获取前端传递密码
        String frontPassword = employee.getPassword();
        // 对前端密码加密
        String encodePassword = DigestUtils.md5DigestAsHex(frontPassword.getBytes());
        // 2.2 根据用户名查询数据库
        LambdaQueryWrapper<Employee> wrappers = new LambdaQueryWrapper<>();
        wrappers.eq(Employee::getUsername, employee.getUsername());
        Employee dbEmployee = this.getOne(wrappers);
        if (ObjectUtils.isEmpty(dbEmployee)) {
            return R.error("登录失败！");
        }
        if (ObjectUtils.isEmpty(dbEmployee.getPassword())) {
            return R.error("登陆失败！");
        }
        // 2.3 密码比对
        if (!encodePassword.equals(dbEmployee.getPassword())) {
            return R.error("登陆失败！");
        }
        // 2.4 检查用户状态
        if (dbEmployee.getStatus() != 1) {
            return R.error("登录失败！");
        }
        // 3、数据封装返回
        dbEmployee.setPassword("");
        R<Employee> success = R.success(dbEmployee);
        return success;
    }

    @Transactional
    @Override
    public R<String> add(Employee employee) {

        log.info("当前线程id是+++++++++++++++++++++++++++ service +add ：{} ",Thread.currentThread().getId());

        // 1、参数校验
        // 1.1 对象校验
        if (ObjectUtils.isEmpty(employee)) {
            return R.error("参数非法！");
        }
        // 1.2 对象属性校验
        if (ObjectUtils.isEmpty(employee.getUsername())
                || ObjectUtils.isEmpty(employee.getIdNumber())
                || ObjectUtils.isEmpty(employee.getName())
                || ObjectUtils.isEmpty(employee.getPhone())) {
            return R.error("参数非法！");
        }
        // 2、业务处理
        // 2.1 补全完整 数据！
        // ① 补全密码 ，默认密码 123456  ，加密然后
        String encodePassword = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(encodePassword);
        // ② 创建时间
      //  employee.setCreateTime(LocalDateTime.now());
        // ③ 补全用户状态 ：默认 正常 1
        employee.setStatus(1);
        // ④ 补全更新时间
       // employee.setUpdateTime(LocalDateTime.now());
        // ⑤ 补全创建人 、和修改人 都为当前用户 :seession中！
        /*   employee.setCreateUser();
        employee.setUpdateUser();
       */
        this.save(employee);
        // 3、数据返回
        return R.success("添加员工成功！");
    }

    @Override
    public R<Page<Employee>> findByPage(String name, Long page, Long size) {
        // 1.参数校验
        if (ObjectUtils.isEmpty(page) || page <= 0) {
            page = 1L;
        }
        if (ObjectUtils.isEmpty(size) || size <= 0) {
            size = 10L;
        }

        // 2、处理核心业务 ：条件查询 、分页
        // MP 提供分页对象： 封装分页条件（当前页，页面大小）
        Page<Employee> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 封装查询条件 ：根据用户姓名查询 模糊查询
        // 参数1 ：查询数据库列，面向对象方式编写 Lambda  类名：：get方法  指定查询哪个列名
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Employee::getName, name);
        }
        Page<Employee> pageResult = this.page(pageParam, wrapper);
        List<Employee> records = pageResult.getRecords(); //
        // 3. 数据结果返回！
        return R.success(pageResult);
    }

    @Transactional
    @Override
    public R updateEmployeeStatusById(Long employeeId, Integer status) {

        // 1. 校验参数
        if (ObjectUtils.isEmpty(employeeId)) {
            return R.error("参数有问题！");
        }
        // 1.1 校验status ，为了防止 用户非法数据 3
        if (ObjectUtils.isEmpty(status)) {
            return R.error("参数有问题！");
        }
        // 1.2 对于以后我们遇到这样场景一定要校验 ：status ：0或者1 唯一校验
        boolean flag = (status == 1 || status == 0);
        if (!flag) {
            return R.error("参数有问题！");
        }
        // 2、核心逻辑
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getId, employeeId);
        // 2.1 从数据库中根据传递员工id查询员工信息
        Employee dbEmployee = this.getOne(wrapper);
        // 员工不存在 ，提示数据有问题
        if (ObjectUtils.isEmpty(dbEmployee)) {
            return R.error("数据有问题！");
        }
        // 员工存在 ，更改状态和数据库中状态一样就没有必要更新！
        Integer dbStatus = dbEmployee.getStatus();
        // 如果数据库中 正常 ，没有必要在更新程正常！
        if (status == dbStatus) {
            return R.error("数据有问题！");
        }
        dbEmployee.setStatus(status);
        // 更新状态！
        this.updateById(dbEmployee);
        // 3、数据封装
        return R.success("更新成功！");
    }

    @Override
    public R<Employee> findEmployeeById(Long empId) {
        // 1. 参数校验
        if (ObjectUtils.isEmpty(empId)) {
            return R.error("参数错误！");
        }
        // 2. 逻辑
        Employee dbEmployee = this.getById(empId);
        if (ObjectUtils.isEmpty(dbEmployee)) {
            return R.error("数据有误！");
        }
        // 3. 数据返回
        R<Employee> success = R.success(dbEmployee);
        return success;
    }

    @Override
    public R<String> updateEmployee(Employee employee) {

        // 校验省略 （Controller 层做过了）

        // 逻辑
        // 补充完整 数据！
        //  employee.setUpdateUser();  controller 层补全  需要从session 中获取用户id 因此需要 controller
      //  employee.setUpdateTime(LocalDateTime.now());
        // 更新操作
        this.saveOrUpdate(employee);  // 有主键id 更新操作 （update ） 没有主键 id 新增操作（insert）！

        // 数据返回
        return R.success("更新成功！");
    }

}
