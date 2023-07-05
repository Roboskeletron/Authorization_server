package com.roboskeletron.authentication_server.security;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordGeneratorTest {


    @Test
    void generate() {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$";
        PasswordGenerator passwordGenerator = new PasswordGenerator(regex);

        String password = passwordGenerator.generate();

        Matcher matcher = Pattern.compile(regex).matcher(password);

        assertThat(matcher.matches()).isTrue();
    }
}