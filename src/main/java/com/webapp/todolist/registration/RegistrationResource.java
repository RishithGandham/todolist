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
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        System.out.println(request);
        ConfirmationToken confirmationToken = registrationService.register(request);
        final String jwt = jwtTokenUtil.generateToken(confirmationToken.getAppUserDetails());
        RegistrationResponse registrationResponse =
                new RegistrationResponse("You were registered, please see your email for a verification link, this link will expire in 15 minutes", jwt);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }


}
