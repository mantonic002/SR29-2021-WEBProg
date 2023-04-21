package com.sr29_2021.Service;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.User;
import com.sr29_2021.Repository.Interfaces.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository repo;

    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }

    public void save(User user) {
        repo.save(user);
    }

    public void update(User user) {
        repo.update(user);
    }

    public User get(Integer id) throws UserNotFoundException {
        User result = repo.findOne(id);
        return result;
    }

    public User get(String email) throws UserNotFoundException {
        User result = repo.findOne(email);
        return result;
    }

    public void delete(Integer id) {
        repo.delete(id);
    }
}
