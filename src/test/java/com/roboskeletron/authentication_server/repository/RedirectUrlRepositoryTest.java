package com.roboskeletron.authentication_server.repository;

import com.roboskeletron.authentication_server.domain.RedirectUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RedirectUrlRepositoryTest {
    @Autowired
    RedirectUrlRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findByUrl() {
        String url = "some url";

        RedirectUrl redirectUrl = RedirectUrl.builder().url(url).build();

        repository.save(redirectUrl);

        var actualRedirectUrl = repository.findByUrl(url);

        assertThat(actualRedirectUrl.isPresent()).isTrue();
        assertThat(actualRedirectUrl.get().getUrl()).isEqualTo(url);
    }

    @Test
    void existsByUrl() {
        var val = repository.existsByUrl("some url");
        assertThat(val).isFalse();
    }
}