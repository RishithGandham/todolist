package com.webapp.todolist.tasklist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.tasklist.task.Task;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskList {

    public String name;
    public Date dueDate;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "taskList", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    public List<Task> taskList = new ArrayList<Task>();
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "app_user_details_id")
    @JsonIgnore private AppUserDetails appUserDetails;


    public TaskList(String name) {
        super();
        this.name = name;
    }


}
