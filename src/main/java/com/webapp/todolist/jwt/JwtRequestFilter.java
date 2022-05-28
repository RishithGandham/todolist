package com.webapp.todolist.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.exceptions.ApiException;
import com.webapp.todolist.exceptions.ApiExceptionHandler;
import com.webapp.todolist.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private ApiExceptionHandler apiExceptionHandler;

    // Filters the jwt on every request, if it is valid then proceed else send 403 or smth
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            filterChain.doFilter(request, response);
        }
        System.out.println("hello from filter");


        try {
            String authorization = request.getHeader("Authorization");
            String token = null;
            String email = null;


            if (authorization != null & authorization.startsWith("Bearer " )) {
                token = authorization.substring(7);
                email = jwtTokenUtil.extractUsername(token);

            }

            if (email != null & SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


                }
            }
        } catch  (Exception e){

            ApiException exception = apiExceptionHandler.handleApiRequestException(new ApiRequestException("Token invalid or session expired", HttpStatus.valueOf(403))).getBody();
            response.setStatus(exception.getHttpStatus().value());
            response.setContentType("application/json");


            //convert to json
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            PrintWriter out = response.getWriter();
            out.print(mapper.writeValueAsString(exception));
            out.flush();

            System.out.println("hello from error");


            return;

        }
        filterChain.doFilter(request, response);


    }

    // what should not be filtered (api's that require no authentication)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {

        Collection<String> excludeUrlPatterns = new ArrayList<>();
        excludeUrlPatterns.add("/api/v1/register/**");
        excludeUrlPatterns.add("/api/v3/authresource/**");
        excludeUrlPatterns.add("/api/v3/authresource/authenticate");
        excludeUrlPatterns.add("/api/v1/tokenresource/**");
        excludeUrlPatterns.add("/api/v5/isauthenticated");
//        excludeUrlPatterns.add("/api/v2/listresource/**/");
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String path = request.getServletPath();
        if (path.endsWith("/")) {
            path = path.substring(0, request.getServletPath().length());
        }
        System.out.println(path);
        return excludeUrlPatterns.stream()
                .anyMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }


}
