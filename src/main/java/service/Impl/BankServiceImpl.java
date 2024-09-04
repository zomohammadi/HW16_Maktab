package service.Impl;

import entity.Bank;
import exceptions.BankExceptions;
import jakarta.persistence.NoResultException;
import repository.BankRepository;
import repository.BaseEntityRepository;

import java.util.List;

public class BankServiceImpl implements service.BankService {
    private final BankRepository bankRepository;
    private final BaseEntityRepository<Bank> bankBaseEntityRepository;

    public BankServiceImpl(BankRepository bankRepository, BaseEntityRepository<Bank> bankBaseEntityRepository) {
        this.bankRepository = bankRepository;
        this.bankBaseEntityRepository = bankBaseEntityRepository;
    }

    @Override
    public Bank findByName(String name) {
        try {
            return bankRepository.findByName(name);
        } catch (NoResultException e) {
            throw new BankExceptions.NotFoundException(e.getMessage());
        }
    }
    @Override
    public List<Bank> findAll() {
        try {
            List<Bank> cities = bankBaseEntityRepository.findAll();
            if (cities == null || cities.isEmpty()) {
                throw new BankExceptions.NotFoundException("No banks found. contact your admin");
            }
            return cities;
        } catch (Exception e) {
            throw new BankExceptions.DatabaseException(e.getMessage());
        }
    }
}
