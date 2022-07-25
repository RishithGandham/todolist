package com.webapp.todolist.authorization;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.jwt.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v5/isauthenticated")
public class AuthResource {

    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserDetailsService appUserDetailsService;


    // takes jwt and determines if it is valid
    @PostMapping
    public ResponseEntity<Boolean> isAuthenticated( @RequestBody AuthRequest authRequest ) {

        Boolean isValid;

        System.out.println(authRequest.getJwt());

        try {
            AppUserDetails appUserDetails = (AppUserDetails) appUserDetailsService.loadUserByUsername(jwtTokenUtil.extractUsername(authRequest.getJwt()));
            isValid = jwtTokenUtil.validateToken(authRequest.getJwt(), appUserDetails);
            System.out.println(isValid);

            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }

    }
}
