package com.sr29_2021.Service;

import com.sr29_2021.Model.Application;
import com.sr29_2021.Repository.Interfaces.IApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final IApplicationRepository repo;

    public ApplicationService(IApplicationRepository repo) {
        this.repo = repo;
    }

    public List<Application> listAll() { return repo.findAll(); }

    public void save(Application application) {
        repo.save(application);
    }

    public void update(Application application){
        repo.update(application);
    }

    public Application get(Integer id) {
        return repo.findOne(id);
    }

    public void delete(Integer id) { repo.delete(id); }

    public void deleteByPatient(Integer patientId,Integer id) { repo.deleteByPatient(patientId, id); }

    public List<Application> searchApplications(String query) {
        return repo.searchApplications(query);
    }
}