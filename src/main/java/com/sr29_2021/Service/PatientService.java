package com.sr29_2021.Service;

import com.sr29_2021.Model.Patient;
import com.sr29_2021.Repository.Interfaces.IPatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final IPatientRepository repo;

    public PatientService(IPatientRepository repo) {
        this.repo = repo;
    }

    public List<Patient> listAll() { return repo.findAll(); }

    public void save(Patient patient) {
        repo.save(patient);
    }

    public void update(Patient patient){
        repo.update(patient);
    }

    public Patient get(Integer id) {
        return repo.findOne(id);
    }

    public void delete(Integer id) { repo.delete(id); }
}
