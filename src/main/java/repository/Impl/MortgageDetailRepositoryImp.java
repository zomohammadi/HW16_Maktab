package repository.Impl;

import entity.MortgageDetail;
import jakarta.persistence.EntityManager;
import repository.MortgageDetailRepository;


public class MortgageDetailRepositoryImp extends BaseEntityRepositoryImpl<MortgageDetail> implements MortgageDetailRepository {

    public MortgageDetailRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<MortgageDetail> getEntityClass() {
        return MortgageDetail.class;
    }
}
