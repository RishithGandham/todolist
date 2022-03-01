package com.webapp.todolist.registration;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.appuser.AppUserDetailsRepository;
import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.appuser.UserRole;
import com.webapp.todolist.email.EmailService;
import com.webapp.todolist.email.EmailService;
import com.webapp.todolist.email.EmailValidator;
import com.webapp.todolist.registration.token.ConfirmationToken;
import com.webapp.todolist.registration.token.ConfirmationTokenRepository;
import com.webapp.todolist.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserDetailsService appUserDetailsService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService sender;
    private final AppUserDetailsRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken register(RegistrationRequest req) {
        EmailValidator emailValidator = new EmailValidator();

        if (!emailValidator.test(req.getEmail()))
            throw new IllegalStateException("NOT VALID EMAIL");

        ConfirmationToken token = appUserDetailsService.signUpUser(new AppUserDetails(req.getFirstName(), req.getLastName(), req.getEmail(),
                req.getPassword(), UserRole.USER));


        String confirmationTokenLink = "http://localhost:8080/api/v1/tokenresource/confirmtoken?token=" + token.getToken();

        sender.send(req.getEmail(), this.buildEmail(req.getFirstName(), confirmationTokenLink));
        System.out.println(token);

        return token;
    }

    @Transactional
    public String confirmToken(String token) throws UserPrincipalNotFoundException {


        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            confirmationToken.setExpiresAt(confirmationToken.getExpiresAt().plusMinutes(15));
            confirmationTokenRepository.save(confirmationToken);
            resend(confirmationToken.getAppUserDetails().getEmail(), confirmationToken.getToken());
            throw new IllegalStateException("token expired");

        }

        confirmationTokenService.setConfirmedAt(token);
        appUserDetailsService.enableAppUser(confirmationToken.getAppUserDetails().getEmail());


        return "home";
    }

    public void resend(String email, String token) throws UserPrincipalNotFoundException {


        AppUserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new UserPrincipalNotFoundException("userName"));


        token = token.replaceAll("\\s", "");

        System.out.println(token);


        sender.send(email, this.buildEmail(user.getFirstName(), "http://localhost:8080/api/v1/tokenresource/confirmtoken?token="  + token));
    }


    private String buildEmail(String name, String link) {

        String html = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\" />" +
                "" +
                "" +
                "    <script src=\"https://code.jquery.com/jquery-3.4.1.slim.min.js\"></script>" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js\"></script>" +
                "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js\"></script>" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>email</title>" +
                "</head>" +
                "" +
                "<body>" +
                "    <section class=\"vh-100\" style=\"background-color: #eee;\">" +
                "        <div class=\"container h-100\">" +
                "            <div class=\"row d-flex justify-content-center align-items-center h-100\">" +
                "                <div class=\"col-lg-12 col-xl-11\">" +
                "                    <div class=\"card text-black\" style=\"border-radius: 25px;\">" +
                "                        <div class=\"card-body p-md-5\">" +
                "                            <div class=\"row justify-content-center\">" +
                "                                <div class=\"col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1\">" +
                "                                    <h1>Please confirm your email using the link below, The link will expire in 15 minutes.</h1>" +
                "<br>" +
                "<br>" +
                "" +
                "" +
                "" +
                "                                    <div class=\"d-flex justify-content-center mx-4 mb-3 mb-lg-4\">" +
                "                                        <a href=\"" + link + "\" role=\"button\" class=\"btn btn-primary btn-lg\">Confirm</a>" +
                "" +
                "                                    </div>" +
                "" +
                "                                </div>" +
                "                            </div>" +
                "                        </div>" +
                "                    </div>" +
                "                </div>" +
                "            </div>" +
                "        </div>" +
                "    </section>" +
                "</body>" +
                "" +
                "</html>";


        return html;
    }

}
