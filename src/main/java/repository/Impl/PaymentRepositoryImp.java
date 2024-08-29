package repository.Impl;

import entity.Payment;
import jakarta.persistence.EntityManager;
import repository.PaymentRepository;

public class PaymentRepositoryImp extends BaseEntityRepositoryImpl<Payment> implements PaymentRepository {

    public PaymentRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Payment> getEntityClass() {
        return Payment.class;
    }

}
