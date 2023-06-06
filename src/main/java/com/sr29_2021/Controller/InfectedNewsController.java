package com.sr29_2021.Controller;

import com.sr29_2021.Model.InfectedNews;
import com.sr29_2021.Service.InfectedNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InfectedNewsController {

    @Autowired
    private InfectedNewsService service;

    @GetMapping("/infectedNews")
    public String showNewsList(Model model){
        List<InfectedNews> list = service.listAll();
        model.addAttribute("news", new InfectedNews());
        model.addAttribute("listNews", list);
        return "admin_layouts/infected_news";
    }

    @GetMapping("/infectedNews/new")
    public String showNewForm(Model model){
        model.addAttribute("news", new InfectedNews());
        model.addAttribute("pageTitle", "Add news");
        return "admin_layouts/infected_news_form";
    }

    @PostMapping("/infectedNews/save")
    public String saveNews(InfectedNews news, RedirectAttributes ra) {
        service.save(news);
        ra.addFlashAttribute("message", "News have been saved");
        return "redirect:/infectedNews";
    }

    @GetMapping("/infectedNews/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        InfectedNews news = service.get(id);
        model.addAttribute("news", news);
        model.addAttribute("pageTitle",
                "Edit news (name:" + news.getId() + ")");
        return "admin_layouts/infected_news_form";
    }

    @GetMapping("/infectedNews/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "News have been deleted");
        return "redirect:/infectedNews";
    }
}
