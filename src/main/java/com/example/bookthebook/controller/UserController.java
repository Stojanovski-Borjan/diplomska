package com.example.bookthebook.controller;

import com.example.bookthebook.model.Rental;
import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.Role;
import com.example.bookthebook.service.RentalService;
import com.example.bookthebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public String listUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                List<User> users = userService.findAll();
                model.addAttribute("users", users);
                return "users/list";
            } else {
                return "redirect:/books";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/{id}")
    public String listUserById(HttpSession session, Model model, @PathVariable Long id) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                List<Rental> rentals = rentalService.findAllByStudentId(id);
                model.addAttribute("rentals", rentals);
                Optional<User> userById = userService.findUserById(id);
                String firstName = null;
                if (userById.isPresent())
                    firstName = userById.get().getFirstName();
                model.addAttribute("firstName", firstName);
                return "users/user";
            } else if (id != user.getId()) {
                return "redirect:/books";

            }else {
                Optional<User> userById = userService.findUserById(id);
                String firstName = null;
                if (userById.isPresent())
                    firstName = userById.get().getFirstName();
                model.addAttribute("firstName", firstName);
                List<Rental> rentals = rentalService.findAllByStudentId(user.getId());
                model.addAttribute("rentals", rentals);
                return "users/user";
            }
        }
        return "redirect:/login";

    }

    private static boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

}
