package com.example.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactAppController {
    @GetMapping({
            "/",
            "/login",
            "/signup",
            "/employees",
            "/attendance",
            "/payroll",
            "/leave",
            "/jobs",
            "/reports",
            "/settings"
    })
    public String reactApp() {
        return "forward:/index.html";
    }
}
