package util;

import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import menu.*;
import repository.*;
import repository.Impl.*;
import service.*;
import service.BankService;
import service.Impl.*;

public class ApplicationContext {

    private EntityManagerFactory enf;
    private EntityManager em;

    private final StudentService studentService;
    //menu
    private final MainMenu mainMenu;


    public ApplicationContext() {
        this.em = getEntityManager();

        BaseEntityRepository<Student> studentBaseEntityRepository = new StudentRepositoryImpl(em);
        StudentRepository studentRepository = new StudentRepositoryImpl(em);
        BaseEntityRepositoryImpl<University> universityBaseEntityRepository = new UniversityRepositoryImpl(em);
        UniversityRepository universityRepository = new UniversityRepositoryImpl(em);
        BaseEntityRepository<City> cityBaseEntityRepository = new CityRepositoryImpl(em);
        BaseEntityRepository<Term> termBaseEntityRepository = new TermRepositoryImpl(em);
        LoanRepository loanRepository = new LoanRepositoryImpl(em);
        BaseEntityRepository<Loan> loanBaseEntityRepository = new LoanRepositoryImpl(em);
        BankRepository bankRepository = new BankRepositoryImpl(em);
        BaseEntityRepository<Account> accountBaseEntityRepository = new AccountRepositoryImp(em);
        BaseEntityRepository<CreditCard> creditCardBaseEntityRepository = new CreditCardRepositoryImp(em);
        CreditCardRepository creditCardRepository = new CreditCardRepositoryImp(em);
        BaseEntityRepository<MortgageDetail> mortgageDetailBaseEntityRepository = new MortgageDetailRepositoryImp(em);
        PaymentRepository paymentRepository = new PaymentRepositoryImp(em);

        studentService = new StudentServiceImpl(studentBaseEntityRepository, studentRepository);
        UniversityService universityService = new UniversityServiceImpl(universityBaseEntityRepository, universityRepository);
        CityService cityService = new CityServiceImpl(cityBaseEntityRepository);
        TermService termService = new TermServiceImpl(termBaseEntityRepository);
        LoanService loanService = new LoanServiceImpl(loanRepository, loanBaseEntityRepository);
        BankService bankService = new BankServiceImpl(bankRepository);

        AccountService accountService = new AccountServiceImpl(accountBaseEntityRepository);
        CreditCardService creditCardService = new CreditCardServiceImpl(creditCardBaseEntityRepository, creditCardRepository);
        MortgageDetailService mortgageDetailService = new MortgageDetailServiceImpl(mortgageDetailBaseEntityRepository);
        PaymentService paymentService = new PaymentServiceImpl( paymentRepository);
        //menu
        LoginMenu loginMenu = new LoginMenu(studentService,
                cityService, universityService);
        LoanMenu loanMenu = new LoanMenu(termService, loanService, bankService,
                accountService, creditCardService, /*loanCreditCardService,*/ studentService, mortgageDetailService, paymentService);
        RepaymentMenu repaymentMenu=new RepaymentMenu(paymentService);
        StudentMenu studentMenu = new StudentMenu(loanMenu, studentService, repaymentMenu);
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

    //menu
    public MainMenu getMainMenu() {
        return mainMenu;
    }

}
