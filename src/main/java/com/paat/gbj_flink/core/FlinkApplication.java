package com.paat.gbj_flink.core;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年05月19日
 */
public class FlinkApplication {
    public static ApplicationContext run(Class<? extends ApplicationContext> clazz) {
        return ApplicationContextHolder.getBean(clazz);
    }
}