package com.sr29_2021.Service;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.User;
import com.sr29_2021.Model.UserRole;
import com.sr29_2021.Repository.Interfaces.IUserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository repo;

    public List<User> listAll(){
        return repo.findAll();
    }

    public void save(User user) {
        repo.save(user);
    }

    public void update(User user) {
        repo.update(user);
    }

    public User get(Integer id) throws UserNotFoundException {
        return repo.findOne(id);
    }

    public User get(String email,String password) throws UserNotFoundException {
        return repo.findOne(email,password);
    }

    public User get(String email) throws UserNotFoundException {
        return repo.findOne(email);
    }

    public void delete(Integer id) {
        repo.delete(id);
    }

    public boolean checkCookies(Cookie[] cookies, UserRole role) throws UserNotFoundException {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    User temp = this.get(cookie.getValue());
                    return temp.getRole().equals(role);
                }
            }
        }
        return false;
    }

    public User checkCookieUser(Cookie[] cookies) throws UserNotFoundException {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    User temp = this.get(cookie.getValue());
                    return temp;
                }
            }
        }
        return null;
    }
}
