package com.sr29_2021.Service;

import com.sr29_2021.Model.InfectedNews;
import com.sr29_2021.Repository.Interfaces.IInfectedNewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfectedNewsService {

    private final IInfectedNewsRepository repo;

    public InfectedNewsService(IInfectedNewsRepository repo) {
        this.repo = repo;
    }

    public List<InfectedNews> listAll() { return repo.findAll(); }

    public void save(InfectedNews infectedNews) {
        if(repo.findOne(infectedNews.getId()) != null){
            repo.update(infectedNews);
        } else{
            repo.save(infectedNews);
        }
    }

    public InfectedNews get(Integer id) {
        return repo.findOne(id);
    }

    public List<InfectedNews> getToday() {
        List<InfectedNews> news = repo.findAll();
        List<InfectedNews> newsToday = new ArrayList<>();
        for (InfectedNews i : news) {
            LocalDate newsDate = i.getDateTime().toLocalDate(); // Assuming 'getDateTime()' returns a java.util.Date

            if (newsDate.isEqual(LocalDate.now())) {
                newsToday.add(i);
            }
        }
        return newsToday;
    }

    public void delete(Integer id) { repo.delete(id); }
}
