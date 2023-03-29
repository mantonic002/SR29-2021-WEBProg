package com.sr29_2021.Repository;

import com.sr29_2021.Model.Manufacturer;

import java.util.List;

public interface IManufacturerRepository {
    public Manufacturer findOne(Integer id);

    public Manufacturer findOne(String name);

    public List<Manufacturer> findAll();

    public int save(Manufacturer manufacturer);

    public int update(Manufacturer manufacturer);

    public int delete(Integer id);
}
