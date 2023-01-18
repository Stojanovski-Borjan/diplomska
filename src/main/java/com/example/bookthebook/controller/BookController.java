package com.example.bookthebook.controller;

import com.example.bookthebook.model.Book;
import com.example.bookthebook.model.Rental;
import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.Role;
import com.example.bookthebook.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
                List<Book> books = bookService.findAll();
                model.addAttribute("books", books);
                model.addAttribute("rental", new Rental());

            return "books/books";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/add-new")
    public String addNewBook(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                model.addAttribute("book", new Book());
                return "books/add-book";
            } else  {
                return "redirect:/books";
            }
        }

        return "redirect:/login";
    }

    private static boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    @GetMapping("/{id}/edit")
    public String editBook(HttpSession session,Model model, @PathVariable Long id) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (isAdmin(user)) {
                try {
                    Book book = bookService.findById(id);

                    model.addAttribute("book", book);
                    return "books/add-book";
                } catch (RuntimeException ex) {
                    return "redirect:/books?error=" + ex.getMessage();
                }
            } else {
                return "redirect:/books";
            }
        }
        return "redirect:/login";
    }

    @PostMapping
    public String saveBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
