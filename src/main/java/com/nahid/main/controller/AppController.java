package com.nahid.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping({"/","/home"})
    public String homePage(){
        return "home-page";
    }

}
