package service;

import entity.Loan;
import entity.Student;
import jakarta.persistence.Tuple;

import java.util.List;

public interface PaymentService {
    void createPaymentsForLoan(Loan loan);
    List<jakarta.persistence.Tuple> showPaidInstallments(Student student);
    List<Tuple> showUnPaidInstallments(Student student);
    List<Tuple> listOfLoanThatMustBePayed(Long id);
}
