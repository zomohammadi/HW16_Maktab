package service.Impl;

import entity.Student;
import enumaration.Degree;
import exceptions.StudentExceptions;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import repository.BaseEntityRepository;
import repository.StudentRepository;
import service.StudentService;

import java.time.ZonedDateTime;

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
                throw new StudentExceptions.NotFoundException("User not found with the provided credentials");
            }

            return student;

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
        } catch (NoResultException e) {
            throw new StudentExceptions.NotFoundException(e.getMessage());
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public Student findStudentByNationalCode(String nationalCode) {
        try {
            return studentRepository.findStudentByNationalCode(nationalCode);
        } catch (NoResultException e) {
            throw new StudentExceptions.NotFoundException(e.getMessage());
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public Student findStudentByStudentCode(String studentCode) {
        try {
            return studentRepository.findStudentByStudentCode(studentCode);
        } catch (NoResultException e) {
            throw new StudentExceptions.NotFoundException(e.getMessage());
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void findStudentByPartnerCode(String partnerCode) {
        try {
            studentRepository.findStudentByPartnerCode(partnerCode);
        } catch (NoResultException e) {
            throw new StudentExceptions.NotFoundException(e.getMessage());
        } catch (PersistenceException e) {
            throw new StudentExceptions.DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public ZonedDateTime calculateGraduationDate(Student student) {
        int entryYear = student.getEntryYear();
        int endOfGraduation;
        Degree degree = student.getDegree();
        switch (degree) {
            case Associate, DisContinuous_Bachelor,
                    DisContinuousMaster -> endOfGraduation = entryYear + 3;
            case Continuous_Bachelor -> endOfGraduation = entryYear + 5;
            case IntegratedMaster -> endOfGraduation = entryYear + 6;
            case ProfessionalDoctorate, IntegratedDoctorate, PhD -> endOfGraduation = entryYear + 7;
            default -> endOfGraduation = entryYear;
        }
        return ZonedDateTime.of(endOfGraduation, 6, 22
                , 0, 0, 0, 0, ZonedDateTime.now().getZone());
    }

    public boolean checkStudentIsGraduation(ZonedDateTime currentDate, ZonedDateTime graduationDate) {
        boolean conditions = true;
        if (currentDate.getYear() < graduationDate.getYear()) {
            conditions = false;
        } else if (currentDate.getYear() == graduationDate.getYear()) {
            if (currentDate.getMonthValue() < graduationDate.getMonthValue()) {
                conditions = false;
            } else if (currentDate.getMonthValue() == graduationDate.getMonthValue()) {
                if (currentDate.getDayOfMonth() < graduationDate.getDayOfMonth()) {
                    conditions = false;
                }
            } else {
                System.out.print("");
            }
        }
        return conditions;
    }


}
