package repository.Impl;

import entity.Account;
import jakarta.persistence.EntityManager;
import repository.AccountRepository;

public class AccountRepositoryImp extends BaseEntityRepositoryImpl<Account> implements AccountRepository {

    public AccountRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }
}
