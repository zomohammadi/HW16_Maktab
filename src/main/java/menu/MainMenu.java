package menu;

import entity.Student;

public class MainMenu {
    private Student token;

    private final LoginMenu loginMenu;
    private final StudentMenu studentMenu;

    public MainMenu(LoginMenu loginMenu, LoanMenu loanMenu, StudentMenu studentMenu) {
        this.loginMenu = loginMenu;
        this.studentMenu = studentMenu;
    }

    public void showMainMenu(){
        while (true) {
            loginMenu.showLoginMenu();
            token = loginMenu.getToken();
            if (token != null) {
                studentMenu.showStudentMenu(token);
                token = null;
            } else {
                break;
            }
        }
        System.out.println("Exiting the application...");
    }
}
