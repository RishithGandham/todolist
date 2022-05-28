package com.webapp.todolist.registration.token;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ConfirmResponse {
    private Boolean confirmed;
    private String token;
}
