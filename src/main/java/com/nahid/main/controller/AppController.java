package com.nahid.main.controller;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import com.nahid.main.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final StoryService storyService;

    @GetMapping({"/","/home"})
    public String homePage(Model model){

        List<Story> storyList = storyService.getStories();
        model.addAttribute("storyList",storyList);

        return "home-page";
    }

}
