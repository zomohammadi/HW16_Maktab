package service.Impl;

import entity.CreditCard;
import exceptions.CreditCardExceptions;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import repository.BaseEntityRepository;
import repository.CreditCardRepository;
import service.CreditCardService;

import java.time.LocalDate;

public class CreditCardServiceImpl implements CreditCardService {
    private final BaseEntityRepository<CreditCard> creditCardBaseEntityRepository;
    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(BaseEntityRepository<CreditCard> creditCardBaseEntityRepository
            , CreditCardRepository creditCardRepository) {
        this.creditCardBaseEntityRepository = creditCardBaseEntityRepository;
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardBaseEntityRepository.save(creditCard);
    }

    @Override
    public Tuple findByCardNumber(String cardNumber, Long id) {
        try {
            return creditCardRepository.findByCardNumber(cardNumber, id);
        } catch (NoResultException e) {
            throw new CreditCardExceptions.NotFoundException();
        }
    }


}
