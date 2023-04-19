package com.sr29_2021.Repository.Interfaces;


import com.sr29_2021.Model.User;

import java.util.List;

public interface IUserRepository {
    public User findOne(Integer id);

    public User findOne(String email);

    public User findOne(String email, String sifra);

    public List<User> findAll();

    public int save(User user);

    public int update(User user);

    public int delete(Integer id);

}
