package com.example.bookthebook.service;

import com.example.bookthebook.model.Rental;
import com.example.bookthebook.model.User;
import com.example.bookthebook.model.enumeration.BookStatus;
import com.example.bookthebook.repo.RentalRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    private final JavaMailSender mailSender;

    public RentalService(RentalRepository rentalRepository, JavaMailSender mailSender) {
        this.rentalRepository = rentalRepository;
        this.mailSender = mailSender;
    }


    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public List<Rental> findAllByStudentId(Long id) {
        return rentalRepository.findAllByUserId(id);
    }

    public List<Rental> findAllByBookId(Long id) {
        return rentalRepository.findAllByBookId(id);
    }

    public void saveOrUpdate(Rental rentalUpdate) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalUpdate.getId());
        Rental rental = new Rental();
        if (optionalRental.isPresent()) {
            rental = optionalRental.get();
            rental.setBookStatus(rentalUpdate.getBookStatus());
            if (rentalUpdate.getBookStatus() == BookStatus.ACCEPTED) {
                rental.setStartDate(LocalDate.now().plusDays(1L));
                rental.setEndDate(LocalDate.now().plusDays(15L));

                sendMailForAcceptedBook(rental);
            }
        }

        rentalRepository.save(rental);
    }

    public void save(Rental rental) {
        rentalRepository.save(rental);
    }

    public void deleteById(Long id) {
        rentalRepository.deleteById(id);
    }

    public Optional<Rental> findById(Long id) {
        return rentalRepository.findById(id);
    }

    private void sendMailForAcceptedBook(Rental rental) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rental.getUser().getEmail());
        message.setSubject(String.format("Book Rental Confirmation - %s!", rental.getBook().getTitle()));
        message.setText(String.format("Dear %s,\n" +
                "\n" +
                "Thank you for your interest in renting the book %s. We are pleased to confirm that we are able to accommodate your request.\n" +
                "\n" +
                "You are welcome to take the book from %s to %s.",
                rental.getUser().getFirstName(), rental.getBook().getTitle(), rental.getStartDate(), rental.getEndDate()));
        mailSender.send(message);
    }
}
