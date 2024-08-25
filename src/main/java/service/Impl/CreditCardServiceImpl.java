package service.Impl;

import entity.CreditCard;
import exceptions.CreditCardExceptions;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;
import repository.BaseEntityRepository;
import repository.CreditCardRepository;
import service.CreditCardService;

public class CreditCardServiceImpl implements CreditCardService {
    private final BaseEntityRepository<CreditCard> creditCardBaseEntityRepository;
    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(BaseEntityRepository<CreditCard> creditCardBaseEntityRepository, CreditCardRepository creditCardRepository) {
        this.creditCardBaseEntityRepository = creditCardBaseEntityRepository;
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardBaseEntityRepository.save(creditCard);
    }

    @Override
    public Tuple findByCardNumber(String cardNumber) {
        try {
            return creditCardRepository.findByCardNumber(cardNumber);
        } catch (PersistenceException e) {
            throw new CreditCardExceptions.NotFoundException();
        }
    }
}
