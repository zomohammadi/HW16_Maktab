package repository.Impl;

import entity.Payment;
import entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import repository.PaymentRepository;

import java.time.LocalDate;
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
    @SuppressWarnings("JpaQlInspection")
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

    @Override
    public List<Tuple> listOfLoanThatMustBePayed(Long id) {
        TypedQuery<Tuple> query = getEntityManager().createQuery("""
                    select p.id as id, p.amountPerInstallment as amount_per_installment
                    , p.installmentNumber as installment_number, l.loanType as loan_type,
                     l.createDate as request_date, p.prepaymentDate as prepayment_date
                    from Payment p
                             join Loan l on l = p.loan
                             join Student s on l.student = s
                    where p.isPayed = false
                    and s.id = ?1
                    and  p.prepaymentDate in (select  min(g.prepaymentDate)
                                                        from Payment g
                                                        where g.isPayed = false
                                                        and p.loan = g.loan
                                                        group by g.loan )
                                    """, Tuple.class);

        query.setParameter(1, id);
        return query.getResultList();
    }

    @Override
    public int update(Payment payment, Long studentId, String cardNumber, String cvv2, LocalDate expirationDate) {
        getEntityManager().getTransaction().begin();

        if (payment != null) {
            // Verify card details
            Query query = getEntityManager().createQuery("""
                        select count(c) from CreditCard c
                         join LoanCreditCard lc on lc.creditCard = c
                         join Loan l on lc.loan = l
                         where c.cardNumber = ?1 and c.cvv2 = ?2 and c.expirationDate = ?3 and l.student.id = ?4 and l.id =?5
                    """);
            query.setParameter(1, cardNumber);
            query.setParameter(2, cvv2);
            query.setParameter(3, expirationDate);
            query.setParameter(4, studentId);
            query.setParameter(5, payment.getLoan().getId());

            Long count = (Long) query.getSingleResult();
            if (count > 0) {
                // Update the payment status and persist
                payment.setPayed(true);
                getEntityManager().merge(payment);
                getEntityManager().getTransaction().commit();
                return 1;
            }
        }
        getEntityManager().getTransaction().rollback();
        return 0;
    }
}
/*    @Override
        public List<Tuple> listOfLoanThatMustBePayed(Long id) {
            TypedQuery<Tuple> query = getEntityManager().createQuery("""
                    select p.id as id, p.amountPerInstallment as amount_per_installment
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
        }*/

   /* @Override
    public int update(Long paymentId, Long studentId, String cardNumber, String cvv2, LocalDate expirationDate) {
        getEntityManager().getTransaction().begin();
        Query query = getEntityManager().createQuery("""
                update Payment p set p.isPayed = ?1 
                where exists (select l from Loan l 
                join LoanCreditCard lc on lc.loan = l
                join CreditCard  c on c = lc.creditCard
                where l=p.loan
                and c.cardNumber = ?2 and c.cvv2 = ?3 and c.expirationDate = ?4 and l.student.id = ?5 and p.id = ?6 )
                """);
        query.setParameter(1, true);
        query.setParameter(2, cardNumber);
        query.setParameter(3, cvv2);
        query.setParameter(4, expirationDate);
        query.setParameter(5, studentId);
        query.setParameter(6, paymentId);

        int result = query.executeUpdate();

        getEntityManager().getTransaction().commit();

        return result;
    }*/
