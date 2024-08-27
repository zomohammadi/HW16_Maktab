package repository;

import entity.Loan;
import entity.Student;
import enumaration.Degree;
import enumaration.LoanType;

public interface LoanRepository {
    Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType);
    Loan findStudentMortgage(Student student, Degree degree, LoanType loanType);
}
