package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.BuyRequest;
import com.sr29_2021.Model.News;

import java.util.List;

public interface IBuyRequestRepository {
    public BuyRequest findOne(Integer id);

    public List<BuyRequest> findAll();

    public int save(BuyRequest buyRequest);

    public int update(BuyRequest buyRequest);

    public int delete(Integer id);
}
