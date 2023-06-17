package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Manufacturer;

import java.util.List;

public interface IManufacturerRepository {
    Manufacturer findOne(Integer id);

    Manufacturer findOne(String name);

    List<Manufacturer> findAll();

    int save(Manufacturer manufacturer);

    int update(Manufacturer manufacturer);

    int delete(Integer id);
}
