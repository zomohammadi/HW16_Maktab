package service.Impl;

import entity.Loan;
import entity.Payment;
import entity.Student;
import exceptions.PaymentExceptions;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import repository.BaseEntityRepository;
import repository.PaymentRepository;
import service.PaymentService;
import util.ApplicationContext;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BaseEntityRepository<Payment> paymentBaseEntityRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BaseEntityRepository<Payment> paymentBaseEntityRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentBaseEntityRepository = paymentBaseEntityRepository;
    }

    @Override
    public void createPaymentsForLoan(Loan loan) {

        Double totalAmount = null;
        totalAmount = getTotalAmount(loan, totalAmount);
        int numberOfPaymentYear = 5;
        int numberOfMonth = numberOfPaymentYear * 12;

        int numberOfInstallments = calculateNumberOfInstallments(numberOfPaymentYear);
        double amountPerInstallment = totalAmount / numberOfInstallments;
        //round 500.3458788 to 500.346
        amountPerInstallment = Math.round(amountPerInstallment * 1000.0) / 1000.0;

        // Assuming payments start from the current date and are monthly
        ZonedDateTime startDate = ApplicationContext.getInstance()
                .getStudentService().calculateGraduationDate(loan.getStudent());

        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i <= numberOfMonth; i++) {
            if ((i - 1) != 0 && (i - 1) % 12 == 0) {
                amountPerInstallment *= 2;
            }
            Payment payment = Payment.builder()
                    .loan(loan)
                    .installmentNumber(i)
                    .amountPerInstallment(amountPerInstallment)
                    .prepaymentDate(startDate.plusMonths(i - 1))
                    .isPayed(false)
                    .build();

            payments.add(payment);
        }
        loan.setPayments(payments);
    }

    private int calculateNumberOfInstallments(int numberOfPaymentYear) {
        int numberOfInstallments = 0;
        for (int i = 0; i < numberOfPaymentYear; i++) {
            numberOfInstallments += Math.pow(2, i) * 12;
        }
        return numberOfInstallments;
    }

    @Override
    public List<Tuple> showPaidInstallments(Student student) {
        return paymentRepository.showPaidInstallments(student);

    }

    @Override
    public List<Tuple> showUnPaidInstallments(Student student) {
        return paymentRepository.showUnPaidInstallments(student);

    }

    @Override
    public List<Tuple> listOfLoanThatMustBePayed(Long id) {
        try {
            return paymentRepository.listOfLoanThatMustBePayed(id);
        } catch (NoResultException e) {
            throw new PaymentExceptions.NotFoundException(e.getMessage());
        }
    }

    @Override
    public int update(Long paymentId, Long studentId, String cardNumber, String cvv2, LocalDate expirationDate) {
        try {
            Payment payment = paymentBaseEntityRepository.findById(paymentId);
            return paymentRepository.update(payment, studentId, cardNumber, cvv2, expirationDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Double getTotalAmount(Loan loan, Double totalAmount) {
        Double amount = loan.getAmount();
        if (amount != null) {
            // Calculate 4% of the amount
            Double percentage = 0.04 * amount;

            // Calculate total amount
            totalAmount = amount + percentage;
        }
        return totalAmount;
    }

}
