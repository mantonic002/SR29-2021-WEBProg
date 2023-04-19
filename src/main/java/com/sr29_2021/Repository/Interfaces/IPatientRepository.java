package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Patient;
import com.sr29_2021.Model.User;

import java.util.List;

public interface IPatientRepository {
    public Patient findOne(Integer id);

    public List<Patient> findAll();

    public int save(Patient patient);

    public int update(Patient patient);

    public int delete(Integer id);
}
