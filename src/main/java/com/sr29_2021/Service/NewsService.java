package com.sr29_2021.Service;

import com.sr29_2021.Model.News;
import com.sr29_2021.Repository.Interfaces.INewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final INewsRepository repo;

    public NewsService(INewsRepository repo) {
        this.repo = repo;
    }

    public List<News> listAll() { return repo.findAll(); }

    public void save(News news) {
        if(repo.findOne(news.getId()) != null){
            repo.update(news);
        } else{
            repo.save(news);
        }
    }

    public News get(Integer id) {
        return repo.findOne(id);
    }

    public void delete(Integer id) { repo.delete(id); }
}
