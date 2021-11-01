package com.paat.gbj_flink.task.transform;

import com.paat.gbj_flink.common.utils.JacksonUtils;
import com.paat.gbj_flink.entity.oplog.OplogInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年07月15日
 */
@Slf4j
public class OplogInfoTransform implements MapFunction<String, OplogInfo> {
    private static final long serialVersionUID = -6574122175299442677L;

    @Override
    public OplogInfo map(String info) throws Exception {
        try {
            OplogInfo oplogInfo = JacksonUtils.toBean(info, OplogInfo.class);
            if(null ==oplogInfo){
                return null;
            }
            if(StringUtils.isNotBlank(oplogInfo.getOp()) && !"i".equals(oplogInfo.getOp())){
                return null;
            }
            if (StringUtils.isBlank(oplogInfo.getNs())) {
                return null;
            }

            return oplogInfo;
        } catch (Exception e) {
            log.error("爬虫数据第一阶段转换异常",e);
        }
        return null;
    }
}