package com.webapp.todolist.bean;


import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;


// BEANS!!
@Configuration
public class Beans {

    @Bean
    public JwtRequestFilter jwtRequestFilter() { return new JwtRequestFilter(); }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SimpleDateFormat simpleDateFormat() { return new SimpleDateFormat("yyyy-MM-dd"); }



}
