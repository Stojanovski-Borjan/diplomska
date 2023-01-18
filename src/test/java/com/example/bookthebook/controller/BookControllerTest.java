package com.example.bookthebook.controller;

import com.example.bookthebook.model.Book;
import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.Role;
import com.example.bookthebook.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void listBooks_WhenUserIsLoggedIn_ShouldReturnListBooksView() throws Exception {
        User user = new User();
        user.setRole(Role.STUDENT);
        List<Book> books = Collections.singletonList(new Book());

        Mockito.when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/books").sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("books/books"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("rental"));
    }

    @Test
    public void listBooks_WhenUserIsNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void addNewBook_WhenUserIsAdmin_ShouldReturnAddBookView() throws Exception {
        User user = new User();
        user.setRole(Role.ADMIN);

        mockMvc.perform(get("/books/add-new").sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("books/add-book"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    public void addNewBook_WhenUserIsNotAdmin_ShouldRedirectToBooks() throws Exception {
        User user = new User();
        user.setRole(Role.STUDENT);

        mockMvc.perform(get("/books/add-new").sessionAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    public void addNewBook_WhenUserIsNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/add-new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void editBook_WhenUserIsAdmin_ShouldReturnEditBookView() throws Exception {
        User user = new User();
        user.setRole(Role.ADMIN);
        Book book = new Book();
        book.setId(1L);

        Mockito.when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1/edit").
                        sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("books/add-book"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    public void editBook_WhenUserIsNotAdmin_ShouldRedirectToBooks() throws Exception {
        User user = new User();
        user.setRole(Role.STUDENT);

        mockMvc.perform(get("/books/1/edit").sessionAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    public void editBook_WhenUserIsNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void editBook_WhenBookNotFound_ShouldRedirectToBooksWithError() throws Exception {
        User user = new User();
        user.setRole(Role.ADMIN);

        Mockito.when(bookService.findById(1L)).thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(get("/books/1/edit").sessionAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books?error=Book not found"));
    }

    @Test
    public void saveBook_ShouldSaveBookAndRedirectToBooks() throws Exception {
        User user = new User();
        user.setRole(Role.ADMIN);
        Book book = new Book();

        mockMvc.perform(post("/books").sessionAttr("user", user).flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        Mockito.verify(bookService, Mockito.times(1)).save(book);
    }

    @Test
    public void deleteBook_ShouldDeleteBookAndRedirectToBooks() throws Exception {
        User user = new User();
        user.setRole(Role.ADMIN);

        mockMvc.perform(post("/books/1").sessionAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        Mockito.verify(bookService, Mockito.times(1)).deleteById(eq(1L));
    }
}