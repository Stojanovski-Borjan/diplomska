package com.example.bookthebook.repo;

import com.example.bookthebook.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
