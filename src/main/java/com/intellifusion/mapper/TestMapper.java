package com.intellifusion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intellifusion.entity.TestEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {

    @Insert("${sql}")
    public int insertMapper(String sql);

}
