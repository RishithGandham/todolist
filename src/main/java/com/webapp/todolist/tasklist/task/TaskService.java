package com.webapp.todolist.tasklist.task;


import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.TaskListRepository;
import com.webapp.todolist.tasklist.TaskListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.delete(taskId);
        return;
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
        Task task = new Task();
        task.setName(name);
        task.setDescription(desc);
        task.setTaskList(taskListRepository.findById(listId)
                .orElseThrow(() -> new ApiRequestException("that list could not be found")));
        taskRepository.save(task);
//        System.out.println(task);
        return taskListRepository.findById(listId).orElseThrow(() -> new ApiRequestException("that list could not be found"));
    }
}
