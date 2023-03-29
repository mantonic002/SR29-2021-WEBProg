package com.sr29_2021.Service;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.User;
import com.sr29_2021.Repository.IUserRepository;
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
        if(repo.findOne(user.getId()) != null){
            repo.update(user);
        }   else{
            repo.save(user);
        }
    }

    public User get(Integer id) throws UserNotFoundException {
        User result = repo.findOne(id);
        return result;
    }

    public void delete(Integer id) {
        repo.delete(id);
    }
}
