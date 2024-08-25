package repository;

import entity.Account;
import entity.BaseEntity;
import entity.Student;

import java.util.List;

public interface BaseEntityRepository<T extends BaseEntity> {
    T save(T t);

    List<T> findAll();

    T findById(Long id);

    void update(T t);
}
