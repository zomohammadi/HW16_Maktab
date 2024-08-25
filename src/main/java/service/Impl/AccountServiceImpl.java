package service.Impl;

import entity.Account;
import repository.AccountRepository;
import repository.BaseEntityRepository;
import service.AccountService;

import java.util.Random;

public class AccountServiceImpl implements AccountService {
    private int accountNumber;
    private final BaseEntityRepository<Account> accountBaseEntityRepository;
   // private final AccountRepository accountRepository;

    public AccountServiceImpl(BaseEntityRepository<Account> accountBaseEntityRepository/*, AccountRepository accountRepository*/) {
        this.accountBaseEntityRepository = accountBaseEntityRepository;
       // this.accountRepository = accountRepository;
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

   /* @Override
    public Account findByCardNumber(String CardNumber) {
        return null;
    }*/
}
