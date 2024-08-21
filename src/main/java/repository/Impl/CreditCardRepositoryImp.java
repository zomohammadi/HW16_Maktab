package repository.Impl;

import entity.CreditCard;
import jakarta.persistence.EntityManager;
import repository.CreditCardRepository;

public class CreditCardRepositoryImp extends BaseEntityRepositoryImpl<CreditCard> implements CreditCardRepository {

    public CreditCardRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<CreditCard> getEntityClass() {
        return CreditCard.class;
    }
}
