package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.User;
import com.sr29_2021.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {
    public static final String USER_KEY = "USER";

    @Autowired
    private UserService service;

    private Map<String, String> loggedInUsers = new HashMap<>();

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "index";
    }
    @PostMapping("/login/save")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpServletRequest request, HttpServletResponse response, Model model,
                        RedirectAttributes redirectAttributes, @CookieValue(value = "sessionID", defaultValue = "") String sessionID)
            throws UserNotFoundException {

        User user = service.get(email, password);
        if (user != null) {
            if (isUserLoggedIn(user.getEmail())) {
                model.addAttribute("error", "Korisnik je već ulogovan.");
                setSessionCookie(response, user.getEmail());
                return "index";
            }

            if (sessionID.isEmpty()) {
                sessionID = generateSessionID();
                setSessionCookie(response, user.getEmail());
            }

            setLoggedInUser(sessionID, user.getEmail());
            return "redirect:/user";
        } else {
            model.addAttribute("error", "Pogrešan email ili lozinka.");
            return "index";
        }
    }

    private boolean isUserLoggedIn(String email) {
        return loggedInUsers.containsValue(email);
    }

    private void setSessionCookie(HttpServletResponse response, String sessionID) {
        Cookie sessionCookie = new Cookie("sessionID", sessionID);
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    private String generateSessionID() {
        return UUID.randomUUID().toString();
    }

    private void setLoggedInUser(String sessionID, String email) {
        loggedInUsers.put(sessionID, email);
    }


}
