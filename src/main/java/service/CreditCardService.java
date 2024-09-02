package service;

import jakarta.persistence.Tuple;

public interface CreditCardService {
    entity.CreditCard save(entity.CreditCard creditCard);
    Tuple findByCardNumber(String cardNumber);
}
