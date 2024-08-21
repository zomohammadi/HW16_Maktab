package repository;

import entity.Bank;

public interface BankRepository {
    Bank findByName(String name);
}
