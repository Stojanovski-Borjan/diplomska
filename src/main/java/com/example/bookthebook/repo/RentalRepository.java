package com.example.bookthebook.repo;

import com.example.bookthebook.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findAllByUserId(Long id);

    List<Rental> findAllByBookId(Long id);


}
