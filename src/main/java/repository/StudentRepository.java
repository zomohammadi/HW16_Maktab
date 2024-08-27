package repository;

import entity.Student;

public interface StudentRepository {
    Student login(String username, String password);

    boolean existsNationalCode(String nationalCode);

    Student findStudentByNationalCode(String nationalCode);
}
