/**
 * @(#)PersonRecordEntity.java
 * @author wangyi
 * @version 1.0 2017年2月17日
 * <p>
 * Copyright (C) 2000,2017 , TeamSun, Inc.
 */
package com.intellifusion.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;


@Document(collection = "TPersonRecordTest")
@Data
public class PersonRecordEntity implements Serializable {

    private static final long serialVersionUID = -4664759939375427912L;

    @Id
    private long id;

    /**
     * 人员注册图像Url
     */
    private String personRegImageUrl;

    /**
     * 人员记录图片Url
     */
    private String personRecordImageUrl;
    /**
     * 人脸小图
     */
    private String personFaceImageUrl;
    /**
     * 人员姓名
     */
    private String personName;

    private String splitName;

    /**
     * 比对置信度
     */
    private double confidence;

    /**
     * 人员编码
     */
    private String personNum;

    /**
     * 机构ID
     */
    private Long orgId ;

    /**
     * 人员标识ID
     */
    private String personId;

    /**
     * 记录时间
     */
    private long recordTime;

    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备类型id
     */
    private Integer deviceTypeId;

    /**
     * protocol person type
     * 人员类型 0 访客 1 人员 2 black 3 vip
     */
    private Integer personType;

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备地址
     */
    private String deviceAddress;

    /**
     * 设备方向：-1：全部，0:暂无，1：进，2：出
     */
    private Integer deviceDirection;

    /**
     * 图片文件名称
     */
    private String fileName;
    /**
     * 通行是否有效
     */
    private Boolean passed;
    /**
     * 识别时间
     */
    private Integer recgTime;

    /**
     * 特征值
     */
    private byte[] feature;

    /**
     * 检测返回结果类型 0 表示人脸 1 表示IC卡 2表示人脸+IC卡 3表示二维码 4人证一比一 5表示身份证
     * 以前的数据默认为人脸
     */
    private Integer type;

    /**
     *温度
     */
    private Float temperature = 0F;

    /**
     * 温度是否异常,0:未检测体温,1:正常,2:异常
     * @return
     */
    private Integer unusual = 0;

    /**
     * 0 表示未开启口罩识别 1 表示无口罩 2 表示有口罩
     */
    private Integer respirator = 0;

    /**
     * 处理类型,1:未处理,2:确认异常,3:解除警报,默认未处理
     */
    private Integer handleType = 1;

    /**
     * 人员组名称
     * @return
     */
    private String personGroup = "";

    /**
     * 疫情处理备注
     * @return
     */
    private String remark = "";

    private String icCard;

    private String idCard;

    private String telephone;

    private String email;

    private Integer workStatus; //在职0，离职状态2

    private String interviwee; //受访人姓名

    private String interviweePhone; //受访人电话

    private String interviweeNum; //受访人编号

    private String identityNumber;//访客身份证号

    private String title; //人员职务

    private String hireDate;    // 入职日期: 格式yyyy-MM-dd

    /*
     * 协议中:
     * Male=0;//男性
     * Female=1;//女性
     * UnknownGender=2;//未指定性别或者未知性别
     *
     * 数据库中:
     * 性别(0->女, 1->男, null->未知)
     */
    private Integer sex;

    private Date created;

    private Date updated;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}