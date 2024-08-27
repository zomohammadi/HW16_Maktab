package service;

import entity.Loan;
import entity.Student;
import enumaration.Degree;
import enumaration.LoanType;

public interface LoanService {
    Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType);
    Loan save(Loan loan);
    Loan findStudentMortgage(Student student, Degree degree, LoanType loanType);
}
