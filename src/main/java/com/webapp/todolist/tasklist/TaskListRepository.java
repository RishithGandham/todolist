package com.webapp.todolist.tasklist;

import com.webapp.todolist.appuser.AppUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {


    Optional<TaskList> findTaskListById(Long aLong);

    Optional<TaskList> deleteListById(Long id);

    Optional<List<TaskList>> findByAppUserDetails(AppUserDetails appUserDetails);


    @Transactional
    @Modifying
    @Query("delete from TaskList t where t.id=:id")
    void deleteList(@Param("id") Long id);


}
