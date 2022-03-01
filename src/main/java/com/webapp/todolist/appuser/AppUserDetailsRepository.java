package com.webapp.todolist.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
// Repo for appuser
public interface AppUserDetailsRepository extends JpaRepository<AppUserDetails, Long> {

    // find user by email
    Optional<AppUserDetails> findByEmail(String email);


    // Updates whether the user in enabled or not
    @Transactional
    @Modifying
    @Query("UPDATE AppUserDetails a " + "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

}
