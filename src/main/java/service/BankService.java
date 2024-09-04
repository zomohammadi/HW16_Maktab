package service;

import entity.Bank;

import java.util.List;

public interface BankService {
    Bank findByName(String name);

    List<Bank> findAll();
}
