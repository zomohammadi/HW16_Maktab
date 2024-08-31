package repository.Impl;

import entity.Payment;
import entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import repository.PaymentRepository;

import java.util.List;

public class PaymentRepositoryImp extends BaseEntityRepositoryImpl<Payment> implements PaymentRepository {

    public PaymentRepositoryImp(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Payment> getEntityClass() {
        return Payment.class;
    }

    @Override
    public List<Tuple> showPaidInstallments(Student student) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                select l.loanType as loan_type,l.createDate as request_date,
                p.installmentNumber as installment_Number,p.amountPerInstallment as amount_Per_Installment,
                p.prepaymentDate as prepayment_Date from Payment p
                , Loan l,Student s where l.student = ?1 and p.isPayed = ?2 and p.loan=l and l.student=s
                                """, Tuple.class);
        query.setParameter(1, student);
        query.setParameter(2, true);
        return query.getResultList();
    }

    @Override
    public List<Tuple> showUnPaidInstallments(Student student) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                select l.loanType as loan_type,l.createDate as request_date,
                p.installmentNumber as installment_Number,p.amountPerInstallment as amount_Per_Installment,
                p.prepaymentDate as prepayment_Date from Payment p
                , Loan l,Student s where l.student = ?1 and p.isPayed = ?2 and p.loan=l and l.student=s
                                """, Tuple.class);
        query.setParameter(1, student);
        query.setParameter(2, false);
        return query.getResultList();
    }

    /*
    select p.id, p.amount_per_installment, p.installment_number, l.loan_type, l.create_date, p.prepayment_date
from payment p
         join loan l on l.id = p.loan_id
where p.is_payed = false
  and (p.loan_id, p.prepayment_date) in (select g.loan_id, min(g.prepayment_date)
                                    from payment g
                                    where g.is_payed = false
                                    group by g.loan_id);
     */
    @Override
    public List<Tuple> listOfLoanThatMustBePayed(Long id) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                select p.id, p.amountPerInstallment as amount_per_installment
                , p.installmentNumber as installment_number, l.loanType as loan_type,
                 l.createDate as request_date, p.prepaymentDate as prepayment_date
                from Payment p
                         join Loan l on l = p.loan
                         join Student s on l.student = s
                where p.isPayed = false
                and s.id = ?1
                and (p.loan, p.prepaymentDate) in (select g.loan, min(g.prepaymentDate)
                                                    from Payment g
                                                    where g.isPayed = false
                                                    group by g.loan )
                                """, Tuple.class);

        query.setParameter(1, id);
        return query.getResultList();
    }
}
