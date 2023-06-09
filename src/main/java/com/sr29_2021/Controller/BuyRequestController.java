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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class BuyRequestController {
    @Autowired
    private UserService userService;
    @Autowired
    private VaxService vaxService;
    @Autowired
    private BuyRequestService service;

    @PostMapping("/buyRequest/new")
    public String saveBuyRequest(BuyRequest buyRequest, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {

        Cookie[] cookies = request.getCookies();
        User user = userService.checkCookieUser(cookies);

        if(user.getRole().equals(UserRole.STAFF)){
            buyRequest.setStaffId(user.getId());

            buyRequest.setDate(LocalDateTime.now());
            buyRequest.setStatus(Status.SENT);
            service.save(buyRequest);

            ra.addFlashAttribute("message", "request sent");
        } else{
            ra.addFlashAttribute("message", "request failed");
        }
        return "redirect:/staff/vax";
    }

    @GetMapping("/buyRequests")
    public String showBuyRequestsList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<BuyRequest> list = service.listAll();
        model.addAttribute("list", list);

        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.ADMIN)){
            return "admin_layouts/buy_requests";
        }
        return "access_denied";
    }

    @GetMapping("/buyRequest/accept/{id}")
    public String showBuyRequestsAccept(@PathVariable("id") Integer id, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.ADMIN)){
            BuyRequest buyRequest = service.get(id);
            buyRequest.setStatus(Status.ACCEPTED);

            Vax vax = vaxService.get(buyRequest.getVax().getId());
            vax.setAvailableNum(vax.getAvailableNum() + buyRequest.getAmount());

            vaxService.save(vax);
            service.update(buyRequest);

            ra.addFlashAttribute("message", "request accepted");

            return "redirect:/buyRequests";
        } else{
            ra.addFlashAttribute("message", "request failed");
        }
        return "access_denied";
    }

    @GetMapping("/buyRequest/comment/{id}/{method}")
    public String BuyRequestsComment(@PathVariable("id") Integer id, @PathVariable("method") String method, Model model, HttpServletRequest request) throws UserNotFoundException {
        BuyRequest buyRequest = service.get(id);

        Cookie[] cookies = request.getCookies();
        if (userService.checkCookies(cookies, UserRole.ADMIN)) {
            model.addAttribute("buyRequest", buyRequest);
            model.addAttribute("method", method);

            if (method.equals("revise")) {
                model.addAttribute("pageTitle", "Add a comment for revising a buy request");
            } else if (method.equals("deny")) {
                model.addAttribute("pageTitle", "Add a comment for denying a buy request");
            }

            return "admin_layouts/buy_request_comment";
        }

        return "access_denied";
    }

    @PostMapping("/buyRequest/comment")
    public String BuyRequestsComment(BuyRequest buyRequest, @RequestParam("method") String method, RedirectAttributes ra) throws UserNotFoundException {


        if (method.equals("revise")) {
            buyRequest.setStatus(Status.REVISION);
            ra.addFlashAttribute("message", "Buy request has been sent for revision");
        } else if (method.equals("deny")){
            buyRequest.setStatus(Status.DENIED);
            ra.addFlashAttribute("message", "Buy request has been denied");
        }
        buyRequest.setUser(userService.get(buyRequest.getStaffId()));
        buyRequest.setVax(vaxService.get(buyRequest.getVaxId()));

        service.update(buyRequest);


        return "redirect:/buyRequests";
    }
}
