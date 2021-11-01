package com.paat.gbj_flink.autoconfigure;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年07月14日
 */
@Setter
@Getter
@ToString
@Configuration
public class KafkaProperties {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap-servers";

    @NacosValue(value = "${" + KAFKA_BOOTSTRAP_SERVERS + "}", autoRefreshed = true)
    private String bootstrapServers;
}