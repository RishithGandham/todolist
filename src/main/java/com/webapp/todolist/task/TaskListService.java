package com.webapp.todolist.task;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.exceptions.ListNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor

public class TaskListService {

    public final TaskListRepository tlr;
    private final TaskRepository taskRepository;

    public TaskList addList(String name, AppUserDetails appUserDetails, Date date, String description) {
        TaskList newList = new TaskList(name);
        newList.setAppUserDetails(appUserDetails);
        newList.setDueDate(date);
        newList.setDescription(description);
        newList.taskList.add(new Task("Add Your First Task To This List!", "", newList));
        return tlr.save(newList);

    }

    public TaskList editList(String name, AppUserDetails appUserDetails, Date date, String description, Long id) throws ListNotFoundException {
        TaskList taskList = this.findById(id);
        TaskList editedTaskList = new TaskList(name,date,taskRepository.findByTaskList(taskList),id, description,appUserDetails);
        tlr.save(editedTaskList);
        return editedTaskList;
    }


    public void deleteList(Long id) throws ListNotFoundException {

        tlr.deleteList(id);


    }

    public List<TaskList> findByAppUser(AppUserDetails appUserDetails) {
        List<TaskList> list = tlr.findByAppUserDetails(appUserDetails);
        return list;
    }

    public TaskList findById(Long id) throws ListNotFoundException {
        return tlr.getById(id);

    }

    public TaskList deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException(String.format("could not find task with id: %", taskId)));
        taskRepository.delete(task);
        return task.getTaskList();


    }
//	public TaskList findSpecificListByAppUser(AppUser appUser, String name) {
//		List<TaskList> list = alr.findByAppuser(appUser);
//		for (TaskList l : list) {
//			if (l.name.equals(name)) {
//				return l;
//			}
//		}
//		throw new IllegalStateException("list not found");
//	}

}
