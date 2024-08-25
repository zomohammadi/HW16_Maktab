package service;

import entity.Account;

public interface AccountService {
    Account save(Account account);
    Account findById(Long id);
    void update(Account account);
    //Account findByCardNumber(String CardNumber);
}
