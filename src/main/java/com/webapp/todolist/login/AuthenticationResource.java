package com.webapp.todolist.login;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.jwt.JwtTokenUtil;
import com.webapp.todolist.messageresponse.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/authresource")
@AllArgsConstructor
public class AuthenticationResource {

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            final UserDetails appUserDetails = appUserDetailsService
                    .loadUserByUsername(authenticationRequest.getEmail());
            final String jwt = jwtTokenUtil.generateToken(appUserDetails);
            return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return new ResponseEntity<MessageResponse>(new MessageResponse("Problem authenticating"), HttpStatus.FORBIDDEN);
        }
    }

}
