package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Model.Vax;

import java.util.List;

public interface IVaxRepository {
    Vax findOne(Integer id);

    List<Vax> findByManufacturer(Manufacturer manufacturer);

    List<Vax> findAll();

    int save(Vax vax);

    int update(Vax vax);

    int delete(Integer id);
    List<Vax> searchVaxes(String query);
    List<Vax> findSortedVaxes(String sort, String direction);

    List<Vax> searchVaxesByAmountRange(Integer minAmount, Integer maxAmount);
}
