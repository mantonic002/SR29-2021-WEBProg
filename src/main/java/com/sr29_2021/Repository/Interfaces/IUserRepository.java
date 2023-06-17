package com.sr29_2021.Repository.Interfaces;


import com.sr29_2021.Model.User;

import java.util.List;

public interface IUserRepository {
    User findOne(Integer id);

    User findOne(String email);

    User findOne(String email, String password);

    List<User> findAll();

    int save(User user);

    int update(User user);

    int delete(Integer id);

}
