package com.webapp.todolist.tasklist.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateTodoRequest {
    private String name;
    private String desc;
    private Long listId;
}
