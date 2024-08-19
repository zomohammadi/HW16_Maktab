package repository.Impl;

import entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repository.StudentRepository;

public class StudentRepositoryImpl extends BaseEntityRepositoryImpl<Student> implements StudentRepository {

    public StudentRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Student> getEntityClass() {
        return Student.class;
    }

    @Override
    public Student login(String username, String password) {
        TypedQuery<Student> query = getEntityManager()
                .createQuery("select s from Student s where s.userName = ?1 and s.password =?2 ", Student.class);
        query.setParameter(1, username);
        query.setParameter(2, password);
        return query.getSingleResult();
    }

    @Override
    public boolean existsNationalCode(String nationalCode) {
        TypedQuery<Student> query = getEntityManager()
                .createQuery("select s from Student s where s.nationalCode = ?1 ", Student.class);
        query.setParameter(1, nationalCode);
        Student student = query.getSingleResult();
        return student != null;
    }
}
