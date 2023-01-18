package com.example.bookthebook.controller;

import com.example.bookthebook.model.Rental;
import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.BookStatus;
import com.example.bookthebook.model.enumeration.Role;
import com.example.bookthebook.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;


    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public String listRentals(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                List<Rental> rentals = rentalService.findAll();
                model.addAttribute("rentals", rentals);
                return "rentals/rentals";
            } else{
                return "redirect:/books";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/{id}")
    public String updateRental(HttpSession session, Model model, @PathVariable Long id) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                Optional<Rental> rentalOptional = rentalService.findById(id);
                Rental rental = new Rental();
                if (rentalOptional.isPresent())
                    rental = rentalOptional.get();

                model.addAttribute("rental", rental);
                return "rentals/edit";
            } else {
                return "redirect:/books";
            }
        }
        return "redirect:/login";
    }

    @PostMapping
    public String saveRental(@ModelAttribute Rental rental) {
        rentalService.save(rental);
        return "redirect:/books";
    }

    @PostMapping("/edit")
    public String updateRental(@ModelAttribute Rental rentalUpdate) {
        rentalService.saveOrUpdate(rentalUpdate);
        return "redirect:/books";
    }

    private static boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

}
