package menu;

import entity.Student;
import service.StudentService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

import static util.Utility.getDate;

public class StudentMenu {
    // private Student token = null;
    private final LoanMenu loanMenu;
    private final StudentService studentService;
    private final RepaymentMenu repaymentMenu;

    public StudentMenu(LoanMenu loanMenu, StudentService studentService, RepaymentMenu repaymentMenu) {
        this.loanMenu = loanMenu;
        this.studentService = studentService;
        this.repaymentMenu = repaymentMenu;
    }

    public void showStudentMenu(Student token) {
        Scanner input = new Scanner(System.in);
        boolean continueRunning = true;
        while (continueRunning) {
            System.out.println("""
                        Student Menu:
                        1. Loan Registration
                        2. Loan Repayment
                        3. Exit
                    """);
            System.out.print("Option: ");
            String stringOption = input.nextLine();
            if (stringOption == null || stringOption.isEmpty()) {
                System.out.println("Input can not be null or empty");
                showStudentMenu(token);
                break;
            }
            try {
                int option = Integer.parseInt(stringOption);
                /*LocalDate currentDate = getDate(date -> null);*/
                //LocalDate currentDate = getDate(date -> LocalDate.of(2020, 10, 23));
                //     LocalDate currentDate = getDate(date -> LocalDate.of(2019, 2, 14));
                LocalDate currentDate = getDate(date -> LocalDate.of(2021, 10, 23));
                switch (option) {
                    case 1 -> loanRegistration(token, currentDate);

                    case 2 -> {
                        if (studentService.checkStudentIsGraduation(currentDate,
                                studentService.calculateGraduationDate(token))) {
                            repaymentMenu.showRepaymentMenu(token);
                        } else {
                            System.out.println("Loan repayment has not been activated for you!");
                        }
                    }
                    case 3 -> continueRunning = false;
                    default -> System.out.println("Wrong option!");
                }
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Wrong option!");
                }
            }
        }
    }

    private void loanRegistration(Student token, LocalDate currentDate) {
        if (studentService.checkStudentIsGraduation(currentDate,
                studentService.calculateGraduationDate(token))) {
            System.out.println("you graduated!");
        } else if (!(isAbanRange(currentDate) || isBahmanRange(currentDate))) {
            System.out.println("You are not allowed to register a loan! Take action within the announced time frame!");
        } else {
            if (token != null) {
                loanMenu.showLoanMenu(token, currentDate);
            }
        }
    }

    private boolean isAbanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 23);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 29);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }

    private boolean isBahmanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 14);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 20);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }


}
