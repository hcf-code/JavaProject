package com.intellifusion.service;

import com.intellifusion.entity.TestEntity;

import java.util.HashMap;
import java.util.List;

public interface TestService {
    public TestEntity TestHcf(Long id);

    void bulkInsert() throws CloneNotSupportedException;

    void bulkSaveES();

    List<TestEntity> SearchES(String name,Double sale);

    void reIndex();

    List<HashMap<String, Long>> aggSelect(Integer group,String name);
}
