package com.paat.gbj_flink.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 获取Spring容器管理的所有bean
 *
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年05月19日
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    static {
        new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context != null) {
            return context.getBean(clazz);
        }
        return null;
    }

    public static <T> T getBean(String name) {
        if (context != null) {
            return (T) context.getBean(name);
        }
        return null;
    }
}