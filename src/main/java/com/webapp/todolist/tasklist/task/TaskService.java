package com.webapp.todolist.tasklist.task;


import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.TaskListRepository;
import com.webapp.todolist.tasklist.TaskListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;


    public TaskList deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApiRequestException("Could not find that task"));
        taskRepository.delete(task);
        return task.getTaskList();
    }

    public TaskList editTask(String name, String desc, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find that task"));
        task.setId(id);
        task.setDescription(desc);
        task.setName(name);
        taskRepository.save(task);

        return task.getTaskList();
    }

    public TaskList createTask(String name, String desc, Long listId) {

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new ApiRequestException("that list could not be found "));
        Task task = new Task(name, desc, taskList);
        taskRepository.save(task);
        return taskList;
    }
}
