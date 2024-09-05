package menu;

import entity.Student;
import service.StudentService;
import util.Utility;

import java.time.ZonedDateTime;
import java.util.Scanner;

public class StudentMenu {
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

                //get current date
                ZonedDateTime currentDate = Utility.useDate();

                switch (option) {
                    case 1 -> loanRegistration(token, currentDate);
                    case 2 -> loanRepayment(token, currentDate);
                    case 3 -> continueRunning = false;
                    default -> System.out.println("Wrong option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong option!");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void loanRepayment(Student token, ZonedDateTime currentDate) {
        if (studentService.checkStudentIsGraduation(currentDate,
                studentService.calculateGraduationDate(token))) {
            repaymentMenu.showRepaymentMenu(token);
        } else {
            System.out.println("Loan repayment has not been activated for you!");
        }
    }

    private void loanRegistration(Student token, ZonedDateTime currentDate) {
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

    private boolean isAbanRange(ZonedDateTime currentDate) {
        ZonedDateTime startOfRange = ZonedDateTime.of(currentDate.getYear(), 10, 23,
                0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime endOfRange = ZonedDateTime.of(currentDate.getYear(), 10, 29
                , 23, 59, 59, 0, ZonedDateTime.now().getZone());

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }

    private boolean isBahmanRange(ZonedDateTime currentDate) {
        ZonedDateTime startOfRange = ZonedDateTime.of(currentDate.getYear(), 2, 14,
                0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime endOfRange = ZonedDateTime.of(currentDate.getYear(), 2, 20
                , 23, 59, 59, 0, ZonedDateTime.now().getZone());

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }
}
