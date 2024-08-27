package service.Impl;

import entity.MortgageDetail;
import repository.BaseEntityRepository;
import service.MortgageDetailService;

public class MortgageDetailServiceImpl implements MortgageDetailService {
    private final BaseEntityRepository<MortgageDetail> mortgageDetailBaseEntityRepository;

    public MortgageDetailServiceImpl(BaseEntityRepository<MortgageDetail> mortgageDetailBaseEntityRepository) {
        this.mortgageDetailBaseEntityRepository = mortgageDetailBaseEntityRepository;
    }

    @Override
    public MortgageDetail save(MortgageDetail mortgageDetail) {
        return mortgageDetailBaseEntityRepository.save(mortgageDetail);
    }
}
