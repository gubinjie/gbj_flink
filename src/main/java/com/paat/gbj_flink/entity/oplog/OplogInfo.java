package com.paat.gbj_flink.entity.oplog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年06月24日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OplogInfo implements Serializable {
    private static final long serialVersionUID = 1472965684665566346L;
    private Long ts;
    private Integer v;
    private String op;
    private String ns;
    private List<Map<String, Object>> o;
}