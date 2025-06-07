package com.example.scheduletracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(value = "/{path:^(?!api|swagger-ui$).*$}")
    public String redirectRoot() {
        return "forward:/index.html";
    }

    @GetMapping(value = "/**/{path:^(?!api|swagger-ui$)[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
