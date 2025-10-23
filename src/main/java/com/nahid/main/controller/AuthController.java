package com.nahid.main.controller;

import com.nahid.main.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam @Nullable String error, @RequestParam @Nullable String logout, RedirectAttributes redirectAttributes){
        if(error!= null && error.equals("true")){
            redirectAttributes.addFlashAttribute("error","Invalid credentials");
            return "redirect:/login";
        }
        else if(logout != null && logout.equals("true")){
            redirectAttributes.addFlashAttribute("message","Logout Successful!");
            return "redirect:/login";
        }

        return "login-page";

    }

    @GetMapping("/register")
    public String registrationPage(){
        return "registration-page";
    }

    @PostMapping("/register")
    public String doUserRegistration(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes){
        try {
            userService.registerUser(username,password);
            redirectAttributes.addFlashAttribute("message","Registration Successful!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
        }
        return "redirect:/register";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpServletRequest httpServletRequest){
        System.out.println("In dashboard controller");
        if (httpServletRequest.isUserInRole("ADMIN")){
            return "redirect:/";
        }else if (httpServletRequest.isUserInRole("USER")){
            System.out.println("In else if");
            return "redirect:/user/dashboard";
        }

        return "redirect:/";
    }
}
