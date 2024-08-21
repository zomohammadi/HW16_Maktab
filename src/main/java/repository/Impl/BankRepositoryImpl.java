package repository.Impl;

import entity.Bank;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repository.BankRepository;

public class BankRepositoryImpl extends BaseEntityRepositoryImpl<Bank> implements BankRepository {

    public BankRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Bank> getEntityClass() {
        return Bank.class;
    }


    public Bank findByName(String name) {
        TypedQuery<Bank> query = getEntityManager().createQuery("""
                select u from Bank u where u.name = ?1
                """, Bank.class);
        query.setParameter(1, name);
        return query.getSingleResult();
    }
}
