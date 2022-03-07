package com.itheima.reggie.common.exception.catchs;

import com.itheima.reggie.common.R;
import com.itheima.reggie.common.exception.BuinessException;
import com.itheima.reggie.common.exception.SystemException;
import com.itheima.reggie.common.exception.UnKnowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获、异常处理
 */
@RestControllerAdvice
@Slf4j
public class CatchHandleException {

    @ExceptionHandler(SystemException.class)
    public R handleSystemException(SystemException e) {
        // 保存现场 ：记录日志
        log.info("出现异常：{}", e.getMessage());
        // 通知系统管理员
        //......
        // 欺骗用户：友好提示！
        return R.error("您的网路有问题，请拔掉网线再试试！");
    }

    // 。。。。 未知异常，业务异常自己处理！

    @ExceptionHandler(UnKnowException.class)
    public R handleUnKonwException(UnKnowException e) {
        // 保存现场 ：记录日志
        log.info("出现异常：{}", e.getMessage());
        // 通知系统管理员
        //......
        // 欺骗用户：友好提示！
        return R.error("您的网路有问题，请拔掉网线再试试！");
    }

    @ExceptionHandler(BuinessException.class)
    public R handleUnKonwException(BuinessException e) {
        // 保存现场 ：记录日志
        log.info("出现异常：{}", e.getMessage());
        // 通知系统管理员
        //......
        // 欺骗用户：友好提示！
        return R.error("您的网路有问题，请拔掉网线再试试！");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        // 保存现场 ：记录日志
        log.info("出现异常：{}", e.getMessage());
        // 通知系统管理员
        //......
        // 欺骗用户：友好提示！
        return R.error("您的网路有问题，请拔掉网线再试试！");
    }


}
