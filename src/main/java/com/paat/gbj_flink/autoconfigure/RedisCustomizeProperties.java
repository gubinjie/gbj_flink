package com.paat.gbj_flink.autoconfigure;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年10月09日
 */

@Getter
@Setter
@Configuration
public class RedisCustomizeProperties implements Serializable {


    public static final String SPRING_REDIS_HOST = "spring.redis.host";
    private static final long serialVersionUID = 3711635575350989925L;

    @NacosValue(value = "${" + SPRING_REDIS_HOST + "}", autoRefreshed = true)
    private String host;
}