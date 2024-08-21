package service;

import entity.Bank;

public interface BankService {
    Bank findByName(String name);
}
