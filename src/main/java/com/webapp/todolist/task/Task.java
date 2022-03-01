package com.webapp.todolist.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Task {


    private String name;


    private String description;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private TaskList taskList;

    public Task(String name, String description, TaskList taskList) {
        super();
        this.name = name;
        this.description = description;

    }


}
