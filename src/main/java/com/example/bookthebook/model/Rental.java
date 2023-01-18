package com.example.bookthebook.model;


import com.example.bookthebook.model.enumeration.BookStatus;
import com.example.bookthebook.model.enumeration.Role;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "rental")
@Data
public class Rental {

    @Id //oznachuva deka ova pole e primaren kluch na ovoj entitet vo tabelata
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus = BookStatus.PENDING;
    public Rental() {
    }

    public Rental(Long id, Book book, User user, LocalDate startDate, LocalDate endDate, BookStatus bookStatus) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookStatus = bookStatus;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", book=" + book +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", bookStatus=" + bookStatus +
                '}';
    }
}

