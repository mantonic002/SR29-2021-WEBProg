package com.sr29_2021.Service;
import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Repository.Interfaces.IManufacturerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private IManufacturerRepository repo;

    public List<Manufacturer> listAll(){
        return (List<Manufacturer>) repo.findAll();
    }

    public void save(Manufacturer manufacturer) {
        if(repo.findOne(manufacturer.getId()) != null){
            repo.update(manufacturer);
        }   else{
            repo.save(manufacturer);
        }
    }

    public Manufacturer get(Integer id) {
        Manufacturer result = repo.findOne(id);
        return result;
    }

    public void delete(Integer id) {
        repo.delete(id);
    }
}
