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
}
