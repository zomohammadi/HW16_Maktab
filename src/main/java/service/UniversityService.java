package service;

import entity.Student;
import entity.University;

public interface UniversityService {
    University save(University university);
    University findByName(String name);
}
