package com.example.bookthebook.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private Long quantity;

    private Long page;

    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();

    public Book() {
    }

    public Book(Long id, String title, String author, Long quantity, Long page, String description, List<Rental> rentals) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.page = page;
        this.description = description;
        this.rentals = rentals;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", quantity=" + quantity +
                ", page=" + page +
                ", description='" + description + '\'' +
                ", rentals=" + rentals +
                '}';
    }
}
