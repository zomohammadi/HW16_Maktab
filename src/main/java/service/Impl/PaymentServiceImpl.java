package service.Impl;

import entity.Loan;
import entity.Payment;
import enumaration.Degree;
import repository.BaseEntityRepository;
import service.PaymentService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaymentServiceImpl implements PaymentService {
    private final BaseEntityRepository<Payment> paymentBaseEntityRepository;

    public PaymentServiceImpl(BaseEntityRepository<Payment> paymentBaseEntityRepository) {
        this.paymentBaseEntityRepository = paymentBaseEntityRepository;
    }

    @Override
    public void createPaymentsForLoan(Loan loan) {

        Double totalAmount = null;
        totalAmount = getTotalAmount(loan, totalAmount);

        int numberOfInstallments = 372; // method baraye mohasebe in bayad beneisi
        double amountPerInstallment = totalAmount / numberOfInstallments;
        //round 500/345
        amountPerInstallment = Math.round(amountPerInstallment * 1000.0) / 1000.0;

        // Assuming payments start from the current date and are monthly
        LocalDate startDate = calculateStartDateOfLoanPayment(loan);

        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {  //in 60 bayad moteghayer bashe to in method
            if ((i-1)!=0 && (i-1) % 12 == 0) {
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

    private LocalDate calculateStartDateOfLoanPayment(Loan loan) {
        int entryYear = loan.getStudent().getEntryYear();
        int endOfGraduation;
        Degree degree = loan.getStudent().getDegree();
        switch (degree) {
            case Associate, DisContinuous_Bachelor,
                    DisContinuousMaster -> endOfGraduation = entryYear + 3;
            case Continuous_Bachelor -> endOfGraduation = entryYear + 5;
            case IntegratedMaster -> endOfGraduation = entryYear + 6;
            case ProfessionalDoctorate, IntegratedDoctorate, PhD -> endOfGraduation = entryYear + 7;
            default -> endOfGraduation = entryYear;
        }
        return LocalDate.of(endOfGraduation, Month.JUNE, 22);
    }

    private static Double getTotalAmount(Loan loan, Double totalAmount) {
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
