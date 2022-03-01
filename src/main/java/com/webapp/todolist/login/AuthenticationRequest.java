package com.webapp.todolist.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AuthenticationRequest implements Serializable {
    private String email;
    private String password;
}
