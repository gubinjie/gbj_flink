package com.paat.gbj_flink.entity.administrative;

import lombok.Data;

import java.util.List;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年10月15日
 */
@Data
public class AreaCode {

    /**
     * 父级编号
     */
    private String pCode;

    /**
     * 当前编号
     */
    private String code;

    /**
     * 下级行政区集合
     */
    private List<AreaBaseCode> listCodes;
}