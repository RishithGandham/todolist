package com.webapp.todolist.registration;

import com.webapp.todolist.jwt.JwtTokenUtil;
import com.webapp.todolist.messageresponse.MessageResponse;
import com.webapp.todolist.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/register")
public class RegistrationResource {

    private final RegistrationService registrationService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<?> register( @RequestBody RegistrationRequest request) {

        ConfirmationToken confirmationToken = registrationService.register(request);

        return new ResponseEntity<>(new MessageResponse("You have been registered, please look at your email for a confirmation link"), HttpStatus.CREATED);
    }


}
