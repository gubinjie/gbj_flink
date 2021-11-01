package com.paat.gbj_flink.connector;

import com.paat.gbj_flink.autoconfigure.KafkaProperties;
import com.paat.gbj_flink.common.utils.JacksonUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年07月14日
 */
@Component
public class KafkaConnector {

    @Autowired
    private KafkaProperties kafkaProperties;

    public FlinkKafkaConsumer source(String topic) {
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), consumerConfig());
        consumer.setStartFromGroupOffsets();
        //consumer.setStartFromEarliest();
        return consumer;
    }

    public FlinkKafkaProducer sink(String topic) {
        return sink(topic, null);
    }

    public FlinkKafkaProducer sink(String topic, String key) {
        return new FlinkKafkaProducer<>(topic, new KafkaSerializationSchema() {
            private static final long serialVersionUID = -8327782808583028433L;

            @Override
            public ProducerRecord<byte[], byte[]> serialize(Object element, @Nullable Long timestamp) {
                if (key == null || "".equals(key)) {
                    return new ProducerRecord<>(topic, JacksonUtils.toJson(element).getBytes(StandardCharsets.UTF_8));
                } else {
                    return new ProducerRecord<>(topic, key.getBytes(StandardCharsets.UTF_8), JacksonUtils.toJson(element).getBytes(StandardCharsets.UTF_8));
                }
            }
        }, producerConfig(), FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
    }


    private Properties consumerConfig() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "paat_realtime_deal");

        return config;
    }

    private Properties producerConfig() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 300000);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return config;
    }
}