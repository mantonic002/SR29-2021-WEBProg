package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.News;


import java.util.List;

public interface INewsRepository {
    News findOne(Integer id);

    List<News> findAll();

    int save(News news);

    int update(News news);

    int delete(Integer id);
}
