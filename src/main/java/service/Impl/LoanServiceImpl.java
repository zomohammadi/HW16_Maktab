package service.Impl;

import entity.Loan;
import entity.Student;
import exceptions.LoanExceptions;
import jakarta.persistence.PersistenceException;
import repository.BaseEntityRepository;
import repository.LoanRepository;
import service.LoanService;

public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final BaseEntityRepository<Loan> loanBaseEntityRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BaseEntityRepository<Loan> loanBaseEntityRepository) {
        this.loanRepository = loanRepository;
        this.loanBaseEntityRepository = loanBaseEntityRepository;
    }

    @Override
    public Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType) {
        try {

            return loanRepository.findLoanForStudentInTerm(year, termType, student, loanType);
        } catch (PersistenceException e) {
            throw new LoanExceptions.DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public Loan save(Loan loan) {
        return loanBaseEntityRepository.save(loan);
    }
}
