package menu;

import entity.City;
import entity.Student;
import entity.University;
import enumaration.AdmissionType;
import enumaration.Degree;
import enumaration.TypeOfUniversity;
import exceptions.CityExceptions;
import exceptions.StudentExceptions;
import exceptions.UniversityExceptions;
import service.CityService;
import service.StudentService;
import service.UniversityService;
import util.Utility;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LoginMenu {
    private Student token = null;
    private final StudentService studentService;
    private final CityService cityService;
    private final UniversityService universityService;

    public LoginMenu(StudentService studentService, CityService cityService, UniversityService universityService) {
        this.studentService = studentService;
        this.cityService = cityService;
        this.universityService = universityService;
    }

    public void showLoginMenu() {
        Scanner input = new Scanner(System.in);
        boolean continueRunning = true;
        while (continueRunning) {
            System.out.println("""
                        Login Menu:
                        1. Login
                        2. Sign up
                        3. Exit
                    """);
            System.out.print("Option: ");
            String stringOption = input.nextLine();
            if (stringOption == null || stringOption.isEmpty()) {
                System.out.println("Input can not be null or empty");
                showLoginMenu();
                break;
            }
            try {
                int option = Integer.parseInt(stringOption);

                switch (option) {
                    case 1 -> continueRunning = !login(input);
                    case 2 -> continueRunning = !signUp(input);
                    case 3 -> {
                        token = null;
                        continueRunning = false;  // Exit the application
                    }
                    default -> System.out.println("Wrong option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong option!");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            } /*catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    System.out.println("Wrong option!");
                }
            }*/
        }
    }

    private boolean login(Scanner input) {
        System.out.println("Enter username: ");
        String username = input.nextLine();
        System.out.println("Enter password: ");
        String password = input.nextLine();
        try {

            token = studentService.login(username, password);
        } catch (StudentExceptions.DatabaseAccessException e) {
            System.out.println("Student not Found " );
            return false;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return false;
        }
        if (token == null) {
            System.out.println("username or password is incorrect");
            return false;
        }
        return true;
    }

    private boolean signUp(Scanner input) {
        String firstName;
        String lastName;
        String fatherName;
        String motherName;
        String birthCertificateNumber;
        String nationalCode;
        String studentCode;
        String entryYear;
        LocalDate birthDate;
        String degree;
        String partnerCode = null;
        String universityName;
        String typeOfUniversity = null;
        String admissionType = null;
        City city = null;
        boolean isMarried;
        boolean haveDormitory = false;

        boolean continues;
        firstName = getFirstName(input);
        lastName = getLastName(input);
        fatherName = getFatherName(input);
        motherName = getMotherName(input);

        birthCertificateNumber = getCertificateNumber(input);
        boolean condition;
        do {
            condition = false;
            nationalCode = getNationalCode(input);
            try {
                studentService.findStudentByNationalCode(nationalCode);
                System.out.println("the student with national code are exists! ");
                condition = true;

            } catch (StudentExceptions.NotFoundException e) {
                System.out.println();
            }
        } while (condition);

        do {
            condition = false;
            studentCode = getStudentCode(input);
            try {
                studentService.findStudentByStudentCode(studentCode);
                System.out.println("the student with Student Code are exists!");
                condition = true;

            } catch (StudentExceptions.NotFoundException e) {
                System.out.println();
            }
        } while (condition);

        entryYear = getEntryYear(input);
        birthDate = getBirthDate(input);
        degree = getDegree(input);
        isMarried = checkOperation("Is Married? (Enter 0 for No and 1 for Yes)", input);

        if (isMarried) {
            do {
                condition = false;
                partnerCode = getPartnerCode(input);
                try {
                    studentService.findStudentByPartnerCode(partnerCode);
                    System.out.println("the student with Partner Code are exists!");
                    condition = true;

                } catch (StudentExceptions.NotFoundException e) {
                    System.out.println();
                }
            } while (condition);
            haveDormitory = checkOperation("have Dormitory? (Enter 0 for No and 1 for Yes)", input);
        }

        universityName = getUniversityName(input);
        University university = null;
        try {
            university = universityService.findByName(universityName);
            System.out.println("Found university: " + university.getName());
        } catch (UniversityExceptions.NotFoundException e) {
            System.out.println();
            //list the city name with id List;
            List<City> cities;
            try {
                System.out.println("List of Cities: ");
                cities = cityService.findAll();
                cities.forEach(System.out::println);
            } catch (CityExceptions.CityNotFoundException e1) {
                System.out.println("Error: contact your admin " + e1.getMessage());
                return false;
            } catch (CityExceptions.CityDatabaseException e2) {
                System.out.println("Database Error:  contact your admin " + e2.getMessage());
                return false;
            } catch (Exception e3) {
                System.out.println("Unexpected Error: contact your admin " + e3.getMessage());
                return false;
            }

            String id;
            boolean cityCheck = true;
            do {
                do {
                    System.out.print("Enter the city id of this List: ");
                    id = input.nextLine();
                } while (!fillInputNumbers_v2(id));
                for (City c : cities) {
                    if (c.getId().equals(Long.valueOf(id))) {
                        city = c;
                        cityCheck = false;
                        break;
                    }
                }
            } while (cityCheck);

            typeOfUniversity = getTypeOfUniversity(input, typeOfUniversity);


            university = University.builder().name(universityName).city(city)
                    .typeOfUniversity(TypeOfUniversity.valueOf(typeOfUniversity)).build();

            universityService.save(university);

        } catch (UniversityExceptions.InvalidArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        if (university != null) {
            typeOfUniversity = String.valueOf(university.getTypeOfUniversity());
        }

        if (typeOfUniversity != null) {
            if (typeOfUniversity.equals("Governmental")) {
                do {
                    System.out.println("Enter admission Type -> Daytime(Enter 1) or Nighttime(Enter 0)");
                    String admissionTypeInput = input.nextLine();
                    switch (admissionTypeInput) {
                        case "0" -> {
                            admissionType = "Nighttime";
                            continues = false;
                        }

                        case "1" -> {
                            admissionType = "Daytime";
                            continues = false;
                        }
                        default -> {
                            System.out.println("Enter the valid number: ");
                            continues = true;
                        }
                    }
                } while (continues);
            }
        }
        String password = Utility.generateSecurePassword();
        Student student;
        if (admissionType == null) {
            student = Student.builder()
                    .firstName(firstName).lastName(lastName).fatherName(fatherName).motherName(motherName)
                    .birthCertificateNumber(birthCertificateNumber).nationalCode(nationalCode)
                    .studentCode(studentCode).entryYear(Integer.valueOf(entryYear))
                    .birthdate(birthDate).degree(Degree.valueOf(degree))
                    .isMarried(isMarried).haveDormitory(haveDormitory).partnerCode(partnerCode)
                    .university(university).userName(nationalCode).password(password)
                    .build();
        } else {
            student = Student.builder()
                    .firstName(firstName).lastName(lastName).fatherName(fatherName).motherName(motherName)
                    .birthCertificateNumber(birthCertificateNumber).nationalCode(nationalCode)
                    .studentCode(studentCode).entryYear(Integer.valueOf(entryYear))
                    .birthdate(birthDate).degree(Degree.valueOf(degree))
                    .isMarried(isMarried).haveDormitory(haveDormitory).partnerCode(partnerCode).admissionType(AdmissionType.valueOf(admissionType))
                    .university(university).userName(nationalCode).password(password)
                    .build();
        }
        token = studentService.save(student);
        System.out.println("save done");
        System.out.println("your username is: " + token.getUserName() + "    and password is : " + token.getPassword());
        return token != null;

    }

    private static String getTypeOfUniversity(Scanner input, String typeOfUniversity) {
        boolean continues;
        do {
            System.out.print("Enter typeOfUniversity: Governmental(Enter 1) or NonGovernmental(Enter 0): ");
            String typeOfUniversityInput = input.nextLine();
            switch (typeOfUniversityInput) {
                case "0" -> {
                    typeOfUniversity = "NonGovernmental";
                    continues = false;
                }

                case "1" -> {
                    typeOfUniversity = "Governmental";
                    continues = false;
                }
                default -> {
                    System.out.println("Enter the valid number: ");
                    continues = true;
                }
            }
        } while (continues);
        return typeOfUniversity;
    }

    private String getPartnerCode(Scanner input) {
        String partnerCode;
        do {
            System.out.println("Enter the PARTNER CODE: ");
            partnerCode = input.nextLine();
        } while (!fillInputNumbers(partnerCode, 10));
        return partnerCode;
    }

    private String getStudentCode(Scanner input) {
        String studentCode;
        do {
            System.out.print("Enter the STUDENT CODE (5 digit): ");
            studentCode = input.nextLine();
        } while (!fillInputNumbers(studentCode, 5));
        return studentCode;
    }

    private String getNationalCode(Scanner input) {
        String nationalCode;
        do {
            System.out.print("Enter the National Code (10 digit): ");
            nationalCode = input.nextLine();
        } while (!fillInputNumbers(nationalCode, 10));
        return nationalCode;
    }

    private String getCertificateNumber(Scanner input) {
        String birthCertificateNumber;
        do {
            System.out.print("Enter the Birth Certificate Number: ");
            birthCertificateNumber = input.nextLine();
        } while (!fillInputNumbersWithMinAndMaxNumber(birthCertificateNumber, 1, 10));
        return birthCertificateNumber;
    }

    private String getMotherName(Scanner input) {
        String motherName;
        do {
            System.out.print("Enter the motherName: ");
            motherName = input.nextLine();
        } while (!fillInputString(motherName));
        return motherName;
    }

    private String getFatherName(Scanner input) {
        String fatherName;
        do {
            System.out.print("Enter the fatherName: ");
            fatherName = input.nextLine();
        } while (!fillInputString(fatherName));
        return fatherName;
    }

    private String getLastName(Scanner input) {
        String lastName;
        do {
            System.out.print("Enter the LastName: ");
            lastName = input.nextLine();
        } while (!fillInputString(lastName));
        return lastName;
    }

    private String getFirstName(Scanner input) {
        String firstName;
        do {
            System.out.print("Enter the FirstName: ");
            firstName = input.nextLine();
        } while (!fillInputString(firstName));
        return firstName;
    }

    private String getUniversityName(Scanner input) {
        String universityName;
        do {
            System.out.print("Enter the University Name: ");
            universityName = input.nextLine();
        } while (!fillInputString(universityName));
        return universityName;
    }

    private boolean checkOperation(String x, Scanner input) {
        boolean studentProperty;
        boolean continues;
        do {
            studentProperty = false;
            System.out.println(x);
            String inputString = input.nextLine();
            switch (inputString) {
                case "0" -> continues = false;
                case "1" -> {
                    studentProperty = true;
                    continues = false;
                }
                default -> {
                    System.out.println("Enter the valid number: ");
                    continues = true;
                }
            }
        } while (continues);
        return studentProperty;
    }

    private String getEntryYear(Scanner input) {
        String entryYear;
        do {
            System.out.print("Enter the entryYear: ");
            entryYear = input.nextLine();
        } while (!fillInputNumbersWithMinAndMaxYear(entryYear, 2010, 2024));
        return entryYear;
    }

    private static String getDegree(Scanner input) {
        String degree;
        boolean continues;
        do {

            System.out.print("""
                    Enter the Degree:
                        Associate(Enter 0),
                        Continuous_Bachelor(Enter 1),
                        DisContinuous_Bachelor(Enter 2),
                        IntegratedMaster(Enter 3),
                        DisContinuousMaster(Enter 4),
                        ProfessionalDoctorate(Enter 5),
                        IntegratedDoctorate(Enter 6),
                        PhD(Enter 7)
                    """);
            degree = input.nextLine();
            switch (degree) {
                case "0" -> {
                    degree = "Associate";
                    continues = false;
                }
                case "1" -> {
                    degree = "Continuous_Bachelor";
                    continues = false;
                }
                case "2" -> {
                    degree = "DisContinuous_Bachelor";
                    continues = false;
                }
                case "3" -> {
                    degree = "IntegratedMaster";
                    continues = false;
                }
                case "4" -> {
                    degree = "DisContinuousMaster";
                    continues = false;
                }
                case "5" -> {
                    degree = "ProfessionalDoctorate";
                    continues = false;
                }
                case "6" -> {
                    degree = "IntegratedDoctorate";
                    continues = false;
                }
                case "7" -> {
                    degree = "PhD";
                    continues = false;
                }
                default -> {
                    System.out.println("Enter the valid number: ");
                    continues = true;
                }
            }
            //break;
        } while (continues);
        return degree;
    }

    private LocalDate getBirthDate(Scanner input) {
        LocalDate birthDate;
        while (true) {
            System.out.print("Enter the Year Of Birthdate: ");
            String yearOfBirthdate = input.nextLine();
            if (fillInputNumbersWithMinAndMaxYear(yearOfBirthdate, 1930, 2006)) {
                System.out.print("Enter the Month Of Birthdate: ");
                String monthOfBirthdate = input.nextLine();
                if (fillInputNumbersWithMinAndMaxYear(monthOfBirthdate, 1, 12)) {
                    System.out.print("Enter the Day Of Birthdate: ");
                    String dayOfBirthdate = input.nextLine();
                    switch (Integer.parseInt(monthOfBirthdate)) {
                        case 1, 3, 5, 6, 7, 8, 10, 12 -> fillInputNumbersWithMinAndMaxYear(dayOfBirthdate, 1, 31);
                        case 4, 9, 11 -> fillInputNumbersWithMinAndMaxYear(dayOfBirthdate, 1, 30);
                        default -> fillInputNumbersWithMinAndMaxYear(dayOfBirthdate, 1, 29);
                    }
                    birthDate = LocalDate.of(Integer.parseInt(yearOfBirthdate), Integer.parseInt(monthOfBirthdate), Integer.parseInt(dayOfBirthdate));
                    break;
                }
            }
        }
        return birthDate;
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

    private boolean fillInputNumbersWithMinAndMaxNumber(String input, int minDigit, int maxDigit) {
        if (checkedNullInput(input)) return false;
        if (input.length() > maxDigit || input.length() < minDigit) {
            System.out.println("input must be between " + minDigit + " and " + maxDigit + " digit number");
            return false;
        }
        return fillInputNumbers_v2(input);
    }

    private boolean fillInputNumbersWithMinAndMaxYear(String input, int minDigit, int maxDigit) {
        if (checkedNullInput(input)) return false;
        if (Integer.parseInt(input) > maxDigit || Integer.parseInt(input) < minDigit) {
            System.out.println("input must be between " + minDigit + " and " + maxDigit + " digit number");
            return false;
        }
        return fillInputNumbers_v2(input);
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


    public Student getToken() {
        return token;
    }


}
