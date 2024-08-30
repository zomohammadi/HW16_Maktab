package service;

import entity.CreditCard;
import jakarta.persistence.Tuple;

public interface CreditCardService {
    entity.CreditCard save(entity.CreditCard creditCard);
    Tuple findByCardNumber(String cardNumber,Long id);
   // CreditCard findByCardNumber(String cardNumber);
}
