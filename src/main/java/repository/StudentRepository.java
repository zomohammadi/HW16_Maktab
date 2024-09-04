package repository;

import entity.Student;

public interface StudentRepository {
    Student login(String username, String password);

    boolean existsNationalCode(String nationalCode);

    Student findStudentByNationalCode(String nationalCode);

    Student findStudentByStudentCode(String studentCode);

    void findStudentByPartnerCode(String partnerCode);
}
