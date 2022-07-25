package com.webapp.todolist.registration;

import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class RegistrationRequest implements Serializable {

    private String firstName;
    private String lastName;
    private String password;
    private String email;


}
