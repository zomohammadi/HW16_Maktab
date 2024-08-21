package service.Impl;

import entity.Bank;
import repository.BankRepository;

public class BankServiceImpl implements service.BankService {
    private final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public Bank findByName(String name) {
        return bankRepository.findByName(name);
    }
}
