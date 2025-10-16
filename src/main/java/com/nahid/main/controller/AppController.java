package com.nahid.main.controller;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import com.nahid.main.service.StoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final StoryService storyService;

    @GetMapping({"/","/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest){

        Pageable pageable = PageRequest.of(page
                , 2);

        Page<Story> storyPage = storyService.getStories(pageable);

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",storyPage.getTotalPages());
        model.addAttribute("currentPage",page);

        if (httpServletRequest.isUserInRole("ADMIN")){
            model.addAttribute("isAdmin",true);
        }

        return "home-page";
    }

    @GetMapping("/search")
    public String searchStory(@RequestParam String searchQuery, Model model){
        List<Story> storyList = storyService.searchStoryByName(searchQuery);

        model.addAttribute("storyList",storyList);
        model.addAttribute("searchQuery",searchQuery);
        return "home-page";
    }

    @GetMapping("/story/{storyId}")
    public String storyDetails(@PathVariable UUID storyId, Model model){
        Story story = storyService.getStoryById(storyId);
        model.addAttribute("story",story);

        return "story-page";

    }

    @PostMapping("/story/{storyId}/delete")
    public String deleteStory(@PathVariable UUID storyId){
        storyService.deleteStoryById(storyId);
        return "redirect:/";
    }



}
