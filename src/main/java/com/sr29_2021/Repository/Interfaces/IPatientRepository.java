package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Patient;

import java.util.List;

public interface IPatientRepository {
    Patient findOne(Integer id);

    List<Patient> findAll();

    int save(Patient patient);

    int update(Patient patient);

    int delete(Integer id);
}
