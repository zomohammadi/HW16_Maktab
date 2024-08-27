package repository.Impl;

import entity.Loan;
import entity.Student;
import enumaration.Degree;
import enumaration.LoanType;
import enumaration.TermType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repository.LoanRepository;

public class LoanRepositoryImpl extends BaseEntityRepositoryImpl<Loan> implements LoanRepository {
    public LoanRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public Class<Loan> getEntityClass() {
        return Loan.class;
    }

    @Override
    public Loan findLoanForStudentInTerm(int year, String termType, Student student, String loanType) {
        TypedQuery<Loan> query = getEntityManager().createQuery("""
                select l from Loan l , Term t where l.term=t and  t.year = ?1 and t.termType = ?2 and l.student = ?3
                 and l.loanType = ?4
                """, Loan.class);
        query.setParameter(1, year);
        query.setParameter(2, TermType.valueOf(termType));
        query.setParameter(3, student);
        query.setParameter(4, LoanType.valueOf(loanType));

        return query.getSingleResult();
    }
    @Override
    public Loan findStudentMortgage(Student student, Degree degree, LoanType loanType) {
        TypedQuery<Loan> query = getEntityManager().createQuery("""
                select l from Loan l where l.student = ?1 and l.degree = ?2 and l.loanType = ?3
                """, Loan.class);
        query.setParameter(1, student);
        query.setParameter(2, degree);
        query.setParameter(3, loanType);

        return query.getSingleResult();
    }
}
