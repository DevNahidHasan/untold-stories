package com.nahid.main.controller;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import com.nahid.main.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final StoryService storyService;

    @GetMapping({"/","/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model){

        Pageable pageable = PageRequest.of(page
                , 2);

        Page<Story> storyPage = storyService.getStories(pageable);

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",storyPage.getTotalPages());
        model.addAttribute("currentPage",page);

        return "home-page";
    }

    @GetMapping("/search")
    public String searchFeedback(@RequestParam String searchQuery, Model model){

    }

}
