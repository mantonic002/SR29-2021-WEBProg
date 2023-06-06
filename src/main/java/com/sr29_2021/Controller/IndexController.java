package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.InfectedNews;
import com.sr29_2021.Model.News;
import com.sr29_2021.Model.UserRole;
import com.sr29_2021.Service.InfectedNewsService;
import com.sr29_2021.Service.NewsService;
import com.sr29_2021.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private NewsService service;

    @Autowired
    private InfectedNewsService serviceInfected;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String showIndex(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<News> list = service.listAll();
        model.addAttribute("listNews", list);

        List<InfectedNews> infToday = serviceInfected.getToday();
        model.addAttribute("infectedToday", infToday);

        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.ADMIN)){
            model.addAttribute("role", "admin");
        }
        else if(userService.checkCookies(cookies, UserRole.STAFF)){
            model.addAttribute("role", "staff");
        }
        else if(userService.checkCookies(cookies, UserRole.PATIENT)){
            model.addAttribute("role", "patient");
        } else{
            model.addAttribute("role", "none");
        }

        return "index";
    }
}
