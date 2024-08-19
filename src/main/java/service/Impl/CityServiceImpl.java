package service.Impl;

import entity.City;
import exceptions.CityExceptions;
import repository.BaseEntityRepository;
import service.CityService;

import java.util.List;

public class CityServiceImpl implements CityService {
    private final BaseEntityRepository<City> cityBaseEntityRepository;

    public CityServiceImpl(BaseEntityRepository<City> cityBaseEntityRepository) {
        this.cityBaseEntityRepository = cityBaseEntityRepository;
    }

    @Override
    public List<City> findAll() {
        try {
            List<City> cities = cityBaseEntityRepository.findAll();
            if (cities == null || cities.isEmpty()) {
                throw new CityExceptions.CityNotFoundException("No cities found.");
            }
            return cities;
        } catch (Exception e) {
            throw new CityExceptions.CityDatabaseException(e.getMessage());
        }
    }

    @Override
    public City findById(Long id) {
        try {
            City city = cityBaseEntityRepository.findById(id);
            if (city == null) {
                throw new CityExceptions.CityNotFoundException("City not found with id: " + id);
            }
            return city;
        } catch (IllegalArgumentException e) {
            throw new CityExceptions.CityNotFoundException("Invalid ID provided: " + id, e);
        } catch (Exception e) {
            throw new CityExceptions.CityDatabaseException("Error occurred while retrieving city with id: " + id, e);
        }
    }
}
