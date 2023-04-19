package com.sr29_2021.Service;

import com.sr29_2021.Model.Vax;
import com.sr29_2021.Repository.Interfaces.IVaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaxService {

    @Autowired
    private IVaxRepository repo;

    public List<Vax> listAll() { return(List<Vax>) repo.findAll(); }

    public void save(Vax vax) {
        if(repo.findOne(vax.getId()) != null){
            repo.update(vax);
        } else{
            repo.save(vax);
        }
    }

    public Vax get(Integer id) {
        Vax vax = repo.findOne(id);
        return vax;
    }

    public void delete(Integer id) { repo.delete(id); }
}
