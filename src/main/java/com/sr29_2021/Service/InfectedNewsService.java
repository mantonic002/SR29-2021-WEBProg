package com.sr29_2021.Service;

import com.sr29_2021.Model.InfectedNews;
import com.sr29_2021.Repository.Interfaces.IInfectedNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfectedNewsService {

    @Autowired
    private IInfectedNewsRepository repo;

    public List<InfectedNews> listAll() { return(List<InfectedNews>) repo.findAll(); }

    public void save(InfectedNews infectedNews) {
        if(repo.findOne(infectedNews.getId()) != null){
            repo.update(infectedNews);
        } else{
            repo.save(infectedNews);
        }
    }

    public InfectedNews get(Integer id) {
        InfectedNews infectedNews = repo.findOne(id);
        return infectedNews;
    }

    public void delete(Integer id) { repo.delete(id); }
}
