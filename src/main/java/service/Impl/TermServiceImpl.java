package service.Impl;

import entity.Term;
import repository.BaseEntityRepository;
import service.TermService;

public class TermServiceImpl implements TermService {

    private final BaseEntityRepository<Term> termBaseEntityRepository;

    public TermServiceImpl(BaseEntityRepository<Term> termBaseEntityRepository) {
        this.termBaseEntityRepository = termBaseEntityRepository;
    }

    @Override
    public Term save(Term term) {
        return termBaseEntityRepository.save(term);
    }
}
