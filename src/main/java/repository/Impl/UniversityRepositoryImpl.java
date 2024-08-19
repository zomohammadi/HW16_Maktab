package repository.Impl;

import entity.Student;
import entity.University;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repository.UniversityRepository;

public class UniversityRepositoryImpl extends BaseEntityRepositoryImpl<University> implements UniversityRepository {

    public UniversityRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<University> getEntityClass() {
        return University.class;
    }

    @Override
    public University findByName(String name) {
        TypedQuery<University> query = getEntityManager().createQuery("""
                select u from University u where u.name = ?1
                """, University.class);
        query.setParameter(1, name);
        return query.getSingleResult();
    }
}
