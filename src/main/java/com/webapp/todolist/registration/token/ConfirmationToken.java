package com.webapp.todolist.registration.token;

import com.webapp.todolist.appuser.AppUserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            sequenceName = "student_sequence",
            name = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_details_id"
    )
    private AppUserDetails appUserDetails;

    public ConfirmationToken(String token, LocalDateTime localDateTime, LocalDateTime expiredAt, AppUserDetails appUserDetails) {
        super();
        this.token = token;
        this.localDateTime = localDateTime;
        this.expiresAt = expiredAt;
        this.appUserDetails = appUserDetails;

    }

    @Override
    public String toString() {
        return this.getToken();
    }
}
