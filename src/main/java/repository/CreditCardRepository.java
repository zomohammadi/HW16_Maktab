package repository;

import entity.CreditCard;
import jakarta.persistence.Tuple;

public interface CreditCardRepository {
    Tuple findByCardNumber(String cardNumber);
    //CreditCard findByCardNumber(String cardNumber);
}
