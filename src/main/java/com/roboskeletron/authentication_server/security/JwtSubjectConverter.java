package com.roboskeletron.authentication_server.security;

import lombok.Getter;
import org.springframework.core.convert.converter.Converter;

@Getter
public class JwtSubjectConverter implements Converter<Object, String> {
    private String subject;
    @Override
    public String convert(Object source) {
        subject = source.toString();
        return subject;
    }
}
