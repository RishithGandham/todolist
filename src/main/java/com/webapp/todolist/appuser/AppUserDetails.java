package com.webapp.todolist.appuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webapp.todolist.tasklist.TaskList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class AppUserDetails implements UserDetails {

    /**
     *
     */

    //User  attr.
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole apr;
    private Boolean locked = false;
    private Boolean enabled = false;


    // List of task using foreign key in sql
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "appUserDetails", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private List<TaskList> listOfTaskLists = new ArrayList<TaskList>();


    public AppUserDetails(String firstName, String lastName, String email, String password, UserRole apr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.apr = apr;


    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(apr.name()));

    }


    // Returns email instead of username since login with email
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }


    public List<TaskList> getAssignmentList() {

        return this.listOfTaskLists;
    }


}
