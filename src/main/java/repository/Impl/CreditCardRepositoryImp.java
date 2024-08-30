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
    public Tuple findByCardNumber(String cardNumber,Long id) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                select u as creditCard ,a as account from CreditCard u,Account a
                where u.account=a and u.cardNumber = ?1 and a.student.id = ?2 """, Tuple.class);
        query.setParameter(1, cardNumber);
        query.setParameter(2, id);
        return  query.getSingleResult();
    }

}
