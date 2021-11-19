/**
 * @(#)PersonRecordEntity.java
 * @author wangyi
 * @version 1.0 2017年2月17日
 * <p>
 * Copyright (C) 2000,2017 , TeamSun, Inc.
 */
package com.intellifusion.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.intellifusion.handler.TimeChangeHandler;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "t_person_record",schema = "public") //PG 注解
@Document(createIndex = true,indexName = "t_person_record")
@Setting(settingPath = "/ElasticSearchSetting.json")
public class PersonRecordEntityES implements Serializable {

    private static final long serialVersionUID = -4664759939375427912L;

    @TableId
    @Id
    @Field(type = FieldType.Long)
    private long id;

    /**
     * 人员注册图像Url
     */
    @Field(type = FieldType.Keyword)
    private String personRegImageUrl;

    /**
     * 人员记录图片Url
     */
    @Field(type = FieldType.Keyword)
    private String personRecordImageUrl;
    /**
     * 人脸小图
     */
    @Field(type = FieldType.Keyword)
    private String personFaceImageUrl;
    /**
     * 人员姓名
     */
    @MultiField(mainField = @Field(type = FieldType.Text,analyzer = "name_analyzer",searchAnalyzer = "name_analyzer"),
                otherFields = @InnerField(type = FieldType.Keyword,suffix = "keyword"))
    private String personName;


    /**
     * 比对置信度
     */
    @Field(type = FieldType.Double)
    private double confidence;

    /**
     * 人员编码
     */
    @Field(type = FieldType.Keyword)
    private String personNum;

    /**
     * 机构ID
     */
    @Field(type = FieldType.Keyword)
    private Long orgId ;

    /**
     * 人员标识ID
     */
    @Field(type = FieldType.Keyword)
    private String personId;

    /**
     * 记录时间
     */
//    @Field(type = FieldType.Date,format = DateFormat.custom,pattern ="yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
//    private Date recordTime;

    @Field(type = FieldType.Date)
    @TableField(typeHandler = TimeChangeHandler.class)
    private long recordTime;


    /**
     * 设备类型
     */
    @Field(type = FieldType.Keyword)
    private String deviceType;
    /**
     * 设备类型id
     */
    @Field(type = FieldType.Integer)
    private Integer deviceTypeId;

    /**
     * protocol person type
     * 人员类型 0 访客 1 人员 2 black 3 vip
     */
    @Field(type = FieldType.Keyword)
    private Integer personType;

    /**
     * 设备ID
     */
    @Field(type = FieldType.Keyword)
    private String deviceId;
    /**
     * 设备名称
     */
    @MultiField(mainField = @Field(type = FieldType.Text,analyzer = "name_analyzer",searchAnalyzer = "name_analyzer"),
            otherFields = @InnerField(type = FieldType.Keyword,suffix = "keyword"))
    private String deviceName;

    /**
     * 设备地址
     */
    @Field(type = FieldType.Keyword)
    private String deviceAddress;

    /**
     * 设备方向：-1：全部，0:暂无，1：进，2：出
     */
    @Field(type = FieldType.Keyword)
    private Integer deviceDirection;

    /**
     * 识别时间
     */
    @Field(type = FieldType.Date)
    @TableField(typeHandler = TimeChangeHandler.class)
    private long recgTime;

    /**
     * 检测返回结果类型 0 表示人脸 1 表示IC卡 2表示人脸+IC卡 3表示二维码 4人证一比一 5表示身份证
     * 以前的数据默认为人脸
     */
    @Field(type = FieldType.Keyword)
    private Integer type;

    /**
     *温度
     */
    @Field(type = FieldType.Float)
    private Float temperature = 0F;

    @Field(type = FieldType.Keyword)
    private String interviwee; //受访人姓名

    @Field(type = FieldType.Keyword)
    private String interviweePhone; //受访人电话

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}