package com.sr29_2021.Service;

import com.sr29_2021.Model.News;
import com.sr29_2021.Repository.Interfaces.INewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private INewsRepository repo;

    public List<News> listAll() { return(List<News>) repo.findAll(); }

    public void save(News news) {
        if(repo.findOne(news.getId()) != null){
            repo.update(news);
        } else{
            repo.save(news);
        }
    }

    public News get(Integer id) {
        News news = repo.findOne(id);
        return news;
    }

    public void delete(Integer id) { repo.delete(id); }
}
