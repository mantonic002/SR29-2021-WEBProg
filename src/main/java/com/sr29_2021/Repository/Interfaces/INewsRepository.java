package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.News;


import java.util.List;

public interface INewsRepository {
    public News findOne(Integer id);

    public List<News> findAll();

    public int save(News news);

    public int update(News news);

    public int delete(Integer id);
}
