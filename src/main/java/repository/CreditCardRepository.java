package repository;

import jakarta.persistence.Tuple;

public interface CreditCardRepository {
    Tuple findByCardNumber(String cardNumber);
}
