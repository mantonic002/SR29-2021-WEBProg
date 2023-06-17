package com.sr29_2021.Service;

import com.sr29_2021.Model.BuyRequest;
import com.sr29_2021.Repository.Interfaces.IBuyRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyRequestService {

    private final IBuyRequestRepository repo;

    public BuyRequestService(IBuyRequestRepository repo) {
        this.repo = repo;
    }

    public List<BuyRequest> listAll() { return repo.findAll(); }

    public void save(BuyRequest BuyRequest) {
        repo.save(BuyRequest);
    }

    public void update(BuyRequest BuyRequest){
        repo.update(BuyRequest);
    }

    public BuyRequest get(Integer id) {
        return repo.findOne(id);
    }

    public void delete(Integer id) { repo.delete(id); }

}