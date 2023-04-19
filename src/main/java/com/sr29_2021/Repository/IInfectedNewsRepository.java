package com.sr29_2021.Repository;

import com.sr29_2021.Model.InfectedNews;

import java.util.List;

public interface IInfectedNewsRepository {
    public InfectedNews findOne(Integer id);

    public List<InfectedNews> findAll();

    public int save(InfectedNews infectedNews);

    public int update(InfectedNews infectedNews);

    public int delete(Integer id);
}