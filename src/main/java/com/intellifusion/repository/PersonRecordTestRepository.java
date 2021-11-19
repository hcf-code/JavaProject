package com.intellifusion.repository;

import com.intellifusion.entity.PersonRecordEntityES;
import com.intellifusion.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRecordTestRepository extends CrudRepository<PersonRecordEntityES, Long> {
    @Override
    Optional<PersonRecordEntityES> findById(Long id);

    @Override
    <S extends PersonRecordEntityES> S save(S s);

}
