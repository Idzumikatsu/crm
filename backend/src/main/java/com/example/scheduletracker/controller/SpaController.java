package com.example.scheduletracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

  @RequestMapping(
      value = {
        "/",
        "/{path:^(?!api|swagger-ui$|index\\.html$).*$}",
        "/**/{path:^(?!api|swagger-ui$)[^\\.]*}"
      })
  public String redirect() {
    return "forward:/index.html";
  }
}
