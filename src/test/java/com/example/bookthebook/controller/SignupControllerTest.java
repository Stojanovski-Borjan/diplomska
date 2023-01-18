package com.example.bookthebook.controller;

import com.example.bookthebook.model.User;
import com.example.bookthebook.service.RegisterUserService;
import com.example.bookthebook.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SignupController.class)
public class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterUserService registerUserService;

    @MockBean
    private UserService userService;

    @Test
    public void getRegistration_ShouldReturnSignupView() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void createNewUser_WithBlankEmailOrPassword_ShouldRedirectToSignup() throws Exception {
        User user = new User();
        user.setEmail("");
        user.setPassword("password");
        mockMvc.perform(post("/signup")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"));
    }

    @Test
    public void createNewUser_WithExistEmail_ShouldRedirectToSignup() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        Mockito.when(userService.findUserByEmail("test@email.com")).thenReturn(true);
        mockMvc.perform(post("/signup")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"));
    }

    @Test
    public void createNewUser_WithValidData_ShouldRedirectToBooks() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        Mockito.when(userService.findUserByEmail("test@email.com")).thenReturn(false);
        mockMvc.perform(post(
                        "/signup")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(registerUserService, Mockito.times(1)).createUser(user);
    }
}