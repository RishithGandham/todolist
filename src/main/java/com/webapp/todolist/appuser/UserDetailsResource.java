package com.webapp.todolist.appuser;

import com.webapp.todolist.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v4/userdetails")
@AllArgsConstructor
public class UserDetailsResource {

    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserDetailsService appUserDetailsService;

    @GetMapping
    public ResponseEntity<FrontendUserDetails> getUserDetails( Authentication auth) {

        AppUserDetails appUserDetails = (AppUserDetails) auth.getPrincipal();
        FrontendUserDetails frontendUserDetails =
                new FrontendUserDetails(appUserDetails.getEmail(), appUserDetails.getFirstName(), appUserDetails.getLastName(), appUserDetails.getId(), appUserDetails.getListOfTaskLists());
        return new ResponseEntity<>(frontendUserDetails, OK);
    }



}

