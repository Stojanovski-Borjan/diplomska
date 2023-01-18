package com.example.bookthebook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotExist extends RuntimeException {
    public BookNotExist(Long id) {
        super(String.format("Book with %d was not found!", id));
    }
}
