package util;

import entity.City;
import entity.Student;
import entity.University;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import menu.LoanMenu;
import menu.LoginMenu;
import menu.MainMenu;
import repository.BaseEntityRepository;
import repository.CityRepository;
import repository.Impl.BaseEntityRepositoryImpl;
import repository.Impl.CityRepositoryImpl;
import repository.Impl.StudentRepositoryImpl;
import repository.Impl.UniversityRepositoryImpl;
import repository.StudentRepository;
import repository.UniversityRepository;
import service.CityService;
import service.Impl.CityServiceImpl;
import service.Impl.StudentServiceImpl;
import service.Impl.UniversityServiceImpl;
import service.StudentService;
import service.UniversityService;

public class ApplicationContext {

    private EntityManagerFactory enf;
    private EntityManager em;


    private final StudentService studentService;
    private final UniversityService universityService;
    private final CityService cityService;

    //menu
    private final MainMenu mainMenu;
    private final LoginMenu loginMenu;
    private final LoanMenu loanMenu;


    public ApplicationContext() {
        this.em = getEntityManager();

        BaseEntityRepository<Student> studentBaseEntityRepository = new StudentRepositoryImpl(em);
        StudentRepository studentRepository = new StudentRepositoryImpl(em);
        BaseEntityRepositoryImpl<University> universityBaseEntityRepository = new UniversityRepositoryImpl(em);
        UniversityRepository universityRepository = new UniversityRepositoryImpl(em);
        BaseEntityRepository<City> cityBaseEntityRepository = new CityRepositoryImpl(em);

        studentService = new StudentServiceImpl(studentBaseEntityRepository, studentRepository);
        universityService = new UniversityServiceImpl(universityBaseEntityRepository, universityRepository);
        cityService = new CityServiceImpl(cityBaseEntityRepository);

        //menu
        this.loginMenu = new LoginMenu(studentService,
                cityService, universityService);
        this.loanMenu = new LoanMenu();
        this.mainMenu = new MainMenu(loginMenu, loanMenu);
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
}
