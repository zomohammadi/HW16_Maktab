package repository.Impl;

import entity.City;
import jakarta.persistence.EntityManager;
import repository.CityRepository;

public class CityRepositoryImpl extends BaseEntityRepositoryImpl<City> implements CityRepository {
    public CityRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<City> getEntityClass() {
        return City.class;
    }
}
