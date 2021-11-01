package com.paat.gbj_flink.autoconfigure;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年05月20日
 * 开发、测试 - 6acf31ca-65ae-41d0-a08e-367c67ce404f  application-test.properties
 * 生产 -  ef159372-bbde-45e6-b5e6-05c0c383b5d8 application.properties
 */


@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "nacos.paat.com:8848", namespace = "ef159372-bbde-45e6-b5e6-05c0c383b5d8"))
@NacosPropertySource(dataId = "application.properties", groupId = "PDC_REALTIME_DEAL", autoRefreshed = true)
public class NacosConfiguration {
}