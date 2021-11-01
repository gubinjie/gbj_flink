package com.paat.gbj_flink.entity.epm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年09月02日
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpmInfoDTO implements Serializable {
    private static final long serialVersionUID = -4023425109352971833L;

    /**
     * 爬取时间
     */
    private String crawlerTime;

    /**
     * id
     */
    private String id;

    /**
     * 园区名字
     */
    private String parkName;
    /**
     * 园区图片地址
     */
    private String parkImgUrl;
    /**
     * 园区占地面积
     */
    private String parkArea;
    /**
     * 园区联系电话
     */
    private String parkPhone;
    /**
     * 园区地址
     */
    private String parkAddress;
    /**
     * 园区等级
     */
    private String parkLevel;
    /**
     * 园区邮政编号
     */
    private String postalCode;
    /**
     * 园区信息
     */
    private String parkInfo;
    /**
     * 园区行业
     */
    private String parkIndustry;
    /**
     * 园区载体
     */
    private String parkCarrier;
    /**
     * 园区网站地址
     */
    private String parkUrl;
    /**
     * 数据来源
     */
    private String web;

    /**
     * 园区所属省
     */
    private String province;

    /**
     * 园区所属市
     */
    private String city;

    /**
     * 园区所属区
     */
    private String area;

    /**
     * 园区所属省Code
     */
    private String parkProvince;

    /**
     * 园区所属市Code
     */
    private String parkCity;

    /**
     * 园区所属区Code
     */
    private String parkDistrict;

    /**
     * 公司数量
     */
    private String companyNum;

    /**
     * pid
     */
    private String pid;
}