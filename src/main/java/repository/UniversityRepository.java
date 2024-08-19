package repository;

import entity.Student;
import entity.University;

public interface UniversityRepository {
    University save(University university);

    University findByName(String name);
}
