package com.webapp.todolist.messageresponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class MessageResponse implements Serializable {
    public String responseMessage;
}
