package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ① 编写过滤器 ：实现 Filter接口
 * ② 过滤器生效:
 * <p>
 * 1、@WebFilter 声明是一个过滤器同时指定拦截请求 ： /*  拦截所有
 * 2、开启组件扫描 @ServletComponentScan // 让我们filter生效
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 创建一个路径匹配器对象
    public static AntPathMatcher matcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("当前线程id是+++++++++++++++++++++++++++：filter{}",Thread.currentThread().getId());

        // 核心逻辑
        // 1、对象转换
        HttpServletRequest req = (HttpServletRequest)request ;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 2、获取请求路径
        String requestURI = req.getRequestURI();  //  /employee/abc
        //3、构建白名单 （不需要拦截路径 比如登录和登出）
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/upload",
                "/user/sendMsg",
                "/user/login"
        };
        //4、判断当前请求是否需要拦截
        // 4.1 如果在白名单 不需要拦截
        Boolean checked = check(uris, requestURI);
        // 在白名单
        if (checked) {
            log.info("放行的路径："+requestURI);
            // 放行
            chain.doFilter(req, resp);
            return;
        }
        // 4.2  如果不在白名单 判断是否登录
        Object employee = req.getSession().getAttribute("employee");
        //   登录(后台) 放行
        if (!ObjectUtils.isEmpty(employee)) {
            log.info("用户已经登录，放行路径："+requestURI);
            // 把当前用户id 放入 ThreadLocal
            BaseContext.setCurrentId((Long) employee);
            chain.doFilter(req, resp);
            return;
        }
         // 登录（前台） 放行
        if(req.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",req.getSession().getAttribute("user"));
            Long userId = (Long) req.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            chain.doFilter(req,resp);
            return;
        }
        // 5、没登录 拦截
        log.info("路劲不在白名单，而且灭有登录，路径"+requestURI);

        //测试不出效果 ：原因 ：前端需要根据后端提供数据 路劲处理！
        // 前端如果收不到 NOTLOGIN 直接页面显示
        // 前端如果收到 NOTLOGIN ，跳转登录页面
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
    }

    /**
     * 校验当前请求路径是否在白名单
     *
     * @param uris       白名单列表
     * @param requestURI 当前 请求路径
     * @return Boolean
     */
    private Boolean check(String[] uris, String requestURI) {
        // 介绍 spring提供了一个路径匹配器！
        for (String uri : uris) {
           boolean match = matcher.match(uri, requestURI);// 比较两个路径是否是同一个！
            // 如果匹配到 返回 ture
           if (match){
               return true;
           }
        }
         return false;
    }

}
