package com.repuestosla21.repuestosla21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login") public String login(){return "login";}
}
