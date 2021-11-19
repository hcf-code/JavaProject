package com.intellifusion.repository;

import com.intellifusion.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends CrudRepository<com.intellifusion.entity.TestEntity, Long> {
    @Override
    Optional<TestEntity> findById(Long id);

    @Override
    <S extends TestEntity> S save(S s);
}
