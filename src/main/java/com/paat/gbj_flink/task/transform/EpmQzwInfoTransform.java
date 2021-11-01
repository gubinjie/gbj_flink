package com.paat.gbj_flink.task.transform;

import com.paat.gbj_flink.common.utils.JacksonUtils;
import com.paat.gbj_flink.entity.epm.EpmInfoDTO;
import com.paat.gbj_flink.entity.oplog.OplogInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年09月02日
 */
@Slf4j
public class EpmQzwInfoTransform implements MapFunction<OplogInfo, EpmInfoDTO> {
    private static final long serialVersionUID = -1755162447366550100L;

    @Override
    public EpmInfoDTO map(OplogInfo oplogInfo) throws Exception {

        List<Map<String, Object>> o = oplogInfo.getO();
        Map<String, Object> collect = o.stream().collect(Collectors.toMap(v -> v.get("Name").toString(), v -> v.get("Value")));

        try {
            EpmInfoDTO epmInfoDTO = EpmInfoDTO.builder().id(checkNull(collect.get("_id")))
                    .crawlerTime(checkNull(collect.get("update_time"))).parkName(checkNull(collect.get("name")))
                    .province(checkNull(collect.get("province"))).pid(checkNull(collect.get("pid")))
                    .city(checkNull(collect.get("city"))).area(checkNull(collect.get("area")))
                    .parkAddress(checkNull(collect.get("address"))).parkArea(landArea(collect.get("ar")))
                    .companyNum(checkNull(collect.get("companyNum")))
                    .parkUrl(checkNull(collect.get("url"))).build();

            if (StringUtils.isBlank(epmInfoDTO.getParkName())) return null;

            return epmInfoDTO;
        } catch (Exception e) {
            log.error("园区数据转换异常，错园区数据:{}", oplogInfo, e);
        }
        return null;
    }

    private String checkNull(Object value) {
        String info = JacksonUtils.toJson(value);
        if (StringUtils.isBlank(info)) return "";
        return info.replace("\\n", "").replace("\\r\\n", "")
                .replace("</p>", "").replace("<p>", "")
                .replace("&nbsp;", "").replace("&#13;", "").replace("\n\r", "");
    }

    private String landArea(Object value) {
        String info = JacksonUtils.toJson(value);
        boolean matches = Pattern.matches("\\d+", info);
        if (matches) {
            BigDecimal area = new BigDecimal(info);
            BigDecimal divider = area.divide(new BigDecimal(1500), 2, 4);
            return divider.toString();
        }
        return "";
    }
}