package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;

public interface EmployeeService extends IService<Employee> {

    /**
     *  登录业务
     * @param employee
     * @return
     */
    R<Employee> login(Employee employee);

    /**
     *  新增员工
     * @param employee
     * @return
     */
    R<String> add(Employee employee);

    /**
     *  根据条件分页查询员工列表
     * @param name  条件
     * @param page  当前页
     * @param size  一页显示几条数据
     * @return
     */
     R<Page<Employee>> findByPage(String name, Long page, Long size);

    /**
     *  根据员工id 更新员工状态
     * @param employeeId  员工id
     * @param status  员工状态 ： 禁用  正常    （启用：禁用--->正常 0->1   禁用： 正常 --->禁用  1->0）
     * @return R
     *
     */
     R updateEmployeeStatusById(Long employeeId,Integer status);

    /**
     *  根据员工id查询员工信息
     * @param empId
     * @return
     */
    R<Employee> findEmployeeById(Long empId);

    R<String> updateEmployee(Employee employee);
}
