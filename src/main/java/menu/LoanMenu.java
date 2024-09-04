package menu;

import entity.*;
import enumaration.*;
import exceptions.BankExceptions;
import exceptions.CreditCardExceptions;
import exceptions.LoanExceptions;
import exceptions.StudentExceptions;
import jakarta.persistence.Tuple;
import service.*;
import util.ApplicationContext;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

public class LoanMenu {

    private final TermService termService;
    private final LoanService loanService;
    private final BankService bankService;
    private final AccountService accountService;
    private final CreditCardService creditCardService;
    private final StudentService studentService;
    private final MortgageDetailService mortgageDetailService;
    private final PaymentService paymentService;

    public LoanMenu(TermService termService, LoanService loanService, BankService bankService, AccountService accountService, CreditCardService creditCardService, StudentService studentService, MortgageDetailService mortgageDetailService, PaymentService paymentService) {
        this.termService = termService;
        this.loanService = loanService;
        this.bankService = bankService;
        this.accountService = accountService;
        this.creditCardService = creditCardService;
        this.studentService = studentService;
        this.mortgageDetailService = mortgageDetailService;
        this.paymentService = paymentService;
    }


    public void showLoanMenu(Student token, ZonedDateTime currentDate) {
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
                    case 3 -> registerMortgage(token, currentDate, input);
                    case 4 -> continueRunning = false;
                    default -> System.out.println("Wrong option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong option!");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();  // This will print the stack trace for debugging
            }
        }
    }

    private void registerMortgage(Student token, ZonedDateTime currentDate, Scanner input) {
        if (!token.isMarried()) {
            System.out.println("You are not permit to Register Mortgage");
        } else {
            if (token.isHaveDormitory()) {
                System.out.println("You are not permit to Register Mortgage! because you have Dormitory");
            }
        }
        Loan loan;
        Student student = null;
        try {
            loan = loanService.findStudentMortgage(token, token.getDegree(), LoanType.Mortgage);
            if (loan != null) {
                System.out.println("You are currently getting a mortgage at this point");
                return;
            }
        } catch (LoanExceptions.NotFoundException e) {
            try {
                student = studentService.findStudentByNationalCode(token.getPartnerCode());

                //refresh student to ensure this is up-to-date
                ApplicationContext.getInstance().getEntityManager().refresh(student);
            } catch (StudentExceptions.NotFoundException e3) {

                loanOperation(token, input, currentDate);

            }
            if (student != null) {
                try {
                    loan = loanService.findStudentMortgage(student, student.getDegree(), LoanType.Mortgage);
                    if (loan != null) {
                        System.out.println("""
                                Your partner received a mortgage while studying
                                Therefore, you are not allowed to get a mortgage.
                                         """);
                        return;
                    }
                } catch (LoanExceptions.NotFoundException e1) {
                    loanOperation(token, input, currentDate);

                }
            }
        }
    }

    private void loanOperation(Student token, Scanner input, ZonedDateTime currentDate) {
        Loan loan;
        Double amountOfLoan = getAmountOfLoan(
                token.getUniversity().getCity().getTypeOfCity()
                , token);
        String contractNumber;
        String address;
        do {
            System.out.print("Enter the Contract Number (13 digit): ");
            contractNumber = input.nextLine();
        } while (!fillInputNumbers(contractNumber, 13));

        System.out.print("Enter the address: ");
        address = input.nextLine();


        //get CreditCard
        boolean conditions;
        String cardNumber;
        String cvv2 = null;
        ZonedDateTime expirationDate = null;
        Account account = null;
        CreditCard creditCard = null;
        do {
            conditions = false;
            cardNumber = enterCardNumber(input);
            try {
                Tuple result = creditCardService.findByCardNumber(cardNumber);
                creditCard = result.get("creditCard", CreditCard.class);
                account = result.get("account", Account.class);
                Student student = result.get("student", Student.class);
                if (!student.equals(token)) {
                    System.out.println("The entered card number has already been entered by someone else.");
                    conditions = true;
                    creditCard = null;
                    account = null;
                }
            } catch (CreditCardExceptions.NotFoundException e6) {
                cvv2 = enterCvv2(input);
                boolean expireDateCondition = true;
                do {
                    try {
                        expirationDate = enterExpirationDate(input);
                    } catch (Exception e4) {
                        System.out.println("invalid number! input the number");
                    }
                    if (expirationDate != null) expireDateCondition = false;
                } while (expireDateCondition);

                conditions = checkCardIsExpired(currentDate, false, expirationDate);
                if (conditions) System.out.println("Enter the Card Number that is not expired");

            }

        } while (conditions);

        if (creditCard == null) {
            //-------------------------------------------------------------------------
            //get bank
            Bank bank;
            bank = getBank(input, null);
            if (bank == null) return;

            //------------------------------------------------------------------------------
            //get account number and create Account Object and save in DB
            account = Account.builder().student(token).bank(bank).build();

            //--------------create object credit card and save in DB:
            creditCard = CreditCard.builder().account(account).cardNumber(cardNumber)
                    .cvv2(cvv2).expirationDate(expirationDate).build();

            accountService.save(account);
            creditCard = creditCardService.save(creditCard);
            System.out.println("creditCard save ");
        }

        loan = Loan.builder().loanType(LoanType.Mortgage).student(token)
                .amount(amountOfLoan).degree(token.getDegree()).build();
        LoanCreditCard loanCreditCard = LoanCreditCard.builder()
                .creditCard(creditCard).loan(loan).build();
        loan.setLoanCreditCard(loanCreditCard);
        //create payment
        paymentService.createPaymentsForLoan(loan);

        loan = loanService.save(loan);
        if (account != null)
            account.setBalance(amountOfLoan + account.getBalance());
        accountService.update(account);
        MortgageDetail mortgageDetail = MortgageDetail.builder().loan(loan).address(address)
                .contractNumber(contractNumber).build();

        mortgageDetailService.save(mortgageDetail);

        System.out.println("The operation was successful.");
    }

    private void registerTuitionLoan(Student token, ZonedDateTime currentDate, Scanner input) {

        //-------------------------------------------------------------------------
        if (token.getAdmissionType() == AdmissionType.Daytime) {
            System.out.println("you are not authorise to select the Tuition Loan");
            return;
        }
        registerEducationLoan(token, currentDate, input, LoanType.TuitionFee);

    }

    private void registerEducationLoan(Student token, ZonedDateTime currentDate, Scanner input, LoanType loanType) {
        String termType;
        termType = getCurrentTermType(currentDate);
        if (checkStudentGetLoanInTermOfYear(token, currentDate, termType, String.valueOf(loanType)))
            return;

        //-------------------------------------------------------------------------
        //get credit card from student:
        boolean conditions;
        String cardNumber;
        String cvv2 = null;
        ZonedDateTime expirationDate = null;
        Account account = null;
        CreditCard creditCard = null;
        do {
            conditions = false;
            cardNumber = enterCardNumber(input);
            try {
                Tuple result = creditCardService.findByCardNumber(cardNumber);
                creditCard = result.get("creditCard", CreditCard.class);
                account = result.get("account", Account.class);
                Student student = result.get("student", Student.class);
                if (!student.equals(token)) {
                    System.out.println("The entered card number has already been entered by someone else.");
                    conditions = true;
                    creditCard = null;
                    account = null;
                }
            } catch (CreditCardExceptions.NotFoundException e6) {
                cvv2 = enterCvv2(input);
                boolean expireDateCondition = true;
                do {
                    try {
                        expirationDate = enterExpirationDate(input);
                    } catch (Exception e4) {
                        System.out.println("invalid number! input the number");
                    }
                    if (expirationDate != null) expireDateCondition = false;
                } while (expireDateCondition);

                conditions = checkCardIsExpired(currentDate, false, expirationDate);
                if (conditions) System.out.println("Enter the Card Number that is not expired");

            }

        } while (conditions);

        if (creditCard == null) {
            //-------------------------------------------------------------------------
            //get bank
            Bank bank;
            bank = getBank(input, null);
            if (bank == null) return;
            //------------------------------------------------------------------------------
            //get account number and create Account Object and save in DB
            account = Account.builder().student(token).bank(bank).balance(0.0).build();

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

        LoanCreditCard loanCreditCard = LoanCreditCard.builder()
                .loan(loan).creditCard(creditCard).build();
        loan.setLoanCreditCard(loanCreditCard);

        //create payment
        paymentService.createPaymentsForLoan(loan);

        /*Loan loanAfterSave =*/
        loanService.save(loan);
        //update account
        if (account != null) {
            account.setBalance(amountOfLoan + account.getBalance());
            accountService.update(account);
        }
        System.out.println("The operation was successful.");
    }

    private Bank getBank(Scanner input, Bank bank) {
        List<Bank> bankList;
        try {
            bankList = bankService.findAll();
        } catch (BankExceptions.NotFoundException | BankExceptions.DatabaseException e) {
            System.out.println(e.getMessage());
            return null;
        }

        //boolean bankCondition = true;
        // do {
        String bankName = enterBankName(input);
        for (Bank b : bankList) {
            if (b.getName().equalsIgnoreCase(bankName)) {
                //bankCondition = false;
                bank = b;
                break;
            }
        }
        // } while (bankCondition);
        return bank;
    }

    private Double getAmountOfLoan(Degree degree, LoanType loanType) {
        double amountOfLoan;
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

    private Double getAmountOfLoan(TypeOfCity typeOfCity, Student student) {
        double amountOfLoan;
        // if (loanType == LoanType.Mortgage) {
        switch (typeOfCity) {
            case Metropolis -> {
                if (student.getUniversity().getCity().getName().equalsIgnoreCase("tehran"))
                    amountOfLoan = 32000000.0;
                else amountOfLoan = 26000000.0;
            }
            case OtherCities -> amountOfLoan = 19500000.0;
            default -> amountOfLoan = 0.0;
        }
        //   }
        return amountOfLoan;
    }

    private ZonedDateTime enterExpirationDate(Scanner input) {
        ZonedDateTime expirationDate = null;
        System.out.print("expiration date:  ");
        System.out.print("Enter the Year of expiration date:  ");
        String yearOfExpirationDate = input.nextLine();
        if (fillInputNumbersWithMinAndMaxDate(yearOfExpirationDate, 2019, 2028)) {
            System.out.print("Enter the Month Of expiration date: ");
            String monthExpirationDate = input.nextLine();
            if (fillInputNumbersWithMinAndMaxDate(monthExpirationDate, 1, 12)) {
                String dayOfExpirationDate = getDayOfExpirationDate(input, monthExpirationDate);
                LocalDate localDate = LocalDate.of(
                        Integer.parseInt(yearOfExpirationDate),
                        Integer.parseInt(monthExpirationDate),
                        Integer.parseInt(dayOfExpirationDate)
                );

                expirationDate = localDate.atStartOfDay(ZonedDateTime.now().getZone());

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

    private boolean checkCardIsExpired(ZonedDateTime currentDate, boolean conditions, ZonedDateTime expirationDate) {
        if (currentDate.getYear() < expirationDate.getYear()) {
            System.out.print("");
        } else if (currentDate.getYear() == expirationDate.getYear()) {
            if (currentDate.getMonthValue() < expirationDate.getMonthValue()) {
                System.out.print("");
            } else if (currentDate.getMonthValue() == expirationDate.getMonthValue()) {
                if (currentDate.getDayOfMonth() <= expirationDate.getDayOfMonth()) System.out.println();
                System.out.print("");
            }
        } else {
            System.out.println("your card is expired! Please Enter an unexpired card");
            conditions = true;
        }
        return conditions;
    }

    private String enterBankName(Scanner input) {
        boolean conditions;
        String bankName = "";
        String bankNameInput;
        do {
            System.out.print("""
                    enter the Bank name :
                    Meli (Enter 0)
                    Refah (Enter 1)
                    Tejarat (Enter 2)
                    Maskan (Enter 3)
                    """);
            bankNameInput = input.nextLine();
            switch (bankNameInput) {
                case "0" -> {
                    bankName = "Meli";
                    conditions = false;
                }
                case "1" -> {
                    bankName = "Refah";
                    conditions = false;
                }
                case "2" -> {
                    bankName = "Tejarat";
                    conditions = false;
                }
                case "3" -> {
                    bankName = "Maskan";
                    conditions = false;
                }
                default -> {
                    System.out.println("Enter the valid number: ");
                    conditions = true;
                }
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
            System.out.print("Enter the CardNumber (16 digit): ");
            cardNumber = input.nextLine();
        } while (!fillInputNumbers(cardNumber, 16));
        return cardNumber;
    }


    private boolean checkStudentGetLoanInTermOfYear(Student token, ZonedDateTime currentDate, String termType, String
            loanType) {
        try {
            loanService.findLoanForStudentInTerm(currentDate.getYear(),
                    termType, token, loanType);
            System.out.println("You have received a loan this semester");
            return true;
        } catch (LoanExceptions.NotFoundException e) {
            System.out.println("this student has not received a loan this semester");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private String getCurrentTermType(ZonedDateTime currentDate) {
        String termType = null;
        if (isAbanRange(currentDate)) {
            termType = String.valueOf(TermType.Autumn);
        } else if (isBahmanRange(currentDate))
            termType = String.valueOf(TermType.Winter);
        return termType;
    }

    private boolean isAbanRange(ZonedDateTime currentDate) {
        ZonedDateTime startOfRange = ZonedDateTime.of(currentDate.getYear(), 10, 22
                , 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime endOfRange = ZonedDateTime.of(currentDate.getYear(), 10, 28
                , 23, 59, 59, 0, ZonedDateTime.now().getZone());

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

    private boolean checkedNullInput(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Input can not be null or empty");
            return true;
        }
        return false;
    }

    private boolean isBahmanRange(ZonedDateTime currentDate) {
        ZonedDateTime startOfRange = ZonedDateTime.of(currentDate.getYear(), 2, 13,
                0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime endOfRange = ZonedDateTime.of(currentDate.getYear(), 2, 19
                , 23, 59, 59, 0, ZonedDateTime.now().getZone());

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }
}
