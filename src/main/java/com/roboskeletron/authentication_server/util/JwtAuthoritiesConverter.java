package com.roboskeletron.authentication_server.util;

import com.roboskeletron.authentication_server.domain.User;
import com.roboskeletron.authentication_server.domain.UserAuthority;
import com.roboskeletron.authentication_server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthoritiesConverter  implements Converter<Object, Set<String>> {
    private final UserService userService;
    private final JwtSubjectConverter subjectConverter;
    @Override
    public Set<String> convert(Object source) {
        String subject = subjectConverter.getSubject();

        log.info("attempting to covert user authorities with subject: {}", subject);
        User user = userService.getUser(subject);

        return SetMapper.mapObjectToSet(UserAuthority::getAuthority, String::toString,
                user.getUserAuthorities());
    }
}
