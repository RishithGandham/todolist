package com.webapp.todolist.registration.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor

public class ConfirmRequest {
    public ConfirmRequest() {}
    private String token;
}
