package com.sr29_2021.Service;

import com.sr29_2021.Model.Patient;
import com.sr29_2021.Repository.Interfaces.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private IPatientRepository repo;

    public List<Patient> listAll() { return(List<Patient>) repo.findAll(); }

    public void save(Patient patient) {
        if(repo.findOne(patient.getUserId()) != null){
            repo.update(patient);
        } else{
            repo.save(patient);
        }
    }

    public Patient get(Integer id) {
        Patient patient = repo.findOne(id);
        return patient;
    }

    public void delete(Integer id) { repo.delete(id); }
}
