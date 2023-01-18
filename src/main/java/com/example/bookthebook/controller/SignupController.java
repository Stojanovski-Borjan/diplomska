package com.example.bookthebook.controller;

import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.Role;
import com.example.bookthebook.service.RegisterUserService;
import com.example.bookthebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getRegistration(Model model) {
        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping
    public String createNewUser(@ModelAttribute User user) {
        // check if exist user email or password is blank
        user.setRole(Role.STUDENT);
        if (isEmailOrPasswordBlank(user)) {
            return "redirect:/signup";
        }

        // check if exist user with same email
        if (userService.findUserByEmail(user.getEmail())) {
            return "redirect:/signup";
        }
        registerUserService.createUser(user);
        return "redirect:/books";
    }

    private static boolean isEmailOrPasswordBlank(User user) {
        return user.getEmail().isBlank() || user.getPassword().isBlank();
    }
}
