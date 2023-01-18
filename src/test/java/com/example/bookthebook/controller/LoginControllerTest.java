package com.example.bookthebook.controller;

import com.example.bookthebook.model.User;
import com.example.bookthebook.service.RegisterUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterUserService registerUserService;

    @Test
    public void getRegistration_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void signUpUser_WithValidData_ShouldRedirectToBooks() throws Exception {
        User logedInUser = new User();
        Mockito.when(registerUserService.signUpUser("test@email.com", "password")).thenReturn(logedInUser);
        mockMvc.perform(post("/login")
                        .param("email", "test@email.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    public void signUpUser_WithInvalidData_ShouldRedirectToLogin() throws Exception {
        Mockito.when(registerUserService.signUpUser("test@email.com", "password")).thenReturn(null);
        mockMvc.perform(post("/login")
                        .param("email", "test@email.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}