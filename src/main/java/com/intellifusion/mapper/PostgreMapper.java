package com.intellifusion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intellifusion.entity.PersonRecordEntityES;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostgreMapper extends BaseMapper<PersonRecordEntityES> {
}
