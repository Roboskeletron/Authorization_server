package com.roboskeletron.authentication_server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static String passwordPattern;

    public static boolean isPasswordValid(String password){
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Value("${spring.security.password.regex}")
    private void readConfig(String regex){
        PasswordValidator.passwordPattern = regex;
    }
}
