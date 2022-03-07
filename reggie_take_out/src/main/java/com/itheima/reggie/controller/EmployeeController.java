package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        R<Employee> login = employeeService.login(employee);
        Employee data = login.getData();
        if (ObjectUtils.isEmpty(data)) {
            return R.error("登录失败！");
        }
        // 请求对象获取session ,用户id 写入session！
        request.getSession().setAttribute("employee", data.getId());
        return login;
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        try {
            // 从session中清空，用户信息！
            request.getSession().removeAttribute("employee");
            log.info("用户退出。。。。。。。。");
            return R.success("退出成功！");
        } catch (Exception e) {
            return R.error("退出失败！");
        }
    }

    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("当前线程id是+++++++++++++++++++++++++++：controller +add {}",Thread.currentThread().getId());

        // 创建人 id
        Long userId = (Long) request.getSession().getAttribute("employee");
       // employee.setCreateUser(userId);
      //  employee.setUpdateUser(userId);
        R<String> add = employeeService.add(employee);
        return add;
    }

    /**
     * springmvc 中 参数获取的时候 根据前端传递参数名 和后端方法名称 匹配 匹配则自动封装！
     * 还可以使用对象，对象的中属性名称和前端传递的参数名称保持一致！
     *
     * @param name
     * @param page
     * @param pageSize
     * @return
     */

    // http://localhost:8080/user?name=tom
    @GetMapping("/page")
    public R<Page<Employee>> findByPage(String name, Long page, Long pageSize) {

        return employeeService.findByPage(name, page, pageSize);

    }

    /**
     *
     *   思想 ： 前端公用一个接口干两件事：
     *      ① controller 层 判断  条件（至关重要！）！
     *      ② service 中提供两个 方法处理两件事
     *
     * @param request
     * @param employee
     * @return
     */

    @PutMapping
    public R<String> updateEmployeeStatusById(HttpServletRequest request,@RequestBody Employee employee) {

        if (ObjectUtils.isEmpty(employee)) {
            return R.error("参数错误！");
        }
        // 选择：id 和 status 其他字段必须未为空
        boolean phoneEmpty = ObjectUtils.isEmpty(employee.getPhone());
        boolean nameEmpty = ObjectUtils.isEmpty(employee.getName());
        boolean usernameEmpty = ObjectUtils.isEmpty(employee.getUsername());
        boolean idNumberEmpty = ObjectUtils.isEmpty(employee.getIdNumber());
        boolean sexEmpty = ObjectUtils.isEmpty(employee.getSex());
        // 如果我们 五个字段全部为空我就认为你是在做 禁用或者启用状态
        boolean flag = phoneEmpty&& nameEmpty&& usernameEmpty && idNumberEmpty && sexEmpty;
        R<String> res = null;

        // 节省带宽！
        if (flag){
             res = employeeService.updateEmployeeStatusById(employee.getId(), employee.getStatus());
        }else {
            Long empId = (Long) request.getSession().getAttribute("employee");
          //  employee.setUpdateUser(empId);
            res = employeeService.updateEmployee(employee);
        }

        return res;

    }


    // 根据员工id查询员工信息接口
    @GetMapping("/{id}")
    public R<Employee> findEmployeeById(@PathVariable("id") Long empId){
         // 调用业务逻辑
        R<Employee> result = employeeService.findEmployeeById(empId);
        return result; // json 视图
    }


}
