package com.sr29_2021.Controller;

import com.sr29_2021.Exceptions.UserNotFoundException;
import com.sr29_2021.Model.*;
import com.sr29_2021.Service.ApplicationService;
import com.sr29_2021.Service.PatientService;
import com.sr29_2021.Service.UserService;
import com.sr29_2021.Service.VaxService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ApplicationController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private UserService userService;
    @Autowired
    private VaxService vaxService;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/application/new")
    public String saveApplication(@RequestParam("vaxId") int vaxId, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        Vax vax = vaxService.get(vaxId);

        Cookie[] cookies = request.getCookies();
        User user = userService.checkCookieUser(cookies);

        if(user.getRole().equals(UserRole.PATIENT)){
            Patient patient = patientService.get(user.getId());

            Application application = new Application();

            application.setDateTime(LocalDateTime.now());
            application.setPatient(patient);
            application.setVax(vax);

            applicationService.save(application);

            ra.addFlashAttribute("message", "application submitted");
        } else{
            ra.addFlashAttribute("message", "application failed");
        }
        return "redirect:/patient/vax";
    }

    @GetMapping("/applications")
    public String showApplicationList(Model model,
                                      HttpServletRequest request ,
                                      @RequestParam(name = "query", required = false) String query,
                                      RedirectAttributes ra) throws UserNotFoundException {
        List<Application> list;
        if (query != null && !query.isEmpty()) {
            list = applicationService.searchApplications(query);
        } else {
            list = applicationService.listAll();
        }
        model.addAttribute("listApplications", list);

        if (ra.getFlashAttributes().containsKey("message")) {
            String message = (String) ra.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

        Cookie[] cookies = request.getCookies();
        if(userService.checkCookies(cookies, UserRole.STAFF)){
            return "staff_layouts/applications";
        }
        return "access_denied";
    }

    @PostMapping("/application/confirm")
    public String confirmApplication(@RequestParam("applicationId") int applicationId, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        Application application = applicationService.get(applicationId);

        Cookie[] cookies = request.getCookies();
        User user = userService.checkCookieUser(cookies);

        Patient patient = application.getPatient();


        if (patient.getReceivedDoses() < 4){
            if ((patient.getReceivedDoses() == 1 && isPast(1, patient.getLastDoseDate()))
            || (patient.getReceivedDoses() == 2 && isPast(2, patient.getLastDoseDate()))
            || (patient.getReceivedDoses() == 3 && isPast(1, patient.getLastDoseDate()))
            || patient.getReceivedDoses() == 0) {
                if(user.getRole().equals(UserRole.STAFF)){
                    Vax vax = application.getVax();
                    vax.setAvailableNum(vax.getAvailableNum() - 1);

                    patient.setLastDoseDate(LocalDateTime.now());
                    patient.setReceivedDoses(patient.getReceivedDoses() + 1);
                    if (!patient.getVaxxed()){
                        patient.setVaxxed(true);
                    }

                    applicationService.deleteByPatient(patient.getUserId(), applicationId);
                    vaxService.save(vax);
                    patientService.update(patient);

                    ra.addFlashAttribute("message", "application confirmed");
                }
            } else {
                System.out.println(patient.getLastDoseDate().plusMinutes(1));
                ra.addFlashAttribute("message", "confirmation faileddd");
            }
        }else{
            ra.addFlashAttribute("message", "confirmation failed");
        }
        return "redirect:/applications";
    }


    public boolean isPast(Integer minutes, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(date.plusMinutes(minutes));
    }
}
