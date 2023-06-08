package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.*;
import com.sr29_2021.Service.BuyRequestService;
import com.sr29_2021.Service.UserService;
import com.sr29_2021.Service.VaxService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class BuyRequestController {
    @Autowired
    private UserService userService;
    @Autowired
    private VaxService vaxService;
    @Autowired
    private BuyRequestService service;


//    @PostMapping("/news/save")
//    public String saveNews(News news, RedirectAttributes ra) {
//        service.save(news);
//        ra.addFlashAttribute("message", "News have been saved");
//        return "redirect:/news";
//    }
    @PostMapping("/buyRequest/new")
    public String saveBuyRequest(BuyRequest buyRequest, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {

        Cookie[] cookies = request.getCookies();
        User user = userService.checkCookieUser(cookies);

        if(user.getRole().equals(UserRole.STAFF)){
            buyRequest.setStaffId(user.getId());

            buyRequest.setDate(LocalDateTime.now());

            service.save(buyRequest);

            ra.addFlashAttribute("message", "request sent");
        } else{
            ra.addFlashAttribute("message", "request failed");
        }
        return "redirect:/staff/vax";
    }
}
