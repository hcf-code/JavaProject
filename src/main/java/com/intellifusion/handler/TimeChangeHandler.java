package com.intellifusion.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

/**
 * long  转 date 类型转换器
 */

@MappedTypes(Long.class)
@MappedJdbcTypes(JdbcType.DATE)
public class TimeChangeHandler extends BaseTypeHandler<Long> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long aLong, JdbcType jdbcType) throws SQLException {
        preparedStatement.setDate(i,new Date(aLong));
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Date date = resultSet.getDate(s);
        return date.getTime();
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Date date = resultSet.getDate(i);
        return date.getTime();
    }

    @Override
    public Long getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Date date = callableStatement.getDate(i);
        return date.getTime();
    }
}
