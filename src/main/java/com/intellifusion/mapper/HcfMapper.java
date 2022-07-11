package com.intellifusion.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * s
 *
 * @author SanheDashen
 * @date 2022/04/22 17:48
 */
@Mapper
public interface HcfMapper {
    @Select("select name from t_area_manage")
    List<String> selectTest();
}
