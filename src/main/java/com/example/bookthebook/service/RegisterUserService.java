package com.example.bookthebook.service;

import com.example.bookthebook.model.User;
import com.example.bookthebook.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public User createUser(User user) {
        User createdUser = userRepository.save(user);
        sendWelcomeEmail(createdUser);
        return createdUser;
    }

    public User signUpUser(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    private void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(String.format("Welcome %s to our site!", user.getFirstName()));
        message.setText("Thank you for creating an account with us. We hope you have a great experience on our site.");
        mailSender.send(message);
    }


}
