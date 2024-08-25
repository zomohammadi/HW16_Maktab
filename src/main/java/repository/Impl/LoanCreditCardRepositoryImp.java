package repository.Impl;

import entity.LoanCreditCard;
import jakarta.persistence.EntityManager;
import repository.LoanCreditCardRepository;


public class LoanCreditCardRepositoryImp extends BaseEntityRepositoryImpl<LoanCreditCard> implements LoanCreditCardRepository {

    public LoanCreditCardRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<LoanCreditCard> getEntityClass() {
        return LoanCreditCard.class;
    }
}
