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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final StoryService storyService;

    @GetMapping({"/","/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest){

        Pageable pageable = PageRequest.of(page, 3);

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
    public String searchStory(@RequestParam String searchQuery, @RequestParam(defaultValue = "0") int page, Model model){
//        List<Story> storyList = storyService.searchStoryByName(searchQuery);
//        model.addAttribute("storyList",storyList);
//        model.addAttribute("searchQuery",searchQuery);
//        return "home-page";

        Pageable pageable = PageRequest.of(page, 3);

        Page<Story> storyPage = storyService.searchStoryByNamePageable(searchQuery,pageable);

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",storyPage.getTotalPages());
        model.addAttribute("currentPage",page);
        model.addAttribute("searchQuery",searchQuery);

        return "home-page";



    }
//---------------------------------------------------------------------
// "user dashboard" only for user
    @GetMapping("/user/dashboard")
    public String userDashboardPage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest){

        //System.out.println("in user dashboard controller");
//        String username = httpServletRequest.getUserPrincipal().getName();
//
//        List<Story> storyList = storyService.searchStoryByUser(username);
//
//        model.addAttribute("storyList",storyList);
//        return "user-dashboard-page";

        String username = httpServletRequest.getUserPrincipal().getName();

        Pageable pageable = PageRequest.of(page, 3);

        Page<Story> storyPage = storyService.searchStoryByUserPageable(username, pageable);

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",storyPage.getTotalPages());
        model.addAttribute("currentPage",page);

        return "user-dashboard-page";


    }


//------------------------------------------------------------------------------------
// "Share story and save" "only for user" get controller and post controller
    @GetMapping("/user/story")
    public String postStoryPage(Model model){
        model.addAttribute("story",new Story());
        model.addAttribute("mode","SUBMIT");
        return "post-page";
    }

    @PostMapping("/user/story")
    public String saveStory(@ModelAttribute Story story, HttpServletRequest httpServletRequest)  {
        String username = httpServletRequest.getUserPrincipal().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        story.setStoryBy(username);
        story.setCreatedAt(currentTime);

        storyService.saveStory(story);
        return "redirect:/user/dashboard";
    }
//------------------------------------------------------------------------------------------
// "edit story" "only for user" ,get and post controller
    @GetMapping("/user/story/{storyId}/edit")
    public String editStory(@PathVariable UUID storyId, Model model){

        Story story = storyService.getStoryById(storyId);
        model.addAttribute("story",story);
        model.addAttribute("mode","UPDATE");

        return "post-page";

    }

    @PostMapping("/user/story/{storyId}/edit")
    public String doEditStory(@PathVariable UUID storyId, @ModelAttribute Story story, HttpServletRequest httpServletRequest){

        String username = httpServletRequest.getUserPrincipal().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        story.setStoryId(storyId);
        story.setStoryBy(username);
        story.setCreatedAt(currentTime);

        storyService.saveStory(story);
        return "redirect:/user/dashboard";

    }
//--------------------------------------------------------------------
// "delete story"  for both user and admin
    @PostMapping("/story/{storyId}/delete")
    public String deleteStory(@PathVariable UUID storyId, HttpServletRequest httpServletRequest){
        storyService.deleteStoryById(storyId);

//        if (httpServletRequest.isUserInRole("ADMIN")){
//            return "redirect:/";
//        }else if (httpServletRequest.isUserInRole("USER")){
//            System.out.println("In else if");
//            return "redirect:/user-dashboard";
//        }

        if (httpServletRequest.isUserInRole("USER")){
            return "redirect:/user/dashboard";
        }else {

            return "redirect:/";
        }

//        return "redirect:/";
    }

//--------------------------------------------------------------------------
// "see full story" for everyone
    @GetMapping("/story/{storyId}")
    public String storyDetails(@PathVariable UUID storyId, Model model){
        Story story = storyService.getStoryById(storyId);
        model.addAttribute("story",story);

        return "story-page";

    }





}
