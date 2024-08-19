package service.Impl;

import entity.University;
import exceptions.UniversityExceptions;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import repository.BaseEntityRepository;
import repository.UniversityRepository;
import service.UniversityService;

public class UniversityServiceImpl implements UniversityService {
    private final BaseEntityRepository<University> universityBaseEntityRepository;
    private final UniversityRepository universityRepository;

    public UniversityServiceImpl(BaseEntityRepository<University> universityBaseEntityRepository, UniversityRepository universityRepository) {
        this.universityBaseEntityRepository = universityBaseEntityRepository;
        this.universityRepository = universityRepository;
    }

    @Override
    public University save(University university) {

        try {
            if (university == null) {
                throw new UniversityExceptions.NullPointerException("University cannot be null");
            }

            universityBaseEntityRepository.save(university);

        } catch (IllegalArgumentException e) {
            throw new UniversityExceptions.InvalidArgumentException("Invalid argument passed", e);
        } catch (EntityExistsException e) {
            throw new UniversityExceptions.EntityExistsException("Entity already exists in the database ", e);
        } catch (TransactionRequiredException e) {
            throw new UniversityExceptions.TransactionRequiredException("Transaction is required but was not active", e);
        } catch (PersistenceException e) {
            throw new UniversityExceptions.DatabaseAccessException("Database access error occurred while saving in ", e);
        } catch (NullPointerException e) {
            throw new UniversityExceptions.NullPointerException("A required object was null", e);
        } catch (Exception e) {
            System.out.println("Error: University not saved. " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
        return university;
    }

    @Override
    public University findByName(String name) {
        University university;
        try {
            // Handle the case where the provided name is null or empty
            if (name == null || name.trim().isEmpty()) {
                throw new UniversityExceptions.InvalidArgumentException("The university name cannot be null or empty", null);
            }

            // Attempt to find the university by name
            university = universityRepository.findByName(name);


            return university;

        } catch (IllegalArgumentException e) {
            throw new UniversityExceptions.InvalidArgumentException("Invalid argument passed", e);
        } catch (PersistenceException e) {
            throw new UniversityExceptions.DatabaseAccessException( e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: University not found. " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred during findByName", e);
        }
    }
}
