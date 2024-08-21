package menu;

import entity.*;
import enumaration.Degree;
import enumaration.LoanType;
import enumaration.TermType;
import exceptions.LoanExceptions;
import service.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

public class LoanMenu {

    private final TermService termService;
    private final LoanService loanService;
    private final BankService bankService;
    private final AccountService accountService;
    private final CreditCardService creditCardService;

    public LoanMenu(TermService termService, LoanService loanService, BankService bankService, AccountService accountService, CreditCardService creditCardService) {
        this.termService = termService;
        this.loanService = loanService;
        this.bankService = bankService;
        this.accountService = accountService;
        this.creditCardService = creditCardService;
    }


    public void showTweetMenu(Student token, LocalDate currentDate) {
        Scanner input = new Scanner(System.in);
        boolean condition = true;
        while (condition) {
            System.out.println("""
                        Loan Menu:
                        1. Register Education Loan
                        2. Register Tuition Loan
                        3. Register Mortgage
                        4. Exit
                    """);
            int option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1 -> {
                    String termType;
                    termType = getCurrentTermType(currentDate);
                    try {
                        Loan loan = loanService
                                .findLoanForStudentInTerm(currentDate.getYear(),
                                        termType, token, String.valueOf(LoanType.Education));
                        System.out.println("You have received a loan this semester");
                        return;
                    } catch (LoanExceptions.DatabaseAccessException e) {
                        System.out.println(e.getMessage());
                        System.out.println("this student has not received a loan this semester");
                    }

                    //-------------------------------------------------------------------------
                    //get credit card from student:
                    String cardNumber;
                    String cvv2;
                    LocalDate expirationDate = null;
                    boolean conditions = true;
                    do {
                        System.out.print("Enter the CardNumber (6 digit): ");
                        cardNumber = input.nextLine();
                    } while (!fillInputNumbers(cardNumber, 6));
                    do {
                        System.out.print("Enter the CardNumber (3 digit): ");
                        cvv2 = input.nextLine();
                    } while (!fillInputNumbers(cvv2, 3));
                    do {
                        System.out.print("expiration date:  ");
                        System.out.print("Enter the Year of expiration date:  ");
                        String yearOfExpirationDate = input.nextLine();
                        if (fillInputNumbersWithMinAndMaxYear(yearOfExpirationDate, 2019, 2028)) {
                            //if (Integer.parseInt(yearOfExpirationDate) != currentDate.getYear()) {
                            System.out.print("Enter the Month Of expiration date: ");
                            String monthExpirationDate = input.nextLine();
                            if (fillInputNumbersWithMinAndMaxYear(monthExpirationDate, 1, 12)) {
                                System.out.print("Enter the Day Of expiration date: ");
                                String dayOfExpirationDate = input.nextLine();
                                switch (Integer.parseInt(monthExpirationDate)) {
                                    case 1, 3, 5, 6, 7, 8, 10, 12 ->
                                            fillInputNumbersWithMinAndMaxYear(dayOfExpirationDate,
                                                    1, 31);
                                    case 4, 9, 11 -> fillInputNumbersWithMinAndMaxYear(dayOfExpirationDate,
                                            1, 30);
                                    default -> fillInputNumbersWithMinAndMaxYear(dayOfExpirationDate,
                                            1, 29);
                                }
                                expirationDate = LocalDate.of(Integer.parseInt(yearOfExpirationDate),
                                        Integer.parseInt(monthExpirationDate), Integer.parseInt(dayOfExpirationDate));
                                if (currentDate.getYear() < Integer.parseInt(yearOfExpirationDate)
                                    && currentDate.getMonthValue() < Integer.parseInt(monthExpirationDate)
                                    && currentDate.getDayOfMonth() < Integer.parseInt(dayOfExpirationDate)) {
                                    System.out.println("your card is expired! ");
                                } else conditions = false;
                            }
                        }
                    } while (conditions);
                    //-------------------------------------------------------------------------
                    String bankName;
                    do {
                        do {
                            System.out.print("""
                                    enter the Bank name: 
                                    Meli
                                    Refah
                                    Tejarat
                                    Maskan
                                    """);
                            bankName = input.nextLine();
                        } while (!fillInputString(bankName));
                        if (bankName.equals("Meli") || bankName.equals("Refah") || bankName.equals("Tejarat")
                            || bankName.equals("Maskan")) {
                            System.out.println("enter the bank name of list!");
                            conditions = true;
                        }

                        conditions = false;
                    } while (conditions);

                    Bank bank = bankService.findByName(bankName);
                    //get account number and create Account Object and save in DB
                    //------------------------------------------------------------------------------
                    Account account = Account.builder().student(token).bank(bank).build();
                    accountService.save(account);
                    //--------------create object credit card and save in DB:
                    CreditCard creditCard = CreditCard.builder().account(account).cardNumber(cardNumber)
                            .cvv2(cvv2).expirationDate(expirationDate).build();

                    //-------------------------------------------------------------------------
                    Term term = Term.builder().termType(TermType.valueOf(termType)).
                            year(currentDate.getYear()).build();

                    //TODOo exception for save method for term
                    Term termAfterSaveInDB;
                    try {

                        termAfterSaveInDB = termService.save(term);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    Degree degree = token.getDegree();
                    Double amountOfLoan;
                    switch (degree) {
                        case Associate, Continuous_Bachelor,
                                DisContinuous_Bachelor -> amountOfLoan = 1900000.0;
                        case IntegratedMaster, DisContinuousMaster, ProfessionalDoctorate,
                                IntegratedDoctorate -> amountOfLoan = 2250000.0;
                        case PhD -> amountOfLoan = 2600000.0;
                        default -> amountOfLoan = 0.0;
                    }
                    creditCardService.save(creditCard);
                    System.out.println("creditCard save ");
                    Loan loan = Loan.builder().loanType(LoanType.Education).student(token).amount(amountOfLoan)
                            .degree(degree).term(termAfterSaveInDB).build();
                    loanService.save(loan);
                    System.out.println("done!");

                }
              /*  case 2 -> ;
                case 3 -> */
                case 4 -> condition = false;
                default -> System.out.println("Wrong option!");
            }
        }
    }

