package service;

import entity.Loan;

public interface PaymentService {
    void createPaymentsForLoan(Loan loan);
}
