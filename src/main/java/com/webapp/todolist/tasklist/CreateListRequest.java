package com.webapp.todolist.tasklist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateListRequest {
    private String name;
    private String description;
    private String date;
}
