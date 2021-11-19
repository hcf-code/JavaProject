package com.intellifusion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

import java.util.List;

@Data
@TableName("test")
@Document(createIndex = true,indexName = "hcf_test")
@Setting(settingPath = "/ElasticSearchSetting.json")
public class TestEntity implements Cloneable {
    @TableId(value = "id",type = IdType.AUTO)
    @Id
    @Field(type = FieldType.Keyword)
    public Long id;

    @Field(type = FieldType.Keyword)
    public String name;

    @Field(type = FieldType.Text,store = true,analyzer = "ik_max_word")
    @TableField(exist = false)
    public String fullName;

    @Field(type = FieldType.Keyword)
    public Integer age;

    @Field(type = FieldType.Keyword)
    public String sex;

    @Field(type = FieldType.Keyword)
    public Double sale;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
