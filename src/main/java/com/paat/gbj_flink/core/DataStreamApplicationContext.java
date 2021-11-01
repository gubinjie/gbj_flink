package com.paat.gbj_flink.core;

import com.paat.gbj_flink.autoconfigure.FlinkProperties;
import com.paat.gbj_flink.connector.KafkaConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年07月14日
 */
@Slf4j
public class DataStreamApplicationContext implements ApplicationContext{

    @Autowired
    private Environment environment;

    @Autowired
    private FlinkProperties flinkProperties;

    @Autowired
    private KafkaConnector kafkaConnector;

    private StreamExecutionEnvironment env;

    @Override
    public void getStreamExecutionEnvironment() throws Exception {
        if(null == env){
            env = createStreamExecutionEnvironment();
        }
    }

    @Override
    public void execute(DataStream<String>... sources) throws Exception {
        if(sources.length == 1){
            transform(env,sources[0]);
        }else{
            transform(env,sources);
        }

        env.execute(getClass().getSimpleName());
    }

    @Override
    public void execute(String topic) throws Exception {
        FlinkKafkaConsumer consumer = kafkaConnector.source(topic);
        getStreamExecutionEnvironment();
        execute(env.addSource(consumer));
    }

    private StreamExecutionEnvironment createStreamExecutionEnvironment() throws Exception {
        int parallelism = flinkProperties.getParallelism();
        int checkpointInterval = flinkProperties.getCheckpointInterval();
        String checkpointDataUri = flinkProperties.getCheckpointDataUri();

        StreamExecutionEnvironment env;
        boolean local = isLocal();
        log.info("当前环境:{}",local);

        if(local){
            Configuration conf = new Configuration();
            conf.setInteger(RestOptions.PORT, 8082);
            env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

            if (StringUtils.isNotBlank(checkpointDataUri)) {
                env.setStateBackend(new FsStateBackend(checkpointDataUri));
            }
        }else{
            env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setStateBackend(new RocksDBStateBackend(checkpointDataUri, true));
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(60, Time.of(1, TimeUnit.MINUTES)));
        }

        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.setParallelism(parallelism);
        env.enableCheckpointing(checkpointInterval);

        return env;
    }

    private boolean isLocal() {
        return Arrays.asList(environment.getActiveProfiles()).contains("local");
    }
}