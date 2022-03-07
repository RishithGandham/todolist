package com.webapp.todolist.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EditListRequest {
    private Long id;
    private String name;
    private String date;
    private String description;
}
