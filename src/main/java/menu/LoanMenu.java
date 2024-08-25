package menu;

import entity.*;
import enumaration.AdmissionType;
import enumaration.Degree;
import enumaration.LoanType;
import enumaration.TermType;
import exceptions.CreditCardExceptions;
import exceptions.LoanExceptions;
import jakarta.persistence.Tuple;
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
    private final LoanCreditCardService loanCreditCardService;

    public LoanMenu(TermService termService, LoanService loanService, BankService bankService, AccountService accountService, CreditCardService creditCardService, LoanCreditCardService loanCreditCardService) {
        this.termService = termService;
        this.loanService = loanService;
        this.bankService = bankService;
        this.accountService = accountService;
        this.creditCardService = creditCardService;
        this.loanCreditCardService = loanCreditCardService;
    }


    public void showLoanMenu(Student token, LocalDate currentDate) {
        Scanner input = new Scanner(System.in);
        boolean continueRunning = true;
        while (continueRunning) {
            System.out.println("""
                        Loan Menu:
                        1. Register Education Loan
                        2. Register Tuition Loan
                        3. Register Mortgage
                        4. Exit
                    """);
            System.out.print("Option: ");
            String stringOption = input.nextLine();
            if (stringOption == null || stringOption.isEmpty()) {
                System.out.println("Input can not be null or empty");
                showLoanMenu(token, currentDate);
                break;
            }
            try {
                int option = Integer.parseInt(stringOption);

                switch (option) {
                    case 1 -> registerEducationLoan(token, currentDate, input, LoanType.Education);
                    case 2 -> registerTuitionLoan(token, currentDate, input);
                    /* case 3 -> */
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

    private void registerTuitionLoan(Student token, LocalDate currentDate, Scanner input) {

        //-------------------------------------------------------------------------
        if (token.getAdmissionType() == AdmissionType.Daytime) {
            System.out.println("you are not authorise to select the Tuition Loan");
            return;
        }
        registerEducationLoan(token, currentDate, input, LoanType.TuitionFee);

    }

    private void registerEducationLoan(Student token, LocalDate currentDate, Scanner input, LoanType loanType) {
        String termType;
        termType = getCurrentTermType(currentDate);
        if (checkStudentGetLoanInTermOfYear(token, currentDate, termType, String.valueOf(loanType)))
            return;

        //-------------------------------------------------------------------------
        //get credit card from student:
        boolean conditions;
        String cardNumber;
        String cvv2 = null;
        LocalDate expirationDate = null;
        CreditCard creditCard = null;
        Account account = null;
        do {

            conditions = false;

            cardNumber = enterCardNumber(input);

            try {
                Tuple result = creditCardService.findByCardNumber(cardNumber);
                creditCard = result.get("creditCard", CreditCard.class);
                account = result.get("account", Account.class);
            } catch (CreditCardExceptions.NotFoundException e6) {
                cvv2 = enterCvv2(input);
                boolean expireDateCondition = true;
                do {
                    try {
                        expirationDate = enterExpirationDate(input);
                    } catch (Exception e) {
                        System.out.println("invalid number! input the number");
                    }
                    if (expirationDate != null) expireDateCondition = false;
                } while (expireDateCondition);

                conditions = checkCardIsExpired(currentDate, conditions, expirationDate);
                if (conditions) System.out.println("Enter the Card Number that is not expired");

            }

        } while (conditions);

        if (creditCard == null) {
            //-------------------------------------------------------------------------
            //get bank
            String bankName = enterBankName(input);

            Bank bank = bankService.findByName(bankName);
            //------------------------------------------------------------------------------
            //get account number and create Account Object and save in DB
            account = Account.builder().student(token).bank(bank).build();

            //--------------create object credit card and save in DB:
            creditCard = CreditCard.builder().account(account).cardNumber(cardNumber)
                    .cvv2(cvv2).expirationDate(expirationDate).build();

            account = accountService.save(account);
            creditCard = creditCardService.save(creditCard);
            System.out.println("creditCard save ");
        }
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
        Double amountOfLoan = getAmountOfLoan(degree, loanType);


        Loan loan = Loan.builder().loanType(loanType).student(token).amount(amountOfLoan)
                .degree(degree).term(termAfterSaveInDB).build();
        Loan loanAfterSave = loanService.save(loan);
        //update account
        if (account != null)
            account.setBalance(amountOfLoan);
        accountService.save(account);
        //-------------------------------------------------------------------------
        //add new table with card loanAfterSave --> id_crd and id_loan loan_creditCard
        loanCreditCardService.save(LoanCreditCard.builder().loan(loanAfterSave).creditCard(creditCard).build());
        //-------------------------------------------------------------------------
        //create payment table of this loan
        ///TO//DO

        System.out.println("The operation was successful.");
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
                expirationDate = LocalDate.of(Integer.parseInt(yearOfExpirationDate),
                        Integer.parseInt(monthExpirationDate), Integer.parseInt(dayOfExpirationDate));

            }
        }
        return expirationDate;
    }

    private static boolean checkCardIsExpired(LocalDate currentDate, boolean conditions, LocalDate expirationDate) {
        if (currentDate.getYear() < expirationDate.getYear()) {
            System.out.println();
        } else if (currentDate.getYear() == expirationDate.getYear()) {
            if (currentDate.getMonthValue() < expirationDate.getDayOfMonth()) {
                System.out.println();
            } else if (currentDate.getMonthValue() == expirationDate.getDayOfMonth()) {
                if (currentDate.getDayOfMonth() <= expirationDate.getDayOfMonth()) System.out.println();
            }
        } else {
            System.out.println("your card is expired! Please Enter an unexpired card");
            conditions = true;
        }

        return conditions;
    }

    private static Double getAmountOfLoan(Degree degree, LoanType loanType) {
        double amountOfLoan = 0;
        switch (loanType) {
            case Education -> {
                switch (degree) {
                    case Associate, Continuous_Bachelor,
                            DisContinuous_Bachelor -> amountOfLoan = 1900000.0;
                    case IntegratedMaster, DisContinuousMaster, ProfessionalDoctorate,
                            IntegratedDoctorate -> amountOfLoan = 2250000.0;
                    case PhD -> amountOfLoan = 2600000.0;
                    default -> amountOfLoan = 0.0;
                }

            }
            case TuitionFee -> {
                switch (degree) {
                    case Associate, Continuous_Bachelor,
                            DisContinuous_Bachelor -> amountOfLoan = 1300000.0;
                    case IntegratedMaster, DisContinuousMaster, ProfessionalDoctorate,
                            IntegratedDoctorate -> amountOfLoan = 2600000.0;
                    case PhD -> amountOfLoan = 65000000.0;
                    default -> amountOfLoan = 0.0;
                }
            }
            default -> amountOfLoan = 0.0;
        }
        return amountOfLoan;
    }

    private String enterBankName(Scanner input) {
        boolean conditions = true;
        String bankName;
        do {
            do {
                System.out.print("""
                        enter the Bank name :
                        Meli
                        Refah
                        Tejarat
                        Maskan
                        """);
                bankName = input.nextLine();
            } while (!fillInputString(bankName));

            if (bankName.equals("Meli") || bankName.equals("Refah") || bankName.equals("Tejarat")
                || bankName.equals("Maskan")) {
                System.out.println("Enter the bank name of list!");
                conditions = false;
            }


        } while (conditions);
        return bankName;
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
            System.out.print("Enter the CardNumber (6 digit): ");
            cardNumber = input.nextLine();
        } while (!fillInputNumbers(cardNumber, 6));
        return cardNumber;
    }


    private boolean checkStudentGetLoanInTermOfYear(Student token, LocalDate currentDate, String termType, String loanType) {
        try {
            loanService.findLoanForStudentInTerm(currentDate.getYear(),
                    termType, token, loanType);
            System.out.println("You have received a loan this semester");
            return true;
        } catch (LoanExceptions.DatabaseAccessException e) {
            System.out.println(e.getMessage());
            System.out.println("this student has not received a loan this semester");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private String getCurrentTermType(LocalDate currentDate) {
        String termType = null;
        if (isAbanRange(currentDate)) {
            termType = String.valueOf(TermType.Autumn);
        } else if (isBahmanRange(currentDate))
            termType = String.valueOf(TermType.Winter);
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

    private boolean isBahmanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 13);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 19);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }
}
