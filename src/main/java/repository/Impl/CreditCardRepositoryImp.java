package repository.Impl;

import entity.CreditCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import repository.CreditCardRepository;

public class CreditCardRepositoryImp extends BaseEntityRepositoryImpl<CreditCard> implements CreditCardRepository {

    public CreditCardRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<CreditCard> getEntityClass() {
        return CreditCard.class;
    }

    @Override
    public Tuple findByCardNumber(String cardNumber) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                select u as creditCard ,a as account,s as student from CreditCard u,Account a,Student s
                where u.account=a and u.cardNumber = ?1 and s=a.student
                """, Tuple.class);
        query.setParameter(1, cardNumber);
        return query.getSingleResult();
    }

}
