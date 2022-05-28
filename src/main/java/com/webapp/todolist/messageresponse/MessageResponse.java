package com.webapp.todolist.messageresponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class MessageResponse<E> {

    private String responseMessage;
    private E data;

    public MessageResponse(String message) {
        this.setResponseMessage(message);
    }
}
