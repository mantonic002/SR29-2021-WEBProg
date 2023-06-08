package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Application;
import com.sr29_2021.Model.News;

import java.util.List;

public interface IApplicationRepository {
    public Application findOne(Integer id);

    public List<Application> findAll();

    public int save(Application application);

    public int update(Application application);

    public int delete(Integer id);

    public int deleteByPatient(Integer patientId, Integer id);

    public List<Application> searchApplications(String query);

}
