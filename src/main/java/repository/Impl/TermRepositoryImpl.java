package repository.Impl;

import entity.Term;
import jakarta.persistence.EntityManager;
import repository.BaseEntityRepository;
import repository.TermRepository;

public class TermRepositoryImpl extends BaseEntityRepositoryImpl<Term> implements TermRepository {
    public TermRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public Class<Term> getEntityClass() {
        return Term.class;
    }
}
