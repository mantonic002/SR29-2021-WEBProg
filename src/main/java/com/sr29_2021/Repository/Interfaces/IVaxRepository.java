package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Model.Vax;

import java.util.List;

public interface IVaxRepository {
    public Vax findOne(Integer id);

    public List<Vax> findByManufacturer(Manufacturer manufacturer);

    public List<Vax> findAll();

    public int save(Vax vax);

    public int update(Vax vax);

    public int delete(Integer id);
}
