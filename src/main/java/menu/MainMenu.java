package menu;

import entity.Student;

import java.sql.SQLException;

public class MainMenu {
    private Student token;

    private final LoginMenu loginMenu;
    private final LoanMenu loanMenu;

    public MainMenu(LoginMenu loginMenu, LoanMenu loanMenu) {
        this.loginMenu = loginMenu;
        this.loanMenu = loanMenu;
    }

    public void showMainMenu(){
        while (true) {
            loginMenu.showLoginMenu();
            token = loginMenu.getToken();
            if (token != null) {
               // loanMenu.showLoanMenu(token);
                token = null;
            } else {
                break;
            }
        }
        System.out.println("Exiting the application...");
    }
}
