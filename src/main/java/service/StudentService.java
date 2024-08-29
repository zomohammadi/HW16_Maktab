package service;

import entity.Student;

import java.time.LocalDate;

public interface StudentService {
   // String generateSecurePassword();
    Student save(Student student);
    Student login(String username, String password);
    boolean existsNationalCode(String nationalCode);
    Student findStudentByNationalCode(String nationalCode);
    LocalDate calculateGraduationDate(Student student);
 boolean checkStudentIsGraduation(LocalDate currentDate, LocalDate graduationDate);
}
