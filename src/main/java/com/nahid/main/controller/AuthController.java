package com.nahid.main.controller;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

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

}
