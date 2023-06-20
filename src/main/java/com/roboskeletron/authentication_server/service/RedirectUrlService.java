package com.roboskeletron.authentication_server.service;

import com.roboskeletron.authentication_server.domain.RedirectUrl;
import com.roboskeletron.authentication_server.repository.RedirectUrlRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedirectUrlService {
    private final RedirectUrlRepository redirectUrlRepository;

    public void createRedirectUrl(RedirectUrl url){
        if (redirectUrlRepository.existsByUrl(url.getUrl()))
            throw new EntityExistsException("url " + url.getUrl() + " has been taken");

        redirectUrlRepository.save(url);
    }

    public RedirectUrl updateRedirectUrl(RedirectUrl url){
        if (!redirectUrlRepository.existsById(url.getId()))
            throw new EntityNotFoundException("url not found");

        return redirectUrlRepository.save(url);
    }

    public void deleteRedirectUrl(RedirectUrl url){
        if (!redirectUrlRepository.existsById(url.getId()))
            throw new EntityNotFoundException("url not found");

        redirectUrlRepository.delete(url);
    }

    public void deleteRedirectUrl(int id){
        if (!redirectUrlRepository.existsById(id))
            throw new EntityNotFoundException("url not found");

        redirectUrlRepository.deleteById(id);
    }

    public RedirectUrl getRedirectUrl(int id){
        return redirectUrlRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public RedirectUrl getRedirectUrl(String url){
        return redirectUrlRepository.findByUrl(url).orElseThrow(EntityNotFoundException::new);
    }
}
