package service.Impl;

import entity.CreditCard;
import repository.BaseEntityRepository;
import service.CreditCardService;

public class CreditCardServiceImpl implements CreditCardService {
    private final BaseEntityRepository<CreditCard> creditCardBaseEntityRepository;

    public CreditCardServiceImpl(BaseEntityRepository<CreditCard> creditCardBaseEntityRepository) {
        this.creditCardBaseEntityRepository = creditCardBaseEntityRepository;
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardBaseEntityRepository.save(creditCard);
    }
}
