package com.sr29_2021.Service;

import com.sr29_2021.Model.BuyRequest;
import com.sr29_2021.Repository.Interfaces.IBuyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyRequestService {

    @Autowired
    private IBuyRequestRepository repo;

    public List<BuyRequest> listAll() { return(List<BuyRequest>) repo.findAll(); }

    public void save(BuyRequest BuyRequest) {
        repo.save(BuyRequest);
    }

    public void update(BuyRequest BuyRequest){
        repo.update(BuyRequest);
    }

    public BuyRequest get(Integer id) {
        BuyRequest BuyRequest = repo.findOne(id);
        return BuyRequest;
    }

    public void delete(Integer id) { repo.delete(id); }

}