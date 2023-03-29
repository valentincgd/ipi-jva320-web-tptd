package com.ipi.jva320.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(name = "/")
    public String home(){
        return "home";
    }
}
