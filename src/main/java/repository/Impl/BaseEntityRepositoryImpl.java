package repository.Impl;

import entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import repository.BaseEntityRepository;

import java.util.List;

public abstract class BaseEntityRepositoryImpl<T extends BaseEntity> implements BaseEntityRepository<T> {
    private final EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    public BaseEntityRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T save(T t) {
        if (t != null) {
            entityManager.getTransaction().begin();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
        }
        return t;
    }

    public List<T> findAll() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> employeeRoot = query.from(getEntityClass());
        return entityManager.createQuery(query).getResultList();
        // TypedQuery<Employee> query1 = entityManager.createQuery(query);
    }

    public abstract Class<T> getEntityClass();

    public T findById(Long id) {
        return entityManager.find(getEntityClass(), id);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
