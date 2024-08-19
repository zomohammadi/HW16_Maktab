package service;

import entity.City;

import java.util.List;

public interface CityService {
    List<City> findAll();

    City findById(Long id);
}
