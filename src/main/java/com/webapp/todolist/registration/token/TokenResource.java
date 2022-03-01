package com.webapp.todolist.registration.token;

import com.webapp.todolist.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.attribute.UserPrincipalNotFoundException;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/tokenresource")
public class TokenResource {

    private final RegistrationService registrationService;

    @GetMapping(path = "/sendemailagain")
    public ResponseEntity<String> sendEmailAgain(@RequestParam("token") String token, @RequestParam("email") String email, Model model)
            throws UserPrincipalNotFoundException {
        registrationService.resend(email, token);


        return new ResponseEntity<String>("email was sent again", HttpStatus.OK);
    }

    @GetMapping(path = "/confirmtoken")
    public void confirmToken(@RequestParam("token") String token)
            throws UserPrincipalNotFoundException {
        registrationService.confirmToken(token);


    }


}
