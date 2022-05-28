package com.webapp.todolist.tasklist.task;


import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.TaskListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v2/taskresource/")
@AllArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskListService taskListService;
    private final TaskService taskService;

    @PostMapping("/deletetodo")
    public ResponseEntity<TaskList> deleteTask(@RequestParam("id") Long id, Authentication authentication) {
        if (!taskRepository.existsById(id)) throw new ApiRequestException("Task was not found");
        if (taskRepository.getById(id).getTaskList().getAppUserDetails().getId() != ((AppUserDetails) authentication.getPrincipal()).getId())
            throw new ApiRequestException("you are not authorized to delete this list");
        TaskList taskList = taskService.deleteTask(id);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    @PostMapping("/edittodo")
    public ResponseEntity<TaskList> edittodo(@RequestBody EditTodoRequest editTodoRequest, Authentication authentication) {
        if (!taskRepository.existsById(editTodoRequest.getId())) {
            throw new ApiRequestException("Task was not found");
        }
        if (taskRepository.getById(editTodoRequest.getId()).getTaskList().getAppUserDetails().getId() != ((AppUserDetails) authentication.getPrincipal()).getId()) {
            throw new ApiRequestException("you are not authorized to delete this list");
        }

        TaskList newList = taskService.editTask(editTodoRequest.getName(), editTodoRequest.getDesc(), editTodoRequest.getId());

        return new ResponseEntity<>(newList, HttpStatus.OK);

    }

    @PostMapping("/createtodo")
    public ResponseEntity<TaskList> createTodo(@RequestBody CreateTodoRequest createTodoRequest, Authentication auth) {
        for (TaskList tasklist: ((AppUserDetails) auth.getPrincipal()).getListOfTaskLists()) {
            if (tasklist.getId() == createTodoRequest.getListId()) break;
        }
        return new ResponseEntity<>(taskService.createTask(createTodoRequest.getName(), createTodoRequest.getDesc(), createTodoRequest.getListId()), HttpStatus.OK);
    }
}
