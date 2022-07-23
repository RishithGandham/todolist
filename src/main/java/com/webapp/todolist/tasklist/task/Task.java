package com.webapp.todolist.tasklist.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.todolist.tasklist.TaskList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Task {


    private String name;


    private String description;

    private Boolean checked = false;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    @JsonIgnore
    private TaskList taskList;

    public Task(String name, String description, TaskList taskList) {
        super();
        this.name = name;
        this.description = description;

    }


}
