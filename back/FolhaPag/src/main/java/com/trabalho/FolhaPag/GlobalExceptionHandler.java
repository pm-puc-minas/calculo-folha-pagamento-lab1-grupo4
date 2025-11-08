package com.trabalho.FolhaPag;

import com.trabalho.FolhaPag.Exceptions.ExceptionDetails;
import com.trabalho.FolhaPag.Exceptions.UnexpectedException;
import com.trabalho.FolhaPag.Exceptions.ValueNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<ExceptionDetails> handle(UnexpectedException e){
        return new ResponseEntity<>(ExceptionDetails.builder()
                .date(LocalDate.now()).message(e.getMessage()).issuer("A sad Unexpected Exception!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValueNotValidException.class)
    public ResponseEntity<ExceptionDetails> handle(ValueNotValidException e){
        return new ResponseEntity<>(ExceptionDetails.builder()
                .date(LocalDate.now()).message(e.getMessage()).issuer("Value Not Found Exception").build(), HttpStatus.BAD_REQUEST);
    }
}
