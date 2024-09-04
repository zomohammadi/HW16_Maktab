package service;

import entity.Student;

import java.time.ZonedDateTime;

public interface StudentService {
    // String generateSecurePassword();
    Student save(Student student);

    Student login(String username, String password);

    boolean existsNationalCode(String nationalCode);

    Student findStudentByNationalCode(String nationalCode);

    ZonedDateTime calculateGraduationDate(Student student);

    Student findStudentByStudentCode(String studentCode);

    void findStudentByPartnerCode(String partnerCode);

    boolean checkStudentIsGraduation(ZonedDateTime currentDate, ZonedDateTime graduationDate);
}
