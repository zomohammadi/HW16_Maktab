package menu;

import entity.Student;

import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

import static util.Utility.getDate;

public class StudentMenu {
    // private Student token = null;
    private final LoanMenu loanMenu;

    public StudentMenu(LoanMenu loanMenu) {
        this.loanMenu = loanMenu;
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

                switch (option) {
                    case 1 -> {
                        // LocalDate currentDate = getDate(date -> null);
                        //LocalDate currentDate = getDate(date -> LocalDate.of(2023, 10, 22));
                        LocalDate currentDate = getDate(date -> LocalDate.of(2018, 10, 24));
                        if (!(isAbanRange(currentDate) || isBahmanRange(currentDate))) {
                            System.out.println("You are not allowed to register a loan! Take action within the announced time frame!");
                        } else {
                            if (token != null) {
                                // continueRunning=false;
                                loanMenu.showLoanMenu(token, currentDate);
                            }
                        }
                    }
                    // case 2 -> continueRunning = !signUp(input);
                    case 3 -> {
                        token = null;
                        continueRunning = false;  // Exit the application
                    }
                    default -> System.out.println("Wrong option!");
                }
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Wrong option!");
                }
            }
        }
    }

    private boolean isAbanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 22);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 28);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }

    private boolean isBahmanRange(LocalDate currentDate) {
        LocalDate startOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 13);
        LocalDate endOfRange = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 19);

        return !currentDate.isBefore(startOfRange) && !currentDate.isAfter(endOfRange);
    }
}
