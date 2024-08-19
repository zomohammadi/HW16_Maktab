package service;

import entity.Student;

public interface StudentService {
    String generateSecurePassword();
    Student save(Student student);
    Student login(String username, String password);
    boolean existsNationalCode(String nationalCode);
}
