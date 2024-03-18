package com.enigma.shorten_link.service;

import com.enigma.shorten_link.entity.Credential;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface CredentialService extends UserDetailsService {
    Credential findOrFail(String id);
    Credential getByContext();
    Optional<Credential> findByUsername(String username);
    Credential save(Credential credential);
}
