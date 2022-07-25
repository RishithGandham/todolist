package com.webapp.todolist.registration;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.appuser.AppUserDetailsRepository;
import com.webapp.todolist.appuser.AppUserDetailsService;
import com.webapp.todolist.appuser.UserRole;
import com.webapp.todolist.email.EmailService;
import com.webapp.todolist.email.EmailService;
import com.webapp.todolist.email.EmailValidator;
import com.webapp.todolist.exceptions.ApiException;
import com.webapp.todolist.exceptions.ApiRequestException;
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
            throw new ApiRequestException("Not a valid email");

        ConfirmationToken token = appUserDetailsService.signUpUser(new AppUserDetails(req.getFirstName(), req.getLastName(), req.getEmail(),
                req.getPassword(), UserRole.USER));


        String confirmationTokenLink = "http://localhost:3000/#/confirm/" + token.getToken();

        sender.send(req.getEmail(), this.buildEmail(req.getFirstName(), confirmationTokenLink));
        System.out.println(token);

        return token;
    }

    @Transactional
    public Boolean confirmToken(String token) throws UserPrincipalNotFoundException {


        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);


        if (confirmationToken.getConfirmedAt() != null) {
            throw new ApiRequestException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            userRepository.deleteById(confirmationToken.getAppUserDetails().getId());
            throw new ApiRequestException("token expired, the user was deleted, please try registering again");



        }

        confirmationTokenService.setConfirmedAt(token);
        appUserDetailsService.enableAppUser(confirmationToken.getAppUserDetails().getEmail());


        return true;
    }

    public void resend(String email) throws UserPrincipalNotFoundException {


        AppUserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new ApiRequestException("Username not found"));


        if (user.isEnabled()) throw new ApiRequestException("User is already enabled");


        ConfirmationToken token = confirmationTokenRepository.findByAppUserDetailsId(user.getId())
                .orElseThrow(() -> new ApiRequestException("Error fetching token"));


        sender.send(email, this.buildEmail(user.getFirstName(), "http://localhost:3000/#/confirm/"  + token));
        return;
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
