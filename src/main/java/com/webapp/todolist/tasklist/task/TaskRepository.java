package com.webapp.todolist.tasklist.task;

import com.webapp.todolist.tasklist.TaskList;
import com.webapp.todolist.tasklist.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskById(Long id);

    List<Task> findByTaskList(TaskList taskList);

    @Modifying
    @Query("delete from Task t where t.id = ?1")
    void delete(Long entityId);

}
