package com.example.domitory.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Return the name of a custom error page (e.g., "error.html")
        return "error";
    }

    public String getErrorPath() {
        // This method is required to implement the interface
        return "/error";
    }
}
