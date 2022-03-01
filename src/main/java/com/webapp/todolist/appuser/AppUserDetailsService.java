package com.webapp.todolist.appuser;

import com.webapp.todolist.registration.token.ConfirmationToken;
import com.webapp.todolist.registration.token.ConfirmationTokenService;
import com.webapp.todolist.task.TaskListRepository;
import com.webapp.todolist.task.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter

public class AppUserDetailsService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final AppUserDetailsRepository apr;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final TaskListRepository assignmentListRepository;
    private final TaskRepository assignmentRepository;


    // gets user by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        return apr.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));

    }

    // checks if email is taken, if not then encodes pass and creates a confirmation token and saves it in the database
    public ConfirmationToken signUpUser(AppUserDetails appUserDetails) {

        if (apr.findByEmail(appUserDetails.getEmail()).isPresent()) {
            throw new IllegalStateException("email already taken");
        }

        appUserDetails.setPassword(bCryptPasswordEncoder.encode(appUserDetails.getPassword()));
        apr.save(appUserDetails);


        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), appUserDetails);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    //enables the app user
    public int enableAppUser(String email) {
        return apr.enableAppUser(email);
    }

}
