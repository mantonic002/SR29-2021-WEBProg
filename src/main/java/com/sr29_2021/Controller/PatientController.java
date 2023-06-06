package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.*;
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
public class PatientController {

    @Autowired
    private PatientService service;
    @Autowired
    private UserService userService;

    @GetMapping("/patients")
    public String showPatientList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<Patient> list = service.listAll();
        model.addAttribute("listPatients", list);

        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.ADMIN)){
            return "admin_layouts/patients";
        }
        return "access_denied";
    }

    @PostMapping("/patients/update")
    public String updatePatient(Patient patient, RedirectAttributes ra) {
        service.update(patient);
        ra.addFlashAttribute("message", "Patient has been updated");
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model){
        Patient patient = service.get(id);
        model.addAttribute("patient", patient);
        model.addAttribute("pageTitle",
                "Edit patient (name:" + patient.getUser().getFirstName() + ")");
        return "admin_layouts/patient_form";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        userService.delete(id);
        ra.addFlashAttribute("message", "Patient has been deleted");
        return "redirect:/patients";
    }
}

