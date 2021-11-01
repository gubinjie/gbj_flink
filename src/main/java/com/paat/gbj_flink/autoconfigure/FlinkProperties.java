package com.paat.gbj_flink.autoconfigure;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年05月19日
 */
@Getter
@Setter
@Configuration
public class FlinkProperties {

    public static final String FLINK_NAME = "flink.name";
    public static final String FLINK_PARALLELISM = "flink.parallelism";
    public static final String FLINK_CHECKPOINT_INTERVAL = "flink.checkpoint.interval";
    public static final String FLINK_CHECKPOINT_DATA_URI = "flink.checkpoint.data-uri";

    @NacosValue(value = "${" + FLINK_NAME + "}", autoRefreshed = true)
    private String name;

    @NacosValue(value = "${" + FLINK_PARALLELISM + "}", autoRefreshed = true)
    private int parallelism;

    @NacosValue(value = "${" + FLINK_CHECKPOINT_INTERVAL + "}", autoRefreshed = true)
    private int checkpointInterval;

    @NacosValue(value = "${" + FLINK_CHECKPOINT_DATA_URI + "}", autoRefreshed = true)
    private String checkpointDataUri;

    @Override
    public String toString() {
        return "\n" + FLINK_PARALLELISM + "=" + parallelism + "\n" +
                FLINK_CHECKPOINT_INTERVAL + "=" + checkpointInterval + "\n" +
                FLINK_CHECKPOINT_DATA_URI + "=" + checkpointDataUri;
    }

}