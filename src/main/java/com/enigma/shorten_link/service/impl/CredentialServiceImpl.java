package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.entity.Credential;
import com.enigma.shorten_link.repository.CredentialRepository;
import com.enigma.shorten_link.service.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository repo;
    @Override
    @Transactional(readOnly = true)
    public Credential findOrFail(String id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND)
        );
    }

    @Override
    public Credential getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        return findOrFail(principal);
    }

    @Override
    public Optional<Credential> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public Credential save(Credential credential) {
        return repo.saveAndFlush(credential);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_USER_NOT_FOUND)
        );
    }
}
