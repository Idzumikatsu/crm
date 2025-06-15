package com.example.scheduletracker.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpaController {

  @RequestMapping(
      method = {RequestMethod.GET, RequestMethod.HEAD},
      value = {
        "/",
        "/{path:^(?!api|swagger-ui$|error$|index\\.html$).*$}",
        "/**/{path:^(?!api|swagger-ui$|error$)[^\\.]*}"
      })
  public String redirect(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    return "forward:/index.html";
  }
}
