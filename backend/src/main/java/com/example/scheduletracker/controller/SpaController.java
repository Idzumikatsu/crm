package com.example.scheduletracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping("/")
    public String redirectIndex() {
        return "forward:/index.html";
    }

    @RequestMapping("/{*path:^(?!api|swagger-ui).*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
