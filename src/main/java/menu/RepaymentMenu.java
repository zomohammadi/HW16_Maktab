package menu;

import entity.Student;
import exceptions.PaymentExceptions;
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

                    case 3 -> installmentPayment(token, input);
                    case 4 -> continueRunning = false;
                    default -> System.out.println("Wrong option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong option!");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void installmentPayment(Student token, Scanner input) {
        List<Tuple> result;
        try {
            result = paymentService.listOfLoanThatMustBePayed(token.getId());
            if (result.size() == 0) {
                System.out.println("You have no installments to pay");
                return;
            }
            result.forEach(System.out::println);
        } catch (PaymentExceptions.NotFoundException e) {
            System.out.println("You have no installments to pay" + e.getMessage());
            return;
        }


        //   checkInputIsEqualToList(input, result,paymentId);
        String paymentId;
        boolean condition;
        do {

            paymentId = enterNumber(input);

            if (checkInputIsEqualToList(result, paymentId)) {
                condition = false;
                break;
            } else {
                condition = true;
                System.out.println("Please choose a number from the above list ");
            }

        } while (condition);

        String cardNumber;
        String cvv2;
        LocalDate expirationDate;

        cardNumber = enterCardNumber(input);
        cvv2 = enterCvv2(input);
        boolean expireDateCondition = true;
        do {

            expirationDate = enterExpirationDate(input);

            if (expirationDate != null) expireDateCondition = false;
        } while (expireDateCondition);

        int updateResult = 0;
        try {

            updateResult = paymentService.update(Long.valueOf(paymentId),
                    token.getId(), cardNumber, cvv2, expirationDate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (updateResult == 0)
            System.out.println("No payable installments were found with these card details. ");
        else if (updateResult == 1) System.out.println("operation done");
    }

    private boolean checkInputIsEqualToList(List<Tuple> result, String paymentId) {
        for (Tuple tuple : result) {
            if (Long.valueOf(paymentId).equals(tuple.get("id", Long.class)))
                return true;
        }
        return false;
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

    private String enterCvv2(Scanner input) {
        String cvv2;
        do {
            System.out.print("Enter the cvv2 (3 digit): ");
            cvv2 = input.nextLine();
        } while (!fillInputNumbers(cvv2, 3));
        return cvv2;
    }

    private String enterCardNumber(Scanner input) {
        String cardNumber;
        do {
            System.out.print("Enter the CardNumber (16 digit): ");
            cardNumber = input.nextLine();
        } while (!fillInputNumbers(cardNumber, 16));
        return cardNumber;
    }

    private String enterNumber(Scanner input) {
        String number;
        do {
            System.out.print("Enter the Number of this list: ");
            number = input.nextLine();
        } while (!fillInputNumbers_v2(number));
        return number;
    }

    private LocalDate enterExpirationDate(Scanner input) {
        LocalDate expirationDate = null;
        System.out.print("expiration date:  ");
        System.out.print("Enter the Year of expiration date:  ");
        String yearOfExpirationDate = input.nextLine();
        if (fillInputNumbersWithMinAndMaxDate(yearOfExpirationDate, 2019, 2028)) {
            System.out.print("Enter the Month Of expiration date: ");
            String monthExpirationDate = input.nextLine();
            if (fillInputNumbersWithMinAndMaxDate(monthExpirationDate, 1, 12)) {
                String dayOfExpirationDate = getDayOfExpirationDate(input, monthExpirationDate);
                expirationDate = LocalDate.of(Integer.parseInt(yearOfExpirationDate),
                        Integer.parseInt(monthExpirationDate), Integer.parseInt(dayOfExpirationDate));

            }
        }
        return expirationDate;
    }

    private String getDayOfExpirationDate(Scanner input, String monthExpirationDate) {
        System.out.print("Enter the Day Of expiration date: ");
        String dayOfExpirationDate = input.nextLine();
        switch (Integer.parseInt(monthExpirationDate)) {
            case 1, 3, 5, 6, 7, 8, 10, 12 -> fillInputNumbersWithMinAndMaxDate(dayOfExpirationDate,
                    1, 31);
            case 4, 9, 11 -> fillInputNumbersWithMinAndMaxDate(dayOfExpirationDate,
                    1, 30);
            default -> fillInputNumbersWithMinAndMaxDate(dayOfExpirationDate,
                    1, 29);
        }
        return dayOfExpirationDate;
    }

    private boolean fillInputNumbersWithMinAndMaxDate(String input, int minDigit, int maxDigit) {
        if (checkedNullInput(input)) return false;
        try {
            if (Integer.parseInt(input) > maxDigit || Integer.parseInt(input) < minDigit) {
                System.out.println("input must be between " + minDigit + " and " + maxDigit + " digit number");
                return false;
            }
            return fillInputNumbers_v2(input);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private boolean checkedNullInput(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Input can not be null or empty");
            return true;
        }
        return false;
    }

    private boolean fillInputNumbers_v2(String input) {

        char[] chars = input.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                System.out.println("input must contain only numbers between (0-9)");
                return false;
            }
        }
        return true;
    }

    private boolean fillInputNumbers(String input, int digit) {
        if (checkedNullInput(input)) return false;
        if (input.length() != digit) {
            System.out.println("input must be " + digit + " digit number");
            return false;
        }
        return fillInputNumbers_v2(input);
    }

}
/*
    private boolean checkCardIsExpired(LocalDate currentDate, LocalDate expirationDate) {
        boolean conditions;
        if (currentDate.getYear() < expirationDate.getYear()) {
            conditions = false;
        } else if (currentDate.getYear() == expirationDate.getYear()) {
            if (currentDate.getMonthValue() < expirationDate.getMonthValue()) {
                conditions = false;
            } else if (currentDate.getMonthValue() == expirationDate.getMonthValue()) {
                if (currentDate.getDayOfMonth() <= expirationDate.getDayOfMonth()) System.out.println();
                conditions = false;
            } else {
                conditions = true;
                System.out.println("your card is expired! Please contact your bank");
            }
        } else {
            System.out.println("your card is expired! Please contact your bank");
            conditions = true;
        }
        return conditions;
    }*/
