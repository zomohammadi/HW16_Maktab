package repository.Impl;

import entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repository.AccountRepository;

public class AccountRepositoryImp extends BaseEntityRepositoryImpl<Account> implements AccountRepository {

    public AccountRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

   /* @Override
    public Account findByCardNumber(String cardNumber) {
        TypedQuery<Account> query = getEntityManager().createQuery(
                """
                        select a,u from CreditCard u, Account a where u.account=a and u.cardNumber = ?1
                                                """
                , Account.class);
        query.setParameter(1,cardNumber);
        return ;
             }*/
}
