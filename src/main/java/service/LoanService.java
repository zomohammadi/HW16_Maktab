package service;

import entity.Loan;
import entity.Student;

public interface LoanService {
    Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType);
    Loan save(Loan loan);
}
