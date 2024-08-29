package service.Impl;

import entity.Account;
import repository.BaseEntityRepository;
import service.AccountService;

import java.util.Random;

public class AccountServiceImpl implements AccountService {
    private final int accountNumber;
    private final BaseEntityRepository<Account> accountBaseEntityRepository;

    public AccountServiceImpl(BaseEntityRepository<Account> accountBaseEntityRepository) {
        this.accountBaseEntityRepository = accountBaseEntityRepository;
        this.accountNumber = new Random().nextInt(100, 1000);
    }

    @Override
    public Account save(Account account) {
        account.setAccountNumber(accountNumber);
        return accountBaseEntityRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountBaseEntityRepository.findById(id);
    }

    @Override
    public void update(Account account) {
        Account accountFoundById = findById(account.getId());
        accountBaseEntityRepository.update(accountFoundById);
    }

}
