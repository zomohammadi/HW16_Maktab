package service.Impl;

import entity.Student;
import exceptions.StudentExceptions;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import repository.BaseEntityRepository;
import repository.StudentRepository;
import service.StudentService;

public class StudentServiceImpl implements StudentService {

    private final BaseEntityRepository<Student> studentBaseEntityRepository;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(BaseEntityRepository<Student> studentBaseEntityRepository, StudentRepository studentRepository) {
        this.studentBaseEntityRepository = studentBaseEntityRepository;
        this.studentRepository = studentRepository;
    }



    @Override
    public Student save(Student student) {

        try {
            if (student == null) {
                throw new StudentExceptions.StudentNullPointerException("Student cannot be null");
            }

            student = studentBaseEntityRepository.save(student);

        } catch (IllegalArgumentException e) {
            throw new StudentExceptions.StudentIllegalArgumentException("Invalid argument passed", e);
        } catch (EntityExistsException e) {
            throw new StudentExceptions.StudentEntityExistsException("Entity already exists in the database", e);
        } catch (TransactionRequiredException e) {
            throw new StudentExceptions.StudentTransactionRequiredException("Transaction is required but was not active", e);
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException("Database access error occurred while saving in", e);
        } catch (NullPointerException e) {
            throw new StudentExceptions.StudentNullPointerException("A required object was null", e);
        } catch (Exception e) {
            System.out.println("Error: Student not saved. " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
        return student;
    }

    @Override
    public Student login(String username, String password) {
        try {
            Student student = studentRepository.login(username, password);

            if (student == null) {
                throw new StudentExceptions.UserNotFoundException("User not found with the provided credentials");
            }

            return student;

        } catch (IllegalArgumentException e) {
            throw new StudentExceptions.InvalidCredentialsException("Invalid credentials provided", e);
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Login failed. " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred during login", e);
        }

    }

    @Override
    public boolean existsNationalCode(String nationalCode) {
        try {

            return studentRepository.existsNationalCode(nationalCode);
        }catch (PersistenceException e){
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        }
    }
}
