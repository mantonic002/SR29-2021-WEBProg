package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.Application;

import java.util.List;

public interface IApplicationRepository {
    Application findOne(Integer id);

    List<Application> findAll();

    int save(Application application);

    int update(Application application);

    int delete(Integer id);

    void deleteByPatient(Integer patientId, Integer id);

    List<Application> searchApplications(String query);

}
