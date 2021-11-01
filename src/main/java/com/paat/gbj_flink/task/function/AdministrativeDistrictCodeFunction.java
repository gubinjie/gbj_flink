package com.paat.gbj_flink.task.function;

import com.paat.gbj_flink.common.utils.JacksonUtils;
import com.paat.gbj_flink.entity.administrative.AreaBaseCode;
import com.paat.gbj_flink.entity.administrative.AreaCode;
import com.paat.gbj_flink.entity.epm.EpmInfoDTO;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年10月15日
 */
@Slf4j
public class AdministrativeDistrictCodeFunction extends RichAsyncFunction<EpmInfoDTO, EpmInfoDTO> {

    private static final long serialVersionUID = -2164111108394630146L;
    RedisClient redisClient = null;
    StatefulRedisConnection<String, String> connection = null;
    RedisAsyncCommands<String, String> async = null;

    private String host;

    public AdministrativeDistrictCodeFunction(String host) {
        this.host = host;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        RedisURI redisUri = RedisURI.builder()
                .withHost(host).withDatabase(2)
                .withPort(6379)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        redisClient = RedisClient.create(redisUri);
        connection = redisClient.connect();
        async = connection.async();
    }


    /**
     * input格式 {"code":"440000","listCodes":[{"name":"上海1","code":"1"},{"name":"上海2","code":"2"},{"name":"上海3","code":"3"}],"pcode":"100000"}
     *
     * @param input
     * @param resultFuture
     * @throws Exception
     */
    @Override
    public void asyncInvoke(EpmInfoDTO input, ResultFuture<EpmInfoDTO> resultFuture) throws Exception {
        getProvinceCode(input, resultFuture);
    }

    private void getProvinceCode(EpmInfoDTO input, ResultFuture<EpmInfoDTO> resultFuture) {
        ScanCursor curs = ScanCursor.INITIAL;
        RedisFuture<String> provinceInfo = async.get("pr_" + input.getProvince());
        provinceInfo.thenAccept(info -> {
            if (null == info) {
                //如果精确匹配无法匹配到，开始进行模糊匹配
                ScanArgs s1 = ScanArgs.Builder.limit(10000L).match("pr_" + input.getProvince() + "*");
                RedisFuture<KeyScanCursor<String>> sc1 = async.scan(curs, s1);
                sc1.thenAccept(ksc -> {
                    List<String> kl1 = ksc.getKeys();
                    if (kl1.size() == 1) {
                        RedisFuture<String> future = async.get(kl1.get(0));
                        future.thenAccept(i2 -> {
                            AreaCode v1 = JacksonUtils.toBean(i2, AreaCode.class);
                            input.setParkProvince(v1.getCode());
                            getDistrict(input, resultFuture);
                        });
                    } else {
                        getDistrict(input, resultFuture);
                    }
                });
            } else {
                AreaCode v2 = JacksonUtils.toBean(info, AreaCode.class);
                input.setParkProvince(v2.getCode());
                getDistrict(input, resultFuture);
            }
        });
    }

    private void getDistrict(EpmInfoDTO input, ResultFuture<EpmInfoDTO> resultFuture) {
        RedisFuture<String> cityFuture = async.get("ci_" + input.getCity());
        cityFuture.thenAccept(i1 -> {
            if (null != i1) setDistrictCode(input, i1, resultFuture);
            else {
                //开始进行模糊匹配
                ScanCursor curs = ScanCursor.INITIAL;
                ScanArgs s1 = ScanArgs.Builder.limit(10000L).match("ci_" + input.getCity().replace("市", "") + "*");
                RedisFuture<KeyScanCursor<String>> sc1 = async.scan(curs, s1);
                sc1.thenAccept(i2 -> {
                    List<String> keys = i2.getKeys();
                    if (keys.size() == 1) {
                        RedisFuture<String> c2 = async.get(keys.get(0));
                        c2.thenAccept(i3 -> setDistrictCode(input, i3, resultFuture));
                    } else resultFuture.complete(Collections.singleton(input));
                });
            }
        });
    }

    private void setDistrictCode(EpmInfoDTO input, String i1, ResultFuture<EpmInfoDTO> resultFuture) {
        AreaCode v2 = JacksonUtils.toBean(i1, AreaCode.class);
        if (CollectionUtils.isNotEmpty(v2.getListCodes())) {
            Map<String, String> c2 = v2.getListCodes().stream().collect(Collectors.toMap(AreaBaseCode::getName, AreaBaseCode::getCode));
            String areaInfo = c2.get(input.getArea());
            if (StringUtils.isBlank(areaInfo)) {
                List<String> collect = c2.entrySet().stream().filter(i -> i.getKey().contains(input.getArea())).map(Map.Entry::getValue).collect(Collectors.toList());
                if (collect.size() == 1) input.setParkDistrict(collect.get(0));
            } else input.setParkDistrict(areaInfo);

            input.setParkCity(v2.getCode());
            resultFuture.complete(Collections.singleton(input));
        }
    }

    @Override
    public void timeout(EpmInfoDTO input, ResultFuture<EpmInfoDTO> resultFuture) throws Exception {
        resultFuture.complete(Collections.singleton(input));
    }

    @Override
    public void close() {
        if (null != redisClient) redisClient.shutdown();
        if (null != connection) connection.close();
    }
}