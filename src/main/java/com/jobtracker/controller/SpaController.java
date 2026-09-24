package com.jobtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/index.html";
    }
}