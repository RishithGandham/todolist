package com.webapp.todolist.tasklist.task;

import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskById(Long id);

    List<Task> findByTaskList(TaskList taskList);


}
