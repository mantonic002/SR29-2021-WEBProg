package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Model.UserRole;
import com.sr29_2021.Service.ManufacturerService;
import com.sr29_2021.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ManufacturerController {

    private final UserService userService;

    private final ManufacturerService service;

    public ManufacturerController(UserService userService, ManufacturerService service) {
        this.userService = userService;
        this.service = service;
    }

    @GetMapping("/manufacturers")
    public String showManufacturerList(Model model , HttpServletRequest request) throws UserNotFoundException {
        List<Manufacturer> list = service.listAll();
        model.addAttribute("listManufacturers", list);

        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.ADMIN)){
            return "admin_layouts/manufacturers";
        }
        return "access_denied";
    }

    @GetMapping("/manufacturers/new")
    public String showNewForm(Model model){
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("pageTitle", "Add new manufacturer");
        return "admin_layouts/manufacturer_form";
    }

    @PostMapping("/manufacturers/save")
    public String saveManufacturer(Manufacturer manufacturer, RedirectAttributes ra) {
        service.save(manufacturer);
        ra.addFlashAttribute("message", "Manufacturer has been saved");
        return "redirect:/manufacturers";
    }

    @GetMapping("/manufacturers/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model){

            Manufacturer manufacturer = service.get(id);
            model.addAttribute("manufacturer", manufacturer);
            model.addAttribute("pageTitle",
                    "Edit manufacturer (name:" + manufacturer.getName() + ", country:" + manufacturer.getCountry() + ")");
            return "admin_layouts/manufacturer_form";
    }

    @GetMapping("/manufacturers/delete/{id}")
    public String deleteManufacturer(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "Manufacturer has been deleted");
        return "redirect:/manufacturers";
    }

}
