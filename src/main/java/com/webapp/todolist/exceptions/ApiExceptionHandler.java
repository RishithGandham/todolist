package com.webapp.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.ZonedDateTime;
//@EnableWebMvc
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException apiRequestException) {
//        System.out.println("hello from error");
        HttpStatus s;
        if (apiRequestException.httpStatus != null) s = apiRequestException.httpStatus;
        else s = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                apiRequestException.getMessage(),
                s,
                ZonedDateTime.now()
        );


        System.out.println(apiException);

        return new ResponseEntity<>(apiException, s);


    }

}
