package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.Patient;
import com.sr29_2021.Model.User;
import com.sr29_2021.Model.UserRole;
import com.sr29_2021.Service.PatientService;
import com.sr29_2021.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private PatientService patientService;

    @GetMapping("/users")
    public String showUserList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);

        Cookie[] cookies = request.getCookies();
        if(service.checkCookies(cookies, UserRole.ADMIN)){
            return "admin_layouts/users";
        }
        return "access_denied";
    }

    @GetMapping("/users/{src}")
    public String showNewForm(@PathVariable("src") String src, Model model) {
        if (src.equals("new")) {
            model.addAttribute("pageTitle", "Add new user");
            model.addAttribute("redirect", "/euprava/users");
        } else if (src.equals("registration")) {
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("redirect", "/euprava/");
        }

        model.addAttribute("user", new User());
        model.addAttribute("method", "/users/save");

        return "admin_layouts/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes ra) throws UserNotFoundException {
        service.save(user);
        User userNew = service.get(user.getEmail());

        if(user.getRole() == UserRole.PATIENT){
            Patient patient = new Patient(userNew);
            patientService.save(patient);
        }
        ra.addFlashAttribute("message", "User has been saved");
        return "redirect:/";
    }

    @PostMapping ("/users/update")
    public String updateUser(User user, RedirectAttributes ra) throws UserNotFoundException {
        User oldUser = service.get(user.getId());
        if(user.getPassword().isEmpty() || user.getPassword() == null){
            user.setPassword(oldUser.getPassword());
        }
        service.update(user);
        ra.addFlashAttribute("message", "User has been updated");
        return "redirect:/profile";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try{
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("method", "/users/update");
            model.addAttribute("pageTitle", "Edit user (Email:" + user.getEmail() + ")");
            return "admin_layouts/user_form";

        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "User couldn't been updated");
            return "redirect:/profile";
        }
    }
    @GetMapping("users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        patientService.delete(id);
        ra.addFlashAttribute("message", "User has been deleted");
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) throws UserNotFoundException {

        Cookie[] cookies = request.getCookies();
        User user = service.checkCookieUser(cookies);
        model.addAttribute("user", user);
        if(user.getRole().equals(UserRole.ADMIN)){
            model.addAttribute("role", "admin");
        }
        else if(user.getRole().equals(UserRole.STAFF)){
            model.addAttribute("role", "staff");
        }
        else if(user.getRole().equals(UserRole.PATIENT)){
            model.addAttribute("role", "patient");
        }
        if(user != null){
            return "profile";
        }
        return "access_denied";
    }

}
