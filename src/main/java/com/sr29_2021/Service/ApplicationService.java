package com.sr29_2021.Service;

import com.sr29_2021.Model.Application;
import com.sr29_2021.Repository.Interfaces.IApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private IApplicationRepository repo;

    public List<Application> listAll() { return(List<Application>) repo.findAll(); }

    public void save(Application application) {
        repo.save(application);
    }

    public void update(Application application){
        repo.update(application);
    }

    public Application get(Integer id) {
        Application application = repo.findOne(id);
        return application;
    }

    public void delete(Integer id) { repo.delete(id); }

    public void deleteByPatient(Integer patientId,Integer id) { repo.deleteByPatient(patientId, id); }

    public List<Application> searchApplications(String query) {
        return repo.searchApplications(query);
    }
}