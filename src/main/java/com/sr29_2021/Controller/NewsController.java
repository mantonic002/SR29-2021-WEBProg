package com.sr29_2021.Controller;

import com.sr29_2021.Model.News;
import com.sr29_2021.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService service;

    @GetMapping("/news")
    public String showNewsList(Model model){
        List<News> list = service.listAll();
        model.addAttribute("news", new News());
        model.addAttribute("listNews", list);
        return "news";
    }

    @GetMapping("/news/new")
    public String showNewForm(Model model){
        model.addAttribute("news", new News());
        model.addAttribute("pageTitle", "Add news");
        return "news_form";
    }

    @PostMapping("/news/save")
    public String saveNews(News news, RedirectAttributes ra) {
        service.save(news);
        ra.addFlashAttribute("message", "News have been saved");
        return "redirect:/news";
    }

    @GetMapping("/news/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        News news = service.get(id);
        model.addAttribute("news", news);
        model.addAttribute("pageTitle",
                "Edit news (name:" + news.getName() + ")");
        return "news_form";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "News have been deleted");
        return "redirect:/news";
    }
}
