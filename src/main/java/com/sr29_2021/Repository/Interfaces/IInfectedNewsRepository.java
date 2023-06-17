package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.InfectedNews;

import java.util.List;

public interface IInfectedNewsRepository {
    InfectedNews findOne(Integer id);

    List<InfectedNews> findAll();

    int save(InfectedNews infectedNews);

    int update(InfectedNews infectedNews);

    int delete(Integer id);
}