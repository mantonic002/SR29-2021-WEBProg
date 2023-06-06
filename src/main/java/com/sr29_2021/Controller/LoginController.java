package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.User;
import com.sr29_2021.Model.UserRole;
import com.sr29_2021.Service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private UserService service;

    private final Map<String, String> loggedInUsers = new HashMap<>();

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login/save")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpServletResponse response, Model model,
                        @CookieValue(value = "sessionID", defaultValue = "") String sessionID)
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
            if(user.getRole().equals(UserRole.ADMIN)){
                return "redirect:/news";
            } else if(user.getRole().equals(UserRole.STAFF)){
                return "redirect:/staff/vax";
            } else if(user.getRole().equals(UserRole.PATIENT)){
                return "redirect:/";
            }

        }
        model.addAttribute("error", "Pogrešan email ili lozinka.");
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, @CookieValue(value = "sessionID") String sessionID) {
        // Remove the session from loggedInUsers map
        loggedInUsers.remove(sessionID);

        // Clear the session cookie
        Cookie sessionCookie = new Cookie("sessionID", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        // Redirect to the login page or any other desired page
        return "redirect:/login";
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