    private String getCurrentTermType(LocalDate currentDate) {
        String termType;
        if (isAbanRange(currentDate)) {
            termType = String.valueOf(TermType.Autumn);
        } else termType = String.valueOf(TermType.Winter);
        return termType;
    }

    private boolean isAbanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 22);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 28);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
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

    private boolean fillInputNumbersWithMinAndMaxNumber(String input, int minDigit, int maxDigit) {
        if (checkedNullInput(input)) return false;
        if (input.length() > maxDigit || input.length() < minDigit) {
            System.out.println("input must be between " + minDigit + " and " + maxDigit + " digit number");
            return false;
        }
        return fillInputNumbers_v2(input);
    }

    private boolean fillInputNumbersWithMinAndMaxYear(String input, int minDigit, int maxDigit) {
        if (checkedNullInput(input)) return false;
        if (Integer.parseInt(input) > maxDigit || Integer.parseInt(input) < minDigit) {
            System.out.println("input must be between " + minDigit + " and " + maxDigit + " digit number");
            return false;
        }
        return fillInputNumbers_v2(input);
    }


    private boolean fillInputString(String input) {
        if (checkedNullInput(input)) return false;
        return fillInputString_v2(input);
    }

    private boolean fillInputString_v2(String input) {
        char[] chars = input.toCharArray();
        if (chars[0] == ' ') {
            System.out.println("can not start with space");
            return false;
        }
        for (char c : chars) {
            if ((int) c != 32) {
                if (!Character.isLetter(c)) {
                    System.out.println("Input must contain only letters between (a-z) or (A-Z)");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkedNullInput(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Input can not be null or empty");
            return true;
        }
        return false;
    }

    private Integer checkNumber(Scanner input) {
        String id = input.nextLine();
        if (id == null || id.isEmpty()) {
            System.out.println("Input can not be null or empty");
            return null;
        }
        char[] chars = id.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                System.out.println("Input must contain only digit between (0-9)");
                return null;
            }
        }
        return Integer.valueOf(id);
    }
}
