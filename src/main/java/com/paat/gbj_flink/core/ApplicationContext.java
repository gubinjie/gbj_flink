package com.paat.gbj_flink.core;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年07月14日
 */
public interface ApplicationContext {

    /**
     * flink 执行阶段
     * @param env
     * @param source
     * @throws Exception
     */
    default void transform(StreamExecutionEnvironment env, DataStream<String> source) throws Exception {
    }


    /**
     * flink 执行阶段
     *
     * @param env
     * @param source
     * @throws Exception
     */
    default void transform(StreamExecutionEnvironment env, DataStream<String>... source) throws Exception {
    }

    /**
     * 获取Flink执行环境
     *
     * @return
     */
    void getStreamExecutionEnvironment() throws Exception;

    /**
     * 任务执行
     *
     * @param sources
     * @throws Exception
     */
    void execute(DataStream<String>... sources) throws Exception;

    /**
     * 任务执行
     *
     * @param topic
     * @throws Exception
     */
    void execute(String topic) throws Exception;
}