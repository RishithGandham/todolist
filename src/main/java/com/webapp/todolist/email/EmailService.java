package com.webapp.todolist.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService  {


    private final JavaMailSender mailSender;


    // sends email

    @Async
    public void send(String to, String email) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("AssignmentChecker -- Confirm Your Email!");
            helper.setFrom("AssignmentChecker@gmail.com");
            mailSender.send(mimeMessage);

        } catch (Exception e) {

        }

    }

}
