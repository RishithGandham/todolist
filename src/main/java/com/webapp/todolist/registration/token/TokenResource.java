package com.webapp.todolist.registration.token;

import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.jwt.JwtTokenUtil;
import com.webapp.todolist.messageresponse.MessageResponse;
import com.webapp.todolist.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.nio.file.attribute.UserPrincipalNotFoundException;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/tokenresource")
public class TokenResource {

    private final RegistrationService registrationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping(path = "/resend")
    public ResponseEntity<MessageResponse> sendEmailAgain(@RequestBody ResendRequest email)
            throws UserPrincipalNotFoundException {
        registrationService.resend(email.getEmail());


        return new ResponseEntity<>(new MessageResponse("email was sent again"), HttpStatus.OK);
    }

    @PostMapping(path = "/confirmtoken")
    public ResponseEntity<MessageResponse> confirmToken(@RequestBody ConfirmRequest confirmRequest)
            throws UserPrincipalNotFoundException {
        try {
            ConfirmationToken confirmationToken = confirmationTokenService.getToken(confirmRequest.getToken());
            final String jwt = jwtTokenUtil.generateToken(confirmationToken.getAppUserDetails());

            Boolean confirmed = registrationService.confirmToken(confirmRequest.getToken());
            return new ResponseEntity<>(new MessageResponse<>("Confirmed", new ConfirmResponse(confirmed, jwt)), HttpStatus.OK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage());
        }




    }


}
