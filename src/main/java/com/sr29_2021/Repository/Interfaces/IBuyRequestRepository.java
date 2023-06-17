package com.sr29_2021.Repository.Interfaces;

import com.sr29_2021.Model.BuyRequest;

import java.util.List;

public interface IBuyRequestRepository {
    BuyRequest findOne(Integer id);

    List<BuyRequest> findAll();

    int save(BuyRequest buyRequest);

    int update(BuyRequest buyRequest);

    int delete(Integer id);
}
