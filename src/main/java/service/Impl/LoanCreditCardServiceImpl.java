package service.Impl;

import entity.LoanCreditCard;
import repository.BaseEntityRepository;
import service.LoanCreditCardService;

public class LoanCreditCardServiceImpl implements LoanCreditCardService {
    private final BaseEntityRepository<LoanCreditCard> loanCreditCardBaseEntityRepository;

    public LoanCreditCardServiceImpl(BaseEntityRepository<LoanCreditCard> loanCreditCardBaseEntityRepository) {
        this.loanCreditCardBaseEntityRepository = loanCreditCardBaseEntityRepository;
    }


  /*  @Override
    public LoanCreditCard save(LoanCreditCard loanCreditCard) {
        return loanCreditCardBaseEntityRepository.save(loanCreditCard);
    }*/
}
