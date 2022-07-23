package com.webapp.todolist.tasklist.task;


import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.exceptions.ListNotFoundException;
import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.TaskListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/taskresource/")
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskListService taskListService;
    private final TaskService taskService;

    @PostMapping("/deletetodo")
    public ResponseEntity<TaskList> deleteTask(@RequestBody DeleteTodoRequest deleteTodoRequest, Authentication authentication) {
        Long id = deleteTodoRequest.getId();
        if (!taskRepository.existsById(id)) throw new ApiRequestException("Task was not found");
        if (taskRepository.getById(id).getTaskList().getAppUserDetails().getId() != ((AppUserDetails) authentication.getPrincipal()).getId())
            throw new ApiRequestException("you are not authorized to delete this list");
        taskService.deleteTask(id);
        return new ResponseEntity<>(taskRepository.getById(id).getTaskList(), HttpStatus.OK);
    }


    @PostMapping("/edittodo")
    public ResponseEntity<TaskList> edittodo(@RequestBody EditTodoRequest editTodoRequest, Authentication authentication) {
        if (!taskRepository.existsById(editTodoRequest.getId())) {
            throw new ApiRequestException("Task was not found");
        }
        if (taskRepository.getById(editTodoRequest.getId()).getTaskList().getAppUserDetails().getId() != ((AppUserDetails) authentication.getPrincipal()).getId()) {
            throw new ApiRequestException("you are not authorized to edit this list");
        }
        return new ResponseEntity<>(taskService.editTask(editTodoRequest.getName(), editTodoRequest.getDesc(), editTodoRequest.getId()), HttpStatus.OK);
    }

    @PostMapping("/createtodo")
    public ResponseEntity<TaskList> createTodo(@RequestBody CreateTodoRequest createTodoRequest, Authentication auth) throws ListNotFoundException {
        Boolean authorized = false;
        for (TaskList tasklist: ((AppUserDetails) auth.getPrincipal()).getListOfTaskLists()) {
            if (tasklist.getId() == createTodoRequest.getListId()) {
                authorized = true;
                break;
            }

        }

        if (!authorized) throw new ApiRequestException("You are not authorized to edit this task");
        taskService.createTask(createTodoRequest.getName(), createTodoRequest.getDesc(), createTodoRequest.getListId());

        return new ResponseEntity<>(taskListService.findById(createTodoRequest.getListId()), HttpStatus.OK);
    }

    @PostMapping("/toggletodo/{todoid}/{listid}")
    public ResponseEntity<TaskList> createTodo(@PathVariable("todoid") Long id, @PathVariable("listid") Long listId, Authentication auth) throws ListNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() ->  new ApiRequestException("could not find this task"));
        System.out.println(task);
        Boolean authorized = false;
        for (TaskList tasklist: ((AppUserDetails) auth.getPrincipal()).getListOfTaskLists()) {
            if (tasklist.getId() == listId) {
                authorized = true;
                break;
            }
        }
        if (!authorized) throw new ApiRequestException("You are not authorized to edit this task");

        task.setChecked(!task.getChecked());
        taskRepository.save(task);

        return new ResponseEntity<>(task.getTaskList(), HttpStatus.OK);

    }
}
