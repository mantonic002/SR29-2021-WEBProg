package com.sr29_2021.Service;

import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Repository.Interfaces.IManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    private final IManufacturerRepository repo;

    public ManufacturerService(IManufacturerRepository repo) {
        this.repo = repo;
    }

    public List<Manufacturer> listAll(){
        return repo.findAll();
    }

    public void save(Manufacturer manufacturer) {
        if(repo.findOne(manufacturer.getId()) != null){
            repo.update(manufacturer);
        }   else{
            repo.save(manufacturer);
        }
    }

    public Manufacturer get(Integer id) {
        return repo.findOne(id);
    }

    public void delete(Integer id) {
        repo.delete(id);
    }
}
