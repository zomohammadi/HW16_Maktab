package util;

import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import menu.LoanMenu;
import menu.LoginMenu;
import menu.MainMenu;
import menu.StudentMenu;
import repository.*;
import repository.Impl.*;
import service.*;
import service.BankService;
import service.Impl.*;

public class ApplicationContext {

    private EntityManagerFactory enf;
    private EntityManager em;


    private final StudentService studentService;
    private final UniversityService universityService;
    private final CityService cityService;
    private final TermService termService;
    private final LoanService loanService;
    private final BankService bankService;
    private final AccountService accountService;
    private final CreditCardService creditCardService;

    //menu
    private final MainMenu mainMenu;
    private final LoginMenu loginMenu;
    private final LoanMenu loanMenu;
    private final StudentMenu studentMenu;


    public ApplicationContext() {
        this.em = getEntityManager();

        BaseEntityRepository<Student> studentBaseEntityRepository = new StudentRepositoryImpl(em);
        StudentRepository studentRepository = new StudentRepositoryImpl(em);
        BaseEntityRepositoryImpl<University> universityBaseEntityRepository = new UniversityRepositoryImpl(em);
        UniversityRepository universityRepository = new UniversityRepositoryImpl(em);
        BaseEntityRepository<City> cityBaseEntityRepository = new CityRepositoryImpl(em);
        TermRepository termRepository = new TermRepositoryImpl(em);
        BaseEntityRepository<Term> termBaseEntityRepository = new TermRepositoryImpl(em);
        LoanRepository loanRepository = new LoanRepositoryImpl(em);
        BaseEntityRepository<Loan> loanBaseEntityRepository = new LoanRepositoryImpl(em);
        BankRepository bankRepository = new BankRepositoryImpl(em);
        BaseEntityRepository<Account> accountBaseEntityRepository = new AccountRepositoryImp(em);
        BaseEntityRepository<CreditCard> creditCardBaseEntityRepository = new CreditCardRepositoryImp(em);

        studentService = new StudentServiceImpl(studentBaseEntityRepository, studentRepository);
        universityService = new UniversityServiceImpl(universityBaseEntityRepository, universityRepository);
        cityService = new CityServiceImpl(cityBaseEntityRepository);
        termService = new TermServiceImpl(termBaseEntityRepository);
        loanService = new LoanServiceImpl(loanRepository, loanBaseEntityRepository);
        bankService = new BankServiceImpl(bankRepository);
        accountService = new AccountServiceImpl(accountBaseEntityRepository);
        creditCardService = new CreditCardServiceImpl(creditCardBaseEntityRepository);

        //menu
        this.loginMenu = new LoginMenu(studentService,
                cityService, universityService);
        this.loanMenu = new LoanMenu(termService, loanService, bankService, accountService, creditCardService);
        this.studentMenu = new StudentMenu(loanMenu);
        this.mainMenu = new MainMenu(loginMenu, loanMenu, studentMenu);
    }

    private static ApplicationContext applicationContext;

    public static ApplicationContext getInstance() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }


    public EntityManagerFactory getEntityManagerFactory() {
        if (enf == null) {
            enf = Persistence.createEntityManagerFactory("default");
        }
        return enf;
    }

    public EntityManager getEntityManager() {
        if (em == null) {
            em = getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public UniversityService getUniversityService() {
        return universityService;
    }

    public CityService getCityService() {
        return cityService;
    }

    public LoanService getLoanService() {
        return loanService;
    }

    public BankService getBankService() {
        return bankService;
    }
    //menu

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public LoginMenu getLoginMenu() {
        return loginMenu;
    }

    public LoanMenu getLoanMenu() {
        return loanMenu;
    }

    public StudentMenu getStudentMenu() {
        return studentMenu;
    }


    public TermService getTermService() {
        return termService;
    }
}
