package menu;

import entity.Student;
import jakarta.persistence.Tuple;
import service.PaymentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RepaymentMenu {
    private final PaymentService paymentService;

    public RepaymentMenu(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void showRepaymentMenu(Student token) {
        Scanner input = new Scanner(System.in);
        boolean continueRunning = true;
        while (continueRunning) {
            System.out.println("""
                        Loan Menu:
                        1. Paid Installments
                        2. Unpaid installments
                        3. Installment payment
                        4. Exit
                    """);
            System.out.print("Option: ");
            String stringOption = input.nextLine();
            if (stringOption == null || stringOption.isEmpty()) {
                System.out.println("Input can not be null or empty");
                showRepaymentMenu(token);
                break;
            }
            try {
                int option = Integer.parseInt(stringOption);

                switch (option) {
                    case 1 -> paidInstallments(token);

                    case 2 -> unpaidInstallments(token);

//                    case 3 ->;
                    case 4 -> continueRunning = false;
                    default -> System.out.println("Wrong option!");
                }
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Wrong option!");
                }
            }
        }
    }

    private void unpaidInstallments(Student token) {
        List<Tuple> result = paymentService.showUnPaidInstallments(token);
        if (!result.isEmpty()) {
            System.out.println(" Loan type , request date , installment Number , amount Per Installment, prepayment Date");
            result.forEach(System.out::println);
        } else {
            System.out.println("You do not have an unpaid loan");
        }
    }

    private void paidInstallments(Student token) {
        List<Tuple> result = paymentService.showPaidInstallments(token);
        if (!result.isEmpty()) {
            System.out.println(" Loan type , request date , installment Number , amount Per Installment, prepayment Date");
            result.forEach(System.out::println);
        } else {
            System.out.println("You do not have a paid loan ");
        }
    }
}