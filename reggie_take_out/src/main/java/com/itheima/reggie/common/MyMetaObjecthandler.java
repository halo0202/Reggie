package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("当前线程id是+++++++++++++++++++++++++++：MetaObjectHandler{}",Thread.currentThread().getId());
        // 插入的时候对哪些字段 赋值赋值什么
        // 参数1 :你要赋值字段属性名称  参数2 ：你赋的值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

        log.info("自动填充。。。。。。。。。insert");

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("当前线程id是+++++++++++++++++++++++++++：MetaObjectHandler{}",Thread.currentThread().getId());
        // 更新的时候对哪些字段 赋值赋值什么
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

        log.info("自动填充。。。。。。。。。update");
    }
}
