package repository;

import entity.Loan;
import entity.Student;

public interface LoanRepository {
    Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType);
}
