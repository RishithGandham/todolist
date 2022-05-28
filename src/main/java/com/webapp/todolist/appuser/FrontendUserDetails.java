package com.webapp.todolist.appuser;


import com.webapp.todolist.tasklist.TaskList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;


// sends this object to the frontend to have the user details.
@AllArgsConstructor
@Getter
@Setter
public class FrontendUserDetails implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private Long id;
    private List<TaskList> lists;

}
