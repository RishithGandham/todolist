package com.webapp.todolist.registration;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegistrationResponse {
    private String message;
    private String jwt;
}
