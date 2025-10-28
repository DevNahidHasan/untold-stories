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

    /**
     * Homepage- <br>
     * Accessibility: Any types of visitors <br>
     * Provides "Delete" feature for "ROLE_ADMIN"
     */
    @GetMapping({"/","/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest){

        Pageable pageable = PageRequest.of(page, 4);

        Page<Story> storyPage = storyService.getStories(pageable);

        int totalPages = storyPage.getTotalPages();

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPage",page);

        if (httpServletRequest.isUserInRole("ADMIN")){
            model.addAttribute("isAdmin",true);
        }

        if (totalPages == 0) {
            return "home-page";
        }

        if(page+1 > storyPage.getTotalPages()){
            return "redirect:/?page="+ (totalPages-1);
        }

        return "home-page";
    }

    /**
     * Search Story- <br>
     * Accessibility: Any types of visitors <br>
     * Provides "Delete" feature for "ROLE_ADMIN"
     */
    @GetMapping("/search")
    public String searchStory(@RequestParam String searchQuery, @RequestParam(defaultValue = "0") int page,
                              Model model, HttpServletRequest httpServletRequest){

        Pageable pageable = PageRequest.of(page, 4);

        Page<Story> storyPage = storyService.searchStoryByNamePageable(searchQuery,pageable);

        int totalPages = storyPage.getTotalPages();

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPage",page);
        model.addAttribute("searchQuery",searchQuery);

        if (httpServletRequest.isUserInRole("ADMIN")){
            model.addAttribute("isAdmin",true);
        }

        if (totalPages == 0) {
            return "home-page";
        }

        if(page+1 > storyPage.getTotalPages()){
            return "redirect:/search?page="+ (totalPages-1) + "&searchQuery=" + searchQuery;
        }

        return "home-page";
    }

    /**
     * User Dashboard Page- <br>
     * Accessibility: only to "ROLE_USER" <br>
     * Provides "EDIT" and "Delete" feature for "ROLE_USER"
     */
    @GetMapping("/user/dashboard")
    public String userDashboardPage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest){

        String username = httpServletRequest.getUserPrincipal().getName();

        Pageable pageable = PageRequest.of(page, 4);

        Page<Story> storyPage = storyService.searchStoryByUserPageable(username, pageable);

        int totalPages = storyPage.getTotalPages();

        model.addAttribute("storyList",storyPage.getContent());
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPage",page);

        if (totalPages == 0) {
            return "user-dashboard-page";
        }

        if(page+1 > storyPage.getTotalPages()){
            return "redirect:/user/dashboard?page=" + (totalPages-1);
        }

        return "user-dashboard-page";


    }



    /**
     * Share story and Save Story to database- <br>
     * Accessibility: only to "ROLE_USER" <br>
     */
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

    /**
     * Edit story and Save Edite Story to database- <br>
     * Accessibility: only to "ROLE_USER" <br>
     */
    @GetMapping("/user/story/{storyId}/edit")
    public String editStory(@PathVariable UUID storyId,  @RequestHeader(value = "Referer", required = false) String referer,
                            Model model){

        Story story = storyService.getStoryById(storyId);
        model.addAttribute("story",story);
        model.addAttribute("mode","UPDATE");
        model.addAttribute("referer",referer);

        return "post-page";

    }

    @PostMapping("/user/story/{storyId}/edit")
    public String doEditStory(@PathVariable UUID storyId, @ModelAttribute Story story,
                              @RequestParam(value = "referer", required = false) String referer,
                              HttpServletRequest httpServletRequest){

        String username = httpServletRequest.getUserPrincipal().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        story.setStoryId(storyId);
        story.setStoryBy(username);
        story.setCreatedAt(currentTime);

        storyService.saveStory(story);

        // Redirect back to the page the user came from
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        // Fallback if referer is missing
        return "redirect:/user/dashboard";

    }

    /**
     * Delete story - <br>
     * Accessibility: only to "ROLE_USER" and "ROLE_ADMIN <br>
     */
    @PostMapping("/story/{storyId}/delete")
    public String deleteStory(@PathVariable UUID storyId, HttpServletRequest httpServletRequest,
                              @RequestHeader(value = "Referer", required = false) String referer){
        storyService.deleteStoryById(storyId);

        if (httpServletRequest.isUserInRole("USER")){
            // Redirect back to the page the user came from
            if (referer != null && !referer.isEmpty()) {
                return "redirect:" + referer;
            }
            else {// Fallback if referer is missing
                return "redirect:/user/dashboard";
            }

        }else {
            // Redirect back to the page the Admin came from
            if (referer != null && !referer.isEmpty()) {
                return "redirect:" + referer;
            }// Fallback if referer is missing
            return "redirect:/";
        }

    }

    /**
     * See Full Story- <br>
     * Accessibility: Any types of visitors <br>
     */
    @GetMapping("/story/{storyId}")
    public String storyDetails(@PathVariable UUID storyId, Model model){
        Story story = storyService.getStoryById(storyId);
        model.addAttribute("story",story);

        return "story-page";

    }





}
