package com.webapp.todolist.email;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.function.Predicate;

public class EmailValidator implements Predicate<String> {


    // validate email
    @Override
    public boolean test(String t) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(t);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
            throw new IllegalStateException("not valid email");

        }
        return result;
    }
}
