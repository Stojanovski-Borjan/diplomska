package com.example.bookthebook.controller;

import com.example.bookthebook.model.User;
import com.example.bookthebook.service.RegisterUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("login")
public class LoginController {

    private final RegisterUserService registerUserService;

    public LoginController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }


    @GetMapping
    public String getRegistration() {
        return "login";
    }

    @PostMapping
    public String signUpUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        System.out.printf("email: " + email + " password: " + password);
        User logedInUser = this.registerUserService.signUpUser(email, password);
        if(logedInUser != null){
            session.setAttribute("user", logedInUser);
            return "redirect:/books";
        }
        return "redirect:/login";
    }


}
